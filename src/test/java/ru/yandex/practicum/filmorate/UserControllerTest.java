package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private final UserController controller = new UserController();
    User user = new User();

    @Test
    void testNotEmptyEmail() {
        user.setEmail("");
        user.setName("Name");
        user.setLogin("a");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(user), "Электронная почта не может быть " +
                "пустой и должна содержать символ @");
    }

    @Test
    void testNotEmptyLogin() {
        user.setEmail("abc@yandex.ru");
        user.setName("Name");
        user.setLogin("");
        user.setBirthday(LocalDate.parse("2000-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(user), "Логин не может быть пустым и " +
                "содержать пробелы");
    }

    @Test
    void testNotBirthdayIsFuture() {
        user.setEmail("abc@yandex.ru");
        user.setName("Name");
        user.setLogin("a");
        user.setBirthday(LocalDate.parse("3000-01-01"));
        assertThrows(ValidationException.class, () -> controller.create(user), "Дата рождения не может быть в " +
                "будущем");
    }
}
