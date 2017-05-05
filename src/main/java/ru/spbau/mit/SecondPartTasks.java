package ru.spbau.mit;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
            .flatMap(path -> {
                try {
                    return Files.lines(Paths.get(path));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }).filter(p -> p.contains(sequence))
            .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final Random random = new Random();
        final double radius = 0.5;
        final double centerX = 0.5;
        final double centerY = 0.5;
        final long shots = 100000;

        final long hits = Stream
            .generate(() -> new Point2D.Double(random.nextDouble(), random.nextDouble()))
            .limit(shots)
            .filter(pt -> pt.distance(centerX, centerY) <= radius)
            .count();

        return (double) hits / shots;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
            .map(p -> new AbstractMap.SimpleEntry<String, Integer>(p.getKey(), p.getValue()
                    .stream()
                    .mapToInt(String::length)
                    .sum())
            ).max(Comparator.comparing(AbstractMap.SimpleEntry<String, Integer>::getValue))
            .get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
            .flatMap(p -> p.entrySet().stream())
            .collect(Collectors.groupingBy(
                Map.Entry<String, Integer>::getKey,
                Collectors.summingInt(Map.Entry<String, Integer>::getValue)));
    }
}
