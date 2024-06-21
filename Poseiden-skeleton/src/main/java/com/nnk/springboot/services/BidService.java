package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
    public Optional<BidList> getBidById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public BidList updateBid(Integer id, BidList bidList) {
        BidList bid = repository.findById(id).orElseThrow(RuntimeException::new);
        bid.setAccount(bidList.getAccount());
        bid.setType(bidList.getType());
        bid.setBidQuantity(bidList.getBidQuantity());
        return repository.save(bid);
    }

    @Override
    public void deleteBid(int id) {
        BidList bid = repository.findById(id).orElseThrow(RuntimeException::new);
        repository.delete(bid);
    }
}
