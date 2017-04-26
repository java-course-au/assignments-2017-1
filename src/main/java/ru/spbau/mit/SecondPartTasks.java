package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        List<String> result = new ArrayList<>();
        paths.forEach(path -> {
            try (Stream<String> stream = Files.lines(Paths.get(path))) {
                stream.filter(str -> str.contains(sequence)).forEach(result::add);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return result;
    }


    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int numberOfTries = 1000000;
        double numberOfSuccesses = IntStream.range(0, numberOfTries).mapToDouble(e -> getShootResult()).sum();
        return numberOfSuccesses / numberOfTries;
    }

    private static Double getShootResult() {
        final double radius = 0.5;
        double xVal = new Random().nextDouble() - radius;
        double yVal = new Random().nextDouble() - radius;
        double distFromCenter = Math.sqrt(xVal * xVal + yVal * yVal);
        return distFromCenter < radius ? 1.0 : 0.0;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions
                .entrySet()
                .stream()
                .max(Comparator.comparing(entry -> entry.getValue().stream().mapToInt(String::length).sum()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        Map<String, Integer> result = new HashMap<>();
        orders.forEach(map -> map.forEach((good, quantity) -> result.merge(good, quantity, Integer::sum)));
        return result;
    }
}
