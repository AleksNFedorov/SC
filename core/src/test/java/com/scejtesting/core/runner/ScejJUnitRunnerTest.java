package com.scejtesting.core.runner;

import org.junit.runners.model.Statement;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * User: Fedorovaleks
 * Date: 3/19/14
 */
public class ScejJUnitRunnerTest {


    @org.junit.Test(expected = AssertionError.class)
    public void testFailFlow() throws Throwable {
        ScejJUnitRunner runner = spy(new ScejJUnitRunner(this.getClass()));

        doReturn(new ResultSummaryAdapter(0, 1, 0, 0)).when(runner).runScejSuite();

        Statement statement = runner.specExecStatement(this);
        statement.evaluate();
    }


    @org.junit.Test
    public void testSuccessFlow() throws Throwable {

        ScejJUnitRunner runner = spy(new ScejJUnitRunner(this.getClass()));

        doReturn(new ResultSummaryAdapter(1, 0, 0, 0)).when(runner).runScejSuite();

        Statement statement = runner.specExecStatement(this);
        statement.evaluate();

        //must finish without exceptions

    }


}
