package ru.spbau.mit;

import java.io.File;
import java.nio.file.Paths;

public class SimpleImplementor implements Implementor {
    private String outputDirectory;

    public SimpleImplementor(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    private String getAbsolutePath(String directoryPath, String className) {
        final String[] split = className.split("\\.");
        split[split.length - 1] += ".class";
        return Paths.get(directoryPath, split).toAbsolutePath().toString();
    }

    /**
     * Имплементор по данной папке с class файлами java ищет в ней java класс, которые требуется реализовать.
     * Класс записывает реализацию в папку `outputDirectory` (учитывая пакеты)
     * и возвращает полное название нового класса.
     * Реализация должна находится в том же пакете, что и исходный класс/интерфейс.
     * <p>
     * Например, требуется реализовать интерфейс `ru.spbau.mit.AnInterface`.
     * Тогда в папке ожидается файл ru/spbau/mit/AnInterface.class.
     * Implementor генерирует реализацию этого интерфейса, кладет её в
     * `workingDirectory`/ru/spbau/mit/AnInterfaceImpl.java
     * и возвращает полное имя сгенерированного класса ru.spbau.mit.AnInterfaceImpl.
     *
     * @param directoryPath путь до директории, которая содержит данный класс/интерфейс
     * @param className     полное название класса/интерфейса, которое требуется реализовать
     * @throws ImplementorException в тех ситуациях когда
     *                              1) Невозможно создать наследника класса.
     *                              2) Входной класс не найден.
     *                              3) Невозможно записать сгенерированный класс.
     */
    @Override
    public String implementFromDirectory(String directoryPath, String className) throws ImplementorException {
        String pathToClass = getAbsolutePath(directoryPath, className);
        File clsFile = new File(pathToClass);
        if (!clsFile.isFile()) {
            throw new ImplementorException("File not found");
        }
        return null;
    }

    /**
     * Имплементор ищет java класс/интерфейс из стандартной библеотеки, которые требуется реализовать.
     * Класс записывает реализацию в папку `outputDirectory`.
     * Реализация должна находится в default пакете.
     * <p>
     * Например, требуется реализовать интерфейс `java.lang.Comparable`
     * Имплементор генерирует реализацию этого интерфейса, кладет её в `workingDirectory`/ComparableImpl.java и
     * возвращает полное имя сгенерированного класса ComparableImpl.
     *
     * @param className полное название класса/интерфейса, которое требуется реализовать
     * @return полное название нового класса, например ComparableImpl
     * @throws ImplementorException в тех ситуациях когда
     *                              1) Невозможно создать наследника класса.
     *                              2) Входной класс не найден.
     *                              3) Невозможно записать сгенерированный класс.
     */
    @Override
    public String implementFromStandardLibrary(String className) throws ImplementorException {
        return null;
    }
}
