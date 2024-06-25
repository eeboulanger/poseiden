package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;

    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/list";
    }


    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("User has field errors: " + result.getAllErrors());
            return "user/add";
        } else {
            try {
                User savedUser = userService.createUser(user);
                logger.info("User saved successfully: " + savedUser.getId());
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to save new User: " + exception.getMessage());
            }
        }
        return "redirect:/user/list";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> optional = userService.getUserById(id);
        if (optional.isEmpty()) {
            logger.error("No user found with id: " + id);
            return "redirect:/user/list";
        } else {
            User user = optional.get();
            user.setPassword("");
            model.addAttribute("user", optional.get());
            return "user/update";
        }
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/user/update";
        } else {
            try {
                userService.updateUser(id, user);
                logger.info("Updated user: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update user id={id} :" + exception.getMessage());
            }
        }
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        try {
            userService.deleteUser(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete user with id: " + id + ". User not found in database");
        }
        return "redirect:/user/list";
    }
}
