package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(css="#inputUsername")
    private WebElement usernameField;

    @FindBy(css="#inputPassword")
    private WebElement passwordField;

    @FindBy(css=".login-btn")
    private WebElement loginButton;

    @FindBy(css = "#error-msg")
    private WebElement errorMessage;

    @FindBy(css = "#logout-msg")
    private WebElement logoutMessage;

    // driver:
    private final WebDriver driver;

    public LoginPage(WebDriver webDriver) {

        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void fillLogin(String username, String password) {
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
    }

    public String getUsernameField() {
        return usernameField.getAttribute("value");
    }

    public String getPasswordField() {
        return passwordField.getAttribute("value");
    }

    public void clickLoginButton(){
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.loginButton);
    }

    public boolean getErrorMessage() {
        return this.errorMessage.isDisplayed();
    }



    public boolean getLogoutMessage() {
        return this.logoutMessage.isDisplayed();
    }


}


