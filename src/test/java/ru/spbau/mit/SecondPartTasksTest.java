package ru.spbau.mit;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> files = Arrays.asList("./src/test/resources/file1.txt",
                                                "./src/test/resources/file2.txt");

        assertEquals(Collections.singletonList("String, Integer, Double"),
                SecondPartTasks.findQuotes(files, "Integer"));

        assertEquals(Collections.EMPTY_LIST,
                        SecondPartTasks.findQuotes(files, "not in files"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(SecondPartTasks.piDividedBy4() - Math.PI / 4 <= 1e-10);
    }

    @Test
    public void testFindPrinter() {
        assertEquals("",
                    SecondPartTasks.findPrinter(new HashMap<>()));

        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("Pushkin", Arrays.asList("Ruslan and Ludmila",
                                                  "Kapitanskaya dochka",
                                                  "Dubrovsky"));
        compositions.put("Shakespeare", Arrays.asList("Hamlet",
                                                      "Romeo and Juliet"));

        compositions.put("Sobir", new ArrayList<>());

        assertEquals("Pushkin",
                    SecondPartTasks.findPrinter(compositions));

        compositions.remove("Pushkin");

        assertEquals("Shakespeare",
                SecondPartTasks.findPrinter(compositions));

    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> order1 = new HashMap<>();
        order1.put("apple", 10);
        order1.put("samsung", 10);

        Map<String, Integer> order2 = new HashMap<>();
        order2.put("apple", 10);
        order2.put("samsung", 10);
        order2.put("Nokia", 10);

        Map<String, Integer> res = new HashMap<>();
        res.put("apple", 20);
        res.put("samsung", 20);
        res.put("Nokia", 10);

        List<Map<String, Integer>> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        assertEquals(res, SecondPartTasks.calculateGlobalOrder(orders));

        assertEquals(Collections.EMPTY_MAP,
                SecondPartTasks.calculateGlobalOrder(new ArrayList<>()));
    }
}
