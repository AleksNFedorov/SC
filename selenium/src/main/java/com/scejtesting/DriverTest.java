package com.scejtesting;

import com.scejtesting.selenium.webdriver.RemoteWebDriverBuilderServiceRegistry;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 27/3/14.
 */
public class DriverTest {


    /*
        @BeforeClass
        public static void init() throws IOException {

            driverService = new ChromeDriverService.Builder().
                    usingDriverExecutable(new File("/Users/macbookair/Projects/scejtesting/selenium/bin/chromedriver")).
                    usingAnyFreePort().build();
            driverService.start();

            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    onFinish();
                    System.out.println("Shutdown hook invoked");
                }
            });
        }

        public static void onFinish() {
            driverService.stop();
        }


        @Test
        public void testDriver() throws MalformedURLException {

            WebDriver driver = new RemoteWebDriver(driverService.getUrl(), DesiredCapabilities.chrome());
            driver.get("http://www.google.com");

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.quit();


        }
    */
    @Test
    public void launchChromeDriver() {

        RemoteWebDriverBuilderServiceRegistry factory = new RemoteWebDriverBuilderServiceRegistry();


        RemoteWebDriver chromeDriver = factory.buildRemoteWebDriver("chrome");

        chromeDriver.get("http://yandex.ru");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
