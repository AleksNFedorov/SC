package com.scejtesting.core;

import com.scejtesting.core.concordion.extension.ScejExtensions;
import com.scejtesting.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejExtensions.class)
public class CoreTest {
}
