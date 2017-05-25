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
import java.util.function.Function;
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

            createFiles(pathToImpl);
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

            createFiles(pathToImpl);
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


    private static void createFiles(Path pathToImpl) throws IOException {
        if (!Files.exists(pathToImpl.getParent())) {
            Files.createDirectories(pathToImpl.getParent());
        }
        if (!Files.exists(pathToImpl)) {
            Files.createFile(pathToImpl);
        }
    }

    private static String createClass(Class<?> classImpl, String baseClass)
            throws ImplementorException {

        if (Modifier.isFinal(classImpl.getModifiers())) {
            throw new ImplementorException(classImpl.getSimpleName() + " is final");
        }

        StringBuilder sb = new StringBuilder();
        createClassName(classImpl, baseClass, sb);
        sb.append(" {").append(System.lineSeparator());
        createMethods(classImpl, sb);
        sb.append("}");
        return sb.toString();
//        return createClassName(classImpl, baseClass)
//                + " {"
//                + System.lineSeparator()
//                + createMethods(classImpl)
//                + "}";
    }

    private static void createClassName(Class<?> classImpl, String baseClass,
                                          StringBuilder sb) {
        sb.append("public class ")
                .append(classImpl.getSimpleName())
                .append("Impl")
                .append(classImpl.isInterface() ? " implements " : " extends ")
                .append(baseClass);

//        return "public class "
//                + classImpl.getSimpleName()
//                + "Impl"
//                + (classImpl.isInterface() ? " implements " : " extends ")
//                + baseClass;
    }

    private static void createMethods(Class<?> classImpl, StringBuilder sb) {
        List<Method> methods = new ArrayList<>();
        Class<?> cur = classImpl;
        while (cur != null) {
//            Arrays.stream(cur.getDeclaredMethods())
//                    .filter(m -> Modifier.isAbstract(m.getModifiers())
//                            || Modifier.isInterface(m.getModifiers()))
//                    .forEach(methods::add);

            Arrays.stream(cur.getMethods())
                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
                    .forEach(methods::add);

//            methods.addAll(Arrays.asList(cur.getDeclaredMethods()));
//            methods.addAll(Arrays.asList(cur.getMethods()));

//            Arrays.stream(cur.getDeclaredMethods())
//                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
//                    .map(SimpleImplementor::createMethod)
//                    .forEach(methods::add);
//
//            Arrays.stream(cur.getInterfaces())
//                    .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
//                    .map(SimpleImplementor::createMethod)
//                    .forEach(methods::add);

            cur = cur.getSuperclass();
        }

        methods
            .stream()
            .collect(Collectors.toMap(SimpleImplementor::methodToString,
                    Function.identity(), (p, q) -> p))
                .values()
                .forEach(m -> createMethod(m, sb));

//                .distinct()
//            .forEach(m -> createMethod(m, sb));
//        return methods
//                .stream()
//                .distinct()
//                .collect(Collectors.joining(""));
    }

    private static String methodToString(Method method) {
        return method.getName()
                + Arrays.stream(method.getParameters())
                    .map(param -> param.getType().getSimpleName())
                    .collect(Collectors.joining(", ", "(", ")"));
    }

    private static void createMethod(Method method, StringBuilder sb) {
        sb.append("\t");
        createMethodDeclaration(method, sb);
        sb.append(" {")
                .append(System.lineSeparator())
                .append("\t\t");
        createReturn(method, sb);
        sb.append(System.lineSeparator())
                .append("\t}")
                .append(System.lineSeparator());

//        return  "\t"
//                + createMethodDeclaration(method)
//                + " {"
//                + System.lineSeparator()
//                + "\t\t"
//                + createReturn(method)
//                + System.lineSeparator()
//                + "\t}"
//                + System.lineSeparator();
    }

    private static void createMethodDeclaration(Method method, StringBuilder sb) {
        sb.append(Modifier.toString(method.getModifiers()).replace("abstract", ""))
                .append(" ")
                .append(method.getReturnType().getCanonicalName())
                .append(" ")
                .append(method.getName());
        createMethodParams(method.getParameters(), sb);

//        return  Modifier.toString(method.getModifiers()).replace("abstract", "")
//                + " "
//                + method.getReturnType().getCanonicalName()
//                + " "
//                + method.getName()
//                + createMethodParams(method.getParameters());
    }

    private static void createMethodParams(Parameter[] params, StringBuilder sb) {
        sb.append(Arrays.stream(params)
                .map(param -> param.getType().getCanonicalName() + " " + param.getName())
                .collect(Collectors.joining(", ", "(", ")")));
    }

    private static void createReturn(Method method, StringBuilder sb) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            if (returnType.equals(boolean.class)) {
                sb.append("return true;");
                return;
            }
            if (returnType.equals(void.class)) {
                return;
            }
            sb.append("return 0;");
            return;
        }
        sb.append("return null;");
    }
}
