package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.*;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsThumbRendererProcessingListener;
import com.scejtesting.core.concordion.extension.specificationprocessing.VelocityResultsRenderer;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejCoreExtensions implements ConcordionExtension {


    @Override
    public void addTo(ConcordionExtender concordionExtender) {
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

    private void onExtensionInitialized() {
        new TestContextService().setTestContextInitialized();
    }
}
