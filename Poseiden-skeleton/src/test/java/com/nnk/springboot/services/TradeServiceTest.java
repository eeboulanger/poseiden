package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
    @Mock
    private TradeRepository repository;
    @InjectMocks
    private TradeService service;

    @Test
    public void getAllTradesTest() {
        List<Trade> list = List.of(
                new Trade("account1", "type1", 1d),
                new Trade("account2", "type2", 1d));
        when(repository.findAll()).thenReturn(list);

        List<Trade> result = service.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void getTradeByIdTest() {
        Trade trade = new Trade();
        when(repository.findById(1)).thenReturn(Optional.of(trade));

        Optional<Trade> result = service.getById(1);

        assertTrue(result.isPresent());
    }

    @Test
    public void updateTradeTest() {
        Trade trade = new Trade("Test account", "Test type", 100.0);
        Trade dto = new Trade("Updated account", "Updated type", 200.0);
        when(repository.findById(1)).thenReturn(Optional.of(trade));
        when(repository.save(any(Trade.class))).thenReturn(trade);

        Trade result = service.update(1, dto);

        assertEquals(dto.getAccount(), result.getAccount());
        assertEquals(dto.getType(), result.getType());
        assertEquals(dto.getBuyQuantity(), result.getBuyQuantity());
    }

    @Test
    public void givenNoTradeWithId_whenUpdate_thenThrowException() {
        Trade dto = new Trade("account", "type", 20d);
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1, dto));

        verify(repository, times(1)).findById(1);
    }

    @Test
    public void deleteTradeTest() {
        Trade dto = new Trade("Test account", "Test type", 100.0);
        when(repository.existsById(1)).thenReturn(true);

        service.delete(1);

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    public void givenNoTradeWithId_whenDelete_thenThrowException() {
        when(repository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(1));

        verify(repository, times(1)).existsById(1);
        verify(repository, never()).deleteById(1);
    }
}
