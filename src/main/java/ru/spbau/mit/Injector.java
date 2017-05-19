package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public final class Injector {

    private static HashSet<Class<?>> classVisited = new HashSet<>();
    private static HashMap<Class<?>, Object> classToObject = new HashMap<>();

    private Injector(){}

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */


    public static Class<?> loadClass(final String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static ArrayList<Class<?>> loadImplementationClasses(final List<String> implementationClassNames)
            throws ClassNotFoundException {
        final ArrayList<Class<?>> result = new ArrayList<>();
        for (String className : implementationClassNames) {
            result.add(loadClass(className));
        }
        return result;
    }

    public static Constructor<?> getConstructor(final Class<?> cl) {
        Constructor<?>[] constructors = cl.getConstructors();
        return constructors[0];
    }

    public static Class<?> getClass(Class<?> cl, ArrayList<Class<?>> implementationClasses)
            throws ImplementationNotFoundException, AmbiguousImplementationException {
        boolean flag = false;
        Class<?> out = null;
        for (Class<?> implClass : implementationClasses) {
            int count = 0;
            if (cl.isAssignableFrom(implClass)) {
                flag = true;
                out = implClass;
                count++;
                if (count > 2) {
                    throw new AmbiguousImplementationException();
                }
            }
        }
        if (!flag) {
            throw new ImplementationNotFoundException();
        }
        return out;
    }

    public static Object run(Class<?> current, final ArrayList<Class<?>> implementationClasses)
            throws InjectionCycleException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            ImplementationNotFoundException, AmbiguousImplementationException {
        classVisited.add(current);
        final Class<?>[] parameters = current.getConstructors()[0].getParameterTypes();
        final Object[] constructorArgs = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> implClass = getClass(parameters[i], implementationClasses);
            if (classVisited.contains(implClass) && !classToObject.containsKey(implClass)) {
                throw new InjectionCycleException();
            }
            if (classToObject.containsKey(implClass)) {
                constructorArgs[i] = classToObject.get(implClass);
                continue;
            }
            constructorArgs[i] = run(implClass, implementationClasses);
        }
        classToObject.put(current, getConstructor(current).newInstance(constructorArgs));
        return classToObject.get(current);
    }

    public static void clear() {
        classVisited.clear();
        classToObject.clear();
    }

    public static Object initialize(String rootClassName,
                                    List<String> implementationClassNames) throws Exception {

        clear();
        final Class<?> clazz = loadClass(rootClassName);
        final ArrayList<Class<?>> implementationClasses = loadImplementationClasses(implementationClassNames);
        return run(clazz, implementationClasses);
    }
}
