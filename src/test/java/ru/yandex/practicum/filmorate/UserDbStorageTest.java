package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;
    private User user;

    @Autowired
    void set() {
        userDbStorage = new UserDbStorage(jdbcTemplate);

        user = new User(0,"alex121@mail.ru", "Alex121", "Alexander Ivanov",
                LocalDate.of(1999,2,2));
    }

    @Test
    @DirtiesContext
    public void testFindUserById() {
        userDbStorage.addUser(user);

        User savedUser = userDbStorage.userById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DirtiesContext
    public void testFindAll() {
        userDbStorage.addUser(user);

        Collection<User> savedUsers = new ArrayList<>(userDbStorage.getUsers());

        assertThat(savedUsers.size())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void testCreate() {
        User newUser = userDbStorage.addUser(user);

        assertThat(newUser.getId())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void testUpdate() {
        userDbStorage.addUser(user);
        user.setName("No name");

        userDbStorage.updateUser(user);

        assertThat(userDbStorage.userById(1).getName())
                .isEqualTo("No name");
    }


}
