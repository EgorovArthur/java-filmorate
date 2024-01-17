package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserStorage userStorage;
    private FriendsStorage friendsStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(@Valid @RequestBody User user) {
        UserValidation.validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(@Valid @RequestBody User user) {
        try {
            UserValidation.validateUser(user);
            return userStorage.updateUser(user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        try {
            friendsStorage.addFriend(userId, friendId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public User userById(Integer id) {
        try {
            return userStorage.userById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        try {
            friendsStorage.deleteFriend(userId, friendId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public Collection<User> getUsersFriends(Integer id) {
        try {
            return friendsStorage.getFriendsByUserId(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public Collection<User> findCommonFriends(int userId, int otherUserId) {
        try {
            return friendsStorage.commonFriends(userId, otherUserId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

}
