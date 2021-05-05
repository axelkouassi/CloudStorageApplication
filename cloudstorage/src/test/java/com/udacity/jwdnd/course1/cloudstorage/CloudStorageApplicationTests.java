package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port; //random port

//	private WebDriver driver;
	public static WebDriver driver;

	public String baseURL;

	//User registration information
	String firstName = "A musician";
	String lastName = "Nicky";
	String userName = "minaj";
	String password = "sons";

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private NoteService noteService;

	private EncryptionService encryptionService;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}

	@BeforeEach
	public void beforeEach() {
		driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		if (driver != null) {
			Thread.sleep(2000);
			driver.quit();
		}
	}

	@Test
	public void testDisplayOfLoginPage() {
		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/*Write a test that verifies that an unauthorized user can only access the login and signup pages.*/
	@Test
	public void testUnauthorizedAccessRestrictions() throws InterruptedException{
		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());

		Thread.sleep(2000);

		driver.get(baseURL + "/signup");
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testSuccessfullySignUpUser() throws InterruptedException{
		// go to signup page
		driver.get(baseURL + "/signup");

		//Initialize web driver
		SignUpPage signupPage = new SignUpPage(driver);
		//Have user fill registration fields
		signupPage.fillSignUp(firstName, lastName, userName, password);

		//Check user's information filled
		assertEquals(firstName, signupPage.getFirstNameField());
		assertEquals(lastName, signupPage.getLastNameField());
		assertEquals(userName, signupPage.getUsernameField());
		assertEquals(password, signupPage.getPasswordField());

		//Wait for 5 seconds
		Thread.sleep(2000);

		//Click signup button
		signupPage.clickSignUpButton();

		assertTrue(signupPage.getSuccessMessage());

	}

	@Test
	public void testSuccessfullyLoginUser() throws InterruptedException{
		// Sign up
		testSuccessfullySignUpUser();
		// Go to login page
		driver.get(baseURL + "/login");

		//Initialize driver for login page
		LoginPage loginPage = new LoginPage(driver);
		//Have user fill login fields
		loginPage.fillLogin(userName, password);

		//Check user's information entered in each field
		assertEquals(userName, loginPage.getUsernameField());
		assertEquals(password, loginPage.getPasswordField());

		//Wait for 5 seconds
		Thread.sleep(2000);

		//Click login button
		loginPage.clickLoginButton();

		//Check that user has access to home page
		assertEquals("Home", driver.getTitle());

	}

	/* Write a test that signs up a new user, logs in,
	verifies that the home page is accessible, logs out,
	and verifies that the home page is no longer accessible.*/
	@Test
	public void testUserSignUpLoginHomePageAccessLogOutHomePageRestrict()
			throws InterruptedException{

		// Sign up
		testSuccessfullySignUpUser();
		// Go to login page
		driver.get(baseURL + "/login");

		//Initialize driver for login page
		LoginPage loginPage = new LoginPage(driver);

		//Have user fill login fields
		loginPage.fillLogin(userName, password);

		//Check user's information entered in each field
		assertEquals(userName, loginPage.getUsernameField());
		assertEquals(password, loginPage.getPasswordField());

		//Wait for 5 seconds
		Thread.sleep(2000);

		//Click login button
		loginPage.clickLoginButton();

		//Check that user has access to home page
		assertEquals("Home", driver.getTitle());

		//Wait for 5 seconds
		Thread.sleep(2000);

		//Initialize driver for home page
		HomePage homePage = new HomePage(driver);

		//Click log out button to exit home page
		homePage.clickLogoutButton();

		new WebDriverWait(driver,4).until(ExpectedConditions.titleIs("Login"));

		assertEquals("Login", driver.getTitle());
		assertTrue(loginPage.getLogoutMessage());

		Thread.sleep(2000);

		//Try accessing home page after logging out
		driver.get(baseURL + "/home");

		//Verifies that such attempt redirects user to login page
		assertEquals("Login", driver.getTitle());
	}


	//Write a test that creates a note, and verifies it is displayed.
	@Test
	public void testCreateNoteAndVerifyDisplay() throws InterruptedException{
		testSuccessfullyLoginUser();

		Thread.sleep(2000);

		// initialize homepage page:
		NotesPage notesPage = new NotesPage(driver);

		// Click Notes tab:
		notesPage.clickNoteTab();

		Thread.sleep(2000);

		// simulate user to click "Add/Edit a Note" button to add new note:
		notesPage.clickAddNoteBtn();

		Thread.sleep(2000);

		// fill in data to add a new note:
		notesPage.fillNoteData("Test Title", "Test Description");

		Thread.sleep(2000);

		// Click Notes tab:
		notesPage.clickNoteTab();

		Thread.sleep(2000);
	}

	/*Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.*/
	@Test
	public void testCreateCredentialAndVerifyDisplay() throws InterruptedException {
		testSuccessfullyLoginUser();

		// initialize Encryption service to encrypt/decrypt password
		// inside add credential and edit credential:
		encryptionService = new EncryptionService();

		// initialize homepage page:
		CredentialsPage credentialsPage = new CredentialsPage(driver);

		Thread.sleep(2000);

		// simulate user to click on Credentials tab on nav bar:
		credentialsPage.clickCredTab();

		Thread.sleep(2000);

		// simulate user to click on Add new credential button:
		credentialsPage.clickAddCredBtn();

		Thread.sleep(2000);

		// simulate user to add new data to create add credential:
		credentialsPage.fillCredentialData("cred", "cred", "cred");

		Thread.sleep(2000);



	}


}
