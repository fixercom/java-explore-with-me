# java-explore-with-me

#### Explore-with-me является афишей, в которой можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём. Приложение состит из двух сервисов:
- Основной сервис - содержит всё необходимое для работы продукта;
- Сервис статистики - хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.

API основного сервиса разделено на три части: публичную, закрытую (только для зарегистрированных пользователей) и административную.

Публичная часть:
- Получение подборок событий;
- Просмотр категорий событий;
- Получение событий с возможностью фильтрации;
- Получение подробной информации об опубликованном событии по его идентификатору;
- Получение топ событий.

Закрытая часть:
- Добавление/удаление/редактирование/просмотр собственных событий;
- Получение информации о запросах на участие в событии текущего пользователя;
- Подтверждение/отмена заявок на участии в событии текущего пользователя;
- Добавление запроса на участие в событии от текущего пользователя;
- Отмена своего запроса на участие в событии;
- Получение информации о заявках текущего пользователя на участие в чужих событиях;
- Добавление/удаление лайка событию.

Административная часть:
- Добавление/изменение/удаление категорий;
- Поиск событий;
- Редактирование данных события и его статуса (отклонение/публикация);
- Добавление/удаление/просмотр пользователей;
- Добавление/редактирование/удаление подборок событий.

Развернуть приложение можно с помощью файла docker-compose.yml

Tехнологический стек: Java 11, Spring Boot, Spring web MVC, Spring Data JPA, Postgres, Lombok, MapStruct, QueryDSL, JUnit5

![Схема БД](https://github.com/fixercom/sql_img_schemes/raw/master/schema_ewm.png)