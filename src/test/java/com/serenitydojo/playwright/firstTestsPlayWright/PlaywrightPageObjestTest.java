package com.serenitydojo.playwright.firstTestsPlayWright;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.pageobjects.*;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PlaywrightPageObjestTest extends PlaywrightTestCase {



    @Nested
    class whenSearchingProductsByKeyword{
        @BeforeEach
        void openPage(){
            page.navigate("https://practicesoftwaretesting.com");
        }
        @DisplayName("Without Page Object")
        @Test
        void withoutPageObjects(){
            page.waitForResponse("**/products/search?q=tape",()->{
                page.getByPlaceholder("Search").fill("tape");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });

           var matchingProducts = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m","Measuring Tape","Tape Measure 5m");
        }
        @DisplayName("With Page Object")
        @Test
        void withPageObjects(){
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("Tape");

            var matchingProducts = productList.getProductNames();
            Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m","Measuring Tape","Tape Measure 5m");
        }
    }
    @Nested
    class WhenAddingItemsToTheCart{
        SearchComponent searchComponent;
        ProductList productList;
        ProductDetails productDetails;
        NavBar navBar;
        CheckoutCart checkoutCart;


        @BeforeEach
        void setUp(){
            searchComponent = new SearchComponent(page);
            productList = new ProductList(page);
            productDetails = new ProductDetails(page);
            navBar = new NavBar(page);
            checkoutCart = new CheckoutCart(page);
            page.navigate("https://practicesoftwaretesting.com");
        }
        @BeforeEach
        void setupTrace(){
            browserContext.tracing().start(
                    new Tracing.StartOptions()
                            .setScreenshots(true)
                            .setSnapshots(true)
                            .setSources(true)
            );
        }
        @AfterEach
        void recordTrace(TestInfo testInfo){
            String traceName = testInfo.getDisplayName().replace(" ","-").toLowerCase();
            browserContext.tracing().stop(
                    new Tracing.StopOptions()
                            .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
            );
        }
        @DisplayName("Without Page object")
        @Test
        void withoutPageObject(){
            page.waitForResponse("**products/search?q=pliers",()->{
                page.getByPlaceholder("Search").fill("pliers");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
            page.locator(".card").getByText("Combination Pliers").click();

            page.getByTestId("increase-quantity").click();
            page.getByTestId("increase-quantity").click();
            page.getByText("Add to cart").click();
            page.waitForCondition(()-> page.getByTestId("cart-quantity").textContent().equals("3"));
            page.getByTestId("nav-cart").click();

            assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
            assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();;
        }
        @DisplayName("With Page object")
        @Test
        void withPageObject(){
            searchComponent.searchBy("pliers");
            productList.viewProductDetails("Combination Pliers");
            productDetails.increaseQuanityBy(2);
            productDetails.addToCart();
            navBar.openCart();
            List <CartLineItem> listOfItems = checkoutCart.getLineItems();
            Assertions.assertThat(listOfItems)
                    .hasSize(1)
                    .first()
                    .satisfies(cartLineItem -> {
                          Assertions.assertThat(cartLineItem.title()).contains("Combination Pliers");
                          Assertions.assertThat(cartLineItem.quantity()).isEqualTo(3);
                          Assertions.assertThat(cartLineItem.total()).isEqualTo(cartLineItem.quantity() * cartLineItem.price());
                            }
                    );
        }
        @Test
        void whenCheckingOutTheMultipleItems(){
            productList.viewProductDetails("Bolt Cutters");
            productDetails.increaseQuanityBy(2);
            productDetails.addToCart();
            navBar.openHomePage();

            productList.viewProductDetails("Slip Joint Pliers");
            productDetails.addToCart();
            navBar.openCart();
            List <CartLineItem> listOfItems = checkoutCart.getLineItems();
            Assertions.assertThat(listOfItems).hasSize(2);
            List<String> productNames = listOfItems.stream().map(CartLineItem::title).toList();
            Assertions.assertThat(productNames).contains("Bolt Cutters","Slip Joint Pliers");
            Assertions.assertThat(listOfItems)
                    .allSatisfy(cartLineItem -> {
                                Assertions.assertThat(cartLineItem.quantity()).isGreaterThanOrEqualTo(1);
                                Assertions.assertThat(cartLineItem.price()).isGreaterThan(1);
                                Assertions.assertThat(cartLineItem.total()).isGreaterThan(1);
                                Assertions.assertThat(cartLineItem.total()).isEqualTo(cartLineItem.quantity() * cartLineItem.price());
                            }
                    );
        }
    }



}
