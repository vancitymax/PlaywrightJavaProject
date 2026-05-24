package com.serenitydojo.playwright.firstTestsPlayWright;

import org.junit.jupiter.api.*;

//@UsePlaywright(ASimplePlaywrightTest.Myoptions.class)
public class ASimplePlaywrightTest extends BaseTest {

//    public static class MyOptions implements OptionsFactory{
//
//        @Override
//        public Options getOptions() {
//            return new Options()
//                    .setHeadless(false)
//                    .setLaunchOptions(
//                            new BrowserType.LaunchOptions()
//                                    .setArgs(Arrays.asList("--no-sandbox","--disable-extentions","--disable-gpu"))
//                    );
//        }
//    }

    @Test
    public void shouldShowThePageTitle(){
           String title = page.title();
           Assertions.assertTrue(title.contains("Practice Software Testing"));


    }

    @Test
    public void searchByKeyword(){
            page.locator("[placeholder=Search]").fill("Pliers");
            page.locator("button:has-text('Search')").click();
            int matchingSearchResults = page.locator(".card").count();
            Assertions.assertTrue(matchingSearchResults > 0);

    }
}
