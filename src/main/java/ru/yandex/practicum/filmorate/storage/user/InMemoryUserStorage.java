package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с id = " + user.getId() + " не существует.");
        }
        users.put(user.getId(), user);
        log.info("Пользователь {} обновлен", user);
        return user;
    }

    @Override
    public User findById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", id));
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", id));
        }
    }
}


