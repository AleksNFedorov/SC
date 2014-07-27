package com.scejtesting.coretests;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.coretests.exceptions.DerivedTestException;
import com.scejtesting.coretests.exceptions.SuiteException;
import com.scejtesting.coretests.exceptions.TestException;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ofedorov on 7/11/14.
 */
@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class CoreTestsFirst2 {

    private static final Logger LOG = LoggerFactory.getLogger(CoreTestsFirst2.class);


    public String getSame(String content) {
        return content;
    }

    public String getWorks() {
        return "works";
    }

    public boolean getTrue() {
        return Boolean.TRUE;
    }

    public boolean getFalse() {
        return Boolean.FALSE;
    }

    public String getExtraValue() {
        return "extraValue";
    }

    public void runLongTask(Integer value) throws InterruptedException {
        TimeUnit.SECONDS.sleep(value);
    }

    public void throwTestException() throws TestException {
        throw new TestException();
    }

    public void throwSuiteException() throws SuiteException {
        throw new SuiteException();
    }

    public void longRunningTask(Integer waitTime) throws InterruptedException {
        LOG.debug("method invoked [{}]", waitTime);
        TimeUnit.SECONDS.sleep(waitTime);
        LOG.debug("method finished ");
    }

    public void throwDerivedTestException() throws DerivedTestException {
        throw new DerivedTestException();
    }

    public void throwUnsupportedOperationException() {
        throw new UnsupportedOperationException();
    }

    public List<Integer> getList(Integer count) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < count; ++i) {
            list.add(i);
        }
        return list;
    }

    public void throwException() throws Exception {
        throw new Exception("Thrown by purpose");
    }

}
