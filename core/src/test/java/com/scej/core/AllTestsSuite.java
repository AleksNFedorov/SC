package com.scej.core;

import com.scej.core.concordion.ChildSpecificationRunnerTest;
import com.scej.core.concordion.extension.FileTargetWithDateTimePrefixTest;
import com.scej.core.concordion.extension.HierarchySpecificationLocatorTest;
import com.scej.core.config.SpecificationLocatorServiceTest;
import com.scej.core.config.SpecificationTest;
import com.scej.core.config.SuiteInitialization;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TestContextTest.class,
        SuiteInitialization.class,
        SpecificationTest.class,
        SpecificationLocatorServiceTest.class,
        ChildSpecificationRunnerTest.class,
        HierarchySpecificationLocatorTest.class,
        FileTargetWithDateTimePrefixTest.class})
public class AllTestsSuite {
}
