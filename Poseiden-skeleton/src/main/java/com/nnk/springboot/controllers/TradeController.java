package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
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
@RequestMapping("/trade")
public class TradeController implements ICrudController<Trade> {
    private final Logger logger = LoggerFactory.getLogger(TradeController.class);
    @Autowired
    private ICrudService<Trade> service;

    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("trades", service.getAll());
        return "trade/list";
    }

    public String addForm(Trade trade) {
        return "trade/add";
    }

    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Trade has field errors: " + result.getAllErrors());
            return "trade/add";
        } else {
            Trade savedTrade = service.save(trade);
            logger.info("Trade saved successfully: " + savedTrade.getTradeId());
            return "redirect:/trade/list";
        }
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<Trade> optional = service.getById(id);
        if (optional.isEmpty()) {
            logger.error("No trade found with id: " + id);
            return "redirect:/trade/list";
        } else {
            model.addAttribute("trade", optional.get());
            return "trade/update";
        }
    }

    public String update(@PathVariable("id") Integer id, @Valid Trade trade,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/trade/update";
        } else {
            try {
                service.update(id, trade);
                logger.info("Updated trade: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update trade id={id} :" + exception.getMessage());
            }
        }
        return "redirect:/trade/list";
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete trade with id: " + id + ". Trade not found in database");
        }
        return "redirect:/trade/list";
    }
}
