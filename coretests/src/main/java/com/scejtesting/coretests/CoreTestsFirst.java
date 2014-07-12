package com.scejtesting.coretests;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofedorov on 7/11/14.
 */
@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class CoreTestsFirst {

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
