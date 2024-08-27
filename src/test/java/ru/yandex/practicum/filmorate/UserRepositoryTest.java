package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserMapper.class})
public class UserRepositoryTest {
    @Qualifier("userRepository")
    private final UserRepository userRepository;

    static User getUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("email");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(2000, 01, 01));
        return user;
    }

    @Test
    public void shouldCreateUser() {
        User user1 = new User();
        user1.setId(4);
        user1.setEmail("email");
        user1.setLogin("login");
        user1.setName("name");
        user1.setBirthday(LocalDate.of(2000, 01, 01));
        userRepository.createUser(user1);
        Optional<User> userOptional = userRepository.getUserId(4);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(user1);
    }

    @Test
    public void shouldUpdateUser() {
        User user = getUser();
        user.setId(3);
        Optional<User> userOptional = userRepository.getUserId(3);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isNotEqualTo(user);
        userRepository.updateUser(user);
        userOptional = userRepository.getUserId(3);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(user);
    }

    @Test
    public void shouldGetAllUsers() {
        Collection<User> users = userRepository.getAllUsers();
        assertThat(users.size() == 1);
        assertThat(users.stream().findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void shouldGetUserId() {
        Optional<User> userOptional = userRepository.getUserId(1);
        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void shouldAddFriend() {
        Collection<User> users = userRepository.getAllFriends(3);
        assertThat(users.size() == 1);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst()).isEmpty();
        userRepository.addFriend(3, 1);
        users = userRepository.getAllFriends(3);
        assertThat(users.size() == 1);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }

    @Test
    public void shouldDeleteFriend() {
        Collection<User> users = userRepository.getAllFriends(2);
        assertThat(users.size() == 2);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
        userRepository.deleteFriend(2, 1);
        users = userRepository.getAllFriends(2);
        assertThat(users.size() == 1);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst()).isEmpty();
    }

    @Test
    public void shouldGetCommonFriends() {
        Collection<User> users = userRepository.getCommonFriends(1, 2);
        assertThat(users.size() == 1);
        assertThat(users.stream().filter(user -> user.getId() == 3).findFirst()).isPresent();
    }

    @Test
    public void shouldGetAllfriends() {
        Collection<User> users = userRepository.getAllFriends(3);
        assertThat(users.size() == 2);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst()).isEmpty();
        users = userRepository.getAllFriends(2);
        assertThat(users.size() == 2);
        assertThat(users.stream().filter(user -> user.getId() == 1).findFirst())
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(getUser());
    }
}