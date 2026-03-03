package com.wikipedia.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    public AndroidDriver driver;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setPlatformName("Android");
        options.setAppPackage("org.wikipedia.alpha");
        options.setAppActivity("org.wikipedia.main.MainActivity");
        options.setNoReset(true);
        options.setAutoGrantPermissions(true);
        options.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        options.setCapability("appium:adbExecTimeout", 60000);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Before every test method:
     * 1. Terminates the Wikipedia app
     * 2. Relaunches it fresh from MainActivity
     * This ensures every test starts from the home screen.
     */
    @BeforeMethod
    public void resetApp() {
        try {
            // Terminate the app
            driver.terminateApp("org.wikipedia.alpha");
            Thread.sleep(1000);

            // Reactivate the app
            driver.activateApp("org.wikipedia.alpha");
            Thread.sleep(2000);

            // Dismiss onboarding skip button if it appears
            try {
                WebElement skip = driver.findElement(
                        By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button"));
                skip.click();
                Thread.sleep(500);
            } catch (Exception ignored) {}

            // Dismiss any "Got it" tooltip on home screen
            try {
                WebElement gotIt = driver.findElement(By.xpath("//*[@text='Got it']"));
                gotIt.click();
                Thread.sleep(500);
            } catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("resetApp() warning: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}