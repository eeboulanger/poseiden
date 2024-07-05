package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.ICrudService;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TradeController.class)
@WithMockUser(roles = "USER")
public class TradeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ICrudService<Trade> service;
    @InjectMocks
    private TradeController controller;

    @Test
    @DisplayName("Given there are trades, then return list ")
    public void homeTest() throws Exception {
        List<Trade> list = List.of(
                new Trade("account1", "type1", 1d),
                new Trade("account2", "type2", 1d));
        when(service.getAll()).thenReturn(list);

        mockMvc.perform(get("/trade/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", hasSize(2)))
                .andExpect(model().attribute("trades", contains(
                        hasProperty("account", is("account1")),
                        hasProperty("account", is("account2"))
                )));

        verify(service, times(1)).getAll();
    }

    @Test
    public void addTradeTest() throws Exception {
        mockMvc.perform(get("/trade/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @DisplayName("Given the trade is valid, when submitting form, then save trade and redirect")
    public void validateTradeTest() throws Exception {
        Trade trade = new Trade();
        when(service.save(ArgumentMatchers.any(Trade.class))).thenReturn(trade);
        mockMvc.perform(post("/trade/validate")
                        .param("account", "test account")
                        .param("type", "test type")
                        .param("buyQuantity", "2")
                        .with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(service, times(1)).save(ArgumentMatchers.any(Trade.class));
    }

    @Test
    @DisplayName("Given the trade has field errors, when submitting form, then don't save trade and return list of trades")
    public void whenInvalidTrade_thenDisplayFieldErrors() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "20")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/add"))
                .andExpect(model().errorCount(4))//Account and Type Not blank, Not Empty
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));

        verify(service, never()).save(ArgumentMatchers.any(Trade.class));
    }

    @Test
    @DisplayName("Given there's a trade with the id, then add to model")
    public void updateTradeTest() throws Exception {
        Trade trade = new Trade("Account", "Type", 100d);
        when(service.getById(1)).thenReturn(Optional.of(trade));

        mockMvc.perform(get("/trade/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attribute("trade", hasProperty("account", is("Account"))))
                .andExpect(model().attribute("trade", hasProperty("type", is("Type"))))
                .andExpect(model().attribute("trade", hasProperty("buyQuantity", is(100.0))));

        verify(service, times(1)).getById(1);
    }

    @Test
    @DisplayName("Given there's no trade with the id, then don't add trade to model")
    public void givenNoTradeWithId_whenUpdate_thenDontAddToModel() throws Exception {
        when(service.getById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/trade/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));

        verify(service, times(1)).getById(1);
    }

    @Test
    @DisplayName("Given fields are valid, update trade in database")
    public void updateTradeSuccessTest() throws Exception {
        mockMvc.perform(post("/trade/update/{id}", 1)
                        .param("account", "Test account")
                        .param("type", "Test type")
                        .param("buyQuantity", "200")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
        verify(service, times(1)).update(eq(1), ArgumentMatchers.any(Trade.class));
    }

    @Test
    @DisplayName("Given fields have errors, don't update trade and display errors")
    public void updateTradeFailsTest() throws Exception {
        mockMvc.perform(post("/trade/update/{id}", 1)
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "200")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(4)) //Not blank, not Empty
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"))
                .andExpect(view().name("/trade/update"));

        verify(service, never()).update(eq(1), ArgumentMatchers.any(Trade.class));
    }

    @Test
    public void deleteTradeTest() throws Exception {
        mockMvc.perform(get("/trade/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    //TODO handle exception
    @Test
    @DisplayName("Given there's no trade with the id, then redirect to list")
    public void deleteTradeFailsTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(service).delete(1);

        mockMvc.perform(get("/trade/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(service, times(1)).delete(1);
    }
}
