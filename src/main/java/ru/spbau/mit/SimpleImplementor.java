package ru.spbau.mit;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SimpleImplementor implements Implementor {

    private static final String TAB = "    ";

    private String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        Class<?> classToExtend = loadClassFromDirectory(directoryPath, className);
        return implement(classToExtend, true);
    }

    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        Class<?> classToExtend = getClassFromStandardLibrary(className);
        return implement(classToExtend, false);
    }

    private String implement(Class<?> classToExtend, boolean packageNeeded) throws ImplementorException {
        File outputFile = createOutputFile(classToExtend, packageNeeded);
        checkClassNotFinal(classToExtend);
        return doGenerate(classToExtend, outputFile, packageNeeded);
    }

    private static void checkClassNotFinal(Class<?> clazz) throws ImplementorException {
        if (Modifier.isFinal(clazz.getModifiers())) {
            throw new ImplementorException("Class is final.");
        }
    }

    private String doGenerate(Class<?> classToExtend, File outputFile, Boolean packageNeeded)
            throws ImplementorException {
        try (BufferedWriter output = new BufferedWriter(new FileWriter(outputFile))) {
            generatePackage(classToExtend, packageNeeded, output);
            generateGeneratedWarning(output);
            generateClassBegin(classToExtend, output);
            for (Method method : classToExtend.getMethods()) {
                if (!Modifier.isStatic(method.getModifiers()) && !Modifier.isFinal(method.getModifiers())) {
                    generateMethod(output, method);
                }
            }
            for (Method method : classToExtend.getDeclaredMethods()) {
                if (Modifier.isProtected(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())
                        && !Modifier.isFinal(method.getModifiers())) {
                    generateMethod(output, method);
                }
            }
            generateClassEnd(output);
        } catch (IOException ex) {
            throw new ImplementorException("Cannot write to output file.", ex);
        }
        return getGeneratedClassName(classToExtend, packageNeeded);
    }

    private void generateMethod(BufferedWriter output, Method method) throws IOException, ImplementorException {
        generateMethodBegin(output, method);
        generateMethodBody(output, method);
        generateMethodEnd(output);
    }

    private void generateMethodBegin(BufferedWriter output, Method method)
            throws IOException, ImplementorException {
        output.write(TAB);
        if (Modifier.isPublic(method.getModifiers())) {
            output.write(" public ");
        } else if (Modifier.isProtected(method.getModifiers())) {
            output.write(" protected ");
        } else {
            throw new ImplementorException("Invalid input.");
        }
        output.write(method.getReturnType().getCanonicalName() + " ");
        output.write(method.getName());
        String params =
                Arrays.stream(method.getParameters())
                        .map(param -> param.getType().getCanonicalName() + " " + param.getName())
                        .collect(Collectors.joining(", "));
        output.write("(" + params + ") {");
        output.newLine();
    }

    private void generateMethodBody(BufferedWriter output, Method method) throws IOException {
        output.write(TAB + TAB);
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(void.class)) {
            output.write("return;");
        } else if (returnType.equals(boolean.class)) {
            output.write("return false;");
        } else if (returnType.isPrimitive()) {
            output.write("return 0;");
        } else {
            output.write("return null;");
        }
        output.newLine();
    }

    private void generateMethodEnd(BufferedWriter output) throws IOException {
        output.write(TAB + "}");
        output.newLine();
    }

    private void generateClassBegin(Class<?> classToExtend, BufferedWriter output) throws IOException {
        output.write("public class " + getGeneratedClassName(classToExtend, false));
        if (classToExtend.isInterface()) {
            output.write(" implements " + classToExtend.getCanonicalName() + " {");
        } else {
            output.write(" extends " + classToExtend.getCanonicalName() + " {");
        }
        output.newLine();
    }

    private void generateClassEnd(BufferedWriter output) throws IOException {
        output.write("}");
        output.newLine();
    }

    private void generatePackage(Class<?> classToExtend, Boolean packageNeeded, BufferedWriter output)
            throws IOException {
        if (packageNeeded && classToExtend.getPackage() != null) {
            output.write("package " + classToExtend.getPackage().getName() + ";");
            output.newLine();
        }
    }

    private void generateGeneratedWarning(BufferedWriter output) throws IOException {
        output.write("// this code is generated.");
        output.newLine();
    }

    private File createOutputFile(Class<?> classToExtend, Boolean packageNeeded) throws ImplementorException {
        String generatedClassPath =
                getGeneratedClassName(classToExtend, packageNeeded)
                        .replaceAll("\\.", File.separator) + ".java";
        File generatedFile = new File(outputDirectory, generatedClassPath);
        try {
            generatedFile.getParentFile().mkdirs();
            generatedFile.createNewFile();
        } catch (IOException ex) {
            throw new ImplementorException("Cannot create output file.", ex);
        }
        return generatedFile;
    }

    private static String getGeneratedClassName(Class<?> classToGenerate, Boolean packageNeeded) {
        if (packageNeeded) {
            return classToGenerate.getCanonicalName() + "Impl";
        } else {
            return classToGenerate.getSimpleName() + "Impl";
        }
    }

    private Class<?> getClassFromStandardLibrary(String className) throws ImplementorException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new ImplementorException("Cannot access the class.", ex);
        }
    }

    private Class<?> loadClassFromDirectory(String directoryPath, String className) throws ImplementorException {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new ImplementorException("Invalid directory.");
        }

        try {
            URL jar = new URL("file://" + dir.getCanonicalPath() + File.separator);
            ClassLoader cl = new URLClassLoader(new URL[]{jar});
            return cl.loadClass(className);
        } catch (IOException | ClassNotFoundException ex) {
            throw new ImplementorException("Cannot access the class.", ex);
        }
    }
}
