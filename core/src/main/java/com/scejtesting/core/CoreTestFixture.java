package com.scejtesting.core;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class CoreTestFixture {

    public List asList(Object firstParameter, Object secondParameter) {
        return Arrays.asList(firstParameter, secondParameter);
    }

}
