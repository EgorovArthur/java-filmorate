package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private Film film;

    @Autowired
    void set() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmGenresDbStorage filmGenresDbStorage = new FilmGenresDbStorage(jdbcTemplate, genreDbStorage);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, filmGenresDbStorage);

        film = new Film(0, "Avatar", "Avatar description", LocalDate.of(2012, 1,
                1), 210, new Mpa(4, "R"));
    }

    @Test
    @DirtiesContext
    void testGetFilmWithId() {
        filmDbStorage.addFilm(film);

        Film saveFilm = filmDbStorage.filmById(1);

        assertThat(saveFilm)
                .isNotNull()
                .isEqualTo(film);
    }

    @Test
    @DirtiesContext
    void testAddFilmWithId() {

        Film saveFilm = filmDbStorage.addFilm(film);

        assertThat(saveFilm.getId())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void testNoNameFilm() {
        filmDbStorage.addFilm(film);
        film.setName("No movie name");

        filmDbStorage.updateFilm(film);

        assertThat(filmDbStorage.filmById(1).getName())
                .isEqualTo("No movie name");
    }
}


