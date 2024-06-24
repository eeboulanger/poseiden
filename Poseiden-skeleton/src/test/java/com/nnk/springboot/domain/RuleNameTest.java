package com.nnk.springboot.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleNameTest {
    @Test
    public void getterAndSetterTest() {
        RuleName ruleName=new RuleName();
        ruleName.setName("Name");
        ruleName.setDescription("Description");
        ruleName.setJson("json");
        ruleName.setTemplate("Template");
        ruleName.setSqlPart("sqlPart");
        ruleName.setSqlStr("SqlStr");

        assertEquals("Name", ruleName.getName());
        assertEquals("Description", ruleName.getDescription());
        assertEquals("json", ruleName.getJson());
        assertEquals("Template", ruleName.getTemplate());
        assertEquals("sqlPart", ruleName.getSqlPart());
        assertEquals("SqlStr", ruleName.getSqlStr());
    }
}
