package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;
import org.concordion.api.ResultSummary;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.internal.XMLParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 4/27/14.
 */
public class VelocityResultsRendererTest {

    public static final String SPECIFICATION_FAKE_CONTENT = "<html><body></body></html>";

    @Before
    public void init() {
        Specification root = new Specification("/root.html");
        com.scejtesting.core.config.Test test = mock(com.scejtesting.core.config.Test.class);
        when(test.getSpecification()).thenReturn(root);
        new TestContextService().createNewTestContext(test);
    }

    @Test
    public void defaultResultsTemplateFound() {
        VelocityResultsRenderer renderer = new VelocityResultsRenderer();
        String fileTemplate = renderer.getTemplateFileName();
        Assert.assertEquals(Constants.VELOCITY_DEFAULT_TEMPLATE_FILE, fileTemplate);
    }

    @Test
    public void positiveFlow() throws IOException {

        TestContext context = new TestContextService().getCurrentTestContext();

        SpecificationResultRegistry registry = context.getCurrentSpecificationContext().getResultRegistry();
        registry.addResult(buildResultSummary(2, 0, 1, 1));

        Element rootElement = buildNewRootElement();

        SpecificationProcessingEvent fakeEvent = new SpecificationProcessingEvent(null, rootElement);

        System.setProperty(Constants.VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY, "resultsUnitTest.vm");

        VelocityResultsRenderer renderer = spy(new VelocityResultsRenderer());

        doReturn(registry).when(renderer).getCurrentSpecificationResults();

        // First iteration

        renderer.afterProcessingSpecification(fakeEvent);

        Element resultElement = rootElement.getElementById("unitresult");

        Assert.assertNotNull(resultElement);

        Assert.assertEquals("2011", resultElement.getText().trim());

        // Second iteration

        registry.addResult(buildResultSummary(0, 2, 0, 1));

        renderer.afterProcessingSpecification(fakeEvent);

        resultElement = rootElement.getElementById("unitresult");

        Assert.assertNotNull(resultElement);

        Assert.assertEquals("2212", resultElement.getText().trim());
    }

    private ResultSummary buildResultSummary(long success, long fail, long exception, long ignore) {
        ResultSummary summaryMock = mock(ResultSummary.class);

        when(summaryMock.getExceptionCount()).thenReturn(exception);
        when(summaryMock.getSuccessCount()).thenReturn(success);
        when(summaryMock.getFailureCount()).thenReturn(fail);
        when(summaryMock.getIgnoredCount()).thenReturn(ignore);

        return summaryMock;
    }


    @Test(expected = RuntimeException.class)
    public void noTemplate() {

        System.setProperty(Constants.VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY, "someUnknownFile.vm");

        new VelocityResultsRenderer();
    }

    private Element buildNewRootElement() throws IOException {
        return new Element(XMLParser.parse(SPECIFICATION_FAKE_CONTENT).getRootElement());
    }

}
