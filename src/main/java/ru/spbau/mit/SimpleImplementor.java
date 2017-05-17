package ru.spbau.mit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        Class classToImplement;
        StringBuilder implementation;

        try {
            URL dir = new URL("file://" + directoryPath);
            ClassLoader loader = new URLClassLoader(new URL[]{dir});
            classToImplement = loader.loadClass(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e);
        }

        implementation = new StringBuilder();

        generatePackage(implementation, classToImplement);
        generateDeclaration(implementation, classToImplement);
        generateClassBody(implementation, classToImplement);

        File file = new File(outputDirectory + "/" + className.replace(".", "/") + "Impl.java");
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(implementation);
        } catch (IOException e) {
            throw new ImplementorException(e.getMessage(), e);
        }

        return className + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        ClassLoader classLoader = SimpleImplementor.class.getClassLoader();
        Class classToImplement;
        StringBuilder implementation;

        try {
            classToImplement = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException(e.getMessage(), e);
        }

        implementation = new StringBuilder();
        generateDeclaration(implementation, classToImplement);
        generateClassBody(implementation, classToImplement);

        String[] classNameSplitted = className.split("\\.");
        String simpleClassName = classNameSplitted[classNameSplitted.length - 1];
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(outputDirectory + "/" + simpleClassName + "Impl.java"))) {
            writer.append(implementation);
        } catch (IOException e) {
            throw new ImplementorException(e.getMessage(), e);
        }

        return simpleClassName + "Impl";
    }

    private void generatePackage(StringBuilder implementation, Class classToImplement) {
        Package classPackage = classToImplement.getPackage();

        if (classPackage != null) {
            implementation.append("package ");
            implementation.append(classToImplement.getPackage().getName()).append(";\n\n");
        }
    }

    private void generateDeclaration(StringBuilder implementation, Class classToImplement) {
        implementation.append("public class ");
        implementation.append(classToImplement.getSimpleName()).append("Impl");
        implementation.append(classToImplement.isInterface() ? " implements " : " extends ");
        implementation.append(classToImplement.getCanonicalName());
    }

    private void generateClassBody(StringBuilder implementation, Class classToImplement) {
        implementation.append(" {\n\t");

        Constructor<?>[] constructors = classToImplement.getDeclaredConstructors();
        if (!classToImplement.isInterface() && Stream.of(constructors).
                noneMatch(c -> c.getParameterCount() == 0)) {
            generateConstructor(implementation, classToImplement);
        }

        Set<String> methodsImpl = new HashSet<>();
        generateMethodsForClass(methodsImpl, classToImplement);
        implementation.append(String.join("\n\n\t", methodsImpl));

        implementation.append("\n}\n");
    }

    private void generateConstructor(StringBuilder implementation, Class classToImplement) {
        Constructor<?> constructorWithParams = Stream.of(classToImplement.getDeclaredConstructors()).
                filter(c -> c.getParameterCount() > 0).
                findAny().
                get();
        List<String> constructorImpl = new ArrayList<>();
        constructorImpl.add("public");
        constructorImpl.add(classToImplement.getSimpleName() + "Impl");
        constructorImpl.add(getParameters(constructorWithParams.getParameters()));
        constructorImpl.add("{\n\t\tsuper");
        constructorImpl.add(Stream.of(constructorWithParams.getParameters()).
                map(Parameter::getName).
                collect(Collectors.joining(", ", "(", ")")));
        constructorImpl.add(";\n}\n\t");

        implementation.append(String.join(" ", constructorImpl));
    }

    private void generateMethodsForClass(Set<String> methodsImpl, Class classToImplement) {
        for (Method method : classToImplement.getDeclaredMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                methodsImpl.add(implementMethod(method));
            }
        }

        Class<?> superClass = classToImplement.getSuperclass();
        if (superClass != null) {
            generateMethodsForClass(methodsImpl, superClass);
        }

        Class<?>[] interfaces = classToImplement.getInterfaces();
        for (Class<?> implementedInterface : interfaces) {
            generateMethodsForClass(methodsImpl, implementedInterface);
        }
    }

    private String implementMethod(Method method) {
        List<String> methodImpl = new ArrayList<>();

        methodImpl.add(getModifier(method));
        methodImpl.add(method.getReturnType().getCanonicalName());
        methodImpl.add(method.getName());
        methodImpl.add(getParameters(method.getParameters()));
        methodImpl.add(getImplementationByReturnType(method.getReturnType()));

        return String.join(" ", methodImpl);
    }

    private String getModifier(Method method) {
        //transient is here to deal with varargs
        int modifier = method.getModifiers() & (~Modifier.ABSTRACT) & (~Modifier.TRANSIENT);
        return Modifier.toString(modifier);
    }

    private String getParameters(Parameter[] parameters) {
        return Stream.of(parameters).
                map(p -> p.getType().getCanonicalName() + " " + p.getName()).
                collect(Collectors.joining(", ", "(", ")"));
    }

    private String getImplementationByReturnType(Class<?> returnType) {
        String returnStatement = "{\n\t\treturn";
        String returnValue;

        if (!returnType.isPrimitive()) {
            returnValue = " null";
        } else {
            if (returnType.equals(boolean.class)) {
                returnValue = " false";
            } else if (returnType.equals(void.class)) {
                returnValue = "";
            } else {
                returnValue = " 0";
            }
        }

        return returnStatement + returnValue + ";\n\t}";
    }
}
