package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User createUser(User user) {
        log.info("Запрос на создание пользователя {}", user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создание пользователя прошло успешно {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя {}", user);
        if (user.getId() == 0) {
            throw new NotFoundException("Id пользователя должен быть указан");
        }
        users.put(user.getId(), user);
        log.info("Обновление пользователя прошло успешно {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        if (users.isEmpty()) {
            log.info("Список пользователей пуст");
            throw new NotFoundException("Список пользователей пуст");
        }
        log.info("Получение списка всех пользователей прошло успешно");
        return users.values();
    }

    @Override
    public Optional<User> getUserId(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUser(long id) {
        log.info("Запрос на удаление из друзей пользователя");
        if (getUserId(id).isEmpty() || getUserId(id) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        log.info("Удаление пользователя прошло успешно");
        users.remove(id);
    }

    @Override
    public void addFriend(long id, long friendId) {
        log.info("Запрос на добавление в друзья пользователя {} пользователем {}", friendId, id);
        User user = users.get(id);
        Set<Long> friendSet;
        if (user.getFriends() == null) {
            friendSet = new HashSet<>();
        } else {
            friendSet = new HashSet<>(user.getFriends());
        }
        friendSet.add(friendId);
        user.setFriends(friendSet);
        log.info("Добавление пользователя {} в друзья пользователя {} прошло успешно", id, friendId);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        log.info("Запрос на удаление из друзей пользователя {} пользователем {}", friendId, id);
        User user = users.get(id);
        User friend = users.get(friendId);
        if (user.getFriends() == null) {
            return;
        }
        if (user == null || friend == null) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Set<Long> friendSet1;
        Set<Long> friendSet2;
        if (user.getFriends() == null || friend.getFriends() == null) {
            throw new NotFoundException("Список друзей пользователя не найден");
        } else {
            friendSet1 = user.getFriends();
            friendSet2 = friend.getFriends();
        }
        friendSet1.remove(friendId);
        user.setFriends(friendSet1);
        friendSet2.remove(id);
        friend.setFriends(friendSet2);
        log.info("Удаление пользователя {} из друзей пользователем {} прошло успешно", id, friendId);
    }

    @Override
    public Set<User> getCommonFriends(long id, long otherId) {
        log.info("Запрос на получение списка общих друзей пользователей {} и {}", id, otherId);
        log.info("Получение списка общих друзей пользователей {} и {} прошло упешно", id, otherId);
        Set<Long> friends = users.get(otherId).getFriends();
        return users.get(id).getFriends().stream()
                .filter(friends::contains)
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getAllFriends(long id) {
        log.info("Запрос на получение списка друзей пользователя {}", id);
        if (users.get(id).getFriends() == null) {
            return new HashSet<>();
        }
        log.info("Получение списка друзей пользователя {} прошло успешно", id);
        return users.get(id).getFriends().stream()
                .map(users::get).collect(Collectors.toSet());
    }
}
