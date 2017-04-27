package ru.spbau.mit;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final List<String> emptyList = new ArrayList<>();
        assertEquals(emptyList, findQuotes(emptyList, "lorem"));

        final List<String> test = Arrays.asList("ipsum ipsum", "lorem ipsum",
                "lorem", "tell me why", "how not to sleep and stay strong",
                "don't worry, be happy, lorem", "save my lorem, tristan", "");

        assertEquals(emptyList, findQuotes(test, "nonExistingWord"));

        assertEquals(Arrays.asList("lorem ipsum", "lorem",
                "don't worry, be happy, lorem", "save my lorem, tristan"),
                findQuotes(test, "lorem"));

        assertEquals(test, findQuotes(test, ""));
    }

    @Test
    public void testPiDividedBy4() {
        final double piDiv4 = Math.PI / 4.;
        final double eps = 1e-2;

        assertEquals(piDividedBy4(), piDiv4, eps);
    }

    @Test
    public void testFindPrinter() {
        final HashMap<String, List<String>> compositions = new HashMap<String, List<String>>() {{
                put("Lermontov", Arrays.asList(
                        "Tell me my uncle",
                        "it's not for nothing,",
                        "Moscow",
                        "burnt by a fire to the Frenchman is given?"));
                put("Esenin", Arrays.asList(
                        "Oh",
                        "you are my native Russia",
                        "huts in garments of the image",
                        "do not see the end and edges",
                        "only blue sucks eyes"));
                put("Hocku", Arrays.asList("The cat is wet",
                        "and the dog is wet.",
                        "It's a not a happy dog",
                        "it's a not a happy cat"));

                put("Father", Collections.singletonList("I'm your father"));
                put("SOS", Collections.emptyList());
            }};

        assertEquals("Esenin", findPrinter(compositions));

        final HashMap<String, List<String>> smallCompositions = new HashMap<String, List<String>>() {{
            put("aba", Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
            put("bab", Collections.singletonList("abcdefg"));
            put("bb", Collections.emptyList());
        }};

        assertEquals("aba", findPrinter(smallCompositions));

        final HashMap<String, List<String>> longCompositions = new HashMap<String, List<String>>() {{
            put("aba", Arrays.asList("a", "b", "c", "d", "e", "f"));
            put("bab", Collections.singletonList("abcdefghijk"));
            put("bb", Collections.emptyList());
        }};

        assertEquals("bab", findPrinter(longCompositions));

        assertEquals("", findPrinter(new HashMap<>()));
    }

    // CHECKSTYLE:OFF
    @Test
    public void testCalculateGlobalOrder() {
        HashMap<String, Integer> order1 = new HashMap<String, Integer>() {{
            put("1", 1);
            put("2", 2);
        }};

        HashMap<String, Integer> order2 = new HashMap<String, Integer>() {{
            put("1", 2);
            put("2", 4);
        }};

        HashMap<String, Integer> order3 = new HashMap<String, Integer>() {{
            put("3", 3);
            put("5", 5);
        }};

        HashMap<String, Integer> order4 = new HashMap<String, Integer>() {{
            put("5", 3);
            put("12", 0);
        }};

        List<Map<String, Integer>> orders = Arrays.asList(
                order1, order2, order3, order4
        );

        HashMap<String, Integer> resultOrder = new HashMap<String, Integer>() {{
            put("1", 3);
            put("2", 6);
            put("3", 3);
            put("5", 8);
            put("12", 0);
        }};

        assertEquals(resultOrder, calculateGlobalOrder(orders));


        final List<Map<String, Integer>> emptyList = new ArrayList<>();
        assertEquals(Collections.EMPTY_MAP, calculateGlobalOrder(emptyList));
    }
    // CHECKSTYLE:ON
}
