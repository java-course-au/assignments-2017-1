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
import java.util.Arrays;
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

            Files.write(pathToImpl, createClass(classImpl).getBytes());
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
            if (classImpl.getPackage() != null && !classImpl.getPackage().getName().isEmpty()) {
                packageName = "package "
                        + classImpl.getPackage().getName()
                        + ";"
                        + System.lineSeparator()
                        + System.lineSeparator();
            }

            String res = packageName + createClass(classImpl);
            Files.write(pathToImpl, res.getBytes());
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("class not found", e);
        } catch (IOException e) {
            throw new ImplementorException("cannot write class to file", e);
        }

        return className + "Impl";
    }

    private String createClass(Class<?> classImpl) throws ImplementorException {

        if (Modifier.isFinal(classImpl.getModifiers())) {
            throw new ImplementorException(classImpl.getSimpleName() + " is final");
        }

        return createClassName(classImpl)
                + " {"
                + System.lineSeparator()
                + createMethods(classImpl)
                + "}";
    }

    private String createClassName(Class<?> classImpl) {
        return "public class "
                + classImpl.getSimpleName()
                + "Impl"
                + (classImpl.isInterface() ? " implements " : " extends ")
                + classImpl.getCanonicalName();
    }

    private String createMethods(Class<?> classImpl) {
        StringBuilder sb = new StringBuilder();

        Class<?> cur = classImpl;
        while (cur != null) {
            Arrays.stream(cur.getDeclaredMethods())
                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
                    .map(this::createMethod)
                    .forEach(sb::append);

            Arrays.stream(cur.getInterfaces())
                    .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                    .map(this::createMethod)
                    .forEach(sb::append);

            cur = cur.getSuperclass();
        }
        return sb.toString();
    }

    private String createMethod(Method method) {
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

    private String createMethodDeclaration(Method method) {
        return  Modifier.toString(method.getModifiers()).replace("abstract", "")
                + " "
                + method.getReturnType().getCanonicalName()
                + " "
                + method.getName()
                + createMethodParams(method.getParameters());
    }

    private String createMethodParams(Parameter[] params) {
        return Arrays.stream(params)
                .map(param -> param.getType().getCanonicalName() + " " + param.getName())
                .collect(Collectors.joining(", ", "(", ")"));
    }

    private String createReturn(Method method) {
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
