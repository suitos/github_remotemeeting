package mandatory;


import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;



public class P2PEnterprise {
	
	public static String XPATH_PROFILE_BTN = "//button[@id='btn-user']";
	public static String XPATH_LOUNGE_BTN = "//div[@class='header-item btn-lounge']";
	public static String XPATH_QUICKSTART_BTN = "//button[@id='btn-gnb-create']";
	
	public static String XPATH_QUICKSTARTTITLE_INPUT = "//input[@name='title']";
	public static String XPATH_QUICKSTARTSTART_BTN = "//button[@class='button round large green']";

	public static String HREF_PROFILE = "//a[@href='/ko/setting/profile']";
	
	public static String TOAST_LOCK = "회의실이 잠금 상태로 변경되었습니다.";
	public static String TOAST_CHANGEPW = "비밀번호가 변경 되었습니다.";
	public static String TOAST_UNLOCK = "회의실 잠금이 해제 되었습니다.";
	
	public static String TOOLTIP_AI = "현재 AI 회의 기록이 OFF 상태입니다.\n" + "상단의 AI 버튼을 ON으로 해주세요.";
	
	public static String MSG_NOTESTATE = "모든 변경사항이 HISTORY에 저장됨.";
	
	private static String RoomTitle = "P2PEnterprise";
	
	public String roomID = "";
	
	public static WebDriver driver;
	
	private StringBuffer verificationErrors = new StringBuffer();
	
	CommonValues comm = new CommonValues();
	
	@Parameters({"browser"})
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {
	
		comm.setDriverProperty(browsertype);

		//lang=en_US, ko_KR
		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);

		context.setAttribute("webDriver", driver);

	}
	
	@Test(priority = 1)
	public void Login() throws Exception {
		
		driver.get(CommonValues.MEETING_URL);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_FREECREATE_BTN)));
		
		comm.login(driver, CommonValues.ADMEMAIL, CommonValues.USERPW);
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
	
	@Test(priority = 2)
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
		
		driver.findElement(By.xpath("//button[@class='cola-btn size-sm type-full green']")).click();
		
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
	
	@Test(priority = 3)
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
	
	@Test(priority = 4)
	public void QuickStart() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_QUICKSTART_BTN)).click();
		
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
	
	@Test(priority = 4)
	public void QuickStart2() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(XPATH_QUICKSTARTTITLE_INPUT)).sendKeys(RoomTitle);
		driver.findElement(By.xpath(XPATH_QUICKSTARTSTART_BTN)).click();
		
		comm.waitForLoad(driver);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_ROOM_INVITE)));
		
		roomID = driver.getCurrentUrl().replace(CommonValues.MEETING_URL + CommonValues.ROOM_URL , "");

		if (!driver.getCurrentUrl().contains(CommonValues.MEETING_URL + CommonValues.ROOM_URL)) {
			failMsg = "1.Can't enter Room [Expected]" + CommonValues.MEETING_URL + CommonValues.ROOM_URL + roomID
					+ " [Actual]" + driver.getCurrentUrl();
		}
		
		if(driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITE)).getAttribute("style").contains("block")) {
			failMsg = failMsg + "\n2.Can't dispay Invitation screen";
		}
	}
	
	@Test(priority = 5)
	public void inviteEnterprise() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)));
		
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).click();
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITEINPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_ROOM_INVITESUBMIT_BTN)).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), CommonValues.TOAST_SENDIVITATION));
		
		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(CommonValues.TOAST_SENDIVITATION)) {
			failMsg = "Wrong SendInvitation MSG [Expected]" + CommonValues.TOAST_SENDIVITATION
					+ " [Actual]" + msg;
		}
		
		if(!CommonValues.USERS[0].contains(driver.findElement(By.xpath(CommonValues.XPATH_SENTLIST_NAME)).getText())) {
			failMsg = "1.Wrong Sent list";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6)
	public void inviteEnterprise2() throws Exception {
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
	
	@Test(priority = 7)
	public void LockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='lock']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='lock-drop-box']")));
		
		if(!driver.findElement(By.xpath("//div[@id='lock-drop-box']")).isDisplayed()) {
			failMsg = "1.Lock drop box is not display";
		}
		
		driver.findElement(By.xpath("//button[@class='on']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), TOAST_LOCK));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(TOAST_LOCK)) {
			failMsg = failMsg + "\n2.Wrong Lock MSG [Expected]" + TOAST_LOCK
					+ " [Actual]" + msg;
		}
			
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8)
	public void ChangeRoomPW() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='reload-password']")).click();
	
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), TOAST_CHANGEPW));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
	
		if(!msg.contentEquals(TOAST_CHANGEPW)) {
			failMsg = failMsg + "Wrong ChangePW MSG [Expected]" + TOAST_CHANGEPW
					+ " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9)
	public void UnlockRoom() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@class='off active']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), TOAST_UNLOCK));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
	
		if(!msg.contentEquals(TOAST_UNLOCK)) {
			failMsg = failMsg + "Wrong Unlock MSG [Expected]" + TOAST_UNLOCK
					+ " [Actual]" + msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10)
	public void Enterprise_note() throws Exception {
		String failMsg = "";
		
		Actions action = new Actions(driver);
		action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER).keyUp(Keys.SHIFT).perform();
		Thread.sleep(1000);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contentEquals("active") ) {
			failMsg = "Inactive Note";
		}
		
		if(!driver.findElement(By.xpath("//aside[@id='meeting-note']//h2")).isDisplayed() ) {
			failMsg = "\n2.Don't Display Note";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11)
	public void TurnOn_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='off']")).click();
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("on") ) {
			failMsg = "AI don't turn ON";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12)
	public void TurnOff_NoteAI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//span[@class='on']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath("//div[@class='content-wrap']")).isDisplayed()) {
			failMsg = "AI OFF popup is not display";
		}
		
		driver.findElement(By.xpath("//button[@id='btn-confirm']")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='content-wrap']")));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("data-automatic").contentEquals("off") ) {
			failMsg = failMsg + "\n2.AI don't turn OFF";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).isDisplayed()) {
			failMsg = failMsg + "\n3.AI tooltip is not display";
		}
		
		if(!driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).getText().contentEquals(TOOLTIP_AI)) {
			failMsg = failMsg + "\n4.AI tooltip text is wrong [Expected]" + TOOLTIP_AI 
					+ " [Actual]" + driver.findElement(By.xpath("//div[@class='tooltip-switch-info note-tooltip']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13)
	public void TurnOff_NoteManual() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@data-menu='manual']")).click();
		
		if(!driver.findElement(By.xpath("//ul[@class='wrap-tab-menu']")).getAttribute("data-selected").contentEquals("manual") ) {
			failMsg = failMsg + "\n2.Manual Tab inactive";
		}
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTETITLE_INPUT)).sendKeys("AUTOTEST");
		driver.findElement(By.xpath("//html[@dir='ltr']/body")).sendKeys("AUTOTEST_context");
		
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
	
	@Test(priority = 14)
	public void Enterprise_Sharenote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTE)).getAttribute("class").contains("active") ) {
			failMsg = "Inactive Note";
		}
		
		Thread.sleep(1000);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_BTN)).click();
		
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(CommonValues.XPATH_NOTESHARE)));
		
		if(!driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE)).isDisplayed() ) {
			failMsg = "Inactive ShareNote";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15)
	public void Enterprise_Sendnote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE_INPUT)).sendKeys(CommonValues.USERS[0]);
		driver.findElement(By.xpath(CommonValues.XPATH_NOTESHARE + "//button")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(CommonValues.XPATH_TOAST)), CommonValues.TOAST_NOTESHARE));

		String msg = driver.findElement(By.xpath(CommonValues.XPATH_TOAST)).getText();
		
		if(!msg.contentEquals(CommonValues.TOAST_NOTESHARE)) {
			failMsg = "Wrong SendNote MSG [Expected]" + CommonValues.TOAST_NOTESHARE
					+ " [Actual]" + msg;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16)
	public void Enterprise_Historynote() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues.XPATH_NOTEHISTORY_BTN)).click();
	}


}
