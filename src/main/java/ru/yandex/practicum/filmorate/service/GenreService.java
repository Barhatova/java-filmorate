package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;

    public Collection<Genre> getAllGenres() {
        log.info("Запрос на получение списка жанров");
        return genreRepository.getAllGenres();
    }

    public Genre getGenreById(int id) {
        Optional<Genre> genreOptional = genreRepository.getGenreById(id);
        if (genreOptional.isEmpty()) {
            log.error("Жанр с ID {} не найден", id);
            throw new NotFoundException("Жанр с id = " + id + " не найден");
        }
        return genreOptional.get();
    }
}
