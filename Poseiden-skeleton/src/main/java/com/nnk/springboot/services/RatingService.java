package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService implements ICrudService<Rating> {
    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Optional<Rating> getById(int id) {
        return ratingRepository.findById(id);
    }

    @Override
    public Rating update(int id, Rating rating) {
        return ratingRepository.findById(id).map(
                currentRating -> {
                    currentRating.setMoodysRating(rating.getMoodysRating());
                    currentRating.setFitchRating(rating.getFitchRating());
                    currentRating.setSandPRating(rating.getSandPRating());
                    currentRating.setOrderNumber(rating.getOrderNumber());
                    return ratingRepository.save(currentRating);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void delete(int id) {
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
