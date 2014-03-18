package com.scej.core.concordion.extension.exceptions;

import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejExtensions.class)
public class NoFailFastExceptionTest extends CoreExceptionTest {

    @Override
    protected Throwable buildException() {
        return new NullPointerException("No fail fast exceptions");
    }
}
