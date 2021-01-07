package mandatory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CommonValues {
	public static final String WEB_CHROME_DRIVER_PATH = System.getProperty("user.dir") + "/driver/chromedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_PATH = System.getProperty("user.dir") + "/driver/geckodriver.exe";
	public static final String WEB_EDGE_DRIVER_PATH = System.getProperty("user.dir") + "/driver/msedgedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_LINUX_PATH = "/tools/webdriver/geckodriver";
	
	public static boolean FOR_JENKINS = true;
	
	public static String MEETING_URL = "https://st.remotemeeting.com";
	
	public static String ADMEMAIL = "rmrsupadm@gmail.com";
	public static String PARTNERKR_EMAIL = "rsupkor@rsupport.com";
	public static String[] USERS = {"rmrsup1@gmail.com"};
	
	public static String USERPW = "!Rsupport0";
	
	public static String ADMIN_URL = "/rmadmin";
	public static String KRHOME_URL = "/ko/home";
	public static String ROOM_URL = "/room/";
	public static String LOUNGE_URL = "/lounge/room-list";
	public static String SCHEDULE_URL = "/lounge/schedule";
	public static String HISTORY_URL = "/lounge/history";
	public static String KRPROFILE_URL = "/ko/setting/profile";
	
	public static String XPATH_HOME_LOGIN_BTN = "//a[@id='login-open-btn']";
	public static String XPATH_HOME_LOGIN_EMAIL = "//input[@name='j_username']";
	public static String XPATH_HOME_LOGIN_PW = "//input[@name='j_password']";
	public static String XPATH_HOME_LOGIN_SUBMIT = "//button[@class='cola-btn size-sm type-full green']";
	
	public static String XPATH_FOOTER_LANG_BTN = "//div[@id='language-selection']";
	public static String XPATH_FOOTER_LANG_KO = "//ul[@class='lang-list']/li[@class='lang-item ko']";
	public static String XPATH_FOOTER_LANG_JA = "//ul[@class='lang-list']/li[@class='lang-item ja']";
	public static String XPATH_FOOTER_LANG_EN = "//ul[@class='lang-list']/li[@class='lang-item en']";
	
	public static String XPATH_HEADER_ADMIN_BTN = "//div[@id='gnb-admin']/a";
	
	public static String XPATH_ROOM_INVITE = "//div[@id='invite-dialog']";
	public static String XPATH_ROOM_INVITEINPUT = "//input[@id='invite-input']";
	public static String XPATH_ROOM_INVITESUBMIT_BTN = "//button[@class='button round green']";
	public static String XPATH_ROOM_INVITECLOSE_BTN = "//section[@id='invite-header']/button/i";
	
	public static String XPATH_SENTLIST_BTN = "//p[@id='sent-list']";
	public static String XPATH_SENTLIST_NAME = "//span[@id='names']";
	
	public static String XPATH_FREECREATE_BTN = "//button[@class='cola-btn active']";
	public static String XPATH_FREECREATE_INPUT = "//input[@id='nickname']";
	public static String XPATH_FREECREATEATTEND_BTN = "//section[@id='gateway']//form/button";
	public static String XPATH_FREECREATE_DIALOG = "//div[@id='dialog']";
	public static String XPATH_FREECREATESUBMIT_BTN = "//button[@class='cola-btn size-md type-full green submit']";

	public static String XPATH_QUICKSTART_BTN = "//button[@id='btn-gnb-create']";
	public static String XPATH_QUICKSTARTTITLE_INPUT = "//input[@name='title']";
	public static String XPATH_QUICKSTARTSTART_BTN = "//button[@class='button round large green']";
	
	public static String XPATH_INVITE_BTN = "//button[@id='invite']";
	public static String XPATH_INVITELIST = "//div[@class='dialog-body']//li";
	public static String XPATH_INVITELISTCONFIRM_BTN = "//button[@class='button round green close']";
	
	public static String XPATH_CAMERA_BTN = "//button[@id='camera']";
	public static String XPATH_MIC_BTN = "//button[@id='mic']";
	
	public static String XPATH_SETTING_BTN = "//button[@id='setting']";
	
	public static String XPATH_EXIT_BTN = "//button[@id='exit']";
	
	public static String XPATH_TIMELINE = "//div[@id='log-content']";
	public static String XPATH_TIMELINE_BTN = "//button[@id='btn-timeline']";
	public static String XPATH_TIMELINE_INPUT = "//textarea[@id='chat-textarea']";
	public static String XPATH_TIMELINESEND_BTN = "//button[@class='send active']";
	
	public static String XPATH_NOTE = "//aside[@id='meeting-note']";
	public static String XPATH_NOTETITLE_INPUT = "//input[@id='note-title']";
	public static String XPATH_NOTESTATE = "//footer[@id='note-state']";
	public static String XPATH_NOTE_BTN = "//button[@id='btn-note']";
	public static String XPATH_NOTESHARE_BTN = "//button[@id='btn-share']";
	public static String XPATH_NOTESHARE = "//div[@id='note-share-wrap']";
	public static String XPATH_NOTESHARE_INPUT = "//input[@id='note-share-input']";
	public static String XPATH_NOTEHISTORY_BTN = "//button[@id='btn-history']";
	
	public static String XPATH_SHARESCREEN_BTN = "//button[@id='screen-share']";
	public static String XPATH_STOPSHARESCREEN_BTN = "//button[@class='screen-share-close button round green large']";
	
	public static String XPATH_TOAST = "//div[@id='msg-box']/p";
	
	public static String TOAST_SENDIVITATION = "초대장을 전송하였습니다.";
	public static String TOAST_NOTESHARE = "회의록을 전송하였습니다.";
	public static String TOAST_STARTSCREENSHARE = "화면 공유가 시작 되었습니다.";
	public static String TOAST_STOPSCREENSHARE = "화면 공유가 종료 되었습니다.";
	
	public static String ADMINNICKNAME = "자동화기업용관리자";
	public static String FREENICKNAME = "FREEGUEST";
	public static String ATTENDEEFREENICKNAME = "FREEATTENDEE";
	
	public static String TESTFILE_PATH = System.getProperty("user.dir") + "\\testdata\\";
	public static String TESTFILE_PATH_MAC = System.getProperty("user.dir") + "/testdata/";
	public static String TESTFILE_LIST[] = {"doc1.docx", "excel.xlsx", "hwp.hwp", "image.png", "image2.jpg", "image3.gif" ,"pdf.pdf", "ppt.pptx", "textdata.txt"};

	public String roomCode = "";
	
	public void setDriverProperty(String browser) {
		String os = System.getProperty("os.name").toLowerCase();
		if (browser.contains("Chrome")) {
			if (os.contains("mac")) {
				 String path = System.getenv("DRIVER_HOME") + "/chromedriver";
				 System.setProperty("webdriver.chrome.driver", path);
			} else {
				System.setProperty("webdriver.chrome.driver", CommonValues.WEB_CHROME_DRIVER_PATH);
			}
		} else if(browser.contains("Edge")) {
			if (os.contains("mac")) {
				 String path = System.getenv("DRIVER_HOME") + "/msedgedriver";
				 System.setProperty("webdriver.edge.driver", path);
			} else if (os.contains("window")) {
				System.setProperty("webdriver.edge.driver", CommonValues.WEB_EDGE_DRIVER_PATH);
			}
		} else {
			System.setProperty("webdriver.gecko.driver", CommonValues.WEB_FIREFOX_DRIVER_PATH);
		}
	}
	
	public WebDriver setDriver(WebDriver driver, String browser, String lang, boolean presenter) {
		if (browser.contains("Chrome")) {
			ChromeOptions options = new ChromeOptions();
		    options.addArguments(lang);
		    //options.addArguments("headless");
		    options.addArguments("disable-gpu");
		    options.addArguments("enable-automation");
		    options.addArguments("--no-sandbox");
		    options.addArguments("--disable-infobars"); 
		    options.addArguments("--disable-dev-shm-usage");
		    options.addArguments("--disable-browser-side-navigation");
		    options.addArguments("--start-maximized");
		    options.addArguments("use-fake-ui-for-media-stream");
		    //dpi조정
		    options.addArguments("force-device-scale-factor=0.75");
		    options.addArguments("high-dpi-support=0.75");
		   
		    if(presenter) {
		    	options.addArguments("auto-select-desktop-capture-source=Entire screen");

			    Map<String, Object> prefs = new HashMap<>();

			    // with this chrome still asks for permission
				prefs.put("profile.managed_default_content_settings.media_stream", 1);
				prefs.put("profile.managed_default_content_settings.media_stream_camera", 1);
				prefs.put("profile.managed_default_content_settings.media_stream_mic", 1);
				prefs.put("profile.default_content_setting_values.notifications", 2);

				// and this prevents chrome from starting
				prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.setting", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.last_used", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.setting", 1);
				prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.last_used", 1);
				
				options.setExperimentalOption("prefs", prefs);
				
		    }
	        driver = new ChromeDriver(options);
		} else if (browser.contains("Edge")) {
			EdgeOptions options = new EdgeOptions();

			//options.addArguments(lang);
		    //options.addArguments("disable-gpu");
		 
		    options.setCapability("dom.webnotifications.enabled", 1);
		    options.setCapability("permissions.default.microphone", 1);
		    options.setCapability("permissions.default.camera", 1);

		    Map<String, Object> prefs = new HashMap<>();
		    // with this chrome still asks for permission
			prefs.put("profile.managed_default_content_settings.media_stream", 1);
			prefs.put("profile.managed_default_content_settings.media_stream_camera", 1);
			prefs.put("profile.managed_default_content_settings.media_stream_mic", 1);

			// and this prevents chrome from starting
			prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.setting", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_mic.https://*,*.last_used", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.setting", 1);
			prefs.put("profile.content_settings.exceptions.media_stream_camera.https://*,*.last_used", 1);
		    
			//options.setExperimentalOption("prefs", prefs);
			
			driver = new EdgeDriver(options);
		} else {
			driver = new FirefoxDriver();
		}
		
		if(browser.contains("_test")){
			FOR_JENKINS = false;
		} else {
			
		}
		
		driver.manage().window().maximize();
		return driver;
	}
	
	public WebDriver setDriver(WebDriver driver, String browser, String lang) {
		driver = setDriver(driver, browser, lang, false);
		return driver;
	}
	
	public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
                WebDriverWait wait = new WebDriverWait(driver, 30);
                wait.until(pageLoadCondition);
	}
	
	public void createFreeMeeting(WebDriver driver, String nickname) throws InterruptedException {	
		driver.findElement(By.xpath(XPATH_FREECREATE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(XPATH_FREECREATE_DIALOG)));
		
		driver.findElement(By.xpath(XPATH_FREECREATE_INPUT)).sendKeys(nickname);
		driver.findElement(By.xpath(XPATH_FREECREATESUBMIT_BTN)).click();
		
		if(isAlertPresent(driver) == true) {
			TimeUnit.SECONDS.sleep(15);
			driver.findElement(By.xpath(XPATH_FREECREATESUBMIT_BTN)).click();
		}
		
		waitForLoad(driver);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_ROOM_INVITEINPUT)));
		driver.findElement(By.xpath(XPATH_ROOM_INVITEINPUT)).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//section[@id='invite-header']/button")).click();
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(XPATH_ROOM_INVITE))));
	}
	
	public String findCode(WebDriver driver) {
		if(driver.findElement(By.xpath(XPATH_ROOM_INVITE)).getAttribute("style").contains("block")) {
			roomCode = driver.findElement(By.xpath("//div[@id='search-wrap']/button[1]")).getText();
			
		}else {
			driver.findElement(By.xpath(XPATH_INVITE_BTN)).click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_ROOM_INVITEINPUT)));
			roomCode = driver.findElement(By.xpath("//div[@id='search-wrap']/button[1]")).getText();
			
		}
		return roomCode;		
	}
	
	public void attendMeeting(WebDriver driver, WebDriver attenddriver) throws InterruptedException {
		roomCode = findCode(driver);
		
		attenddriver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_FREECREATE_BTN)));
		System.out.println(roomCode);
		attenddriver.findElement(By.xpath(XPATH_FREECREATEATTEND_BTN)).click();
		Thread.sleep(2000);
		attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(roomCode);
		Thread.sleep(2000);
		
		if(isAlertPresent(driver) == true) {
			for(int i=0; i<6; i++) {
			attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(Keys.BACK_SPACE); }
			
			attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(roomCode);
		}
		
		attenddriver.findElement(By.xpath(XPATH_FREECREATEATTEND_BTN)).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_FREECREATE_INPUT)));
		
		attenddriver.findElement(By.xpath(XPATH_FREECREATE_INPUT)).sendKeys(ATTENDEEFREENICKNAME);
		attenddriver.findElement(By.xpath("//button[@class='cola-btn size-md type-full green submit']")).click();
		
		waitForLoad(attenddriver);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//section[@id='conference-wrap']")));
	}
	
	public String checkTimeline(WebDriver driver) {

		List<WebElement> TimelineList = driver.findElements(By.xpath("//div[@id='log-content']/ol/li"));
		
		WebDriverWait wait = new WebDriverWait(driver, 10);

		String[] a = new String[TimelineList.size()];
		String b;

		if (TimelineList.size() != 1) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//div[@class='body']/p")));
			for (int i = 0; i < TimelineList.size(); i++) {
				a[i] = TimelineList.get(i).findElement(By.xpath(".//div[@class='body']/p")).getText();

			}
			b = a[a.length - 1];
		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@class='body']/p")));
			b = TimelineList.get(0).findElement(By.xpath(".//div[@class='body']/p")).getText();
		}
		return b;
	}
	
	public void login(WebDriver driver, String ID, String PW) throws Exception {
		
		driver.findElement(By.xpath(XPATH_HOME_LOGIN_BTN)).click();
		
		if(!driver.findElements(By.xpath("//div[@class='header-item form-login']")).isEmpty()) {
			Exception e = new Exception("LoginWindow is not open");
			throw e;
		}
		if(!driver.findElement(By.xpath("//form[@action='/account/login']")).isDisplayed()) {
			Exception e = new Exception("LoginWindow is not display");
			throw e;
		}
		
		driver.findElement(By.xpath(XPATH_HOME_LOGIN_EMAIL)).sendKeys(ID);
		driver.findElement(By.xpath(XPATH_HOME_LOGIN_PW)).sendKeys(PW);
		
		driver.findElement(By.xpath(XPATH_HOME_LOGIN_SUBMIT)).click();
		waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='channel-wrap']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			Exception e = new Exception("Wrong URL :" + driver.getCurrentUrl());
			throw e;
		}
	}
	
	public boolean GetAndCheckToastMsg(WebDriver driver, String ExpectedMsg) {
		WebDriverWait waitMSG = new WebDriverWait(driver, 10);
		waitMSG.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), ExpectedMsg));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(ExpectedMsg)) {
			return false;
		}
		return true;
	}
	
	public boolean isAlertPresent(WebDriver driver) {
	    try 
	    { 
	    	Alert alert =driver.switchTo().alert();
	        alert.accept();
	        return true; 
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        return false; 
	    }   
	}
}


