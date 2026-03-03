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
import java.util.List;

public class SearchPage {

    AndroidDriver driver;

    @AndroidFindBy(id = "org.wikipedia.alpha:id/search_container")
    private WebElement searchBar;

    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void tapSearchBar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("org.wikipedia.alpha:id/search_container"))).click();
    }

    public void enterSearchTerm(String term) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Dismiss stylus dialog if it appears
        try {
            WebElement cancel = driver.findElement(By.xpath("//*[@text='Cancel']"));
            cancel.click();
            Thread.sleep(1000);
        } catch (Exception ignored) {}

        // Find search input - try multiple possible IDs
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

        // Fallback to any EditText
        if (input == null) {
            input = wait.until(ExpectedConditions.elementToBeClickable(
                    By.className("android.widget.EditText")));
        }

        input.clear();
        input.sendKeys(term);

        // Wait for results to load
        Thread.sleep(2000);
    }

    /**
     * Gets search results using multiple strategies since
     * Wikipedia alpha uses Jetpack Compose (no standard resource IDs on result items)
     */
    private List<WebElement> getResultElements() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Strategy 1: standard resource id (older APK versions)
        try {
            List<WebElement> results = driver.findElements(
                    By.id("org.wikipedia.alpha:id/page_list_item_title"));
            if (!results.isEmpty()) return results;
        } catch (Exception ignored) {}

        // Strategy 2: Compose-based results - find by class android.view.View inside search results
        try {
            List<WebElement> results = driver.findElements(
                    By.xpath("//androidx.compose.ui.platform.ComposeView//android.view.View[@clickable='true']"));
            if (!results.isEmpty()) return results;
        } catch (Exception ignored) {}

        // Strategy 3: any clickable view inside the search result list container
        try {
            List<WebElement> results = driver.findElements(
                    By.xpath("//*[@resource-id='org.wikipedia.alpha:id/search_results_list']//*[@clickable='true']"));
            if (!results.isEmpty()) return results;
        } catch (Exception ignored) {}

        // Strategy 4: broader - all clickable android.view.View elements (Compose rendered)
        try {
            List<WebElement> results = driver.findElements(
                    By.xpath("//android.view.View[@clickable='true']"));
            if (!results.isEmpty()) return results;
        } catch (Exception ignored) {}

        // Strategy 5: fallback - RecyclerView children
        try {
            List<WebElement> results = driver.findElements(
                    By.xpath("//androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout"));
            if (!results.isEmpty()) return results;
        } catch (Exception ignored) {}

        return driver.findElements(By.className("android.view.View"));
    }

    public int getResultsCount() {
        try {
            List<WebElement> results = getResultElements();
            return results.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void tapFirstResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        List<WebElement> results = getResultElements();
        if (!results.isEmpty()) {
            results.get(0).click();
        } else {
            throw new RuntimeException("No search results found to tap");
        }
    }

    public String getFirstResultTitle() {
        List<WebElement> results = getResultElements();
        if (!results.isEmpty()) {
            String text = results.get(0).getText();
            // getText() on Compose views sometimes returns empty - try attribute
            if (text == null || text.isEmpty()) {
                text = results.get(0).getAttribute("content-desc");
            }
            return text != null ? text : "";
        }
        return "";
    }

    public void clearSearch() {
        try {
            driver.findElement(
                    By.id("org.wikipedia.alpha:id/search_close_btn")).click();
        } catch (Exception ignored) {}
    }
}