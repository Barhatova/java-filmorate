package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private final FilmController controller = new FilmController();
    Film film = new Film();

    @Test
    void testNotEmptyName() {
        film.setName("");
        film.setDescription("Description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.parse("2000-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(film), "Название не может быть пустым");
    }

    @Test
    void testNotReleaseDateBefore() {
        film.setName("Name");
        film.setDescription("Description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.parse("1800-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(film), "Дата релиза — не раньше " +
                "28 декабря 1895 года");
    }

    @Test
    void testNotDurationIsNegative() {
        film.setName("Name");
        film.setDescription("Description");
        film.setDuration(-100);
        film.setReleaseDate(LocalDate.parse("2000-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(film), "Продолжительность фильма должна" +
                "быть положительным числом");
    }
}

