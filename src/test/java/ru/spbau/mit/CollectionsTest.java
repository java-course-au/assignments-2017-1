package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junitx.framework.Assert.assertEquals;

public class CollectionsTest {
    private static final Function1<Integer, Integer> SQR =
            new Function1<Integer, Integer>() {
                @Override
                Integer apply(Integer integer) {
                    return integer * integer;
                }
            };

    private static final Function2<Number, Number, Number> SUM =
            new Function2<Number, Number, Number>() {
                @Override
                Number apply(Number x, Number y) {
                    return x.doubleValue() + y.doubleValue();
                }
            };

    private static final Function2<Integer, Integer, Integer> SUB =
            new Function2<Integer, Integer, Integer>() {
                @Override
                Integer apply(Integer x, Integer y) {
                    return x - y;
                }
            };

    private static final Predicate<Integer> LESS_THAN_ZERO =
            new Predicate<Integer>() {
                @Override
                Boolean apply(Integer integer) {
                    return integer < 0;
                }
            };

    private static final Predicate<Integer> GREATER_THAN_FIVE =
            new Predicate<Integer>() {
                @Override
                Boolean apply(Integer integer) {
                    final int five = 5;
                    return integer > five;
                }
            };

    private static final Predicate<Integer> LESS_THAN_FIVE =
            new Predicate<Integer>() {
                @Override
                Boolean apply(Integer integer) {
                    final int five = 5;
                    return integer < five;
                }
            };

    private static final Predicate<Integer> LESS_THAN_ARRAY_LENGTH =
            new Predicate<Integer>() {
                @Override
                Boolean apply(Integer integer) {
                    return integer < ARRAY_LENGTH;
                }
            };

    private static final int ARRAY_LENGTH = 10;
    private static final List<Integer> SEQUENCE =
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    @Test
    public void testMap() {
        List<Integer> mappedSequence = Collections.map(SEQUENCE, SQR);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            assertEquals((Integer) (SEQUENCE.get(i) * SEQUENCE.get(i)),
                    mappedSequence.get(i));
        }
    }

    @Test
    public void testFilter() {
        List<Integer> filteredSeq = Collections.filter(SEQUENCE, LESS_THAN_ZERO);

        ArrayList<Integer> trueFilteredSeq = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : SEQUENCE) {
            if (element < 0) {
                trueFilteredSeq.add(element);
            }
        }

        for (int i = 0; i < trueFilteredSeq.size(); i++) {
            assertEquals(filteredSeq.get(i), trueFilteredSeq.get(i));
        }
    }

    @Test
    public void testFilterAll() {
        List<Integer> filteredSeq = Collections.filter(SEQUENCE,
                Predicate.ALWAYS_TRUE);

        for (int i = 0; i < SEQUENCE.size(); i++) {
            assertEquals(SEQUENCE.get(i), filteredSeq.get(i));
        }

        filteredSeq = Collections.filter(SEQUENCE, Predicate.ALWAYS_FALSE);
        assertEquals(0, filteredSeq.size());
    }

    @Test
    public void testTakeWhile() {
        List<Integer> takeWhileSequence =
                Collections.takeWhile(SEQUENCE, LESS_THAN_FIVE);

        ArrayList<Integer> trueTakeWhileSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : SEQUENCE) {
            if (LESS_THAN_FIVE.apply(element)) {
                break;
            }
            trueTakeWhileSequence.add(element);
        }

        for (int i = 0; i < trueTakeWhileSequence.size(); i++) {
            assertEquals(trueTakeWhileSequence.get(i),
                    takeWhileSequence.get(i));
        }

        takeWhileSequence = Collections.takeWhile(SEQUENCE,
                Predicate.ALWAYS_TRUE);
        for (int i = 0; i < SEQUENCE.size(); i++) {
            assertEquals(SEQUENCE.get(i), takeWhileSequence.get(i));
        }

        takeWhileSequence = Collections.takeWhile(SEQUENCE,
                Predicate.ALWAYS_FALSE);
        assertEquals(0, takeWhileSequence.size());
    }

    @Test
    public void testTakeUnless() {
        List<Integer> takeUnlessSequence =
                Collections.takeUnless(SEQUENCE, GREATER_THAN_FIVE);
        ArrayList<Integer> trueTakeUnlessSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : SEQUENCE) {
            if (GREATER_THAN_FIVE.apply(element)) {
                break;
            }
            trueTakeUnlessSequence.add(element);
        }

        for (int i = 0; i < trueTakeUnlessSequence.size(); i++) {
            assertEquals(trueTakeUnlessSequence.get(i),
                    takeUnlessSequence.get(i));
        }

        takeUnlessSequence = Collections.takeUnless(SEQUENCE, LESS_THAN_ARRAY_LENGTH);

        assertEquals(0, takeUnlessSequence.size());

        takeUnlessSequence = Collections.takeUnless(SEQUENCE, LESS_THAN_ZERO);
        for (int i = 0; i < SEQUENCE.size(); i++) {
            assertEquals(SEQUENCE.get(i), takeUnlessSequence.get(i));
        }
    }

    @Test
    public void testFoldrSum() {
        Number value = Collections.foldr(SEQUENCE, SUM, 0);
        final Double fortyFive = 45.;
        assertEquals(fortyFive, value);
    }

    @Test
    public void testFoldrSub() {
        Integer value = Collections.foldr(SEQUENCE, SUB, 0);
        final Integer minusFive = -5;
        assertEquals(minusFive, value);
    }

    @Test
    public void testFoldlSum() {
        Number value = Collections.foldl(SEQUENCE, SUM, 0);
        final Double fortyFive = 45.;
        assertEquals(fortyFive, value);
    }
}
