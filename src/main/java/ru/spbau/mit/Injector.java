package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        ClassLoader loader = Injector.class.getClassLoader();
        Map<String, Object> initialized = new HashMap<>();
        Set<String> unInitialized = new HashSet<>();
        unInitialized.add(rootClassName);
        List<String> newImplClassNames = new ArrayList<>(implementationClassNames);
        newImplClassNames.add(rootClassName);

        List<Class<?>> implementations = implementationClassNames.stream().map(s -> {
            try {
                return loader.loadClass(s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        Class root = loader.loadClass(rootClassName);
        Constructor<?> rootConstructor = root.getConstructors()[0];
        Class<?>[] dependencies = rootConstructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();

        for (Class<?> dependency : dependencies) {
            List<String> realisations = implementations.stream().
                    filter(dependency::isAssignableFrom).
                    map(Class::getCanonicalName).
                    collect(Collectors.toList());

            if (realisations.isEmpty()) {
                throw new ImplementationNotFoundException();
            }
            if (realisations.size() > 1) {
                throw new AmbiguousImplementationException();
            }

            initializeChildren(initialized, realisations.get(0), newImplClassNames, unInitialized);
            parameters.add(initialized.get(realisations.get(0)));
        }

        return rootConstructor.newInstance(parameters.toArray());
    }

    private static void initializeChildren(Map<String, Object> initialized,
                                           String initMe,
                                           List<String> implementationClassNames,
                                           Set<String> unInitialised) throws Exception {

        if (initialized.containsKey(initMe)) {
            return;
        }

        ClassLoader loader = Injector.class.getClassLoader();
        unInitialised.add(initMe);

        List<Class<?>> implementations = implementationClassNames.stream().map(s -> {
            try {
                return loader.loadClass(s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        Class root = loader.loadClass(initMe);

        Constructor<?> rootConstructor = root.getConstructors()[0];
        Class<?>[] dependencies = rootConstructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();
        for (Class<?> dependency : dependencies) {
            List<String> realisations = implementations.stream().
                    filter(dependency::isAssignableFrom).
                    map(Class::getCanonicalName).
                    collect(Collectors.toList());

            if (realisations.isEmpty()) {
                throw new ImplementationNotFoundException();
            }
            if (realisations.size() > 1) {
                throw new AmbiguousImplementationException();
            }
            if (unInitialised.contains(realisations.get(0))) {
                throw new InjectionCycleException();
            }

            initializeChildren(initialized, realisations.get(0), implementationClassNames, unInitialised);
            parameters.add(initialized.get(realisations.get(0)));
        }

        unInitialised.remove(initMe);
        initialized.put(initMe, rootConstructor.newInstance(parameters.toArray()));
    }
}
