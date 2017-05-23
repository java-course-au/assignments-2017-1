package ru.spbau.mit;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleImplementor implements Implementor {
    private String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        URLClassLoader classLoader;
        try {
            classLoader = new URLClassLoader(new URL[]{Paths.get(directoryPath).toAbsolutePath().toUri().toURL()});
        } catch (MalformedURLException e) {
            throw new ImplementorException("Malformed url");
        }

        Class<?> loadedClass;
        try {
            loadedClass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("Could not load class");
        }

        Package clsPackage = loadedClass.getPackage();
        return makeAndSaveClass(loadedClass, clsPackage);
    }

    private String makeAndSaveClass(Class<?> loadedClass, Package desiredPackage) throws ImplementorException {
        if (cannotExtendClass(loadedClass)) {
            throw new ImplementorException("Cannot extend class");
        }
        String simpleName = loadedClass.getSimpleName() + "Impl";
        String name = simpleName;
        if (desiredPackage != null) {
            name = desiredPackage.getName() + "." + name;
        }

        String classBody = makeClassBody(loadedClass, desiredPackage, simpleName);
        saveClass(name, classBody);

        return name;
    }

    private void saveClass(String className, String classBody) throws ImplementorException {
        final String[] split = className.split("\\.");
        Path dir = Paths.get(outputDirectory);

        for (int i = 0; i + 1 < split.length; i++) {
            dir = Paths.get(dir.toString(), split[i]);
        }

        try {
            FileUtils.writeStringToFile(new File(dir.toString(), split[split.length - 1] + ".java"),
                    classBody,
                    Charset.defaultCharset());
        } catch (IOException e) {
            throw new ImplementorException("Could not create resulting file");
        }
    }

    private boolean cannotExtendClass(Class<?> loadedClass) {
        return loadedClass.isAnonymousClass() || Modifier.isFinal(loadedClass.getModifiers());
    }

    private Set<Method> getAllMethods(Class<?> loadedClass) {
        Set<Method> interestingMethods = new HashSet<>();
        while (loadedClass != null) {
            interestingMethods.addAll(Arrays.asList(loadedClass.getDeclaredMethods()));
            // Get inherited public methods (e.g. from interfaces).
            interestingMethods.addAll(Arrays.asList(loadedClass.getMethods()));
            loadedClass = loadedClass.getSuperclass();
        }
        return interestingMethods;
    }

    private String makeClassBody(Class<?> loadedClass, Package desiredPackage, String simpleName) {
        StringBuilder body = new StringBuilder();
        if (desiredPackage != null) {
            body.append(String.format("package %s;\n\n", desiredPackage.getName()));
        }

        String extendAction = "extends";
        if (loadedClass.isInterface()) {
            extendAction = "implements";
        }
        body.append(String.format("public class %s %s %s {\n", simpleName,
                extendAction, loadedClass.getCanonicalName()));

        getAllMethods(loadedClass).stream().filter(meth -> {
            int methodModifiers = meth.getModifiers();
            return Modifier.isInterface(methodModifiers) || Modifier.isAbstract(methodModifiers);
        }).map(this::buildOneMethod).distinct().forEach(body::append);

        body.append("}");

        return body.toString();
    }

    private String buildOneMethod(Method meth) {
        StringBuilder methodBody = new StringBuilder();
        methodBody.append("@Override\n");
        methodBody.append("public ");

        Class<?> returnType = meth.getReturnType();

        methodBody.append(returnType.getCanonicalName()).append(" ");
        methodBody.append(meth.getName());

        addMethodParams(methodBody, meth);
        addThrowsForMethod(methodBody, meth);

        methodBody.append(" {\n");
        methodBody.append("throw new UnsupportedOperationException();\n");
        methodBody.append("}\n");

        return methodBody.toString();
    }

    private void addMethodParams(StringBuilder body, Method meth) {
        body.append("(");
        int argIdx = 0;
        for (Class<?> arg : meth.getParameterTypes()) {
            if (argIdx > 0) {
                body.append(", ");
            }
            body.append(arg.getCanonicalName());
            body.append(" ");
            body.append(String.format("arg%d", argIdx));
            argIdx++;
        }
        body.append(") ");
    }

    private void addThrowsForMethod(StringBuilder body, Method meth) {
        Class<?>[] exceptionTypes = meth.getExceptionTypes();
        if (exceptionTypes.length > 0) {
            body.append("throws ");
        }
        for (int idx = 0; idx < exceptionTypes.length; idx++) {
            Class<?> exc = exceptionTypes[idx];
            if (idx > 0) {
                body.append(", ");
            }
            body.append(exc.getCanonicalName());
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        Class<?> loadedClass;
        try {
            loadedClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("Standard class not found");
        }

        return makeAndSaveClass(loadedClass, null);
    }
}
