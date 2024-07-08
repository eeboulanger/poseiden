package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.services.ICrudService;
import com.nnk.springboot.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController implements ICrudController<UserDTO> {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;

    public String home(Model model, Principal principal) {
        model.addAttribute("users", userService.getAll());
        return "user/list";
    }


    public String addForm(UserDTO userDTO) {
        return "user/add";
    }

    public String validate(@Valid UserDTO user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("User has field errors: " + result.getAllErrors());
            return "user/add";
        } else {
            try {
                User savedUser = userService.saveWithDto(user);
                logger.info("User saved successfully: " + savedUser.getId());
            } catch (IllegalArgumentException exception) {
                logger.error("Failed to save new User: " + exception.getMessage());
                model.addAttribute("error", exception.getMessage());
                return "user/add";
            }
        }
        return "redirect:/user/list";
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> optional = userService.getById(id);
        if (optional.isEmpty()) {
            logger.error("No user found with id: " + id);
            return "redirect:/user/list";
        } else {
            User user = optional.get();
            UserDTO dto = new UserDTO(user.getUsername(), "", user.getFullname(), user.getRole()); //convert user to dto
            model.addAttribute("userDTO", dto);
            model.addAttribute("id", id);
            return "user/update";
        }
    }

    public String update(@PathVariable("id") Integer id, @Valid UserDTO user,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/user/update";
        } else {
            try {
                userService.updateWithDto(id, user);
                logger.info("Updated user: " + id);
            } catch (EntityNotFoundException | IllegalArgumentException exception) {
                logger.error("Failed to update user id={id} :" + exception.getMessage());
                model.addAttribute("error", exception.getMessage());
                return "/user/update";
            }
        }
        return "redirect:/user/list";
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete user with id: " + id + ". User not found in database");
        }
        return "redirect:/user/list";
    }
}
