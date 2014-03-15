package com.scej.core;

import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.runner.ScejTestRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ScejTestRunner.class)
@Extensions(value = ScejExtensions.class)
//@FailFast(onExceptionType = {UnreachableBrowserException.class, NoSuchWindowException.class})
public class CoreTest {
}
