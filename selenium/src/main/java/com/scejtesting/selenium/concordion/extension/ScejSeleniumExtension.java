package com.scejtesting.selenium.concordion.extension;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.selenium.concordion.extension.command.CloseDriver;
import com.scejtesting.selenium.concordion.extension.command.OpenDriver;
import com.scejtesting.selenium.concordion.extension.command.WaitCommand;
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
        }
    };


    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        super.addTo(concordionExtender);
        addCommands(concordionExtender);
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
