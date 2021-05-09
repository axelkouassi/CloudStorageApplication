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
    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-title-edit")
    private WebElement noteTitleEdit;

    //@FindBy(xpath = "//*[@id='noteTitleText']")
    @FindBy(xpath = "noteTitleText")
    private WebElement noteTitleText;

    // description:
    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "note-description-edit")
    private WebElement noteDescriptionEdit;

    //@FindBy(xpath = "//*[@id='noteDescriptionText']")
    @FindBy(id = "noteDescriptionText")
    private WebElement noteDescriptionText;

    // buttons:
    @FindBy(id = "note-save-button")
    private WebElement saveNoteButton;

    @FindBy(id = "add-note-btn")
    private WebElement addNoteBtn;

    @FindBy(id = "note-editBtn")
    private WebElement editNoteBtn;

    @FindBy(id = "note-savechanges-btn")
    private WebElement saveEditNoteButton;

    @FindBy(id = "note-deleteBtn")
    private WebElement deleteNoteBtn;

    @FindBy(id = "success-message")
    private WebElement successMessage;

    @FindBy(id = "success-message2")
    private WebElement successMessage2;



    // driver:
    private final WebDriver driver;

    // constructor:
    public NotesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /** define methods */

    // method to add a new note:
    public void addNote(String title, String description) {
        // fill in data:
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", this.noteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", this.noteDescription);
    }

    // method to edit an existing note:
    public void editNote(String title, String description) {
        // fill in data:
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", this.noteTitleEdit);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", this.noteDescriptionEdit);
    }

    // method to simulate user to click on Notes tab:
    public void clickNoteTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.noteTab);
    }

    // method to click on Add button:
    public void clickAddNoteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.addNoteBtn);
    }

    // method to click to save note button:
    public void clickSaveNoteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveNoteButton);
    }

    // method to click Edit button:
    public void clickEditBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.editNoteBtn);
    }

    // method to click to save edit note button:
    public void clickSaveEditNoteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.saveEditNoteButton);
    }

    // method to click on Delete button:
    public void clickDeleteBtn() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", this.deleteNoteBtn);
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

    //Get success message
    public boolean getSuccessMessage() {
        return this.successMessage.isDisplayed();
    }

    //Get success message
    public boolean getSuccessMessage2() {
        return this.successMessage2.isDisplayed();
    }


}
