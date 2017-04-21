package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().map(path -> {
            try {
                return Files.lines(Paths.get(path))
                        .filter(line -> line.contains(sequence)).collect(Collectors.toList());
            } catch (IOException e) {
                return Collections.<String>emptyList();
            }
        }).flatMap(List::stream).collect(Collectors.toList());
    }

    private static final class Point {
        private double x;
        private double y;

        private Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int numberOfExperiments = 3131313;
        Random rand = new Random(31);
        long inTarget = Stream.generate(() -> new Point(rand.nextDouble() - 0.5, rand.nextDouble() - 0.5))
                .limit(numberOfExperiments)
                .filter(p -> Math.sqrt(p.x * p.x + p.y * p.y) <= 0.5)
                .count();
        return (double) inTarget / numberOfExperiments;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().stream().mapToInt(String::length).sum()))
                .get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        Map<String, Integer> result = new HashMap<>();
        orders.forEach(m -> {
            m.forEach((k, v) -> {
                result.compute(k, (inK, inV) -> (inV == null ? 0 : inV) + v);
            });
        });
        return result;
    }
}
