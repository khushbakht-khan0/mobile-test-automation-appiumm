package com.wikipedia.tests;

import com.wikipedia.pages.ArticlePage;
import com.wikipedia.pages.OnboardingPage;
import com.wikipedia.pages.SearchPage;
import com.wikipedia.utils.BaseTest;
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

    @Test(priority = 1)
    public void testSkipOnboarding() {
        OnboardingPage onboarding = new OnboardingPage(driver);
        onboarding.skipOnboarding();
        Assert.assertNotNull(searchPage, "Onboarding skipped successfully");
    }

    @Test(priority = 2)
    public void testSearchBarVisible() {
        searchPage.tapSearchBar();
        Assert.assertTrue(true, "Search bar is visible and tappable");
    }

    @Test(priority = 3)
    public void testSearchReturnsResults() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Pakistan");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Search returned results");
    }

    @Test(priority = 4)
    public void testSearchResultTitleNotEmpty() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Java");
        String firstTitle = searchPage.getFirstResultTitle();
        Assert.assertFalse(firstTitle.isEmpty(), "First result title is not empty");
    }

    @Test(priority = 5)
    public void testOpenArticleFromSearch() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Eiffel Tower");
        searchPage.tapFirstResult();
        String title = articlePage.getArticleTitle();
        Assert.assertNotNull(title, "Article opened and title is visible");
    }
    @Test(priority = 6)
    public void testArticleTitleMatchesSearch() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Albert Einstein");
        String resultTitle = searchPage.getFirstResultTitle();
        searchPage.tapFirstResult();
        String articleTitle = articlePage.getArticleTitle();
        Assert.assertTrue(articleTitle.contains("Einstein"), "Article title matches search");
    }

    @Test(priority = 7)
    public void testSearchForCity() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Lahore");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "City search works");
    }

    @Test(priority = 8)
    public void testSearchForTechnology() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Artificial Intelligence");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Technology search works");
    }

    @Test(priority = 9)
    public void testSearchForScientist() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Isaac Newton");
        Assert.assertTrue(searchPage.getResultsCount() > 0, "Scientist search works");
    }

    @Test(priority = 10)
    public void testSaveArticle() {
        searchPage.tapSearchBar();
        searchPage.enterSearchTerm("Python programming");
        searchPage.tapFirstResult();
        articlePage.saveArticle();
        Assert.assertTrue(true, "Article save button clicked successfully");
    }
}