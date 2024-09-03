package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userRepository") UserStorage userStorage) {
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
        userValidate(user);
        checkId(user.getId());
        if (user.getId() == 0) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
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

    public void checkId(int id) {
        if (userStorage.getUserId(id).isEmpty()) {
            log.warn("Пользователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    public void addFriend(int id, int friendId) {
        log.info("Запрос на добавление в друзья пользователем {} пользователя {}", id, friendId);
        checkId(id);
        checkId(friendId);
        if (!friendReciprocity(id, friendId)) {
            userStorage.addFriend(id, friendId);
        } else {
            throw new ValidationException("Пользователи уже дружат");
        }
        log.info("Пользователь {} добавлен в друзья пользователя {}", id, friendId);
    }

    public void deleteFriend(int id, int friendId) {
        log.info("Запрос на удаление из друзей пользователем {} пользователя {}", id, friendId);
        checkId(id);
        checkId(friendId);
        if (friendReciprocity(friendId, id)) {
            userStorage.deleteFriend(id, friendId);
        }
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Запрос на получение списка общих друзей пользователей {} и {}", id, otherId);
        checkId(id);
        checkId(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public Collection<User> getAllFriends(int id) {
        log.info("Запрос на получение списка друзей пользователя {}", id);
        checkId(id);
        return userStorage.getAllFriends(id);
    }

    public User getUserId(int id) {
        Optional<User> userOptional = userStorage.getUserId(id);
        if (userOptional.isEmpty()) {
            log.error("Пользователь с ID {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userOptional.get();
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

    public boolean friendReciprocity(int id, int friendId) {
       /* checkId(id);
        checkId(friendId);*/
        return userStorage.getAllFriends(friendId).stream().anyMatch(user -> user.getId() == id);
    }
}