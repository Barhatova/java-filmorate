package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Film> films = new ArrayList<>();
        Film film = new Film();
        while (rs.next()) {
            Integer id = rs.getInt("film_id");
            if (films.stream().noneMatch(o -> o.getId() == id)) {
                film = new Film();
                film.setId(id);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
                if (rs.getLong("genre_id") != 0) {
                    Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                    ArrayList<Genre> genres = new ArrayList<>();
                    genres.add(genre);
                    film.setGenres(new ArrayList<>(genres));
                }
            } else {
                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
                film.getGenres().add(genre);
            }
                films.add(film);
            }
            return films;
        }
    }
