package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidService implements ICrudService<BidList> {

    @Autowired
    private BidListRepository repository;

    @Override
    public List<BidList> getAll() {
        return repository.findAll();
    }

    @Override
    public BidList save(BidList bid) {
        return repository.save(bid);
    }

    @Override
    public Optional<BidList> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public BidList update(int id, BidList bidList) {
        return repository.findById(id).map(bid -> {
                    bid.setAccount(bidList.getAccount());
                    bid.setType(bidList.getType());
                    bid.setBidQuantity(bidList.getBidQuantity());
                    return repository.save(bid);
                })
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
