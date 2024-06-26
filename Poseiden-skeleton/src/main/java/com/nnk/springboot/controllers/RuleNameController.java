package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.IRuleNameService;
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
public class RuleNameController {
    private final Logger logger = LoggerFactory.getLogger(RuleNameController.class);
    @Autowired
    private IRuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("ruleNames", ruleNameService.getAllRuleNames());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Rule name has field errors: " + result.getAllErrors());
            return "ruleName/add";
        } else {
            try {
                RuleName rule = ruleNameService.createRuleName(ruleName);
                logger.info("Rule name saved successfully: " + rule.getId());
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to save new rule name: " + exception.getMessage());
            }
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<RuleName> optional = ruleNameService.getRuleNameById(id);
        if (optional.isEmpty()) {
            logger.error("No rule name found with id: " + id);
            return "redirect:/ruleName/list";
        } else {
            model.addAttribute("ruleName", optional.get());
            return "ruleName/update";
        }
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/ruleName/update";
        } else {
            try {
                ruleNameService.updateRuleName(id, ruleName);
                logger.info("Updated rule name: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update rule name id={id} :" + exception.getMessage());
            }
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.deleteRuleName(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete rule name with id: " + id + ". Rule name not found in database");
        }
        return "redirect:/ruleName/list";
    }
}
