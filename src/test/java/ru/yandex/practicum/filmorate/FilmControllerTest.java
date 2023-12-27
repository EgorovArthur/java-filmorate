package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;


import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController;
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void addFilm() {
        Film film = new Film(1, "Аватар", "Фантастика",
                LocalDate.of(2012, 11, 11), 100, new HashSet<>());
        filmController.addFilm(film);
        Collection<Film> films = filmController.getFilms();
        assertNotNull(film, "Список фильмов пустой");
        assertEquals(1, films.size());
    }

    @Test
    void emptyNameFilm() {
        Film film = new Film(2, "", "Фантастика",
                LocalDate.of(2012, 11, 11), 100, new HashSet<>());
        Collection<Film> films = filmController.getFilms();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void incorrectDurationFilm() {
        Film film = new Film(3, "Аватар", "Фантастика",
                LocalDate.of(2012, 11, 11), -120, new HashSet<>());
        Collection<Film> films = filmController.getFilms();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
        assertEquals(0, films.size());
    }

}
