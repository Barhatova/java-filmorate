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
        userValidate(user);
        log.info("Запрос на добавление пользователя {}", user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user = userStorage.createUser(user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        userValidate(user);
        checkId(user.getId());
        log.info("Запрос на обновление пользователя {}", user);
        User oldUser = userStorage.getUserId(user.getId()).get();
        oldUser = oldUser.toBuilder()
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .name(user.getName())
                .build();
        userStorage.updateUser(oldUser);
        log.info("Обновлен пользователь {}", user);
        return user;
    }

    public Collection<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public void checkId(long id) {
        if (userStorage.getUserId(id).isEmpty()) {
            log.warn("Пользователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    public void addFriend(long userId, long friendId) {
        log.info("Запрос на добавление в друзья пользователем {} пользователя {}", userId, friendId);
        checkId(userId);
        checkId(friendId);
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
        log.info("Пользователь {} добавлен в друзья пользователя {}", userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        log.info("Запрос на удаление из друзей пользователем {} пользователя {}", userId, friendId);
        checkId(userId);
        checkId(friendId);
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
    }

    public Set<User> getCommonFriends(long id, long otherId) {
        log.info("Запрос на получение списка общих друзей пользователей {} и {}", id, otherId);
        checkId(id);
        checkId(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public Set<User> getAllFriends(long id) {
        log.info("Запрос на получение списка друзей пользователя {}", id);
        checkId(id);
        return userStorage.getAllFriends(id);
    }

    private void userValidate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                !(user.getEmail().matches("^(.+)@(\\S+)$"))) {
            log.warn("Имейл пользователя введен некорректно {}", user);
            throw new ValidationException("Имейл пользователя введен некорректно");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ") || user.getLogin().isBlank() ||
                !(user.getLogin().matches("^(.+)$"))) {
            log.warn("Логин пользователя введен некоректно {}", user);
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
}