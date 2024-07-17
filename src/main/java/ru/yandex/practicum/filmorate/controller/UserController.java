package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long counter = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Поступил запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Поступил запрос на создание нового пользователя", user);
        userValidate(user);
        user.setId(counter++);
        users.put(user.getId(), user);
        log.info("Создание пользователя прошло успешно", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Поступил запрос на обновление пользователя", user);
        userValidate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
        users.put(user.getId(), user);
        log.info("Обновление пользователя прошло успешно", user);
        return user;
    }

    private void userValidate(User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}

