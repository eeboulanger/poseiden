package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;
    @InjectMocks
    private RatingService ratingService;
    private Rating rating;

    @BeforeEach
    public void setUp() {
        rating = new Rating("moodys", "sandP", "fitch", 10);
        rating.setId(1);
    }

    @Test
    public void getAllRatingsTest() {
        List<Rating> list = List.of(rating);
        when(ratingRepository.findAll()).thenReturn(list);

        List<Rating> result = ratingService.getAll();

        assertEquals(rating.getId(), result.get(0).getId());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    public void createRatingTest() {
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating result = ratingService.save(rating);

        assertEquals(rating.getId(), result.getId());
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    public void getRatingByIdTest() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(rating.getId(), result.get().getId());
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    public void updateRatingSuccessTest() {
        Rating dto = new Rating("Updated moodys", "Updated sandP", "Updatedfitch", 11);
        when(ratingRepository.findById(1)).thenReturn(Optional.ofNullable(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.update(1, dto);

        assertNotNull(result);
        assertEquals(dto.getMoodysRating(), result.getMoodysRating());
        assertEquals(dto.getFitchRating(), result.getFitchRating());
        assertEquals(dto.getSandPRating(), result.getSandPRating());
        assertEquals(dto.getOrderNumber(), result.getOrderNumber());
        verify(ratingRepository, times(1)).findById(1);
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    @DisplayName("Given there is no rating with id, then don't update and throw exception")
    public void updateRatingFailsTest() {
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.update(1, rating));
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    public void deleteRatingSuccessTest() {
        when(ratingRepository.existsById(1)).thenReturn(true);

        ratingService.delete(1);

        verify(ratingRepository, times(1)).existsById(1);
        verify(ratingRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Given no rating with id exists, then don't delete and throw exception")
    public void deleteRatingFailsTest() {
        when(ratingRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> ratingService.delete(1));

        verify(ratingRepository, never()).deleteById(1);
    }
}
