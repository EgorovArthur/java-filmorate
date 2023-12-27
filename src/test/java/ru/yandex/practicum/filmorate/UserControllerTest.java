package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController;
    UserStorage userStorage;
    UserService userService;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void addUser() {
        User user = new User(1, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1), new HashSet<>());
        userController.addUser(user);
        Collection<User> users = userController.getUsers();
        assertNotNull(users, "Список пользователей пуст");
        assertEquals(1, users.size());
    }

    @Test
    void incorrectEmailUser() {
        User user = new User(2, "yandex-yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1), new HashSet<>());
        Collection<User> users = userController.getUsers();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void updateUser() {
        User user = new User(3, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1), new HashSet<>());
        userController.addUser(user);
        user.setLogin("yandexAlisa");
        userController.updateUser(user);
        Collection<User> users = userController.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    void incorrectBirthdayUser() {
        User user = new User(4, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(2050, 1, 1), new HashSet<>());
        Collection<User> users = userController.getUsers();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals(0, users.size());
    }
}
