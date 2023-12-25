package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Ошибка валидации");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.info("Ошибка валидации");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Ошибка валидации");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
