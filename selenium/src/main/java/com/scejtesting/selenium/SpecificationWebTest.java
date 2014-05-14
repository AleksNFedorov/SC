package com.scejtesting.selenium;

import com.scejtesting.selenium.concordion.extension.ScejSeleniumExtension;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

/**
 * Created by aleks on 5/10/14.
 */
@RunWith(ConcordionRunner.class)
@Extensions(value = ScejSeleniumExtension.class)
public class SpecificationWebTest extends WebTestFixture {
}
