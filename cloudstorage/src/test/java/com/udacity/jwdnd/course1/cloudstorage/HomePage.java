package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(css=".logout-button")
    private WebElement logoutButton;

    private final WebDriver driver;

    public HomePage(WebDriver webDriver) {

        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void clickLogoutButton(){
        ( (JavascriptExecutor)driver).executeScript("arguments[0].click;",logoutButton);
    }
}
