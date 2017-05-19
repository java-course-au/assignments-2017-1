package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;


public final class Injector {
    private Injector() {
    }

    private static class Graph {
        private HashMap<String, Object> classToObj = new HashMap<>();
        private List<String> implementationClassNames;
        private String rootClassName;

        Graph(String rootClassName, List<String> implementationClassName) {
            this.rootClassName = rootClassName;
            this.implementationClassNames = implementationClassName;
        }

        Object dfs(String curClassName) throws ImplementationNotFoundException,
                AmbiguousImplementationException, InjectionCycleException {
            if (classToObj.containsKey(curClassName)) {
                if (classToObj.get(curClassName) == null) {
                    throw new InjectionCycleException();
                }
                return classToObj.get(curClassName);
            }
            classToObj.put(curClassName, null);

            Class<?> curClass = null;
            try {
                curClass = Class.forName(curClassName);
            } catch (ClassNotFoundException e) {
                throw new ImplementationNotFoundException();
            }
            if (curClass.isInterface() || Modifier.isAbstract(curClass.getModifiers())) {
                int index = -1;
                int count = 0;
                for (int i = 0; i < implementationClassNames.size(); i++) {
                    try {
                        if (curClass.isAssignableFrom(
                                Class.forName(implementationClassNames.get(i)))) {
                            index = i;
                            ++count;
                            if (count > 1) {
                                throw new AmbiguousImplementationException();
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        throw new ImplementationNotFoundException();
                    }
                }
                if (index == -1) {
                    throw new ImplementationNotFoundException();
                }
                Object obj = null;
                try {
                    obj = dfs(implementationClassNames.get(index));
                } catch (InjectionCycleException
                        | ImplementationNotFoundException
                        | AmbiguousImplementationException e) {
                    throw e;
                }
                classToObj.put(curClassName, obj);
                return obj;
            }

            if (!curClassName.equals(rootClassName) && !implementationClassNames.contains(curClassName)) {
                throw new ImplementationNotFoundException();
            }

            Constructor constructor = curClass.getConstructors()[0];
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length == 0) {
                Object obj;
                try {
                    obj = constructor.newInstance();
                } catch (InstantiationException
                        | IllegalAccessException
                        | InvocationTargetException e) {
                    throw new ImplementationNotFoundException();
                }
                classToObj.put(curClassName, obj);
                return obj;
            }

            Object[] objects = new Object[types.length];
            int i = 0;
            for (Class<?> type: types) {
                try {
                    objects[i] = dfs(type.getCanonicalName());
                } catch (ImplementationNotFoundException | AmbiguousImplementationException
                        | InjectionCycleException e) {
                    throw e;
                }
                ++i;
            }

            Object obj;
            try {
                obj = constructor.newInstance(objects);
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                throw new ImplementationNotFoundException();
            }

            classToObj.put(curClassName, obj);
            return obj;
        }
    }
    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Graph graph = new Graph(rootClassName, implementationClassNames);
        return graph.dfs(rootClassName);
    }
}
