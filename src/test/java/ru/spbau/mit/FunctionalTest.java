package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalTest {
    private static final Integer MAX_GEN_NUMBER = 100;

    private static final Function1<Object, Object> ID =
            new Function1<Object, Object>() {
                @Override
                Object apply(Object x) {
                    return x;
                }
            };

    private static final Function1<Integer, Integer> SQR =
            new Function1<Integer, Integer>() {
                @Override
                Integer apply(Integer integer) {
                    return integer * integer;
                }
            };

    private static final Function1<Number, Number> SQRT =
            new Function1<Number, Number>() {
                @Override
                Number apply(Number number) {
                    return Math.sqrt(number.doubleValue());
                }
            };

    private static final Function2<Integer, Integer, Integer> INTEGER_SUM =
            new Function2<Integer, Integer, Integer>() {
                @Override
                Integer apply(Integer x, Integer y) {
                    return x + y;
                }
            };

    private static final Function2<Number, Number, Number> SUM =
            new Function2<Number, Number, Number>() {
                @Override
                Number apply(Number x, Number y) {
                    return x.doubleValue() + y.doubleValue();
                }
            };

    private static final Function2<Integer, Integer, Double> DIVIDE =
            new Function2<Integer, Integer, Double>() {
                @Override
                Double apply(Integer x, Integer y) {
                    return x.doubleValue() / y;
                }
            };

    private static final Predicate<Object> THAT_THROWS = new Predicate<Object>() {
        @Override
        Boolean apply(Object o) {
            throw new RuntimeException("THAT_THROWS predicate was called");
        }
    };

    private Random random = new Random();

    @Test
    public void testSimple() {
        Integer testValue = random.nextInt();
        assertEquals((Integer) (testValue * testValue), SQR.apply(testValue));
    }

    @Test
    public void testGeneralityOfFunction1() {
        Function1<Integer, Object> comp1 = SQR.compose(ID);
        Integer testValue = Math.abs(random.nextInt(MAX_GEN_NUMBER));
        assertEquals(SQR.apply(testValue), comp1.apply(testValue));

        Function1<Integer, Number> comp2 = SQR.compose(SQRT);
        assertEquals(testValue.doubleValue(), comp2.apply(testValue));
    }

    @Test
    public void testGeneralityOfFunction2() {
        Function2<Integer, Integer, Object> comp1 = INTEGER_SUM.compose(ID);
        Integer x1 = Math.abs(random.nextInt(MAX_GEN_NUMBER));
        Integer x2 = Math.abs(random.nextInt(MAX_GEN_NUMBER));
        assertEquals(INTEGER_SUM.apply(x1, x2), comp1.apply(x1, x2));

        Function2<Integer, Integer, Integer> comp2 = INTEGER_SUM.compose(SQR);
        assertEquals((Integer) ((x1 + x2) * (x1 + x2)), comp2.apply(x1, x2));
    }

    @Test
    public void testBind() {
        final int length = 100;
        List<Integer> sequence = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            sequence.add(random.nextInt());
        }

        Function1<Number, Number> sumWithZero1 = SUM.bind1(0);
        Function1<Number, Number> sumWithZero2 = SUM.bind2(0);

        for (Integer element : sequence) {
            assertEquals(element.doubleValue(), sumWithZero1.apply(element));
            assertEquals(sumWithZero1.apply(element),
                    sumWithZero2.apply(element));
        }

        Function1<Integer, Double> divideTwoBy1 = DIVIDE.bind1(2);
        Function1<Integer, Double> divideByTwo2 = DIVIDE.bind2(2);

        for (Integer element : sequence) {
            assertEquals((Double) (2. / element), divideTwoBy1.apply(element));
            assertEquals((Double) (element / 2.), divideByTwo2.apply(element));
        }
    }

    @Test
    public void testCurry() {
        Function2<String, String, String> loggerToString =
                new Function2<String, String, String>() {
                    @Override
                    String apply(String s, String s2) {
                        return s + s2;
                    }
                };

        Function1<String, String> loggerToError =
                loggerToString.curry().apply("ERROR: ");

        assertEquals("ERROR: fatal error",
                loggerToError.apply("fatal error"));

        final int length = 100;
        Integer[] numbers = new Integer[length];
        for (int i = 0; i < length; i++) {
            numbers[i] = random.nextInt();
        }

        for (int i = 0; i < length; i++) {
            String strNumber = numbers[i].toString();
            assertEquals(loggerToString.apply("ERROR: ", strNumber),
                    loggerToError.apply(strNumber));
        }
    }

    @Test
    public void testPredicateAlwaysTrueFalse() {
        assertTrue(Predicate.ALWAYS_TRUE.apply(random.nextInt()));
        assertFalse(Predicate.ALWAYS_FALSE.apply(random.nextInt()));
    }

    @Test
    public void testOr() {
        assertTrue(Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_TRUE).apply(
                random.nextInt()));
        assertTrue(Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE).apply(
                random.nextInt()));
        assertTrue(Predicate.ALWAYS_FALSE.or(Predicate.ALWAYS_TRUE).apply(
                random.nextInt()));
        assertFalse(Predicate.ALWAYS_FALSE.or(Predicate.ALWAYS_FALSE).apply(
                random.nextInt()));
    }

    @Test
    public void testAnd() {
        assertTrue(Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_TRUE).apply(
                random.nextInt()));
        assertFalse(Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_FALSE).apply(
                random.nextInt()));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_TRUE).apply(
                random.nextInt()));
        assertFalse(Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_FALSE).apply(
                random.nextInt()));
    }

    @Test
    public void testNot() {
        assertTrue(Predicate.ALWAYS_FALSE.not().apply(random.nextInt()));
        assertFalse(Predicate.ALWAYS_TRUE.not().apply(random.nextInt()));
    }

    @Test
    public void testLazyAnd() {
        Predicate.ALWAYS_FALSE.and(THAT_THROWS).apply(random.nextInt());
    }

    @Test
    public void testLazyOr() {
        Predicate.ALWAYS_TRUE.or(THAT_THROWS).apply(random.nextInt());
    }
}
