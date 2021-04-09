package partners;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonValues_Partners {
	
	public static String PARTNER_URL = "https://stpartners.remotemeeting.com";
	
	public static String TESTACCOUNT_COMPANYCODE = "2c908a9176926493017692bcee930000";

	public static String LOGIN_URI = "/login";
	public static String LOGOUT_URI = "/login?logout";
	public static String DASHBOARD_URI = "/dashboard";
	public static String COMPANY_URI = "/company";
	public static String COMPANYADD_URI = "/company/add";
	public static String PARTNER_URI = "/partner";
	public static String PARTNERADD_URI = "/partner/add";
	public static String LICENSE_URI = "/license";
	public static String PAYMENT_URI = "/payment";
	public static String CONFERENCELOGSTAT_URI = "/conference-log-stats";
	public static String CONFERENCE_URI = "/conference-log";
	public static String QUESTION_URI = "/question";
	public static String QUESTIONADD_URI = "/question/add";
	public static String ANSWER_URI = "/answer";
	public static String MAILSEND_URI = "/mailsend-log";
	public static String BRANCHPARTNER_URI = "/branch-partner";
	public static String MYPAGE_URI = "/mypage";
	public static String INVALIDEMAIL_URI = "/invalidEmail";
	public static String BANWORD_URI = "/banword";
	public static String SALESLIST_URI = "/sales/list";
	
	public static String DASHBOARD_BTN = "//li[@data-menu='dashboard']";
	public static String COMPANY_BTN = "//li[@data-menu='company']";
	public static String PARTNER_BTN = "//li[@data-menu='partner']";
	public static String HISTORY_BTN = "//li[@data-menu='conference-log']/a";
	public static String ERRORLOG_BTN = "//li[@data-menu='conference-log']/ul";
	public static String LICENSE_BTN = "//li[@data-menu='license']";
	public static String PAYMENT_BTN = "//li[@data-menu='payment']";
	public static String LOGSTATS_BTN = "//li[@data-menu='conference-log-stats']";
	public static String QUESTION_BTN = "//li[@data-menu='question']";
	public static String ANSWER_BTN = "//li[@data-menu='answer']";
	public static String MAILSEND_BTN = "//li[@data-menu='mailsend-log']";
	public static String BRANCHPARTNER_BTN = "//li[@data-menu='branch-partner']";
	public static String MYPAGE_BTN = "//li[@data-menu='mypage']";
	public static String INVALIDEMAIL_BTN = "//li[@data-menu='invalidEmail']";
	public static String BANWORD_BTN = "//li[@data-menu='banword']";
	public static String SALESLIST_BTN = "//li[@data-menu='saleslist']";
	
	public static String KO_PARTNER = "rsupkor@rsupport.com";
	public static String JA_PARTNER = "rsupjpn@rsupport.com";

	public static String PW = "111111";
	
	public static String TESTFILE_LIST[] = {"user-list.xlsx", "wrong-user-list.xlsx", "wrong-user-list2.xlsx" };
	
	public void login(WebDriver driver, String ID, String PW) throws Exception {
		
		driver.findElement(By.xpath("//input[@id='j_username']")).sendKeys(ID);
		driver.findElement(By.xpath("//input[@id='j_password']")).sendKeys(PW);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		if (isAlertPresent(driver) == false) {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@class='wrap-monthly-stats']")));

			if (!driver.getCurrentUrl().contentEquals(PARTNER_URL + DASHBOARD_URI)) {
				Exception e = new Exception("Not Login");
				throw e;
			}
		} else {

		}
	}
	
	public void logout(WebDriver driver) throws Exception {
		
		driver.findElement(By.xpath("//div[@id='profile-info']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='wrap-more-profile']")));
		
		driver.findElement(By.xpath("//a[@class='btn-logout']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='login-box']")));
		
		if(!driver.getCurrentUrl().contentEquals(PARTNER_URL + LOGOUT_URI)) {
			Exception e = new Exception("Not Logout");
			throw e;
		}
	}
	
	public boolean isAlertPresent(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public void checkExcelFile(String filename) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {

			path = home + "\\Downloads\\" + filename + ".xlsx";
			File file = new File(path);

			if (file.exists()) {
				deleteExcelFile(path);
			}

		}
	}
	
	public String Excelpath(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {

			path = home + "\\Downloads\\" + filename + ".xlsx";

		} else {
			path = home + "/Downloads/" + filename + ".xlsx";
		}
		return path;
	}
	
	public static void deleteExcelFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("delete file : " + filepath);
	        } else {
	            System.out.println("File is not exist");  
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}

}
