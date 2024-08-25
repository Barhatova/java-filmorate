package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String GET_ALL_GENRES_QUERY = "SELECT genre_id, genre_name FROM genre";
    private static final String GET_GENRE_BY_ID_QUERY = "SELECT genre_id, genre_name FROM genre WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getAllGenres() {
        return findMany(GET_ALL_GENRES_QUERY);
    }

    public Optional<Genre> getGenreById(int id) {
        return findOne(GET_GENRE_BY_ID_QUERY,id);
    }
}
