package ru.spbau.mit;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class FunctionsAndCollectionsTest {
    private static <T> Function2<T, List<T>, List<T>> getListAppender() {
        return new Function2<T, List<T>, List<T>>() {
            @Override
            public List<T> apply(T firstArg, List<T> secondArg) {
                List<T> res = new ArrayList<>();
                res.add(firstArg);
                res.addAll(secondArg);
                return res;
            }
        };
    }

    @Test
    public void foldFact() {
        // factorial n = fold{r|l} (*) 1 [1..n]
        Collection<BigDecimal> ints = new ArrayDeque<>();
        for (int i = 1; i <= 100; i++) {
            ints.add(BigDecimal.valueOf(i));
        }
        Function2<BigDecimal, BigDecimal, BigDecimal> prod = new Function2<BigDecimal, BigDecimal, BigDecimal>() {
            @Override
            public BigDecimal apply(BigDecimal firstArg, BigDecimal secondArg) {
                return firstArg.multiply(secondArg);
            }
        };

        BigDecimal resL = Collections.foldr(prod, BigDecimal.ONE, ints);
        BigDecimal resR = Collections.foldl(prod, BigDecimal.ONE, ints);

        assertEquals(resL, resR);
        assertEquals("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000",
                resL.toString());
    }

    @Test
    public void foldlCovariant() {
        Collection<Integer> ints = new ArrayList<>();
        for (int i = 50; i >= 1; i--) {
            ints.add(i);
        }

        Function2<Object, Object, Integer> hashCodeSecondArg = new Function2<Object, Object, Integer>() {
            @Override
            public Integer apply(Object firstArg, Object secondArg) {
                return secondArg.hashCode();
            }
        };

        Integer firstHashCode = Collections.foldl(hashCodeSecondArg, 12345, ints);
        assertEquals(firstHashCode.intValue(), Integer.valueOf(1).hashCode());
    }

    @Test
    public void funWithStrings() {
        Collection<String> strings = new LinkedHashSet<>();
        String zimin = "";
        for (int i = 1; i <= 10; i++) {
            char newChar = (char) ((int) 'a' + i - 1);
            zimin = zimin + newChar + zimin;
            strings.add(zimin);
        }

        Function1<String, Integer> funLength = new Function1<String, Integer>() {
            @Override
            public Integer apply(String obj) {
                return obj.length();
            }
        };

        Function1<String, String> funUppercase = new Function1<String, String>() {
            @Override
            public String apply(String obj) {
                return obj.toUpperCase();
            }
        };

        Function1<String, Integer> funToUppercaseLength = funUppercase.compose(funLength);

        ArrayList<Integer> lengths = new ArrayList<>();
        lengths.addAll(Collections.map(funToUppercaseLength, strings));
        for (int i = 1; i <= 10; i++) {
            assertEquals((1 << i) - 1, lengths.get(i - 1).intValue());
        }

        Predicate<CharSequence> lengthGreater = new Predicate<CharSequence>() {
            @Override
            public boolean apply(CharSequence obj) {
                return obj.length() > 3;
            }
        };

        Collection<String> filtered = Collections.filter(lengthGreater, strings);
        assertEquals(8, filtered.size());

        Collection<String> takenWhile = Collections.takeWhile(lengthGreater, strings);
        assertEquals(0, takenWhile.size());

        Collection<String> takenUnless = Collections.takeUnless(lengthGreater, strings);
        assertEquals(2, takenUnless.size());
    }

    @Test
    public void predicateCombination() {
        List<Integer> ints = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ints.add(i);
        }

        Predicate<Integer> odd = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer obj) {
                return obj % 2 == 1;
            }
        };

        Predicate<Number> valGreaterTen = new Predicate<Number>() {
            @Override
            public boolean apply(Number obj) {
                return obj.intValue() > 10;
            }
        };

        Predicate<Integer> oddGreaterTen = odd.and(valGreaterTen);

        assertEquals(5, Collections.filter(oddGreaterTen, ints).size());
        assertEquals(5, Collections.filter(oddGreaterTen.and(Predicate.ALWAYS_TRUE), ints).size());
        assertEquals(0, Collections.filter(oddGreaterTen.and(Predicate.ALWAYS_FALSE), ints).size());
        assertEquals(20, Collections.filter(oddGreaterTen.or(Predicate.ALWAYS_TRUE), ints).size());
        assertEquals(5, Collections.filter(oddGreaterTen.or(Predicate.ALWAYS_FALSE), ints).size());
    }

    @Test
    public void lazyPreds() {
        Collection<Integer> ints = new ArrayList<>();
        ints.add(512);
        ints.add(-666);

        Predicate<Integer> thrower = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer obj) {
                throw new IllegalArgumentException();
            }
        };

        assertEquals(2, Collections.filter(Predicate.ALWAYS_TRUE.or(thrower), ints).size());
        assertEquals(0, Collections.filter(Predicate.ALWAYS_FALSE.and(thrower), ints).size());
    }

    @Test
    public void bind12() {
        Function2<Integer, Integer, Integer> subtracter = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer firstArg, Integer secondArg) {
                return firstArg - secondArg;
            }
        };

        Function1<Integer, Integer> minusHundred = subtracter.bind2(100);
        Function1<Integer, Integer> hundredMinus = subtracter.bind1(100);

        assertEquals(5, subtracter.apply(15, 10).intValue());
        assertEquals(430, minusHundred.apply(530).intValue());
        assertEquals(-430, hundredMinus.apply(530).intValue());
    }

    @Test
    public void curryComposeFunction2() {
        Function1<Integer, Integer> negator = new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer obj) {
                return obj * -1;
            }
        };

        Function2<Integer, Integer, Integer> multiplier = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer firstArg, Integer secondArg) {
                return firstArg * secondArg;
            }
        };

        Function2<Integer, Integer, Integer> negMult = multiplier.compose(negator);

        assertEquals(-6, negMult.apply(2, 3).intValue());
        assertEquals(6, negMult.apply(-2, 3).intValue());
        assertEquals(6, negMult.apply(2, -3).intValue());

        Function1<Integer, Function1<Integer, Integer>> curried = negMult.curry();
        assertEquals(-6, curried.apply(2).apply(3).intValue());
        assertEquals(6, curried.apply(-2).apply(3).intValue());
    }
}
