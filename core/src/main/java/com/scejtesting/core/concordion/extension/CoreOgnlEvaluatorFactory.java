package com.scejtesting.core.concordion.extension;

import org.concordion.api.Evaluator;
import org.concordion.api.EvaluatorFactory;
import org.concordion.internal.OgnlEvaluator;

/**
 * Created by aleks on 8/8/14.
 */
public class CoreOgnlEvaluatorFactory implements EvaluatorFactory {
    @Override
    public Evaluator createEvaluator(Object fixture) {
        return new OgnlEvaluator(fixture);
    }
}
