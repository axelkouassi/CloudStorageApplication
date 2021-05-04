package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(css="#inputUsername")
    private WebElement usernameField;

    @FindBy(css="#inputPassword")
    private WebElement passwordField;

    @FindBy(css="#submit-button")
    private WebElement submitButton;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void fillLogin(String username, String password) {
        this.usernameField.sendKeys(username);
        this.passwordField.sendKeys(password);
    }

    public void clickLoginButton(){
        this.submitButton.click();
    }

    public String getUsernameField() {
        return usernameField.getAttribute("value");
    }

    public String getPasswordField() {
        return passwordField.getAttribute("value");
    }
}
