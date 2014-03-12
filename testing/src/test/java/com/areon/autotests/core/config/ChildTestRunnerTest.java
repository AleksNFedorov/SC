package com.areon.autotests.core.config;

import com.areon.autotests.core.WebCoreTest;
import com.areon.autotests.core.integration.ChildTestRunner;
import com.areon.autotests.core.integration.GlobalTestContext;
import org.junit.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 15.01.14
 * Time: 9:39
 * To change this template use File | Settings | File Templates.
 */
public class ChildTestRunnerTest {

    @org.junit.Test
    public void testIncludeAll() {

        initGlobalContextWithRootSpecification(new Specification("includeAllSpecification"));

        ChildTestRunner testRunner = new ChildTestRunner();
        Specification specification = testRunner.resolveSpecification("fakeSpecification");
        Assert.assertNotNull("Resolved specification is null");
        Assert.assertEquals("fakeSpecification", specification.getLocation());
    }

    @org.junit.Test
    public void testExcludeOnlyAll() {


        Specification fakeSpecificationTwo = new Specification("FakeTwo");
        Specification fakeSpecificationThree = new Specification("FakeThree");

        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationTwo, fakeSpecificationThree));
        Specification excludeOnly = mock(Specification.class);
        when(excludeOnly.getExcludes()).thenReturn(excludes);

        initGlobalContextWithRootSpecification(excludeOnly);

        ChildTestRunner testRunner = new ChildTestRunner();

        Specification currentSpec = testRunner.resolveSpecification("someNotExludedSpec");

        Assert.assertNotNull("Not excluded spec can't be null", currentSpec);
        Assert.assertEquals("someNotExludedSpec", currentSpec.getLocation());

        currentSpec = testRunner.resolveSpecification("FakeTwo");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeThree");
        Assert.assertNull("Excluded spec must be null", currentSpec);


    }


    @org.junit.Test
    public void testIncludeOnly() {
        Specification fakeSpecificationOne = new Specification("FakeOne");
        Specification fakeSpecificationTwo = new Specification("FakeTwo");
        Specification fakeSpecificationThree = new Specification("FakeThree");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo,
                fakeSpecificationThree));

        Specification includeOnly = mock(Specification.class);
        when(includeOnly.getIncludes()).thenReturn(includes);

        initGlobalContextWithRootSpecification(includeOnly);

        ChildTestRunner testRunner = new ChildTestRunner();

        Specification currentSpec = testRunner.resolveSpecification("someSpec");
        Assert.assertNull("Not included spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeTwo");
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals("FakeTwo", currentSpec.getLocation());

        currentSpec = testRunner.resolveSpecification("FakeOne");
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals("FakeOne", currentSpec.getLocation());

        currentSpec = testRunner.resolveSpecification("FakeThree");
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals("FakeThree", currentSpec.getLocation());
    }

    @org.junit.Test
    public void testIncludeExclude() {
        Specification fakeSpecificationOne = new Specification("FakeOne");
        Specification fakeSpecificationTwo = new Specification("FakeTwo");
        Specification fakeSpecificationThree = new Specification("FakeThree");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo));
        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationOne, fakeSpecificationThree));
        Specification includeExclude = mock(Specification.class);

        when(includeExclude.getExcludes()).thenReturn(excludes);
        when(includeExclude.getIncludes()).thenReturn(includes);

        initGlobalContextWithRootSpecification(includeExclude);

        ChildTestRunner testRunner = new ChildTestRunner();

        Specification currentSpec = testRunner.resolveSpecification("someNotExludedSpec");
        Assert.assertNull("Not included spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeTwo");
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals("FakeTwo", currentSpec.getLocation());


        currentSpec = testRunner.resolveSpecification("FakeOne");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeThree");
        Assert.assertNull("Excluded spec must be null", currentSpec);
    }

    private static void initGlobalContextWithRootSpecification(Specification rootSpecification) {
        RemoteWebDriver driver = mock(RemoteWebDriver.class);
        Test test = new Test(rootSpecification, WebCoreTest.class);
        GlobalTestContext.createGlobalContext(test);
        GlobalTestContext.getInstance().initWithSeleniumDriver(driver);
    }

}
