package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmMapper.class, FilmExtractor.class})
public class FilmRepositoryTest {
    @Qualifier("filmRepository")
    private final FilmRepository filmRepository;

    static Film getFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 01, 01));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        film.setGenres(genres);
        return film;
    }

    @Test
    public void shouldCreateFilm() {
        Collection<Film> films = filmRepository.getAllFilms();
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film -> film.getId() == 3).findFirst()).isEmpty();
        Film film = getFilm();
        film.setId(3);
        filmRepository.createFilm(film);
        films = filmRepository.getAllFilms();
        assertThat(films.size() == 3);
        assertThat(films.stream().filter(film1 -> film1.getId() == 3).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(film);
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = getFilm();
        film.setId(1);
        Optional<Film> films = filmRepository.getFilmById(1);
        assertThat(films)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isNotEqualTo(film);
        filmRepository.updateFilm(film);
        films = filmRepository.getFilmById(2);
        assertThat(films)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(film);
    }

    @Test
    public void shouldGetAllFilms() {
        Collection<Film> films = filmRepository.getAllFilms();
        assertThat(films.size() == 2);
        assertThat(films.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void shouldGetFilmById() {
        Optional<Film> films = filmRepository.getFilmById(1);
        assertThat(films)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void shouldAddLike() {
        Collection<Film> films = filmRepository.getFilmsTop(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst()).isEmpty();
        filmRepository.addLike(1, 2);
        films = filmRepository.getFilmsTop(1);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void shouldDeleteLike() {
        Collection<Film> films = filmRepository.getFilmsTop(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst()).isEmpty();
        filmRepository.deleteLike(2, 1);
        films = filmRepository.getFilmsTop(1);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }

    @Test
    public void shouldGetFilmsTop() {
        Collection<Film> films = filmRepository.getFilmsTop(1);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst()).isEmpty();
        films = filmRepository.getFilmsTop(5);
        assertThat(films.size() == 2);
        assertThat(films.stream().filter(film1 -> film1.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getFilm());
    }
}