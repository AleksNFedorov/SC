package exceptions;

import com.scejtesting.selenium.webdriver.RemoteWebDriverFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 1/4/14.
 */
public class ScreenShootBuilder {

    public static final String VIEWPORT_WIDTH = "var w=window.innerWidth\n" +
            "|| document.documentElement.clientWidth\n" +
            "|| document.body.clientWidth; return w;";

    public static final String VIEWPORT_HEIGHT = "var w=window.innerHeight\n" +
            "|| document.documentElement.clientHeight\n" +
            "|| document.body.clientHeight; return w;";


    public static final String PAGE_HEIGHT = "    var D = document;\n" +
            "    return Math.max(\n" +
            "        D.body.scrollHeight, D.documentElement.scrollHeight,\n" +
            "        D.body.offsetHeight, D.documentElement.offsetHeight,\n" +
            "        D.body.clientHeight, D.documentElement.clientHeight\n" +
            "    );";


    public static final String PAGE_WIDTH = "    var D = document;\n" +
            "    return Math.max(\n" +
            "        D.body.scrollWidth, D.documentElement.scrollWidth,\n" +
            "        D.body.offsetWidth, D.documentElement.offsetWidth,\n" +
            "        D.body.clientWidth, D.documentElement.clientWidth\n" +
            "    );";

    public static final String PAGE_OFFSET_X = "var element = document.body;var position = element.getBoundingClientRect();return position.left";
    public static final String PAGE_OFFSET_Y = "var element = document.body;var position = element.getBoundingClientRect();return position.top";


    {
        System.setProperty("webdriver.chrome.driver", "/Users/macbookair/Projects/scejtesting/chrome/bin/chromedriver");

    }

    private final RemoteWebDriverFactory factory = new RemoteWebDriverFactory();

    private RemoteWebDriver chromeDriver = factory.buildRemoteWebDriver("chrome");

    public static void main(String... args) throws IOException, AWTException, InterruptedException {
        ScreenShootBuilder builder = new ScreenShootBuilder();
        builder.takeScreenShot();

        System.out.println("ViewPort width [" + builder.getBrowserViewportWidth() + "]");
        System.out.println("Viewport height [" + builder.getBrowserViewportHeight() + "]");

        System.out.println("Page height [" + builder.getBrowserPageHeight() + "]");
        System.out.println("Page height [" + builder.getBrowserViewportWidth() + "]");
        System.out.println("Page offset x [" + builder.getPageOffsetX() + "]");
        System.out.println("Page offset Y [" + builder.getPageOffsetY() + "]");


        System.exit(0);


    }

    public void takeScreenShot() throws AWTException, IOException, InterruptedException {

        chromeDriver.get("http://yandex.ru");

        WebDriver.Window window = chromeDriver.manage().window();

        Point position = window.getPosition();
        Dimension dimension = window.getSize();

        Robot robot = new Robot();

        List<WebElement> element = chromeDriver.findElementsByTagName("body");

        Actions mouseActions = new Actions(chromeDriver);

        mouseActions.moveByOffset(-50, -50).perform();

        TimeUnit.MILLISECONDS.sleep(100);

//        chromeDriver.getMouse().mouseMove(((Locatable)element.get(0)).getCoordinates());


        TimeUnit.SECONDS.sleep(10);

        java.awt.Point mouseLocation = MouseInfo.getPointerInfo().getLocation();


        Rectangle screenSize = new Rectangle(mouseLocation.getLocation().x, mouseLocation.getLocation().y, getBrowserViewportWidth(), getBrowserPageHeight());

        BufferedImage imageStream = robot.createScreenCapture(screenSize);

        File screenFile = new File("/Users/macbookair/Projects/scejtesting/test/results/" + System.currentTimeMillis() + ".png");

        ImageIO.write(imageStream, "png", screenFile);

    }

    public int getBrowserViewportWidth() {

        Object result = chromeDriver.executeScript(VIEWPORT_WIDTH);

        return Integer.parseInt(result.toString());
    }

    public int getBrowserViewportHeight() {
        Object result = chromeDriver.executeScript(VIEWPORT_HEIGHT);

        return Integer.parseInt(result.toString());

    }

    public int getBrowserPageHeight() {
        Object result = chromeDriver.executeScript(PAGE_HEIGHT);

        return Integer.parseInt(result.toString());
    }

    public int getBrowserPageWidth() {
        Object result = chromeDriver.executeScript(PAGE_WIDTH);

        return Integer.parseInt(result.toString());
    }

    public int getPageOffsetX() {
        Object result = chromeDriver.executeScript(PAGE_OFFSET_X);

        return Integer.parseInt(result.toString());
    }

    public int getPageOffsetY() {
        Object result = chromeDriver.executeScript(PAGE_OFFSET_Y);

        return Integer.parseInt(result.toString());
    }


}
