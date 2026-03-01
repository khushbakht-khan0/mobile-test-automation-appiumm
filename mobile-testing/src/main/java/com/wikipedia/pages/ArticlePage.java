package com.wikipedia.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ArticlePage {

    AndroidDriver driver;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/articleTitle")
    private WebElement articleTitle;

    @AndroidFindBy(accessibility = "org.wikipedia.alpha:id/page_save")
    private WebElement saveButton;

    public ArticlePage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public String getArticleTitle() {
        return articleTitle.getText();
    }

    public void saveArticle() {
        saveButton.click();
    }
}