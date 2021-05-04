package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

//	private WebDriver driver;
	public static WebDriver driver;

	public String baseURL;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
//		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}

	@BeforeEach
	public void beforeEach() {
//		this.driver = new ChromeDriver();
		driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
		//wait = new WebDriverWait(driver, 100);
	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		if (driver != null) {
			Thread.sleep(3000);
			driver.quit();
		}
	}

	@Test
	public void testDisplayOfLoginPage() {
		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/*Write a test that verifies that an unauthorized user
	 can only access the login and signup pages.*/
	@Test
	public void testUnauthorizedAccessRestrictions() throws InterruptedException{
		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
		Thread.sleep(2000);
		driver.get(baseURL + "/signup");
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUserSignUpLoginHomePageAccessLogOutHomePageRestrict()
			throws InterruptedException{

		//User registration information
		String firstName = "A musician";
		String lastName = "Nicky";
		String userName = "minaj";
		String password = "sons";

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
		Thread.sleep(5000);

		//Click signup button
		signupPage.clickSignUpButton();

		//Wait for 5 seconds
		Thread.sleep(5000);

		//Go to login page
		driver.get(baseURL + "/login");

		//Initialize driver for login page
		LoginPage loginPage = new LoginPage(driver);
		//Have user fill login fields
		loginPage.fillLogin(userName, password);

		//Check user's information entered in each field
		assertEquals(userName, loginPage.getUsernameField());
		assertEquals(password, loginPage.getPasswordField());

		//Wait for 5 seconds
		Thread.sleep(5000);

		//Click login button
		loginPage.clickLoginButton();

		//Check that user has access to home page
		assertEquals("Home", driver.getTitle());

		//Wait for 5 seconds
		Thread.sleep(5000);

		//Initialize driver for home page
		HomePage homePage = new HomePage(driver);
		//Click log out button
		homePage.clickLogoutButton();

		//Try accessing home page after logging out
		driver.get(baseURL + "/home");
		//Verifies that such attempt redirects user to login page
		assertEquals("Login", driver.getTitle());
	}

}
