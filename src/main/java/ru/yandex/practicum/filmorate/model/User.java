package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Data
@Slf4j
public class User {
    private Long id;
    @NotNull(message = "Email не может быть пустым")
    private String email;
    @NotNull(message = "Логин не может быть пустым")
    private String login;
    @NotNull(message = "Имя пользователя не может быть пустым")
    private String name;
    @NotNull(message = "Дата рождения пользователя не может быть пустой")
    private LocalDate birthday;
}
