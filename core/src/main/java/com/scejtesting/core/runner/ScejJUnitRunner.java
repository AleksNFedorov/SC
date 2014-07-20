package com.scejtesting.core.runner;

import com.scejtesting.core.context.SpecificationResultRegistry;
import org.concordion.api.ResultSummary;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ScejJUnitRunner extends ConcordionRunner {

    private final ScejStandAloneRunner runner;


    public ScejJUnitRunner(Class<?> fixtureClass) throws InitializationError {
        super(fixtureClass);
        this.runner = new ScejStandAloneRunner();
    }

    @Override
    protected Statement specExecStatement(final Object fixture) {
        return new Statement() {
            public void evaluate() throws Throwable {
                SpecificationResultRegistry registry = new SpecificationResultRegistry();
                ResultSummary result = runner.runSuite();
                registry.addResult(result);
                registry.processResults();
                registry.assertIsSatisfied(fixture);
            }
        };
    }
}
