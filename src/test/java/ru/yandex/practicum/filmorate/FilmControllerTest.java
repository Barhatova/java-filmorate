package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final FilmService filmService;

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

