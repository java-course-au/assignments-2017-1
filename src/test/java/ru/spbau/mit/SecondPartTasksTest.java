package ru.spbau.mit;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final List<String> paths = Arrays.asList("src/test/resources/files/file1.txt",
                                                 "src/test/resources/files/file2.txt",
                                                 "src/test/resources/files/file3.txt");
        final List<String> expected = Arrays.asList("3ACGT3", "2ACGTACGT2");
        final List<String> actual = SecondPartTasks.findQuotes(paths, "ACGT");
        assertEquals(expected, actual);
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 10e-4);
    }

    @Test
    public void testFindPrinter() {
        final Map<String, List<String>> compositions = new HashMap<String, List<String>>() {{
            put("printer1", Arrays.asList("A", "AA", "AAA"));
            put("printer2", Arrays.asList("AA", "AAA", "AA"));
            put("printer3", Arrays.asList("AAA", "AA", "A"));
        }};
        final String expected = "printer2";
        final String actual = SecondPartTasks.findPrinter(compositions);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateGlobalOrder() {
        final List<Map<String, Integer>> orders = Arrays.asList(
                new HashMap<String, Integer>() {{
                    put("product1", 1);
                    put("product2", 1);
                }},
                new HashMap<String, Integer>() {{
                    put("product2", 2);
                    put("product3", 2);
                }},
                new HashMap<String, Integer>() {{
                    put("product1", 4);
                    put("product2", 4);
                    put("product3", 4);
                }}
        );
        final Map<String, Integer> expected = new HashMap<String, Integer>() {{
            put("product1", 5);
            put("product2", 7);
            put("product3", 6);
        }};
        final Map<String, Integer> actual = SecondPartTasks.calculateGlobalOrder(orders);
        assertEquals(expected, actual);
    }
}
