package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


public final class Injector {
    private static Map<String, Object> alreadyCreatedObjects;
    private static Set<String> enqueued;
//    private static Map<String, Constructor> usedConstructors;
//    private static Set<Injector> initialized;

//    private static Set<Object> alreadyCreatedObjects;
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        initializeStructures();
        Object object = initialize0(rootClassName, implementationClassNames);
        deinitializeStructures();
        return object;
    }

    private static void initializeStructures() {
        if (Objects.isNull(alreadyCreatedObjects)) {
            assert Objects.isNull(enqueued);
            alreadyCreatedObjects = new HashMap<>();
            enqueued = new HashSet<>();
        }
        if (!alreadyCreatedObjects.isEmpty() || !enqueued.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private static void deinitializeStructures() {
        alreadyCreatedObjects.clear();
        enqueued.clear();
    }

    private static Object initialize0(String rootClassName, List<String> implementationClassNames)
            throws ImplementationNotFoundException,
            ClassNotFoundException,
            AmbiguousImplementationException {
        if (alreadyCreatedObjects.containsKey(rootClassName)) {
            return alreadyCreatedObjects.get(rootClassName);
        }
        if (enqueued.contains(rootClassName)) {
            return new InjectionCycleException();
        }
        enqueued.add(rootClassName);

        Class rootClass = Class.forName(rootClassName);
        Constructor rootConstructor = rootClass.getConstructors()[0];
        List<Object> args = new ArrayList<>();
        for (Class dependencyClass : rootConstructor.getParameterTypes()) {
            List<String> allowedClasses = implementationClassNames
                    .stream()
                    .filter((implName) -> {
                        try {
                            Class implementationClass = Class.forName(implName);
                            return dependencyClass.isAssignableFrom(implementationClass);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException();
                        }
                    })
                    .collect(Collectors.toList());
            if (allowedClasses.size() > 1) {
                throw new AmbiguousImplementationException();
            } else if (allowedClasses.isEmpty()) {
                throw new ImplementationNotFoundException();
            } else {
                String allowedClass = allowedClasses.get(0);
                Object arg = initialize0(allowedClass, implementationClassNames);
                args.add(arg);
            }
        }

        Object rootObject;
        try {
            rootObject = rootConstructor.newInstance(args.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException();
        }
        enqueued.remove(rootClassName);
        alreadyCreatedObjects.put(rootClassName, rootObject);
        return rootObject;
    }
}
