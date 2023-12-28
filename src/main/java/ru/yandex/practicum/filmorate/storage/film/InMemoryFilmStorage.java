package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film);
        return film;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film findById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(String.format("Фильм с id = %d не найден", id));
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new NotFoundException(String.format("Фильм с id = %d не найден", id));
        }
    }

}

