package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {

    @FindBy(css = "#inputFirstName")
    private WebElement firstNameField;

    @FindBy(css = "#inputLastName")
    private WebElement lastNameField;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;

    @FindBy(css = "#inputPassword")
    private WebElement passwordField;

    @FindBy(css = "#submit-button")
    private WebElement submitButton;

    public SignUpPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void fillSignUp(String firstName, String lastName, String username, String password) {
        this.firstNameField.sendKeys(firstName);
        this.lastNameField.sendKeys(lastName);
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
    }

    public void clickSignUpButton(){
        this.submitButton.click();
    }

    public String getFirstNameField() {

        return firstNameField.getAttribute("value");
    }

    public String getLastNameField() {
        return lastNameField.getAttribute("value");
    }

    public String getUsernameField() {
        return usernameField.getAttribute("value");
    }

    public String getPasswordField() {
        return passwordField.getAttribute("value");
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }
}
