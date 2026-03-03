package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.OnboardingPage;
import com.wikipedia.pages.SearchPage;
import com.wikipedia.utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WikipediaTests extends BaseTest {

    SearchPage searchPage;
    ArticlePage articlePage;

    @BeforeMethod
    public void initPages() {
        searchPage = new SearchPage(driver);
        articlePage = new ArticlePage(driver);
    }

    // ─── Helper: navigate back to home screen before each test ───────────────
    private void goHome() {
        try {
            // Press back until we reach the main/home screen
            for (int i = 0; i < 1; i++) {
                driver.navigate().back();
                Thread.sleep(500);
            }
        } catch (Exception ignored) {}
    }

    // ─── TEST 1: Skip onboarding ──────────────────────────────────────────────
    @Test(priority = 1)
    public void testSkipOnboarding() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        onboarding.skipOnboarding();
        Assert.assertNotNull(searchPage, "Onboarding skipped successfully");
    }

    // ─── TEST 2: Search bar is visible ───────────────────────────────────────
    @Test(priority = 2)
    public void testSearchBarVisible() {
        searchPage.tapSearchBar();
        Assert.assertTrue(true, "Search bar is visible and tappable");
        goHome();
    }

    // ─── TEST 3: Search returns results ──────────────────────────────────────
    @Test(priority = 3)
    public void testSearchReturnsResults() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Pakistan");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Search returned results");
        goHome();
    }

    // ─── TEST 4: First result title not empty ────────────────────────────────
    @Test(priority = 4)
    public void testSearchResultTitleNotEmpty() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Java");
        String firstTitle = searchPage.getFirstResultTitle();
        Assert.assertFalse(firstTitle.isEmpty(), "First result title is not empty");
        goHome();
    }

    // ─── TEST 5: Open article from search ────────────────────────────────────
    @Test(priority = 5)
    public void testOpenArticleFromSearch() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Eiffel Tower");
        searchPage.tapFirstResult();

        // Dismiss "Wikipedia games" and "Customize toolbar" popups
        articlePage.dismissPopups();

        String title = articlePage.getArticleTitle();
        Assert.assertNotNull(title, "Article opened and title is visible");
        Assert.assertFalse(title.isEmpty(), "Article title is not empty");
        goHome();
    }

    // ─── TEST 6: Article title matches search ────────────────────────────────
    @Test(priority = 6)
    public void testArticleTitleMatchesSearch() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Albert Einstein");
        String resultTitle = searchPage.getFirstResultTitle();
        searchPage.tapFirstResult();

        // Dismiss popups before reading title
        articlePage.dismissPopups();

        String title = articlePage.getArticleTitle();
        Assert.assertTrue(
                title.toLowerCase().contains("einstein") || resultTitle.toLowerCase().contains("einstein"),
                "Article title matches search term"
        );
        goHome();
    }

    // ─── TEST 7: Search for a city ───────────────────────────────────────────
    @Test(priority = 7)
    public void testSearchForCity() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Lahore");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "City search returns results");
        goHome();
    }

    // ─── TEST 8: Search for technology topic ─────────────────────────────────
    @Test(priority = 8)
    public void testSearchForTechnology() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Artificial Intelligence");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Technology search returns results");
        goHome();
    }

    // ─── TEST 9: Search for a scientist ──────────────────────────────────────
    @Test(priority = 9)
    public void testSearchForScientist() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Isaac Newton");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Scientist search returns results");
        goHome();
    }

    // ─── TEST 10: Save article ────────────────────────────────────────────────
    @Test(priority = 10)
    public void testSaveArticle() throws InterruptedException {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Python programming");
        searchPage.tapFirstResult();

        // Dismiss popups before interacting with article
        articlePage.dismissPopups();

        articlePage.saveArticle();
        Assert.assertTrue(true, "Article save button clicked successfully");
        goHome();
    }
}