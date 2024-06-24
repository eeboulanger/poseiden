package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;

import java.util.List;
import java.util.Optional;

/**
 * Handles operations on Trade entity in database
 */

public interface ITradeService {
    List<Trade> getAllTrades();

    Trade saveTrade(Trade trade);

    Optional<Trade> getTradeById(int id);

    Trade updateTrade(int id, Trade trade);

    void deleteTrade(int id);
}
