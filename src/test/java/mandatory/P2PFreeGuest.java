package mandatory;

import static org.testng.Assert.fail;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/*

 */

public class P2PFreeGuest {
	
	public static WebDriver driver;
	public static WebDriver attenddriver;

	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	public String roomUrl = "";
	public String attendUser = "";
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		attenddriver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);//free user
		context.setAttribute("webDriver2", attenddriver);//login user
		
		driver.get(CommonValues.MEETING_URL);
		comm.login(driver, CommonValues.USERS[0], CommonValues.USERPW);
		attendUser = CommonValues.ATTENDEEFREENICKNAME;
	}
	
	// 1. 로그인 유저 유저 룸 생성 - 로그인 유저 참여
	@Test(priority = 1)
	public void freeAttend() throws Exception {
		String failMsg = "";
		WebDriverWait wait_admin = new WebDriverWait(driver, 10);
		WebDriverWait wait_attend = new WebDriverWait(attenddriver, 20);
		
		driver.get(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL);
		
		comm.createNormalMeeting(driver, "NormalRoom");
		
		attenddriver.get(CommonValues.MEETING_URL);
		comm.attendMeeting(driver, attenddriver);
		wait_attend.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='dialog']")));
		wait_attend.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait_attend.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='device-setting-notification-box']")));
		
		roomUrl = driver.getCurrentUrl();
		
		//기존 참여자 타임로그 확인
		//타임라인 안보이면 열기
		if (!driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class")
				.contains("active")) {
			driver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
			wait_admin.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)));
		}
		String timeline = comm.checkTimeline(driver);
		String expected = String.format(CommonValues.MSG_ATTEND, attendUser);
		if(!timeline.contentEquals(expected)) {
			failMsg = failMsg + "\n1. timeline [Expected]" + expected + " [Actaul]" + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//2. free 유저가 lock 클릭 - 팝업 확인
	@Test(priority = 2, enabled = true)
	public void roomLock() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_LOCK_BTN)));
		
		//free 유저가 룸에러 lock 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_LOCK_BTN)).click();
		
		//팝업 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(P2PFree.XPATH_DIALOG_HEADER)));
		if(!attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n1.popup header is wrong [Expected]" +P2PFree. EXIT_HEADER 
					+ " [Actual]" + attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText();
		}
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//3. 무료유저가 초대 - 초대 후 상태 확인
	@Test(priority = 3, enabled = true)
	public void roomInvite() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_INVITE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_INVITE_BTN)).click();
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_INVITE)));
		attenddriver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).clear();
		attenddriver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).sendKeys(CommonValues.ADM_ID);
		attenddriver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITESUBMIT_BTN)).click();
		
		//toast 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_SENDIVITATION)) {
			failMsg = "\n1. toast message [Expected]" + CommonValues.TOAST_SENDIVITATION + " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//발송내역 링크 선택
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_SENTLIST_BTN)));
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_BTN)).click();
		
		//dialog확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-body']/ul")));
		List<WebElement> list = attenddriver.findElements(By.xpath("//div[@class='dialog-body']/ul"));
		if(list.size() != 1) {
			failMsg = failMsg + "\n2. invite list size error. actual size : " + list.size();
		} else {
			if(!list.get(0).findElement(By.xpath(".//span[@class='sent-email']")).getText().contentEquals(CommonValues.ADM_ID)) {
				failMsg = failMsg + "\n3. sent email. [Expected]" + CommonValues.ADM_ID 
						+ " [Actual]" + list.get(0).findElement(By.xpath(".//span[@class='sent-email']")).getText();
			}
			if(!list.get(0).findElement(By.xpath(".//span[2]")).getText().contentEquals("전송성공")) {
				failMsg = failMsg + "\n4. sent result. [Expected]" + "전송성공"
						+ " [Actual]" + list.get(0).findElement(By.xpath(".//span[@class='sent-email']")).getText();
			}
			
		}
		
		//x클릭 닫기.
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='dialog-body']/ul")));
		
		try {
			attenddriver.findElement(By.xpath("//div[@id='invite-dialog']//button[@class='close']")).click();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//4. 문서공유 - 이미지 파일
	@Test(priority = 4, enabled = true)
	public void room_shareImg() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//png 파일 share
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[3]; //png
		attenddriver.findElement(By.xpath(P2PFree_Share.XPATH_DOCSHARE_INPUT)).sendKeys(addedfile);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		
		//타임라인 안보이면 열기
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)));
		}
		
		//타임라인 확인
		String timeline = comm.checkTimeline(attenddriver);
		String expected = String.format(P2PFree3.SHARE_MESSAGE, attendUser, CommonValues.TESTFILE_LIST[3]);
		if(!timeline.contentEquals(expected)) {
			failMsg = failMsg + "\n1. timeline message [Expected]" + expected + " [Actual]" + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//5. 문서공유 - 텍스트 파일
	@Test(priority = 5, enabled = true)
	public void room_shareTxt() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//png 파일 share
		String filePath = CommonValues.TESTFILE_PATH;
		if (System.getProperty("os.name").toLowerCase().contains("mac")) 
			filePath = CommonValues.TESTFILE_PATH_MAC;
		String addedfile = filePath + CommonValues.TESTFILE_LIST[8]; //text
		attenddriver.findElement(By.xpath(P2PFree_Share.XPATH_DOCSHARE_INPUT)).sendKeys(addedfile);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		
		//타임라인 안보이면 열기
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)));
		}
		
		//타임라인 확인
		String timeline = comm.checkTimeline(attenddriver);
		String expected = String.format(P2PFree3.SHARE_MESSAGE, attendUser, CommonValues.TESTFILE_LIST[8]);
		if(!timeline.contentEquals(expected)) {
			failMsg = failMsg + "\n1. timeline message [Expected]" + expected + " [Actual]" + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	//6. 화면공유 시작 
	@Test(priority = 6, enabled = true)
	public void room_ShareScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//화면공유 시작
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SHARESCREEN_BTN)).click();

		//Toast 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STARTSCREENSHARE)){
			failMsg = failMsg + "\n1. start screen share toast message [Expected]" + CommonValues.TOAST_STARTSCREENSHARE
					+ " [Actual]" + msg;
		}
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//7. 화면공유 종료
	@Test(priority = 7, dependsOnMethods = {"room_ShareScreen"}, alwaysRun = true,  enabled = true)
	public void room_StopScreen() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//화면공유 종료
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)));
		attenddriver.findElement(By.xpath(CommonValues.XPATH_STOPSHARESCREEN_BTN)).click();

		//Toast 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_STOPSCREENSHARE)){
			failMsg = failMsg + "\n1. stop screen share toast message [Expected]" + CommonValues.TOAST_STOPSCREENSHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//8. 스크린샷 클릭 - 토스트 확인
	@Test(priority = 8,  enabled = true)
	public void room_ScreenShot() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//free 유저가 스크린샷 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SCREENSHOT_BTN)).click();
		
		//팝업 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(P2PFree.XPATH_DIALOG_HEADER)));
		if (!attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n1.popup header is wrong [Expected]" + P2PFree.EXIT_HEADER + " [Actual]"
					+ attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText();
		}
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//9. 녹화 클릭 - 팝업 확인
	@Test(priority = 9,  enabled = true)
	public void room_Rec() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//free 유저가 녹화 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_RECORD_BTN)).click();
		
		//팝업 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(P2PFree.XPATH_DIALOG_HEADER)));
		if (!attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n1.popup header is wrong [Expected]" + P2PFree.EXIT_HEADER + " [Actual]"
					+ attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText();
		}
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//10. 사회자모드 클릭 - 팝업 확인
	@Test(priority = 10,  enabled = true)
	public void room_AdminMode() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//free 유저가 사회자모드 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_CROWN_BTN)).click();
		
		//팝업 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(P2PFree.XPATH_DIALOG_HEADER)));
		if (!attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n1.popup header is wrong [Expected]" + P2PFree.EXIT_HEADER + " [Actual]"
					+ attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText();
		}
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//20. 모드변경 팝업 확인 - 분할모드
	@Test(priority = 20,  enabled = true)
	public void room_splitMode() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//free 유저가 모드 변경 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SWITCHMODE_BTN)).click();
		
		//팝업 확인
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(P2PFree.XPATH_DIALOG_HEADER)));
		if (!attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText().contentEquals(P2PFree.EXIT_HEADER)) {
			failMsg = "\n1.popup header is wrong [Expected]" + P2PFree.EXIT_HEADER + " [Actual]"
					+ attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_HEADER)).getText();
		}
		attenddriver.findElement(By.xpath(P2PFree.XPATH_DIALOG_CLOSE_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	
	//31. 참여자 리스트확인
	@Test(priority = 31,  enabled = true)
	public void room_attendeesList() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SPEAKLIST_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='participants-list-default']/li")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='user-name ']")));
		
		List<WebElement> userlist = attenddriver.findElements(By.xpath("//ul[@class='participants-list-default']/li"));
		if(userlist.size()!= 2) {
			failMsg = failMsg + "\n1. userlist size : " + userlist.size();
		} else {
			if(!userlist.get(0).findElement(By.xpath(".//span[@class='attendee-name']/span[2]")).getText().contains(attendUser)){
				failMsg = "\n1. attendlist username [Expected]" + attendUser
						+ " [Actual]" + userlist.get(0).findElement(By.xpath(".//span[@class='attendee-name']/span[2]")).getText();
			}
			String user2 = CommonValues.USERS[0].replace("@gmail.com", "");
			if(!userlist.get(1).findElement(By.xpath(".//span[@class='attendee-name']/span[2]")).getText().contains(user2)){
				failMsg = "\n2. attendlist username [Expected]" + user2
						+ " [Actual]" + userlist.get(1).findElement(By.xpath(".//span[@class='attendee-name']/span[2]")).getText();
			}
		}
		
		//참여자 리스트 다시 클릭(레이어 없애기)
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SPEAKLIST_BTN)).click();
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//32. 카메라 on/off
	@Test(priority = 32,  enabled = true)
	public void room_cam() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//camera off
		attenddriver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_CAMERA_OFF)){
			failMsg = failMsg + "\n1. camera off toast message [Expected]" + P2PEnterprise.TOAST_CAMERA_OFF
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//camera on
		attenddriver.findElement(By.xpath(CommonValues.XPATH_CAMERA_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_CAMERA_ON)){
			failMsg = failMsg + "\n2. camera on toast message [Expected]" + P2PEnterprise.TOAST_CAMERA_ON
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//33. 오디오 on/off
	@Test(priority = 33,  enabled = true)
	public void room_mic() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		//mic off
		attenddriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_AUDIO_OFF)){
			failMsg = failMsg + "\n1. mic off toast message [Expected]" + P2PEnterprise.TOAST_AUDIO_OFF
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		//mic on
		attenddriver.findElement(By.xpath(CommonValues.XPATH_MIC_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(P2PEnterprise.TOAST_AUDIO_ON)){
			failMsg = failMsg + "\n2. mic on toast message [Expected]" + P2PEnterprise.TOAST_AUDIO_ON
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//41. 환경설정 - 마이크 설정
	@Test(priority = 41,  enabled = true)
	public void room_settingMic() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));

		// 마이크 워닝 툴팁 떠있으면 없애기
		if (attenddriver.findElement(By.xpath("//div[@class='device-title']/em")).isDisplayed())
			attenddriver.findElement(By.xpath("//div[@class='device-title']/em")).click();
		
		//마이크 리스트 클릭
		attenddriver.findElement(By.xpath("//div[@id='mic-list']//select")).click();
		String micValue = "";
		List<WebElement> miclist = attenddriver.findElements(By.xpath("//div[@id='mic-list']//select/option"));
		if(miclist.size() > 0) {
			micValue = miclist.get(miclist.size()-1).getText();
			miclist.get(miclist.size()-1).click();
		}

		//저장 클릭
		attenddriver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='dialog']")));
			attenddriver.findElement(By.xpath("//div[@class='buttons align-center']/button")).click();
		} catch (Exception e) {
			// do not anything
		}
		
		//다시 설정 팝업
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!attenddriver.findElement(By.xpath("//div[@id='mic-list']//select/option[@class='saved-option']")).getText().contains(micValue)) {
			failMsg = failMsg + "\n1. saved mic value [Expected]" + micValue 
					+ " [Actual]" + attenddriver.findElement(By.xpath("//div[@id='mic-list']//select/option[@class='saved-option']")).getText();
		}
		
		//취소 클릭
		attenddriver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[2]")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//42. 환경설정 - 스피커 설정
	@Test(priority = 42,  enabled = true)
	public void room_settingSpeaker() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));

		// 마이크 워닝 툴팁 떠있으면 없애기
		if (attenddriver.findElement(By.xpath("//div[@class='device-title']/em")).isDisplayed())
			attenddriver.findElement(By.xpath("//div[@class='device-title']/em")).click();
		
		//카메라 리스트 클릭
		attenddriver.findElement(By.xpath("//div[@id='speaker-list']//select")).click();
		String value = "";
		List<WebElement> list = attenddriver.findElements(By.xpath("//div[@id='speaker-list']//select/option"));
		if(list.size() > 0) {
			value = list.get(list.size()-1).getText();
			list.get(list.size()-1).click();
		} else {
			System.out.println("speaker size is 0");
		}

		//저장 클릭
		attenddriver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[1]")).click();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_ROOM_LOADER)));
		} catch (Exception e) {
			//do not anything
		}
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='dialog']")));
			attenddriver.findElement(By.xpath("//div[@class='buttons align-center']/button")).click();
		} catch (Exception e) {
			// do not anything
		}
		
		//다시 설정 팝업
		attenddriver.findElement(By.xpath(CommonValues.XPATH_SETTING_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!attenddriver.findElement(By.xpath("//div[@id='speaker-list']//select/option[@class='saved-option']")).getText().contains(value)) {
			failMsg = failMsg + "\n1. saved speaker value [Expected]" + value 
					+ " [Actual]" + attenddriver.findElement(By.xpath("//div[@id='speaker-list']//select/option[@class='saved-option']")).getText();
		}
		
		//취소 클릭
		attenddriver.findElement(By.xpath("//div[@id='device-setting-wrap']//div[5]/button[2]")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));		
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//43. 회의록 보기 - ai기록탭 마우스 오버
	@Test(priority = 43,  enabled = true)
	public void room_note() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		}
		//탭 보이면..
		wait.until(ExpectedConditions.attributeContains(By.xpath(CommonValues.XPATH_NOTE_BTN), "class", "active"));
		Thread.sleep(1000);
		String msg = attenddriver.findElement(By.xpath("//div[@class='timeline timeline-init']//p[@class='infomation guest']")).getText();
		String expected = "Guest는 보기 전용 권한만 가지고 있습니다.";
		if(!msg.contentEquals(expected)) {
			failMsg = failMsg + "\n1. ai note tab msg [Expected]" + expected + " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//44. 회의록 보기 - 수동탭 회의록공유 클릭
	@Test(priority = 44,  enabled = true)
	public void room_noteshare() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		}
		//탭 보이면..
		wait.until(ExpectedConditions.attributeContains(By.xpath(CommonValues.XPATH_NOTE_BTN), "class", "active"));
		Thread.sleep(1000);
		
		//수동기록 탭활성화 
		if(!attenddriver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath("//ul[@class='wrap-tab-menu']/li[2]/a")).click();
			Thread.sleep(1000);
		}

		//회의록 공유 클릭
		attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));
		
		if(!attenddriver.findElement(By.xpath("//div[@class='dialog-header']")).getText().contentEquals("회의록 공유")) {
			failMsg = failMsg + "\n1. dialog header [Expected]회의록 공유 [Actual]" +  attenddriver.findElement(By.xpath("//div[@class='dialog-header']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//45. 회의록 보기 - 수동탭 회의록공유 클릭
	@Test(priority = 45, dependsOnMethods = {"room_noteshare"}, enabled = true)
	public void room_noteshare2() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dialog-header']")));

		attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).clear();
		attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(CommonValues.ADMEMAIL);
		
		//click send
		attenddriver.findElement(By.xpath("//div[@class='dialog-body']//button[@type='submit']")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		String msg = attenddriver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		if(!msg.contentEquals(CommonValues.TOAST_NOTESHARE)) {
			failMsg = failMsg + "\n1. note share toast [Expected]" + CommonValues.TOAST_NOTESHARE
					+ " [Actual]" + msg;
		}
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(CommonValues.XPATH_TOAST)));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//46. 회의록 보기 - esc로 닫기
	@Test(priority = 46, enabled = true)
	public void room_noteEsc() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(attenddriver, 20);
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		}
		//탭 보이면..
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@class='wrap-tab-menu']/li")));
		
		//send esc
		Actions action = new Actions(attenddriver);
		action.sendKeys(Keys.ESCAPE).perform();

		Thread.sleep(1000);
		
		if(attenddriver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n1. note is active";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//47. 타임라인 오픈
	@Test(priority = 47, enabled = true)
	public void room_timeline() throws Exception {
		String failMsg = "";
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		Thread.sleep(1000);
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class").contains("active")) {
			failMsg = failMsg + "\n1. timeline is not atived";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//48. 타임라인 메세지 전송
	@Test(priority = 48, enabled = true)
	public void room_sendMsg() throws Exception {
		String failMsg = "";
		
		if(!attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).getAttribute("class").contains("active")) {
			attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_BTN)).click();
		}
		
		String sendmsg = "hello remotemeeting~";
		attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)).clear();
		attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINE_INPUT)).sendKeys(sendmsg);
		attenddriver.findElement(By.xpath(CommonValues.XPATH_TIMELINESEND_BTN)).click();
		
		Thread.sleep(1000);
		
		String timeline = comm.checkTimeline(attenddriver);
		if(!timeline.contentEquals(sendmsg)) {
			failMsg = failMsg + "\n1. sent message [Expected]" + sendmsg + " [Actual]" + timeline;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	//48. 룸 나가기
	@Test(priority = 50, enabled = true)
	public void room_leave() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait_free = new WebDriverWait(driver, 10);
		WebDriverWait wait_attend = new WebDriverWait(attenddriver, 10);
		
		driver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		try {
			wait_free.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
			driver.findElement(By.xpath(P2PFree.XPATH_LEAVE_BTN)).click();
			wait_free.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		} catch (Exception e) {
			//do not anything
		}
		
		attenddriver.findElement(By.xpath(CommonValues.XPATH_EXIT_BTN)).click();
		try {
			wait_attend.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
			attenddriver.findElement(By.xpath(P2PFree.XPATH_LEAVE_BTN)).click();
			wait_attend.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		} catch (Exception e) {
			//do not anything
		}
		Thread.sleep(2000);
		if(!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.LOUNGE_URL)) {
			failMsg = failMsg + "\n1.not lounge url after leave the room(login user) : " + driver.getCurrentUrl();
		}
		if(!attenddriver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.KRHOME_URL)) {
			failMsg = failMsg + "\n1.not home url after leave the room(free user) :" + attenddriver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		attenddriver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
	
}
