package ru.spbau.mit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;

    SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        URL directoryURL;
        Class<?> clazz;
        try {
            directoryURL = new URL("file://" + directoryPath);
            ClassLoader classLoader = new URLClassLoader(new URL[]{directoryURL});
            clazz = classLoader.loadClass(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new ImplementorException("Cannot load class " + className
                    + " no such file or directory", e);
        }

        return implement(clazz, clazz.getPackage().getName());
    }

    private String implement(Class<?> clazz, String destinationPackage) throws ImplementorException {
        String derivedClassName = clazz.getSimpleName() + "Impl";
        StringBuilder classBuilder = new StringBuilder();

        if (!destinationPackage.equals("")) {
            classBuilder.append("package ")
                    .append(destinationPackage)
                    .append(";\n");
        }
        classBuilder.append("public class ").append(derivedClassName);

        if (clazz.isInterface()) {
            classBuilder.append(" implements ");
        } else if (!Modifier.isFinal(clazz.getModifiers())) {
            classBuilder.append("extends ");
        } else {
            throw new ImplementorException("Cannot inherit class "
                    + clazz.getCanonicalName());
        }

        classBuilder.append(clazz.getCanonicalName());
        classBuilder.append(" {\n");

        for (Method method : clazz.getDeclaredMethods()) {
            classBuilder.append(createDefaultImpl(method)).append('\n');
        }

        classBuilder.append("}\n");

        String packages = destinationPackage.replace('.', File.separatorChar);
        try {
            String path = outputDirectory + File.separatorChar
                    + packages + File.separatorChar;

            Files.createDirectories(Paths.get(path));
            PrintWriter writer = new PrintWriter(path + derivedClassName + ".java");
            writer.write(classBuilder.toString());
            writer.close();
        } catch (IOException e) {
            throw new ImplementorException("Cannot create output file", e);
        }

        if (destinationPackage.equals("")) {
            return derivedClassName;
        }

        return destinationPackage + "." + derivedClassName;
    }

    private static String createDefaultImpl(Method method) {
        StringBuilder implBuilder = new StringBuilder();

        if (Modifier.isPublic(method.getModifiers())) {
            implBuilder.append("public ");
        } else if (Modifier.isProtected(method.getModifiers())) {
            implBuilder.append("protected ");
        }

        implBuilder.append(method.getReturnType().getCanonicalName()).append(' ');
        implBuilder.append(method.getName());

        String parameters = Arrays.stream(method.getParameters())
                .map(parameter -> parameter.getType().getCanonicalName()
                    + " " + parameter.getName())
                .collect(Collectors.joining(
                        ", ", "(", ")"));

        implBuilder.append(parameters).append(" {\n");
        implBuilder.append("return ");
        implBuilder.append(getDefaultValueByType(method.getReturnType()));
        implBuilder.append(";\n}");

        return implBuilder.toString();
    }

    private static String getDefaultValueByType(Class<?> clazz) {
        if (clazz == boolean.class) {
            return "false";
        }

        if (clazz == byte.class || clazz == short.class
            || clazz == int.class || clazz == long.class) {
            return "0";
        }

        if (clazz == float.class || clazz == double.class) {
            return "0.";
        }

        return "null";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("Cannot load class " + className
                    + " no such file or directory", e);
        }

        return implement(clazz, "");
    }
}
