package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {
    private static final LocalDate date = LocalDate.of(1895, 12, 28);

    public static void validateFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.info("Ошибка валидации");
            throw new ValidationException("Наименование фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Ошибка валидации");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            log.info("Ошибка валидации");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (film.getReleaseDate().isBefore(date)) {
            log.info("Ошибка валидации");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года;");
        }

    }
}
