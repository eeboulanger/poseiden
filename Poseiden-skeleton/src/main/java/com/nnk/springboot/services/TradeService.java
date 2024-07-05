package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService implements ICrudService<Trade> {
    @Autowired
    private TradeRepository repository;

    @Override
    public List<Trade> getAll() {
        return repository.findAll();
    }

    @Override
    public Trade save(Trade trade) {
        return repository.save(trade);
    }

    @Override
    public Optional<Trade> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public Trade update(int id, Trade newTrade) {
        return repository.findById(id).map(trade -> {
                    trade.setAccount(newTrade.getAccount());
                    trade.setType(newTrade.getType());
                    trade.setBuyQuantity(newTrade.getBuyQuantity());
                    return repository.save(trade);
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
