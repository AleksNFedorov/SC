package com.scejtesting.core.runner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ScejJUnitRunner extends Runner {

    private final ScejStandAloneRunner runner;

    public ScejJUnitRunner() {
        runner = new ScejStandAloneRunner();
    }

    @Override
    public Description getDescription() {
        return Description.createSuiteDescription("Scej suite runner");
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestRunStarted(getDescription());
        Result result = runSuite();
        notifier.fireTestRunFinished(result);
    }

    Result runSuite() {
        return runner.runSuite();
    }
}
