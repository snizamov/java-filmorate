package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получение списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        checkReleaseDateOfFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Создан фильм id={} name={}", film.getId(), film.getName());
        return film;
    }


    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("При обновлении фильма не указан ID");
            throw new ValidationException("Id должен быть указан");
        }

        Film existingFilm = films.get(newFilm.getId());

        if (existingFilm != null) {
            checkReleaseDateOfFilm(newFilm);
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм id={} name={} обновлен", newFilm.getId(), newFilm.getName());
            return newFilm;
        }
        log.warn("Фильм с id={} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с ID = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxID = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxID;
    }

    private void checkReleaseDateOfFilm(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            log.warn("Дата релиза раньше допустимой (28.12.1895)");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}
