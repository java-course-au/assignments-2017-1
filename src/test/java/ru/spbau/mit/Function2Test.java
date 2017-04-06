package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.spbau.mit.Function1Test.*;

public class Function2Test {
    static final int VALUE1 = 3;
    static final int VALUE2 = 8;

    private static final Function2<A, Integer, B> SUM_FUNC = new Function2<A, Integer, B>() {
        public A apply(Integer param1, B param2) {
            return new A(param1 + param2.get());
        }
    };

    @Test
    public void testApply() {
        int resValue = VALUE1 + VALUE2;
        assertTrue(SUM_FUNC.apply(VALUE1, new C(VALUE2)).get() == resValue);
    }

    @Test
    public void testCompose() {
        int resValue = 2 * (VALUE1 + VALUE2);
        Function2<B, Integer, B> func = SUM_FUNC.compose(DOUBLE_FUNC);
        assertTrue(func.apply(VALUE1, new C(VALUE2)).get() == resValue);
    }

    @Test
    public void testBind() {
        int resValue = VALUE1 + VALUE2;
        assertTrue(SUM_FUNC.bind1(VALUE1).apply(new C(VALUE2)).get() == resValue);
        assertTrue(SUM_FUNC.bind2(new C(VALUE2)).apply(VALUE1).get() == resValue);
    }

    @Test
    public void testCurry() {
        int resValue = VALUE1 + VALUE2;
        assertTrue(SUM_FUNC.curry().apply(VALUE1).apply(new C(VALUE2)).get() == resValue);
    }
}
