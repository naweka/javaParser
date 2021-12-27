import java.io.IOException;
import java.sql.*;

import org.jfree.ui.RefineryUtilities;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // Если база данных не создана и не заполнена, нужно раскомментировать код ниже
        //Parser.parseData("Книга1.csv");
        //List<Building> parsedBuildings = Parser.getParsedBuildings();
        //List<Prefix> parsedPrefixes = Parser.getParsedPrefixes();

        Database.connect("dbTerentev.s3db");

        // Если база данных не создана и не заполнена, нужно раскомментировать код ниже
        //Database.createStructure();
        //Database.fillDataToDB(parsedBuildings, parsedPrefixes);

        Queries.getQuery1();
        Queries.getQuery2();
        Queries.getQuery3();

        Chart chart = new Chart();
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
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