package admin;

import static org.testng.Assert.fail;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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

/* AccessPage
 * 1. 접속페이지 설정 화면 기본값 사용안함, 사용안함 - 메뉴 안보임, 사용함 - 메뉴 보임 확인
 * 2. 그룹정보 - 접속페이지 서브타이틀 툴팁 확인
 * 3. 그룹정보 - 접속페이지 사용 설정 후 URI 입력 란 확인 (한글, 특문 입력확인, 20자 초과 입력, 대문자 입력, 정상케이스 확인)
 * 4. 그룹정보 - 접속페이지 사용 설정 후 URI 입력 확인 (숫자만 입력, 안내 풍선확인, 사용가능한 주소 확인 후 얼럿 확인)
 * 5. 그룹정보 - 접속페이지 사용 설정 - 사용할 수 없는 단어 포함 케이스 확인
 * 6. 그룹정보 - 접속페이지 : 중복되는 uri 설정 케이스 확인
 * 7. 그룹정보 - 접속페이지 : 안내 타이틀 확인
 * 8. 그룹정보 - 접속페이지 : 안내 서브 타이틀 확인
 * 10. 그룹정보 - 접속페이지 : 배경화면 이미지 설정 라디오 버튼 확인
 * 11. 그룹정보 - 접속페이지 : 배경화면 이미지 현재 이미지 확인 - 새이미지로 저장후 현재 이미지 확인
 * 12. 그룹정보 - 접속페이지 : 배경화면 이미지 기본 이미지 확인
 * 13. 그룹정보 - 접속페이지 : 배경화면 이미지 파일 아닌 파일로 등록 시도
 * 14. 그룹정보 - 접속페이지 : 접속페이지 uri 빈채로 저장 시도
 * 15. 그룹정보 - 접속페이지 : 접속페이지 uri 중복체크 하지 않은 상태에서 저장 시도
 * 16. 그룹정보 - 접속페이지 : 접속페이지 사용하도록 저장후 url 복사 동작 확인
 * 17. 그룹정보 - 접속페이지 : 사용함 - 사용안함 - 사용함 으로 저장변경 후 기존 uri 값 남아있음 확인
 */

public class AccessPage {
	
	public static String XPATH_GROUPINFO_CUSTOMPAGE_RADIO1 = "//div[@id='customPageSettings']//div[@class='radio-wrap option-group mobile'][1]";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_RADIO2 = "//div[@id='customPageSettings']//div[@class='radio-wrap option-group mobile'][2]";
	
	public static String XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP = "//div[@class='panel-title wrap-access-page']/button[@class='tooltip-mark']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_OPTIONS = "//div[@id='customPageSettings']/div[@class='row optional']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT = "//input[@id='customAdress']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_URIERROR = "//span[@id='customAdress-error']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_URIBTN = "//button[@id='adressOverlap']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_ERROR = "//span[@id='customAdress-error']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_DESC_TITLE = "//input[@name='title']";
	public static String XPATH_GROUPINFO_CUSTOMPAGE_DESC_SUBTITLE = "//textarea[@name='subTitle']";
	public static String XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1 = "//div[@id='wrapperBackgroundImage']/div[@class='wrapper-radio']/div[1]";
	public static String XPATH_GROUPINFO_BACKGROUND_IMG_RADIO2 = "//div[@id='wrapperBackgroundImage']/div[@class='wrapper-radio']/div[2]";
	public static String XPATH_GROUPINFO_BACKGROUND_IMG_RADIO3 = "//div[@id='wrapperBackgroundImage']/div[@class='wrapper-radio']/div[3]";
	public static String XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW = "//div[@class='view-area-inner']/div[@class='image-holder']";
	public static String XPATH_GROUPINFO_BACKGROUND_NEW_INPUT = "//input[@id='backgroundImage']";
	
	public static String MSG_GROUPINFO_ACCESSPAGE_TOOLTIP = "요금제 이용 중인 그룹만 사용 가능한 URL 설정 기능입니다.";
	public static String MSG_GROUPINFO_ACCESSPAGE_URLDUPLICATED = "사용중인 접속 주소입니다.";
	public static String MSG_GROUPINFO_ACCESSPAGE_URLVALID = "사용 가능한 접속 주소입니다.";
	public static String MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD = "사용할 수 없는 단어가 포함되었습니다.\n다시 입력해주세요.";
	public static String MSG_GROUPINFO_ACCESSPAGE_CHECKURI_ERROR = "접속주소 중복확인을 해주세요.";
	public static String MSG_GROUPINFO_ACCESSPAGE_BACKGROUND_INVALIDIMG = "형식에 맞지 않는 파일을 선택하셨습니다.\n"+ "(지원형식 : png, jpg, gif)";
	public static String MSG_GROUPINFO_ACCESSPAGE_COPYURL = "접속 주소가 복사되었습니다.";
	
	public static String GROUPINFO_TESTURI = "autotest";
	
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
		//login - 요금제 사용중인 유저만 접속페이지 설정이 가능하다.
		conn.loginAdmin(driver, DashBoard.BASIC_ADM, CommonValues.USERPW);

	}
	
	@BeforeMethod
	public void setBrowser() {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		if(tabs.size() > 1) {
			driver.switchTo().window(tabs.get(1));
			driver.close();
			driver.switchTo().window(tabs.get(0));
		}
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + GroupInfo.URL_GROUPINFO)) {
			driver.get(CommonValues.MEETING_URL + GroupInfo.URL_GROUPINFO);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(GroupInfo.XPATH_GROUPINFO_VIEW_SUBTITLE)));
		} 
	}
	
	@Test(priority = 1, enabled = true)
	public void GroupInfo_AccessPageDefault() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)));
		
		if(driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1 + "/input")).isSelected() 
				|| !driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO2 + "/input")).isSelected()) {
			failMsg = failMsg + "\n1. Access custom page value [Use]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1 + "/input")).isSelected()
					+ " [Not Use]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO2 + "/input")).isSelected();
		}
		
		//menu 확인 - 사용안함 메뉴 안보임
		List<WebElement> options = driver.findElements(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_OPTIONS));
		if(options.size() > 0 ) {
			for (WebElement webElement : options) {
				if(webElement.isDisplayed()) {
					failMsg = failMsg + "\n2. Access custom option is displayed";;
				}
			}
		}
		
		// 사용함 클릭
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_OPTIONS)));
		options = driver.findElements(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_OPTIONS));
		if(options.size() > 0 ) {
			for (WebElement webElement : options) {
				if(!webElement.isDisplayed()) {
					failMsg = failMsg + "\n3. Access custom option is not displayed(after click USE)";
				}
			}
		} else {
			failMsg = failMsg + "\n4. cannot find page option menu";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void GroupInfo_AccessPageToolTip() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Actions actions = new Actions(driver);
		WebElement web = driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP));
		actions.moveToElement(web).perform();
		Thread.sleep(500);
		
		String tootipXpath = "//div[@class='panel-title wrap-access-page']/div[@class='tooltip fade top in']";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tootipXpath)));
		if(!driver.findElement(By.xpath(tootipXpath)).getText().contentEquals(MSG_GROUPINFO_ACCESSPAGE_TOOLTIP)) {
			failMsg = failMsg + "\n1. access page tooltip msg [Exepcted]" + MSG_GROUPINFO_ACCESSPAGE_TOOLTIP
					 + " [Actual]" + driver.findElement(By.xpath(tootipXpath)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void GroupInfo_AccessPageUri() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		//특수문자, 한글 입력
		String str = "한글!!";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).click();
		js.executeScript("arguments[0].select();", driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		if(driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before").contains(str)) {
			failMsg = failMsg + "\n1. input korean, special Characters : " 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before");
		}
		
		//20자 초과 입력
		String longstr = "aaaaaaaaaaaaaaaaaaaab";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(longstr);
		if(driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before").length() != 20) {
			failMsg = failMsg + "\n2. uri max length is not 20, lenght : " 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before").length();
		}
	
		//대문자 입력
		String upper = "ABCDE";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(upper);
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).getAttribute("data-checked").contentEquals(upper.toLowerCase())) {
			failMsg = failMsg + "\n3. input UpperCase : " 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-checked");
		}
		
		//정상케이스 5~20자 영문 숫자 조합
		str = "abc123";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before").contentEquals(str)) {
			failMsg = failMsg + "\n4. input valid characters : " 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("data-before");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void GroupInfo_AccessPageUriCheck() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		//숫자만 5자 버튼 비활성화
		String str = "12345";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
	
		if(driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).isEnabled()) {
			failMsg = failMsg + "\n1. input number only, button is enabled";
		}
	
		//안내 풍선 확인
		str = "hellotest";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_ERROR)));
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_ERROR)).getText().contentEquals(MSG_GROUPINFO_ACCESSPAGE_CHECKURI_ERROR)) {
			failMsg = failMsg + "\n2. uri description balloon msg [Expected]" + XPATH_GROUPINFO_CUSTOMPAGE_ERROR
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_ERROR)).getText();
		}
		
		//사용가능한 주소 확인
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLVALID)) {
			failMsg = "\n3. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLVALID
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void GroupInfo_AccessPageUriWord() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		//사용할수 없는 단어 포함1
		String str = "shit1";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD)) {
			failMsg = "\n2. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
		
		//사용할수 없는 단어 포함2
		str = "shit2 ";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD)) {
			failMsg = "\n2. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
	
		//사용할수 없는 단어 포함3
		str = "1shit";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD)) {
			failMsg = "\n2. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
		
		//사용할수 없는 단어 포함4
		str = "1shit2";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD)) {
			failMsg = "\n2. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLINVALIDWORD
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void GroupInfo_AccessPageUriDup() throws Exception {
		String failMsg = "";
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		//중복체크
		String str = "englishsub ";
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(str);
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIBTN)).click();
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		if(!alert.getText().contains(MSG_GROUPINFO_ACCESSPAGE_URLDUPLICATED)) {
			failMsg = "\n2. alert msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_URLDUPLICATED
					 + " [Actual]" + alert.getText();
		}
		alert.accept();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void GroupInfo_AccessPageSubDescTitle() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_TITLE)).getAttribute("placeholder").contentEquals("Title")) {
			failMsg = failMsg + "\n1. sub description title placeholder [Expected]Title [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_TITLE)).getAttribute("placeholder");
		}
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_TITLE)).getAttribute("maxlength").contentEquals("15")) {
			failMsg = failMsg + "\n1. sub description title maxlength [Expected]15 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_TITLE)).getAttribute("maxlength");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 8, enabled = true)
	public void GroupInfo_AccessPageSubDescTitle2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_ACCESSPAGE_TOOLTIP)));
		
		//placeholder
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_SUBTITLE)).getAttribute("placeholder").contentEquals("Sub title")) {
			failMsg = failMsg + "\n1. sub description title placeholder [Expected]Sub title [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_SUBTITLE)).getAttribute("placeholder");
		}

		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_SUBTITLE)).getAttribute("maxlength").contentEquals("150")) {
			failMsg = failMsg + "\n1. sub description title maxlength [Expected]150 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_DESC_SUBTITLE)).getAttribute("maxlength");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void GroupInfo_backgroundImgRadio() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1 + "/label")).getText().contentEquals("현재 이미지")) {
			failMsg = failMsg + "\n1. background image radio button rabel1 [Expected]현재 이미지 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1 + "/label")).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO2 + "/label")).getText().contentEquals("기본 이미지")) {
			failMsg = failMsg + "\n2. background image radio button rabel2 [Expected]기본 이미지 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO2 + "/label")).getText();
		}
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO3 + "/label")).getText().contentEquals("신규 등록")) {
			failMsg = failMsg + "\n3. background image radio button rabel2 [Expected]신규 등록 [Actual]" 
					+ driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO3 + "/label")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void GroupInfo_backgroundCurrent() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)).isEnabled()) {
			failMsg = failMsg + "\n1. background image defalut selected value is not first radiobutton.";
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)).getAttribute("style")
				.contains("/public/customized/custom-user/img/bg-custom-login.jpg")) {
			failMsg = failMsg + "\n2. have to check background image (defalut)";
		}
		
		//신규등록 선택
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO3)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='image-preview-wrapper']//div[@class='text-placeholder']")));
		
		String imgFile = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[3];
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_NEW_INPUT)).sendKeys(imgFile);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='view-area preview preview-new preview-on']")));
		
		//default uri 설정
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(GROUPINFO_TESTURI);
		
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)));
		
		//현재 이미지 확인
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)).getAttribute("style")
				.contains(CommonValues.MEETING_URL + "/storage/accessPage/")) {
			failMsg = failMsg + "\n2. have to check background image (new image)";
		}
		
		//미사용으로 다시 원복
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO2)).click();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void GroupInfo_backgroundDefault() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		//기본으로 다시 원복
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO2)).click();
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_PREVIEW)).getAttribute("style")
				.contains("/public/customized/custom-user/img/bg-custom-login.jpg")) {
			failMsg = failMsg + "\n1. have to check background image (defalut)";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 13, enabled = true)
	public void GroupInfo_backgroundInvalid() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO3)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='image-preview-wrapper']//div[@class='text-placeholder']")));
		
		//invalid file 업로드 시도
		String docFile = CommonValues.TESTFILE_PATH + CommonValues.TESTFILE_LIST[0];
		driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_NEW_INPUT)).sendKeys(docFile);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		
		if(!driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText().contentEquals(MSG_GROUPINFO_ACCESSPAGE_BACKGROUND_INVALIDIMG)) {
			failMsg = failMsg + "\n2. popup msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_BACKGROUND_INVALIDIMG
					+" [Actual]" + driver.findElement(By.xpath(Connect.XPATH_MODAL_RESULTBODY)).getText();
		} 
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		//업로드 실패 후 업로드 화면 다시 나타야함
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='image-preview-wrapper']//div[@class='text-placeholder']")));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void GroupInfo_AccessPageMissUri() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)));
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)).getText().contentEquals(Users2.MSG_MISS_ESSENTIAL)) {
			failMsg = failMsg + "\n1. empty uri msg [Expected]" + Users2.MSG_MISS_ESSENTIAL
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)).getText();
		}
		

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void GroupInfo_AccessPageCheckDup() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys("hellotest");
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)));
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)));
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)).getText().contentEquals(MSG_GROUPINFO_ACCESSPAGE_CHECKURI_ERROR)) {
			failMsg = failMsg + "\n1. do not duplicate check msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_CHECKURI_ERROR
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIERROR)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void GroupInfo_AccessPageCopyUrl() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).clear();
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).sendKeys(GROUPINFO_TESTURI);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//i[@class='rm-icon-copy']")));
		
		driver.findElement(By.xpath("//i[@class='rm-icon-copy']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users2.XPATH_TOAST_CONTENTS)));
		
		if(!driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getAttribute("innerText").contentEquals(MSG_GROUPINFO_ACCESSPAGE_COPYURL)) {
			failMsg = failMsg + "\n1. copyuri toast msg [Expected]" + MSG_GROUPINFO_ACCESSPAGE_COPYURL 
					+ " [Actual]" + driver.findElement(By.xpath(Users2.XPATH_TOAST_CONTENTS)).getAttribute("innerText");
		}
		String clipboardtxt = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		String savedUrl = CommonValues.MEETING_URL + "/rm/" + GROUPINFO_TESTURI;
		if(!clipboardtxt.contentEquals(savedUrl)) {
			failMsg = failMsg + "\n2. clipboard data [Expected]" + savedUrl + " [Actual]" + clipboardtxt;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e =  new Exception(failMsg);
	    	throw e;
		}
	}

	@Test(priority = 17, dependsOnMethods = {"GroupInfo_AccessPageCopyUrl"}, alwaysRun = true, enabled = true)
	public void GroupInfo_AccessPageOldData() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO2)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO1)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)));
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath(XPATH_GROUPINFO_BACKGROUND_IMG_RADIO1)));
		
		if(!driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("value").contentEquals(GROUPINFO_TESTURI)) {
			failMsg = failMsg + "\n1. saved url [Expected]" + GROUPINFO_TESTURI
					+ " [Actual]" + driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_URIINPUT)).getAttribute("value");
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
	
	public void setAccessDefault() {
		driver.findElement(By.xpath(XPATH_GROUPINFO_CUSTOMPAGE_RADIO2)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.findElement(By.xpath(GroupInfo.XPATH_GROUPINFO_SAVE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connect.XPATH_MODAL_RESULTBODY)));
		driver.findElement(By.xpath(Users.XPATH_MODAL_FOOTER_BTN_Y)).click();
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		setAccessDefault();
		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}


