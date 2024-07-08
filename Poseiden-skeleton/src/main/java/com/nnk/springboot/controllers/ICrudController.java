package com.nnk.springboot.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public interface ICrudController<T> {

    @RequestMapping("/list")
    String home(Model model, Principal principal);

    @GetMapping("/add")
    String addForm(T entity);

    @PostMapping("/validate")
    String validate(@Valid T entity, BindingResult result, Model model);

    @GetMapping("/update/{id}")
    String showUpdateForm(@PathVariable("id") Integer id, Model model);

    @PostMapping("/update/{id}")
    String update(@PathVariable("id") Integer id, @Valid T entity,
                     BindingResult result, Model model);

    @GetMapping("/delete/{id}")
    String delete(@PathVariable("id") Integer id, Model model);
}
