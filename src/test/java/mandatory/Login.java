package mandatory;

import static org.testng.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.rsupport.rm.GoogleSheets;


public class Login {
	
	public static WebDriver driver;
	
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		CommonValues comm = new CommonValues();
		
		driver = comm.setDriver(driver, browsertype, "lang=en_US", true);

		context.setAttribute("webDriver", driver);
		
		driver.get(CommonValues.MEETING_URL);

		System.out.println("End BeforeTest!!!");
	}
	
	@Test(priority = 1)
	public void loginseminar() throws Exception {
		
		driver.findElement(By.xpath(CommonValues.XPATH_HOME_LOGIN_BTN)).click();
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
