package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
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

    @FindBy(css = ".submit-button")
    private WebElement signUpButton;

    @FindBy(css = "#success-msg")
    private WebElement successMessage;

    @FindBy(css = "#error-msg")
    private WebElement errorMessage;

    private final WebDriver driver;

    public SignUpPage(WebDriver webDriver) {

        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void fillSignUp(String firstName, String lastName, String username, String password) {
        this.firstNameField.sendKeys(firstName);
        this.lastNameField.sendKeys(lastName);
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
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
        return signUpButton;
    }

    public void clickSignUpButton(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signUpButton);
    }

    public boolean getSuccessMessage() {
        return this.successMessage.isDisplayed();
    }

    public boolean getErrorMessage() {
        return this.errorMessage.isDisplayed();
    }


}
