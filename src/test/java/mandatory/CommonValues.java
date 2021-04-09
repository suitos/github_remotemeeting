package mandatory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonValues {
	public static final String WEB_CHROME_DRIVER_PATH = System.getProperty("user.dir") + "/driver/chromedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_PATH = System.getProperty("user.dir") + "/driver/geckodriver.exe";
	public static final String WEB_EDGE_DRIVER_PATH = System.getProperty("user.dir") + "/driver/msedgedriver.exe";
	public static final String WEB_FIREFOX_DRIVER_LINUX_PATH = "/tools/webdriver/geckodriver";
	
	public static boolean FOR_JENKINS = true;
	public static boolean SPREADSHEET = true;
	
	public static String MEETING_URL_REAL = "https://www.remotemeeting.com";
	public static String MEETING_URL_ST= "https://st.remotemeeting.com";
	public static String MEETING_URL_REAL6= "https://www6.remotemeeting.com";
	public static String MEETING_URL = (System.getProperty("server")==null)||(System.getProperty("server").contains("st"))?MEETING_URL_ST:System.getProperty("server");
	
	public static String ADMEMAIL = "rmrsupadm@gmail.com";
	public static String ADMEMAIL2 = "rmrsup2@gmail.com"; //생성, 삭제용. 
	public static String PARTNERKR_EMAIL = "rsupkor@rsupport.com";
	public static String PARTNERTEST_EMAIL = "rmrsuppartner@gmail.com";
	public static String[] USERS = {"rmrsup1@gmail.com", "rmrsup3@gmail.com", "rmrsup4@gmail.com", "rmrsup5@gmail.com", "rmrsup6@gmail.com", "rmrsup7@gmail.com"};
	public static String ADM_ID = "rsrsup1@gmail.com";
	public static String USER_NOLIC = "rsrsup2@gmail.com";//라이선스 없음
	
	public static String PLANEMAIL = "room01@rsupport.com"; //회의실 요금제용 테스트 계정
	public static String PLANEMAIL2 = "room02@rsupport.com";
	public static String PLANEMAIL3 = "room03@rsupport.com";

	public static String PLANPW = "111111";
	
	//파트너사명 : 자동화테스트용
	//관리자 : rmrsuppartner@gmail.com
	
	//고객사명 : 자동화테스트용
	//관리자 : rmrsupadm@gmail.com
	//사용자 : rmrsup1@gmail.com
	
	//고객사명 : autotest (라이센스 테스트용)
	//관리자 : rmrsup2@gmail.com
	//사용자 : rmrsup3@gmail.com , rmrsup4@gmail.com
	
	//rsupkor 지사 사용자 추가 테스트용 : rmrsup5@gmail.com
	
	//접속페이지 테스트용 고객사명 : 자동화테스트용.function
	//관리자 : rmrsup6@gmail.com
	//사용자 : rmrsup7@gmail.com
	
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
	
	public static String XPATH_GNB_USERBOX_BTN = "//div[@id='gnb-user']/button";
	public static String XPATH_GNB_USERBOX_DROPDOWN_LOGOUT = "//div[@id='gnb-user']/ul[@class='dropdown']/li[2]";
	
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
	
	public static String XPATH_FREECREATE_BTN = "//section[@id='gateway']//div[2]/div/button";
	public static String XPATH_FREECREATE_INPUT = "//input[@id='nickname']";
	public static String XPATH_FREECREATEATTEND_BTN = "//section[@id='gateway']//form/button";
	public static String XPATH_FREECREATE_DIALOG = "//div[@id='dialog']";
	public static String XPATH_FREECREATESUBMIT_BTN = "//button[@class='cola-btn size-md type-full green submit']";
	public static String XPATH_ROOM_LOADER = "//div[@id='loader-bi']";

	public static String XPATH_QUICKSTART_BTN = "//button[@id='btn-gnb-create']";
	public static String XPATH_QUICKSTARTTITLE_INPUT = "//input[@name='title']";
	public static String XPATH_QUICKSTARTSTART_BTN = "//button[@class='button round large green']";
	
	public static String XPATH_INVITE_BTN = "//button[@id='invite']";
	public static String XPATH_INVITELIST = "//div[@class='dialog-body']//li";
	public static String XPATH_INVITELISTCONFIRM_BTN = "//button[@class='button round green close']";
	
	public static String XPATH_CAMERA_BTN = "//button[@id='camera']";
	public static String XPATH_MIC_BTN = "//button[@id='mic']";
	public static String XPATH_REQUESTSPEAK_BTN = "//button[@id='speak-request']";
	
	public static String XPATH_SETTING_BTN = "//button[@id='setting']";
	
	public static String XPATH_EXIT_BTN = "//button[@id='exit']";
	
	public static String XPATH_TIMELINE = "//div[@id='log-content']";
	public static String XPATH_TIMELINE_BTN = "//button[@id='btn-timeline']";
	public static String XPATH_TIMELINE_INPUT = "//textarea[@id='chat-textarea']";
	public static String XPATH_TIMELINESEND_BTN = "//div[@id='chat-input']//button";    
	
	public static String XPATH_NOTE = "//aside[@id='meeting-note']";
	public static String XPATH_NOTETITLE_INPUT = "//input[@id='note-title']";
	public static String XPATH_NOTESTATE = "//footer[@id='note-state']";
	public static String XPATH_NOTE_BTN = "//button[@id='btn-note']";
	public static String XPATH_NOTESHARE_BTN = "//button[@id='btn-share']";
	public static String XPATH_NOTESHARE = "//div[@id='note-share-wrap']";
	public static String XPATH_NOTESHARE_INPUT = "//input[@id='note-share-input']";
	public static String XPATH_NOTEHISTORY_BTN = "//button[@id='btn-history']";
	
	public static String XPATH_RECORD_BTN = "//button[@id='recording']";
	public static String XPATH_LOCK_BTN = "//button[@id='lock']";
	
	public static String XPATH_SHARESCREEN_BTN = "//button[@id='screen-share']";
	public static String XPATH_STOPSHARESCREEN_BTN = "//button[@class='screen-share-close button round green large']";
	
	public static String XPATH_SWITCHMODE_BTN = "//button[@id='switch-mode']";
	public static String XPATH_SWITCHMODE_BOX = "//div[@class='mode-option-dropbox']";
	
	public static String XPATH_ROOM_DOCSHARE_BTN = "//button[@id='doc-share']";
	public static String XPATH_ROOM_DOCUPLOAD_BTN = "//a[@id='doc-upload-btn']";
	public static String XPATH_ROOM_DOCUPLOAD_INPUT = "//input[@id='doc-upload-input']";
	public static String XPATH_ROOM_DOCCONTENT_VIEW = "//article[@id='document-content']";
	public static String XPATH_ROOM_SCREENSHARE_VIEWDESC = "//span[@id='screen-motion']";
	public static String XPATH_RECORDING_BTN = "//button[@id='recording']";
    public static String XPATH_SCREENSHOT_BTN = "//button[@id='screen-shot']";
    public static String XPATH_CROWN_BTN = "//button[@id='speak-right']";
    public static String XPATH_SPEAKLIST_BTN = "//button[@id='speak-list']";
	
    public static String XPATH_SCHEDULE_BTN = "//i[@class='rmicon-calendar']";
	public static String XPATH_HISTORY_BTN = "//i[@class='rmicon-history']";
	
	public static String XPATH_TOAST = "//div[@id='msg-box']/p";
	
	public static String TOAST_SENDIVITATION = "초대장을 전송하였습니다.";
	public static String TOAST_NOTESHARE = "회의록을 전송하였습니다.";
	public static String TOAST_STARTSCREENSHARE = "화면공유가 시작되었습니다.";
	public static String TOAST_STOPSCREENSHARE = "화면공유가 종료되었습니다.";
	public static String TOAST_BLOCK = "회의실이 접근 차단 상태로 변경되었습니다.";
	public static String TOAST_LOCK = "회의실이 잠금 상태로 변경되었습니다.";
	public static String TOAST_CHANGEPW = "비밀번호가 변경 되었습니다.";
	public static String TOAST_UNLOCK = "회의실 잠금이 해제 되었습니다.";
	public static String TOAST_FREELIMIT = "Free 버전 회의에서는 사용하실 수 없습니다.";
	public static String MSG_ATTEND = "[%s] 님이 참여했습니다.";

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
		    options.addArguments("--use-fake-ui-for-media-stream");
		    options.addArguments("--use-fake-device-for-media-stream");
		    //dpi조정
		    options.addArguments("force-device-scale-factor=0.75");
		    options.addArguments("high-dpi-support=0.75");
		   
		    options.addExtensions(new File(CommonValues.TESTFILE_PATH + "meetingcapture.crx"));
		    
		    DesiredCapabilities dc = new DesiredCapabilities();
		    dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		    
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
	
	public void createNormalMeeting(WebDriver driver, String roomTitle) throws InterruptedException {	
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_QUICKSTART_BTN)));
		driver.findElement(By.xpath(XPATH_QUICKSTART_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//section[@id='create-room-dialog']//input[@name='title']")));
		
		driver.findElement(By.xpath("//section[@id='create-room-dialog']//input[@name='title']")).clear();
		driver.findElement(By.xpath("//section[@id='create-room-dialog']//input[@name='title']")).sendKeys(roomTitle);
		driver.findElement(By.xpath("//section[@id='create-room-dialog']//button[@type='submit']")).click();
		
		waitForLoad(driver);
		Thread.sleep(3000);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_ROOM_INVITE)));
			Thread.sleep(3000);
			driver.findElement(By.xpath("//section[@id='invite-header']/button")).click();
			wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(XPATH_ROOM_INVITE))));
		} catch (Exception e) {
			// do not anything
		}
	}
	
	public String findCode(WebDriver driver) throws InterruptedException {
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_INVITE_BTN)));
			driver.findElement(By.xpath(XPATH_INVITE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_ROOM_INVITE)));
			roomCode = driver.findElement(By.xpath("//button[@id='room-code']")).getText();
		} catch (Exception e) {
			roomCode = driver.findElement(By.xpath("//button[@id='room-code']")).getText();
		}
		return roomCode;		
	}
	
	public void attendMeeting(WebDriver driver, WebDriver attenddriver) throws InterruptedException {
		roomCode = findCode(driver);
		
		attenddriver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_FREECREATE_BTN)));
		System.out.println("code : " + roomCode);
		attenddriver.findElement(By.xpath(XPATH_FREECREATEATTEND_BTN)).click();
		Thread.sleep(2000);
		
		for (int i = 0; i < roomCode.length(); i++) {
			attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(roomCode.substring(i, i+1));
	          Thread.sleep(1000);
		}
	          
		attenddriver.findElement(By.xpath(XPATH_FREECREATEATTEND_BTN)).click();
		
		if(isAlertPresent(attenddriver) == true) {
			attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(Keys.CONTROL, "a");
			attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(Keys.BACK_SPACE);
			
			for (int i = 0; i < roomCode.length(); i++) {
				attenddriver.findElement(By.xpath("//section[@id='gateway']//form/input")).sendKeys(roomCode.substring(i, i+1));
		          Thread.sleep(1000);
			}
			
			attenddriver.findElement(By.xpath(XPATH_FREECREATEATTEND_BTN)).click();
			
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_FREECREATESUBMIT_BTN)));
		
		attenddriver.findElement(By.xpath(XPATH_FREECREATE_INPUT)).sendKeys(ATTENDEEFREENICKNAME);
		attenddriver.findElement(By.xpath(XPATH_FREECREATESUBMIT_BTN)).click();
		
		waitForLoad(attenddriver);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//section[@id='conference-wrap']")));
	}
	
	public String checkTimeline(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[@id='log-content']/ol/li"), 0));
		
		List<WebElement> TimelineList = driver.findElements(By.xpath("//div[@id='log-content']/ol/li"));

		String[] a = new String[TimelineList.size()];
		String b;

		if (TimelineList.size() != 1) {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//div[@class='body']/p")));
			for (int i = 0; i < TimelineList.size(); i++) {
				if(!TimelineList.get(i).findElements(By.xpath(".//div[@class='body']/p/img")).isEmpty()) {
					
					a[i] = TimelineList.get(i).findElement(By.xpath(".//div[@class='body']/p/img")).getAttribute("class");
					
				} else {
					
					a[i] = TimelineList.get(i).findElement(By.xpath(".//div[@class='body']/p")).getText();
				}
			}
			b = a[a.length - 1];
		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@class='body']/p")));
			b = TimelineList.get(0).findElement(By.xpath(".//div[@class='body']/p")).getText();
		}
		return b;
	}
	
	public void login(WebDriver driver, String ID, String PW) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_HOME_LOGIN_BTN)));
		
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
		
		if(isAlertPresent2(driver) == false) {
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='channel-wrap']")));
			} catch (Exception e) {
				if(isElementPresent(driver, By.xpath("//div[@class='button-box']/a"))) {
					driver.findElement(By.xpath("//div[@class='button-box']/a")).click();
				}
			}
			
			if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
				Exception e = new Exception("Wrong URL :" + driver.getCurrentUrl());
				throw e;
			}
		} 
	}
	
	public void logout(WebDriver driver) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		if(driver.getCurrentUrl().contains("/home")) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='header-item gnb-user']/button")));
			driver.findElement(By.xpath("//div[@class='header-item gnb-user']/button")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='menu']/li[4]")));
			driver.findElement(By.xpath("//ul[@class='menu']/li[4]")).click();;
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_HOME_LOGIN_BTN)));
		} else {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_GNB_USERBOX_BTN)));
			driver.findElement(By.xpath(XPATH_GNB_USERBOX_BTN)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_GNB_USERBOX_DROPDOWN_LOGOUT)));
			driver.findElement(By.xpath(XPATH_GNB_USERBOX_DROPDOWN_LOGOUT)).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_HOME_LOGIN_BTN)));
		}
	}
	
	public boolean GetAndCheckToastMsg(WebDriver driver, String ExpectedMsg) {
		waitForLoad(driver);
		
		WebDriverWait waitMSG = new WebDriverWait(driver, 15);
		waitMSG.until(ExpectedConditions.textToBe((By.xpath(XPATH_TOAST)), ExpectedMsg));
		waitMSG.until(ExpectedConditions.textToBePresentInElementLocated((By.xpath(XPATH_TOAST)), ExpectedMsg));
		waitMSG.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		
		String msg = driver.findElement(By.xpath(XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(ExpectedMsg)) {
			return false;
		}
		return true;
	}
	
	public boolean isAlertPresent(WebDriver driver) {
	    try 
	    { 
	    	Alert alert = driver.switchTo().alert();
	        alert.accept();
	        return true; 
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        return false; 
	    }   
	}
	
	public boolean isAlertPresent2(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isElementPresent(WebDriver wd, By by) {
		try {
			wd.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public String JPGpath(String filename) {
		String os = System.getProperty("os.name").toLowerCase();
		String path = "";
		
		String home = System.getProperty("user.home");
		if (os.contains("windows")) {
			
			path = home + "\\Downloads\\" + filename;
			
		} else {
			path = home + "/Downloads/" + filename ;
			System.out.println(path);
		}
		return path;
	}
	
	public void deleteJPGFile(String filepath) throws Exception {
		
	    File file = new File(filepath);

	    try {
	        if (file.exists()) {
	            file.delete();
	            System.out.println("delete file : " + filepath);
	        } else {
	        	Exception e = new Exception("File is not exist");
	        	e.printStackTrace(); 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}
	
	public enum filetype{
		JPG, PNG, GIF, DOC, DOCX, PPT, PPTX, XLSX, HWP
	}
	public void ShareDocument(WebDriver webdriver, filetype type) {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = "";
		
		switch (type) {
		case DOCX:
			addedfile = filePath + TESTFILE_LIST[0];
			break;
		case XLSX:
			addedfile = filePath + TESTFILE_LIST[1];
			break;
		case HWP:
			addedfile = filePath + TESTFILE_LIST[2];
			break;
		case PNG:
			addedfile = filePath + TESTFILE_LIST[3];
			break;
		case JPG:
			addedfile = filePath + TESTFILE_LIST[4];
			break;
		case PPTX:
			addedfile = filePath + TESTFILE_LIST[7];
			break;

		default:
			addedfile = filePath + TESTFILE_LIST[4];
			break;
		}
		webdriver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
	}
	
	public void attendRoomLoginUser(WebDriver wd, String code) {
		if(!wd.getCurrentUrl().contains(MEETING_URL + LOUNGE_URL)) {
			wd.get(MEETING_URL + LOUNGE_URL);
		}
		
		WebDriverWait wait = new WebDriverWait(wd, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='gnb-conference']")));	
		wd.findElement(By.xpath("//div[@id='gnb-conference']//input")).sendKeys(code);
		wd.findElement(By.xpath("//div[@id='gnb-conference']//button")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_EXIT_BTN)));
	}
	
	public Boolean checkDisplay(WebDriver driver, By by) {
		try {
			return driver.findElement(by).isDisplayed();
		} catch (NoSuchElementException ignored) {
			return false;
		} catch (StaleElementReferenceException ignored) {
			return false;
		}
	}

}


