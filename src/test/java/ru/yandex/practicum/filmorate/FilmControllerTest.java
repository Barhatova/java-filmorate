package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private final FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

    @Test
    void testNotEmptyName() {
        Film film = Film.builder()
                .name("")
        .description("Description")
        .duration(100)
        .releaseDate(LocalDate.parse("2000-01-01"))
                .build();
        assertThrows(ValidationException.class, () -> filmService.createFilm(film), "Название не может быть пустым");
    }

    @Test
    void testNotReleaseDateBefore() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .duration(100)
                .releaseDate(LocalDate.parse("1800-01-01"))
                        .build();
        assertThrows(ValidationException.class, () -> filmService.createFilm(film), "Дата релиза — не раньше " +
                "28 декабря 1895 года");
    }

    @Test
    void testNotDurationIsNegative() {
        Film film = Film.builder()
        .name("Name")
                .description("Description")
                .duration(-100)
                .releaseDate(LocalDate.parse("1800-01-01"))
                        .build();
        assertThrows(ValidationException.class, () -> filmService.createFilm(film), "Продолжительность фильма должна" +
                "быть положительным числом");
    }
}

