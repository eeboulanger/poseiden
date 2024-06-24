package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleNameService implements IRuleNameService {
    @Autowired
    private RuleNameRepository repository;

    @Override
    public List<RuleName> getAllRuleNames() {
        return repository.findAll();
    }

    @Override
    public RuleName createRuleName(RuleName ruleName) {
        return repository.save(ruleName);
    }

    @Override
    public Optional<RuleName> getRuleNameById(int id) {
        return repository.findById(id);
    }

    @Override
    public RuleName updateRuleName(int id, RuleName ruleName) {
        return repository.findById(id).map(
                currentRuleName -> {
                    currentRuleName.setName(ruleName.getName());
                    currentRuleName.setDescription(ruleName.getDescription());
                    currentRuleName.setJson(ruleName.getJson());
                    currentRuleName.setTemplate(ruleName.getTemplate());
                    currentRuleName.setSqlStr(ruleName.getSqlStr());
                    currentRuleName.setSqlPart(ruleName.getSqlPart());
                    return repository.save(currentRuleName);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void deleteRuleName(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }
}
