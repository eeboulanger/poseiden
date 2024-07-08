package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
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
@RequestMapping("/bidList")
public class BidListController implements ICrudController<BidList> {
    private final Logger logger = LoggerFactory.getLogger(BidListController.class);

    @Autowired
    private ICrudService<BidList> bidService;

    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        model.addAttribute("bidLists", bidService.getAll());
        return "bidList/list";
    }

    public String addForm(BidList bid) {
        return "bidList/add";
    }

    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Bid has field errors: " + result.getAllErrors());
            return "bidList/add";
        } else {
            BidList savedBid = bidService.save(bid);
            logger.info("Bid saved successfully: " + savedBid.getId());
            return "redirect:/bidList/list";
        }
    }

    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> optional = bidService.getById(id);
        if (optional.isEmpty()) {
            logger.error("No bid found with id: " + id);
            return "redirect:/bidList/list";
        } else {
            model.addAttribute("bidList", optional.get());
            return "bidList/update";
        }
    }

    public String update(@PathVariable("id") Integer id, @Valid BidList bidList,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "bidList/update";
        } else {
            try {
                bidService.update(id, bidList);
                logger.info("Updated bid: " + id);
            } catch (EntityNotFoundException exception) {
                logger.error("Failed to update bid id={id} :" + exception.getMessage());
            }
        }
        return "redirect:/bidList/list";
    }

    public String delete(@PathVariable("id") Integer id, Model model) {
        try {
            bidService.delete(id);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to delete bid with id: " + id + ". Bid not found in database");
        }
        return "redirect:/bidList/list";
    }
}
