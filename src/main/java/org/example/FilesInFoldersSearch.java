package org.example;

import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesInFoldersSearch {
    @Getter
    private static final List<String> csvFiles = new ArrayList<>();
    @Getter
    private static final List<String> jsonFiles = new ArrayList<>();

    public static void searchFilesInFolder(File file) {
        File[] subFiles = file.listFiles();

        if (subFiles != null) {
            for (File subFile : subFiles) {
                if (subFile.isFile()) {
                    addFileIfValid(subFile);
                } else {
                    searchFilesInFolder(subFile);
                }
            }
        }
    }

    private static void addFileIfValid(File file) {
        String filePath = file.getPath();
        if (filePath.endsWith(".json")) {
            jsonFiles.add(filePath);
        } else if (filePath.endsWith(".csv")) {
            csvFiles.add(filePath);
        }
    }

    public static List<String> getAllValidFiles() {
        return Stream.of(csvFiles, jsonFiles).flatMap(Collection::stream).collect(Collectors.toList());
    }
}

/*
Класс поиска файлов в папках.
В методах этого класса необходимо реализовать обход папок, лежащих в архиве.
Разархивируйте его и напишите код, который будет обходить все вложенные папки и искать в них файлы
форматов JSON и CSV (с расширениями *.json и *.csv).
Метод для обхода папок должен принимать путь до папки, в которой надо производить поиск.
______________________________________
Проверьте работу данного класса: передайте ему на вход путь к папке и убедитесь,
что он вывел информацию о трёх найденных JSON-файлах и о трёх CSV-файлах.
 */
