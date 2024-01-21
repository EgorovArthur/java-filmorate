package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;

    private static final String queryAllUsers = "SELECT * FROM users;";
    private static final String queryUserById = "SELECT * FROM users WHERE user_id = ?;";
    private static final String queryUpdateUser = "UPDATE users SET " +
            "name = ?, " +
            "login = ?, " +
            "email = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?;";

    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query(queryAllUsers, userRowMapper());
    }

    @Override
    public User userById(Integer userId) {
        try {
            return jdbcTemplate.queryForObject(queryUserById, userRowMapper(), userId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден.", userId));
        }
    }

    @Override
    public User updateUser(User user) {
        if (userById(user.getId()) == null) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден.", user.getId()));
        }
        jdbcTemplate.update(queryUpdateUser, user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return userById(user.getId());
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, String> params = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday().toString()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        return user;
    }

    protected RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()
                );
                return user;
            }
        };
    }
}
