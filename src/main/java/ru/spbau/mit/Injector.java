package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public final class Injector {

    private static HashSet<Class<?>> classVisited = new HashSet<>();
    private static HashMap<Class<?>, Object> classToObject = new HashMap<>();
    private static Class<?> root = null;

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

    public static void checkAmbiguousImplementation(final Class<?>[] types,
                                                    final ArrayList<Class<?>> implementationClasses)
            throws AmbiguousImplementationException {
        for (Class<?> type : types) {
            int count = 0;
            for (Class<?> implClass : implementationClasses) {
                if (type.isAssignableFrom(implClass)) {
                    count++;
                }
            }
            if (count > 2) {
                throw new AmbiguousImplementationException();
            }
        }
    }

    public static void checkImplementationNotFound(final Class<?>[] types,
                                                   final ArrayList<Class<?>> implementationClasses)
            throws ImplementationNotFoundException {
        final boolean[] check = new boolean[types.length];
        for (int i = 0; i < types.length; i++) {
            for (Class<?> implClass : implementationClasses) {
                if (types[i].isAssignableFrom(implClass)) {
                    check[i] = true;
                    break;
                }
            }
        }
        for (boolean i : check) {
            if (!i) {
                throw new ImplementationNotFoundException();
            }
        }
    }

    public static Class<?> getClass(Class<?> cl, ArrayList<Class<?>> implementationClasses)
            throws ImplementationNotFoundException {
        for (Class<?> implClass : implementationClasses) {
            if (cl.isAssignableFrom(implClass)) {
                return implClass;
            }
        }
        throw new ImplementationNotFoundException();
    }

    public static Object run(Class<?> current, final ArrayList<Class<?>> implementationClasses)
            throws InjectionCycleException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            ImplementationNotFoundException {
        if (classToObject.containsKey(current)) {
            return classToObject.get(current);
        }
        if (classVisited.contains(current)) {
            throw new InjectionCycleException();
        } else {
            classVisited.add(current);
        }
        if (Modifier.isInterface(current.getModifiers()) || Modifier.isAbstract(current.getModifiers())) {
            current = getClass(current, implementationClasses);
        }
        Class<?>[] parameterTypes = current.getConstructors()[0].getParameterTypes();
        if (parameterTypes.length == 0) {
            return current.getConstructors()[0].newInstance();
        }
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(root)) {
                throw new InjectionCycleException();
            }
            params[i] = run(parameterTypes[i], implementationClasses);
        }
        classToObject.put(current, current.getConstructors()[0].newInstance(params));
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
        final Constructor<?> constructor = getConstructor(clazz);
        final Class<?>[] parametrTypes = constructor.getParameterTypes();
        final ArrayList<Class<?>> implementationClasses = loadImplementationClasses(implementationClassNames);
        checkAmbiguousImplementation(parametrTypes, implementationClasses);
        checkImplementationNotFound(parametrTypes, implementationClasses);
        root = clazz;
        return run(clazz, implementationClasses);
    }
}
