package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {

    private FilmStorage filmStorage;
    private UserService userService;
    private FilmLikesStorage filmLikesStorage;

    public Film addFilm(@Valid @RequestBody Film film) {
        FilmValidation.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            FilmValidation.validateFilm(film);
            return filmStorage.updateFilm(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден", e);
        }
    }

    public Collection<Film> getFilms() {
        try {
            return filmStorage.getFilms();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден", e);
        }
    }

    public Film filmById(@PathVariable("id") Integer id) {
        try {
            return filmStorage.filmById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден", e);
        }
    }

    public void addLike(Integer userId, Integer filmId) {
        try {
            filmLikesStorage.addLikeByFilmId(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLike(Integer userId, Integer filmId) {
        try {
            Film film = filmStorage.filmById(filmId);
            userService.userById(userId);
            filmLikesStorage.deleteLikeByFilmId(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> findTheMostPopulars(Integer count) {
        return filmLikesStorage.topFilms(count);
    }
}
