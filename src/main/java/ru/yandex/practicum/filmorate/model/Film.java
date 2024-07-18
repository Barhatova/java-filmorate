package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Data
@Slf4j
public class Film {
    private Long id;
    @NotNull(message = "Название фильма не может быть пустым")
    private String name;
    @NotNull(message = "Описание фильма не может быть пустым")
    private String description;
    @NotNull(message = "Дата выхода не может быть пустой")
    private LocalDate releaseDate;
    @NotNull(message = "Длительность фильма не может быть пустой")
    private int duration;
}
