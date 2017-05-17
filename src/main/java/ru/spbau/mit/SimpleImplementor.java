package ru.spbau.mit;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
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
            String name = generateImpl(clazz);
            Package pack = clazz.getPackage();

            String packagePath = (pack == null) ? "" : pack.getName().replace('.', File.separatorChar);
            Path resFilePath = Paths.get(outputDirectory, packagePath, name + ".java");
            File fd = resFilePath.toFile();
            File parent = fd.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            FileWriter fw = new FileWriter(fd);
            fw.write(builder.toString());
            fw.flush();
            fw.close();
            return name;
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
            String name = generateImpl(base);

            FileWriter fd = new FileWriter(new File(outputDirectory + File.separator + name + ".java"));
            fd.write(builder.toString());
            fd.flush();
            fd.close();
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

    private String genHead(Class base) throws ImplementorException {
        int modifiers = base.getModifiers();
        if (Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers)) {
            throw new ImplementorException("Invalid interface/abstract class");
        }

        final String name = base.getSimpleName() + "Impl";
        boolean isInterface = base.isInterface();

        builder.append("class ");
        builder.append(name);
        builder.append(isInterface ? " implements " : " extends ");
        builder.append(base.getCanonicalName());
        builder.append(" {\n");
        return name;
    }

    private void generateParams(Method method) {
        Class[] params = method.getParameterTypes();
        if (params.length == 0) {
            return;
        }

        builder.append(params[0].getCanonicalName());
        builder.append(" arg" + Integer.toString(0));

        for (int i = 0; i < params.length; i++) {
            builder.append(" , ");
            builder.append(params[i].getCanonicalName());
            builder.append(" arg" + Integer.toString(i));
        }
    }

    private void generateMethod(Class base, Method method) {
        int modifiers = method.getModifiers();
        builder.append(Modifier.isPublic(modifiers) ? "public " : "protected ");
        builder.append(method.getReturnType().getCanonicalName());
        builder.append(' ');

        builder.append(method.getName());
        builder.append("(");
        generateParams(method);
        builder.append(" ) {\n");

        Class retType = method.getReturnType();
        if (retType != Void.TYPE) {
            builder.append(" return ");
            if (!retType.isPrimitive()) {
                builder.append(" null ");
            } else if (retType == Boolean.TYPE) {
                builder.append(" false ");
            } else {
                builder.append(" 0 ");
            }
            builder.append(";\n");
        }
        builder.append("}\n");
    }

    private void generateMethods(Class base) {
        HashSet<String> methods = new HashSet<>();
        Queue<Class> cls = new LinkedList<>();
        cls.add(base);

        while (!cls.isEmpty()) {
            Class clazz = cls.poll();
            for (Method method : clazz.getDeclaredMethods()) {
                if (methods.contains(method.getName())) {
                    continue;
                }

                int modifiers = method.getModifiers();
                if ((Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers))
                        && !Modifier.isFinal(modifiers)
                        && !Modifier.isStatic(modifiers)) {
                    methods.add(method.getName());
                    generateMethod(base, method);
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

    private String generateImpl(Class base) throws ImplementorException {
        String name = genHead(base);
        generateMethods(base);
        builder.append("}");
        return name;
    }
}
