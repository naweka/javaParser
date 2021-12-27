import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Queries {
    public static Map<String, String> getQuery1() throws SQLException {
        return Database.execQueryWithData(
                "\nДома с кол-вом этажей и их кол-во:",
                """
                        select
                            buildingTypeFloors as 'Кол-во этажей',
                            count(buildingTypeFloors) as 'Кол-во таких зданий'
                        from Buildings
                        where buildingTypeFloors > 0
                        group by buildingTypeFloors;"""
        );
    }

    public static void getQuery2() throws SQLException {
        Database.execQuery(
                "\nЗарегистрированные участки, по улице шлиссельбургское шоссе с префиксом 9881:",
                """
                        select
                            description,
                            address
                        from Buildings inner join Prefixes
                        on Buildings.number = Prefixes.number
                        where
                            address like '%лиссель%шоссе%'
                            and prefixCode = 9881
                            and description like '%арегистри%часто%';""",
                new ArrayList<>() {
                    {
                        add(1);
                        add(2);
                    }
                }
        );
    }

    public static void getQuery3() throws SQLException {
        Database.execQuery(
                "\nУниверситеты выше 5 этажа с известным годом постройки и вычислите средний prefix_code:",
                """
                        select
                            avg(prefixCode) as 'Средний префикс'
                        from Buildings inner join Prefixes
                        on Buildings.number = Prefixes.number
                        where
                            yearConstruction <> ''
                            and buildingTypeFloors > 5
                            and description like '%ниверсит%';""",
                new ArrayList<>() {
                    {
                        add(1);
                    }
                }
        );
    }
}
