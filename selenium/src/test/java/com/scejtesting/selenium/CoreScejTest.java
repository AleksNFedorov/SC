package com.scejtesting.selenium;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 5/3/14.
 */
public abstract class CoreScejTest<T extends CoreWebTestFixture> {

    protected final URL operateURL;
    protected T currentTestFixture;

    protected CoreScejTest() {
        operateURL = getClass().getClassLoader().getResource("WebTestFixtureTest.html");
    }

    @BeforeClass
    public static void init() {

        Test specTest = mock(Test.class);
        when(specTest.getSpecification()).thenReturn(new Specification());
        new TestContextService().createNewTestContext(specTest);

    }

    @AfterClass
    public static void finish() {
        new TestContextService().destroyTestContextService();
    }

    protected abstract T buildTestFixture();

    @Before
    public void initTestCase() {

        currentTestFixture = buildTestFixture();

        currentTestFixture.openDriver("fakeDriver");

        currentTestFixture.goToURL(operateURL.toString());

    }

    @After
    public void finishTestCase() {
        currentTestFixture.closeCurrentDriver();
        currentTestFixture = null;

        new TestContextService().getCurrentTestContext().
                cleanAttribute(DriverHolderService.SCEJ_DRIVER_SERVICE);

    }

}
