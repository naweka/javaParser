import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// В этом классе происходит парсинг CSV
public class Parser {
    private static List<Building> parsedBuildings = new ArrayList<>();
    private static List<Prefix> parsedPrefixes = new ArrayList<>();
    public static List<Building> getParsedBuildings() {
        return parsedBuildings;
    }
    public static List<Prefix> getParsedPrefixes() {
        return parsedPrefixes;
    }

    // Главный метод парсера
    public static void parseData(String path) throws IOException {
        System.out.println("Запущен процесс парсинга");
        List<String> fileLines = Files.readAllLines(Paths.get(path));
        for (String line : fileLines.subList(2, fileLines.size() - 1)) {
            processParsedLine(line);
        }
        System.out.println("Парсинг завершен");
    }

    // Здесь просиходит парсинг одной строки из CSV
    // В случае удачного парсинга в лист добавляется новый объект
    private static void processParsedLine(String line) {
        try {
            String[] data = line.replace("\"", "").replace("'", "").split(";");
            String number = data[0];
            String address = data[1];
            String snapshot = data[2];
            String description = data[3];
            String numberOfFloor = data[4];
            int prefixCode = -1;
            try {prefixCode = Integer.parseInt(data[5]);} catch (Exception e) {}
            String buildingType = data[6];
            int id_ = -1;
            try {id_ = Integer.parseInt(data[7]);} catch (Exception e) {}
            String year = "";
            if (data.length == 9)
                year = data[8];

            parsedBuildings.add(new Building(
                    id_, number, address, getMaterial(snapshot),
                    year, getFloorsCount(snapshot, numberOfFloor),
                    description, isHabited(snapshot, buildingType)));

            parsedPrefixes.add(new Prefix(id_, number, prefixCode));
        } catch (Exception e) {
            System.out.println("Возникла ошибка при парсинге строки: " + line);
        }
    }

    // Парсинг материала, из которого построено здание
    private static String getMaterial(String snapshot) {
        if (snapshot.contains("К"))
            return "Камень";
        if (snapshot.contains("Д"))
            return "Дерево";
        return "";
    }

    // Парсинг параметра жилое это здание или нет
    private static boolean isHabited(String snapshot, String buildingType) {
        if (snapshot.contains("Н"))
            return false;
        if (snapshot.contains("Ж"))
            return true;
        if (buildingType.contains("Нежи"))
            return false;
        return buildingType.contains("Жи");
    }

    // Парсинг количества этажей в здании
    private static int getFloorsCount(String snapshot, String buildingType) {
        if (snapshot.length() > 0 && Character.isDigit(snapshot.charAt(0)))
            return Integer.parseInt(Character.toString(snapshot.charAt(0)));

        if (buildingType.length() > 0 && Character.isDigit(buildingType.charAt(0)))
            return Integer.parseInt(Character.toString(buildingType.charAt(0)));

        return -1;
    }
}
