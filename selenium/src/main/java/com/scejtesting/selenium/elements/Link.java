package com.scejtesting.selenium.elements;

import com.scejtesting.selenium.CoreWebTestFixture;

/**
 * Created by aleks on 8/5/14.
 */
public class Link extends BaseElement {

    public final String text;
    public final String href;

    public Link(String id, String clazz, CoreWebTestFixture.YesNo displayed, String tagName, String text, String href) {
        super(id, clazz, displayed, tagName);
        this.text = text;
        this.href = href;
    }

    @Override
    public String toString() {
        return "Link{" +
                "super='" + super.toString() + '\'' +
                "text='" + text + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
