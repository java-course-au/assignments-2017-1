package ru.spbau.mit;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;

    public SimpleImplementor(final String dir) {
        outputDirectory = dir;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        StringBuilder builder = new StringBuilder();

        try {
            URL[] urls = new URL[] {Paths.get(directoryPath).toUri().toURL()};
            Class clazz = new URLClassLoader(urls).loadClass(className);

            generatePackage(clazz, builder);
            generateImpl(clazz, builder);
            String fullName = className + "Impl";
            String relPath = fullName.replace('.', File.separatorChar) + ".java";
            Path resFilePath = Paths.get(outputDirectory, relPath);

            File fd = resFilePath.toFile();
            File parent = fd.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            FileUtils.writeStringToFile(fd, builder.toString());
            return fullName;
        } catch (Exception e) {
            throw new ImplementorException(e.toString());
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        StringBuilder builder = new StringBuilder();
        ClassLoader loader = this.getClass().getClassLoader();

        try {
            Class base = loader.loadClass(className);
            generateImpl(base, builder);
            String name = base.getSimpleName() + "Impl";
            File fd = Paths.get(outputDirectory, name + ".java").toFile();
            FileUtils.writeStringToFile(fd, builder.toString());
            return name;
        } catch (Exception e) {
            throw new ImplementorException(e.toString());
        }
    }

    private void generatePackage(Class base, StringBuilder builder) {
        Package pack = base.getPackage();
        if (pack != null) {
            builder.append("package ");
            builder.append(pack.getName());
            builder.append(";\n");
        }
    }

    private void generateHead(Class base, StringBuilder builder) throws ImplementorException {
        int modifiers = base.getModifiers();
        if (Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers)) {
            throw new ImplementorException("Invalid interface/abstract class");
        }

        String name = base.getSimpleName() + "Impl";
        boolean isInterface = base.isInterface();

        builder.append("class ");
        builder.append(name);
        builder.append(isInterface ? " implements " : " extends ");
        builder.append(base.getCanonicalName());
        builder.append(" {\n");
    }

    private String generateParams(Method method) {
        return Arrays.stream(method.getParameters())
                .map(x -> x.getType().getCanonicalName() + ' ' + x.getName())
                .collect(Collectors.joining(",", "(", ")"));
    }

    private String tryGenerateMethod(Method method) {
        int modifiers = method.getModifiers();
        StringBuilder mbuilder = new StringBuilder();

        mbuilder.append(Modifier.isPublic(modifiers) ? "public " : "protected ");
        mbuilder.append(method.getReturnType().getCanonicalName());
        mbuilder.append(' ');
        mbuilder.append(method.getName());
        mbuilder.append(generateParams(method));
        mbuilder.append("{\n");

        Class retType = method.getReturnType();
        if (retType != Void.TYPE) {
            mbuilder.append(" return ");
            if (!retType.isPrimitive()) {
                mbuilder.append(" null ");
            } else if (retType == Boolean.TYPE) {
                mbuilder.append(" false ");
            } else {
                mbuilder.append(" 0 ");
            }
            mbuilder.append(";\n");
        }
        mbuilder.append("}\n");

        return mbuilder.toString();
    }

    private void tryGenerateMethod(Set<String> added, Method method, StringBuilder builder) {
        int modifiers = method.getModifiers();
        if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
            return;
        }
        if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
            String methodBody = tryGenerateMethod(method);
            if (!added.contains(methodBody)) {
                added.add(methodBody);
                builder.append(methodBody);
            }
        }
    }

    private void generateMethods(Class base, StringBuilder builder) {
        Set<String> added = new HashSet<>();
        for (Method method : base.getMethods()) {
            tryGenerateMethod(added, method, builder);
        }
        for (Class clazz = base; clazz != null; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                tryGenerateMethod(added, method, builder);
            }
        }
    }

    private void generateImpl(Class base, StringBuilder builder) throws ImplementorException {
        generateHead(base, builder);
        generateMethods(base, builder);
        builder.append("}");
    }
}
