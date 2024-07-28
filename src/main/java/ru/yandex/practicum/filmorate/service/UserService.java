package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        log.info("Запрос на создание пользователя {}", user);
        userValidate(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user = userStorage.createUser(user);
        log.info("Создание пользователя прошло успешно {}", user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {}", user);
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        userValidate(user);
        checkId(user.getId());
        User oldUser = userStorage.getUserId(user.getId()).get();
        oldUser = oldUser.toBuilder()
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .name(user.getName())
                .build();
        userStorage.updateUser(oldUser);
        log.info("Обновление пользователя прошло успешно {}", user);
        return user;
    }

    public Collection<User> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        log.info("Получение списка всех пользователей прошло успешно");
        return userStorage.getAllUsers();
    }

    private void userValidate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                !(user.getEmail().matches("^(.+)@(\\S+)$"))) {
            log.warn("Имейл пользователя введен некорректно");
            throw new ValidationException("Имейл пользователя введен некорректно");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ") || user.getLogin().isBlank() ||
                !(user.getLogin().matches("^(.+)$"))) {
            log.warn("Логин пользователя введен некоректно");
            throw new ValidationException("Логин пользователя введен некоректно");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void checkId(long id) {
        if (userStorage.getUserId(id).isEmpty()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с id {} не найден");
        }
    }

    public void addFriend(long id, long friendId) {
        log.info("Запрос на добавление в друзья пользователя {} пользователем {}", friendId, id);
        checkId(id);
        checkId(friendId);
        userStorage.addFriend(id, friendId);
        userStorage.addFriend(friendId, id);
        log.info("Добавление пользователя {} в друзья пользователя {} прошло успешно", id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        log.info("Запрос на удаление из друзей пользователя {} пользователем {}", friendId, id);
        checkId(id);
        checkId(friendId);
        userStorage.deleteFriend(id, friendId);
        userStorage.deleteFriend(friendId, id);
        log.info("Удаление пользователя {} из друзей пользователем {} прошло успешно", id, friendId);
    }

    public Set<User> getCommonFriends(long id, long otherId) {
        log.info("Запрос на получение списка общих друзей пользователей {} и {}", id, otherId);
        checkId(id);
        checkId(otherId);
        log.info("Получение списка общих друзей пользователей {} и {} прошло упешно", id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public Set<User> getAllFriends(long id) {
        log.info("Запрос на получение списка друзей пользователя {}", id);
        checkId(id);
        log.info("Получение списка друзей пользователя {} прошло успешно", id);
        return userStorage.getAllFriends(id);
    }
}
