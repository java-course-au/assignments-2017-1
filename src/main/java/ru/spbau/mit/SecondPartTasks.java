package ru.spbau.mit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
            .filter(s -> s.contains(sequence))
            .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random rng = new Random();
        final double radius = 0.5;
        final double center = 0.5;
        final int numberOfShoots = 1_000_000;
        return Stream
            .iterate(0, n -> n + 1)
            .mapToDouble(i -> {
                Double x = rng.nextDouble();
                Double y = rng.nextDouble();
                return (x - center) * (x - center)
                        + (y - center) * (y - center) < radius * radius
                        ? 1.0 : 0.0;
            })
            .limit(numberOfShoots)
            .average()
            .orElse(0.);
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .max(Comparator.comparingInt(
                        stringListEntry -> stringListEntry.getValue().stream()
                        .mapToInt(String::length)
                        .sum()
                ))
                .map(Map.Entry::getKey)
                .orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1 + e2
                                )
                );
    }
}
