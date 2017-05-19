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
        Set<String> roots = new HashSet<>();

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
            initializeChildren(initialized, realisations.get(0), implementationClassNames, roots);
            parameters.add(initialized.get(realisations.get(0)));
        }

        return rootConstructor.newInstance(parameters.toArray());
    }

    private static void initializeChildren(Map<String, Object> initialized,
                                           String rootClassName,
                                           List<String> implementationClassNames,
                                           Set<String> roots) throws Exception {

        ClassLoader loader = Injector.class.getClassLoader();
        roots.add(rootClassName);
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
            if (roots.contains(realisations.get(0))) {
                throw new InjectionCycleException();
            }

            initializeChildren(initialized, realisations.get(0), implementationClassNames, roots);
            parameters.add(initialized.get(realisations.get(0)));
            roots.remove(realisations.get(0));
        }

        initialized.putIfAbsent(rootClassName, rootConstructor.newInstance(parameters.toArray()));
    }
}
