package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.ICrudService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(controllers = RuleNameController.class)
@WithMockUser(roles = "USER")
public class RuleNameControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ICrudService<RuleName> ruleNameService;
    @InjectMocks
    private RuleNameController ruleNameController;
    private static RuleName ruleName;

    @BeforeAll
    public static void setUp() {
        ruleName = new RuleName("Name", "Description", "Json", "Template",
                "SqlStr", "SqlPart");
        ruleName.setId(1);
    }

    @Test
    public void homeTest() throws Exception {
        List<RuleName> list = List.of(ruleName);

        when(ruleNameService.getAll()).thenReturn(list);
        mockMvc.perform(get("/ruleName/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", hasSize(1)))
                .andExpect(model().attribute("ruleNames", contains(
                        allOf(hasProperty("name", is(ruleName.getName())),
                                hasProperty("description", is(ruleName.getDescription())),
                                hasProperty("json", is(ruleName.getJson())),
                                hasProperty("template", is(ruleName.getTemplate())),
                                hasProperty("sqlStr", is(ruleName.getSqlStr())),
                                hasProperty("sqlPart", is(ruleName.getSqlPart()))
                        ))));

        verify(ruleNameService, times(1)).getAll();
    }

    @Test
    public void addRuleFormTest() throws Exception {
        mockMvc.perform(get("/ruleName/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    public void validateRuleNameSuccessTest() throws Exception {
        when(ruleNameService.save(ArgumentMatchers.any(RuleName.class))).thenReturn(new RuleName());

        mockMvc.perform(post("/ruleName/validate")
                        .param("name", ruleName.getName())
                        .param("description", ruleName.getDescription())
                        .param("json", ruleName.getJson())
                        .param("template", ruleName.getTemplate())
                        .param("sqlStr", ruleName.getSqlStr())
                        .param("sqlPart", ruleName.getSqlPart())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(ArgumentMatchers.any(RuleName.class));
    }

    //TODO test validate fails, decide on validation rules for Rule name dto?

    @Test
    public void showUpdateFormTest() throws Exception {
        when(ruleNameService.getById(1)).thenReturn(Optional.ofNullable(ruleName));

        mockMvc.perform(get("/ruleName/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", hasProperty("name", is(ruleName.getName()))))
                .andExpect(model().attribute("ruleName", hasProperty("description", is(ruleName.getDescription()))))
                .andExpect(model().attribute("ruleName", hasProperty("json", is(ruleName.getJson()))))
                .andExpect(model().attribute("ruleName", hasProperty("template", is(ruleName.getTemplate()))))
                .andExpect(model().attribute("ruleName", hasProperty("sqlStr", is(ruleName.getSqlStr()))))
                .andExpect(model().attribute("ruleName", hasProperty("sqlPart", is(ruleName.getSqlPart()))));

        verify(ruleNameService, times(1)).getById(1);
    }

    @Test
    @DisplayName("Given there's no rule name with the id, then redirect to list")
    public void givenNoRuleNameWithId_whenUpdate_thenDontAddToModel() throws Exception {
        when(ruleNameService.getById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ruleName/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

        verify(ruleNameService, times(1)).getById(1);
    }

    @Test
    public void updateRuleNameTest() throws Exception {
        when(ruleNameService.update(eq(1), ArgumentMatchers.any(RuleName.class))).thenReturn(ruleName);

        mockMvc.perform(post("/ruleName/update/{id}", 1)
                        .param("name", ruleName.getName())
                        .param("description", ruleName.getDescription())
                        .param("json", ruleName.getJson())
                        .param("template", ruleName.getTemplate())
                        .param("sqlStr", ruleName.getSqlStr())
                        .param("sqlPart", ruleName.getSqlPart())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
        verify(ruleNameService, times(1)).update(eq(1), ArgumentMatchers.any(RuleName.class));
    }

    @Test
    public void deleteRuleNameTest() throws Exception {
        mockMvc.perform(get("/ruleName/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).delete(1);
    }
}

