package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
@Qualifier("userRepository")
public class UserRepository extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_QUERY = "INSERT INTO users (name,email,login,birthday) VALUES (?,?,?,?)";
    private static final String INSERT_FRIEND_QUERY = "MERGE INTO friends (user_id,friend_id) VALUES (?,?)";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String GET_USER_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_ALL_FRIENDS_QUERY = "SELECT f.friend_id AS user_id, u.email, u.login, " +
            "u.name, u.birthday FROM friends AS f JOIN users AS u ON f.friend_id = u.user_id WHERE f.user_id = ?";
    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT f.friend_id AS user_id, u.email, u.login, " +
            "u.name, u.birthday  FROM friends AS f JOIN users AS u ON f.friend_id = u.user_id " +
            "WHERE f.user_id= ? AND f.friend_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ?  " +
            "WHERE user_id = ?";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String UPDATE_FRIEND_QUERY = "UPDATE friends SET user_id = ?, friend_id = ?" +
            "WHERE user_id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User createUser(User user) {
        Integer id = insert(INSERT_QUERY,
            user.getName(),
            user.getEmail(),
            user.getLogin(),
            Timestamp.valueOf(user.getBirthday().atStartOfDay()));
            user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY,
            user.getName(),
            user.getEmail(),
            user.getLogin(),
            Timestamp.valueOf(user.getBirthday().atStartOfDay()),
            user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(GET_ALL_USERS_QUERY);
    }

    @Override
    public Optional<User> getUserId(int id) {
        return findOne(GET_USER_ID_QUERY, id);
    }

    @Override
    public void deleteUser(int id) {
        update(DELETE_USER_QUERY, id);
    }

    @Override
    public void addFriend(int id, int friendId) {
        update(INSERT_FRIEND_QUERY, id, friendId);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        update(DELETE_FRIEND_QUERY, id, friendId);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, id, otherId);
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        return findMany(GET_ALL_FRIENDS_QUERY, id);
    }

    @Override
    public void updateFriend(int id, int friendId) {
        update(UPDATE_FRIEND_QUERY, id, friendId);
    }
}
