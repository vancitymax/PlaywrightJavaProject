package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.options.LoadState;
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

        @DisplayName("Making assertions about data values")
        @Nested
        class MakingAssertionsAboutDataValues extends  BaseTest{
            @Test
            void allProductPricesShouldBeCorrectValues(){
                List<Double> prices = page.getByTestId("product-price")
                        .allInnerTexts()
                        .stream()
                        .map(price -> Double.parseDouble(price.replace("$","")))
                        .toList();
                Assertions.assertThat(prices)
                        .isNotEmpty()
                        .allMatch(price -> price > 0)
                        .doesNotContain(0.0);
            }

            @Test
            void shouldSortInAlphabeticalOrder(){
                page.getByLabel("Sort").selectOption("Name (A - Z)");
                page.waitForLoadState(LoadState.NETWORKIDLE);

                List<String> productNames = page.getByTestId("product-name").allTextContents();
                Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
            }
        }
    }
}
