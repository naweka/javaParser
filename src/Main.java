import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Utils.parseData("Книга1.csv");
        List<Building> parsedBuildings = Utils.getParsedBuildings();
        List<Prefix> parsedPrefixes = Utils.getParsedPrefixes();

        for (int i = 9000; i < 10000; i += 100) {
            System.out.println(parsedBuildings.get(i).toString());
        }
    }
}

class Utils {
    private static List<Building> parsedBuildings = new ArrayList<Building>();
    private static List<Prefix> parsedPrefixes = new ArrayList<Prefix>();

    // Главный метод парсера
    public static void parseData(String path) throws IOException {
        System.out.println("Запущен процесс парсинга");
        List<String> fileLines = Files.readAllLines(Paths.get(path));
        for (String line : fileLines) {
            processParsedLine(line);
        }
        System.out.println("Парсинг завершен");
    }

    public static List<Building> getParsedBuildings(){ return parsedBuildings; }
    public static List<Prefix> getParsedPrefixes(){ return parsedPrefixes; }

    // Здесь просиходит парсинг одной строки из CSV
    // В случае удачного парсинга в лист добавляется новый объект
    private static void processParsedLine(String line) {
        try {
            String[] data = line.replace("\"", "").replace("\'", "").split(";");
            String number = data[0];
            String address = data[1];
            String snapshot = data[2];
            String description = data[3];
            String numberOfFloor = data[4];
            int prefixCode = -1;
            try {prefixCode = Integer.parseInt(data[5]);} catch (Exception e){}
            String buildingType = data[6];
            int id_ = -1;
            try {id_ = Integer.parseInt(data[5]);} catch (Exception e){}
            String year = "";
            if (data.length == 9)
                year = data[8];

            parsedBuildings.add(new Building(
                    id_, number, address, getMaterial(snapshot),
                    year, getFloorsCount(snapshot, numberOfFloor),
                    description, isHabited(snapshot, buildingType)));

            parsedPrefixes.add(new Prefix(id_, number, prefixCode));
        }
        catch (Exception e){
            System.out.println("Возникла ошибка при парсинге строки: "+line);
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
        if (buildingType.contains("Жи"))
            return true;
        return false;
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

// Модель здания (отдельная сущность в бд)
class Building {
    public int id_;
    public String number;
    public String address;
    public String buildingTypeMaterial;
    public boolean buildingTypeHabited;
    public String yearConstruction;
    public int buildingTypeFloors;
    public String description;

    public Building(int id_,
                    String number,
                    String address,
                    String buildingTypeMaterial,
                    String yearConstruction,
                    int buildingTypeFloors,
                    String description,
                    boolean buildingTypeHabited) {
        this.id_ = id_;
        this.number = number;
        this.address = address;
        this.buildingTypeMaterial = buildingTypeMaterial;
        this.yearConstruction = yearConstruction;
        this.buildingTypeFloors = buildingTypeFloors;
        this.description = description;
        this.buildingTypeHabited = buildingTypeHabited;
    }

    @Override
    public String toString() {
        return "\nОбъект Building:" +
                "\nid_=" + id_ +
                "\nnumber=" + number +
                "\naddress=" + address +
                "\nbuildingTypeMaterial=" + buildingTypeMaterial +
                "\nyearConstruction=" + yearConstruction +
                "\nbuildingTypeFloors=" + buildingTypeFloors +
                "\ndescription=" + description +
                "\nbuildingTypeHabited=" + buildingTypeHabited;
    }
}

// Модель префикса (отдельная сущность в бд)
class Prefix {
    public int prefix_code;
    public int id_;
    public String number;

    public Prefix(int id_, String number, int prefix_code) {
        this.id_ = id_;
        this.number = number;
        this.prefix_code = prefix_code;
    }
}