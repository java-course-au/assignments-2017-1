package ru.spbau.mit;

public abstract class Function2<T1, T2, R> {
    public abstract R apply(T1 param1, T2 param2);

    public <S> Function2<T1, T2, S> compose(final Function1<? super R, S> g) {
        return new Function2<T1, T2, S>() {
            public S apply(T1 param1, T2 param2) {
                return g.apply(Function2.this.apply(param1, param2));
            }
        };
    }

    public Function1<T2, R> bind1(T1 param) {
        return new Bind1<R, T1, T2>(param, this);
    }

    public Function1<T1, R> bind2(T2 param) {
        return new Bind2<R, T1, T2>(param, this);
    }

    public Function1<T1, Function1<T2, R>> curry() {
        return new CurryBinder<R, T1, T2>(this);
    }

    private static class Bind1<R, T1, T2> extends Function1<T2, R> {
        private Function2<T1, T2, R> boundFunc;
        private T1 boundParam;

        Bind1(T1 param, Function2<T1, T2, R> func) {
            boundParam = param;
            boundFunc = func;
        }

        public R apply(T2 param) {
            return boundFunc.apply(boundParam, param);
        }
    }

    private static class Bind2<R, T1, T2> extends Function1<T1, R> {
        private Function2<T1, T2, R> boundFunc;
        private T2 boundParam;

        Bind2(T2 param, Function2<T1, T2, R> func) {
            boundParam = param;
            boundFunc = func;
        }

        public R apply(T1 param) {
            return boundFunc.apply(param, boundParam);
        }
    }

    private static class CurryBinder<R, T1, T2> extends Function1<T1, Function1<T2, R>> {
        private Function2<T1, T2, R> curryFunc;

        CurryBinder(Function2<T1, T2, R> func) {
            curryFunc = func;
        }

        public Function1<T2, R> apply(T1 param) {
            return curryFunc.bind1(param);
        }
    }
}
