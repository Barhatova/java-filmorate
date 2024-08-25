package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmRepository")
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY = "INSERT INTO films " +
            "(name,description,release_date,duration,mpa_id) VALUES (?,?,?,?,?)";
    private static final String GENRE = "MERGE INTO film_genre (film_id,genre_id) VALUES (?,?)";
    private static final String INSERT_LIKES_QUERY = "MERGE INTO likes (film_id,user_id) VALUES (?,?)";
    private static final String REMOVE_LIKES_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.genre_name " +
            " FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id  LEFT JOIN film_genre fg ON f.film_id = fg.film_id " +
            " LEFT JOIN genre g ON fg.genre_id = g.genre_id  ORDER BY f.film_id";
    private static final String GET_FILMS_TOP_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.genre_name " +
            "FROM (SELECT f.*, COUNT (l.user_id) AS count_like FROM films f JOIN likes l ON l.film_id = f.film_id " +
            "GROUP BY l.film_id ORDER BY count_like DESC LIMIT ?) AS f JOIN mpa m ON f.mpa_id  = m.mpa_id  " +
            "LEFT JOIN film_genre fg ON f.film_id = fg.film_id LEFT JOIN genre g ON fg.genre_id = g.genre_id  " +
            "ORDER BY count_like DESC";
    private static final String GET_FILM_ID_QUERY = "SELECT f.*, m.mpa_name, fg.genre_id, g.genre_name " +
            "FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id LEFT JOIN film_genre fg ON f.film_id = fg.film_id " +
            "LEFT JOIN genre g ON fg.genre_id = g.genre_id WHERE f.film_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            " duration = ?, mpa_id = ?  WHERE film_id = ?";
    private static final String DELETE_FILMS_QUERY = "DELETE FROM film_genre WHERE film_id = ?";

    private final FilmExtractor filmExtractor;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.filmExtractor = filmExtractor;
    }

    @Override
    public Film createFilm(Film film) {
        Integer id = insert(INSERT_FILM_QUERY,
            film.getName(),
            film.getDescription(),
            Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
            film.getDuration(),
            film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                update(GENRE, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.valueOf(film.getReleaseDate().atStartOfDay()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        update(DELETE_FILMS_QUERY, film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                update(GENRE, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbc.query(GET_ALL_FILMS_QUERY, filmExtractor);
        }

    @Override
    public Optional<Film> getFilmById(int id) {
        try {
            List<Film> result = jdbc.query(GET_FILM_ID_QUERY, filmExtractor, id);
            return Optional.ofNullable(result.getFirst());
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(int id, int userId) {
        update(INSERT_LIKES_QUERY, id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        update(REMOVE_LIKES_QUERY, id, userId);
    }

    @Override
    public Collection<Film> getFilmsTop(int count) {
        return jdbc.query(GET_FILMS_TOP_QUERY, filmExtractor, count);
    }
}
