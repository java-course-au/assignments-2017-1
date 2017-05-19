package ru.spbau.mit;


import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by boris on 17.05.17.
 */
public class SimpleImplementor implements Implementor {

    private String outputDirectory;
    private int varNameInd = 0;

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
            Package pkg = cls.getPackage();
            return implement(cls, pkg);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try {
            Class cls = loader.loadClass(className);
            return implement(cls, null);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("no such class", e);
        }
    }

    private String implement(Class cls, Package pkg) {

        try {
            String classImplName = (pkg != null ? cls.getCanonicalName() : cls.getSimpleName()) + "Impl";
            File outputFile = new File(outputDirectory + "/" + (pkg != null ? nameToDir(cls.getCanonicalName()) : cls.getName()) + "Impl.java");
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outputFile)));
            generateCode(pw, cls, pkg);
            pw.close();

            BufferedReader reader = new BufferedReader(new FileReader(outputFile));
            reader.lines().forEach(System.out::println);

            return classImplName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private String nameToDir(String fullName) {
        return fullName.replace('.', '/');
    }

    private void generateCode(PrintWriter pw, Class clazz, Package pkg) {
        if (pkg != null) {
            pw.println(String.format("package %s;", clazz.getPackage().getName()));
        }
        pw.println(String.format("public class %s %s %s {",
                clazz.getSimpleName() + "Impl",
                clazz.isInterface() ? "implements" : "extends",
                clazz.getCanonicalName()));

        Arrays.stream(clazz.getMethods())
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .forEach(m -> printMethod(pw, m));

        pw.println("}");

    }

    private void printMethod(PrintWriter pw, Method method) {

        String mods = getMethodModifiersString(method);
        String returnType = method.getReturnType().getCanonicalName();
        String name = method.getName();
        String parametersString = Arrays
                .stream(method.getParameterTypes())
                .map(cls -> cls.getCanonicalName() + " " + getFreshVarName())
                .collect(Collectors.joining(","));

        pw.println(String.format("%s %s %s(%s) {", mods, returnType, name, parametersString));
        pw.println("throw new UnsupportedOperationException();");
        pw.println("}");
    }

    private String getFreshVarName() {
        return "var" + varNameInd++;
    }

    private String getMethodModifiersString(Method method) {
        int mod = method.getModifiers();
        StringBuilder sb = new StringBuilder();
        if (Modifier.isPublic(mod)) {
            sb.append("public ");
        } else if (Modifier.isProtected(mod)) {
            sb.append("protected ");
        } else {
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

}
