package com.wikipedia.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ArticlePage {

    AndroidDriver driver;

    // Corrected ID for the main article title in the Alpha/Beta app
    @AndroidFindBy(id = "org.wikipedia.alpha:id/view_page_title_text")
    private WebElement articleTitle;

    // Fixed: accessibility logic usually looks for the "content-desc"
    // If you want to use the ID, use @AndroidFindBy(id = "...")
    @AndroidFindBy(accessibility = "Add this article to a reading list")
    private WebElement saveButton;

    public ArticlePage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void dismissPopups() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        // 1. Dismiss "Got it" tooltips
        try {
            WebElement gotIt = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@text='GOT IT' or @text='Got it']")));
            gotIt.click();
        } catch (Exception ignored) {}

        // 2. Dismiss Games or News dialogs via Close button
        try {
            WebElement closeBtn = driver.findElement(By.id("org.wikipedia.alpha:id/closeButton"));
            closeBtn.click();
        } catch (Exception ignored) {}
    }

    public String getArticleTitle() {
        dismissPopups();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(articleTitle));
            return articleTitle.getText();
        } catch (Exception e) {
            // Fallback: Sometimes the title is only findable via Xpath in the webview container
            return driver.findElement(By.xpath("//android.view.View[@index='0']/android.widget.TextView")).getText();
        }
    }

    public void saveArticle() {
        dismissPopups();
        try {
            waitAndClick(saveButton);
        } catch (Exception e) {
            // Fallback to the ID if accessibility fails
            driver.findElement(By.id("org.wikipedia.alpha:id/page_save")).click();
        }
    }

    private void waitAndClick(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }
}