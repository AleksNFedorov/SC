package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsBreadcumbRendererProcessingListener;
import com.scejtesting.core.concordion.extension.specificationprocessing.VelocityResultsRenderer;
import com.scejtesting.core.config.Exceptions;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.internal.ConcordionBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/9/14.
 */
public class ScejCoreExtensionsTest {

    @Test(expected = RuntimeException.class)
    public void initFailFastWithNonConcordionBuilder() {
        new ScejCoreExtensions().addTo(mock(ConcordionExtender.class));
    }

    @Test
    public void initFailFastExceptionsNoExceptions() {
        Specification testSpec = new Specification("/test.html");
        com.scejtesting.core.config.Test mockTest = mock(com.scejtesting.core.config.Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        Exceptions exceptions = mock(Exceptions.class);

        ArrayList<Class<? extends Throwable>> exceptionsList = new ArrayList<Class<? extends Throwable>>();
        exceptionsList.add(IllegalArgumentException.class);

        when(exceptions.getExceptions()).thenReturn(exceptionsList);
        when(mockTest.getExceptions()).thenReturn(exceptions);

        TestContextService service = new TestContextService();
        service.createNewTestContext(mockTest);

        Suite mockSuite = mock(Suite.class);

        final ConcordionBuilder extender = spy(new ConcordionBuilder());


        ScejCoreExtensions extensions = spy(new ScejCoreExtensions());
        doReturn(extender).when(extensions).convertToConcordionBuilder(extender);
        doReturn(mockSuite).when(extensions).getCurrentSuite();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Class[] exceptions = (Class[]) invocationOnMock.getArguments()[0];
                Assert.assertTrue(exceptions.length == 1);
                Assert.assertEquals(IllegalArgumentException.class, exceptions[0]);
                return extender;
            }
        }).when(extender).withFailFast(any(new Class[]{}.getClass()));

        extensions.addTo(extender);

    }

    @Test
    public void testFinishOnException() {

        Specification testSpec = new Specification("/test.html");
        com.scejtesting.core.config.Test mockTest = mock(com.scejtesting.core.config.Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        TestContextService service = spy(new TestContextService());
        service.createNewTestContext(mockTest);

        ConcordionExtender extender = mock(ConcordionExtender.class);

        ScejCoreExtensions extensions = spy(new ScejCoreExtensions());
        doReturn(service).when(extensions).getTestContextService();


        try {
            extensions.addTo(extender);
            Assert.fail("Exception expected");
        } catch (Exception ex) {

        }

        InOrder inOrder = inOrder(service);

        inOrder.verify(service, calls(1)).revertContextSwitch();
    }

    @Test
    public void positiveFlowTest() {

        Specification testSpec = new Specification("/test.html");
        com.scejtesting.core.config.Test mockTest = mock(com.scejtesting.core.config.Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        TestContextService service = spy(new TestContextService());
        service.createNewTestContext(mockTest);

        Suite mockSuite = mock(Suite.class);

        final ConcordionBuilder extender = spy(new ConcordionBuilder());

        InOrder inOrder = inOrder(extender);
        InOrder serviceInOrder = inOrder(service);

        ScejCoreExtensions extensions = spy(new ScejCoreExtensions());
        doReturn(extender).when(extensions).convertToConcordionBuilder(extender);
        doReturn(mockSuite).when(extensions).getCurrentSuite();
        doReturn(service).when(extensions).getTestContextService();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Class[] exceptions = (Class[]) invocationOnMock.getArguments()[0];
                Assert.assertTrue(exceptions.length == 0);
                return extender;
            }
        }).when(extender).withFailFast(any(new Class[]{}.getClass()));

        extensions.addTo(extender);

        serviceInOrder.verify(service, calls(1)).revertContextSwitch();

        inOrder.verify(extender, calls(1)).withSpecificationLocator(any(HierarchySpecificationLocator.class));
        inOrder.verify(extender, calls(1)).withTarget(any(FileTargetWithCustomPrefix.class));
        inOrder.verify(extender, calls(1)).withDocumentParsingListener(any(DocumentParsingListenerFacade.class));
        inOrder.verify(extender, calls(1)).withSource(any(ClassPathSpecificationSource.class));
        inOrder.verify(extender, calls(1)).withThrowableListener(any(SuiteFailFastExceptionListener.class));
        inOrder.verify(extender, calls(1)).withSpecificationProcessingListener(any(VelocityResultsRenderer.class));
        inOrder.verify(extender, calls(1)).withSpecificationProcessingListener(any(ResultsBreadcumbRendererProcessingListener.class));

        inOrder.verify(extender, calls(3)).
                withCommand(
                        eq(ScejCommand.SCEJ_TESTING_NAME_SPACE),
                        anyString(),
                        any(ScejCommand.class));

    }

}
