package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Component
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String queryAllMpa = "SELECT * FROM ratings;";
    private static final String queryMpaById = "SELECT * FROM ratings WHERE rating_id = ?;";

    @Override
    public Collection<Mpa> findAll() {
        return jdbcTemplate.query(queryAllMpa, mpaRowMapper());
    }


    @Override
    public Mpa mpaById(Integer mpaId) {
        try {
            return jdbcTemplate.queryForObject(queryMpaById, mpaRowMapper(), mpaId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Рейтинга с id = %d не существует.", mpaId));
        }
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getInt("rating_id"), rs.getString("rating_name"));
    }
}
