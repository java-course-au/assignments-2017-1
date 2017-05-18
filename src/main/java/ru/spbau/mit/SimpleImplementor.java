package ru.spbau.mit;


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
import java.util.LinkedList;
import java.util.Queue;

public class SimpleImplementor implements Implementor {
    private final String outputDirectory;

    public SimpleImplementor(final String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private static Class<?> loadClass(String directoryPath, String className) throws ImplementorException {
        try {
            URL path = new File(directoryPath).toURI().toURL();
            ClassLoader cl = new URLClassLoader(new URL[]{path});
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
        out.append(pkg.isEmpty() ? pkg.isEmpty() : pkg + ";\n\n");
        return pkg;
    }

    private static void implArgs(final StringBuilder out, final Method m) {
        int idx = 0;
        out.append("(");
        for (Class<?> param : m.getParameterTypes()) {
            out.append(param.getName());
            out.append("param").append(idx);
            out.append(", ");
            idx++;
        }
        out.delete(out.length(), out.length() + 1);
        out.append(")");
    }

    private static void implMethods(final StringBuilder out, final Class<?> cl) {
        for (Method method : cl.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isProtected(method.getModifiers())) {
                out.append("\t");
                out.append(Modifier.isPublic(cl.getModifiers()) ? "public " : "protected ");
                out.append(method.getReturnType().getCanonicalName()).append(" ");
                out.append(method.getName()).append(" ");
                implArgs(out, method);
                out.append(" {\n");
                implReturn(out, method.getReturnType());
                out.append("}\n\n");
            }
        }
    }

    private static void implBody(final StringBuilder out, final Class<?> cl) {
        Queue<Class> queue = new LinkedList<>();
        queue.add(cl);
        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();
            for (Class<?> interfaces : clazz.getInterfaces()) {
                implMethods(out, interfaces);
                if (interfaces.getInterfaces() != null) {
                    queue.addAll(Arrays.asList(interfaces.getInterfaces()));
                }
            }
            if (clazz.getSuperclass() != null) {
                implMethods(out, clazz.getSuperclass());
                if (clazz.getInterfaces() != null) {
                    queue.addAll(Arrays.asList(clazz.getSuperclass().getInterfaces()));
                }
                if (clazz.getSuperclass() != null) {
                    queue.add(clazz.getSuperclass().getSuperclass());
                }
            }
        }
    }

    private static void addImpl(final StringBuilder out, final Class<?> cl) throws ImplementorException {
        if (Modifier.isFinal(cl.getModifiers())) {
            throw new ImplementorException("Class" + cl.getSimpleName() + "can't be created");
        }
        out.append("public class ");
        out.append(cl.getSimpleName()).append("Impl ");
        out.append(cl.isInterface() ? "implements " : "extern ");
        out.append(cl.getCanonicalName());
        out.append(" {\n\t");

        implBody(out, cl);

        out.append("}\n");

    }

    private void write(final StringBuilder out, final Class<?> cl, final String pkg) throws ImplementorException {
        final String[] pkgSplit = pkg.split("\\.");
        final File dirs = Paths.get(outputDirectory, pkgSplit).toFile();
        final File target = new File(outputDirectory, cl.getSimpleName() + "Impl.java");
        dirs.mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target))) {
            writer.append(out.toString());
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
        StringBuilder out = new StringBuilder();
        addImpl(out, cl);
        write(out, cl, "");
        return cl.getSimpleName() + "Impl";
    }
}
