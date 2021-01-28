package admin;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/* GroupInfo
 * 1. 그룹정보 화면 이동
 * 2. 그룹정보 - 그룹이름, 전화번호 비우고 저장시도
 * 3. 그룹정보 - 전화번호란에 문자열 입력 후 저장시도
 * 5. 그룹정보 - 사업자등록증 이미지 파일외 파일 등록 시도
 * 6. 그룹정보 - 사업자등록증 정상 이미지 파일 업로드 저장
 * 7. 그룹정보  - 사업자등록증 정상 저장후 미리보기
 * 8. 그룹정보  - 사업자등록증 저장된 이미지 삭제
 * 9. 그룹정보  - 사업자등록증 50자 초과 파일 이름 등록 시도
 * 10. 그룹정보  - 로고 이미지 : 현재 이미지 확인(기본 defalut)
 * 11. 그룹정보  - 로고 이미지 : 신규 이미지 등록
 * 12. 그룹정보  - 로고 이미지 : 신규 이미지 이미지 파일 아닌 파일로 등록 시도
 * 15. 그룹정보  - 로고 이미지 : 기본 이미지 저장
 * 16. 그룹정보  - 기본정보 / 접속페이지 설정 대분류 확인
 * 20. 그룹정보  - free 요금제 사용중일경우 접속페이지 메뉴 사용 불가 확인
 */

public class GroupInfo {
	public static String XPATH_GROUPINFO_VIEW_SUBTITLE = "//div[@class='panel-title default-info']";
	public static String XPATH_GROUPINFO_GROUPNAME = "//input[@id='groupName']";
	public static String XPATH_GROUPINFO_GROUPNAME_ERROR = "//span[@id='groupName-error']";
	public static String XPATH_GROUPINFO_PHONE = "//input[@id='groupPhone']";
	public static String XPATH_GROUPINFO_PHONE_ERROR = "//span[@id='groupPhone-error']";
	public static String XPATH_GROUPINFO_REPNAME = "//input[@id='representativeName']";
	public static String XPATH_GROUPINFO_CONUMBER = "//input[@id='corporateNumber']";
	public static String XPATH_GROUPINFO_LICENSEIMG = "//input[@id='licenseImage']";
	public static String XPATH_GROUPINFO_LICENSEIMG_ERROR = "//span[@id='licenseImage-error']";
	public static String XPATH_GROUPINFO_LICENSEIMGDELETE_BTN = "//span[@class='btn btn-danger remove-file btn-table']";
	
	public static String XPATH_GROUPINFO_LOGO_RADIO_CURRENT = "//div[@id='wrapperLogoImage']//div[@class='radio-wrapper']/div[1]";
	public static String XPATH_GROUPINFO_LOGO_RADIO_DEFAULT = "//div[@id='wrapperLogoImage']//div[@class='radio-wrapper']/div[2]";
	public static String XPATH_GROUPINFO_LOGO_RADIO_NEW = "//div[@id='wrapperLogoImage']//div[@class='radio-wrapper']/div[3]";
	public static String XPATH_GROUPINFO_LOGO_LOUNGE_INPUT = "//input[@id='uploadLogoLounge']";
	public static String XPATH_GROUPINFO_LOGO_ADMIN_INPUT = "//input[@id='uploadLogoAdmin']";
	
	public static String XPATH_GROUPINFO_SAVE_BTN = "//div[@class='row box-footer center']/button[2]";
	public static String XPATH_GROUPINFO_GROUPDELETE_BTN = "//div[@class='row box-footer center']/button[1]";
	public static String XPATH_GROUPINFO_LICENSEIMG_DELETE_BTN = "//span[@class='btn btn-danger remove-file btn-table']";
	public static String XPATH_GROUPINFO_IMGPREVIEW_BTN = "//div[@class='wrap-button-menu input-group margin-left show']/a";
	
	public static String XPATH_LOUNGE_GNB_BI = "//div[@id='gnb-bi-wrap']";
	
	public static String MSG_NOT_IMAGEFILE = "이미지 파일만 등록 가능합니다.";
	public static String MSG_POPUP_SAVE = "저장되었습니다.";
	public static String MSG_IMAGEFILE_TOOLONGNAME = "파일이름은 50자 이내로 제한되어 있습니다.";
	public static String MSG_GROUP_LOGO_INVALIDIMG = "형식에 맞지 않는 파일을 선택하셨습니다.\n" + "(지원형식 : png, jpg, gif)";
	public static String MSG_GROUPINFO_DEFAULT_TOOLTIP = "그룹의 기본정보를 등록해 주세요.\n"
			+ "빠르고 정확한 고객지원을 위해 정확한 정보를 입력해 주시기 바랍니다.";
	
	public static String URL_GROUPINFO = "/customer/view";
	public static String GROUPNAME = "자동화테스트용";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
		
		Connect conn = new Connect();
		if(!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.ADMIN_URL)) {
			conn.logoutAdmin(driver);
		}
		//login
		conn.loginAdmin(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);

	}
	
	@BeforeMethod
	public void setBrowser() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() > 1) {
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
		}
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_GROUPINFO)) {
			driver.get(CommonValues.MEETING_URL + URL_GROUPINFO);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_VIEW_SUBTITLE)));
		} else {
			driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).clear();
			driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).sendKeys(GROUPNAME);
			driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE)).clear();
			driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE)).sendKeys("0100000000");
		}
	}
	
	@Test(priority = 1, enabled = true)
	public void GroupInfo_view() throws Exception {
		String failMsg = "";
		
		//그룹정보 클릭
		Users user = new Users();
		user.selectSideMenu(driver, 6, 0);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_VIEW_SUBTITLE)));
		
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + URL_GROUPINFO)) {
			failMsg = failMsg + "\n1. not groupinfo view. current url : " + driver.getCurrentUrl();
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void GroupInfo_emptySave() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE)).clear();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_GROUPNAME_ERROR)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME_ERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n1. error tooltip msg(empty name) [Expected]" + Users2.MSG_MISS_ESSENTIAL
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME_ERROR)).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE_ERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n2. error tooltip msg(empty phone number) [Expected]" + Users2.MSG_MISS_ESSENTIAL
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE_ERROR)).getText();
		}
		
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void GroupInfo_phoneInvalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).sendKeys(GROUPNAME);
		driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE)).sendKeys(GROUPNAME);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_PHONE_ERROR)));

		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE_ERROR)).getText().contentEquals(Users2.MSG_ADDUSER_VIEW_EMAIL_INVALID)) {
			failMsg = failMsg + "\n1. error tooltip msg(invalid form phone number) [Expected]" + Users2.MSG_ADDUSER_VIEW_EMAIL_INVALID
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_PHONE_ERROR)).getText();
		}
		
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void GroupInfo_licenseImgInvalid() throws Exception {
		String failMsg = "";
		
		String invalidFile = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).sendKeys(invalidFile);
		
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)));

		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)).getText().contentEquals(MSG_NOT_IMAGEFILE)) {
			failMsg = failMsg + "\n1. error tooltip msg(invalid image file) [Expected]" + MSG_NOT_IMAGEFILE
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)).getText();
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void GroupInfo_licenseImgSave() throws Exception {
		String failMsg = "";
		
		String validfile = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[3];
		driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).sendKeys(validfile);
		
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));

		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_POPUP_SAVE)) {
			failMsg = failMsg + "\n1. save popup msg [Expected]" + MSG_POPUP_SAVE
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).getAttribute("data-saved").contentEquals(CommonValues.TESTFILE_LIST[3])) {
			failMsg = failMsg + "\n2. saved image [Expected]" + CommonValues.TESTFILE_LIST[3]
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, dependsOnMethods = {"GroupInfo_licenseImgSave"}, enabled = true)
	public void GroupInfo_licenseImgPreview() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.findElement(By.xpath(XPATH_GROUPINFO_IMGPREVIEW_BTN)).click();
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		//미리보기 확인
		String imgpath = driver.findElement(By.xpath("//img")).getAttribute("src");
		
		if(!imgpath.contains(CommonValues.MEETING_URL + "/storage/customers/")) {
			failMsg = failMsg + "\n1. have to check preview image privew src : " + imgpath;
		}
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 8, dependsOnMethods = {"GroupInfo_licenseImgSave"}, alwaysRun = true, enabled = true)
	public void GroupInfo_licenseImgdelete() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG_DELETE_BTN)).click();
		Thread.sleep(500);

		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).getAttribute("data-saved").isEmpty()) {
			failMsg = failMsg + "\n2. image not deleted image src : " 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).getAttribute("data-saved");
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void GroupInfo_longfilename() throws Exception {
		String failMsg = "";
		
		String filepath = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[3];
		String filepath2 = CommonValues.TESTFILE_PATH + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa51.png";
		File file = new File(filepath);

		try {
			if (file.exists()) {
				Files.copy(new File(filepath).toPath(), new File(filepath2).toPath());
			}
			
			driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG)).sendKeys(filepath2);
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
			driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)));

			if(!driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)).getText().contentEquals(MSG_IMAGEFILE_TOOLONGNAME)) {
				failMsg = failMsg + "\n1. error tooltip msg(long filename) [Expected]" + MSG_IMAGEFILE_TOOLONGNAME
						+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_LICENSEIMG_ERROR)).getText();
			} else {
				driver.navigate().refresh();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_VIEW_SUBTITLE)));
			}
			
			File file2 = new File(filepath2);
			file2.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void GroupInfo_logoCurrent() throws Exception {
		String failMsg = "";
	
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_CURRENT)).click();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		//logo 확인
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LOUNGE_GNB_BI)));
		
		String defaultImg = "/public/service/dist-react/contents/img/logo-main-white.a1fffa149139664d32d67c275fc51de5.svg";
		if(!driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src").contains(defaultImg)) {
			failMsg = failMsg + "\n1. GNB logo img src [Expected]" + defaultImg
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src");
		}
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void GroupInfo_logoNewImg() throws Exception {
		String failMsg = "";
	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 10);
	
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_NEW)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_NEW)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-area preview preview-new']")));
		
		String filepath = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[3];
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_LOUNGE_INPUT)).sendKeys(filepath);
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		//logo 확인
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LOUNGE_GNB_BI)));
		
		String regImg = CommonValues.MEETING_URL + "/storage/companyLoungLogo/";
		if(!driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src").contains(regImg)) {
			failMsg = failMsg + "\n1. GNB logo img src [Expected]" + regImg
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src");
		}
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void GroupInfo_logoNewInvalid() throws Exception {
		String failMsg = "";
	
		driver.navigate().refresh();
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_NEW)));
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_NEW)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_NEW)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-area preview preview-new']")));
		
		String filepath = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_LOUNGE_INPUT)).sendKeys(filepath);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_GROUP_LOGO_INVALIDIMG)) {
			failMsg = "\n1. invalid filetype error msg [Expected]" + MSG_GROUP_LOGO_INVALIDIMG
					+ " [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		}
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void GroupInfo_logoDefault() throws Exception {
		String failMsg = "";
	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_CURRENT)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_LOGO_RADIO_DEFAULT)).click();

		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_SAVE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		js.executeScript("window.open(\"about:blank\");");
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		
		//logo 확인
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_LOUNGE_GNB_BI)));
		
		String defaultImg = "/public/service/dist-react/contents/img/logo-main-white.a1fffa149139664d32d67c275fc51de5.svg";
		if(!driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src").contains(defaultImg)) {
			failMsg = failMsg + "\n1. GNB logo img src [Expected]" + defaultImg
					 + " [Actual]" + driver.findElement(By.xpath(XPATH_LOUNGE_GNB_BI + "//img")).getAttribute("src");
		}
		
		// tab close
		driver.close();
		driver.switchTo().window(tabs2.get(0));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void GroupInfo_logoMenuSplit() throws Exception {
		String failMsg = "";
	
		Actions actions = new Actions(driver);
		WebElement web1 = driver.findElement(By.xpath("//div[@class='panel-section']/div[1]/button[@class='tooltip-mark opend-tooltip']"));
		actions.moveToElement(web1).perform();
		WebElement web = driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME));
		actions.moveToElement(web).perform();
		driver.findElement(By.xpath(XPATH_GROUPINFO_GROUPNAME)).click();
		Thread.sleep(500);
		
		//중분류 기본영역 - 접속페이지
		List<WebElement> sections = driver.findElements(By.xpath("//div[@class='panel-section']/div"));
		
		if(sections.size() != 4) {
			failMsg = failMsg + "\n1. group info view section size is not 4 [Actual]" + sections.size();
		} else {
			if(!sections.get(0).getText().contentEquals("기본정보")) {
				failMsg = failMsg + "\n2. section1 title [Expected]기본정보" + " [Actual]" + sections.get(0).getText();
			}
			if(!sections.get(2).getText().contentEquals("접속페이지 설정")) {
				failMsg = failMsg + "\n3. section2 title [Expected]접속페이지 설정" + " [Actual]" + sections.get(2).getText();
			}
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void GroupInfo_defalutInfoTooltip() throws Exception {
		String failMsg = "";
	
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath("//div[@class='panel-section']/div[1]/button[@class='tooltip-mark opend-tooltip']"));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tooltip fade top in']")));
		if(!driver.findElement(By.xpath("//div[@class='tooltip fade top in']")).getText().contentEquals(MSG_GROUPINFO_DEFAULT_TOOLTIP)) {
			failMsg = failMsg + "\n1. group info defalut info tooltip [Expected]" + MSG_GROUPINFO_DEFAULT_TOOLTIP
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='tooltip fade top in']")).getText();
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void SecurityAccessPage_disabled() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(AccessPage.XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		if(driver.findElement(By.xpath(AccessPage.XPATH_GROUPINFO_CUSTOMPAGE_RADIO1 + "/input")).isEnabled()
				|| driver.findElement(By.xpath(AccessPage.XPATH_GROUPINFO_CUSTOMPAGE_RADIO2 + "/input")).isEnabled()) {
			failMsg = failMsg + "\n1. free user : access page menu is enabled. radio button1 : " 
				+ driver.findElement(By.xpath(AccessPage.XPATH_GROUPINFO_CUSTOMPAGE_RADIO1 + "/input")).isEnabled()
				+ ", radio button2" + driver.findElement(By.xpath(AccessPage.XPATH_GROUPINFO_CUSTOMPAGE_RADIO2 + "/input")).isEnabled();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	public void takescreenshot(WebElement e, String filename) throws IOException {
		System.out.println("try take screenshot");
		String filepath = System.getProperty("user.dir") + "\\test-output\\failimg\\" + filename;
		File scrFile = ((TakesScreenshot) e).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copy somewhere
		FileUtils.copyFile(scrFile, new File(filepath));
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


