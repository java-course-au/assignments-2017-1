package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    static class A {
        private int a;

        A(int value) {
            a = value;
        }

        int get() {
            return a;
        }
    }

    static class B extends A {
        B(int value) {
            super(value);
        }
    }

    static class C extends B {
        C(int value) {
            super(value);
        }
    }

    static final int VALUE = 13;

    static final Function1<B, A> INC_FUNC = new Function1<B, A>() {
        public A apply(B param) {
            return new B(param.get() + 1);
        }
    };

    static final Function1<A, B> DOUBLE_FUNC = new Function1<A, B>() {
        public B apply(A param) {
            return new B(param.get() * 2);
        }
    };

    @Test
    public void testApply() {
        A a = INC_FUNC.apply(new C(VALUE));
        assertTrue(a.a == VALUE + 1);
    }

    @Test
    public void testCompose() {
        Function1<B, B> incDoubleFunc = INC_FUNC.compose(DOUBLE_FUNC);
        Function1<A, A> doubleIncFunc = DOUBLE_FUNC.compose(INC_FUNC);

        assertTrue(incDoubleFunc.apply(new C(VALUE)).get() == (VALUE + 1) * 2);
        assertTrue(doubleIncFunc.apply(new C(VALUE)).get() == VALUE * 2 + 1);
    }
}
