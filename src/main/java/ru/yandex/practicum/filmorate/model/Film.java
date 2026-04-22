package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive
    int duration;
}
