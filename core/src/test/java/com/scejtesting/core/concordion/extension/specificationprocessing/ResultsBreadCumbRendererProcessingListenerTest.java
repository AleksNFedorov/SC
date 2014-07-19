package com.scejtesting.core.concordion.extension.specificationprocessing;

import org.concordion.api.Element;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by ofedorov on 7/10/14.
 */
public class ResultsBreadCumbRendererProcessingListenerTest {

    @Test(expected = RuntimeException.class)
    public void testNullSpecificationEvent() {
        SpecificationProcessingEvent mockEvent = mock(SpecificationProcessingEvent.class);

        ResultsBreadcumbRendererProcessingListener listener = spy(new ResultsBreadcumbRendererProcessingListener());

        doReturn(null).when(listener).getSpecificationRootElement(mockEvent);

        listener.afterProcessingSpecification(mockEvent);
    }

    @Test(expected = RuntimeException.class)
    public void testNoBodyElementInSpecification() {
        new ResultsBreadcumbRendererProcessingListener().buildThumbContainer(null);
    }


    @Test
    public void thumbBuildException() throws IOException {
        Element bodyElement = new Element("body");

        SpecificationProcessingEvent mockEvent = mock(SpecificationProcessingEvent.class);

        ResultsBreadcumbRendererProcessingListener listener = spy(new ResultsBreadcumbRendererProcessingListener());

        doReturn(bodyElement).when(listener).getSpecificationRootElement(mockEvent);
        doThrow(IOException.class).when(listener).buildThumbLinks();

        listener.afterProcessingSpecification(mockEvent);

        Assert.assertEquals(1, bodyElement.getChildElements().length);
        Assert.assertEquals(1, bodyElement.getChildElements("div").length);
        Assert.assertEquals(0, bodyElement.getChildElements("div")[0].getChildElements().length);

    }

    @Test
    public void positiveFlowTest() throws IOException {

        Element bodyElement = new Element("body");

        Element linkElement = new Element("ThumbLink");

        SpecificationProcessingEvent mockEvent = mock(SpecificationProcessingEvent.class);

        ResultsBreadcumbRendererProcessingListener listener = spy(new ResultsBreadcumbRendererProcessingListener());

        doReturn(bodyElement).when(listener).getSpecificationRootElement(mockEvent);
        doReturn(Arrays.asList(linkElement)).when(listener).buildThumbLinks();

        listener.afterProcessingSpecification(mockEvent);

        Assert.assertEquals(1, bodyElement.getChildElements().length);
        Assert.assertEquals(1, bodyElement.getChildElements("div").length);
        Assert.assertEquals(3, bodyElement.getChildElements("div")[0].getChildElements().length);
        Assert.assertEquals(1, bodyElement.getChildElements("div")[0].getChildElements("br").length);
        Assert.assertEquals(1, bodyElement.getChildElements("div")[0].getChildElements("ThumbLink").length);
        Assert.assertEquals(1, bodyElement.getChildElements("div")[0].getChildElements("span").length);
        Assert.assertEquals(linkElement, bodyElement.getChildElements("div")[0].getChildElements()[0]);

    }
}
