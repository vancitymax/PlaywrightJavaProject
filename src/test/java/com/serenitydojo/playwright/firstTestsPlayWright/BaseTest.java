package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    protected Page page;

    @BeforeAll
    static void setUpBrowser(){
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().
                setHeadless(false)
                        .setSlowMo(2)
                .setArgs(Arrays.asList(
                        "--no-sandbox",
                        "--disable-extensions",
                        "--disable-gpu"
                )));

    }
    @BeforeEach
    void setUp(){
        browserContext = browser.newContext();
        page = browserContext.newPage();
        page.navigate("https://practicesoftwaretesting.com");
    }
    @AfterEach
    void closePage() {
       if(browserContext != null) browserContext.close();
    }
    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }


}
