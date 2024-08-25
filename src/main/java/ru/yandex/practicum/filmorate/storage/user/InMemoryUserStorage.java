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
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @Override
    public User createUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new NotFoundException("Id пользователя должен быть указан");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        if (users.isEmpty()) {
            log.info("Список пользователей пуст");
            throw new NotFoundException("Список пользователей пуст");
        }
        return users.values();
    }

    @Override
    public Optional<User> getUserId(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUser(int id) {
        if (getUserId(id).isEmpty() || getUserId(id) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        users.remove(id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        User user = users.get(id);
        Set<Integer> friendSet;
        if (user.getFriends() == null) {
            friendSet = new HashSet<>();
        } else {
            friendSet = new HashSet<>(user.getFriends());
        }
        friendSet.add(friendId);
        user.setFriends(friendSet);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);
        if (user.getFriends() == null) {
            return;
        }
        if (user == null || friend == null) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Set<Integer> friendSet1;
        Set<Integer> friendSet2;
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
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    @Override
    public Set<User> getCommonFriends(int id, int otherId) {
        Set<Integer> friends = users.get(otherId).getFriends();
        return users.get(id).getFriends().stream()
                .filter(friends::contains)
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getAllFriends(int id) {
        if (users.get(id).getFriends() == null) {
            return new HashSet<>();
        }
        return users.get(id).getFriends().stream()
                .map(users::get).collect(Collectors.toSet());
    }
}