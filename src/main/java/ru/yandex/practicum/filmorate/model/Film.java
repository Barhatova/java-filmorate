package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotNull(message = "Название фильма не может быть пустым")
    private String name;
    @NotNull(message = "Описание фильма не может быть пустым")
    private String description;
    @NotNull(message = "Дата выхода не может быть пустой")
    private LocalDate releaseDate;
    @NotNull(message = "Длительность фильма не может быть пустой")
    private int duration;
    @NotNull(message = "Список лайков фильма не может быть пустым")
    private Set<Long> likes = new HashSet<>();
    Mpa mpa;
    List<Genre> genres;
}
