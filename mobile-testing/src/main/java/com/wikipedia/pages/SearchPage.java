package com.wikipedia.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class SearchPage {

    AndroidDriver driver;

    @AndroidFindBy(id = "org.wikipedia:id/search_container")
    private WebElement searchBar;

    @AndroidFindBy(id = "org.wikipedia:id/search_src_text")
    private WebElement searchInput;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_title")
    private List<WebElement> searchResults;

    @AndroidFindBy(id = "org.wikipedia:id/search_close_btn")
    private WebElement clearButton;

    public SearchPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void tapSearchBar() {
        searchBar.click();
    }

    public void enterSearchTerm(String term) {
        searchInput.sendKeys(term);
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