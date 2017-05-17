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

        generateCode(clazz);
        return className + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        try {
            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(className);
            generateCode(clazz);
            return className + "Impl";
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e);
        }
    }

    private void generateCode(Class<?> clazz) throws ImplementorException {
        File outputFile = getOutputFile(clazz.getCanonicalName());
        PrintWriter writer;
        try {
            writer = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e);
        }

        writer.println("package " + clazz.getPackage().getName() + ";");
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

        for (Method m : allMethods) {
            int mods = m.getModifiers();
            if (!Modifier.isAbstract(mods)) {
                continue;
            }

            writer.print("public " + m.getReturnType().getCanonicalName() + " " + m.getName());
            writer.print("(");
            for (int i = 0; i < m.getParameterCount(); i++) {
                if (i != 0) {
                    writer.print(", ");
                }
                writer.print(m.getParameterTypes()[i].getCanonicalName() + " a" + i);
            }
            writer.println(") {");
            writer.println("throw new UnsupportedOperationException();");
            writer.println("}");
        }

        writer.println("}");
        writer.close();
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

    public static void main(String[] args) throws ImplementorException {
        new SimpleImplementor("/home/anton/tmp/gen").implementFromStandardLibrary("ru.spbau.mit.SomeInterface");
    }
}
