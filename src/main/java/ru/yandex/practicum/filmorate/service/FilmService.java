package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage,
                       MpaRepository mpaRepository,
                       GenreRepository genreRepository) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    public Film createFilm(Film film) {
        log.info("Запрос на добавление фильма {}", film);
        filmValidate(film);
        film = filmStorage.createFilm(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmValidate(film);
        getFilmId(film.getId());
        if (film.getId() == 0) {
            throw new ValidationException("Id фильма должен быть указан");
        }
        log.info("Запрос на обновление фильма {}", film);
        Film oldFilm = filmStorage.getFilmById(film.getId()).get();
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

    public void addLike(int filmId, int userId) {
        getFilmId(filmId);
        getUserId(userId);
        log.info("Запрос на добавление лайка к фильму {} пользователем {}", filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void deleteLike(int filmId, int userId) {
        getFilmId(filmId);
        getUserId(userId);
        log.info("Запрос на удаление лайка к фильму {} пользователем {}", filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public Collection<Film> getFilmsTop(int count) {
        log.info("Запрос на получение списка популярных фильмов");
        return filmStorage.getFilmsTop(count);
    }

    public Film getFilmById(int id) {
        Optional<Film> filmOptional = filmStorage.getFilmById(id);
        if (filmOptional.isEmpty()) {
            log.error("Фильм с ID {} не найден", id);
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return filmOptional.get();
    }

    private boolean checkingTheMpa(Film film) {
        Set<Integer> mpaId = mpaRepository.getAllMpa().stream().map(Mpa::getId).collect(Collectors.toSet());
        return (film.getMpa() == null || !mpaId.contains(film.getMpa().getId()));
    }

    private void getFilmId(int id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            log.warn("Фильм с id {} не найден", id);
            throw new NotFoundException("Фильм с id  " + id + " не найден");
        }
    }

    private void getUserId(int id) {
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
        if (checkingTheMpa(film)) {
            log.warn("Некорректно введен рейтинг фильма {}", film);
            throw new ValidationException("Рейтинг фильма некорректен.");
        }
        if (film.getGenres() != null) {
            Set<Integer> genresId = genreRepository.getAllGenres().stream().map(Genre::getId).collect(Collectors.toSet());
            Set<Integer> filmsGenresId = film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
            if (!genresId.containsAll(filmsGenresId)) {
                log.warn("Некорректно введен жанр фильма {}", film);
                throw new ValidationException("Жанр фильма некорректен.");
            }
        }
    }
}