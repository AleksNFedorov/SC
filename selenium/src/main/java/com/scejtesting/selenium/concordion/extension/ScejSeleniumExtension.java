package com.scejtesting.selenium.concordion.extension;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.selenium.concordion.extension.command.*;
import com.scejtesting.selenium.concordion.extension.screenshot.ScreenShotFacade;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.ext.ScreenshotExtension;
import org.concordion.internal.listener.AssertResultRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aleks on 5/3/14.
 */
public class ScejSeleniumExtension extends ScejCoreExtensions {

    private final AssertResultRenderer assertRenderer = new AssertResultRenderer();

    private final List<ScejCommand> allCommands = new LinkedList<ScejCommand>() {
        {
            add(new CloseDriver(assertRenderer));
            add(new OpenDriver(assertRenderer));
            add(new WaitCommand(assertRenderer));
            add(new SetValueToElement(assertRenderer));
            add(new ClickElement(assertRenderer));
            add(new ClearElement(assertRenderer));
            add(new CheckTextOnPage(assertRenderer));
            add(new CheckElementSelected(assertRenderer));
            add(new CheckElementExist(assertRenderer));
            add(new CheckElementEnabled(assertRenderer));
            add(new CheckElementDisplayed(assertRenderer));
            add(new CheckElementContainsText(assertRenderer));
            add(new CheckChildExist(assertRenderer));
        }
    };

    @Override
    protected void addToSafe(ConcordionExtender concordionExtender) {
        addScreenShotTaker(concordionExtender);
    }

    private void addScreenShotTaker(ConcordionExtender concordionExtender) {
        ScreenshotExtension screenshotExtension = new ScreenshotExtension();
        screenshotExtension.setScreenshotTaker(new ScreenShotFacade());
        screenshotExtension.addTo(concordionExtender);
    }

    @Override
    protected List<ScejCommand> getCommandsList() {
        allCommands.addAll(super.getCommandsList());
        return allCommands;
    }
}
