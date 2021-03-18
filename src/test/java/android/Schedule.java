package android;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import mandatory.CommonValues;

/*
 * 1.스케쥴 클릭
 * 2.예약 추가 페이지로 이동
 * 3.제목 입력
 * 4.시작일 설정 조건 확인
 * 5.종료일 설정 조건 확인
 * 6.리셋
 * 7.반복 항복 기본값 확인
 * 8.반복 항목 2번째 옵션 선택
 * 9.반복 항복 3번째 옵션 선택
 * 10.알림 항목 기본값 확인
 * 11.참석자 추가 클릭
 * 12.참석자 검색
 * 13.참석자 삭제
 * 14.참석자 선택 x 설정
 * 15.내용 항목 placeholder 확인
 * 16.예약 추가 후 혹인
 * 17.예약 수정 페이지로 이동
 * 18.일정 삭제 버튼 확인
 * 19.예약 시간과 생성 시간 차이 체크(10분 이내)
 * 20.예약 시간과 생성 시간 차이 체크(10분 이상)
 * 21.10분 이내일 경우 회의 시작하기 클릭
 * 22.시작한 회의 종료하기
 * 23.종료 화면에서 테스트 진행 위해 리셋
 * 24.예약 삭제
 * 25.종료된 예약 확인
 * 26.캘린더 형식으로 변경 후 확인
 */

public class Schedule {

	private static String TEXT;
	private static Long timediff;
	
	public static AndroidDriver<AndroidElement> androidDriver = null;
	
	CommonAndroid commA = new CommonAndroid();
	CommonValues comm = new CommonValues();
	
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context) throws Exception {
		 
		androidDriver = commA.setAndroidDriver(0,true);
		
		context.setAttribute("webDriver", androidDriver);
	
		commA.setServer(androidDriver);
		commA.emailLogin(androidDriver, CommonValues.ADMEMAIL, CommonValues.USERPW);
	}
	
	@Test(priority = 1, enabled = true)
	public void clickSchedule() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText().contentEquals("SCHEDULE")) {
			failMsg = "Wrong Header [Expected] SCHEDULE [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_text")).getText();
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_cardview_list")).isDisplayed()) {
			failMsg = failMsg + "\n2.Default is not cardview";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void moveAddReservation() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_list_floating_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/fab")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText().contentEquals("예약 추가")) {
			failMsg = "Wrong Header [Expected] 예약 추가 [Actual]" + androidDriver.findElement(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText();
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_time_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_endTime_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_repeatTime_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_noti_time_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_join_user_layout")).isDisplayed()
				|| !androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_message_layout")).isDisplayed()) {
			failMsg = "Layout is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void addReservationTitle() throws Exception {
		String failMsg = "";
		
		TEXT = "Reservation test";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text")).sendKeys(TEXT);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text"), TEXT));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text")).getText().contentEquals(TEXT)) {
			failMsg = "TextBox is not working";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text")).clear();;
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));

		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_layout")).isDisplayed()) {
			failMsg = "Popup is not display";
		}
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText().contentEquals("회의 제목을 입력하세요.")) {
			failMsg = failMsg + "\n2.Popup text is wrong [Expected] 회의 제목을 입력하세요. [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_text")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void settingStartdate() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_date_textView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/datePicker")));
		
		SimpleDateFormat format = new SimpleDateFormat("d");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		int int_today = Integer.parseInt(today);
		
		//30일인지 31인지 확인
		List <AndroidElement> list = androidDriver.findElements(By.xpath("//android.view.View"));
		
		for(int i = 1; i< int_today; i++) {
			if(int_today == 1) {
				continue;
			} else {
				if(androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
					failMsg = "Previous Date is enabled [Date]" + i;
				}
			}
		}
		
		for(int i = int_today; i < list.size(); i++) {
			if(!androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
				failMsg = failMsg + "After Date is not enabled [Date]" + i;
			}
		}
		
		androidDriver.findElement(By.id("android:id/button2")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/datePicker")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void settingEnddate() throws Exception {
		String failMsg = "";
	
		String startdate = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_date_textView")).getText();
		String enddate = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_end_date_textView")).getText();

		if(!startdate.contentEquals(enddate)) {
			failMsg = "Don't coincide date [Start]" + startdate + "[End]" + enddate;
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_end_date_textView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/datePicker")));
		
		SimpleDateFormat format = new SimpleDateFormat("d");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		int int_today = Integer.parseInt(today);
		
		List <AndroidElement> list = androidDriver.findElements(By.xpath("//android.view.View"));
				
		if (int_today == list.size() - 1) {
			for (int i = 1; i < list.size(); i++) {
				if (i == int_today) {
					if (!androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
						failMsg = failMsg + "Date is not enabled [Date]" + i;
					}
				} else {
					if (androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
						failMsg = failMsg + "Date is enabled [Date]" + i;
					}
				}
			}
		} else {
			for (int i = 1; i < list.size(); i++) {
				if (i == int_today) {
					if (!androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
						failMsg = failMsg + "Date is not enabled [Date]" + i;
					}
				} else if (i == int_today + 1) {
					if (!androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
						failMsg = failMsg + "Date is not enabled [Date]" + i + 1;
					}
				} else {
					if (androidDriver.findElement(By.xpath("//android.view.View[@text='" + i + "']")).isEnabled()) {
						failMsg = failMsg + "Date is enabled [Date]" + i;
					}
				}
			}
		}
		
		androidDriver.findElement(By.id("android:id/button2")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/datePicker")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_date_textView")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/datePicker")));
		
		list = androidDriver.findElements(By.xpath("//android.view.View"));
		
		if (int_today == list.size() - 1) {
			androidDriver.findElement(By.id("android:id/next")).click();
			Thread.sleep(1000);
			androidDriver.findElement(By.xpath("//android.view.View[@text='1']")).click();

		}else {
			androidDriver.findElement(By.xpath("//android.view.View[@text='" + (int_today + 1) + "']")).click();
		}
		
		androidDriver.findElement(By.id("android:id/button1")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/datePicker")));
		
		startdate = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_date_textView")).getText();
		enddate = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_end_date_textView")).getText();

		if(!startdate.contentEquals(enddate)) {
			failMsg = failMsg + "Don't coincide date [Start]" + startdate + "[End]" + enddate;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void reset() throws Exception {
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/floating_action_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_list_floating_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/fab")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
	}
	
	@Test(priority = 7, enabled = true)
	public void checkRepeatType() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_type_textView")).getText().contentEquals("반복 안함")) {
			failMsg = "Default is wrong [Expected] 반복 안함 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_type_textView")).getText();
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_limit_date_textView")).isEmpty()) {
			failMsg = failMsg + "\n2.Limit date is display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void checkRepeatTypeOption2() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_type_textView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[3]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]"), "반복 일정"));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[3]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText().contentEquals("반복 일정")) {
			failMsg = "Wrong Header [Expected] 반복 일정 [Actual]" + androidDriver.findElement(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/repeat_option2")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		String addDay = AddDate(today, 1, 0, 0);
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).getText().contentEquals(addDay)) {
			failMsg = failMsg + "Repeat end date is wrong [Expected]" + addDay + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/datePicker")));
		
		for(int i = 0; i < 12; i++) {
			androidDriver.findElement(By.id("android:id/next")).click();
			Thread.sleep(200);
		}
		
		if(!androidDriver.findElements(By.id("android:id/next")).isEmpty()) {
			failMsg = failMsg + "Max value is not 2 years";
		}
		
		androidDriver.findElement(By.id("android:id/button2")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/datePicker")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void checkRepeaTypeOption3() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/repeat_option1")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/repeat_option3")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		
		String today = format.format(time.getTime());
		String addDay = AddDate(today, 1, 0, 0);
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).getText().contentEquals(addDay)) {
			failMsg = "Repeat end date is wrong [Expected]" + addDay + " [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_repeat_end_date_textView")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/datePicker")));
		
		for(int i = 0; i < 12; i++) {
			androidDriver.findElement(By.id("android:id/next")).click();
			Thread.sleep(200);
		}
		
		if(!androidDriver.findElements(By.id("android:id/next")).isEmpty()) {
			failMsg = failMsg + "Max value is not 2 years";
		}
		
		androidDriver.findElement(By.id("android:id/button2")).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("android:id/datePicker")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_header")));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void checkNotifyType() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_notify_type_textView")).getText().contentEquals("10분 전 알림")) {
			failMsg = "Default notify is wrong [Expected] 10분 전 알림 [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservatoin_notify_type_textView")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void clickAddAttendee() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView")).getText().contentEquals("참석자 추가")) {
			failMsg = "Wrong Text [Expected] 참석자 추가 [Actual]" + androidDriver.findElement(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView")).getText();
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_add_attendees")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_header")));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_header")).isDisplayed()) {
			failMsg = failMsg + "Layout is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void searchAttendee() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_search")).sendKeys(CommonValues.USERS[0]);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[2]"), CommonValues.USERS[0]));
		
		if(!androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[2]")).getText().contentEquals(CommonValues.USERS[0])) {
			failMsg = "Don't search attendee email";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_search")).clear();
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_search")).sendKeys("rmrsup1");
		
		Thread.sleep(1000);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[2]"), CommonValues.USERS[0]));
		
		if(!androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[2]")).getText().contentEquals(CommonValues.USERS[0])) {
			failMsg = failMsg + "Don't search attendee name";
		}
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[2]")).click();
		
		Thread.sleep(1000);
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_user_count_textview"), "총 2명"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_user_list_textview")).getText().contentEquals("자동화기업용관리자, rmrsup1")) {
			failMsg = failMsg + "Don't add attendee";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void deleteAttendee() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_user_list_textview")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/invite_list_delete")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/invite_list_delete")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[1]")));

		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView"), "참석자 추가"));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView")).getText().contentEquals("참석자 추가")) {
			failMsg = "Don't delete attendee";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 14, enabled = true)
	public void addEmptyAttendee() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_add_attendees")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_invite_header")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView"), "참석자 추가"));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[5]/android.widget.LinearLayout/android.widget.TextView")).getText().contentEquals("참석자 추가")) {
			failMsg = "Add attendee";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 15, enabled = true)
	public void checkContextPlaceholder() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_message_text")).getText().contentEquals("내용을 입력하세요.")) {
			failMsg = "Context Placeholder is wrong [Expected] 내용을 입력하세요. [Actual]" + androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_message_text")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 16, enabled = true)
	public void addReservation() throws Exception {
		String failMsg = "";
		
		TEXT = "Reservation test";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text")).sendKeys(TEXT);
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text"), TEXT));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_message_text")).sendKeys(TEXT);
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_message_text"), TEXT));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
		commA.scrollToAnElementByText(androidDriver, TEXT);
		
		if(!androidDriver.findElement(By.xpath("//android.widget.TextView[@text='" + TEXT + "']")).getText().contentEquals(TEXT)) {
			failMsg = "Don't add Reservation";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 17, enabled = true)
	public void clickReservation() throws Exception {
		String failMsg = "";
		
		commA.scrollToAnElementByText(androidDriver, TEXT);
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='" + TEXT +"']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]"), "예약 수정"));
		
		if(!androidDriver.findElement(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText().contentEquals("예약 수정")) {
			failMsg = "Don't intent layout [Expected] 예약 수정 [Actual]" + androidDriver.findElement(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 18, enabled = true)
	public void checkDeleteBtn() throws Exception {
		String failMsg = "";
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_delete")).isDisplayed()) {
			failMsg = "Delete Btn is not display";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 19, enabled = true)
	public void checkDifftime() throws Exception {
		String failMsg = "";
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_time_textView"));
		
		SimpleDateFormat f = new SimpleDateFormat("HH:mm");
		Date first = f.parse(change(element));
		Date second = f.parse(getTime());
		
		long diff = first.getTime()-second.getTime();
		timediff = diff/60000;
		
		System.out.println("시간 차이 :" + timediff);
		
		if(timediff <= 10) {
			if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_button")).isDisplayed()) {
				failMsg = "Start Btn is not display";
			}
		} else {
			throw new SkipException("This test is skip");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 20, enabled = true)
	public void checkDifftime2() throws Exception {
		String failMsg = "";
		
		if(timediff > 10) {
			if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/reservation_start_button")).isEmpty()) {
				failMsg = "Start Btn is not empty";
			}
		} else {
			throw new SkipException("This test is skip");
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 21, enabled = true, dependsOnMethods = {"checkDifftime"})
	public void clickStartMeetingBtn() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_start_button")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_ROOMACTIVITY)) {
			failMsg = "Don't enter room";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 22, enabled = true, dependsOnMethods = {"checkDifftime"})
	public void exitMeeting() throws Exception {
		String failMsg = "";
	
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/channel_info_close")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/channel_info_invite_layout")));
		
		androidDriver.findElement(By.id(CommonAndroid.ID_ROOM_SIDEMENU_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/menu_list")));
		
		AndroidElement element = androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/menu_list"));

		commA.scrollToAnElementByText(element, "나가기");
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='나가기']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/home_ex_activity")));
		
		if(!androidDriver.currentActivity().contentEquals(CommonAndroid.MEETING_HOMEACTIVITY)) {
			failMsg = "Don't leave room [Expected]" + CommonAndroid.MEETING_HOMEACTIVITY + " [Actual]" + androidDriver.currentActivity();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 23, enabled = true, dependsOnMethods = {"checkDifftime"})
	public void goReset() throws Exception {
		
		androidDriver.findElement(By.xpath("//android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.ImageView")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
		commA.scrollToAnElementByText(androidDriver, TEXT);
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='" + TEXT +"']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView[1]"), "예약 수정"));
		
	}
	
	@Test(priority = 24, enabled = true)
	public void deleteReservation() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_delete")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));

		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_text"), "삭제되었습니다."));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
		if(!androidDriver.findElements(By.xpath("//android.widget.TextView[@text='" + TEXT +"']")).isEmpty()) {
			failMsg = "Don't delete schedule";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 25, enabled = true)
	public void checkFinishedSchedule() throws Exception {
		String failMsg = "";
		
		androidDriver.findElement(By.xpath("//android.widget.TextView[@text='test']")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//android.view.ViewGroup[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.TextView"), "예약 확인"));
		
		if(!androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_finished")).getText().contentEquals("종료된 회의입니다.")) {
			failMsg = "Don't display text";
		}
		
		if(!androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/reservation_start_button")).isEmpty()) {
			failMsg = "Still display Meeting Start Btn";
		}
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_left_image_button")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_header")));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 26, enabled = true)
	public void changeForm() throws Exception {
		String failMsg = "";
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.rsupport.remotemeeting.application:id/header_right_image_string_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_image_string_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/weekView")));
		
		if(androidDriver.findElements(By.id("com.rsupport.remotemeeting.application:id/weekView")).isEmpty()) {
			failMsg = "Don't change calendar form";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	/* 반복일정 보류
	@Test(priority = 24, enabled = true)
	public void add() throws Exception {
		String failMsg = "";
		
		TEXT = "Reservation test";
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_list_empty_imageview")).click();
		
		WebDriverWait wait = new WebDriverWait(androidDriver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_list_floating_button")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text")).sendKeys(TEXT);
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_title_text"), TEXT));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_type_textView")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_header")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/repeat_option2")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_end_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/reservation_repeat_header")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/header_right_string_button")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.rsupport.remotemeeting.application:id/popup_layout")));
		
		androidDriver.findElement(By.id("com.rsupport.remotemeeting.application:id/popup_button2")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("com.rsupport.remotemeeting.application:id/header_text"), "SCHEDULE"));
		
	}
	*/
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		androidDriver.quit();
	}
	
	public String AddDate(String strDate, int year, int month, int day) throws Exception {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date dt = dtFormat.parse(strDate);
		cal.setTime(dt);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		return dtFormat.format(cal.getTime());
	}
	
	public String getTime() {
	
		String format = "HH:mm";

		Calendar today = Calendar.getInstance();

		SimpleDateFormat type = new SimpleDateFormat(format);

		return type.format(today.getTime());
	}
	
	public String change(AndroidElement element) throws ParseException {
		
		String input = element.getText();
		
		DateFormat inputFormat = new SimpleDateFormat("aa KK:mm");
		DateFormat outputFormat = new SimpleDateFormat("HH:mm");
		
		return outputFormat.format(inputFormat.parse(input));
		
	}
		
	
	
	
	
	
	
	
}
