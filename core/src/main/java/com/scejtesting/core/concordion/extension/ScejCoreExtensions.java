package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.*;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsThumbRendererProcessingListener;
import com.scejtesting.core.concordion.extension.specificationprocessing.VelocityResultsRenderer;
import com.scejtesting.core.config.Exceptions;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejCoreExtensions implements ConcordionExtension {

    private static final Logger LOG = LoggerFactory.getLogger(ScejCoreExtensions.class);

    private final TestContextService service = new TestContextService();

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        addFailFastExceptions(concordionExtender);
        concordionExtender.withSpecificationLocator(new HierarchySpecificationLocator());
        concordionExtender.withTarget(new FileTargetWithCustomPrefix());
        concordionExtender.withDocumentParsingListener(new DocumentParsingListenerFacade());
        concordionExtender.withSource(new ClassPathSpecificationSource());
        concordionExtender.withThrowableListener(new SuiteFailFastExceptionListener());
        concordionExtender.withSpecificationProcessingListener(new VelocityResultsRenderer());
        concordionExtender.withSpecificationProcessingListener(new ResultsThumbRendererProcessingListener());
        addCommands(concordionExtender);
        onExtensionInitialized();
    }

    protected void addCommands(ConcordionExtender extender) {
        for (ScejCommand scejCommand : getCommandsList()) {
            extender.withCommand(ScejCommand.SCEJ_TESTING_NAME_SPACE, scejCommand.getCommandName(), scejCommand);
        }
    }

    private void addFailFastExceptions(ConcordionExtender extender) {

        Check.isTrue(extender instanceof ConcordionBuilder, "Concordion builder instance expected");

        ConcordionBuilder concordionBuilder = (ConcordionBuilder) extender;

        Class<? extends Throwable>[] allExceptions = buildExceptionsSuperSet();

        concordionBuilder.withFailFast(allExceptions);

    }

    private Class<? extends Throwable>[] buildExceptionsSuperSet() {
        TestContext currentTestContext = service.getCurrentTestContext();
        Exceptions testExceptions = currentTestContext.getTest().getExceptions();
        Exceptions suiteExceptions = getCurrentSuite().getExceptions();

        Set<Class<? extends Throwable>> allExceptions = new HashSet<Class<? extends Throwable>>();

        allExceptions.addAll(testExceptions.getExceptions());
        allExceptions.addAll(suiteExceptions.getExceptions());

        return allExceptions.toArray(new Class[]{});

    }

    protected List<ScejCommand> getCommandsList() {
        return new LinkedList<ScejCommand>() {
            {
                add(new SetGlobalCommand());
                add(new RegisterGlobalVariablesCommand());
                add(new ScejRunCommand());
                add(new SaveResultsCommand());
            }
        };
    }

    private Suite getCurrentSuite() {
        return SuiteConfiguration.getInstance().getSuite();
    }

    private void onExtensionInitialized() {
        service.setTestContextInitialized();
    }

}
