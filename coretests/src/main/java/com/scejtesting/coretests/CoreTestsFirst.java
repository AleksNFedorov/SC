package com.scejtesting.coretests;

/**
 * Created by ofedorov on 7/11/14.
 */
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

    public void throwException() throws Exception {
        throw new Exception("Thrown by purpose");
    }

}
