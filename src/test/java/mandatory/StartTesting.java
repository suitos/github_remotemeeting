package mandatory;

import static org.testng.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.rsupport.rm.GoogleSheets;


public class StartTesting {
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
		//do not anything
	}
	
	@Parameters({"pkgname"})
	@Test(priority = 1)
	public void clearSheet(String pkgname) throws Exception {
		GoogleSheets sheet= new GoogleSheets(pkgname);
		try {
			sheet.clearSheet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update test date
		
		
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String today = format1.format(cal.getTime());
		try {
			sheet.updateCell("L1", today);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
