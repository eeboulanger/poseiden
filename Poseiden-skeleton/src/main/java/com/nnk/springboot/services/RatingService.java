package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService implements IRatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Optional<Rating> getRatingById(int id) {
        return ratingRepository.findById(id);
    }

    @Override
    public Rating updateRating(int id, Rating rating) {
        Rating opt = ratingRepository.findById(id).map(
                currentRating -> {
                    currentRating.setMoodysRating(rating.getMoodysRating());
                    currentRating.setFitchRating(rating.getFitchRating());
                    currentRating.setSandPRating(rating.getSandPRating());
                    currentRating.setOrderNumber(rating.getOrderNumber());
                    return ratingRepository.save(currentRating);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return opt;
    }

    @Override
    public void deleteRating(int id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
