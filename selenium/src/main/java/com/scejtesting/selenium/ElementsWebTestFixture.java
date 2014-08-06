package com.scejtesting.selenium;

import com.google.common.base.Strings;
import com.scejtesting.selenium.elements.Link;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 8/5/14.
 */
public class ElementsWebTestFixture extends CoreWebTestFixture {

    protected final static Logger LOG = LoggerFactory.getLogger(ElementsWebTestFixture.class);

    public Link getLinkBy(By linkBy) {
        LOG.debug("method invoked [{}]", linkBy);

        Check.notNull(linkBy, "By predicate must be specified");

        WebElement linkElement = findElement(linkBy);

        Check.notNull(linkElement, "Link element not found");

        String linkHref = Strings.nullToEmpty(linkElement.getAttribute("href"));
        String linkText = linkElement.getText();
        String linkClass = Strings.nullToEmpty(linkElement.getAttribute("class"));
        String linkId = Strings.nullToEmpty(linkElement.getAttribute("id"));
        String tagName = linkElement.getTagName();
        YesNo displayed = YesNo.get(linkElement.isDisplayed());

        Link link = new Link(linkId, linkClass, displayed, tagName, linkText, linkHref);

        LOG.info("Link for [{}] created [{}]", linkBy, link);

        LOG.debug("method finished");

        return link;
    }


}
