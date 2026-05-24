package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightWaitsTest extends BaseTest {
   @Test
    void shouldShowAllProductNames(){
       page.waitForSelector("[data-test=product-name]");
       List<String> productName = page.getByTestId("product-name").allInnerTexts();
       Assertions.assertThat(productName).contains("Pliers","Bolt Cutters","Hammer");

   }

   @Nested
    class AutomaticWaits{

       @Test
       @DisplayName("Should wait for the filter checkbox options to appear before clicking")
       void shouldWaitForTheFilterCheckboxes(){
           var screwdriverFilter = page.getByLabel("Screwdriver");
           screwdriverFilter.click();
           assertThat(screwdriverFilter).isChecked();
       }

       @Test
       @DisplayName("Should filter products by category")
       void shouldFilterProductsByCategory(){
           page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
           page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();
           page.waitForSelector(".card",
                   new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
           var filteredProducts = page.getByTestId("product-name").allInnerTexts();
           Assertions.assertThat(filteredProducts).contains("Sheet Sander","Circular Saw");
       }


   }
   @Nested
    class waitingForElementsToAppearAndDisappear{
    @Test
    @DisplayName("It should display a toaster message when an item is addet to the cart")
    void shouldDisplayToasterMessage(){
       page.getByText("Bolt Cutters").click();
       page.getByText("Add to cart").click();

       assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");
        page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
    }
    @Test
    @DisplayName("Should update the card item count")
    void shouldUpdateTheCardItemCount(){

        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();
//        String count = page.getByTestId("cart-quantity").innerText();
//        Assertions.assertThat(count).isEqualTo("1");
        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
        page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
    }
    @Nested
    class WaitingForAPICalls{

        @Test
        void sortByDescendingPrice(){
            //https://api.practicesoftwaretesting.com/products?page=0&sort=price,desc&between=price,1,100&is_rental=false

            page.waitForResponse("**/products?*sort*",
                    ()-> {
                        page.getByTestId("sort").selectOption("Price (High - Low)");
                    });

            List <Double> productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrices)
                    .toList();
            System.out.println(productPrices);
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }
        private static Double extractPrices(String price){
            return Double.parseDouble(price.replace("$",""));
        }

    }

   }

}
