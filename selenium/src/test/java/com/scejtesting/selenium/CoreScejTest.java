package com.scejtesting.selenium;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 5/3/14.
 */
public abstract class CoreScejTest {

    @BeforeClass
    public static void init() {

        Test specTest = mock(Test.class);
        when(specTest.getSpecification()).thenReturn(new Specification());
        new TestContextService().createNewTestContext(specTest);

    }

    @AfterClass
    public static void finish() {
        new TestContextService().destroyTestContext();
    }

    @After
    public void cleanTest() {
        new TestContextService().getCurrentTestContext().
                cleanAttribute(CoreWebTestFixture.SCEJ_DRIVER_SERVICE);

    }
}
