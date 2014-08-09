package com.scejtesting.selenium;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 5/3/14.
 */
public abstract class CoreScejTest<T extends CoreWebTestFixture> {

    public static final String OPERATE_URL = CoreScejTest.class.getClassLoader().getResource("WebTestFixtureTest.html").toString();
    public static final String OPERATE_URL2 = CoreScejTest.class.getClassLoader().getResource("WebTestFixture2Test.html").toString();

    public static final String OPERATE_URL_TITLE = "WebTestFixtureTest";
    public static final String OPERATE_URL2_TITLE = "WebTestFixture2Test";
    protected T currentTestFixture;


    protected CoreScejTest() {
    }

    @BeforeClass
    public static void init() {
        Test specTest = mock(Test.class);
        when(specTest.getSpecification()).thenReturn(new Specification());
        new TestContextService().createNewTestContext(specTest);
    }

    protected abstract T buildTestFixture();

    @Before
    public void initTestCase() {

        currentTestFixture = buildTestFixture();

        currentTestFixture.openDriver("fakeDriver");

        currentTestFixture.goToURL(OPERATE_URL.toString());

    }

    @After
    public void finishTestCase() {
        currentTestFixture.closeCurrentDriver();
        currentTestFixture = null;

        new TestContextService().getCurrentTestContext().
                cleanAttribute(DriverHolderService.SCEJ_DRIVER_SERVICE);

    }

}
