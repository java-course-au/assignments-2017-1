package ru.spbau.mit;

public abstract class Function2<R, T1, T2> {
    public abstract R apply(T1 param1, T2 param2);

    public <S> Function2<S, T1, T2> compose(final Function1<S, ? super R> g) {
        return new Function2<S, T1, T2>() {
            public S apply(T1 param1, T2 param2) {
                return g.apply(Function2.this.apply(param1, param2));
            }
        };
    }

    public Function1<R, T2> bind1(T1 param) {
        return new Bind1<R, T1, T2>(param, this);
    }

    public Function1<R, T1> bind2(T2 param) {
        return new Bind2<R, T1, T2>(param, this);
    }

    public Function1<Function1<R, T2>, T1> curry() {
        return new CurryBinder<R, T1, T2>(this);
    }

    private static class Bind1<R, T1, T2> extends Function1<R, T2> {
        private Function2<R, T1, T2> boundFunc;
        private T1 boundParam;

        Bind1(T1 param, Function2<R, T1, T2> func) {
            boundParam = param;
            boundFunc = func;
        }

        public R apply(T2 param) {
            return boundFunc.apply(boundParam, param);
        }
    }

    private static class Bind2<R, T1, T2> extends Function1<R, T1> {
        private Function2<R, T1, T2> boundFunc;
        private T2 boundParam;

        Bind2(T2 param, Function2<R, T1, T2> func) {
            boundParam = param;
            boundFunc = func;
        }

        public R apply(T1 param) {
            return boundFunc.apply(param, boundParam);
        }
    }

    private static class CurryBinder<R, T1, T2> extends Function1<Function1<R, T2>, T1> {
        private Function2<R, T1, T2> curryFunc;

        CurryBinder(Function2<R, T1, T2> func) {
            curryFunc = func;
        }

        public Function1<R, T2> apply(T1 param) {
            return curryFunc.bind1(param);
        }
    }
}
