# Java-filmorate

Данный проект представляет собой бекенд веб-сервиса для работы с фильмами, пользователями и отзывами, а также для управления связями между ними. Основной целью этого проекта является предоставление пользователю возможности удобно и эффективно управлять информацией о фильмах, обмениваться отзывами и рекомендациями с другими пользователями, а также находить интересные фильмы на основе их предпочтений.
Сервис позволяет пользователям выбирать фильмы, обмениваться мнениями и рекомендациями, находить новые интересные фильмы и узнавать о последних событиях в кругу своих друзей.

## Структура приложения:

Пакет 'controller': Здесь размещаются классы контроллеров, которые обрабатывают HTTP-запросы и управляют взаимодействием с клиентом.

Пакет 'service': В этом пакете содержатся интерфейсы и классы сервисов, которые реализуют бизнес-логику приложения.

Пакет 'storage': Здесь размещаются интерфейсы и классы репозиториев, которые предоставляют абстракцию для доступа к базе данных.

Пакет 'model': В этом пакете определяются классы приложения. Они представляют данные, с которыми работает приложение.

Пакет 'exception': В этом пакете определены классы исключений, которые обрабатываются в приложении.

### Реализованы следующие эндпоинты:

#### 1. Фильмы

POST /films - создание фильма
PUT /films - редактирование фильма
GET /films - получение списка всех фильмов
GET /films/{id} - получение информации о фильме по его id
DELETE /films/{id} - удаление фильма по id
GET /films/common?userId={userId}?friendId={friendId} - получение общих фильмов пользователя и его друга

#### 2. Пользователи
POST /users - создание пользователя
PUT /users - редактирование пользователя
GET /users - получение списка всех пользователей
DELETE /users/{userId} - удаление пользователя по id
GET /users/{id} - получение данных о пользователе по id
PUT /users/{id}/friends/{friendId} — добавление в друзья
DELETE /users/{id}/friends/{friendId} — удаление из друзей
GET /users/{id}/friends — возвращает список друзей
GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем
GET /users/{id}/recommendations - получение рекомендаций по фильмам

#### 3. Жанры
GET /genres - получение списка всех жанров
GET /genres/{id} - получение жанра по id

#### 4. MPA рейтинг
GET /mpa - получение списка всех рейтингов
GET /mpa/{id} - получение рейтинга по id

## Cтек: 
Java,
Spring Boot,
Maven,
JUnit,
REST API,
JDBC