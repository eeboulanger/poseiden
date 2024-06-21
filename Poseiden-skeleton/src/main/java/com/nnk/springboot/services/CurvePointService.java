package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService implements ICurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Override
    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }

    @Override
    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public CurvePoint getCurvePointById(int id) {
        return curvePointRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public CurvePoint updateCurvePoint(int id, CurvePoint curvePoint) {
        CurvePoint curve = curvePointRepository.findById(id).orElseThrow(RuntimeException::new);
        curve.setCurveId(curvePoint.getCurveId());
        curve.setTerm(curvePoint.getTerm());
        curve.setValue(curvePoint.getValue());
        return curvePointRepository.save(curve);
    }

    @Override
    public void deleteCurvePoint(int id) {
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(RuntimeException::new);
        curvePointRepository.delete(curvePoint);
    }
}
