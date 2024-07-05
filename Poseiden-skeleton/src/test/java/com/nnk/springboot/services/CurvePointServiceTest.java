package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
public class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;
    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;

    @BeforeEach
    public void setUp() {
        curvePoint = new CurvePoint((byte) 45, 14d, 10d);
    }

    @Test
    public void findAllTest() {
        when(curvePointRepository.findAll()).thenReturn(List.of(curvePoint));

        List<CurvePoint> result = curvePointService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(curvePointRepository, times(1)).findAll();
    }

    @Test
    public void saveCurvePointTest() {
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        CurvePoint result = curvePointService.save(curvePoint);

        assertNotNull(result);
        verify(curvePointRepository, times(1)).save(curvePoint);
    }

    @Test
    public void getCurvePointByIdTest() throws Exception {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        CurvePoint result = curvePointService.getById(1).orElseThrow(Exception::new);

        assertNotNull(result);
        verify(curvePointRepository, times(1)).findById(1);
    }


    @Test
    public void updateCurvePointTest() {
        CurvePoint dto = new CurvePoint((byte) 11, 12d, 13d);
        when(curvePointRepository.findById(1)).thenReturn(Optional.ofNullable(curvePoint));
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        CurvePoint result = curvePointService.update(1, dto);

        verify(curvePointRepository, times(1)).findById(1);
        verify(curvePointRepository, times(1)).save(curvePoint);
        assertNotNull(result);
        assertEquals(dto.getCurveId(), result.getCurveId());
        assertEquals(dto.getTerm(), result.getTerm());
        assertEquals(dto.getValue(), result.getValue());
    }

    @Test
    public void givenNoCurvePointWithId_whenUpdateCurvePoint_thenThrowException() {
        CurvePoint dto = new CurvePoint((byte) 11, 12d, 13d);
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> curvePointService.update(1, dto));

        verify(curvePointRepository, times(1)).findById(1);
        verify(curvePointRepository, never()).save(curvePoint);
    }

    @Test
    public void deleteCurvePointTest() {
        when(curvePointRepository.existsById(1)).thenReturn(true);

        curvePointService.delete(1);

        verify(curvePointRepository, times(1)).existsById(1);
        verify(curvePointRepository, times(1)).deleteById(1);
    }

    @Test
    public void givenNoCurvePointWithId_whenDelete_thenThrowException() {
        when(curvePointRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> curvePointService.delete(1));

        verify(curvePointRepository, times(1)).existsById(1);
        verify(curvePointRepository, never()).deleteById(1);
    }
}
