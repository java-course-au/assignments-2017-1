package ru.spbau.mit;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;

    public SimpleImplementor(final String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private static Class<?> loadClass(String directoryPath, String className) throws ImplementorException {
        try {
            final URL path = new File(directoryPath).toURI().toURL();
            final ClassLoader cl = new URLClassLoader(new URL[]{path});
            return cl.loadClass(className);

        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new ImplementorException("Class " + className + "can't be loaded");
        }
    }

    private static String getPackage(Class<?> cl) {
        final Package pkg = cl.getPackage();
        return pkg == null ? "" : pkg.getName();
    }

    private static void implReturn(final StringBuilder out, final Class<?> cl) {
        out.append("\t\treturn");
        if (cl.isPrimitive()) {
            if (cl.equals(void.class)) {
                out.append(";\n");
            } else if (cl.equals(boolean.class)) {
                out.append(" true;\n");
            } else {
                out.append(" 0;\n");
            }
        } else {
            out.append(" null;\n");
        }
    }

    private static String implPackage(final StringBuilder out, final Class<?> cl) {
        final String pkg = getPackage(cl);
        out.append(pkg.isEmpty() ? "" : "package " + pkg + ";\n\n");
        return pkg;
    }

    private static void implArgs(final StringBuilder out, final Method m) {
        out.append("(");
        String parameters = Arrays.stream(m.getParameters())
                .map(parameter -> parameter.getType().getCanonicalName() + " " + parameter.getName())
                .collect(Collectors.joining(", "));
        out.append(parameters);
        out.append(")");
    }

    private static void implMethods(final StringBuilder out, final Method method, final HashSet<String> methods) {
        StringBuilder tmp = new StringBuilder();
        if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
            tmp.append("\t");
            tmp.append("public ");
            tmp.append(method.getReturnType().getCanonicalName()).append(" ");
            tmp.append(method.getName()).append(" ");
            implArgs(tmp, method);
            tmp.append(" {\n");
            implReturn(tmp, method.getReturnType());
            tmp.append("\t}\n\n");
        }
        if (!methods.contains(tmp.toString()) && !Modifier.isFinal(method.getModifiers())) {
            methods.add(tmp.toString());
            out.append(tmp.toString());
        }
    }

    private static void implBody(final StringBuilder out, final Class<?> cl) {
        final Queue<Class> queue = new LinkedList<>();
        final HashSet<String> methods = new HashSet<>();
        queue.add(cl);
        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();
            for (Method method : cl.getDeclaredMethods()) {
                implMethods(out, method, methods);
            }
            for (Method method : cl.getMethods()) {
                implMethods(out, method, methods);
            }
            if (clazz.getSuperclass() != null) {
                queue.add(clazz.getSuperclass());
            }
        }
    }

    private static void addImpl(final StringBuilder out, final Class<?> cl) throws ImplementorException {
        if (Modifier.isFinal(cl.getModifiers()) || Modifier.isPrivate(cl.getModifiers())) {
            throw new ImplementorException("Class" + cl.getSimpleName() + "can't be created");
        }
        out.append("public class ");
        out.append(cl.getSimpleName()).append("Impl ");
        out.append(cl.isInterface() ? "implements " : "extends ");
        out.append(cl.getCanonicalName());
        out.append(" {\n");
        implBody(out, cl);
        out.append("}\n");
    }

    private void write(final StringBuilder out, final Class<?> cl, final String pkg) throws ImplementorException {
        final String[] pkgSplit = pkg.split("\\.");
        final File dirs = Paths.get(outputDirectory, pkgSplit).toFile();
        final File target = new File(dirs, cl.getSimpleName() + "Impl.java");
        dirs.mkdirs();
        try {
            FileUtils.writeStringToFile(target, out.toString());
        } catch (IOException e) {
            throw new ImplementorException("Unable to write the buffer to file", e);
        }
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        final Class<?> cl = loadClass(directoryPath, className);
        final StringBuilder out = new StringBuilder();
        final String pkg = implPackage(out, cl);
        addImpl(out, cl);
        write(out, cl, pkg);
        return cl.getCanonicalName() + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        final Class<?> cl;
        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ImplementorException("Class " + className + "can't be loaded");
        }
        final StringBuilder out = new StringBuilder();
        addImpl(out, cl);
        write(out, cl, "");
        return cl.getSimpleName() + "Impl";
    }
}
