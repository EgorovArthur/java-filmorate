package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        UserValidation.validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        UserValidation.validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с id = " + user.getId() + " не существует.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} обновлен", user);
        return user;
    }

}
