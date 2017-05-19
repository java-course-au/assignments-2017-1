package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Injector {

    private static Map<String, Object> intance;
    private static List<String> load;
    private static List<String> implementationClassNames;

    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        clean();
        Injector.implementationClassNames = implementationClassNames;
        load.add(rootClassName);
        return find(rootClassName, false);
    }

    private static void clean() {
        intance = new HashMap<>();
//        load = new ArrayList<>();
        claenLoad();
    }

    private static void claenLoad() {
        load = new ArrayList<>();
    }

    private static Object initializeClass(String rootClassName) throws Exception {
        if (intance.containsKey(rootClassName)) {
            return intance.get(rootClassName);
        }

        if (load.contains(rootClassName)) {
            throw new InjectionCycleException();
        }
        load.add(rootClassName);
        return find(rootClassName, true);
    }

    private static Object find(String rootClassName, boolean flag) throws Exception {
        Class aClass = Class.forName(rootClassName);

        Constructor[] declaredConstructors = aClass.getConstructors();
        Constructor constructor = declaredConstructors[0];
        Parameter[] parameters = constructor.getParameters();

        Object[] values = new Object[parameters.length];
        for (int idx = 0; idx < parameters.length; idx++) {
            Parameter parameter = parameters[idx];
            String name = parameter.getType().getName();
            Class saveClass = null;
            for (String implementationClassName : implementationClassNames) {
                Class bClass = Class.forName(implementationClassName);
                if (parameter.getType().isAssignableFrom(bClass)) {
                    if (saveClass != null) {
                        throw new AmbiguousImplementationException();
                    }
                    saveClass = bClass;
                }
            }
            if (saveClass == null) {
                if (parameter.getType().isAssignableFrom(Class.forName(rootClassName))) {
                    throw new InjectionCycleException();
                }
                throw new InjectionCycleException();
            }

            values[idx] = initializeClass(saveClass.getName());
        }

        Object instance = constructor.newInstance(values);
        if (flag) {
            intance.put(rootClassName, instance);
        }
        return instance;
    }
}
