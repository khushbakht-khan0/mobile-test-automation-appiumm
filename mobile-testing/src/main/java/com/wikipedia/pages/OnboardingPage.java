package com.wikipedia.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class OnboardingPage {

    AndroidDriver driver;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/fragment_onboarding_skip_button")
    private WebElement skipButton;

    public OnboardingPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void skipOnboarding() {
        skipButton.click();
    }
}