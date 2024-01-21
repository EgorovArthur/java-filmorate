package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String queryAllGenres = "SELECT * FROM genres;";
    private static final String queryGenreById = "SELECT * FROM genres WHERE genre_id = ?;";

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.query(queryAllGenres, genreRowMapper());
    }

    @Override
    public Genre genreById(Integer genreId) {
        try {
            return jdbcTemplate.queryForObject(queryGenreById, genreRowMapper(), genreId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Жанра с id = %d не существует", genreId));
        }
    }

    protected RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
    }
}
