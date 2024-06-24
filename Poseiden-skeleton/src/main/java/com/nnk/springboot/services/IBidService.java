package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;
import java.util.Optional;

/**
 * Handles operations on BidList entity in database
 */

public interface IBidService {

    List<BidList> getAllBids();

    BidList saveBid(BidList bid);

    Optional<BidList> getBidById(int id);

    BidList updateBid(int id, BidList bidList);

    void deleteBid(int id);
}
