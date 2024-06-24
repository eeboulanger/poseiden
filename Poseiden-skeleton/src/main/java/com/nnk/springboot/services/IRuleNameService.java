package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;
import java.util.Optional;

/**
 * Any class that handles operations on Rule name entity
 */
public interface IRuleNameService {

    List<RuleName> getAllRuleNames();

    RuleName createRuleName(RuleName ruleName);

    Optional<RuleName> getRuleNameById(int id);

    RuleName updateRuleName(int id, RuleName ruleName);

    void deleteRuleName(int id);
}
