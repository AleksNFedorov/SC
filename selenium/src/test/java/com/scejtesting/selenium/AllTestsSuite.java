package com.scejtesting.selenium;

import com.scejtesting.selenium.webdriver.ScejDriverServiceFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by aleks on 5/1/14.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SeleniumDriverManagerServiceTest.class,
        ScejDriverServiceFactoryTest.class,
})

public class AllTestsSuite {
}
