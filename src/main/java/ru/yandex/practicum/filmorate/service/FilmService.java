package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        log.info("Запрос на добавление фильма {}", film);
        filmValidate(film);
        film = filmStorage.createFilm(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Id фильма должен быть указан");
        }
        filmValidate(film);
        getFilmId(film.getId());
        log.info("Запрос на обновление фильма {}", film);
        Film oldFilm = filmStorage.getFilmId(film.getId()).get();
        oldFilm = oldFilm.toBuilder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();
        filmStorage.updateFilm(oldFilm);
        log.info("Обновлен фильм {}", film);
        return film;
    }

    public Collection<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void addLike(long id, long userId) {
        getFilmId(id);
        getUserId(userId);
        log.info("Запрос на добавление лайка к фильму {} пользователем {}", id, userId);
        filmStorage.addLike(id, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, id);
    }

    public void deleteLike(long id, long userId) {
        getFilmId(id);
        getUserId(userId);
        log.info("Запрос на удаление лайка к фильму {} ползователем {}", id, userId);
        filmStorage.deleteLike(id, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, id);
    }

    public Collection<Film> getFilmsTop(long count) {
        log.info("Запрос на получение списка популярных фильмов");
        return filmStorage.getFilmsTop(count);
    }

    private void getFilmId(long id) {
        if (filmStorage.getFilmId(id).isEmpty()) {
            log.warn("Фильм с id {} не найден", id);
            throw new NotFoundException("Фильм с id  " + id + " не найден");
        }
    }

    private void getUserId(long id) {
        if (userStorage.getUserId(id).isEmpty()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    private void filmValidate(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}