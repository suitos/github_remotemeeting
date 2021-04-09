package mandatory;

import static org.testng.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*
 * 1.리모트미팅 홈 로그인 선택 및 로그인
 * 2.프로필 설정 선택 후 화면 이동 확인
 * 3.사진 불러오기 후 등록 팝업 확인
 * 4.라운지 선택 후 룸 리스트 화면 확인
 * 5.회의 시작 선택 후 빠른 시작 팝업 확인
 * 6.제목 입력 후 회의 시작하기 선택 후 회의 생성 확인
 * 7.이메일 입력 후 초대 메일 전송 후 토스트 메세지 확인
 * 8.초대 발송 내역 내 전송성공 문구 확인
 * 9.환경설정 클릭 후 팝업 확인 및  취소 클릭 시 환경설정 팝업 닫힘 확인
 * 10.마이크 변경 후 환경설정 팝업 내 변경된 마이크 확인 
 * 11.스피커 변경 후 환경설정 팝업 내 변경된 스피커 확인
 * 12.참석자 참석 및 토스트 메세지 확인
 * 13.새로고침 후 룸 재입장 확인(pip로 확인)
 * 14.회의실 잠금 후 로그 확인
 * 15.비밀번호 설정하기 클릭 후 로그 확인
 * 16.참여자 접근 차단하기 클릭 후 로그 확인
 * 17.회의실 잠금 off후 로그 확인
 * 18.Shift+Enter 클릭 후 회의록 슬라이딩 확인
 * 19.AI기록 OFF버튼 클릭하여 ON하기
 * 20.AI기록 ON버튼 클릭하여 OFF하기 및 기록 종료 팝업 확인
 * 21.회의록 수동기록 탭 선택 후 주제 입력 후 저장 문구 확인
 * 22.회의록 공유 선택 후 팝업 확인
 * 23.회의록 전송 후 토스트 메세지 확인
 * 24.이전 회의록 가져오기 클릭 후 팝업 확인
 * 25.회의록 같이 보기 선택 후 토스트 메세지 확인
 * 26.화면 임의 위치 더블 클릭 후 회의록 닫힘 확인
 * 27.카메라 ON/OFF 경우 로그 확인
 * 28.MIC ON/OFF 경우 로그 확인
 * 29.이미지 파일 문서 공유 후 로그 확인
 * 30.텍스트 파일 문서 공유 후 로그 확인
 * 31.화면공유 후 로그 확인
 * 32.화면 공유 중 PIP로 아이콘 확인
 * 33.화면 공유 중지
 * 34.녹화 선택 후 팝업 확인
 * 35.타임라인에 메시지 입력 후 전송 클릭 및 로그 확인	
 * 36.화면 임의 위치 더블 클릭 후 타임라인 닫힘 확인
 * 37.모드 변경 선택
 * 38.분할 모드 선택 후 로그 확인
 * 39.강조 모드 선택 후 로그 확인
 * 40.주화자 모드 선택 후 로그 확인
 * 41.사회자 모드 선택 후 토스트 메세지 확인
 * 42.전체 마이크 ON/OFF경우 로그 확인
 * 43.전체 카메라 ON/OFF경우 로그 확인
 * 44.참여자 리스트 선택 후 참여자명 확인
 * 45.PIP분리 확인
 * 46.PIP보기 확인
 * 47.PIP고정 확인
 * 48.스크린샷 클릭 후 타임라인창 열림 확인
 * 49.스크린샷 다운로드 확인
 * 50.스크린샷 저장 후 문구 확인
 * 51.스크린샷 타임라인으로 저장 후 확인
 * 52.타임라인에 스크린샷 삭제
 * 53.녹화 중 녹화 선택 후 녹화 종료 토스트 메세지 확인
 * 54.PIP숨기기 확인
 * 55.회의 종료
 * 56.스케쥴 선택 후 화면 이동 확인
 * 57.플러스 버튼 선택 후 예약 추가 팝업 확인 및 등록
 * 58.예약 삭제 팝업 및 삭제
 * 59.히스토리 클릭 후 화면 표시 확인
 * 60.히스토리 자세히 보기 클릭 및 팝업 확인
 * 61.히스토리 공유 클릭 및 팝업 확인 및 토스트 메세지 확인
 * 62.스크린캡쳐 자세히 보기 클릭 후 팝업 확인
 * 63.녹화 재생 버튼 클릭 후 재생 팝업 확인
 * 64.별 선택 후 중요탭에서 해당 히스토리 확인
 * 65.히스토리 삭제 확인
 */

public class P2PEnterprise {
	
	public static String XPATH_PROFILE_BTN = "//button[@id='btn-user']";
	public static String XPATH_LOUNGE_BTN = "//div[@class='header-item btn-lounge']";

	public static String HREF_PROFILE = "//a[@href='/ko/setting/profile']";
	
	public static String XPATH_SCREENSHOT_BTN = "//button[@id='screen-shot']";
	public static String XPATH_SPEAKRIGHT_BTN = "//button[@id='speak-right']";
	public static String XPATH_SPEAKLIST_BTN = "//button[@id='speak-list']";
	public static String XPATH_PIP_BTN = "//button[@id='pip-hide-btn']";
	
	public static String TOAST_TOGETHERNOTE = "다른 참여자들의 회의록을 펼쳤습니다.";
	
	public static String TOAST_TILELAYOUT = "%s님이 분할 모드로 변경했습니다.";
	public static String TOAST_FOCUSLAYOUT = "%s님이 강조 모드로 변경했습니다.";
	public static String TOAST_ACTIVESPEACERLAYOUT = "%s님이 주화자 모드로 변경했습니다.";
	
	public static String TOAST_SPEAKRIGHT = "%s님이 사회자 권한을 선택하였습니다.";
	public static String TOAST_ALLMICOFF = "전체 마이크가 OFF 되었습니다.";
	public static String TOAST_ALLMICON = "전체 마이크가 ON 되었습니다.";
	public static String TOAST_ALLCAMOFF = "전체 카메라가 OFF 되었습니다.";
	public static String TOAST_ALLCAMON = "전체 카메라가 ON 되었습니다.";
	
	public static String TOAST_CAMERA_OFF = "카메라가 꺼졌습니다. 상대방에게 영상이 보여지지 않습니다.";
	public static String TOAST_CAMERA_ON = "카메라가 켜졌습니다.";
	public static String TOAST_AUDIO_OFF = "마이크가 꺼졌습니다. 상대방에게 소리가 들리지 않습니다.";
	public static String TOAST_AUDIO_ON = "마이크가 켜졌습니다.";
	
	public static String TOAST_PIPFIX = "주화면을 고정했습니다. 화면 고정을 해제하시려면 고정된 PIP를 한번 더 선택해주세요.";
	public static String TOAST_RECORD = "녹화가 시작되었습니다.";
	public static String TOAST_STOPRECORD = "녹화가 종료되었습니다.";
	public static String TOAST_SAVE = "일정이 저장되었습니다.";
	public static String TOAST_DELETE = "삭제가 완료되었습니다.";
	public static String TOAST_SUCCESS = "정상 처리 되었습니다.";	
	public static String TOAST_DOWNLOAD = "Windows에서 다운로드한 이미지가 열리지 않는 경우 다른 이미지 뷰어를 사용해주세요.";
	
	public static String TOOLTIP_AI = "현재 AI 회의 기록이 OFF 상태입니다.\n상단의 AI 버튼을 ON으로 해주세요.\n"
			+ "(50인 이하에서만 사용 가능합니다.)";
	
	public static String MSG_NOTESTATE = "모든 변경사항이 HISTORY에 저장됨.";
	public static String MSG_SAVETIMELINE = "타임라인으로 저장하시겠습니까?";
	
	public static String CONTENTWRAP_TXT = "녹화를 시작하시겠습니까? 녹화된 영상은 히스토리 메뉴에서 확인할 수 있습니다.";
	public static String CONTENTWRAP_TXT2 = "사회자 모드는 회의 참여자가 많을 경우 원활한 회의 진행을 위해 발언권을 조정하는 기능을 제공합니다.\n" + 
											"사회자 모드를 활성화하면 %s님이 회의 조정자가 됩니다.\n" + 
											"사회자 모드를 활성화 하시겠습니까?";
	
	private static String RoomTitle = "P2PEnterprise";
	
	public String roomID = "";
	
	public static WebDriver driver;
	public static WebDriver Attenddriver;
	
	public String[] user1 = {CommonValues.ADMEMAIL, CommonValues.ADMINNICKNAME};
	public String[] user2 = {CommonValues.USERS[0], CommonValues.USERS[0].replace("@gmail.com", "")};
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		if(CommonValues.MEETING_URL.contentEquals(CommonValues.MEETING_URL_REAL6)) {
			user1[0] = "auto1@rsupport.com";
			user1[1] = "자동화1";
			user2[0] = "auto2@rsupport.com";
			user2[1] = "자동화2";
		} 
		
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		Attenddriver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);
		context.setAttribute("webDriver2", Attenddriver);


	}
	
	@Test(priority = 1)
	public void Login() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		Attenddriver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, user1[0], CommonValues.USERPW);
		comm.login(Attenddriver, user2[0], CommonValues.USERPW);
	}
	
	@Test(priority = 2)
	public void Profile() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_PROFILE_BTN)).click();
		
		if(driver.findElements(By.xpath("//div[@class='header-item is-open']")).isEmpty()) {
			failMsg = "ProfileWindow is not open";
		}
		
		driver.findElement(By.xpath(HREF_PROFILE)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-inner-wrap']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.KRPROFILE_URL)) {
			failMsg = "Can't enter Profile [Expected]" + CommonValues.MEETING_URL + CommonValues.KRPROFILE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3)
	public void Profile_Setting() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='image-type-upload']")));

		driver.findElement(By.xpath("//label[@for='image-type-upload']")).click();
		
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[4];
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(addedfile);
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@class='file-name']"), CommonValues.TESTFILE_LIST[4]));
		wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.xpath("//div[@id='photo']"), "style", "profile-default-person.png")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='button-box footer']/button[1]")));
		driver.findElement(By.xpath("//div[@class='button-box footer']/button[1]")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		
		if(!alertText.contentEquals("정상적으로 등록되었습니다.")) {
			failMsg = "Alert is wrong [Expected] 정상적으로 등록되었습니다. [Actual]" + alertText;
		}
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//fieldset/div[2]/div[1]"), "class", "form-item-wrap original radio-wrap"));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4)
	public void GoLounge() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_LOUNGE_BTN)));
		
		driver.findElement(By.xpath(XPATH_LOUNGE_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='channel-wrap']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "Can't enter lounge [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL 
					+" [Actual]" + driver.getCurrentUrl();
		}
		
		List <WebElement> roomlist = driver.findElements(By.xpath("//div[@class='channel-wrap']"));
		
		if(roomlist.size() != 99 && roomlist.isEmpty()) {
			failMsg = "Room list is empty";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5)
	public void QuickStart() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Quick start popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void QuickStart2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTTITLE_INPUT)).sendKeys(RoomTitle);
		driver.findElement(By.xpath(CommonValues.XPATH_QUICKSTARTSTART_BTN)).click();
		
		comm.waitForLoad(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		//wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='device-setting-notification-box']")));
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "1.Can't enter Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void Enterprise_Invite() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).isDisplayed()) {
			driver.findElement(By.xpath(CommonValues.XPATH_INVITE_BTN)).click();
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITESUBMIT_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_SENDIVITATION)) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + CommonValues.TOAST_SENDIVITATION
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if(!CommonValues.USERS[0].contains(driver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_NAME)).getText())) {
			failMsg = "1.Wrong Sent list";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void Enterprise_Invite2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_INVITELIST)));
		
		if(!CommonValues.USERS[0].contains((driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/strong")).getText()))) {
			failMsg = "1.Wrong Sent list name";
		}
		
		if(!CommonValues.USERS[0].contains((driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/span[1]")).getText()))) {
			failMsg = failMsg + "\n2.Wrong Sent list email";
		}
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_INVITELIST + "/span[2]")).getText().contentEquals("전송성공")) {
			failMsg = failMsg + "\n3.Failed to Sent email";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_INVITELISTCONFIRM_BTN)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITECLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 9, enabled = true)
	public void Enterprise_Setting() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Setting popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='button round gray close']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElements(By.xpath("//div[@class='content-wrap']")).isEmpty()) {
			failMsg = "\n2.Setting popup is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void Enterprise_SettingMIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		//설정팝업 마이크,스피커 다름 warning 뜨면 클릭해서 없애기
		if(driver.findElement(By.xpath("//div[@class='device-title']/em")).isDisplayed())
			driver.findElement(By.xpath("//div[@class='device-title']/em")).click();
		
		driver.findElement(By.xpath("//div[@id='mic-list']//select")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='mic-list']//select/option[2]")));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='mic-list']//select/option[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		
		comm.waitForLoad(driver);
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loader-bi']")));
		Thread.sleep(5000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@id='mic-list']//select/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Don't selected";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void Enterprise_SettingSpeaker() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		driver.findElement(By.xpath("//select[@id='speakers']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='speakers']/option[2]")));
		Thread.sleep(1000);
		driver.findElement(By.xpath("//select[@id='speakers']/option[2]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		
		comm.waitForLoad(driver);
		
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='loader-bi']")));
		Thread.sleep(5000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//select[@id='speakers']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Don't selected";
		}
		
		driver.findElement(By.xpath("//button[@class='button round gray close']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void Enterprise_Attend() throws Exception {
		String failMsg = "";
		
		//comm.attendMeeting(driver, Attenddriver);
		String code = comm.findCode(driver);
		comm.attendRoomLoginUser(Attenddriver, code);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		comm.waitForLoad(driver);
	
		if (!Attenddriver.getCurrentUrl().contentEquals((CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID))) {
			failMsg = "1.Attendee can't entered Free Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + Attenddriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 13, enabled = true)
	public void Enterprise_Refresh() throws Exception {
		String failMsg = "";
		
		String url = driver.getCurrentUrl();
		driver.navigate().refresh();
		
		comm.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.attributeContains(By.xpath("//div[@id='loader-bi']"), "style", "display: none;"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pip-container']")));
		
		if (!driver.getCurrentUrl().contentEquals((url))) {
			failMsg = "1.Attendee can't entered Free Room [Expected]" + url
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void LockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath("//button[@id='lock']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='lock-drop-box']")));
		
		if(!driver.findElement(By.xpath("//div[@id='lock-drop-box']")).isDisplayed()) {
			failMsg = "1.Lock drop box is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='on']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_BLOCK)) {
			failMsg = failMsg + "\n2.Wrong Lock MSG [Expected]" + CommonValues.TOAST_BLOCK
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.TOAST_BLOCK)) {
			failMsg = failMsg + "\n3.Wrong Timeline [Expected]" + CommonValues.TOAST_BLOCK + " [Actual]" + timeline;
		}
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void SetRoomPW() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@id='set-password']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_LOCK)) {
			failMsg = failMsg + "\n1.Wrong Lock MSG [Expected]" + CommonValues.TOAST_LOCK
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.TOAST_LOCK)) {
			failMsg = "\n2.Wrong Timeline [Expcted]" + CommonValues.TOAST_LOCK + " [Actual]" + timeline;
		}
		
		driver.findElement(By.xpath("//button[@id='reload-password']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_CHANGEPW)) {
			failMsg = failMsg + "\n3. Wrong ChangePW MSG [Expected]" + CommonValues.TOAST_CHANGEPW
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void BlockAccessRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@id='block-access']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(CommonValues.TOAST_BLOCK)) {
			failMsg = failMsg + "\n1.Wrong Lock MSG [Expected]" + CommonValues.TOAST_BLOCK
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String timeline = comm.checkTimeline(driver);

		if(!timeline.contentEquals(CommonValues.TOAST_BLOCK)) {
			failMsg = failMsg + "\2. Wrong Timeline [Expcted]" + CommonValues.TOAST_BLOCK + " [Actual]" + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void UnlockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='off active']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(CommonValues.TOAST_UNLOCK)) {
			failMsg = failMsg + "\n1.Wrong Unlock MSG [Expected]" + CommonValues.TOAST_UNLOCK
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(CommonValues.TOAST_UNLOCK)) {
			failMsg = failMsg + "\n2.Wrong Timeline [Expcted]" + CommonValues.TOAST_UNLOCK + " [Actual]" + timeline;
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void Enterprise_note() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER).keyUp(Keys.SHIFT).perform();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contentEquals("active") ) {
			failMsg = failMsg + "\n1. Inactive Note";
		}
		
		if(!driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed() ) {
			failMsg = failMsg + "\n2.Don't Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void TurnOn_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='off']")).click();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("on") ) {
			failMsg = failMsg + "\n1.AI don't turn ON";
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='on']")));
		Thread.sleep(500);
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void TurnOff_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='on']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n1. AI OFF popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")));
		} catch (Exception e) {
			// do not anything
		}
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("off") ) {
			failMsg = failMsg + "\n2.AI don't turn OFF";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).isDisplayed()) {
			failMsg = failMsg + "\n3.AI tooltip is not display";
		}
		
		String tooltip = driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).getText();
		if(!tooltip.contentEquals(TOOLTIP_AI)) {
			failMsg = failMsg + "\n4.AI tooltip text is wrong [Expected]" + TOOLTIP_AI 
					+ " [Actual]" + tooltip;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21,  enabled = true)
	public void NoteManual() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@data-menu='manual']")).click();
		
		if(!driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']")).getAttribute("data-selected").contentEquals("manual") ) {
			failMsg = failMsg + "\n1.Manual Tab inactive";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)).sendKeys("AUTOTEST");
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)), MSG_NOTESTATE));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)).getText().contentEquals(MSG_NOTESTATE)) {
			failMsg = failMsg + "\n2.Wrong MSG [Expected]" + MSG_NOTESTATE
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_NOTESTATE)).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22,  enabled = true)
	public void Enterprise_Sharenote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTESHARE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE)).isDisplayed() ) {
			failMsg = "\n1.Inactive ShareNote";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, dependsOnMethods = {"Enterprise_Sharenote"},   enabled = true)
	public void Enterprise_Sendnote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE + "//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_NOTESHARE)) {
			failMsg = "\n1. Wrong SendNote MSG [Expected]" + CommonValues.TOAST_NOTESHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 24, dependsOnMethods = {"Enterprise_Sendnote"}, alwaysRun = true,   enabled = true)
	public void Enterprise_Historynote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTEHISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "\n1. History note popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='dialog-close']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 25, dependsOnMethods = {"Enterprise_Sharenote"}, alwaysRun = true,  enabled = true)
	public void Enterprise_Togethernote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='btn-see-togather']")).click();
	
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(TOAST_TOGETHERNOTE)) {
			failMsg = "\n1. Wrong Toast MSG [Expected]" + TOAST_TOGETHERNOTE
					+ " [Actual]" +msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 26, dependsOnMethods = {"Enterprise_Sharenote"}, alwaysRun = true,  enabled = true)
	public void Enterprise_Closenote() throws Exception {
		String failMsg = "";
		
		Actions actions = new Actions (driver);
		WebElement background = driver.findElement(By.id("local-video"));
		actions.doubleClick(background).perform ();
		Thread.sleep(1000);
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("float-note") ) {
			failMsg = "\n1. active Note";
		}
		
		if(driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed()) {
			failMsg = failMsg + "\n2.Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 27,  enabled = true)
	public void Enterprise_CheckTimeline_Camera() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		//toast 확인하도록 수정(타임라인 미확인)
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_CAMERA_OFF)) {
			failMsg = "\n1. camera off toast. [Expected]" + TOAST_CAMERA_OFF
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//camera on
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_CAMERA_ON)) {
			failMsg = "\n2. camera on toast. [Expected]" + TOAST_CAMERA_ON
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		/*
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(user1[1] + "님이 카메라를 껐습니다.")) {
			failMsg = "\n1. Wrong Timeline : " + timeline;
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(user1[1] + "님이 카메라를 켰습니다.")) {
			failMsg = failMsg + "\n 2.Wrong Timeline : " + timeline;
		}
		*/
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 28,  enabled = true)
	public void Enterprise_CheckTimeline_MIC() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		//toast 확인하도록 수정(타임라인 미확인)
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if (!msg.contentEquals(TOAST_AUDIO_OFF)) {
			failMsg = "\n1. audio off toast. [Expected]" + TOAST_AUDIO_OFF + " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		
		//on
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if (!msg.contentEquals(TOAST_AUDIO_ON)) {
			failMsg = "\n1. audio on toast. [Expected]" + TOAST_AUDIO_ON + " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		/*
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(user1[1] + "님이 마이크를 음소거했습니다.")) {
			failMsg = "\n1. Wrong Timeline : " + timeline;
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(user1[1] + "님이 마이크 음소거를 해제했습니다.")) {
			failMsg = failMsg + "\n2.Wrong Timeline : " + timeline;
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		*/
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 29,  enabled = true)
	public void Enterprise_ShareDOC_jpg() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,4,1);
		
		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "\n1. Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		//click timeline (to shown)
		if(!driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[4] + "를 공유했습니다.")) {
			failMsg = failMsg + "\n2. Wrong Timeline : " + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 30,  enabled = true)
	public void Enterprise_ShareDOC_txt() throws Exception {
		String failMsg = "";
		
		ShareDocument(driver,8,2);

		if(!driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src").contains("remotemeeting.com")) {
			failMsg = "\n1. Share file failed" + driver.findElement(By.xpath("//article[@id='document-content']/img")).getAttribute("src");
		}
		
		// click timeline (to shown)
		if (!driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(!timeline.contentEquals(user1[1] + "님이 " + CommonValues.TESTFILE_LIST[8] + "를 공유했습니다.")) {
			failMsg = failMsg + "Wrong Timeline : " + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 31,  enabled = true)
	public void Enterprise_StartShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)) {
			failMsg = failMsg + "\n1. Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 32, dependsOnMethods = {"Enterprise_StartShareScreen"},  enabled = true)
	public void Enterprise_CheckShareScreenIcon() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath("//div[@id='local-video-pip']//span[@class='overlap screen screen-share']")).isDisplayed()) {
			failMsg = "\n1. ShareScreen Icon is not displayed in local pip";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 33,  enabled = true)
	public void Enterprise_StopShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)) {
			failMsg = failMsg + "Wrong Screen Share MSG [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 34,  enabled = true)
	public void Record() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "\n1. Recording popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']//p")).getText().contentEquals(CONTENTWRAP_TXT)) {
			failMsg = failMsg + "\n2.Wrong TXT [Expected]" + CONTENTWRAP_TXT
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']")).getText();
		}
		
		driver.findElement(By.xpath("//div[@class='buttons align-center']/button[1]")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_RECORD)) {
			failMsg = "Wrong Record MSG [Expected]" + TOAST_RECORD
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//녹화 시 참석자 화면에 14일무료체험 가입 팝업 노출됨경우에만 클릭
		try {
			if(Attenddriver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
				Attenddriver.findElement(By.xpath("//button[@id='dialog-close']")).click();
			}
		} catch (Exception e) {
			//Do not anything
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 35,  enabled = true)
	public void Enterprise_SendTxt_Timeline() throws Exception {
		String failMsg = "";
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE)).isDisplayed()) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)).sendKeys("TEST");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_TIMELINESEND_BTN)));
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINESEND_BTN)).click();
		
		String timeline = comm.checkTimeline(driver);
		System.out.println(timeline);
		if(!timeline.contentEquals("TEST")) {
			failMsg = "Wrong Timeline";
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 36,  enabled = true)
	public void Enterprise_CloseTimeline() throws Exception {
		String failMsg = "";
		
		Actions actions = new Actions (driver);
		WebElement background = driver.findElement(By.id("local-video"));
		actions.doubleClick(background).perform ();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if(driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE)).isDisplayed()) {
			failMsg = "Timeline is display";
		}
	
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 37,  enabled = true)
	public void Enterprise_selectMode() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		driver.findElement(By.xpath("//button[@id='switch-mode']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='mode-option-dropbox']")));
		
		if(!driver.findElement(By.xpath("//div[@class='mode-option-dropbox']")).isDisplayed()) {
			failMsg = "";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 38,  enabled = true)
	public void Enterprise_selectMode_tileLayout() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@class='tileLayout-option option default']")).click();
		
		driver.findElement(By.xpath("//button[@id='apply-layout']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(String.format(TOAST_TILELAYOUT, user1[1]))) {
			failMsg = "Wrong Timeline [Expected]" + String.format(TOAST_TILELAYOUT, user1[1]) + " [Actual]" + timeline;
		}
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 39,  enabled = true)
	public void Enterprise_selectMode_focusLayout() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='switch-mode']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='mode-option-dropbox']")));
		
		driver.findElement(By.xpath("//div[@class='focusLayout-option option default']")).click();
		
		driver.findElement(By.xpath("//button[@id='apply-layout']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(String.format(TOAST_FOCUSLAYOUT, user1[1]))) {
			failMsg = "Wrong Timeline [Expected]" + String.format(TOAST_FOCUSLAYOUT, user1[1]) + " [Actual]" + timeline;
		}
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='msg-box']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 40,  enabled = true)
	public void Enterprise_selectMode_activespeakerLayout() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='switch-mode']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='mode-option-dropbox']")));
		
		driver.findElement(By.xpath("//div[@class='activeSpeakerLayout-option option default']")).click();
		
		driver.findElement(By.xpath("//button[@id='apply-layout']")).click();
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		} catch (Exception e) {
			// do not anything
		}
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(String.format(TOAST_ACTIVESPEACERLAYOUT, user1[1]))) {
			failMsg = "Wrong Timeline [Expected]" + String.format(TOAST_ACTIVESPEACERLAYOUT, user1[1]) + " [Actual]" + timeline;
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_TIMELINE_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 41,  enabled = true)
	public void Enterprise_SpeakRight() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n1. SpeakRight popup is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).getText().contains(String.format(CONTENTWRAP_TXT2, user1[1]))) {
			failMsg = failMsg + "\n2.Wrong TXT [Expected]" + String.format(CONTENTWRAP_TXT2, user1[1])
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']")).getText();
		}
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();	
		if(!msg.contentEquals(String.format(TOAST_SPEAKRIGHT, user1[1]))) {
			failMsg = failMsg + "\n3.Wrong Speakright MSG [Expected]" + String.format(TOAST_SPEAKRIGHT, user1[1])
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 42,  enabled = true)
	public void Enterprise_SpeakRight_MIC() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='all-mic-off']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();	
		if(!msg.contentEquals(TOAST_ALLMICOFF)) {
			failMsg = "\n1. Wrong Speakright MSG [Expected]" + TOAST_ALLMICOFF
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		driver.findElement(By.xpath("//button[@id='all-mic-on']")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_ALLMICON)) {
			failMsg = failMsg + "\n2. Wrong Speakright MSG [Expected]" + TOAST_ALLMICON
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 43,  enabled = true)
	public void Enterprise_SpeakRight_CAM() throws Exception {
		String failMsg = "";
		
		// click timeline (to shown)
		if (!driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		//전체 마이크/카메라 선택 화살표 클릭
		driver.findElement(By.xpath("//div[@class='control-mic-all']//button[@class='arrow-wrapper']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='control-select-box']")));
		//전체 카메라 클릭
		driver.findElement(By.xpath("//li[@class='label camera select']")).click();
		
		//전체 off 클릭
		driver.findElement(By.xpath("//button[@id='all-mic-off']")).click();
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(TOAST_ALLCAMOFF)) {
			failMsg = failMsg + "\n3.Wrong Timeline [Expected]" + TOAST_ALLCAMOFF + " [Actual]" + timeline;
		}
		
		//전체 on 클릭
		driver.findElement(By.xpath("//button[@id='all-mic-on']")).click();
		
		timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals(TOAST_ALLCAMON)) {
			failMsg = failMsg + "\n3.Wrong Timeline [Expected]" + TOAST_ALLCAMON + " [Actual]" + timeline;
		}
		
		// 사회자 모드일경우 사회자 모드 해제
		if (driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).click();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 44,  enabled = true)
	public void Enterprise_SpeakList() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SPEAKLIST_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='participants']")));
		
		List <WebElement> attendelist = driver.findElements(By.xpath("//span[@class='attendee-name']"));
		
		List<String> speakers = new ArrayList<>();
		for (WebElement webElement : attendelist) {
			speakers.add(webElement.getText().replace("(나) ", ""));
		}
		
		if (!speakers.contains(user1[1])
				|| !speakers.contains(user2[1])) {
			failMsg = "\n1.Wrong SpeakList [Expected]" + user1[1] + "," + user2[1] 
					+ " [Actual]" + speakers.get(0) + "," + speakers.get(1);
		}
		
		//참석자 리스트 다시 클릭해서 팝업 안보이게 함
		try {
			driver.findElement(By.xpath(XPATH_SPEAKLIST_BTN)).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='participants']")));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 45,  enabled = true)
	public void Enterprise_PIPExport() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if(driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		//내 pip export 상태면 원복
		if(!driver.findElement(By.xpath("//div[@id='local-video-pip']//i[@class='icon export export-pip']")).getAttribute("class").contains("pip-exported")){
			if(!driver.findElement(By.xpath("//div[@id='local-video-pip']//i[@class='icon export export-pip']")).isEnabled()) {
				failMsg  = failMsg + "\n1. disabled export pip button.";
			}
		} else {
			failMsg = failMsg + "\n2. pip is already exported.";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 46,  enabled = true)
	public void Enterprise_PIPView() throws Exception {
		String failMsg = "";
		
		//올려져 있으면 내리기
		if(driver.findElement(By.xpath("//div[@id='pip-container']")).getAttribute("style").contains("bottom: 0px")) {
			driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
			Thread.sleep(1000);
		} 
		
		driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
		
		Thread.sleep(500);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='wrap-pip-layout']")));
		
		if(!driver.findElement(By.xpath("//div[@id='pip-container']")).getAttribute("style").contains("bottom: 0px")) {
			failMsg = failMsg + "\n1.Don't display PIP";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
		
	}
	
	@Test(priority = 47,  enabled = true)
	public void Enterprise_PIPFix() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//사회자 모드일경우 사회자 모드 해제
		if(driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).getAttribute("class").contains("active")) {
			driver.findElement(By.xpath(XPATH_SPEAKRIGHT_BTN)).click();
		}
		
		//pip 영역 중 1개 전체 프레임 클릭
		driver.findElement(By.xpath("//div[@class='wrap-pip-layout']/div[1]")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_PIPFIX)) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + TOAST_PIPFIX
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 48,  enabled = true)
	public void Enterprise_Screenshot() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_SCREENSHOT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='shot-count']")));
		wait.until(ExpectedConditions.attributeContains(By.xpath("//aside[@id='timeline']"), "class", "active"));
		
		if(!driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class").contentEquals("active show-screen-shot")) {
			failMsg = "Timeline don't opened" + driver.findElement(By.xpath("//aside[@id='timeline']")).getAttribute("class");
		}
		
		if(!driver.findElement(By.xpath("//div[@class='img-wrap']")).isDisplayed()) {
			failMsg = failMsg + "\n2.Screenshot is not displayed in Timeline";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 49,  enabled = true)
	public void Enterprise_checkScreenshotDownloadMsg() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//i[@class='icon download']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_DOWNLOAD)) {
			failMsg = "Wrong DownloadJPG MSG [Expected]" + TOAST_DOWNLOAD
					+ " [Actual]" + driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		String filename = driver.findElement(By.xpath("//a[@title='다운로드']")).getAttribute("download");
		System.out.println(filename);
		comm.JPGpath(filename);
		
		TimeUnit.SECONDS.sleep(5);
		
		comm.deleteJPGFile(comm.JPGpath(filename));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 50,  enabled = true)
	public void Enterprise_checkScreenshotMsg() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='share']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='confirm-wrap flex-center column']")));
		
		if(!driver.findElement(By.xpath("//div[@class='confirm-wrap flex-center column']/p")).getText().contentEquals(MSG_SAVETIMELINE)) {
			failMsg = "Wrong MSG [Expected]" + MSG_SAVETIMELINE + " [Actual]" + driver.findElement(By.xpath("//div[@class='confirm-wrap flex-center column']/p")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 51,  enabled = true)
	public void Enterprise_checkScreenshotinTimeline() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='confirm button round green']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		Thread.sleep(500);
		
		String timeline = comm.checkTimeline(driver);
		
		if(!timeline.contentEquals("viewer-image")) {
			failMsg = "Don't view screenshot in timeline : " + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 52,  enabled = true)
	public void Enterprise_deleteScreenshot() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='img-wrap']")));
		
		if(!driver.findElements(By.xpath("//div[@class='img-wrap']")).isEmpty()) {
			failMsg = "Don't delete screenshot";
		}
		
		driver.findElement(By.xpath(XPATH_SCREENSHOT_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='shot-count']")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='img-wrap']")));
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	@Test(priority = 53,  enabled = true)
	public void StopRecord() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_RECORD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(TOAST_STOPRECORD)) {
			failMsg = "Wrong Stop Record MSG [Expected]" + TOAST_STOPRECORD
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//녹화 시 참석자 화면에 14일무료체험 가입 팝업 노출됨때만 닫기
		try {
			if(Attenddriver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
				Attenddriver.findElement(By.xpath("//button[@id='dialog-close']")).click();
			}
		} catch (Exception e) {
			//Do not anything
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 54,  enabled = true)
	public void Enterprise_PIPHide() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_PIP_BTN)));
		Thread.sleep(1000);
		
		//이미 숨겨져 있으면 올리기
		if(!driver.findElement(By.xpath("//div[@id='pip-container']")).getAttribute("style").contains("bottom: 0px")) {
			driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
			Thread.sleep(500);
		}
		
		driver.findElement(By.xpath(XPATH_PIP_BTN)).click();
		
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='wrap-pip-layout']/div[@id='local-video-pip']")));
		} catch (Exception e) {
			// do not anything
		}
		if(driver.findElement(By.xpath("//div[@id='pip-container']")).getAttribute("style").contains("bottom: 0px")) {
			failMsg = failMsg + "\n1.Display PIP";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 55,  enabled = true)
	public void Enterprise_Exit() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_EXIT_BTN)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		Attenddriver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		
		comm.waitForLoad(driver);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_QUICKSTART_BTN)));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = "1.Can't leave Room [Expected]" + CommonValues.MEETING_URL + CommonValues.LOUNGE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 56,  enabled = true)
	public void Schedule() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_SCHEDULE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='schedule']")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.SCHEDULE_URL)) {
			failMsg = "1.Wrong URL [Expected]" + CommonValues.MEETING_URL + CommonValues.SCHEDULE_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 57,  enabled = true)
	public void addSchedule() throws Exception {
		String failMsg = "";
		
		List<WebElement> schedule = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		
		driver.findElement(By.xpath("//button[@id='btn-create']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "Add Schedule popup is not display";
		}
		
		driver.findElement(By.xpath("//input[@id='title-input']")).sendKeys("Schedule Test");
		driver.findElement(By.xpath("//button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_SAVE)) {
			failMsg = failMsg + "\n2.Wrong Save MSG [Expected]" + TOAST_SAVE
					+ " [Actual]" + msg;
		}
		
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//div[@class='schedule-card ']"), schedule.size() + 1));
		
		List<WebElement> schedule_affter = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		
		if (!schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//div[1]/p[@class='title']")).getText().contentEquals("Schedule Test")
				|| !schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//li/span[@class='name']")).getText().contentEquals(user1[1])) {

			failMsg = "\n3.Wrong Schedule [Expected]Schedule Test," + user1[1] + " [Acual]"
					+ schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//div[1]/p[@class='title']")).getText() + ","
					+ schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//li/span[@class='name']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 58,  enabled = true)
	public void deleteSchedule() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		
		List<WebElement> schedule = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		
		schedule.get(schedule.size()-1).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		driver.findElement(By.xpath("//button[@class='button round red close large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='question-dialog']")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//section/div/button[@class='button round green large']")));
		
		driver.findElement(By.xpath("//section/div/button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		if(!msg.contentEquals(TOAST_DELETE)) {
			failMsg = failMsg + "\n1.Wrong Delete MSG [Expected]" + TOAST_DELETE
					+ " [Actual]" + msg;
		}
		
		List<WebElement> schedule_after = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		if(schedule_after.size() != (schedule.size()-1)) {
			failMsg = failMsg + "\n3.not Empty Card";
		}
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 60,  enabled = true)
	public void modSchedule() throws Exception {
		String failMsg = "";

		List<WebElement> schedule = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		
		driver.findElement(By.xpath("//button[@id='btn-create']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "\n1.Add Schedule popup is not display";
		}
		String scheduleTitle = "Schedule mod Test";
		driver.findElement(By.xpath("//input[@id='title-input']")).sendKeys(scheduleTitle);
		driver.findElement(By.xpath("//div[@class='react-datepicker__input-container'][1]/button")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@class='react-datepicker__month-select']")));
		driver.findElement(By.xpath("//select[@class='react-datepicker__month-select']")).click();
		
		LocalDateTime nowDateTime = LocalDateTime.now();
		int month = nowDateTime.getMonthValue()==12?1:nowDateTime.getMonthValue()+1;
		
		String xpathM = "//select[@class='react-datepicker__month-select']/option[@value='%d']";
		driver.findElement(By.xpath(String.format(xpathM, month))).click();
		Thread.sleep(500);
		driver.findElement(By.xpath("//div[@class='react-datepicker__week'][2]/div[1]")).click();
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_SAVE)) {
			failMsg = failMsg + "\n2.Wrong Save MSG [Expected]" + TOAST_SAVE
					+ " [Actual]" + msg;
		}
		
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//div[@class='schedule-card ']"), schedule.size() + 1));
		
		List<WebElement> schedule_affter = driver.findElements(By.xpath("//div[@class='schedule-card ']"));
		
		if (!schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//div[1]/p[@class='title']")).getText().contentEquals(scheduleTitle)
				|| !schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//li/span[@class='name']")).getText().contentEquals(user1[1])) {

			failMsg = "\n3.Wrong Schedule [Expected]" + scheduleTitle + "," + user1[1] + " [Acual]"
					+ schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//div[1]/p[@class='title']")).getText() + ","
					+ schedule_affter.get(schedule_affter.size()-1).findElement(By.xpath(".//li/span[@class='name']")).getText();
		}
		schedule_affter.get(schedule_affter.size()-1).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		String header = "예약 수정";
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']//div[@class='dialog-header']")).getText().contentEquals(header)) {
			failMsg = failMsg + "\n4. schedule popup header [Expected] " + header 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='content-wrap']//div[@class='dialog-header']")).getText();
		}

		//삭제
		driver.findElement(By.xpath("//button[@class='button round red close large']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='question-dialog']")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//section/div/button[@class='button round green large']")));
		
		driver.findElement(By.xpath("//section/div/button[@class='button round green large']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 71,  enabled = true)
	public void History() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_HISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		
		if (!driver.getCurrentUrl().contentEquals(CommonValues.MEETING_URL + CommonValues.HISTORY_URL)) {
			failMsg = "1.Wrong URL [Expected]" + CommonValues.MEETING_URL + CommonValues.HISTORY_URL
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 72,  enabled = true)
	public void viewHistory() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@id='content']/ul/li")));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='left']/button")));
		
		List<WebElement> historylist =  driver.findElements(By.xpath("//div[@id='content']/ul/li"));
		historylist.get(0).findElement(By.xpath(".//div[@class='left']/button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "History note popup is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 73, dependsOnMethods = {"viewHistory"},  enabled = true)
	public void shareHistory() throws Exception {
		String failMsg = "";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//section[@id='loader-overlay']")));
		
		driver.findElement(By.xpath("//button[@data-btn='share-note']")).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[2]/div/div")));
		
		if(!driver.findElement(By.xpath("//section[2]/div/div")).isDisplayed()) {
			failMsg = "1.History share popup is not display";
		}
		
		driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath("//input[@id='note-share-input']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//form[@id='contact-form']/div[2]/button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_SUCCESS)) {
			failMsg = failMsg + "\n2.Wrong Success MSG [Expected]" + TOAST_SUCCESS
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		}
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//section[2]/div/div")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='history-list']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 74, dependsOnMethods = {"viewHistory"},  enabled = true)
	public void viewScreenshotDetails() throws Exception {
		String failMsg = "";
		
		List<WebElement> screenshotbtn = driver.findElements(By.xpath("//i[@class='rmicon-image']"));
		
		screenshotbtn.get(0).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='cont']")));
		
		if(!driver.findElement(By.xpath("//section[@id='capture-viewer']//li")).isDisplayed()) {
			failMsg = "Screenshot details is not display";
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@class='dialog-close']")).click();
		Thread.sleep(1000);
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='cont']")));
		
		Thread.sleep(1000);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 75,  enabled = true)
	public void recordingHistory() throws Exception {
		String failMsg = "";
		
		List<WebElement> historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		
		if(historylist.get(0).findElements(By.xpath(".//i[@class='play-btn-icon']")).isEmpty()) {
			for(int i=1; i < historylist.size(); i++) {
				historylist.get(i).findElement(By.xpath(".//i[@class='play-btn-icon']")).click();
				if(!historylist.get(i).findElements(By.xpath(".//i[@class='play-btn-icon']")).isEmpty()) {
					break;
				}
			}
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='video-player']")));
			
			if(!driver.findElement(By.xpath("//div[@class='video-player']")).isDisplayed()) {
				failMsg = "1.Record Start popup is not display";
			}
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 76,  enabled = true)
	public void favoriteHistory() throws Exception {
		String failMsg = "";
		
		List<WebElement> historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		
		historylist.get(0).findElement(By.xpath(".//button[@class='favorite-flag']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='favorite-flag favorite']")));
        
        String date = historylist.get(0).findElement(By.xpath(".//div[@class='date']")).getText();
        
        driver.findElement(By.xpath("//li[@class='filter-item favorite']")).click();
       
        Thread.sleep(2000);
        
        historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
        
        if(!date.contentEquals(historylist.get(0).findElement(By.xpath(".//div[@class='date']")).getText())) {
        	failMsg = "Favorite is wrong";
        }
        
        if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}	
	}
	
	@Test(priority = 77,  enabled = true)
	public void deleteHistory() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//li[@class='filter-item all']")).click();
        
		Thread.sleep(2000);
		
		List<WebElement> historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		
		String date = historylist.get(0).findElement(By.xpath(".//div[@class='date']")).getText();
        
		historylist.get(0).findElement(By.xpath(".//button[@data-btn='history-delete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[@id='question-dialog']")));
		
		driver.findElement(By.xpath("//button[@class='button round green large']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-inner success']")));
		String msg = driver.findElement(By.xpath("//div[@class='toast-inner success']/span")).getText();
		
		if(!msg.contentEquals(TOAST_DELETE)) {
			failMsg = "1.Wrong Save MSG [Expected]" + TOAST_DELETE
					+ " [Actual]" + msg;
		}
		
		comm.waitForLoad(driver);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@class='history-item']")));
		historylist = driver.findElements(By.xpath("//li[@class='history-item']"));
		
		if(!historylist.get(0).findElements(By.linkText(date)).isEmpty()) {
			failMsg = failMsg + "\n2.history is not deleted";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		Attenddriver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public void ShareDocument(WebDriver driver, int i, int j) throws InterruptedException {
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[i];
		driver.findElement(By.xpath("//input[@id='doc-upload-input']")).sendKeys(addedfile);
		Thread.sleep(2000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.attributeContains(driver.findElement(By.xpath("//div[@id='doc-tools']")), "class", "visible"));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='doc-count']"), Integer.toString(j)));
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE)));
		
	}

}
