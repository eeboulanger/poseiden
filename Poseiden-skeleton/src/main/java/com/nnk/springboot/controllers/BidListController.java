package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.IBidService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class BidListController {
    private final Logger logger = LoggerFactory.getLogger(BidListController.class);

    @Autowired
    private IBidService bidService;

    //replacing httpServletRequest.remoteUser since Thymeleaf 3.1 which no longer supports #httpServerRequest
    @ModelAttribute("remoteUser")
    public Object remoteUser(final HttpServletRequest request) {
        return request.getRemoteUser();
    }

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidService.getAllBids());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Bid has field errors: " + result.getAllErrors());
        } else {
            bidService.saveBid(bid);
            logger.info("Bid saved successfully: " + bid);
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Optional<BidList> optional = bidService.getBidById(id);
        if (optional.isEmpty()) {
            logger.error("No bid found with id: " + id);
            return "redirect:/bidList/list";
        } else {
            model.addAttribute("bidList", optional.get());
            return "bidList/update";
        }
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("Fields have errors: " + result.getAllErrors());
            return "/bidList/update";
        } else {
            bidService.updateBid(id, bidList);
            logger.info("Updated bid: " + bidList);
        }
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            bidService.deleteBid(id);
        } catch (RuntimeException e) {
            logger.error("Failed to delete bid with id: " + id + ". Bid not found in database");
        }
        return "redirect:/bidList/list";
    }
}
