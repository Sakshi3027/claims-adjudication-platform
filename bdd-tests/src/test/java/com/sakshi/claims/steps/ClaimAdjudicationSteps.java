package com.sakshi.claims.steps;

import com.sakshi.claims.domain.*;
import com.sakshi.claims.evaluator.HighDollarReviewRule;
import com.sakshi.claims.evaluator.PriorAuthMissingRule;
import com.sakshi.claims.evaluator.RuleEvaluator;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * These steps talk directly to the rules-engine module - no HTTP, no
 * Mongo. The .feature files are meant to be readable as a spec of the
 * adjudication rules themselves, independent of how they're eventually
 * exposed over the API.
 */
public class ClaimAdjudicationSteps {

    private final List<LineItem> lineItems = new ArrayList<>();
    private boolean priorAuthOnFile;
    private AdjudicationResult result;

    private final RuleEvaluator evaluator = new RuleEvaluator(
            List.of(new PriorAuthMissingRule(), new HighDollarReviewRule())
    );

    @Before
    public void reset() {
        lineItems.clear();
        priorAuthOnFile = false;
        result = null;
    }

    @Given("a claim for procedure code {string} with amount {string} that requires prior authorization")
    public void claimRequiringPriorAuth(String procedureCode, String amount) {
        lineItems.add(new LineItem(procedureCode, new BigDecimal(amount), true));
    }

    @Given("a claim for procedure code {string} with amount {string} that does not require prior authorization")
    public void claimNotRequiringPriorAuth(String procedureCode, String amount) {
        lineItems.add(new LineItem(procedureCode, new BigDecimal(amount), false));
    }

    @Given("prior authorization is on file")
    public void priorAuthIsOnFile() {
        priorAuthOnFile = true;
    }

    @Given("no prior authorization is on file")
    public void noPriorAuthOnFile() {
        priorAuthOnFile = false;
    }

    @When("the claim is adjudicated")
    public void adjudicateTheClaim() {
        Claim claim = new Claim("CLM-TEST", "MBR-TEST", ClaimType.MEDICAL,
                List.of(), List.copyOf(lineItems), priorAuthOnFile);
        result = evaluator.evaluate(claim);
    }

    @Then("the outcome should be {string}")
    public void theOutcomeShouldBe(String expectedOutcome) {
        assertEquals(AdjudicationOutcome.valueOf(expectedOutcome), result.outcome());
    }

    @Then("the reason should mention {string}")
    public void theReasonShouldMention(String expectedFragment) {
        assertTrue(result.reason().toLowerCase().contains(expectedFragment.toLowerCase()),
                "Expected reason to mention '" + expectedFragment + "' but was: " + result.reason());
    }
}
