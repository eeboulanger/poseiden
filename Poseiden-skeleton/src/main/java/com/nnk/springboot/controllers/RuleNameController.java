package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
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
@RequestMapping("/ruleName")
public class RuleNameController implements ICrudController<RuleName> {
    private final Logger logger = LoggerFactory.getLogger(RuleNameController.class);
    @Autowired
    private ICrudService<RuleName> ruleNameService;

    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("ruleNames", ruleNameService.getAll());
        return "ruleName/list";
    }

    public String addForm(RuleName bid) {
        return "ruleName/add";
    }

    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Rule name has field errors: " + result.getAllErrors());
            return "ruleName/add";
        } else {
            RuleName rule = ruleNameService.save(ruleName);
            logger.info("Rule name saved successfully: " + rule.getId());
            return "redirect:/ruleName/list";
        }
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> optional = ruleNameService.getById(id);
        if (optional.isEmpty()) {
            logger.error("No rule name found with id: " + id);
            return "redirect:/ruleName/list";
        } else {
            model.addAttribute("ruleName", optional.get());
            return "ruleName/update";
        }
    }

    public String update(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/ruleName/update";
        } else {
            try {
                ruleNameService.update(id, ruleName);
                logger.info("Updated rule name: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update rule name id={id} :" + exception.getMessage());
            }
        }
        return "redirect:/ruleName/list";
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.delete(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete rule name with id: " + id + ". Rule name not found in database");
        }
        return "redirect:/ruleName/list";
    }
}
