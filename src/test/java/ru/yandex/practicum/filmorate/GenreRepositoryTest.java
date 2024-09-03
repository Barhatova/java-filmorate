package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreMapper.class})
public class GenreRepositoryTest {
    private final GenreRepository genreRepository;

    @Test
    public void shouldGetAllGenres() {
        Collection<Genre> genres = genreRepository.getAllGenres();
        assertThat(genres.size() == 6);
        Genre genre = new Genre(1, "Комедия");
        assertThat(genres.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(genre);
    }

    @Test
    public void shouldGetGenreById() {
        Optional<Genre> genreOptional = genreRepository.getGenreById(1);
        Genre genre = new Genre(1, "Комедия");
        assertThat(genreOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(genre);
    }
}
