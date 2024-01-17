package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresDbStorage filmGenresDbStorage;

    private static final String queryAllFilmsWithRatings = "SELECT * FROM films f JOIN ratings r ON r.rating_id = f.rating_id ORDER BY film_id;";
    private static final String queryFilmById = "SELECT * FROM films f JOIN ratings r ON r.rating_id = f.rating_id WHERE film_id = ?;";
    private static final String queryUpdateFilm = "UPDATE films SET " +
            "name = ?, " +
            "description  = ?, " +
            "release_date  = ?, " +
            "duration  = ?, " +
            "rating_id = ? " +
            "WHERE film_id = ?;";

    @Override
    public Collection<Film> getFilms() {
        return jdbcTemplate.query(queryAllFilmsWithRatings, filmRowMapper());
    }

    @Override
    public Film filmById(Integer filmId) {
        try {
            return jdbcTemplate.queryForObject(queryFilmById, filmRowMapper(), filmId);
        } catch (RuntimeException e) {
            throw new NotFoundException(String.format("Фильм с id = %d не существует.", filmId));
        }
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", film.getDuration(),
                "rating_id", film.getMpa().getId()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());

        if (film.getGenres().size() != 0) {
            filmGenresDbStorage.addGenres(film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmById(film.getId()) == null) {
            throw new NotFoundException(String.format("Фильм с id = %d не существует.", film.getId()));
        }
        jdbcTemplate.update(queryUpdateFilm, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        filmGenresDbStorage.updateGenres(film);
        return filmById(film.getId());
    }

    protected RowMapper<Film> filmRowMapper() {
        return new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

                Film film = new Film(
                        rs.getInt("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new Mpa(rs.getInt("rating_id"),
                                rs.getString("rating_name"))
                );
                film.getGenres().addAll(filmGenresDbStorage.getGenresByFilmId(film.getId()));
                return film;
            }
        };
    }
}
