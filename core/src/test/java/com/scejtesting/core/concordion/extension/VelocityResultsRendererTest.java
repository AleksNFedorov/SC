package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.context.SpecificationResultRegistry;
import org.concordion.api.Element;
import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.internal.XMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by aleks on 4/27/14.
 */
public class VelocityResultsRendererTest {

    public static final String SPECIFICATION_FAKE_CONTENT = "<html><body></body></html>";

    @Test
    public void defaultResultsTemplateFound() {
        VelocityResultsRenderer renderer = new VelocityResultsRenderer();
        String fileTemplate = renderer.getTemplateFileName();
        Assert.assertEquals(VelocityResultsRenderer.VELOCITY_DEFAULT_TEMPLATE_FILE, fileTemplate);
    }

    @Test
    public void positiveFlow() throws IOException {

        SpecificationResultRegistry registry = new SpecificationResultRegistry();
        registry.addResult(new RunnerResult(Result.SUCCESS));
        registry.addResult(new RunnerResult(Result.SUCCESS));
        registry.addResult(new RunnerResult(Result.EXCEPTION));
        registry.addResult(new RunnerResult(Result.IGNORED));


        SpecificationResultRegistry newRegistry = new SpecificationResultRegistry();

        newRegistry.addResult(new RunnerResult(Result.FAILURE));
        newRegistry.addResult(new RunnerResult(Result.FAILURE));
        newRegistry.addResult(new RunnerResult(Result.IGNORED));


        Element rootElement = buildNewRootElement();

        SpecificationProcessingEvent fakeEvent = new SpecificationProcessingEvent(null, rootElement);

        System.setProperty(VelocityResultsRenderer.VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY, "resultsUnitTest.vm");

        VelocityResultsRenderer renderer = spy(new VelocityResultsRenderer());

        doReturn(registry).when(renderer).getCurrentSpecificationResults();


        // First iteration

        renderer.afterProcessingSpecification(fakeEvent);

        Element resultElement = rootElement.getElementById("unitresult");

        Assert.assertNotNull(resultElement);

        Assert.assertEquals("2011", resultElement.getText().trim());

        // Second iteration


        registry.addAll(newRegistry);

        renderer.afterProcessingSpecification(fakeEvent);

        resultElement = rootElement.getElementById("unitresult");

        Assert.assertNotNull(resultElement);

        Assert.assertEquals("2212", resultElement.getText().trim());
    }

    @Test(expected = RuntimeException.class)
    public void noTemplate() {

        System.setProperty(VelocityResultsRenderer.VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY, "someUnknownFile.vm");

        new VelocityResultsRenderer();
    }


    private Element buildNewRootElement() throws IOException {
        return new Element(XMLParser.parse(SPECIFICATION_FAKE_CONTENT).getRootElement());
    }
}
