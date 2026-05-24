package com.serenitydojo.playwright.login;

import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.domain.User;
import com.serenitydojo.playwright.pageobjects.LoginPage;
import com.serenitydojo.playwright.pageobjects.UserAPIClient;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

    @Test
    @DisplayName("Should be able to log in with register user")
    void shouldLoginWithRegisterUser(){
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);
       LoginPage loginPage = new LoginPage(page);
       loginPage.open();
       loginPage.loginAs(user);
       Assertions.assertThat(loginPage.title()).isEqualTo("My account");

    }
    @Test
    void should_reject_user_with_invalid_password(){
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("wrong password"));
        Assertions.assertThat(loginPage.errorLogin()).isEqualTo("Invalid email or password");
    }

}
