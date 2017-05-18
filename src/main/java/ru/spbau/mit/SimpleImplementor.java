package ru.spbau.mit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleImplementor implements Implementor {

    private String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        ClassLoader classLoader = SimpleImplementor.class.getClassLoader();
        Class<?> classImpl;
        try {
            classImpl = classLoader.loadClass(className);
            Path pathToImpl = Paths.get(outputDirectory
                    + System.getProperty("file.separator")
                    + classImpl.getSimpleName()
                    + "Impl.java");

            if (!Files.exists(pathToImpl.getParent())) {
                Files.createDirectories(pathToImpl.getParent());
            }
            if (!Files.exists(pathToImpl)) {
                Files.createFile(pathToImpl);
            }

            String res = createClass(classImpl, classImpl.getCanonicalName());
            Files.write(pathToImpl, res.getBytes());
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("class not found", e);
        } catch (IOException e) {
            throw new ImplementorException("cannot write class to file", e);
        }
        return classImpl.getSimpleName() + "Impl";
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className)
            throws ImplementorException {
        try {
            URL classesPath = Paths.get(directoryPath).toUri().toURL();
            ClassLoader classLoader = new URLClassLoader(new URL[]{classesPath});
            Class<?> classImpl = classLoader.loadClass(className);

            Path pathToImpl = Paths.get(outputDirectory + System.getProperty("file.separator")
                    + className.replace(".", System.getProperty("file.separator"))
                    + "Impl.java");
            if (!Files.exists(pathToImpl.getParent())) {
                Files.createDirectories(pathToImpl.getParent());
            }
            if (!Files.exists(pathToImpl)) {
                Files.createFile(pathToImpl);
            }

            String packageName = "";
            if (classImpl.getPackage() != null) {
                packageName = "package "
                        + classImpl.getPackage().getName()
                        + ";"
                        + System.lineSeparator()
                        + System.lineSeparator();
            }

            String res = packageName + createClass(classImpl, classImpl.getSimpleName());
            Files.write(pathToImpl, res.getBytes());
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("class not found", e);
        } catch (IOException e) {
            throw new ImplementorException("cannot write class to file", e);
        }

        return className + "Impl";
    }

    private static String createClass(Class<?> classImpl, String baseClass)
            throws ImplementorException {

        if (Modifier.isFinal(classImpl.getModifiers())) {
            throw new ImplementorException(classImpl.getSimpleName() + " is final");
        }

        return createClassName(classImpl, baseClass)
                + " {"
                + System.lineSeparator()
                + createMethods(classImpl)
                + "}";
    }

    private static String createClassName(Class<?> classImpl, String baseClass) {
        return "public class "
                + classImpl.getSimpleName()
                + "Impl"
                + (classImpl.isInterface() ? " implements " : " extends ")
                + baseClass;
    }

    private static String createMethods(Class<?> classImpl) {
        List<String> methods = new ArrayList<>();
        Class<?> cur = classImpl;
        while (cur != null) {
            Arrays.stream(cur.getDeclaredMethods())
                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
                    .map(SimpleImplementor::createMethod)
                    .forEach(methods::add);

            Arrays.stream(cur.getInterfaces())
                    .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                    .map(SimpleImplementor::createMethod)
                    .forEach(methods::add);

            cur = cur.getSuperclass();
        }
        return methods
                .stream()
                .distinct()
                .collect(Collectors.joining(""));
    }

    private static String createMethod(Method method) {
        // compiler will use StringBuilder automatically
        return  "\t"
                + createMethodDeclaration(method)
                + " {"
                + System.lineSeparator()
                + "\t\t"
                + createReturn(method)
                + System.lineSeparator()
                + "\t}"
                + System.lineSeparator();
    }

    private static String createMethodDeclaration(Method method) {
        return  Modifier.toString(method.getModifiers()).replace("abstract", "")
                + " "
                + method.getReturnType().getCanonicalName()
                + " "
                + method.getName()
                + createMethodParams(method.getParameters());
    }

    private static String createMethodParams(Parameter[] params) {
        return Arrays.stream(params)
                .map(param -> param.getType().getCanonicalName() + " " + param.getName())
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private static String createReturn(Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            if (returnType.equals(boolean.class)) {
                return "return true;";
            }
            if (returnType.equals(void.class)) {
                return "";
            }
            return "return 0;";
        }
        return "return null;";
    }
}
