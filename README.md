# Ход выполнения работы Java Parser

## Подготовка

### Загрузка необходимых файлов
Перед началом работы нужно подготовить всё необходимое.
Поэтому мы начнём с загрузки таблицы CSV (https://drive.google.com/file/d/1RrNeJwPdIjA1dvPEX2bLHD3TQJw46tmm/view?usp=sharing)

Теперь загрузим нашу таблицу в MS Excel и посомтрим на содержимое:
![image](https://user-images.githubusercontent.com/92515117/147056586-f09a7a73-4c2b-4c45-8d7a-58249522f44b.png)

### Анализ данных
Вот список полей, которые нам доступны:

| Поле | Описание |
| ---- | -------- |
| number |  |
| address | полный адрес здания |
| snapshot | тип здания (более подробный) |
| appelation | описание |
| number_of_floor | количество этажей в здании |
| prefix_code | префикс |
| building_type | тип здания (жилое/нежилое) |
| id_ |  |
| year_construction | дата постройки (и реконструкции) |
