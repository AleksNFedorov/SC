package com.scejtesting.selenium.concordion.extension;

import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.CoreWebTestFixture;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.concordion.api.listener.ThrowableCaughtListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 8/2/14.
 */
public class WebTestExceptionListener implements ThrowableCaughtListener {

    private static Logger LOG = LoggerFactory.getLogger(WebTestExceptionListener.class);

    private final CoreWebTestFixture testFixture = new CoreWebTestFixture();

    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        LOG.debug("method invoked [{}]", event);

        if (testStopExceptionExist()) {
            LOG.info("Test stop exception found, closing web driver if any");
            try {
                if (testFixture.getCurrentDriver() != null) {
                    testFixture.closeCurrentDriver();
                    LOG.info("Current driver has been closed successfully");
                }
            } catch (RuntimeException rx) {
                LOG.warn("Get current driver exception [{}]", rx.getMessage());
            }
        }
        LOG.debug("method finished");
    }

    private boolean testStopExceptionExist() {
        return getSuite().getThrownException() != null || getCurrentTest().getThrownException() != null;
    }

    protected Suite getSuite() {
        return SuiteConfiguration.getInstance().getSuite();
    }

    // No need to use predefined text context, test is always the same
    protected Test getCurrentTest() {
        return new TestContextService().getCurrentTestContext().getTest();
    }

}
