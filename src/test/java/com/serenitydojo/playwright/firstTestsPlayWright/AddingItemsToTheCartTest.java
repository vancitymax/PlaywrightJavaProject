package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddingItemsToTheCartTest extends BaseTest {

    @Test
    void searchForPliers(){
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        List<String> products = page.locator(".card").allTextContents();
        Assertions.assertThat(products.get(0)).containsIgnoringCase("Pliers");
    }
}
