package ru.spbau.mit;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static ru.spbau.mit.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final ArrayList<String> inputData = new ArrayList<>(Arrays.asList("World", "First File", "Hello", "Second File Second", "Files"));
        final ArrayList<String> check = new ArrayList<>(Arrays.asList("First File", "Second File Second", "Files"));
        assertEquals(check, findQuotes(inputData, "File"));
        assertEquals(Collections.emptyList(), findQuotes(inputData, "Hi"));
        assertEquals(Collections.emptyList(), findQuotes(Collections.emptyList(), "Hi"));
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4.0, piDividedBy4(), 0.1E-2);
    }

    @Test
    public void testFindPrinter() {
        final Map<String, List<String>> inputData = new HashMap<String, List<String>>() {{
            put("Author_1", new ArrayList<>(Arrays.asList("Book_1", "Book_2")));
            put("Author_2", new ArrayList<>(Arrays.asList("Book_1", "Book_2", "Book_3")));
            put("Author_3", new ArrayList<>(Arrays.asList("Book_1", "Book_2", "Book_33")));
            put("Author_4", new ArrayList<>(Collections.singletonList("Book_1")));
        }};
        final String check = "Author_3";
        assertEquals(check, findPrinter(inputData));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final List<Map<String, Integer>> inputData = Arrays.asList(
                new HashMap<String, Integer>() {{
                    put("Product_1", 10);
                    put("Product_2", 5);
                    put("Product_3", 20);
                }},
                new HashMap<String, Integer>() {{
                    put("Product_1", 5);
                    put("Product_2", 5);
                    put("Product_4", 5);
                }},
                new HashMap<String, Integer>() {{
                    put("Product_3", 15);
                    put("Product_5", 5);
                    put("Product_6", 5);
                }}
        );
        final Map<String, Integer> check = new HashMap<String, Integer>() {{
            put("Product_1", 15);
            put("Product_2", 10);
            put("Product_3", 35);
            put("Product_4", 5);
            put("Product_5", 5);
            put("Product_6", 5);
        }};
        assertEquals(check, calculateGlobalOrder(inputData));
    }
}
