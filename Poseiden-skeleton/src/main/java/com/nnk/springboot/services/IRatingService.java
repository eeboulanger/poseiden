package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;

import java.util.List;
import java.util.Optional;

/**
 * Any class that handles operations on Rating entity
 */

public interface IRatingService {

    List<Rating> getAllRatings();

    Rating createRating(Rating rating);

    Optional<Rating> getRatingById(int id);

    Rating updateRating(int id, Rating rating);
    void deleteRating(int id);
}
