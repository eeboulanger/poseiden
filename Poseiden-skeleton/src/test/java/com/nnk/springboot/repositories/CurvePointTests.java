package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurvePointTests {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest() {
        CurvePoint curvePoint = new CurvePoint((byte) 10, 10d, 30d);

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        Assert.assertNotNull(curvePoint.getId());
        Assert.assertEquals(10, (byte) curvePoint.getCurveId());

        // Update
        curvePoint.setCurveId((byte) 20);
        curvePoint = curvePointRepository.save(curvePoint);
        Assert.assertEquals(20, (byte) curvePoint.getCurveId());

        // Find
        List<CurvePoint> listResult = curvePointRepository.findAll();
        Assert.assertTrue(listResult.size() > 0);

        //Find by Id
        Optional<CurvePoint> optional = curvePointRepository.findById(curvePoint.getId());
        assertTrue(optional.isPresent());

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        Assert.assertFalse(curvePointList.isPresent());
    }

}
