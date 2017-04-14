package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junitx.framework.Assert.assertEquals;

public class CollectionsTest {
    private static final int ARRAY_LENGTH = 10;
    private static final int MAX_RAND_VALUE = 200;

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


    private final Random random = new Random();
    private ArrayList<Integer> sequence;

    @Before
    public void setUp() {
        sequence = new ArrayList<>(ARRAY_LENGTH);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            sequence.add(random.nextInt(MAX_RAND_VALUE));
        }
    }

    private void setIdSequence() {
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            sequence.set(i, i);
        }
    }

    @Test
    public void testMap() {
        List<Integer> mappedSequence = Collections.map(sequence, SQR);
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            assertEquals((Integer) (sequence.get(i) * sequence.get(i)),
                    mappedSequence.get(i));
        }
    }

    @Test
    public void testFilter() {
        List<Integer> filteredSeq = Collections.filter(sequence, LESS_THAN_ZERO);

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
    public void testFilterAll() {
        List<Integer> filteredSeq = Collections.filter(sequence,
                Predicate.ALWAYS_TRUE);

        for (int i = 0; i < sequence.size(); i++) {
            assertEquals(sequence.get(i), filteredSeq.get(i));
        }

        filteredSeq = Collections.filter(sequence, Predicate.ALWAYS_FALSE);
        assertEquals(0, filteredSeq.size());
    }

    @Test
    public void testTakeWhile() {
        setIdSequence();

        List<Integer> takeWhileSequence =
                Collections.takeWhile(sequence, LESS_THAN_FIVE);

        ArrayList<Integer> trueTakeWhileSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : sequence) {
            if (LESS_THAN_FIVE.apply(element)) {
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
    public void testTakeUnless() {
        setIdSequence();

        List<Integer> takeUnlessSequence =
                Collections.takeUnless(sequence, GREATER_THAN_FIVE);
        ArrayList<Integer> trueTakeUnlessSequence = new ArrayList<>(ARRAY_LENGTH);
        for (Integer element : sequence) {
            if (GREATER_THAN_FIVE.apply(element)) {
                break;
            }
            trueTakeUnlessSequence.add(element);
        }

        for (int i = 0; i < trueTakeUnlessSequence.size(); i++) {
            assertEquals(trueTakeUnlessSequence.get(i),
                    takeUnlessSequence.get(i));
        }

        takeUnlessSequence = Collections.takeUnless(sequence, LESS_THAN_ARRAY_LENGTH);

        assertEquals(0, takeUnlessSequence.size());

        takeUnlessSequence = Collections.takeUnless(sequence, LESS_THAN_ZERO);
        for (int i = 0; i < sequence.size(); i++) {
            assertEquals(sequence.get(i), takeUnlessSequence.get(i));
        }
    }

    @Test
    public void testFoldrSum() {
        Integer sumValue = 0;
        for (Integer element : sequence) {
            sumValue += element;
        }

        Number value = Collections.foldr(sequence, SUM, 0);
        assertEquals(sumValue.doubleValue(), value);
    }

    @Test
    public void testFoldrSub() {
        setIdSequence();

        Integer value = Collections.foldr(sequence, SUB, 0);
        final Integer minusFive = -5;
        assertEquals(minusFive, value);
    }

    @Test
    public void testFoldlSum() {
        Integer sumValue = 0;
        for (Integer element : sequence) {
            sumValue += element;
        }

        Number value = Collections.foldl(sequence, SUM, 0);
        assertEquals(sumValue.doubleValue(), value);
    }
}
