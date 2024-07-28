package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilmId(long id);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Collection<Film> getFilmsTop(long count);
}
