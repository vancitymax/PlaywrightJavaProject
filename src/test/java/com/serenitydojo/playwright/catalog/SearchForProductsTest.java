package com.serenitydojo.playwright.catalog;

import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import com.serenitydojo.playwright.pageobjects.ProductList;
import com.serenitydojo.playwright.pageobjects.SearchComponent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Searching for products")
public class SearchForProductsTest extends PlaywrightTestCase {

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Nested
    @DisplayName("Searching by keyword")
    class SearchingByKeyword {

        @Test
        @DisplayName("When there are matching results")
        void whenSearchingByKeyword() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);

            searchComponent.searchBy("tape");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
        }

        @Test
        @DisplayName("When there are no matching results")
        void whenThereIsNoMatchingProduct() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("unknown");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).isEmpty();
            Assertions.assertThat(productList.getSearchCompletedMessage()).contains("There are no products found.");
        }

        @Test
        @DisplayName("When the user clears a previous search results")
        void clearingTheSearchResults() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("saw");

            var matchingFilteredProducts = productList.getProductNames();
            Assertions.assertThat(matchingFilteredProducts).hasSize(2);

            searchComponent.clearSearch();

            var matchingProducts = productList.getProductNames();
            Assertions.assertThat(matchingProducts).hasSize(9);
        }


    }
}