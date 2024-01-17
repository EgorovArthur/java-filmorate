package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @Email
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}
