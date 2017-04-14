package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.spbau.mit.TestUtils.*;

public class TestAll {

    @Test
    public void testFunction1Compose() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> res = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);
        Assert.assertEquals(res, Collections.map(succ(), integerList));

        res = Arrays.asList(4, 6, 8, 10, 12, 14, 16, 18, 20);
        Assert.assertEquals(res, Collections.map(succ().compose(mult2()), integerList));

        res = Arrays.asList(3, 5, 7, 9, 11, 13, 15, 17, 19);
        Assert.assertEquals(res, Collections.map(mult2().compose(succ()), integerList));

        res = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11);
        Assert.assertEquals(res, Collections.map(succ().compose(succ()), integerList));

    }

    @Test
    public void testFunction2Compose() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(45, (int) Collections.foldl(sum(),
                0, integerList));

        Assert.assertEquals(45, (int) Collections.foldr(sum().compose(id()),
                0, integerList));
    }

    @Test
    public void testFunction2Bind1() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> res = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);

        Assert.assertEquals(res, Collections.map(sum().bind1(1), integerList));
        Assert.assertEquals(integerList, Collections.map(mult().bind1(1), integerList));
    }


    @Test
    public void testFunction2Bind2() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> res = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);

        Assert.assertEquals(res, Collections.map(sum().bind2(1), integerList));
        Assert.assertEquals(integerList, Collections.map(mult().bind2(1), integerList));
    }

    @Test
    public void testFunction2Curry() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> res = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);

        Function1<Integer, Function1<Integer, Integer>> curriedSum = sum().curry();

        Assert.assertEquals(res, Collections.map(curriedSum.apply(1), integerList));
        Assert.assertEquals(integerList, Collections.map(curriedSum.apply(0), integerList));
    }


    @Test
    public void testPredicateConst() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(integerList,
                Collections.filter(Predicate.ALWAYS_TRUE, integerList));
        Assert.assertEquals(integerList,
                Collections.takeUnless(Predicate.ALWAYS_FALSE, integerList));
    }

    @Test
    public void testPredicateOr() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(integerList, Collections.filter(
                Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE), integerList));

        Assert.assertEquals(integerList, Collections.filter(
                greaterThan(0).or(lessThan(10)), integerList));

        Assert.assertEquals(integerList, Collections.filter(
                Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE), integerList));
    }

    @Test
    public void testPredicateAnd() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        ArrayList<Object> empty = new ArrayList<>();
        Assert.assertEquals(empty, Collections.filter(
                Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_FALSE), integerList));

        Assert.assertEquals(integerList, Collections.filter(
                greaterThan(0).and(lessThan(10)), integerList));

        Assert.assertEquals(empty, Collections.filter(
                Predicate.ALWAYS_FALSE.and(Predicate.ALWAYS_TRUE), integerList));
    }

    @Test
    public void testPredicateNot() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(integerList, Collections.filter(
                Predicate.ALWAYS_FALSE.not(), integerList));

        Assert.assertEquals(integerList, Collections.filter(
                greaterThan(10).not(), integerList));
    }

    @Test
    public void testCollectionsMap() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> res = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10);

        Assert.assertEquals(res, Collections.map(succ(), integerList));
        Assert.assertEquals(res, Collections.map(sum().bind1(1), integerList));

        res = Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18);
        Assert.assertEquals(res, Collections.map(mult2(), integerList));
    }

    @Test
    public void testCollectionsFilter() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> res = Arrays.asList(6, 7, 8, 9);
        Assert.assertEquals(res, Collections.filter(greaterThan(5), integerList));

        res = Arrays.asList(1, 2, 3, 4, 5);
        Assert.assertEquals(res, Collections.filter(lessThan(6), integerList));
    }

    @Test
    public void testCollectionsTakeWhile() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(integerList, Collections.takeWhile(Predicate.ALWAYS_TRUE, integerList));
        Assert.assertEquals(integerList, Collections.takeWhile(greaterThan(0), integerList));
        Assert.assertEquals(new ArrayList<>(), Collections.takeWhile(Predicate.ALWAYS_FALSE, integerList));
    }

    @Test
    public void testCollectionsTakeUnless() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(new ArrayList<>(), Collections.takeUnless(Predicate.ALWAYS_TRUE, integerList));
        Assert.assertEquals(integerList, Collections.takeUnless(greaterThan(10), integerList));
        Assert.assertEquals(integerList, Collections.takeUnless(Predicate.ALWAYS_FALSE, integerList));
    }

    @Test
    public void testCollectionsFoldr() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(45, (int) Collections.foldr(sum(), 0, integerList));
        Assert.assertEquals(0, (int) Collections.foldr(mult(), 0, integerList));
    }

    @Test
    public void testCollectionsFoldl() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assert.assertEquals(45, (int) Collections.foldr(sum(), 0, integerList));
        Assert.assertEquals(0, (int) Collections.foldr(mult(), 0, integerList));
    }

    @Test
    public void testFoldrFoldl() {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        Function2<Integer, Integer, Integer> constFunc = new Function2<Integer, Integer, Integer>() {
            @Override
            Integer apply(Integer integer, Integer integer2) {
                return integer;
            }
        };

        Assert.assertNotEquals(Collections.foldl(constFunc, 0, integerList),
                                Collections.foldr(constFunc, 0, integerList));
    }


    @Test
    public void testGenerics() {
        Function2<Number, Number, String> numToStr = new Function2<Number, Number, String>() {
            @Override
            String apply(Number num1, Number num2) {
                return num1.toString() + num2.toString();
            }
        };

        Function1<String, Integer> len = new Function1<String, Integer>() {
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
