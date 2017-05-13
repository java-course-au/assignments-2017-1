package ru.spbau.mit;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleImplementor implements Implementor {
    private String directoryForSave;

    public SimpleImplementor(final String outputDir) {
        directoryForSave = outputDir;
    }

    public final String getDirectoryForSave() {
        return directoryForSave;
    }

    @Override
    public String implementFromDirectory(String directoryPath, String className)
            throws ImplementorException {
        try {
            URL classFile = Paths.get(directoryPath).toUri().toURL();
            ClassLoader classLoader = new URLClassLoader(new URL[]{classFile});

            Class classForImplement = classLoader.loadClass(className);
            String packegeClass = className.replace('.', '/');

            Path pathToImplClass = Paths.get(directoryForSave +
                    "/" + packegeClass + "Impl.java");

            Files.createDirectories(pathToImplClass);
            Files.createFile(pathToImplClass.getParent());

            String packageName;

            try {
                packageName = "package" +
                        classForImplement.getPackage().getName() + "; \n\n";
            } catch (Exception ex) {
                packageName = "";
            }

            Files.write(pathToImplClass, packageName.getBytes());
            Files.write(pathToImplClass, createMethod(classForImplement).getBytes());
        } catch (Exception ex) {
            throw new ImplementorException(ex.getMessage());
        }

        return className + "Impl";
    }

    @Override
    public String implementFromStandardLibrary(String className)
            throws ImplementorException {
        try {
            ClassLoader classLoader = SimpleImplementor.class.getClassLoader();
            Class classForImplement = classLoader.loadClass(className);

            Path pathToImplClass = Paths.get(directoryForSave,
                    classForImplement.getSimpleName() + "Impl.java");

            Files.createFile(pathToImplClass);
            Files.write(pathToImplClass, createMethod(classForImplement).getBytes());
            return classForImplement.getSimpleName() + "Impl";
        } catch (Exception ex) {
            throw new ImplementorException(ex.getMessage());
        }
    }


    private String createMethod(final Class classImpl) throws ImplementorException {
        if (Modifier.isFinal(classImpl.getModifiers())) {
            throw new ImplementorException("modifiers is final");
        }

        String textInClass = createClassName(classImpl.getSimpleName(), classImpl.getName(),
                classImpl.isInterface());

        textInClass += " {\n";
        textInClass += traverseMethods(classImpl);
        return textInClass + "}";
    }

    private String traverseMethods(final Class classImp) {
        HashSet<String> createMethod = new HashSet<>();

        Class curClass = classImp;
        while (curClass != null) {

            for (Method method : curClass.getDeclaredMethods()) {
                if (Modifier.isAbstract(method.getModifiers())) {
                    createMethod.add(createMathodImpl(method));
                }
            }

            for (Class aClass : curClass.getInterfaces()) {
                for (Method method: aClass.getDeclaredMethods()) {
                    createMethod.add(createMathodImpl(method));
                }
            }

            curClass = curClass.getSuperclass();
        }

        return createMethod.stream().reduce("",String::concat);
    }

    private String createClassName(final String simpleClassName, final String name,
                                   final boolean isInterface) {
        String implemOrExtend = isInterface ? " implements " : " extends ";
        return "public class "
                + simpleClassName
                + "Impl"
                + implemOrExtend
                + name;
    }


    private String createMathodImpl(final Method method) {
        return createDeclarationMethod(method)
                + "{\n"
                + "\t\t return "
                + createReturnValueFromMethod(method)
                + "; \n\t}\n";
    }

    private String createDeclarationMethod(final Method method) {
        return "\t"
                + Modifier.toString(method.getModifiers()).replace("abstract", "")
                + " "
                + method.getReturnType().getCanonicalName()
                + " "
                + method.getName()
                + "("
                + createParamsMethod(method.getParameters())
                + ")";
    }

    private String createParamsMethod(final Parameter[] parameters) {
        List<String> paramName = Stream.of(parameters)
                .map(parameter -> parameter.getType().getCanonicalName())
                .collect(Collectors.toList());
        return paramName
                .stream()
                .collect(Collectors.toMap(paramName::indexOf, t -> t))
                .entrySet()
                .stream()
                .map(integerStringEntry -> integerStringEntry.getValue() + " args" + integerStringEntry.getKey())
                .collect(Collectors.joining(" , "));
    }

    private String createReturnValueFromMethod(final Method method) {
        Class<?> returnValue = method.getReturnType();
        if (returnValue.isPrimitive()) {
            if (returnValue.equals(void.class)) {
                return "";
            }
            if (returnValue.equals(boolean.class)) {
                return "true";
            }
        } else {
            return "null";
        }
        return "0";
    }
}
