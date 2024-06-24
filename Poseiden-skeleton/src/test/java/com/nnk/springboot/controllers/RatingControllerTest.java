package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.IRatingService;
import org.junit.jupiter.api.BeforeAll;
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

@WebMvcTest(controllers = RatingController.class)
@WithMockUser(roles = "USER")
public class RatingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IRatingService ratingService;
    @InjectMocks
    private RatingController ratingController;
    private static Rating rating;

    @BeforeAll
    public static void setUp() {
        rating = new Rating("Moodys", "Sandp", "Fitch", 10);
        rating.setId(1);
    }

    @Test
    public void homeTest() throws Exception {
        List<Rating> list = List.of(rating);

        when(ratingService.getAllRatings()).thenReturn(list);
        mockMvc.perform(get("/rating/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", hasSize(1)))
                .andExpect(model().attribute("ratings", contains(
                        hasProperty("moodysRating", is("Moodys"))
                )));

        verify(ratingService, times(1)).getAllRatings();
    }

    @Test
    public void addRatingFormTest() throws Exception {
        mockMvc.perform(get("/rating/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rating/add"));
    }

    @Test
    public void validateRatingSuccessTest() throws Exception {
        when(ratingService.createRating(ArgumentMatchers.any(Rating.class))).thenReturn(new Rating());

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Moodys")
                        .param("sandPRating", "sandP")
                        .param("fitchRating", "fitch")
                        .param("orderNumber", "10")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).createRating(ArgumentMatchers.any(Rating.class));
    }

    //TODO test validate fails, decide on validation rules for Rating dto

    @Test
    public void showUpdateFormTest() throws Exception {
        when(ratingService.getRatingById(1)).thenReturn(Optional.ofNullable(rating));

        mockMvc.perform(get("/rating/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", hasProperty("moodysRating", is(rating.getMoodysRating()))))
                .andExpect(model().attribute("rating", hasProperty("sandPRating", is(rating.getSandPRating()))))
                .andExpect(model().attribute("rating", hasProperty("fitchRating", is(rating.getFitchRating()))))
                .andExpect(model().attribute("rating", hasProperty("orderNumber", is(rating.getOrderNumber()))));

        verify(ratingService, times(1)).getRatingById(1);
    }

    @Test
    @DisplayName("Given there's no rating with the id, then redirect to list")
    public void givenNoRatingWithId_whenUpdate_thenDontAddToModel() throws Exception {
        when(ratingService.getRatingById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rating/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        verify(ratingService, times(1)).getRatingById(1);
    }

    @Test
    public void updateRatingTest() throws Exception {
        when(ratingService.updateRating(eq(1), ArgumentMatchers.any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/rating/update/{id}", 1)
                        .param("moodysRating", rating.getMoodysRating())
                        .param("sandPRating", rating.getSandPRating())
                        .param("fitchRating", rating.getFitchRating())
                        .param("orderNumber", rating.getOrderNumber().toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        verify(ratingService, times(1)).updateRating(eq(1), ArgumentMatchers.any(Rating.class));
    }

    @Test
    public void deleteRatingTest() throws Exception {
        mockMvc.perform(get("/rating/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService, times(1)).deleteRating(1);
    }
}
