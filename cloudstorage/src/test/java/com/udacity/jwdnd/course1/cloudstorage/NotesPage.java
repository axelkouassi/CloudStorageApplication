package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotesPage {

    @FindBy(css=".logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    // title field:
    @FindBy(id = "note-title-edit")
    private WebElement noteTitle;

    @FindBy(xpath = "//*[@id='noteTitleText']")
    private WebElement noteTitleText;

    // description:
    @FindBy(id = "note-description-edit")
    private WebElement noteDescription;

    @FindBy(xpath = "//*[@id='noteDescriptionText']")
    private WebElement noteDescriptionText;

    // buttons:
    @FindBy(id = "add-note-btn")
    private WebElement addNoteBtn;

    @FindBy(id = "note-editBtn")
    private WebElement editNoteBtn;

    @FindBy(id = "note-deleteBtn")
    private WebElement deleteNoteBtn;

    @FindBy(id = "note-savechanges-btn")
    private WebElement submitBtn;

    // driver:
    private final WebDriver driver;

    // constructor:
    public NotesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /** define methods */

    // method to simulate user to click on Notes tab:
    public void clickNoteTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
    }

    // method to click on Add button:
    public void clickAddNoteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addNoteBtn);
    }

    // method to click Edit button:
    public void clickEditBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.editNoteBtn);
    }

    // method to click on Delete button:
    public void clickDeleteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deleteNoteBtn);
    }

    // method to fill data for note. Use both for Add and Edit Test:
    public void fillNoteData(String title, String description) {
        // fill in data:
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", this.noteDescription);

        // click on "Save Changes" to submit:
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.submitBtn);
    }

    // verify that new note title is created:
    public String getNoteTitleText() {
        return noteTitleText.getAttribute("innerHTML");
    }

    // verify that new note description is created:
    public String getNoteDescriptionText() {
        return noteDescriptionText.getAttribute("innerHTML");
    }

    // simulate user to click logout button:
    public void logout() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.logoutButton);
    }


}
