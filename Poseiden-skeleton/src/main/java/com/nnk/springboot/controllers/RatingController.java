package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.IRatingService;
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
public class RatingController {
    private final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private IRatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("ratings", ratingService.getAllRatings());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/rating/add";
        } else {
            try {
                Rating savedRating = ratingService.createRating(rating);
                logger.info("Saving new curve point: " + savedRating.getId());
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to save new rating: " + exception.getMessage());
            }
        }
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Rating> optional = ratingService.getRatingById(id);
        if (optional.isEmpty()) {
            logger.error("No rating found with id: " + id);
            return "redirect:/rating/list";
        } else {
            model.addAttribute("rating", optional.get());
            return "rating/update";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/rating/update";
        } else {
            try {
                ratingService.updateRating(id, rating);
                logger.info("Updated rating: {id}");
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update rating id= {id} : " + exception.getMessage());
            }
        }
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.deleteRating(id);
            logger.info("Deleted rating with id: {id}");
        } catch (EntityNotFoundException exception) {
            logger.error("Failed to delete rating with id={id}: " + exception.getMessage());
        }
        return "redirect:/rating/list";
    }
}
