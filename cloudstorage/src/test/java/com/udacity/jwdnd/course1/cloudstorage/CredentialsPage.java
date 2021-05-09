package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CredentialsPage {

    /**
     * define fields
     */
    // logout:
    @FindBy(css=".logout-button")
    private WebElement logoutButton;

    // define fields for Credential:
    @FindBy(id = "nav-credentials-tab")
    private WebElement credTab;

    // url field:
    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(xpath = "//*[@id='credUrlText']")
    private WebElement credentialUrlText;

    // username field:
    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    //@FindBy(xpath = "//*[@id='credUsernameText']")
    @FindBy(id = "credUsernameText")
    private WebElement credentialUsernameText;

    // password field:
    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    //@FindBy(xpath = "//*[@id='credPasswordText']")
    @FindBy(id = "credPasswordText")
    private WebElement credentialPasswordText;

    // buttons:
    @FindBy(id = "add-cred-btn")
    private WebElement addCredBtn;

    @FindBy(id = "cred-EditBtn")
    private WebElement editBtn;

    @FindBy(id = "cred-DeleteBtn")
    private WebElement deleteBtn;

    @FindBy(id = "cred-savechanges-btn")
    private WebElement submitBtn;

    @FindBy(id = "success-message3")
    private WebElement successMessage;

    // driver (Chrome):
    private final WebDriver driver;

    // constructor:
    public CredentialsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // method to fill data to add new credentials:
    public void fillCredentialData(String url, String username, String password) {
        // fill in data:
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url + "';", this.credentialUrl);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", this.credentialUsername);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", this.credentialPassword);
    }

    // method to click on Add New Credential button:
    public void clickSaveChangesBtn() {
        // simulate click to submit:
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.submitBtn);
    }

    // method to simulate user to click on Credentials tab:
    public void clickCredTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.credTab);
    }

    // method to click on Add New Credential button:
    public void clickAddCredBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addCredBtn);
    }

    // method to click on Edit button:
    public void clickEditBtn(int pos) {
        List < WebElement > editButtons = driver.findElements(By.id("cred-EditBtn"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButtons.get(pos - 1));
    }

    // method to click on Delete button:
    public void clickDeleteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deleteBtn);
    }


    // verify that new credential's url is created:
    public String getUrlText(int pos) {
        List<WebElement> urls = driver.findElements(By.id("credUrlText"));
        return urls.get(pos-1).getAttribute("innerHTML");
    }

    // verify that new credential's username is created:
    public String getUsernameText(int pos) {
        List<WebElement> urls = driver.findElements(By.id("credUsernameText"));
        return urls.get(pos-1).getAttribute("innerHTML");
    }

    /* Verify that new credential's password is created:
    this should be the value of ENCRYPTED password:*/
    public String getPasswordText(int pos) {
        List<WebElement> urls = driver.findElements(By.id("credPasswordText"));
        return urls.get(pos-1).getAttribute("innerHTML");
    }


    // get unencrypted password:
    public String getUnencryptedPassword() {
        return this.credentialPassword.getAttribute("value");
    }

    //Get success message
    public boolean getSuccessMessage() {
        return this.successMessage.isDisplayed();
    }
}
