package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;


public final class Injector {
    private Injector() {
    }

    private static Map<Class, Object> alreadyConstructed;

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {

        //todo каждый конструктор можно вызывать только один раз. Надо передавать один и тот же объект

        final ClassLoader loader = ClassLoader.getSystemClassLoader();
        alreadyConstructed = new HashMap<>();

        //load target
        Class rootClass;
        try {
            rootClass = loader.loadClass(rootClassName);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException();
        }


        //load dependencies
        List<Class> implementationClasses = new ArrayList<>();
        for (String name : implementationClassNames) {
            Class dep;
            try {
                dep = loader.loadClass(name);
                implementationClasses.add(dep);
            } catch (ClassNotFoundException e) {
                throw new ImplementationNotFoundException();
            }
        }

        List<Class> alreadyUsed = new ArrayList<>();
        alreadyUsed.add(rootClass);
        return runInitialize(rootClass, implementationClasses, alreadyUsed);
    }

    private static Object runInitialize(Class root, List<Class> implementationClasses, List<Class> alreadyUsed) throws Exception {
        //get constructor
        Constructor[] constructors = root.getDeclaredConstructors();
        Constructor theOnlyOne = constructors[0];

        //run constructor
        return runConstructor(theOnlyOne, implementationClasses, alreadyUsed);
    }

    private static Object runConstructor(Constructor constructor, List<Class> implementationClasses, List<Class> alreadyUsed) throws Exception {
        Class[] params = constructor.getParameterTypes();

        List<Object> implementations = new ArrayList<>();
        for (Class parameter : params) {

            if (alreadyUsed.contains(parameter)) {
                throw new InjectionCycleException();
            }

            if (alreadyConstructed.containsKey(parameter)) {
                return alreadyConstructed.get(parameter);
            }


            List<Class> candidates = getCandidates(parameter, implementationClasses);
            if (candidates.size() == 0) {
                throw new ImplementationNotFoundException();
            }

            if (candidates.size() > 1) {
                throw new AmbiguousImplementationException();
            }


            List<Class> newAlreadyUsed = new ArrayList<>();
            newAlreadyUsed.addAll(alreadyUsed);
            newAlreadyUsed.add(parameter);
            Object parameterImpl = runInitialize(candidates.get(0), implementationClasses, newAlreadyUsed);

            alreadyConstructed.put(parameter, parameterImpl);

            implementations.add(parameterImpl);
        }

        Object result = constructor.newInstance(implementations.toArray());
        return result;
    }

    private static List<Class> getCandidates(Class parameter, List<Class> implementationClasses) {
        List<Class> candidates = new ArrayList<>();
        for (Class impl : implementationClasses) {
            if (parameter.isAssignableFrom(impl)) {
                candidates.add(impl);
            }
        }

        return candidates;
    }
}
