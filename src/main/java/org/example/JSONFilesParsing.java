package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.StationDepth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONFilesParsing {
    public static List<StationDepth> parseJsonFile(String path) throws IOException {
        List<StationDepth> stationsDepth = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        String jsonFile = Files.readString(Paths.get(path));
        JsonNode node = mapper.readTree(jsonFile);

        node.forEach(station -> {
            String name = station.get("station_name").asText();
            long depth = station.get("depth").asLong();
            stationsDepth.add(new StationDepth(name, depth));
        });

        return stationsDepth;
    }
}

/*
Класс парсинга файлов формата JSON.
Изучите структуру JSON-файлов, лежащих в папках, и создайте класс(ы) для хранения данных из этих файлов.
Напишите код, который будет принимать JSON-данные и выдавать список соответствующих им объектов.
______________________________________
Проверьте работу данного класса: передайте ему на вход данные любого из JSON-файлов, найденных на предыдущем шаге,
и убедитесь, что он выводит данные корректно.
 */
