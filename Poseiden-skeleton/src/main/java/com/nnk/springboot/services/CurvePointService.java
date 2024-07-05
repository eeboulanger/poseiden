package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService implements ICrudService<CurvePoint> {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Override
    public List<CurvePoint> getAll() {
        return curvePointRepository.findAll();
    }

    @Override
    public CurvePoint save(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public Optional<CurvePoint> getById(int id) {
        return curvePointRepository.findById(id);
    }

    @Override
    public CurvePoint update(int id, CurvePoint curvePoint) {
        return curvePointRepository.findById(id).map(curve -> {
                            curve.setCurveId(curvePoint.getCurveId());
                            curve.setTerm(curvePoint.getTerm());
                            curve.setValue(curvePoint.getValue());
                            return curvePointRepository.save(curve);
                        }
                )
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void delete(int id) {
        if (curvePointRepository.existsById(id)) {
            curvePointRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
