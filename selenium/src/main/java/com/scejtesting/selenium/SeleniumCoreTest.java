package com.scejtesting.selenium;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.util.Check;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 27/3/14.
 */

@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class SeleniumCoreTest {

    private final SeleniumDriverManagerService driverManagerService = buildDriverManagerService();

    public final RemoteWebDriver openDriver(String driverName) {
        return driverManagerService.openDriver(driverName);
    }

    public RemoteWebDriver getCurrentDriver() {
        RemoteWebDriver currentDriver = driverManagerService.getCurrentDriver();
        Check.notNull(currentDriver, "Current driver is not defined");
        return currentDriver;
    }

    public boolean closeCurrentDriver() {
        driverManagerService.closeCurrentDriver();
        return true;
    }

    public boolean waitSeconds(String seconds) {
        try {
            TimeUnit.SECONDS.sleep(Long.parseLong(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected SeleniumDriverManagerService buildDriverManagerService() {
        return new SeleniumDriverManagerService();
    }

}
