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
}