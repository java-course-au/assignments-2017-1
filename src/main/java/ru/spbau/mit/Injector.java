package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(
            final String rootClassName,
            final List<String> implementationClassNames
    ) throws Exception {
        final Class<?> rootClass = Class.forName(rootClassName);
        final Set<Class<?>> implementationClasses = new HashSet<>();
        for (String implementationClass : implementationClassNames) {
            final Class<?> clazz = Class.forName(implementationClass);
            implementationClasses.add(clazz);
        }
        return newInstance(rootClass, implementationClasses, new HashSet<>(), new HashSet<>());
    }

    private static Object newInstance(
            final Class<?> clazz,
            final Set<Class<?>> implementationClasses,
            final Set<Class<?>> dependencies,
            final Set<Object> cache
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException,
            AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException {
        for (Class<?> dependency : dependencies) {
            if (clazz.isAssignableFrom(dependency)) {
                throw new InjectionCycleException();
            }
        }
        dependencies.add(clazz);

        for (Object impl : cache) {
            if (clazz.isAssignableFrom(impl.getClass())) {
                return impl;
            }
        }

        final Constructor<?> constructor = clazz.getConstructors()[0];
        final List<Object> initargs = new ArrayList<>();
        for (Class<?> paramClazz : constructor.getParameterTypes()) {
            if (clazz.isAssignableFrom(paramClazz) || paramClazz.isAssignableFrom(clazz)) {
                throw new InjectionCycleException();
            }

            final Object param = implementationClasses.contains(paramClazz)
                    ? newInstance(paramClazz, implementationClasses, dependencies, cache)
                    : assignableNewInstance(paramClazz, implementationClasses, dependencies, cache);
            cache.add(param);
            initargs.add(param);
        }

        final Object instance = constructor.newInstance(initargs.toArray());
        cache.add(instance);
        dependencies.remove(clazz);
        return instance;
    }

    private static Object assignableNewInstance(
            final Class<?> clazz,
            final Set<Class<?>> implementationClasses,
            final Set<Class<?>> dependencies,
            final Set<Object> cache
    ) throws IllegalAccessException, InvocationTargetException, InstantiationException,
            AmbiguousImplementationException, ImplementationNotFoundException, InjectionCycleException {
        Class<?> currentClassImpl = null;
        for (Class<?> classImpl : implementationClasses) {
            if (clazz.isAssignableFrom(classImpl)) {
                if (currentClassImpl == null) {
                    currentClassImpl = classImpl;
                } else {
                    throw new AmbiguousImplementationException();
                }
            }
        }

        if (currentClassImpl == null) {
            throw new ImplementationNotFoundException();
        }

        return newInstance(currentClassImpl, implementationClasses, dependencies, cache);
    }
}
