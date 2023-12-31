package org.example;

import org.example.core.Line;
import org.example.core.Station;
import org.example.core.StationDepth;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Document source = WebPageParsing.webPageConnect("https://skillbox-java.github.io/");
        List<Line> lineInfo = WebPageParsing.createLines(source);
        Map<String, List<Station>> stationInfo = WebPageParsing.createStations(source, lineInfo);

        for (Line line : lineInfo) {
            List<Station> stations = stationInfo.get(line.getNumber());
            System.out.println(line.getName() + ": " + stations);
        }

        System.out.println("-------------------");

        File folder = new File("data/stations-data");
        FilesInFoldersSearch.searchFilesInFolder(folder);
        System.out.println(FilesInFoldersSearch.getAllValidFiles());

        System.out.println("-------------------");

        List<StationDepth> stationDepths = JSONFilesParsing.parseJsonFile(FilesInFoldersSearch.getJsonFiles().get(0));
        stationDepths.forEach(System.out::println);

        System.out.println("-------------------");

        CSVFilesParsing.parseCSVFile(FilesInFoldersSearch.getCsvFiles().get(1)).forEach(System.out::println);

        System.out.println("-------------------");

        JSONFileCreation.createJSONMap();

        System.out.println("-------------------");

        JSONFileCreation.createJSONStations();
    }
}
