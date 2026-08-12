package com.sakshi.claims.config;

import com.sakshi.claims.evaluator.AdjudicationRule;
import com.sakshi.claims.evaluator.HighDollarReviewRule;
import com.sakshi.claims.evaluator.PriorAuthMissingRule;
import com.sakshi.claims.evaluator.RuleEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wires up the active rule set. Adding a new rule to production is
 * "write the class, add it to this list, add a matching .feature file" -
 * no changes needed anywhere else in the api module.
 */
@Configuration
public class RuleEngineConfig {

    @Bean
    public List<AdjudicationRule> adjudicationRules() {
        return List.of(
                new PriorAuthMissingRule(),
                new HighDollarReviewRule()
        );
    }

    @Bean
    public RuleEvaluator ruleEvaluator(List<AdjudicationRule> adjudicationRules) {
        return new RuleEvaluator(adjudicationRules);
    }
}
