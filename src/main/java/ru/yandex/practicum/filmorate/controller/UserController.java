package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsersList() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        checkUserNameToBlank(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь id={} login={}", user.getId(), user.getLogin());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {

        if (newUser.getId() == null) {
            log.warn("При обновлении пользователя не указан ID");
            throw new ValidationException("Id должен быть указан");
        }
        checkUserNameToBlank(newUser);
        User existingUser = users.get(newUser.getId());
        if (existingUser != null) {
            users.put(newUser.getId(), newUser);
            log.info("Пользователь id={} name={} обновлен", newUser.getId(), newUser.getName());
            return newUser;
        }
        log.warn("Пользователь с id={} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с ID = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxID = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxID;
    }

    private void checkUserNameToBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя не указано, вместо него будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
