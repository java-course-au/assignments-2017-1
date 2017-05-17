package ru.spbau.mit;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleImplementor implements Implementor {
    private String outputDir;

    public SimpleImplementor(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        URL url;
        try {
            url = new File(directoryPath).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new ImplementorException(e.getMessage(), e);
        }
        ClassLoader loader = new URLClassLoader(new URL[] {url});
        Class<?> clazz;
        try {
            clazz = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e.getCause());
        }

        generateCode(clazz, clazz.getPackage());
        return className + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        try {
            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
            generateCode(clazz, null);
            return className + "Impl";
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e);
        }
    }

    private void generateCode(Class<?> clazz, Package pack) throws ImplementorException {
        try {
            if (Modifier.isFinal(clazz.getModifiers())) {
                throw new ImplementorException("Can't extend final class");
            }
            File outputFile = getOutputFile(clazz.getCanonicalName());
            PrintWriter writer;
            try {
                writer = new PrintWriter(outputFile);
            } catch (FileNotFoundException e) {
                throw new ImplementorException(e.getMessage(), e);
            }

            if (pack != null) {
                writer.println("package " + clazz.getPackage().getName() + ";");
            }
            writer.print("public class " + clazz.getSimpleName() + "Impl ");
            if (clazz.isInterface()) {
                writer.print("implements ");
            } else {
                writer.print("extends ");
            }
            writer.println(clazz.getCanonicalName() + " {");

            Set<Method> allMethods = new HashSet<>();
            Class<?> cur = clazz;
            while (cur != null) {
                allMethods.addAll(Arrays.asList(cur.getDeclaredMethods()));
                allMethods.addAll(Arrays.asList(cur.getMethods()));
                cur = cur.getSuperclass();
            }

            Set<String> printedMethods = new HashSet<>();

            for (Method m : allMethods) {
                int mods = m.getModifiers();
                if (!Modifier.isAbstract(mods)) {
                    continue;
                }

                StringBuilder declaration = new StringBuilder();

                declaration.append("public " + m.getReturnType().getCanonicalName() + " " + m.getName());
                declaration.append("(");
                for (int i = 0; i < m.getParameterCount(); i++) {
                    if (i != 0) {
                        writer.print(", ");
                    }
                    writer.print(m.getParameterTypes()[i].getCanonicalName() + " a" + i);
                }
                declaration.append(")");
                if (printedMethods.contains(declaration.toString())) {
                    continue;
                }
                printedMethods.add(declaration.toString());
                writer.println(declaration.toString() + " {");
                writer.println("throw new UnsupportedOperationException();");
                writer.println("}");
                writer.println();
            }

            writer.println("}");
            writer.close();
        } catch (Exception e) {
            throw new ImplementorException(e.getMessage(), e);
        }
    }

    private File getOutputFile(String className) {
        String[] nameParts = className.split("\\.");
        File file = new File(outputDir);
        for (int i = 0; i + 1 < nameParts.length; i++) {
            file = new File(file, nameParts[i]);
        }
        file.mkdirs();
        return new File(file, nameParts[nameParts.length - 1] + "Impl.java");
    }
}
