package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/*Write a test that verifies that an unauthorized user
	 can only access the login and signup pages.*/
	@Test
	public void testUnauthorizedAccessRestrictions() throws InterruptedException{
		driver.get(baseURL + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
		Thread.sleep(2000);
		driver.get(baseURL + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUserSignUpLoginHomePageAccessLogOutHomePageRestrict()
			throws InterruptedException{

		String firstName = "testing";
		String lastName = "testing";
		String userName = "testing";
		String password = "testing";

		driver.get(baseURL + "/signup");

		SignUpPage signupPage = new SignUpPage(driver);
		signupPage.signUp(firstName, lastName, userName, password);

		Assertions.assertEquals(firstName, signupPage.getFirstNameField());
		Assertions.assertEquals(lastName, signupPage.getLastNameField());
		Assertions.assertEquals(userName, signupPage.getUsernameField());
		Assertions.assertEquals(password, signupPage.getPasswordField());

		Thread.sleep(30000);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);

		Assertions.assertEquals(userName, loginPage.getUsernameField());
		Assertions.assertEquals(password, loginPage.getPasswordField());

		Assertions.assertEquals("Home", driver.getTitle());

		Thread.sleep(5000);
	}

}
