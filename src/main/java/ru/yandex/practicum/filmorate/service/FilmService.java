package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public Film addFilm(@Valid @RequestBody Film film) {
        FilmValidation.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@Valid @RequestBody Film film) {
        FilmValidation.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film findById(@PathVariable("id") Integer id) {
        try {
            return filmStorage.findById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден", e);
        }
    }

    public void addLike(Integer userId, Integer filmId) {
        try {
            Film film = filmStorage.findById(filmId);
            userStorage.findById(userId);
            film.setLikes(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLike(Integer userId, Integer filmId) {
        try {
            Film film = filmStorage.findById(filmId);
            userStorage.findById(userId);
            film.deleteLike(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> findTheMostPopulars(Integer count) {
        return filmStorage.getFilms()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }


}
