import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    public static void execQuery(String desc, String query, List<Integer> indexes) throws SQLException {
        System.out.println(desc);
        resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String res = "";
            for(Integer integer: indexes) {
                res += resultSet.getString(integer) + " - ";
            }
            System.out.println(res.substring(0, res.length() - 2));
        }
    }

    public static void execUpdateQuery(String desc, String query) throws SQLException {
        System.out.println(desc);
        statement.executeUpdate(query);
    }

    public static Map<String, String> execQueryWithData(String desc, String query) throws SQLException {
        System.out.println(desc);
        Map<String, String> chartData = new HashMap<>();
        resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "  -  " + resultSet.getString(2));
            chartData.put(resultSet.getString(1), resultSet.getString(2));
        }
        return chartData;
    }

    public static void connect(String dbName) throws ClassNotFoundException, SQLException {
        System.out.println("Подключение к базе данных \"" + dbName + "\"...");
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        statement = connection.createStatement();
        System.out.println("База \"" + dbName + "\" подключена!");
    }

    // Здесь по таблицам заполняются данные
    public static void fillDataToDB(List<TableItem.Building> parsedBuildings, List<TableItem.Prefix> parsedPrefixes) {
        System.out.println("Заполнение базы данных...");
        int querySize = 800;
        int sizePrefixes = parsedPrefixes.size();
        int sizeBuildings = parsedBuildings.size();

        fillPrefixesTable(parsedPrefixes, querySize, sizePrefixes);
        fillBuildingsTable(parsedBuildings, querySize, sizeBuildings);

        System.out.println("Заполнение базы данных прошло успешно!");
    }

    private static void fillBuildingsTable(List<TableItem.Building> parsedBuildings, int querySize, int sizeBuildings) {
        for (int i = 0; i < sizeBuildings; i += querySize)
            try {
                System.out.print("Процесс заполнения таблицы зданий:" + String.format("%.2f", (float) i / (float) sizeBuildings * 100.0f) + "%\r");

                String query = "BEGIN TRANSACTION;\n";
                for (int j = 0; j < querySize; j++) {
                    TableItem.Building building = parsedBuildings.get(i + j);
                    query += "INSERT INTO 'Buildings' ('id', 'number', 'address', 'buildingTypeMaterial', 'buildingTypeHabited'," +
                            " 'yearConstruction', 'buildingTypeFloors', 'description')" +
                            " VALUES (" + building.id_ + ", '" + building.number + "', '" + building.address + "', '" +
                            building.buildingTypeMaterial + "', " + (building.buildingTypeHabited ? 1 : 0) + ", '" +
                            building.yearConstruction + "', " + building.buildingTypeFloors + ", '" + building.description + "');\n";
                }
                statement.executeUpdate(query + "COMMIT;");
            } catch (Exception e) {
            }
    }

    private static void fillPrefixesTable(List<TableItem.Prefix> parsedPrefixes, int querySize, int sizePrefixes) {
        for (int i = 0; i < sizePrefixes; i += querySize)
            try {
                System.out.print("Процесс заполнения таблицы префиксов:" + String.format("%.2f", (float) i / (float) sizePrefixes * 100.0f) + "%\r");

                String query = "BEGIN TRANSACTION;\n";
                for (int j = 0; j < querySize; j++) {
                    TableItem.Prefix prefix = parsedPrefixes.get(i + j);
                    query += "INSERT INTO 'Prefixes' ('prefixCode', 'id_', 'number')" +
                            " VALUES (" + prefix.prefix_code + ", " + prefix.id_ + ", '" + prefix.number + "');\n";
                }
                statement.executeUpdate(query + "COMMIT;");
            } catch (Exception e) {
            }
    }

    // Здесь происходит создание таблиц в случае если их нет (бд пустая)
    public static void createStructure() throws SQLException {
        execUpdateQuery(
                "Настройка базы данных...",
                """
                        PRAGMA foreign_keys=on;
                        create table if not exists [Buildings] (
                        [id] INTEGER  NULL,
                        [number] VARCHAR(20)  NULL PRIMARY KEY,
                        [address] TEXT  NULL,
                        [buildingTypeMaterial] TEXT  NULL,
                        [buildingTypeHabited] BOOLEAN  NULL,
                        [yearConstruction] TEXT  NULL,
                        [buildingTypeFloors] INTEGER  NULL,
                        [description] TEXT  NULL,
                        FOREIGN KEY (number) REFERENCES Prefixes(number)
                        );
                        create table if not exists [Prefixes] (
                        [prefixCode] INTEGER  NULL,
                        [id_] INTEGER  NULL,
                        [number] VARCHAR(20) PRIMARY KEY UNIQUE NOT NULL
                        );"""
        );
    }
}
