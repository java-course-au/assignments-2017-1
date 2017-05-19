package ru.spbau.mit;

import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
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
//            if (!this.implementationClassNames.contains(rootClassName)) {
//                this.implementationClassNames.add(rootClassName);
//            }
        }

        Object dfs(String curClassName) throws ImplementationNotFoundException,
                AmbiguousImplementationException,
                InjectionCycleException,
                IllegalAccessException,
                InvocationTargetException,
                InstantiationException,
                ClassNotFoundException {

            if (classToObj.containsKey(curClassName)) {
                if (classToObj.get(curClassName) == null) {
                    throw new InjectionCycleException();
                }
                return classToObj.get(curClassName);
            }
            classToObj.put(curClassName, null);

            Class<?> curClass = Class.forName(curClassName);

            if (!curClassName.equals(rootClassName) &&
                    !implementationClassNames.contains(curClassName)) {
                int index = -1;
                int count = 0;
                for (int i = 0; i < implementationClassNames.size(); i++) {
                    if (curClass.isAssignableFrom(
                            Class.forName(implementationClassNames.get(i)))) {
                        index = i;
                        ++count;
                        if (count > 1) {
                            throw new AmbiguousImplementationException();
                        }
                    }
                }
                if (index == -1) {
                    throw new ImplementationNotFoundException();
                }
                Object obj = dfs(implementationClassNames.get(index));
                classToObj.put(curClassName, obj);
                return obj;
            }

//            if (!curClassName.equals(rootClassName) && !implementationClassNames.contains(curClassName)) {
//                throw new ImplementationNotFoundException();
//            }

            Constructor constructor = curClass.getDeclaredConstructors()[0];
            Class<?>[] types = constructor.getParameterTypes();
            if (types.length == 0) {
                Object obj;
                obj = constructor.newInstance();
                classToObj.put(curClassName, obj);
                return obj;
            }

            Object[] objects = new Object[types.length];
            int i = 0;
            for (Class<?> type: types) {
                objects[i] = dfs(type.getCanonicalName());
                ++i;
            }

            Object obj = constructor.newInstance(objects);
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
