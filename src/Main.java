import java.io.IOException;
import java.sql.*;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        // Если база данных не создана и не заполнена, нужно раскомментировать код ниже
        //Parser.parseData("Книга1.csv");
        //List<TableItem.Building> parsedBuildings = Parser.getParsedBuildings();
        //List<TableItem.Prefix> parsedPrefixes = Parser.getParsedPrefixes();

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

