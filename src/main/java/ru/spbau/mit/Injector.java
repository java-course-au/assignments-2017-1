package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;


public final class Injector {
    private static List<Class> implementationClasses;
    private static HashSet<Class> visited;
    private static HashMap<Class, Object> implementations = new LinkedHashMap<>();

    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName,
                                    List<String> implementationClassNames)
            throws Exception {
        implementationClasses = new ArrayList<>(implementationClassNames.size());

        for (String className : implementationClassNames) {
            implementationClasses.add(Class.forName(className));
        }

        Class<?> rootClass = Class.forName(rootClassName);
        implementationClasses.add(rootClass);

        visited = new LinkedHashSet<>();

        return initializeImpl(rootClass);
    }

    private static Object initializeImpl(Class<?> clazz) throws Exception {
        visited.add(clazz);

        Constructor constructor = clazz.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] parameterImpls = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class<?> implClass = getSuitableImplementation(parameters[i].getType());

            if (visited.contains(implClass) && !implementations.containsKey(implClass)) {
                throw new InjectionCycleException();
            }

            if (implementations.containsKey(implClass)) {
                parameterImpls[i] = implementations.get(implClass);
            } else {
                parameterImpls[i] = initializeImpl(implClass);
            }
        }

        Object impl = constructor.newInstance(parameterImpls);
        implementations.put(clazz, impl);

        return impl;
    }

    private static Class<?> getSuitableImplementation(Class<?> clazz)
            throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> suitableClass = null;

        for (Class<?> curClass : implementationClasses) {
            if (clazz.isAssignableFrom(curClass)) {
                if (suitableClass != null) {
                    throw new AmbiguousImplementationException();
                }

                suitableClass = curClass;
            }
        }

        if (suitableClass == null) {
            throw new ImplementationNotFoundException();
        }

        return suitableClass;
    }
}
