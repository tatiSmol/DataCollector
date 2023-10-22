package org.example;

import org.example.core.Line;
import org.example.core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebPageParsing {
    public static Document webPageConnect(String url) throws IOException {
        return Jsoup.connect(url).maxBodySize(0).get();
    }

    public static List<Line> createLines(Document source) {
        List<Line> lines = new ArrayList<>();
        Elements linesInfo = source.select("span.js-metro-line");
        for (Element line : linesInfo) {
            String number = line.attr("data-line");
            String name = line.text();
            lines.add(new Line(number, name));
        }
        return lines;
    }

    public static Map<String, List<Station>> createStations(Document source, List<Line> lines) {
        Map<String, List<Station>> stationsMap = new LinkedHashMap<>();
        for (Line line : lines) {
            String lineNumb = line.getNumber();
            Elements elements = source.select("[data-line=\"" + lineNumb + "\"]").select("span.name");
            List<Station> stations = elements.stream().map(stName -> new Station(stName.text(), lineNumb)).toList();
            stationsMap.put(lineNumb, stations);
        }
        return stationsMap;
    }
}

/*
Класс парсинга веб-страницы.
В нём должно происходить (реализуйте каждую операцию в отдельных методах):
    получение HTML-кода страницы «Список станций Московского метрополитена» с помощью библиотеки jsoup;
    парсинг полученной страницы и получение из неё следующих данных (создайте для каждого типа данных отдельные классы):
            линии московского метро (имя и номер линии, цвет не нужен);
            станции московского метро (имя станции и номер линии).
______________________________________
Проверьте работу данного класса: напишите код, который будет его использовать и выводить полученные данные.
Для удобства рекомендуем у каждого класса, предназначенного для хранения того или иного объекта,
реализовать метод toString, который будет возвращать строку с данными объекта в понятном виде.
 */
