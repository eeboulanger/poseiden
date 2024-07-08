package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
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
@RequestMapping("/curvePoint")
public class CurveController implements ICrudController<CurvePoint> {

    private final Logger logger = LoggerFactory.getLogger(CurveController.class);
    @Autowired
    private ICrudService<CurvePoint> curveService;

    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("curvePoints", curveService.getAll());
        return "curvePoint/list";
    }

    public String addForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }


    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields has errors: " + result.getAllErrors());
            return "curvePoint/add";
        } else {
            CurvePoint savedCurvePoint = curveService.save(curvePoint);
            logger.info("Saving new curve point: " + savedCurvePoint.getCurveId());
            return "redirect:/curvePoint/list";
        }
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<CurvePoint> optional = curveService.getById(id);
        if (optional.isEmpty()) {
            logger.error("No curve point found with id: " + id);
            return "redirect:/curvePoint/list";
        } else {
            model.addAttribute("curvePoint", optional.get());
            return "curvePoint/update";
        }
    }


    public String update(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have error: " + result.getAllErrors());
            return "/curvePoint/update";
        } else {
            try {
                curveService.update(id, curvePoint);
                logger.info("Updated curve point: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update curve point id={id} :" + exception.getMessage());
            }
            return "redirect:/curvePoint/list";
        }
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            curveService.delete(id);
            logger.info("Deleted Curve point id: " + id);
        } catch (EntityNotFoundException exception) {
            logger.error("Failed to delete curve point id: " + id + " " + exception.getMessage());
        }
        return "redirect:/curvePoint/list";
    }
}
