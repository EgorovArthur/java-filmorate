package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.*;

@Component
@AllArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private static final String queryFriendsByUserId = "select * from users where user_id in (select friend_id from "
            + "friends where user_id = ?);";
    private static final String queryInsertFriendsByUserId = "INSERT INTO friends VALUES(?,?);";
    private static final String queryDeleteFriendsByUserId = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";

    @Override
    public Collection<User> getFriendsByUserId(Integer userId) {
        return jdbcTemplate.query(queryFriendsByUserId, userDbStorage.userRowMapper(), userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        try {
            jdbcTemplate.update(queryInsertFriendsByUserId, userId, friendId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Друг с friendId = %d не существует.", friendId));
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(queryDeleteFriendsByUserId, userId, friendId);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer otherId) {
        Set<User> users = new HashSet<>(getFriendsByUserId(userId));
        Set<User> otherIds = new HashSet<>(getFriendsByUserId(otherId));
        List<User> commonFriends = new ArrayList<>();
        for (User user : otherIds) {
            if (users.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }
}
