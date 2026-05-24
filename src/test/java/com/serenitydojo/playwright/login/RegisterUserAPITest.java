package com.serenitydojo.playwright.login;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.domain.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterUserAPITest {
    private APIRequestContext request;
    @BeforeEach
    void setUp(Playwright playwright){
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://api.practicesoftwaretesting.com"));
    }
    @AfterEach
    void tearDown(){
        if (request != null){
            request.dispose();
        }
    }
    @Test
    void shouldRegisterTheUser(){
        User validUser = User.randomUser();
        System.out.println(validUser);
        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type","application/json")
                        .setData(validUser)
        );
        Assertions.assertThat(response.status()).isEqualTo(201);
        String responseBody = response.text();
        Gson gson = new Gson();
        User createdUser = gson.fromJson(responseBody,User.class);

        JsonObject responseObject = gson.fromJson(responseBody,JsonObject.class);

        Assertions.assertThat(createdUser).isEqualTo(validUser.withPassword(null));

        assertSoftly(softly ->{
            softly.assertThat(response.status())
                    .as("Registration should return 201 created status code")
                    .isEqualTo(201);
            softly.assertThat(createdUser)
                    .as("Assert that created user is equal to valid user")
                    .isEqualTo(validUser.withPassword(null));

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();
            softly.assertThat(responseObject.has("password"))
                    .as("Registered user should not have a password in response")
                    .isFalse();

            softly.assertThat(response.headers().get("content-type")).contains("application/json");

        });
    }
    @Test
    void firstNameIsMandatory(){
         User user = User.randomUser();
         User invalidUser = user.withFirstName(null);
         var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type","application/json")
                        .setData(invalidUser)
        );
         Gson gson = new Gson();
         String responseBody = response.text();
         JsonObject jsonObject = gson.fromJson(responseBody,JsonObject.class);
         assertSoftly(softly -> {
             softly.assertThat(response.status()).isEqualTo(422);
             softly.assertThat(jsonObject.has("first_name")).isTrue();
             String errorMessage = jsonObject.get("first_name").getAsString();

             softly.assertThat(errorMessage).isEqualTo("The first name field is required.");
         });
    }
    @Test
    void passwordIsMandatory(){
        User user = User.randomUser();
        User userWithoutPassword = user.withPassword(null);

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-type","application/json")
                        .setData(userWithoutPassword));
        JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);

            softly.assertThat(jsonObject.has("password")).isTrue();
            String errorMessage = jsonObject.get("password").getAsString();

            softly.assertThat(errorMessage).contains("The password field is required.");
        });
    }
    @Test
    void getAllUsers(){
        var response = request.get("/users");
        JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
        JsonArray data = jsonObject.getAsJsonArray("data");
        System.out.println(jsonObject);
    }
}
