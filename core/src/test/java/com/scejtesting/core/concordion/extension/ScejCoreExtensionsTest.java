package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsThumbRendererProcessingListener;
import com.scejtesting.core.concordion.extension.specificationprocessing.VelocityResultsRenderer;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.extension.ConcordionExtender;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/9/14.
 */
public class ScejCoreExtensionsTest {

    @Test
    public void positiveFlowTest() {

        Specification testSpec = new Specification("/test.html");
        com.scejtesting.core.config.Test mockTest = mock(com.scejtesting.core.config.Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        TestContextService service = new TestContextService();
        service.createNewTestContext(mockTest);

        ConcordionExtender extender = mock(ConcordionExtender.class);

        InOrder inOrder = inOrder(extender);

        ScejCoreExtensions extensions = new ScejCoreExtensions();

        extensions.addTo(extender);

        Assert.assertTrue(new TestContextService().isExtensionInitialized());

        inOrder.verify(extender, calls(1)).withSpecificationLocator(any(HierarchySpecificationLocator.class));
        inOrder.verify(extender, calls(1)).withTarget(any(FileTargetWithCustomPrefix.class));
        inOrder.verify(extender, calls(1)).withDocumentParsingListener(any(DocumentParsingListenerFacade.class));
        inOrder.verify(extender, calls(1)).withSource(any(ClassPathSpecificationSource.class));
        inOrder.verify(extender, calls(1)).withThrowableListener(any(SuiteFailFastExceptionListener.class));
        inOrder.verify(extender, calls(1)).withSpecificationProcessingListener(any(VelocityResultsRenderer.class));
        inOrder.verify(extender, calls(1)).withSpecificationProcessingListener(any(ResultsThumbRendererProcessingListener.class));

        inOrder.verify(extender, calls(3)).
                withCommand(
                        eq(ScejCommand.SCEJ_TESTING_NAME_SPACE),
                        anyString(),
                        any(ScejCommand.class));

    }

}
