package com.scejtesting.selenium.concordion.extension;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.selenium.concordion.extension.command.AbstractSeleniumDriverCommand;
import com.scejtesting.selenium.concordion.extension.command.CloseDriverCommand;
import com.scejtesting.selenium.concordion.extension.command.CreateAndOpenDriverCommand;
import com.scejtesting.selenium.concordion.extension.command.WaitCommand;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.internal.listener.AssertResultRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aleks on 5/3/14.
 */
public class ScejSeleniumExtension extends ScejCoreExtensions {

    private final AssertResultRenderer assertRenderer = new AssertResultRenderer();

    private final List<AbstractSeleniumDriverCommand> allCommands = new LinkedList<AbstractSeleniumDriverCommand>() {
        {
            add(new CloseDriverCommand(assertRenderer));
            add(new CreateAndOpenDriverCommand(assertRenderer));
            add(new WaitCommand(assertRenderer));
            add(new CheckTextOnPage(assertRenderer));
        }
    };


    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        super.addTo(concordionExtender);
        addCommands(concordionExtender);

    }

    private void addCommands(ConcordionExtender extender) {
        for (AbstractSeleniumDriverCommand command : allCommands) {
            extender.withCommand(ScejCommand.SCEJ_TESTING_NAME_SPACE, command.getCommandName(), command);
        }
    }
}
