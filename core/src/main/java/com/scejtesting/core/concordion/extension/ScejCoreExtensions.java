package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.command.RegisterGlobalVariablesCommand;
import com.scejtesting.core.concordion.command.SetGlobalVariableCommand;
import com.scejtesting.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scejtesting.core.runner.ScejSuiteRunner;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

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

        RegisterGlobalVariablesCommand registerGlobalVariablesCommand = new RegisterGlobalVariablesCommand();
        SetGlobalVariableCommand setGlobalVariables = new SetGlobalVariableCommand();

        concordionExtender.withCommand(ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE,
                registerGlobalVariablesCommand.getCommandType().getSpecificationCommand(),
                registerGlobalVariablesCommand);

        concordionExtender.withCommand(ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE,
                setGlobalVariables.getCommandType().getSpecificationCommand(),
                setGlobalVariables);

    }
}
