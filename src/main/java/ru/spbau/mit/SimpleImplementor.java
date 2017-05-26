package ru.spbau.mit;


import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;


public class SimpleImplementor implements Implementor {

    private String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        try {
            File file = new File(directoryPath);
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader loader = new URLClassLoader(urls);
            Class cls = loader.loadClass(className);
            if (Modifier.isFinal(cls.getModifiers())) {
                throw new ImplementorException("cannot extend final class");
            }
            Package pkg = cls.getPackage();
            implement(cls, pkg);
            return generateImplementedName(cls, pkg);
        } catch (ClassNotFoundException ex) {
            throw new ImplementorException("class cannot be loaded", ex);
        } catch (MalformedURLException e) {
            throw new ImplementorException("error while creating url", e);
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class cls;
        try {
            cls = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("no such class", e);
        }

        implement(cls, null);

        return generateImplementedName(cls, null);
    }

    private void implement(Class cls, Package pkg) throws ImplementorException {
        try {
            String outputFilePath =
                    outputDirectory + "/"
                            + (pkg != null ? nameToDir(cls.getCanonicalName()) : cls.getSimpleName())
                            + "Impl.java";
            File outputFile = new File(outputFilePath);
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outputFile)));
            generateClass(pw, cls, pkg);
            pw.close();

        } catch (IOException e) {
            throw new ImplementorException("cannot save generated class");
        }
    }

    private String generateImplementedName(Class cls, Package pkg) {
        String classImplName = (pkg != null ? cls.getCanonicalName() : cls.getSimpleName()) + "Impl";
        return classImplName;
    }

    private String nameToDir(String fullName) {
        return fullName.replace('.', '/');
    }

    private void generateClass(PrintWriter pw, Class clazz, Package pkg) {

        if (pkg != null) {
            pw.println(String.format("package %s;", clazz.getPackage().getName()));
        }
        pw.println(String.format("public class %s %s %s {",
                clazz.getSimpleName() + "Impl",
                clazz.isInterface() ? "implements" : "extends",
                clazz.getCanonicalName()));

        Set<Method> methods = new HashSet<>();
        extractMethods(clazz, methods);

        Set<Method> resultMethods = new HashSet<>();
        Set<Integer> seenHashes = new HashSet<>();
        for (Method m : methods) {
            int methodModifiers = m.getModifiers();
            if (!Modifier.isAbstract(methodModifiers)) {
                continue;
            }
            Integer curHash = Arrays.hashCode(m.getParameterTypes()) ^ m.getName().hashCode();
            if (seenHashes.contains(curHash)) {
                continue;
            }
            seenHashes.add(curHash);
            resultMethods.add(m);
        }

        VarNameGenerator gen = new VarNameGenerator();
        resultMethods.forEach(m -> generateMethod(pw, m, gen));

        pw.println("}");

    }

    private void extractMethods(Class cls, Set<Method> methods) {

        if (cls == null) {
            return;
        }

        methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
        extractMethods(cls.getSuperclass(), methods);

        for (Class interf : cls.getInterfaces()) {
            extractMethods(interf, methods);
        }
    }

    private void generateMethod(PrintWriter pw, Method method, VarNameGenerator gen) {
        String mods = getMethodModifiersString(method);
        String returnType = method.getReturnType().getCanonicalName();
        String name = method.getName();
        String parametersString = Arrays
                .stream(method.getParameterTypes())
                .map(cls -> cls.getCanonicalName() + " " + gen.getFreshVarName())
                .collect(Collectors.joining(","));

        pw.println(String.format("%s %s %s(%s) {", mods, returnType, name, parametersString));
        pw.println("throw new UnsupportedOperationException();");
        pw.println("}");
    }

    private String getMethodModifiersString(Method method) {
        int mod = method.getModifiers();
        StringBuilder sb = new StringBuilder();
        if (Modifier.isPublic(mod)) {
            sb.append("public ");
        } else if (Modifier.isProtected(mod)) {
            sb.append("protected ");
        } else if (Modifier.isPrivate(mod)) {
            sb.append("private ");
        }

        if (Modifier.isStatic(mod)) {
            sb.append("static ");
        }
        if (Modifier.isFinal(mod)) {
            sb.append("final ");
        }

        return sb.toString();
    }

    private static class VarNameGenerator {
        private int varNameInd = 0;
        public String getFreshVarName() {
            return "var" + varNameInd++;
        }
    }
}
