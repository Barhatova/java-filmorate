package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == 0 || !films.containsKey(film.getId())) {
            throw new NotFoundException("Id пользователя должен быть указан");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        if(films.isEmpty()) {
            log.info("Список фильмов пуст");
            throw new NotFoundException("Список фильмов пуст");
        }
        return films.values();
    }

    @Override
    public Optional<Film> getFilmId(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addLike(long id, long userId) {
        Film film = films.get(id);
        Set<Long> likeSet = new HashSet<>();
        if (film.getLikes() != null) {
            likeSet = film.getLikes();
        }
        likeSet.add(userId);
        film.setLikes(likeSet);
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = films.get(id);
        Set<Long> likeSet;
        if (film.getLikes() == null) {
            return;
        } else {
            likeSet = film.getLikes();
        }
        likeSet.remove(userId);
        film.setLikes(likeSet);
    }

    @Override
    public Collection<Film> getFilmsTop(long count) {
        return films.values().stream()
                .sorted(new Comparator<Film>() {
                    @Override
                    public int compare(Film o1, Film o2) {
                        if (o1.getLikes() == null) {
                            return o2.getLikes() == null ? 0 : 1;
                        } else if (o2.getLikes() == null) {
                            return -1;
                        }
                        return o2.getLikes().size() - o1.getLikes().size();
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}