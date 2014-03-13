package com.scej.core;

import com.scej.core.concordion.extension.*;
import com.scej.core.runner.ScejRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ScejRunner.class)
@Extensions(value = ScejExtensions.class)
//@FailFast(onExceptionType = {UnreachableBrowserException.class, NoSuchWindowException.class})
public class CoreTest {
}
