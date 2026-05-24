package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
//@UsePlaywright(HeadLessChromeOptions.class)
public class PlaywrightWithLocators extends BaseTest {

      @DisplayName("Locating element by Text")
      @Test
      void ByText(){
          page.getByText("Bolt Cutters").click();
          assertThat(page.getByText("MightyCraft Hardware")).isVisible();
      }

    @DisplayName("Locating element by AltText")
    @Test
    void byAltText(){
          page.getByAltText("Combination Pliers").click();
          assertThat(page.getByText("ForgeFlex Tools")).isVisible();
    }
    @DisplayName("Locating element by Title")
    @Test
    void byTitle(){
          page.getByAltText("Combination Pliers").click();
          page.getByTitle("Practice Software Testing - Toolshop").click();
          //getByLabel
          //getByPlaceholder
    }
    @DisplayName("testing Contact Page")
    @Test
    void contactPageTest() throws URISyntaxException {
        URL resource = ClassLoader
                .getSystemResource("data/data.txt");
        Path fileToUpload = Paths.get(resource.toURI());
          var uploadField = page.getByLabel("Attachment");
          playwright.selectors().setTestIdAttribute("data-test");
          page.getByTestId("nav-contact").click();
          page.locator("#first_name").fill("Anthony");
          assertThat(page.locator("#first_name")).hasValue("Anthony");
          page.locator("#last_name").fill("Joshua");
          page.locator("#email").fill("norrinradd1337@gmail.com");
          page.locator("#subject").selectOption("Payments");
          page.setInputFiles("#attachment",fileToUpload);
          page.locator("#message").fill("Test Message");
          page.locator("input[data-test='contact-submit']").click();
    }

    @Test
    void testAlert(){
        playwright.selectors().setTestIdAttribute("data-test");
        page.getByTestId("nav-contact").click();
        page.locator("#first_name").fill("Anthony");
        page.locator("input[data-test='contact-submit']").click();
        List<String> alerts = page.locator(".alert").allTextContents();
        Assertions.assertTrue(!alerts.isEmpty());
    }
    @Test
    void nestedElements(){
//        page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
//                .getByText("Home")
//                .click();

        List<String> allProducts = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Pliers"))
                .allTextContents();
        Assertions.assertTrue(!allProducts.isEmpty());

    }
    @Test
    void searchForPliers(){
         playwright.selectors().setTestIdAttribute("data-test");
          page.getByPlaceholder("Search").fill("Pliers");
          page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
          assertThat(page.locator(".card")).hasCount(4);
          List <String> productNames = page.getByTestId("product-name").allTextContents();
          Locator outOfStock = page.locator(".card")
                  .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                  .getByTestId("product-name");
          assertThat(outOfStock).hasCount(1);
          assertThat(outOfStock).hasText("Long Nose Pliers");
    }
    @DisplayName("Mandatory Fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name","Last name","Email","Message"})
    void mandatoryFields(String fieldName){
        playwright.selectors().setTestIdAttribute("data-test");
        page.getByTestId("nav-contact").click();
        //fill in the field values
        page.getByTestId("nav-contact").click();
        page.locator("#first_name").fill("Anthony");
        assertThat(page.locator("#first_name")).hasValue("Anthony");
        page.locator("#last_name").fill("Joshua");
        page.locator("#email").fill("norrinradd1337@gmail.com");
        page.locator("#subject").selectOption("Payments");
        page.locator("#message").fill("Test Message");

        //clear
        page.getByLabel(fieldName).clear();
        page.locator("input[data-test='contact-submit']").click();
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
        assertThat(errorMessage).isVisible();

    }




}
