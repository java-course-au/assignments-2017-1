package ru.spbau.mit;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;
    private StringBuilder builder;

    public SimpleImplementor(final String dir) {
        outputDirectory = dir;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        builder = new StringBuilder();

        try {
            URL[] urls = new URL[] {Paths.get(directoryPath).toUri().toURL()};
            Class clazz = new URLClassLoader(urls).loadClass(className);

            generatePackage(clazz);
            generateImpl(clazz);
            String fullName = className + "Impl";
            String relPath = fullName.replace('.', File.separatorChar) + ".java";
            Path resFilePath = Paths.get(outputDirectory, relPath);

            File fd = resFilePath.toFile();
            File parent = fd.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            FileWriter fw = new FileWriter(fd);
            fw.write(builder.toString());
            fw.flush();
            fw.close();
            return fullName;
        } catch (Exception e) {
            throw new ImplementorException(e.toString());
        }
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        builder = new StringBuilder();
        ClassLoader loader = this.getClass().getClassLoader();

        try {
            Class base = loader.loadClass(className);
            generateImpl(base);
            String name = base.getSimpleName() + "Impl";
            File fd = Paths.get(outputDirectory, name + ".java").toFile();

            FileWriter fw = new FileWriter(fd);
            fw.write(builder.toString());
            fw.flush();
            fw.close();
            return name;
        } catch (Exception e) {
            throw new ImplementorException(e.toString());
        }
    }

    private void generatePackage(Class base) {
        Package pack = base.getPackage();
        if (pack != null) {
            builder.append("package ");
            builder.append(pack.getName());
            builder.append(";\n");
        }
    }

    private void generateHead(Class base) throws ImplementorException {
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

    private void generateParams(StringBuilder mbuilder, Method method) {
        Class[] params = method.getParameterTypes();
        if (params.length == 0) {
            return;
        }

        mbuilder.append(params[0].getCanonicalName());
        mbuilder.append(" arg" + Integer.toString(0));

        for (int i = 1; i < params.length; i++) {
            mbuilder.append(" , ");
            mbuilder.append(params[i].getCanonicalName());
            mbuilder.append(" arg" + Integer.toString(i));
        }
    }

    private String generateMethod(Method method) {
        int modifiers = method.getModifiers();
        StringBuilder mbuilder = new StringBuilder();

        mbuilder.append(Modifier.isPublic(modifiers) ? "public " : "protected ");
        mbuilder.append(method.getReturnType().getCanonicalName());
        mbuilder.append(' ');

        mbuilder.append(method.getName());
        mbuilder.append("(");
        generateParams(mbuilder, method);
        mbuilder.append(" ) {\n");

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

    private void generateMethods(Class base) {
        HashSet<String> methods = new HashSet<>();
        Queue<Class> cls = new LinkedList<>();
        cls.add(base);

        while (!cls.isEmpty()) {
            Class clazz = cls.poll();
            for (Method method : clazz.getDeclaredMethods()) {
                int modifiers = method.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }
                if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                    String methodBody = generateMethod(method);
                    if (methods.contains(methodBody)) {
                        continue;
                    }
                    methods.add(methodBody);
                    builder.append(methodBody);
                }
            }

            if (clazz.getSuperclass() != null) {
                cls.add(clazz.getSuperclass());
            }
            for (Class interfacezz : clazz.getInterfaces()) {
                cls.add(interfacezz);
            }
        }
    }

    private void generateImpl(Class base) throws ImplementorException {
        generateHead(base);
        generateMethods(base);
        builder.append("}");
    }
}
