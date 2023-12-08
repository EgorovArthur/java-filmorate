package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Collection<Film> getFilms() {  //возвращает все фильмы
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) { //добавление фильма
        log.info("Фильм {} добавлен", film);
        FilmValidation.validateFilm(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление фильма
        log.info("Фильм {} обновлен", film);
        FilmValidation.validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Неверный id");
        }
        films.put(film.getId(), film);
        return film;
    }


}
