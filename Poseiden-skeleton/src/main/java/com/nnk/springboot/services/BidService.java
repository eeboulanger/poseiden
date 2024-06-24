package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidService implements IBidService {

    @Autowired
    private BidListRepository repository;

    @Override
    public List<BidList> getAllBids() {
        return repository.findAll();
    }

    @Override
    public BidList saveBid(BidList bid) {
        return repository.save(bid);
    }

    @Override
    public Optional<BidList> getBidById(int id) {
        return repository.findById(id);
    }

    @Override
    public BidList updateBid(int id, BidList bidList) {
        return repository.findById(id).map(bid -> {
                    bid.setAccount(bidList.getAccount());
                    bid.setType(bidList.getType());
                    bid.setBidQuantity(bidList.getBidQuantity());
                    return repository.save(bid);
                })
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void deleteBid(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
