# РБПО — лабораторная работа №2

**Вариант 27. Редакция новостного издания**
Java 21 · Spring Boot 3.5.5 · Maven

## Реализованные требования

Проект реализует пять бизнес-сущностей, указанных в задании:

| Сущность | Коллекция API | Основные поля |
|---|---|---|
| Рубрика (`EditorialSection`) | `/api/editorial-sections` | `id`, `name`, `publicationAvailable` |
| Статья (`Article`) | `/api/articles` | `id`, `sectionId`, `authorId`, `title`, `text`, `status` |
| Редакторская проверка (`EditorialReview`) | `/api/editorial-reviews` | `id`, `articleId`, `decision`, `remarks`, `reviewedAt` |
| Выпуск издания (`EditionIssue`) | `/api/edition-issues` | `id`, `date`, `articleIds`, `status` |
| Пользователь (`User`) | `/api/users` | `id`, `username`, `displayName`, `email` |

Для **каждой** сущности реализованы пять операций:

| Операция | HTTP | Успешный ответ |
|---|---|---|
| Получить список | `GET /api/{resources}` | `200 OK`, JSON-массив |
| Получить по ID | `GET /api/{resources}/{id}` | `200 OK`, JSON-объект |
| Создать | `POST /api/{resources}` | `201 Created`, JSON созданного объекта с `id` |
| Частично обновить | `PATCH /api/{resources}/{id}` | `200 OK`, JSON обновлённого объекта |
| Удалить | `DELETE /api/{resources}/{id}` | `204 No Content`, без тела |

## Архитектура

Логика разделена по слоям:

- `controller` — HTTP-маршруты, приём запросов и формирование ответов;
- `service` — бизнес-правила, валидация ссылок и каскадные операции;
- `repository` — доступ к хранилищу;
- `model` — модели сущностей и перечисления;
- `dto` — входные модели запросов;
- `exception` — единый формат ошибок API;
- `config` — системные зависимости, включая серверные часы.

## Связи и целостность данных

- Статья ссылается на существующие рубрику (`sectionId`) и автора (`authorId`).
- Статью нельзя перевести в статус `PUBLISHED`, если у её рубрики `publicationAvailable` равно `false`.
- Редакторская проверка ссылается на существующую статью (`articleId`).
- Выпуск содержит список существующих статей (`articleIds`).
- При удалении статьи удаляются связанные редакторские проверки, а статья удаляется из списков выпусков.
- При удалении пользователя удаляются созданные им статьи; связанные проверки и ссылки в выпусках также очищаются.
- При удалении рубрики удаляются её статьи и связанные с ними данные.

Таким образом, каскадное удаление не оставляет висячих идентификаторов.

## Серверное время

Поле `reviewedAt` не входит в `EditorialReviewRequest`. При создании и обновлении редакторской проверки время устанавливается приложением через `Clock.systemUTC()`. Поле `reviewedAt`, если клиент всё же добавит его в JSON, игнорируется и не влияет на сохранённое время.

## Хранилище

В задании разрешено использовать коллекции или базу данных. В этой реализации используются потокобезопасные in-memory коллекции (`ConcurrentHashMap`). Поэтому данные предназначены для демонстрации API и **сбрасываются при перезапуске приложения**. Внешняя СУБД не требуется.

## Принятые проектные решения

На исходной странице задания не заданы конкретные значения статусов и полный набор полей пользователя. Для однозначной демонстрации API выбраны следующие значения:

- `ArticleStatus`: `DRAFT`, `IN_REVIEW`, `APPROVED`, `REJECTED`, `PUBLISHED`;
- `ReviewDecision`: `APPROVED`, `REJECTED`, `NEEDS_REVISION`;
- `EditionStatus`: `DRAFT`, `PUBLISHED`, `ARCHIVED`;
- пользователь: `username`, `displayName`, необязательный `email`.

Создание рубрики требует `name`; доступность публикации по умолчанию `false`. Для создания статьи нужны `sectionId`, `authorId`, `title` и `text`. Для выпуска обязателен `date`; список статей по умолчанию пустой.

## Запуск

Требуется JDK 21 и Maven 3.9 либо встроенная поддержка Maven в IntelliJ IDEA. В этом ZIP Maven Wrapper не включён.

Из корня проекта:

```powershell
mvn clean test
mvn spring-boot:run
```

Либо открой `pom.xml` в IntelliJ IDEA, дождись импорта Maven и запусти класс `RbpoLab2Application`.

Приложение стартует на `http://localhost:8080`.

## Примеры запросов

Создать рубрику:

```http
POST http://localhost:8080/api/editorial-sections
Content-Type: application/json

{
  "name": "Технологии",
  "publicationAvailable": true
}
```

Создать пользователя:

```http
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "editor1",
  "displayName": "Иван Редактор",
  "email": "editor1@example.test"
}
```

Создать статью (подставь полученные `id` рубрики и пользователя):

```http
POST http://localhost:8080/api/articles
Content-Type: application/json

{
  "sectionId": 1,
  "authorId": 1,
  "title": "Новая технология",
  "text": "Текст новости",
  "status": "DRAFT"
}
```

Создать редакторскую проверку. `reviewedAt` передавать не нужно — сервер установит время самостоятельно:

```http
POST http://localhost:8080/api/editorial-reviews
Content-Type: application/json

{
  "articleId": 1,
  "decision": "NEEDS_REVISION",
  "remarks": "Добавить источник"
}
```

Создать выпуск:

```http
POST http://localhost:8080/api/edition-issues
Content-Type: application/json

{
  "date": "2026-10-31",
  "articleIds": [1],
  "status": "DRAFT"
}
```

Частичное обновление использует `PATCH`: передавай только те поля, которые требуется изменить.

## Тестирование

Автотесты `RbpoLab2ApiTest` проверяют:

- основные CRUD-маршруты всех пяти сущностей;
- HTTP-коды успешных операций;
- присвоение времени редакторской проверки сервером;
- отклонение ссылок на несуществующие сущности;
- каскадное удаление и очистку ссылок из выпусков.

Запуск тестов:

```powershell
mvn clean test
```
