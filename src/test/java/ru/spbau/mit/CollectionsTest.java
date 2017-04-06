package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junitx.framework.Assert.assertEquals;

public class CollectionsTest {
    private static final int ARRAY_LENGTH = 100;
    private static final int MAX_RAND_VALUE = 200;

    private Function1<Integer, Integer> sqr =
            new Function1<Integer, Integer>() {
                @Override
                Integer apply(Integer integer) {
                    return integer * integer;
                }
            };

    private final Random random = new Random();
    private ArrayList<Integer> sequence;

    @Before
    public void setUp() {
        sequence = new ArrayList<>(ARRAY_LENGTH);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            sequence.add(random.nextInt(MAX_RAND_VALUE));
        }
    }

    @Test
    public void mapTest() {
        List<Integer> mappedSequence = Collections.map(sequence, sqr);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            assertEquals((Integer) (sequence.get(i) * sequence.get(i)),
                    mappedSequence.get(i));
        }
    }

    @Test
    public void filterTest() {
        Predicate<Integer> lessThanZero =
                new Predicate<Integer>() {
                    @Override
                    Boolean apply(Integer integer) {
                        return integer < 0;
                    }
                };

        List<Integer> filteredSeq = Collections.filter(sequence, lessThanZero);

        ArrayList<Integer> trueFilteredSeq = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : sequence) {
            if (element < 0) {
                trueFilteredSeq.add(element);
            }
        }

        for (int i = 0; i < trueFilteredSeq.size(); i++) {
            assertEquals(filteredSeq.get(i), trueFilteredSeq.get(i));
        }
    }

    @Test
    public void filterAllTest() {
        List<Integer> filteredSeq = Collections.filter(sequence,
                Predicate.ALWAYS_TRUE);

        for (int i = 0; i < sequence.size(); i++) {
            assertEquals(sequence.get(i), filteredSeq.get(i));
        }

        filteredSeq = Collections.filter(sequence, Predicate.ALWAYS_FALSE);
        assertEquals(0, filteredSeq.size());
    }

    @Test
    public void takeWhileTest() {
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            sequence.set(i, i);
        }

        final int fifty = 50;

        Predicate<Integer> lessThanFifty =
                new Predicate<Integer>() {
                    @Override
                    Boolean apply(Integer integer) {
                        return integer < fifty;
                    }
                };

        List<Integer> takeWhileSequence =
                Collections.takeWhile(sequence, lessThanFifty);

        ArrayList<Integer> trueTakeWhileSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : sequence) {
            if (lessThanFifty.apply(element)) {
                break;
            }
            trueTakeWhileSequence.add(element);
        }

        for (int i = 0; i < trueTakeWhileSequence.size(); i++) {
            assertEquals(trueTakeWhileSequence.get(i),
                    takeWhileSequence.get(i));
        }

        takeWhileSequence = Collections.takeWhile(sequence,
                Predicate.ALWAYS_TRUE);
        for (int i = 0; i < sequence.size(); i++) {
            assertEquals(sequence.get(i), takeWhileSequence.get(i));
        }

        takeWhileSequence = Collections.takeWhile(sequence,
                Predicate.ALWAYS_FALSE);
        assertEquals(0, takeWhileSequence.size());
    }

    @Test
    public void takeUnlessTest() {
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            sequence.set(i, i);
        }

        final int fifty = 50;

        Predicate<Integer> greaterThanFifty =
                new Predicate<Integer>() {
                    @Override
                    Boolean apply(Integer integer) {
                        return integer > fifty;
                    }
                };

        List<Integer> takeUnlessSequence =
                Collections.takeUnless(sequence, greaterThanFifty);
        ArrayList<Integer> trueTakeUnlessSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : sequence) {
            if (greaterThanFifty.apply(element)) {
                break;
            }
            trueTakeUnlessSequence.add(element);
        }

        for (int i = 0; i < trueTakeUnlessSequence.size(); i++) {
            assertEquals(trueTakeUnlessSequence.get(i),
                    takeUnlessSequence.get(i));
        }

        Predicate<Integer> lessThanMax =
                new Predicate<Integer>() {
                    @Override
                    Boolean apply(Integer integer) {
                        return integer < ARRAY_LENGTH;
                    }
                };
        takeUnlessSequence = Collections.takeUnless(sequence, lessThanMax);

        assertEquals(0, takeUnlessSequence.size());

        Predicate<Integer> lessThanMin =
                new Predicate<Integer>() {
                    @Override
                    Boolean apply(Integer integer) {
                        return integer < 0;
                    }
                };

        takeUnlessSequence = Collections.takeUnless(sequence, lessThanMin);
        for (int i = 0; i < sequence.size(); i++) {
            assertEquals(sequence.get(i), takeUnlessSequence.get(i));
        }
    }

    @Test
    public void foldrTest() {
        Function2<Number, Number, Number> sum =
                new Function2<Number, Number, Number>() {
                    @Override
                    Number apply(Number x, Number y) {
                        return x.doubleValue() + y.doubleValue();
                    }
                };

        Integer sumValue = 0;
        for (Integer element : sequence) {
            sumValue += element;
        }

        Number value = Collections.foldr(sequence, sum, 0);
        assertEquals(sumValue.doubleValue(), value);
    }

    @Test
    public void foldlTest() {
        Function2<Number, Number, Number> sum =
                new Function2<Number, Number, Number>() {
                    @Override
                    Number apply(Number x, Number y) {
                        return x.doubleValue() + y.doubleValue();
                    }
                };

        Integer sumValue = 0;
        for (Integer element : sequence) {
            sumValue += element;
        }

        Number value = Collections.foldl(sequence, sum, 0);
        assertEquals(sumValue.doubleValue(), value);
    }
}
