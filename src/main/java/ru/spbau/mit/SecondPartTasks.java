package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(p -> {
            try {
                return Files.lines(Paths.get(p));
            } catch (IOException e) {
                return null;
            }
        }).filter(s -> s.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random random = new Random();
        final int numExperiments = 1000000;
        final double x0 = 0.5;
        final double y0 = 0.5;

        Stream<Double> experiments = Stream.generate(() -> sqrt(pow(random.nextDouble() - x0, 2)
                + pow(random.nextDouble() - y0, 2)));
        return (double) experiments.limit(numExperiments).filter(s -> s <= 1.0 / 2.0).count() / numExperiments;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().max(Comparator.comparing(
                a -> a.getValue().stream().mapToInt(String::length).sum())).get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(m -> m.entrySet().stream()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (g1, g2) -> g1 + g2
        ));
    }
}
