package ru.spbau.mit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths
                .stream()
                .flatMap(s -> {
                    try {
                        return Files.lines(Paths.get(s));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .filter(r -> r.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int n = 999999;
        Random random = new Random();
        return IntStream
                .range(0, n)
                .mapToDouble(value ->
                        (Math.sqrt(Math.pow(random.nextFloat(), 2)
                                + Math.pow(random.nextFloat(), 2)) <= 1 ? 1 : 0) * 1.0 / n
                )
                .sum();
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {

        return compositions
                .entrySet()
                .stream()
                .max(Comparator.comparing(stringListEntry ->
                        stringListEntry
                                .getValue()
                                .stream()
                                .mapToInt(String::length)
                                .max()
                                .orElse(0)))
                .get()
                .getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders
                .stream()
                .flatMap(stringIntegerMap -> stringIntegerMap
                        .entrySet()
                        .stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }
}
