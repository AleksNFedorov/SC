package com.scejtesting.selenium.elements;

import com.scejtesting.selenium.CoreWebTestFixture;

/**
 * Created by aleks on 8/5/14.
 */
public abstract class BaseElement {
    public final String id;
    public final String clazz;
    public final String displayed;
    public final String tagName;


    protected BaseElement(String id, String clazz, CoreWebTestFixture.YesNo displayed, String tagName) {
        this.id = id;
        this.clazz = clazz;
        this.displayed = displayed.name();
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "BaseElement{" +
                "id='" + id + '\'' +
                ", clazz='" + clazz + '\'' +
                ", displayed='" + displayed + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
