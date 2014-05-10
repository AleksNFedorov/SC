package com.scejtesting.selenium.concordion.extension.command;

import com.scejtesting.selenium.WebTestFixture;
import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class CommandsTest {

    private MonitoringAssertListener listener;

    @Before
    public void init() {
        listener = new MonitoringAssertListener();
    }

    @After
    public void finishTest() {
        listener = null;
    }


    @Test
    public void setValueToElementTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractSeleniumDriverCommand command = spy(new SetValueToElement(listener));

        doReturn(fixture).when(command).getTestFixture();


        doNothing().when(fixture).setValueToElement(any(By.class), anyString());
        command.processDriverCommand(Arrays.asList(By.id("someId"), "someText"), new Element("div"));
        verify(fixture, times(1)).setValueToElement(any(By.class), anyString());

        doNothing().when(fixture).setValueToElement(any(WebElement.class), anyString());
        command.processDriverCommand(Arrays.asList(mock(WebElement.class), "someText"), new Element("div"));
        verify(fixture, times(1)).setValueToElement(any(WebElement.class), anyString());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(By.id("someId")), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(By.id("someId"), new Object()), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(new Object(), "string"), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }
    }


    @Test
    public void clearElementTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractSeleniumDriverCommand command = spy(new ClearElement(listener));

        doReturn(fixture).when(command).getTestFixture();


        doNothing().when(fixture).clearElement(any(By.class));
        command.processDriverCommand(By.id("someID"), new Element("div"));
        verify(fixture, times(1)).clearElement(any(By.class));

        doNothing().when(fixture).clearElement(any(WebElement.class));
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        verify(fixture, times(1)).clearElement(any(WebElement.class));


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }


    }


    @Test
    public void clickElementTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractSeleniumDriverCommand command = spy(new ClickElement(listener));

        doReturn(fixture).when(command).getTestFixture();


        doNothing().when(fixture).clickElement(any(By.class));
        command.processDriverCommand(By.id("someID"), new Element("div"));
        verify(fixture, times(1)).clickElement(any(By.class));

        doNothing().when(fixture).clickElement(any(WebElement.class));
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        verify(fixture, times(1)).clickElement(any(WebElement.class));


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }


    }

    @Test
    public void checkElementContainsTextTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractSeleniumDriverCommand command = spy(new CheckElementContainsText(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementContainsText(any(By.class), anyString())).thenReturn(Boolean.TRUE);
        command.processDriverCommand(Arrays.asList(By.id("someId"), "someText"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());

        when(fixture.checkElementContainsText(any(WebElement.class), anyString())).thenReturn(Boolean.TRUE);
        command.processDriverCommand(Arrays.asList(mock(WebElement.class), "someText"), new Element("div"));
        Assert.assertEquals(2, listener.getSuccessCount());


        when(fixture.checkElementContainsText(any(By.class), anyString())).thenReturn(Boolean.FALSE);
        command.processDriverCommand(Arrays.asList(By.id("someId"), "someText"), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(By.id("someId")), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(By.id("someId"), new Object()), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(Arrays.asList(new Object(), "string"), new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(2, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());
    }

    @Test
    public void checkElementExist() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractCheckStateCommand command = spy(new CheckElementDisplayed(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementDisplayed(any(By.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkElementDisplayed(any(By.class))).thenReturn(Boolean.FALSE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }


    @Test
    public void checkElementDisplayed() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractCheckStateCommand command = spy(new CheckElementDisplayed(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementDisplayed(any(By.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkElementDisplayed(any(WebElement.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(2, listener.getSuccessCount());


        when(fixture.checkElementDisplayed(any(WebElement.class))).thenReturn(Boolean.FALSE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(2, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }


    @Test
    public void checkElementEnabled() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        AbstractCheckStateCommand command = spy(new CheckElementEnabled(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementEnabled(any(By.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkElementEnabled(any(WebElement.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(2, listener.getSuccessCount());


        when(fixture.checkElementEnabled(any(WebElement.class))).thenReturn(Boolean.FALSE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(2, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }


    @Test
    public void checkElementSelected() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        CheckElementSelected command = spy(new CheckElementSelected(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementSelected(any(By.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkElementSelected(any(WebElement.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(2, listener.getSuccessCount());


        when(fixture.checkElementSelected(any(WebElement.class))).thenReturn(Boolean.FALSE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(2, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }

    @Test
    public void checkTextOnPageCommandTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        CheckTextOnPage command = spy(new CheckTextOnPage(listener));

        doReturn(fixture).when(command).getTestFixture();

        when(fixture.checkTextOnPage(anyString())).thenReturn(Boolean.TRUE);
        command.processDriverCommand("someDriver", new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkTextOnPage(anyString())).thenReturn(Boolean.FALSE);
        command.processDriverCommand("someDriver", new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());

        when(fixture.checkTextOnPage(anyString())).thenThrow(RuntimeException.class);


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }

    @Test
    public void closeDriverCommand() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        CloseDriver command = spy(new CloseDriver(listener));

        doReturn(fixture).when(command).getTestFixture();

        command.processDriverCommand("someDriver", new Element("div"));

        Assert.assertEquals(1, listener.getSuccessCount());

        command.processDriverCommand(null, new Element("div"));

        Assert.assertEquals(2, listener.getSuccessCount());

        doThrow(RuntimeException.class).when(fixture).closeCurrentDriver();

        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }


        Assert.assertEquals(2, listener.getSuccessCount());

    }

    @Test
    public void openDriverCommandTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        OpenDriver command = spy(new OpenDriver(listener));

        doReturn(fixture).when(command).getTestFixture();

        command.processDriverCommand("someDriver", new Element("div"));

        Assert.assertEquals(1, listener.getSuccessCount());

        when(fixture.openDriver(anyString())).thenThrow(RuntimeException.class);

        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        try {
            command.processDriverCommand(null, new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());


    }

    private class MonitoringAssertListener implements AssertListener {

        private int successCount;
        private int failCount;

        @Override
        public void successReported(AssertSuccessEvent event) {
            successCount++;
        }

        @Override
        public void failureReported(AssertFailureEvent event) {
            failCount++;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailCount() {
            return failCount;
        }
    }


}
