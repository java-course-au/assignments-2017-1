package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        if (!implementationClassNames.contains(rootClassName)) {
            implementationClassNames = new ArrayList<>(implementationClassNames);
            implementationClassNames.add(rootClassName);
        }
        return doGenerate(rootClassName, implementationClassNames, new HashSet<String>(),
                new HashMap<String, Object>());
    }

    private static Object doGenerate(String rootClassName, List<String> impls, Set<String> visited,
                                     Map<String, Object> created) throws Exception {
        if (visited.contains(rootClassName)) {
            throw new InjectionCycleException();
        }
        if (created.containsKey(rootClassName)) {
            return created.get(rootClassName);
        }
        visited.add(rootClassName);
        Class<?> root = Class.forName(rootClassName);
        Constructor<?>[] constructors = root.getConstructors();
        if (constructors.length != 1) {
            throw new AssertionError("More than one constructor in class " + rootClassName);
        }
        Constructor<?> constr = constructors[0];
        Class<?>[] params = constr.getParameterTypes();
        if (params.length == 0) {
            created.put(rootClassName, constr.newInstance());
        } else {
            Object[] paramObjects = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                Class<?> param = params[i];
                String appImpl = null;
                for (String impl : impls) {
                    if (param.isAssignableFrom(Class.forName(impl))) {
                        if (appImpl != null) {
                            throw new AmbiguousImplementationException();
                        }
                        appImpl = impl;
                    }
                }
                if (appImpl == null) {
                    throw new ImplementationNotFoundException();
                }
                paramObjects[i] = doGenerate(appImpl, impls, visited, created);
            }
            created.put(rootClassName, constr.newInstance(paramObjects));
        }

        visited.remove(rootClassName);
        return created.get(rootClassName);
    }
}
