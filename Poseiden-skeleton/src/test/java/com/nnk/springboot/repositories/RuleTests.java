package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.RuleName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTests {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    public void ruleTest() {
        RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

        // Save
        rule = ruleNameRepository.save(rule);
        Assert.assertNotNull(rule.getId());
        assertTrue(rule.getName().equals("Rule Name"));

        // Update
        rule.setName("Rule Name Update");
        rule = ruleNameRepository.save(rule);
        assertTrue(rule.getName().equals("Rule Name Update"));

        // Find
        List<RuleName> listResult = ruleNameRepository.findAll();
        assertTrue(listResult.size() > 0);

        //Find by Id
        Optional<RuleName> optional = ruleNameRepository.findById(rule.getId());
        assertTrue(optional.isPresent());

        // Delete
        Integer id = rule.getId();
        ruleNameRepository.delete(rule);
        Optional<RuleName> ruleList = ruleNameRepository.findById(id);
        Assert.assertFalse(ruleList.isPresent());
    }
}
