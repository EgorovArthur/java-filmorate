package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;


import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }

    @Test
    void addFilm() {
        Film film = new Film(1, "Аватар", "Фантастика",
                LocalDate.of(2012, 11, 11), 100);
        filmController.addFilm(film);
        Collection<Film> films = filmController.getFilms();
        assertNotNull(film, "Список фильмов пустой");
        assertEquals(1, films.size());
    }

    @Test
    void emptyNameFilm() {
        Film film = new Film(2, "", "Фантастика",
                LocalDate.of(2012, 11, 11), 100);
        Collection<Film> films = filmController.getFilms();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void incorrectDurationFilm() {
        Film film = new Film(3, "Аватар", "Фантастика",
                LocalDate.of(2012, 11, 11), -120);
        Collection<Film> films = filmController.getFilms();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, films.size());
    }

}
