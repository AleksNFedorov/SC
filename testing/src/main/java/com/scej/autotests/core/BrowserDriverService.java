package com.scej.autotests.core;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 12/2/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserDriverService {

    public static final String CHROME_PATH_TO_CHROME_DRIVER_PROPERTY = "path.to.chromeDriver";

    public static final String FIRE_FOX_PATH_TO_FIRE_FOX_PROPERTY = "path.to.firefox";
    public static final String FIRE_FOXPATH_TO_FIRE_FOX_DOWNLOAD_FOLDER_PROPERTY = "path.to.firefox.download.folder";

    public static final String INTERNET_EXPLORER_PATH_TO_DRIVER = "path.to.internetExplorer";

    private ChromeDriverService chromeDriverService;


    private static final BrowserDriverService instance = new BrowserDriverService();

    public static final BrowserDriverService getBrowserDriverService() {
        return instance;
    }

    private BrowserDriverService() {
    }

    public RemoteWebDriver createDriver(Locators.Browser browser) throws Exception {
        switch (browser) {
            case Chrome:
                return createChromeDriver();
            case FireFox:
                return createFireFoxDriver();
            case InternetExplorer:
                return createInternetExplorerDriver();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void stopDriver(RemoteWebDriver driver) {
        if(driver instanceof ChromeDriver) {
                if(chromeDriverService != null && chromeDriverService.isRunning())
                    chromeDriverService.stop();
        }
    }

    private RemoteWebDriver createChromeDriver() throws IOException {

        String systemSpecificChromeDriver = Locators.getApplicationProperty(CHROME_PATH_TO_CHROME_DRIVER_PROPERTY);

        System.setProperty("webdriver.chrome.driver", systemSpecificChromeDriver);
        chromeDriverService = ChromeDriverService.createDefaultService();
        chromeDriverService.start();
        ChromeDriver driver = new ChromeDriver(chromeDriverService);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        return driver;
    }

    private RemoteWebDriver createFireFoxDriver() {
        String systemSpecificFireFoxInstallation = Locators.getApplicationProperty(FIRE_FOX_PATH_TO_FIRE_FOX_PROPERTY);
        String systemSpecificFireFoxDownloadFolder = Locators.getApplicationProperty(FIRE_FOXPATH_TO_FIRE_FOX_DOWNLOAD_FOLDER_PROPERTY);

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.dir", systemSpecificFireFoxDownloadFolder);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv, text/xml");

        FirefoxDriver driver = new FirefoxDriver(new FirefoxBinary(new File(systemSpecificFireFoxInstallation)), profile);

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        driver.manage().window().maximize();
        return driver;
    }

    private RemoteWebDriver createInternetExplorerDriver() {

        String pathToIEDriverServer = Locators.getApplicationProperty(INTERNET_EXPLORER_PATH_TO_DRIVER);

        System.setProperty("webdriver.ie.driver", pathToIEDriverServer);

        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                true);

        InternetExplorerDriver driverIE = new InternetExplorerDriver();
        driverIE.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driverIE.manage().window().maximize();
        return driverIE;
    }

    public static void main(String ... args) throws IOException, InterruptedException {
        RemoteWebDriver chromeDriver = new BrowserDriverService().createChromeDriver();
        chromeDriver.get("http://mail.ru/");
        chromeDriver.findElementByXPath("//span[text()='ТВ программа']").click();
        Thread.sleep(10000);
    }
}
