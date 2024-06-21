package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.ICurvePointService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class CurveController {

    private final Logger logger = LoggerFactory.getLogger(CurveController.class);
    @Autowired
    private ICurvePointService curveService;

    //replacing httpServletRequest.remoteUser since Thymeleaf 3.1 which no longer supports #httpServerRequest
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest request) {
        return request.getRemoteUser();
    }

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> curvePoints = curveService.getAllCurvePoints();
        model.addAttribute("curvePoints", curvePoints);
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
        } else {
            curveService.saveCurvePoint(curvePoint);
            logger.info("Saving new curve point: " + curvePoint.getCurveId() + curvePoint.getValue());
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            CurvePoint curvePoint = curveService.getCurvePointById(id);
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        } catch (RuntimeException exception) {
            logger.error(exception.getMessage());
            return "curvePoint/list";
        }
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have error: " + result.getAllErrors());
            return "/curvePoint/update";
        } else {
            curveService.updateCurvePoint(id, curvePoint);
            logger.info("Updated Curve point with id: " + id);
            return "redirect:/curvePoint/list";
        }
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        try {
            curveService.deleteCurvePoint(id);
            logger.info("Deleted Curve point id: " + id);
        } catch (RuntimeException exception) {
            logger.error("Failed to delete curve point id: " + id + " " + exception.getMessage());
        }
        return "redirect:/curvePoint/list";
    }
}
