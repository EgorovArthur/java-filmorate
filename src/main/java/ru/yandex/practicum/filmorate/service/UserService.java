package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(@Valid @RequestBody User user) {
        UserValidation.validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(@Valid @RequestBody User user) {
        UserValidation.validateUser(user);
        return userStorage.updateUser(user);
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        try {
            User user = userStorage.findById(userId);
            User friend = userStorage.findById(friendId);
            user.setFriends(friendId);
            friend.setFriends(userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public User findById(Integer id) {
        try {
            return userStorage.findById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        try {
            User user = userStorage.findById(userId);
            User friend = userStorage.findById(friendId);
            user.deleteFriend(friendId);
            friend.deleteFriend(userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public List<User> getUsersFriends(Integer id) {
        try {
            User user = userStorage.findById(id);
            return user.getFriends()
                    .stream()
                    .map(userStorage::findById)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

    public Collection<User> findCommonFriends(int userId, int otherUserId) {
        try {
            User user = userStorage.findById(userId);
            User otherUser = userStorage.findById(otherUserId);
            Set<Integer> userFriends = user.getFriends();
            Set<Integer> otherUserFriends = otherUser.getFriends();
            return userFriends.stream()
                    .filter(otherUserFriends::contains)
                    .map(userStorage::findById)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден", e);
        }
    }

}
