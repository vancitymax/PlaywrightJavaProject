package com.serenitydojo.playwright.firstTestsPlayWright;

import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightAssertionsTest {

    @DisplayName("Making Assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCss extends BaseTest {

        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues(){
            var firstNameField = page.getByLabel("First name");
            firstNameField.fill("Sarah-Jane");
            assertThat(firstNameField).hasValue("Sarah-Jane");
            assertThat(firstNameField).not().isDisabled();
        }
    }

    // Виносимо вкладений клас наодинці з зовнішнього, щоб він не успадковував openContactPage()
    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues extends BaseTest {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        void allProductPricesShouldBeCorrectValues(){
            var pricesLocator = page.getByTestId("product-price");
            assertThat(pricesLocator.first()).isVisible();

            List<Double> prices = pricesLocator
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                    .toList();

            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 0)
                    .doesNotContain(0.0);
        }

        @Test
        void shouldSortInAlphabeticalOrder(){
            page.getByLabel("Sort").selectOption("Name (A - Z)");

            var namesLocator = page.getByTestId("product-name");
            assertThat(namesLocator.first()).isVisible();

            List<String> productNames = namesLocator.allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
        }
    }
}