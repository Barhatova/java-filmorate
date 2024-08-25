package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    Collection<Film> getFilmsTop(int count);
}
