package com.scejtesting.selenium;

import com.scejtesting.selenium.webdriver.WebDriverControllerFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by aleks on 5/1/14.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CoreWebTestFixtureTest.class,
        WebDriverControllerFactoryTest.class,
        WebTestFixtureTest.class
})

public class AllTestsSuite {
}
