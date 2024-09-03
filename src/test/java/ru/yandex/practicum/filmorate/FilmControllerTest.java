package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    @Test
    void createFilm() {
        Film newFilm = Film.builder()
                .name("Film")
                .description("film")
                .releaseDate(LocalDate.of(2020, 4, 1))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        Film createdFilm = filmController.createFilm(newFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(newFilm.getName(), createdFilm.getName());
        assertEquals(newFilm.getDescription(), createdFilm.getDescription());
        assertEquals(newFilm.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(newFilm.getDuration(), createdFilm.getDuration());
        assertEquals(newFilm.getMpa().getId(), createdFilm.getMpa().getId());
    }

    @Test
    void getFilmById() {
        Film newFilm = Film.builder()
                .name("Film")
                .description("test film")
                .releaseDate(LocalDate.of(2020, 4, 1))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        Film createdFilm = filmController.createFilm(newFilm);
        Film retrievedFilm = filmController.getFilmById(createdFilm.getId());
        assertEquals(createdFilm.getId(), retrievedFilm.getId());
        assertEquals(createdFilm.getName(), retrievedFilm.getName());
        assertEquals(createdFilm.getDescription(), retrievedFilm.getDescription());
        assertEquals(createdFilm.getReleaseDate(), retrievedFilm.getReleaseDate());
        assertEquals(createdFilm.getDuration(), retrievedFilm.getDuration());
        assertEquals(createdFilm.getMpa().getId(), retrievedFilm.getMpa().getId());
    }
}