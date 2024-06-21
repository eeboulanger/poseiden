package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

/**
 * Handles operations on CurvePoint entity in database
 */

public interface ICurvePointService {

    List<CurvePoint> getAllCurvePoints();

    CurvePoint saveCurvePoint(CurvePoint curvePoint);

    CurvePoint getCurvePointById(int id);

    CurvePoint updateCurvePoint(int id, CurvePoint curvePoint);
    void deleteCurvePoint(int id);
}
