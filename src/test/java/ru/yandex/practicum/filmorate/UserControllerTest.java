package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    void addUser() {
        User user = new User(1, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1));
        userController.addUser(user);
        Collection<User> users = userController.getUsers();
        assertNotNull(users, "Список пользователей пуст");
        assertEquals(1, users.size());
    }

    @Test
    void incorrectEmailUser() {
        User user = new User(2, "yandex-yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1));
        Collection<User> users = userController.getUsers();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void updateUser() {
        User user = new User(3, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(1995, 1, 1));
        userController.addUser(user);
        user.setLogin("yandexAlisa");
        userController.updateUser(user);
        Collection<User> users = userController.getUsers();
        assertEquals(1, users.size());
    }

    @Test
    void incorrectBirthdayUser() {
        User user = new User(4, "yandex@yandex.ru", "yandex", "Alisa",
                LocalDate.of(2050, 1, 1));
        Collection<User> users = userController.getUsers();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
        assertEquals(0, users.size());
    }
}
