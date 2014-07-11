package com.scejtesting.core.concordion.extension.documentparsing;

import nu.xom.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created by ofedorov on 7/10/14.
 */
public class DocumentParsingListenerFacadeTest {

    @Test
    public void nonEmptyListenerList() {
        Assert.assertNotNull(new DocumentParsingListenerFacade().getParsingListeners());
    }

    @Test
    public void positiveFlowTest() {

        NamedDocumentParsingListener mockListener = mock(NamedDocumentParsingListener.class);

        DocumentParsingListenerFacade runner = spy(new DocumentParsingListenerFacade());
        doReturn(Arrays.asList(mockListener, mockListener)).when(runner).getParsingListeners();
        Document documentMock = mock(Document.class);

        InOrder listener = inOrder(mockListener);

        runner.beforeParsing(documentMock);

        listener.verify(mockListener, calls(2)).beforeParsing(documentMock);
    }

    @Test(expected = RuntimeException.class)
    public void nullParsingDocument() {
        new DocumentParsingListenerFacade().beforeParsing(null);
    }

    @Test(expected = RuntimeException.class)
    public void documentParsingException() {
        NamedDocumentParsingListener mockListener = mock(NamedDocumentParsingListener.class);

        DocumentParsingListenerFacade runner = spy(new DocumentParsingListenerFacade());
        doReturn(Arrays.asList(mockListener)).when(runner).getParsingListeners();
        Document documentMock = mock(Document.class);
        doThrow(Exception.class).when(mockListener).beforeParsing(documentMock);

        runner.beforeParsing(documentMock);
    }
}
