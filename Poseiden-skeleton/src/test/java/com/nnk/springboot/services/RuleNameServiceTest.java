package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest {

    @Mock
    private RuleNameRepository repository;
    @InjectMocks
    private RuleNameService ruleNameService;
    private RuleName ruleName;

    @BeforeEach
    public void setUp() {
        ruleName = new RuleName("name", "description", "json", "template", "sqlStr", "sqlPart");
        ruleName.setId(1);
    }

    @Test
    public void getAllTest() {
        List<RuleName> list = List.of(ruleName);
        when(repository.findAll()).thenReturn(list);

        List<RuleName> result = ruleNameService.getAllRuleNames();

        assertEquals(ruleName.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void createRatingTest() {
        when(repository.save(ruleName)).thenReturn(ruleName);

        RuleName result = ruleNameService.createRuleName(ruleName);

        assertEquals(ruleName.getId(), result.getId());
        verify(repository, times(1)).save(ruleName);
    }

    @Test
    public void getRatingByIdTest() {
        when(repository.findById(1)).thenReturn(Optional.of(ruleName));

        Optional<RuleName> result = ruleNameService.getRuleNameById(1);

        assertTrue(result.isPresent());
        assertEquals(ruleName.getId(), result.get().getId());
        verify(repository, times(1)).findById(1);
    }

    @Test
    public void updateRatingSuccessTest() {
        RuleName dto = new RuleName("Updated name", "Updated description", "Updated json",
                "Updated template", "sqlStr", "sqlPart");
        when(repository.findById(1)).thenReturn(Optional.ofNullable(ruleName));
        when(repository.save(any(RuleName.class))).thenReturn(ruleName);

        RuleName result = ruleNameService.updateRuleName(1, dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getJson(), result.getJson());
        assertEquals(dto.getTemplate(), result.getTemplate());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(ruleName);
    }

    @Test
    @DisplayName("Given there is no rating with id, then don't update and throw exception")
    public void updateRatingFailsTest() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ruleNameService.updateRuleName(1, ruleName));
        verify(repository, never()).save(any(RuleName.class));
    }

    @Test
    public void deleteRatingSuccessTest() {
        when(repository.existsById(1)).thenReturn(true);

        ruleNameService.deleteRuleName(1);

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Given no rating with id exists, then don't delete and throw exception")
    public void deleteRatingFailsTest() {
        when(repository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> ruleNameService.deleteRuleName(1));

        verify(repository, never()).deleteById(1);
    }
}
