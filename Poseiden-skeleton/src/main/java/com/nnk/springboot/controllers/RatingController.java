package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.ICrudService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("/rating")
public class RatingController implements ICrudController<Rating> {
    private final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private ICrudService<Rating> ratingService;

    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("ratings", ratingService.getAll());
        return "rating/list";
    }

    public String addForm(Rating rating) {
        return "rating/add";
    }

    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/rating/add";
        } else {
            Rating savedRating = ratingService.save(rating);
            logger.info("Saving new curve point: " + savedRating.getId());
            return "redirect:/rating/list";
        }
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> optional = ratingService.getById(id);
        if (optional.isEmpty()) {
            logger.error("No rating found with id: " + id);
            return "redirect:/rating/list";
        } else {
            model.addAttribute("rating", optional.get());
            return "rating/update";
        }
    }

    public String update(@PathVariable("id") Integer id, @Valid Rating rating,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/rating/update";
        } else {
            try {
                ratingService.update(id, rating);
                logger.info("Updated rating: {id}");
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update rating id= {id} : " + exception.getMessage());
            }
        }
        return "redirect:/rating/list";
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.delete(id);
            logger.info("Deleted rating with id: {id}");
        } catch (EntityNotFoundException exception) {
            logger.error("Failed to delete rating with id={id}: " + exception.getMessage());
        }
        return "redirect:/rating/list";
    }
}
