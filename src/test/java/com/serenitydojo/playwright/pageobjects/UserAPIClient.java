package com.serenitydojo.playwright.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.domain.User;

public class UserAPIClient {
    private final Page page;
    private static final String REGISTER_USER = "https://api.practicesoftwaretesting.com/users/register";
    public UserAPIClient(Page page){
        this.page = page;
    }
    public void registerUser(User user){
        var response = page.request().post(REGISTER_USER, RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setData(user));
        if(response.status() != 201){
            throw new IllegalArgumentException("Couldn't create user: "+ response.text() );
        }
    }
}
