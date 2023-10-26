package org.example;

import com.google.gson.GsonBuilder;
import org.example.core.Line;
import org.example.core.Station;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JSONFileCreation {
    private static Map<String, List<String>> getStationNames(Map<String, List<Station>> stationInfo) {
        Map<String, List<String>> stationNames = new LinkedHashMap<>();
        for (Map.Entry<String, List<Station>> entry : stationInfo.entrySet()) {
            stationNames.put(
                    entry.getKey(),
                    entry.getValue().stream()
                            .map(Station::getName)
                            .collect(Collectors.toList())
            );
        }
        return stationNames;
    }

    public static void createJSONMap(List<Line> lineInfo, Map<String, List<Station>> stationInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("lines", lineInfo);
        map.put("stations", getStationNames(stationInfo));

        String jsonFile = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(map);
        try {
            Files.write(Path.of("data/map.json"), Collections.singleton(jsonFile));
        } catch (IOException e) {
            System.out.println("An error occurred while writing JSON file: " + e.getMessage());
        }
    }
}

/*
Класс, в который можно добавлять данные, полученные на предыдущих шагах,
и который создаёт и записывает на диск два JSON-файла:
со списком станций по линиям и списком линий по формату JSON-файла из проекта SPBMetro (файл map.json);
TODO: файл stations.json со свойствами станций в следующем формате:
{
    "stations": [
          {
                "name": "Название станции",
                "line": "Название линии",
                "date": "Дата открытия в формате 19.01.2005",
                "depth": "Глубина станции в виде числа",
                "hasConnection": "Есть ли на станции переход"
          },
          …
    ]
}
Если каких-то свойств для станции нет, то в файле не должно быть соответствующих ключей.
______________________________________
Проверьте созданный класс: запустите получившуюся программу и убедитесь, что она создаёт
и записывает два JSON-файла по описанным выше форматам.
 */
