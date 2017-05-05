package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {

        return paths
                .stream()
                .flatMap(path -> {
                    try {
                        return Files.lines(Paths.get(path));
                    } catch (IOException ex) {
                        return Stream.empty();
                    }
                })
                .filter(str -> str.contains(sequence))
                .collect(Collectors.toList());
    }


    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int numberOfTries = 1000000;
        final Random randomGen = new Random();
        return DoubleStream
                .generate(() -> getShootResult(randomGen))
                .limit(numberOfTries)
                .average()
                .orElse(0);
    }

    private static Double getShootResult(Random randomGen) {
        final double radius = 0.5;
        double xVal = randomGen.nextDouble() - radius;
        double yVal = randomGen.nextDouble() - radius;
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

        return orders
                .stream()
                .flatMap(order -> order.entrySet().stream())
                .collect(
                        Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.summingInt(i -> i)))
                );
    }
}
