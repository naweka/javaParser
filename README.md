# Ход выполнения работы Java Parser

## Подготовка

### Загрузка необходимых файлов
Перед началом работы нужно подготовить всё необходимое.
Поэтому мы начнём с загрузки таблицы CSV (https://drive.google.com/file/d/1RrNeJwPdIjA1dvPEX2bLHD3TQJw46tmm/view?usp=sharing)

Теперь загрузим нашу таблицу в MS Excel и посмотрим на содержимое:
![image](https://user-images.githubusercontent.com/92515117/147056586-f09a7a73-4c2b-4c45-8d7a-58249522f44b.png)

### Анализ данных
Вот список полей, которые нам доступны:

| Поле | Описание |
| ---- | -------- |
| number | (кадастровый номер?) |
| address | полный адрес здания |
| snapshot | тип здания (тип материала, жилой/нежилой) |
| appelation | описание |
| number_of_floor | количество этажей в здании |
| prefix_code | префикс |
| building_type | тип здания (жилое/нежилое) |
| id_ |  |
| year_construction | дата постройки (и реконструкции) |

### Разработка структуры будущей базы данных

Сущность *Префикс*:
* \[ключ\] prefix_code (префикс)
* number
* id_

Сущность *Здание*:
* \[ключ\] number
* id_
* address (адрес здания)
* material (материал, из которого построено здание)
* habited (жилое ли здание?)
* year_construction (год постройки)
* number_of_floor (количество этажей)
* description (описание назначения здания)

> _Примечание: так как связть между сущностями "1 ко многим", то промежуточная таблица не требуется_

### Заключительные штрихи
Пересохраним CSV файл с другим разделителем для удобства парсинга.
На этом приготовления закончены, начинаем писать проект.

## Парсинг данных

### Создание базового парсера

Напишем парсер CSV. Работает он так:

Считаем файл, будем проходиться построчно:

```java
    public static void main(String[] args) throws IOException {
        Utils.parseData("Книга1.csv");
        List<Building> parsedBuildings = Utils.getParsedBuildings();
        List<Prefix> parsedPrefixes = Utils.getParsedPrefixes();

        for (int i = 10000; i < 20000; i += 100) {
            System.out.println(parsedBuildings.get(i).toString());
        }
    }
```

Затем обрабатываем строку, достаем из нее все данные, которые "можно унести с собой":

```java
    // Главный метод парсера
    public static void parseData(String path) throws IOException {
        System.out.println("Запущен процесс парсинга");
        List<String> fileLines = Files.readAllLines(Paths.get(path));
        for (String line : fileLines) {
            processParsedLine(line);
        }
        System.out.println("Парсинг завершен");
    }
```
```java
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
```

По окончании этого этапа мы имеем следующее:

![image](https://user-images.githubusercontent.com/92515117/147113799-3cc4a71b-f9cd-49c4-8041-949214a58e4a.png)

## Создание базы данных SQLite и подключение ее к проекту

### Создание файла базы данных

Для создания воспользуемся утилитой [SQLite Administrator](http://sqliteadmin.orbmu2k.de/)
Интерфейс выглядит достаточно просто:

![image](https://user-images.githubusercontent.com/92515117/147115700-2764cdee-0519-4ef3-be72-0794a62e436c.png)

Создадим файл базы, руководствуясь [этой](https://habr.com/ru/sandbox/88039/) статьёй

### Подключение базы к проекту

Загрузим библиотеку для работы с SQLite [здесь](http://www.java2s.com/Code/Jar/s/Downloadsqlitejdbc372jar.htm#google_vignette)

Теперь проект выглядит вот так:

![image](https://user-images.githubusercontent.com/92515117/147116585-ea45ba6a-2b50-41cb-8da7-56af2ee2d2a5.png)

Как видим, здесь присутствует всё необходимое

Подключаем базу данных:

```java
    public static void connect(String dbName) throws ClassNotFoundException, SQLException {
        System.out.println("Подключение к базе данных \""+dbName+"\"...");
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
        statement = connection.createStatement();
        System.out.println("База \""+dbName+"\" подключена!");
    }
```

Теперь создадим структуру базы данных с помощью SQLite Admin:

```java
    public static void createStructure() throws ClassNotFoundException, SQLException {
        System.out.println("Настройка базы данных...");
        statement.executeUpdate(
                "PRAGMA foreign_keys=on;\n"+
                "create table if not exists [Buildings] (\n" +
                "[id] INTEGER  NULL,\n" +
                "[number] VARCHAR(20)  NULL PRIMARY KEY,\n" +
                "[address] TEXT  NULL,\n" +
                "[buildingTypeMaterial] TEXT  NULL,\n" +
                "[buildingTypeHabited] BOOLEAN  NULL,\n" +
                "[yearConstruction] TEXT  NULL,\n" +
                "[buildingTypeFloors] INTEGER  NULL,\n" +
                "[description] TEXT  NULL,\n" +
                "FOREIGN KEY (number) REFERENCES Prefixes(number)\n"+
                ");\n" +
                "create table if not exists [Prefixes] (\n" +
                "[prefixCode] INTEGER  NULL,\n" +
                "[id_] INTEGER  NULL,\n" +
                "[number] VARCHAR(20) PRIMARY KEY UNIQUE NOT NULL\n" +
                ");");
        System.out.println("Процесс настройки структуры базы данных завершен");
    }
```

После выполнения можно взглянуть на полученную структуру:

![image](https://user-images.githubusercontent.com/92515117/147199541-6063f4bf-bcd6-4587-a9ff-66fe290bd78e.png)

## Заполнение базы данных

Для повышения производительности будем заполнять за раз не по одной строке , а по несколько INSERT'ов в транзакции:

```java
    private static void fillPrefixesTable(List<Prefix> parsedPrefixes, int querySize, int sizePrefixes) {
        for (int i = 0; i < sizePrefixes; i += querySize)
            try {
                System.out.print("Процесс заполнения таблицы префиксов:"+String.format("%.2f", (float)i/(float) sizePrefixes *100.0f)+"%\r");

                String query = "BEGIN TRANSACTION;\n";
                for (int j = 0; j < querySize; j++) {
                    Prefix prefix = parsedPrefixes.get(i+j);
                    query += "INSERT INTO 'Prefixes' ('prefixCode', 'id_', 'number')" +
                            " VALUES (" + prefix.prefix_code + ", " + prefix.id_ + ", '" + prefix.number + "');\n";
                }
                statement.executeUpdate(query+"COMMIT;");
            } catch (Exception e) {}
    }
```

Аналогичный код будет и для таблицы "Buildings". По окончании заполнения закомментируем вызовы методов ```createStructure``` и ```fillDataToDB```, так как их функционал нам больше не потербуется.






