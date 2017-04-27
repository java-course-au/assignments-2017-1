package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.fail;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> actual = Arrays.asList(
                "In the right-hand column, you will find alternatives for the underlined part.",
                "In some cases, you will find in the right-hand column a question about the underlined part."
        );

        List<String> fileName = Arrays.asList(
                "./src/test/resources/test_1.txt",
                "./src/test/resources/test_2.txt"
        );

        Assert.assertEquals(actual, SecondPartTasks.findQuotes(fileName, "will"));
    }

    @Test
    public void testPiDividedBy4() {
        Assert.assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 10e-4);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("author_1", Arrays.asList("a", "b"));
        compositions.put("author_2", Arrays.asList("aa", "bb"));
        compositions.put("author_3", Arrays.asList("aaaa", "bbbbb"));
        compositions.put("author_4", Arrays.asList("aaaaaaaaa", "bbbbb"));
        compositions.put("author_5", Collections.emptyList());

        Assert.assertEquals("author_4", SecondPartTasks.findPrinter(compositions));

    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> food1 = new HashMap<>();
        food1.put("banana", 1000);
        food1.put("apple", 200);
        food1.put("tomato", 300);

        Map<String, Integer> food2 = new HashMap<>();
        food2.put("banana", 1000);
        food2.put("apple", 800);
        food2.put("meat", 1000);

        Map<String, Integer> listFood = SecondPartTasks.calculateGlobalOrder(Arrays.asList(food1, food2));

        Assert.assertEquals(2000, (int) listFood.get("banana"));
        Assert.assertEquals(1000, (int) listFood.get("apple"));
        Assert.assertEquals(300, (int) listFood.get("tomato"));
        Assert.assertEquals(1000, (int) listFood.get("meat"));

    }
}