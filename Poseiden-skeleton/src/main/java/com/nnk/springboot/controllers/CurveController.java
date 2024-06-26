package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.ICurvePointService;
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
public class CurveController {

    private final Logger logger = LoggerFactory.getLogger(CurveController.class);
    @Autowired
    private ICurvePointService curveService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("curvePoints", curveService.getAllCurvePoints());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields has errors: " + result.getAllErrors());
            return "curvePoint/add";
        } else {
            try {
                CurvePoint savedCurvePoint = curveService.saveCurvePoint(curvePoint);
                logger.info("Saving new curve point: " + savedCurvePoint.getCurveId());
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to save new curve point: " + exception.getMessage());
            }
            return "redirect:/curvePoint/list";
        }
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<CurvePoint> optional = curveService.getCurvePointById(id);
        if (optional.isEmpty()) {
            logger.error("No curve point found with id: " + id);
            return "redirect:/curvePoint/list";
        } else {
            model.addAttribute("curvePoint", optional.get());
            return "curvePoint/update";
        }
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have error: " + result.getAllErrors());
            return "/curvePoint/update";
        } else {
            try {
                curveService.updateCurvePoint(id, curvePoint);
                logger.info("Updated curve point: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update curve point id={id} :" + exception.getMessage());
            }
            return "redirect:/curvePoint/list";
        }
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        try {
            curveService.deleteCurvePoint(id);
            logger.info("Deleted Curve point id: " + id);
        } catch (EntityNotFoundException exception) {
            logger.error("Failed to delete curve point id: " + id + " " + exception.getMessage());
        }
        return "redirect:/curvePoint/list";
    }
}
