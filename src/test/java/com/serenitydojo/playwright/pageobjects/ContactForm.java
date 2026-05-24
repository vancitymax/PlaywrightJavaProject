package com.serenitydojo.playwright.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Path;

public class ContactForm {
    private final Page page;
    private Locator firstNameField;
    private Locator lastNameField;
    private Locator emailField;
    private Locator subjectField;
    private Locator messageField;
    private Locator sendButton;
    private Locator attachment;
    public ContactForm(Page page){
        this.page = page;
        this.firstNameField = page.getByLabel("First name");
        this.lastNameField = page.getByLabel("Last name");
        this.emailField = page.getByLabel("Email address");
        this.subjectField = page.getByLabel("Subject");
        this.messageField = page.getByLabel("Message");
        this.sendButton = page.getByTestId("contact-submit");

    }
    public void setFirstNameField(String firstName){
        firstNameField.fill(firstName);
    }

    public void setLastNameField(String lastName) {
        lastNameField.fill(lastName);
    }
    public void setEmailField(String email){
        emailField.fill(email);
    }
    public void setSubjectField(String subj){
        subjectField.selectOption(subj);
    }
    public void setMessageField(String msg){
        messageField.fill(msg);
    }
    public void clickSendButton(){
        sendButton.click();
    }
    public void setAttachment(Path fileToUpload){
        page.setInputFiles("#attachment",fileToUpload);

    }
    public String getAlertMessage()
    {
        return page.getByRole(AriaRole.ALERT).textContent();
    }
    public void clearFieldName(String field){
        page.getByLabel(field).clear();
    }

}
