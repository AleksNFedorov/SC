package com.scej.core;

import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.concordion.extension.exception.ScejException;
import com.scej.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.FailFast;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejExtensions.class)
@FailFast(onExceptionType = {ScejException.class})
public class CoreTest {
}
