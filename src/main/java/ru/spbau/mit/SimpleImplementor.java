package ru.spbau.mit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleImplementor implements Implementor {
    @NotNull
    private final String outputDirectory;

    public SimpleImplementor(@NotNull final String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @NotNull
    @Override
    public String implementFromDirectory(
            @NotNull final String directoryPath,
            @NotNull final String className
    ) throws ImplementorException {
        final Class<?> clazz = loadClassFromDirectory(directoryPath, className);
        final Package classPackage = clazz.getPackage();
        final StringBuffer buffer = new StringBuffer();
        appendPackage(buffer, classPackage);
        appendClass(buffer, clazz);
        writeToFile(buffer, classPackage != null ? classPackage.getName() : "", clazz.getSimpleName());
        return clazz.getCanonicalName() + "Impl";
    }

    @NotNull
    @Override
    public String implementFromStandardLibrary(@NotNull final String className)
            throws ImplementorException {
        final Class<?> clazz = loadClassFromStandardLibrary(className);
        final StringBuffer buffer = new StringBuffer();
        appendClass(buffer, clazz);
        writeToFile(buffer, "", clazz.getSimpleName());
        return clazz.getSimpleName() + "Impl";
    }

    @NotNull
    private Class<?> loadClassFromDirectory(
            @NotNull final String directoryPath,
            @NotNull final String className
    ) throws ImplementorException {
        final File sourceDirectory = new File(directoryPath);
        try {
            final URL[] urls = new URL[]{sourceDirectory.toURI().toURL()};
            final URLClassLoader classLoader = new URLClassLoader(urls);
            return classLoader.loadClass(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new ImplementorException("Class " + className + " cannot be loaded", e);
        }
    }

    @NotNull
    private Class<?> loadClassFromStandardLibrary(@NotNull final String className)
            throws ImplementorException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("Class " + className + " cannot be loaded", e);
        }
    }

    private void appendClass(
            @NotNull final StringBuffer buffer,
            @NotNull final Class<?> clazz
    ) throws ImplementorException {
        appendClassModifiers(buffer, clazz.getModifiers());
        appendClassName(buffer, clazz.getSimpleName());
        appendInterfaceOrSuperclass(buffer, clazz);
        buffer.append(" {");
        appendAbstractMethods(buffer, clazz);
        buffer.append("}");
    }

    private void appendPackage(
            @NotNull final StringBuffer buffer,
            @Nullable final Package classPackage
    ) {
        if (classPackage != null && !classPackage.getName().isEmpty()) {
            buffer.append(classPackage).append(";\n\n");
        }
    }

    private void appendClassModifiers(
            @NotNull final StringBuffer buffer,
            final int modifiers
    ) {
        final int newModifiers = modifiers & ~Modifier.ABSTRACT & ~Modifier.INTERFACE;
        buffer.append(Modifier.toString(newModifiers)).append(" class");
    }

    private void appendClassName(
            @NotNull final StringBuffer buffer,
            @NotNull final String className
    ) {
        buffer.append(" ").append(className).append("Impl");
    }

    private void appendInterfaceOrSuperclass(
            @NotNull final StringBuffer buffer,
            @NotNull final Class<?> clazz
    ) throws ImplementorException {
        if (clazz.isInterface()) {
            buffer.append(" implements ").append(clazz.getCanonicalName());
        } else if (Modifier.isAbstract(clazz.getModifiers())) {
            buffer.append(" extends ").append(clazz.getCanonicalName());
        } else {
            throw new ImplementorException("This is not a valid class");
        }
    }

    private void appendAbstractMethods(
            @NotNull final StringBuffer buffer,
            @NotNull final Class<?> clazz
    ) {
        final Map<String, Method> methods = new HashMap<>();
        fillMethodsTable(methods, clazz);
        methods.values().stream()
                .filter(method -> Modifier.isAbstract(method.getModifiers()))
                .forEach(method -> appendMethod(buffer, method));
    }

    private void fillMethodsTable(
            @NotNull final Map<String, Method> methods,
            @NotNull final Class<?> clazz
    ) {
        for (Method method : clazz.getDeclaredMethods()) {
            final StringBuilder keyBuilder = new StringBuilder();
            keyBuilder.append(method.getName());
            keyBuilder.append(Arrays.stream(method.getParameterTypes())
                    .map(Class::getCanonicalName)
                    .collect(Collectors.joining(", ", "(", ")")));
            if (Modifier.isAbstract(method.getModifiers())) {
                methods.putIfAbsent(keyBuilder.toString(), method);
            } else {
                methods.put(keyBuilder.toString(), method);
            }
        }

        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && Modifier.isAbstract(superclass.getModifiers())) {
            fillMethodsTable(methods, superclass);
        }

        final Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            fillMethodsTable(methods, anInterface);
        }
    }

    private void appendMethod(
            @NotNull final StringBuffer buffer,
            @NotNull final Method method
    ) {
        appendMethodModifiers(buffer, method.getModifiers());
        appendMethodReturnType(buffer, method.getReturnType());
        appendMethodName(buffer, method.getName());
        appendMethodParameters(buffer, method.getParameterTypes());
        appendMethodImplementation(buffer, method);
    }

    private void appendMethodModifiers(
            @NotNull final StringBuffer buffer,
            final int modifiers
    ) {
        final int newModifiers = modifiers & ~Modifier.ABSTRACT;
        buffer.append("\n\t").append(Modifier.toString(newModifiers));
    }

    private void appendMethodReturnType(
            @NotNull final StringBuffer buffer,
            @NotNull final Class<?> returnType
    ) {
        buffer.append(" ").append(returnType.getCanonicalName());
    }

    private void appendMethodName(
            @NotNull final StringBuffer buffer,
            @NotNull final String methodName
    ) {
        buffer.append(" ").append(methodName);
    }

    private void appendMethodParameters(
            @NotNull final StringBuffer buffer,
            @NotNull final Class<?>[] parameters
    ) {
        buffer.append(IntStream.range(0, parameters.length)
                .mapToObj(i -> String.format("%s param%d", parameters[i].getCanonicalName(), i))
                .collect(Collectors.joining(", ", "(", ")")));
    }

    private void appendMethodImplementation(
            @NotNull final StringBuffer buffer,
            @NotNull final Method method
    ) {
        final Class<?> returnType = method.getReturnType();
        buffer.append(" {\n");
        if (!returnType.getName().equals("void")) {
            if (returnType.getName().equals("boolean")) {
                buffer.append("\t\treturn false;");
            } else if (returnType.isPrimitive()) {
                buffer.append("\t\treturn 0;");
            } else {
                buffer.append("\t\treturn null;");
            }
        }
        buffer.append("\n\t}\n");
    }

    private void writeToFile(
            @NotNull final StringBuffer buffer,
            @NotNull final String packageName,
            @NotNull final String classSimpleName
    ) throws ImplementorException {
        final File targetDirectory = getClassOutputDirectory(packageName);
        final File target = new File(targetDirectory, classSimpleName + "Impl.java");
        targetDirectory.mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {
            writer.append(buffer);
        } catch (IOException e) {
            throw new ImplementorException("Unable to write the buffer to file", e);
        }
    }

    @NotNull
    private File getClassOutputDirectory(@NotNull final String packageName) {
        final String[] packageSplit = packageName.split("\\.");
        return Paths.get(outputDirectory, packageSplit).toFile();
    }
}
