package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ru.spbau.mit.FunctionsForTesting.*;

public class TestAll {


    @Test
    public void testFunction1() {
        List<Integer> integerList = new ArrayList<>();
        List<Integer> succRes = new ArrayList<>();
        List<Integer> mult2Res = new ArrayList<>();
        List<Integer> compSuccMultRes = new ArrayList<>();
        List<Integer> compMultSuccRes = new ArrayList<>();
        for (int i = 0; i < SIZE; ++i) {
            integerList.add(i);
            succRes.add(i + 1);
            mult2Res.add(i * 2);
            compSuccMultRes.add((i + 1) * 2);
            compMultSuccRes.add((i * 2) + 1);
        }
        Iterable<Integer> res = Collections.map(succ(), integerList);
        Assert.assertTrue(compareCollections(succRes, res));

        res = Collections.map(mult2(), integerList);
        Assert.assertTrue(compareCollections(mult2Res, res));

        res = Collections.map(succ().compose(mult2()), integerList);
        Assert.assertTrue(compareCollections(compSuccMultRes, res));

        res = Collections.map(mult2().compose(succ()), integerList);
        Assert.assertTrue(compareCollections(compMultSuccRes, res));
    }

    @Test
    public void testFunction2() {
        List<Integer> sum2Res = new ArrayList<>();
        List<Integer> mult2Res = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        List<Integer> compSumMult2 = new ArrayList<>();
        int summa = 0;
        for (int i = 0; i < SIZE; ++i) {
            integerList.add(i);
            sum2Res.add(i + 2);
            mult2Res.add(i * 2);
            compSumMult2.add((i + 2) * 2);
            summa += i;
        }

        Iterable<Integer> mapped = Collections.map(sum().bind1(2), integerList);
        Assert.assertTrue(compareCollections(sum2Res, mapped));

        mapped = Collections.map(mult().bind2(2), integerList);
        Assert.assertTrue(compareCollections(mult2Res, mapped));

        mapped = Collections.map(sum().curry().apply(2), integerList);
        Assert.assertTrue(compareCollections(sum2Res, mapped));

        mapped = Collections.map(sum().compose(mult2()).bind1(2), integerList);
        Assert.assertTrue(compareCollections(compSumMult2, mapped));

        Integer foldSumRes = summa;
        Integer res = Collections.foldl(sum(), 0, integerList);
        Assert.assertEquals(foldSumRes, res);

        res = Collections.foldr(sum(), 1, integerList);
        foldSumRes += 1;
        Assert.assertEquals(foldSumRes, res);

    }

    @Test
    public void testPredicate() {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < SIZE; ++i) {
            integerList.add(i);
        }

        Iterable<Integer> res = Collections.takeWhile(Predicate.ALWAYS_TRUE, integerList);
        Assert.assertEquals(integerList.size(), size(res));

        res = Collections.takeUnless(Predicate.ALWAYS_FALSE, integerList);
        Assert.assertEquals(integerList.size(), size(res));

        for (int i = 0; i < SIZE; ++i) {
            res = Collections.filter(greaterThan(i), integerList);
            Assert.assertEquals(SIZE - i - 1, size(res));

            res = Collections.takeWhile(lessThan(i), integerList);
            Assert.assertEquals(i, size(res));

            res = Collections.takeUnless(greaterThan(i), integerList);
            Assert.assertEquals(i + 1, size(res));

            res = Collections.filter(greaterThan(i).not(), integerList);
            Assert.assertEquals(i + 1, size(res));

            res = Collections.filter(greaterThan(i).and(lessThan(SIZE)), integerList);
            Assert.assertEquals(SIZE - i - 1, size(res));

            res = Collections.filter(lessThan(i).or(greaterThan(i)), integerList);
            Assert.assertEquals(SIZE - 1, size(res));
        }

    }

    @Test
    public void testGenerics() {
        Function2<String, Number, Number> numToStr = new Function2<String, Number, Number>() {
            @Override
            String apply(Number num1, Number num2) {
                return num1.toString() + num2.toString();
            }
        };

        Function1<Integer, String> len = new Function1<Integer, String>() {
            @Override
            Integer apply(String s) {
                return s.length();
            }
        };

        Assert.assertEquals("11".length(),
                (int) numToStr.compose(len).apply(1, 1));
        Assert.assertEquals("hello".length() + 1,
                (int) len.compose(succ()).apply("hello"));
        Assert.assertEquals("11".length(),
                (int) numToStr.bind1(1).compose(len).apply(1));
    }
}
