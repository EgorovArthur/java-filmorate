package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final MpaService mpaService;


    @GetMapping
    public Collection<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa mpaById(@PathVariable("id") Integer mpaId) {
        return mpaService.mpaById(mpaId);
    }

}
