package org.example;

import org.example.core.StationOpeningDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CSVFilesParsing {
    public static List<StationOpeningDate> parseCSVFile(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        lines.remove(0);

        return lines.stream()
                .map(line -> line.split(","))
                .map(stationInfo -> new StationOpeningDate(stationInfo[0], stationInfo[1]))
                .collect(Collectors.toList());
    }
}

/*
Класс парсинга файлов формата CSV.
Изучите структуру CSV-файлов, лежащих в папках, и создайте класс(ы) для хранения данных из этих файлов.
Напишите код, который будет принимать CSV-данные и выдавать список соответствующих им объектов.
______________________________________
Проверьте работу данного класса: передайте ему на вход данные любого из CSV-файлов, найденных двумя шагами ранее,
и убедитесь, что он выводит данные корректно.
 */
