package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    Optional<User> getUserId(int id);

    void deleteUser(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    Collection<User> getCommonFriends(int id, int otherId);

    Collection<User> getAllFriends(int id);
}


