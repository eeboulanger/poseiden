package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidServiceTest {

    @Mock
    private BidListRepository repository;
    @InjectMocks
    private BidService service;

    @Test
    public void getAllBidsTest() {
        List<BidList> list = List.of(
                new BidList("account1", "type1", 1),
                new BidList("account2", "type2", 1));
        when(repository.findAll()).thenReturn(list);

        List<BidList> result = service.getAllBids();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void getBidByIdTest() {
        BidList bid = new BidList();
        when(repository.findById(1)).thenReturn(Optional.of(bid));

        Optional<BidList> result = service.getBidById(1);

        assertTrue(result.isPresent());
    }

    @Test
    public void updateBidTest() {
        BidList bid = new BidList("Test account", "Test type", 100.0);
        BidList bidDto = new BidList("Updated account", "Updated type", 200.0);
        when(repository.findById(1)).thenReturn(Optional.of(bid));
        when(repository.save(any(BidList.class))).thenReturn(bid);

        BidList result = service.updateBid(1, bidDto);

        assertEquals(bidDto.getAccount(), result.getAccount());
        assertEquals(bidDto.getType(), result.getType());
        assertEquals(bidDto.getBidQuantity(), result.getBidQuantity());
    }

    @Test
    public void givenNoBidWithId_whenUpdateBid_thenThrowException() {
        BidList bidDto = new BidList("account", "type", 20);
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateBid(1, bidDto));

        verify(repository, times(1)).findById(1);
    }

    @Test
    public void deleteBidTest() {
        BidList bid = new BidList("Test account", "Test type", 100.0);
        when(repository.findById(1)).thenReturn(Optional.of(bid));

        service.deleteBid(1);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(bid);
    }

    @Test
    public void givenNoBidWithId_whenDeleteBid_thenThrowException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deleteBid(1));

        verify(repository, times(1)).findById(1);
    }
}
