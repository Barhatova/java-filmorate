package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    void testNotEmptyEmail() {
        User user = User.builder()
                .email("")
                .name("Name")
                .login("a")
                .birthday(LocalDate.parse("2000-01-01"))
        .build();
        assertThrows(ValidationException.class, () -> userService.createUser(user), "Электронная почта не " +
                "может быть держать символ @");
    }

    @Test
    void testNotEmptyLogin() {
        User user = User.builder()
        .email("abc@yandex.ru")
                .name("Name")
                .login("")
                .birthday(LocalDate.parse("2000-01-01"))
        .build();
        assertThrows(ValidationException.class, () -> userService.createUser(user), "Логин не может быть пустым и " +
                "содержать пробелы");
    }

    @Test
    void testNotBirthdayIsFuture() {
        User user = User.builder()
                .email("abc@yandex.ru")
                .name("Name")
                .login("a")
                .birthday(LocalDate.parse("3000-01-01"))
        .build();
        assertThrows(ValidationException.class, () -> userService.createUser(user), "Дата рождения не может быть в " +
                "будущем");
    }
}
