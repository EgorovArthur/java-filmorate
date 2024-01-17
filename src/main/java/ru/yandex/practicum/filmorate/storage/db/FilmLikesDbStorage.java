package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;

import java.util.List;

@Component
@AllArgsConstructor
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private static final String queryInsertLikeByFilmUser = "INSERT INTO likes VALUES(?, ?)";
    private static final String queryDeleteLikeByFilmUser = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
    private static final String queryFilmsByLikes = "SELECT count(user_id) AS quantity, f.*, r.rating_name " +
            "FROM likes l " +
            "RIGHT JOIN films f ON f.film_id = l.film_id  " +
            "JOIN ratings r ON r.rating_id = f.rating_id " +
            "GROUP BY f.film_id, r.rating_id " +
            "ORDER BY quantity DESC " +
            "LIMIT ?";

    @Override
    public void addLikeByFilmId(Integer filmId, Integer userId) {
        jdbcTemplate.update(queryInsertLikeByFilmUser, filmId, userId);
    }

    @Override
    public void deleteLikeByFilmId(Integer filmId, Integer userId) {
        jdbcTemplate.update(queryDeleteLikeByFilmUser, filmId, userId);
    }

    @Override
    public List<Film> topFilms(Integer count) {
        return jdbcTemplate.query(queryFilmsByLikes, filmDbStorage.filmRowMapper(), count);
    }
}
