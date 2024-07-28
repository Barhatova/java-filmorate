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
        log.info("Запрос на создание фильма {}", film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Создание фильма прошло успешно {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Запрос на обновление фильма {}", film);
        if (film.getId() == 0 || !films.containsKey(film.getId())) {
            throw new NotFoundException("Id пользователя должен быть указан");
        }
        films.put(film.getId(), film);
        log.info("Обновление фильма прошло успешно {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение списка всех фильмов");
        if(films.isEmpty()) {
            throw new NotFoundException("Список фильмов пуст");
        }
        log.info("Получение списка всех фильмов прошло упешно");
        return films.values();
    }

    @Override
    public Optional<Film> getFilmId(long id) {
        log.warn("Запрос на получение фильма по id {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void addLike(long id, long userId) {
        log.warn("Запрос на добавление лайка к фильму {} пользователем {}", id, userId);
        Film film = films.get(id);
        List<Long> likeList = new ArrayList<>();
        if (film.getLikes() != null) {
            likeList = film.getLikes();
        }
        likeList.add(userId);
        film.setLikes(likeList);
        log.warn("Добавление лайка к фильму {} пользователем {} прошло успешно", userId, id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        log.warn("Запрос на удаление лайка к фильму {} пользователем {}", id, userId);
        Film film = films.get(id);
        List<Long> likeList;
        if (film.getLikes() == null) {
            return;
        } else {
            likeList = film.getLikes();
        }
        likeList.remove(userId);
        film.setLikes(likeList);
        log.warn("Удаление лайка к фильму {} пользователем {} прошло успешно", userId, id);
    }

    @Override
    public Collection<Film> getFilmsTop(long count) {
        log.info("Запрос на получение списка популярных фильмов");
        log.info("Получение списка популярных фильмов прошло упешно");
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


