package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    Optional<User> getUserId(long id);

    void deleteUser(long id);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Set<User> getCommonFriends(long id, long otherId);

    Set<User> getAllFriends(long id);
}


