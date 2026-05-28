package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmorateApplicationTests {
    Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

	@Test
	void contextLoads() {
	}

    @Test
    void userTest_whenEmailIsBlank() {
        User user = new User();
        user.setEmail("");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void userTest_whenEmailWithoutChar() {
        User user = new User();
        user.setEmail("abc.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void userTest_whenEmailIsCorrect() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void userTest_whenLoginIsBlank() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000,1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void userTest_whenLoginWithSpace() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("lo gin");
        user.setBirthday(LocalDate.of(2000,1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("login")));
    }

    @Test
    void userTest_whenLoginIsCorrect() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    void userTest_whenBirthdayIsNull() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("login");
        user.setBirthday(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
    }

    @Test
    void userTest_whenBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("abc@bca.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2030, 1,1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
    }

    @Test
    void filmTest_whenNameIsBlank() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Очень классное кино");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void filmTest_whenDescriptionIsMore200Chars() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("12345678901234567890123456789012345678901234567890" + //в одной строке 50 символов
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "1");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void filmTest_whenReleaseDateIsNull() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("Очень классное кино");
        film.setReleaseDate(null);
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
    }

    @Test
    void filmTest_whenDurationIsNotPositive() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("Очень классное кино");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(-200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
    }

    @Test
    void filmTest_whenAllIsCorrect() {
        Film film = new Film();
        film.setName("Гарри Поттер");
        film.setDescription("Очень классное кино");
        film.setReleaseDate(LocalDate.of(2000,1,1));
        film.setDuration(200);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }
}
