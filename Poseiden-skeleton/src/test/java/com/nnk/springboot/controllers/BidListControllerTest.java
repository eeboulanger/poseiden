package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.IBidService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = BidListController.class)
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IBidService bidService;
    @InjectMocks
    private BidListController controller;

    @Test
    @DisplayName("Given there are bids, then return list of bids")
    @WithMockUser(username = "User", roles = "USER")
    public void getAllBidsTest() throws Exception {
        List<BidList> list = List.of(
                new BidList("account1", "type1", 1),
                new BidList("account2", "type2", 1));
        when(bidService.getAllBids()).thenReturn(list);

        mockMvc.perform(get("/bidList/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", hasSize(2)))
                .andExpect(model().attribute("bidLists", contains(
                        hasProperty("account", is("account1")),
                        hasProperty("account", is("account2"))
                )));

        verify(bidService, times(1)).getAllBids();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void addBidListTest() throws Exception {
        mockMvc.perform(get("/bidList/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @DisplayName("Given the bid is valid, when submitting form, then save bid and return bid")
    @WithMockUser(roles = "USER")
    public void validateBidTest() throws Exception {
        BidList bid = new BidList();
        when(bidService.saveBid(ArgumentMatchers.any(BidList.class))).thenReturn(bid);
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "test account")
                        .param("type", "test type")
                        .param("bidQuantity", "2")
                        .with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidService, times(1)).saveBid(ArgumentMatchers.any(BidList.class));
    }

    @Test
    @DisplayName("Given the bid has field errors, when submitting form, then don't save bid and return list of bids")
    @WithMockUser(roles = "USER")
    public void whenInvalidBid_thenDisplayFieldErrors() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "20")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().errorCount(4))//Account and Type Not blank, Not Empty
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type"));

        verify(bidService, never()).saveBid(ArgumentMatchers.any(BidList.class));
    }

    @Test
    @DisplayName("Given there's a bid with the id, then add bid to model")
    @WithMockUser(roles = "USER")
    public void updateBidTest() throws Exception {
        BidList bid = new BidList("Account", "Type", 100);
        when(bidService.getBidById(1)).thenReturn(Optional.of(bid));

        mockMvc.perform(get("/bidList/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attribute("bidList", hasProperty("account", is("Account"))))
                .andExpect(model().attribute("bidList", hasProperty("type", is("Type"))))
                .andExpect(model().attribute("bidList", hasProperty("bidQuantity", is(100.0))));

        verify(bidService, times(1)).getBidById(1);
    }

    @Test
    @DisplayName("Given there's no bid with the id, then don't add bid to model")
    @WithMockUser(roles = "USER")
    public void givenNoBidWithId_whenUpdateBid_thenDontAddToModel() throws Exception {
        when(bidService.getBidById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bidList/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));

        verify(bidService, times(1)).getBidById(1);
    }

    @Test
    @DisplayName("Given fields are valid, update bid in database")
    @WithMockUser(roles = "USER")
    public void updateBidSuccessTest() throws Exception {
        mockMvc.perform(post("/bidList/update/{id}", 1)
                        .param("account", "Test account")
                        .param("type", "Test type")
                        .param("bidQuantity", "200")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
        verify(bidService, times(1)).updateBid(eq(1), ArgumentMatchers.any(BidList.class));
    }

    @Test
    @DisplayName("Given fields have errors, don't update bid and display errors")
    @WithMockUser(roles = "USER")
    public void updateBidFailsTest() throws Exception {
        mockMvc.perform(post("/bidList/update/{id}", 1)
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "200")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(4)) //Not blank, not Empty
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type"))
                .andExpect(view().name("/bidList/update"));

        verify(bidService, never()).updateBid(eq(1), ArgumentMatchers.any(BidList.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteBidTest() throws Exception {
        mockMvc.perform(get("/bidList/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    //TODO handle exception
    @Test
    @DisplayName("Given there's no user with the id, then redirect to list")
    @WithMockUser(roles = "USER")
    public void deleteBidFailsTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(bidService).deleteBid(1);

        mockMvc.perform(get("/bidList/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidService, times(1)).deleteBid(1);
    }
}
