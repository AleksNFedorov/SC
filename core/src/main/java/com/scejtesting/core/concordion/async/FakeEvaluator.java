package com.scejtesting.core.concordion.async;

import org.concordion.api.Evaluator;

/**
 * Created by aleks on 7/23/14.
 */
public class FakeEvaluator implements Evaluator {
    @Override
    public Object getVariable(String variableName) {
        return null;
    }

    @Override
    public void setVariable(String variableName, Object value) {

    }

    @Override
    public Object evaluate(String expression) {
        return null;
    }
}
