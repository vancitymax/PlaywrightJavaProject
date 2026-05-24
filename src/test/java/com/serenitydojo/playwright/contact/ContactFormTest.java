package com.serenitydojo.playwright.contact;

import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.pageobjects.ContactForm;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import com.serenitydojo.playwright.toolshop.fixtures.ScreenshotManager;
import com.serenitydojo.playwright.toolshop.fixtures.TakesFinalScreenshot;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ContactFormTest extends PlaywrightTestCase  {
    ContactForm contactForm;
    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
    @AfterEach
    void takeFinalScreenshot(){
        ScreenshotManager.takeScreenshot(page,"Final screenshot");
    }
    @Test
    void completeForm() throws URISyntaxException {
        URL resource = ClassLoader
                .getSystemResource("data/data.txt");
        Path fileToUpload = Paths.get(resource.toURI());
        contactForm.setFirstNameField("Max");
        contactForm.setLastNameField("Palamarchuk");
        contactForm.setEmailField("max.palamarchuk1337@gmail.com");
        contactForm.setSubjectField("Webmaster");
        contactForm.setMessageField("Test message test message test message test message test message");
        contactForm.setAttachment(fileToUpload);
        contactForm.clickSendButton();
        Assertions.assertTrue((contactForm.getAlertMessage()).contains("Thanks for your message! We will contact you shortly."));

    }
    @DisplayName("Mandatory Fields")
    @ParameterizedTest
    @ValueSource(strings = {"First name","Last name","Email","Message"})
    void mandatoryFields(String fieldName){
        //fill in the field values
//        URL resource = ClassLoader
//                .getSystemResource("data/data.txt");
//        Path fileToUpload = Paths.get(resource.toURI());
        contactForm.setFirstNameField("Max");
        contactForm.setLastNameField("Palamarchuk");
        contactForm.setEmailField("max.palamarchuk1337@gmail.com");
        contactForm.setSubjectField("Webmaster");
        contactForm.setMessageField("Test message test message test message test message test message");
        //contactForm.setAttachment(fileToUpload);


        //clear
        contactForm.clearFieldName(fieldName);
        contactForm.clickSendButton();
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
        assertThat(errorMessage).isVisible();

    }



}
