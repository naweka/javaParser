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
* \[ключ\] prefix_code

Сущность *Здание*:
* \[ключ\] number
* \[ключ\] id_
* address
* material
* habited
* year_construction
* number_of_floor
* appelation
* prefix_code

### Заключительные штрихи
Пересохраним CSV файл с другим разделителем для удобства парсинга.
На этом приготовления закончены, начинаем писать проект

## Парсинг данных

Напишем парсер CSV. Работает он так:

Считаем файл, будем проходиться построчно:
![image](https://user-images.githubusercontent.com/92515117/147103365-05686280-172e-4c77-8b01-f23d7de6ed4f.png)

Затем обрабатываем строку, достаем из нее все данные, которые "можно унести с собой":
![image](https://user-images.githubusercontent.com/92515117/147103645-f6d91658-4b54-4dc5-a1aa-8ae8865bad20.png)





