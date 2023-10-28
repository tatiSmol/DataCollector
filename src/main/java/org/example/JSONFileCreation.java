package org.example;

import com.google.gson.GsonBuilder;
import org.example.core.Line;
import org.example.core.Station;
import org.example.core.StationDepth;
import org.example.core.StationOpeningDate;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JSONFileCreation {
    private static final Document SOURCE;

    static {
        try {
            SOURCE = WebPageParsing.webPageConnect("https://skillbox-java.github.io/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Line> getLineInfo() {
        return WebPageParsing.createLines(SOURCE);
    }


    private static Map<String, List<Station>> getStationInfo() {
        return WebPageParsing.createStations(SOURCE, getLineInfo());
    }

    private static Map<String, List<String>> getStationNames() {
        Map<String, List<String>> stationNames = new LinkedHashMap<>();
        for (Map.Entry<String, List<Station>> entry : getStationInfo().entrySet()) {
            stationNames.put(
                    entry.getKey(),
                    entry.getValue().stream()
                            .map(Station::getName)
                            .collect(Collectors.toList())
            );
        }
        return stationNames;
    }

    public static void createJSONMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("lines", getLineInfo());
        map.put("stations", getStationNames());

        String jsonFile = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(map);
        try {
            Files.write(Path.of("data/map.json"), Collections.singleton(jsonFile));
        } catch (IOException e) {
            System.out.println("An error occurred while writing JSON file: " + e.getMessage());
        }
    }

    private static void processFilesInFolder() {
        FilesInFoldersSearch.searchFilesInFolder(new File("data/stations-data"));
    }

    private static Map<String, Long> getStationDepths() throws IOException {
        Map<String, Long> depthsMap = new HashMap<>();
        List<String> jsonData = FilesInFoldersSearch.getJsonFiles();

        for (String jsonF : jsonData) {
            List<StationDepth> stationDepths = JSONFilesParsing.parseJsonFile(jsonF);
            for (StationDepth stationDepth : stationDepths) {
                long depth = stationDepth.getDepth();
                depthsMap.compute(stationDepth.getStationName(),
                        (key, value) -> value == null ? depth : Math.min(value, depth));
            }
        }

        return depthsMap;
    }

    private static Map<String, List<String>> getStationOpeningDates() throws IOException {
        Map<String, List<String>> datesMap = new HashMap<>();
        List<String> csvData = FilesInFoldersSearch.getCsvFiles();

        for (String csvF : csvData) {
            List<StationOpeningDate> stationOpeningDates = CSVFilesParsing.parseCSVFile(csvF);
            for (StationOpeningDate openingDate : stationOpeningDates) {
                String name = openingDate.getName();
                String date = openingDate.getOpeningDate();

                if (!datesMap.containsKey(name)) {
                    datesMap.put(name, new LinkedList<>(Collections.singleton(date)));
                } else {
                    datesMap.computeIfPresent(name, (key, value) -> {
                        value.add(date);
                        return value;
                    });
                }
            }
        }

        return datesMap;
    }

    private static List<Station> getStations() {
        return getStationInfo().entrySet().stream()
                .flatMap(lines -> lines.getValue().stream())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public static void createJSONStations() throws IOException {
        processFilesInFolder();
        List<Station> stations = getStations();
        Map<String, Long> stationDepths = JSONFileCreation.getStationDepths();
        Map<String, List<String>> stationOpeningDates = JSONFileCreation.getStationOpeningDates();

        for (Station station : stations) {
            if (stationDepths.get(station.getName()) != null) {
                long depth = stationDepths.get(station.getName());
                station.setDepth(depth);
            }

            if (stationOpeningDates.get(station.getName()) != null) {
                String date = stationOpeningDates.get(station.getName()).get(0);
                stationOpeningDates.get(station.getName()).remove(0);
                station.setDate(date);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("stations", stations);

        String jsonFile = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(map);
        try {
            Files.write(Path.of("data/stations.json"), Collections.singleton(jsonFile));
        } catch (IOException e) {
            System.out.println("An error occurred while writing JSON file: " + e.getMessage());
        }
    }
}

/*
Класс, в который можно добавлять данные, полученные на предыдущих шагах,
и который создаёт и записывает на диск два JSON-файла:
    со списком станций по линиям и списком линий по формату JSON-файла из проекта SPBMetro (файл map.json);
    файл stations.json со свойствами станций в следующем формате:
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
