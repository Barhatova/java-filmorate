package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long counter = 1;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Поступил запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Поступил запрос на создание нового фильма", film);
        filmValidate(film);
        film.setId(counter++);
        films.put(film.getId(), film);
        log.info("Создание нового фильма прошло успешно", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Поступил запрос на обновление фильма", film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким id не найден");
        }
        filmValidate(film);
        films.put(film.getId(), film);
        log.info("Обновление фильма прошло успешно", film);
        return film;
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

