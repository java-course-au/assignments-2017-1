package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public final class Injector {
    private Class root;
    private List<Class> imps;
    private HashMap<Class, Object> hs = new HashMap<>();

    private Injector(String rootn, List<String> impsn) throws ImplementationNotFoundException {
        ClassLoader loader = this.getClass().getClassLoader();
        try {
            root = loader.loadClass(rootn);
            imps = new ArrayList<>();
            for (String cl : impsn) {
                imps.add(loader.loadClass(cl));
            }
            imps.add(root);
        } catch (ClassNotFoundException e) {
            throw new ImplementationNotFoundException();
        }
    }

    private Class find(Class trg, Class dep) throws Exception {
        List<Class> cls = imps.stream().filter(x -> trg.isAssignableFrom(x)).collect(Collectors.toList());
        if (cls.size() == 0) {
            throw new ImplementationNotFoundException();
        } else if (cls.size() != 1) {
            throw new AmbiguousImplementationException();
        } else if (cls.get(0) == dep) {
            throw new InjectionCycleException();
        } else {
            return cls.get(0);
        }
    }

    private Object subs(Class trg, Class dep) throws Exception {
        Constructor ctr = trg.getConstructors()[0];
        Class[] paramst = ctr.getParameterTypes();
        List<Object> params = new ArrayList<>();

        for (Class pt : paramst) {
            Class cl = find(pt, dep);
            if (hs.containsKey(cl)) {
                params.add(hs.get(cl));
            } else {
                Object obj = subs(cl, dep);
                params.add(obj);
                hs.put(cl, obj);
            }
        }

        try {
            return ctr.newInstance(params.toArray());
        } catch (Exception e) {
            throw new ImplementationNotFoundException();
        }
    };

    private Object get() throws Exception {
        return subs(root, root);
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Injector injector = new Injector(rootClassName, implementationClassNames);
        return injector.get();
    }
}
