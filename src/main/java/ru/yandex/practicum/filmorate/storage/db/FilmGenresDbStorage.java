package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenresStorage;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class FilmGenresDbStorage implements FilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    private static final String queryAllGenresByFilm = "SELECT fg.genre_id, g.genre_name " +
            "FROM films_genres fg " +
            "JOIN genres g ON g.genre_id = fg.genre_id " +
            "WHERE film_id = ?";
    private static final String queryInsertGenresByFilm = "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?);";
    private static final String queryDeleteGenresByFilm = "DELETE FROM films_genres WHERE film_id = ?";

    @Override
    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        Collection<Genre> filmGenres = jdbcTemplate.query(queryAllGenresByFilm, genreDbStorage.genreRowMapper(), filmId);
        return filmGenres;
    }

    @Override
    public void addGenres(Film film) {
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(queryInsertGenresByFilm, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, genres.get(i).getId());
                ps.setInt(2, film.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public void updateGenres(Film film) {
        jdbcTemplate.update(queryDeleteGenresByFilm, film.getId());
        addGenres(film);
    }
}
