package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.*;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.concordion.extension.specificationprocessing.AsyncLaunchResultsProcessor;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsBreadcumbRendererProcessingListener;
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

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        LOG.debug("method invoked");
        try {
            addFailFastExceptions(concordionExtender);
            concordionExtender.withSpecificationLocator(new HierarchySpecificationLocator());
            concordionExtender.withTarget(new FileTargetWithCustomPrefix());
            concordionExtender.withDocumentParsingListener(new DocumentParsingListenerFacade());
            concordionExtender.withSource(new ClassPathSpecificationSource());
            concordionExtender.withThrowableListener(new SuiteFailFastExceptionListener());
            concordionExtender.withSpecificationProcessingListener(new AsyncLaunchResultsProcessor());
            concordionExtender.withSpecificationProcessingListener(new VelocityResultsRenderer());
            concordionExtender.withSpecificationProcessingListener(new ResultsBreadcumbRendererProcessingListener());
            addCommands(concordionExtender);
        } catch (Throwable throwable) {
            LOG.error("Exception during test initialization [{}]", throwable.getMessage(), throwable);
            throw new RuntimeException(throwable);
        } finally {
            onExtensionInitialized();
            LOG.debug("method finished");
        }
    }

    protected void addCommands(ConcordionExtender extender) {
        for (ScejCommand scejCommand : getCommandsList()) {
            extender.withCommand(ScejCommand.SCEJ_TESTING_NAME_SPACE, scejCommand.getCommandName(), scejCommand);
            LOG.debug("Command [{}] has been added to extension", scejCommand);
        }
    }

    private void addFailFastExceptions(ConcordionExtender extender) {

        LOG.debug("method invoked");

        ConcordionBuilder concordionBuilder = convertToConcordionBuilder(extender);

        Class<? extends Throwable>[] allExceptions = buildExceptionsSuperSet();

        concordionBuilder.withFailFast(allExceptions);

        LOG.info("Registered [{}] fail fast exceptions", allExceptions.length);

        LOG.debug("method finished");

    }

    ConcordionBuilder convertToConcordionBuilder(ConcordionExtender extender) {
        Check.isTrue(extender instanceof ConcordionBuilder, "Concordion builder instance expected");
        return (ConcordionBuilder) extender;
    }

    private Class<? extends Throwable>[] buildExceptionsSuperSet() {
        TestContext currentTestContext = getTestContextService().getCurrentTestContext();
        Exceptions testExceptions = currentTestContext.getTest().getExceptions();
        Exceptions suiteExceptions = getCurrentSuite().getExceptions();

        Set<Class<? extends Throwable>> allExceptions = new HashSet<Class<? extends Throwable>>();

        if (testExceptions != null) {
            allExceptions.addAll(testExceptions.getExceptions());
            LOG.debug("Found [{}] test exceptions", testExceptions.getExceptions());
        }

        if (suiteExceptions != null) {
            allExceptions.addAll(suiteExceptions.getExceptions());
            LOG.debug("Found [{}] suite exceptions", suiteExceptions.getExceptions());
        }

        LOG.info("Found [{}] fail fast exceptions", allExceptions.size());

        return allExceptions.toArray(new Class[]{});

    }

    protected List<ScejCommand> getCommandsList() {
        return new LinkedList<ScejCommand>() {
            {
                add(new SetGlobalCommand());
                add(new RegisterGlobalVariablesCommand());
                add(new ScejRunCommand());
                add(new ScejRunAsyncCommand());
                add(new SaveResultsCommand());
            }
        };
    }


    private void onExtensionInitialized() {
        getTestContextService().setTestContextInitialized();
    }

    Suite getCurrentSuite() {
        return SuiteConfiguration.getInstance().getSuite();
    }

    TestContextService getTestContextService() {
        return new TestContextService();
    }

}
