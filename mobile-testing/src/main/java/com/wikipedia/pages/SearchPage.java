package com.wikipedia.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {

    AndroidDriver driver;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/search_container")
    private WebElement searchBar;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/search_text_view")
    private WebElement searchInput;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/fragment_container")
    private List<WebElement> searchResults;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/search_close_btn")
    private WebElement clearButton;

    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void tapSearchBar() {
        searchBar.click();
    }

    public void enterSearchTerm(String term) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Dismiss "Try out your stylus" dialog if it appears
        try {
            WebElement cancel = driver.findElement(By.xpath("//*[@text='Cancel']"));
            cancel.click();
            Thread.sleep(1000);
        } catch (Exception ignored) {}

        // Also try dismissing via Back button if dialog is still there
        try {
            WebElement cancel = driver.findElement(
                    By.xpath("//android.widget.Button[@text='Cancel']"));
            cancel.click();
            Thread.sleep(500);
        } catch (Exception ignored) {}

        // Now find the search input and type
        WebElement input = null;
        String[] possibleIds = {
                "org.wikipedia.alpha:id/search_src_text",
                "org.wikipedia.alpha:id/search_text_view",
                "org.wikipedia.alpha:id/cabSearchEditText"
        };

        for (String id : possibleIds) {
            try {
                input = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
                break;
            } catch (Exception ignored) {}
        }

        if (input == null) {
            input = wait.until(ExpectedConditions.elementToBeClickable(
                    By.className("android.widget.EditText")));
        }

        input.clear();
        input.sendKeys(term);
    }
    public int getResultsCount() {
        return searchResults.size();
    }

    public void tapFirstResult() {
        searchResults.get(0).click();
    }

    public String getFirstResultTitle() {
        return searchResults.get(0).getText();
    }

    public void clearSearch() {
        clearButton.click();
    }
}
