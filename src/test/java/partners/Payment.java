package partners;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

/*
 * 1.파트너 로그인
 * 2.결제 관리 화면 이동
 * 3.전체 조건 검색 확인
 * 4.미납 ID 요금제 조건 검색 
 * 5.미납 회의실 요금제 조건 검색 
 * 6.미납 종량 요금제 조건 검색 
 * 7.결제 완료 전체 조건 검색 
 * 8.결제 완료 카드 조건 검색 
 * 9.결제 완료 오프라인 조건 검색 
 * 10.고객 코드 조건 검색
 * 11.고객사명 조건 검색
 * 12.이메일 조건 검색
 * 13.엑셀 데이터 확인
 * 14.30개 단위로 페이징 확인
 * 15.청구서 UI 확인
 * 16.청구서 팝업 닫힘 확인
 * 17.카드 결제 취소 시 팝업 확인
 * 18.오프라인 결제 취소 시 팝업 확인
 * 19.결제 완료 처리 시 팝업 확인
 * 20.수량 셀 확인
 * 21.결제 실패한 경우 결제 상태 영역 "실패" 확인
 * 22.결제 상태 영역 마우스 오버 시 툴팁 확인
 */

public class Payment {
	
	public static String ALL_BTN = "//button[@id='all']";
	public static String IDPREPAYMENT_BTN = "//button[@id='id-prepayment']";
	public static String CONFERENCEPREPAYMENT_BTN = "//button[@id='conference-prepayment']";
	public static String WEIGHTED_BTN = "//button[@id='weighted-system']";
	public static String RESULTALL_BTN = "//button[@id='result-all']";
	public static String RESULTCARD_BTN = "//button[@id='result-card']";
	public static String RESULTOFFLINE_BTN = "//button[@id='result-offline']";
	
	public static String AlertMsg = "결제를 취소하시겠습니까?";
	public static String AlertMsg2 = "미납으로 처리하시겠습니까?";
	public static String AlertMsg3 = "결제 완료 처리를 진행 하시겠습니까?";
	
	//기간 : x, 파트너사 : rsupkor, 결제상태 : 전체, 결제방식 : card
	private static String searchCardPayment_URI = "?search.dateCondition=ALL&search.penalty=false&search.planFor=&search.paymentStatus=S&search.paymentMethod=CARD&search.partnerName=&search.partnerId=&search.keywordCondition=COMPANY_NAME&search.keyword=테스트+고객사3&search.total=true&_search.total=on";
	//기간 : x, 파트너사 : rsupkor, 결제상태 : 실패
	private static String searchFailPayment_URI = "?search.dateCondition=ALL&search.penalty=false&search.planFor=&search.paymentStatus=F&search.paymentMethod=&search.partnerName=rsupkor&search.partnerId=000000005def4da1015def73bb580000&search.keywordCondition=COMPANY_NAME&search.keyword=&search.total=true&_search.total=on";
	
	public String Code;
	public String Companyname;
	public String src;
	
	public static WebDriver driver;
	
	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		CommonValues comm = new CommonValues();
		comm.setDriverProperty(browsertype);

		driver = comm.setDriver(driver, browsertype, "lang=ko_KR", true);
		
		context.setAttribute("webDriver", driver);
	}
	
	@Test(priority = 1, enabled = true)
	public void loginPartners() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='language']")));

		if (!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.LOGIN_URI + " [Actual]" + driver.getCurrentUrl();
		}

		comm.login(driver, CommonValues_Partners.KO_PARTNER, CommonValues_Partners.PW);

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 2, enabled = true)
	public void goPayment() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.PAYMENT_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("결제 관리")) {
			failMsg = "1.Wrong Menu [Expected] 결제 관리 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void searchPayment_All() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(ALL_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(ALL_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.planFor']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.planFor']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[5]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void searchPayment_ID() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(IDPREPAYMENT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(IDPREPAYMENT_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.planFor']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.planFor']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[5]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void searchPayment_Conference() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CONFERENCEPREPAYMENT_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(CONFERENCEPREPAYMENT_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.planFor']/option[4]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.planFor']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[5]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
		
	@Test(priority = 6, enabled = true)
	public void searchPayment_Weighted() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(WEIGHTED_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(WEIGHTED_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.planFor']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.planFor']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[5]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void searchPayment_ResultAll() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(RESULTALL_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(RESULTALL_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void searchPayment_ResultCard() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(RESULTCARD_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(RESULTCARD_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentMethod']/option[2]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentMethod']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void searchPayment_ResultOffline() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(RESULTOFFLINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(RESULTOFFLINE_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		if(!driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).getAttribute("selected").contentEquals("true")) {
			failMsg = "Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='date-condition']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n2.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentStatus']/option[@selected='selected']")).getText();
		}
		
		if(!driver.findElement(By.xpath("//select[@id='search.paymentMethod']/option[3]")).getAttribute("selected").contentEquals("true")) {
			failMsg = failMsg + "\n3.Wrong Search condition :" + driver.findElement(By.xpath("//select[@id='search.paymentMethod']/option[@selected='selected']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void searchPayment_Code() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.PAYMENT_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[@class='btn btn-shortcut rapid-search-btn active']")));

		//기간 선택
		driver.findElement(By.xpath("//select[@id='date-condition']")).click();
		driver.findElement(By.xpath("//select[@id='date-condition']//option[1]")).click();
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		list.get(0).findElement(By.xpath(".//td[2]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		Companyname = driver.findElement(By.xpath("//input[@id='companyName']")).getAttribute("value").toLowerCase();
		System.out.println(Code);
		System.out.println(Companyname);
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));
		
		searchdata(1, Code);
		Thread.sleep(1000);
		checkData(1, Code);
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void searchPayment_Companyname() throws Exception {
		
		searchdata(2, Companyname);
		Thread.sleep(1000);
		checkData(2, Companyname);
	}
	
	@Test(priority = 12, enabled = true)
	public void searchPayment_Email() throws Exception {
		
		searchdata(3, mandatory.CommonValues.ADMEMAIL);
		Thread.sleep(1000);
		checkData(3, mandatory.CommonValues.ADMEMAIL);
	}
	
	@Test(priority = 13, enabled = true)
	public void excelPayment() throws Exception {
		String failMsg = "";
		
		comm.checkExcelFile("payment-list");
		
		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		int ROWcount = rows.size();
		List<WebElement> column = driver.findElements(By.xpath("//tr[1]/td"));
		int Columncount = column.size()-2;

		// Excel Download
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary excel']")).click();
		
		TimeUnit.SECONDS.sleep(5);
		
		String[][] data = new String[ROWcount][Columncount];

		System.out.println("ROWcount " + ROWcount);
		System.out.println("Columncount " + Columncount);
		for (int i = 0; i < ROWcount; i++) {
			for (int j = 0; j < Columncount; j++) {
				
				if(j >= 5) {
					data[i][j] = rows.get(i)
							.findElement(By.xpath( ".//td[" + (j + 2) + "]")).getText();
				} else {
					
				data[i][j] = rows.get(i)
						.findElement(By.xpath( ".//td[" + (j + 1) + "]")).getText();
				Thread.sleep(100);
				}
				
				String webD = data[i][j];
				String excelD = readExcelFile(comm.Excelpath("payment-list"), i, j);
				
				
				if(j == 6) {
					// 날짜 포맷이 다르다 변환필요
					excelD = excelD.substring(0,10);
					
				}
				
				if(j == 7) {
					// 날짜 포맷이 다르다 변환필요
					excelD = excelD.substring(0,10);
				
				}
				
				if(j == 9) {
					
					if(excelD.isEmpty() == true) {
						excelD = "";
					}else {
					excelD = excelD.substring(0,10);
					}
				}
				
				if (!webD.contentEquals(excelD) ){
					failMsg = failMsg + "\nNot equal data "  +i + "," + j + " : [WEB]" +webD + "[Excel]"
							+ excelD;
				}
				
			}
		}

		CommonValues_Partners.deleteExcelFile(comm.Excelpath("payment-list"));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 14, enabled = true)
	public void pagingPayment() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.PAYMENT_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		driver.findElement(By.xpath(ALL_BTN)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if (rows.size() != 30) {
			failMsg = "list rows [Expected]30 [Actual]" + rows.size();
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	//청구서 포함하는 리스트의 부모 xpath : //button[@class='btn btn-table btn-success partner-customer-bill']//ancestor::tr
	//청구서 img xpath : //div[@class='modal-header']/img
	
	@Test(priority= 15, enabled = true)
	public void checkBillUI() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.PAYMENT_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		List<WebElement> billlist = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success partner-customer-bill']//ancestor::tr"));
		
		billlist.get(1).findElement(By.xpath(".//td[2]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		//find companycode
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		billlist = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success partner-customer-bill']//ancestor::tr"));
		
		billlist.get(1).findElement(By.xpath(".//td[3]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "파트너사 관리"));
		//find img src
		if(!driver.findElements(By.xpath("//div/img")).isEmpty()) {
			src = driver.findElement(By.xpath("//div/img")).getAttribute("src");
			System.out.println(src);
		}
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));
		
		billlist = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success partner-customer-bill']//ancestor::tr"));
		//find plan
		String plan = billlist.get(1).findElement(By.xpath(".//td[4]/a")).getText();
		
		//find term
		String term = billlist.get(1).findElement(By.xpath(".//td[8]")).getText() + billlist.get(1).findElement(By.xpath(".//td[9]")).getText();
		String mterm = term.replace("/", "");
		System.out.println(mterm);
		
		//find price
		String price = billlist.get(1).findElement(By.xpath(".//td[10]/span")).getText();
		String mprice = price.replaceAll("[^0-9]", "");
		System.out.println(mprice);

		List<WebElement> billbtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success partner-customer-bill']"));
		
		billbtn.get(1).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='modalBillBody']")));

		//check bill
		if(!driver.findElements(By.xpath("//div[@class='modal-header']/img")).isEmpty()) {
			String bill_src = driver.findElement(By.xpath("//div[@class='modal-header']/img")).getAttribute("src");
			
			if(!bill_src.contentEquals(src)) {
				failMsg = "Wrong src [Expected]" + src + " [Actual]" + bill_src;  
			}
			
		}else {
			driver.findElement(By.xpath("//button[@class='btn btn-default non-print']")).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='modalBillBody']")));
			billbtn.get(2).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='modalBillBody']")));

		}
		//check plan
		if(!driver.findElement(By.xpath("//label[@class='label-product']")).getText().contains(plan)) {
			failMsg = failMsg + "\n2.Wrong plan [Expected]" + plan + " [Actual]" + driver.findElement(By.xpath("//label[@class='label-product']")).getText();
		}
		//check term
		if(!driver.findElement(By.xpath("//p[@class='info line-height-1']")).getText().replaceAll("[^0-9]", "").contentEquals(mterm)) {
			failMsg = failMsg + "\n3.Wrong term [Expected]" + mterm + " [Actual]" + driver.findElement(By.xpath("//p[@class='info line-height-1']")).getText();
		}
		System.out.println(driver.findElement(By.xpath("//div[@id='modalBillBody']/div[2]/p[2]")).getText());
		//check price
		if(!driver.findElement(By.xpath("//span[@class='currency']")).getText().replaceAll("[^0-9]", "").contentEquals(mprice)) {
			failMsg = failMsg + "\n4.Wrong price [Expected]" + mprice + " [Actual]" + driver.findElement(By.xpath("//span[@class='currency']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 16, enabled = true)
	public void closeBill() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//div[@id='modalBillBody']//button[1]")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='modalBillBody']")));
		
		if(!driver.findElements(By.xpath("//div[@id='modalBillBody']")).isEmpty())
		{
			failMsg = "Bill is display";
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 17, enabled = true)
	public void checkCancelCardPaymentAlert() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI + searchCardPayment_URI);
		
		comm2.waitForLoad(driver);
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));
		
		List<WebElement> cancelbtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-danger payment-cancel']"));
		
		cancelbtn.get(0).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if(comm.isAlertPresent(driver) == true) {
			failMsg = failMsg + "\n2.Alert is exist";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 18, enabled = true)
	public void checkCancelOfflinePaymentAlert() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(RESULTOFFLINE_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(RESULTOFFLINE_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		List<WebElement> cancelbtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-danger paidCancel']"));
		
		cancelbtn.get(0).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg2)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg2 + " [Actual]" + alert_msg;
		}
		
		if(comm.isAlertPresent(driver) == true) {
			failMsg = failMsg + "\n2.Alert is exist";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		} 
	}
	
	@Test(priority= 19, enabled = true)
	public void checkCompletePaymentAlert() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(ALL_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(By.xpath(ALL_BTN), "class", "btn btn-shortcut rapid-search-btn active"));
		
		List<WebElement> cancelbtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-success paid']"));
		
		cancelbtn.get(0).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.dismiss();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg3)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg3 + " [Actual]" + alert_msg;
		}
		
		if(comm.isAlertPresent(driver) == true) {
			failMsg = failMsg + "\n2.Alert is exist";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		} 
	}
	
	@Test(priority= 20, enabled = true)
	public void checkAmount() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.PAYMENT_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

		//데이터 없을 경우
		if(driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]")).isEmpty()) {
			driver.findElement(By.xpath("//button[@id='id-prepayment']")).click();
			comm2.waitForLoad(driver);
			Thread.sleep(1000);
		}
		
		List<WebElement> amountlist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]"));
		List<WebElement> Companylist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[2]/a"));
		List<WebElement> Licenselist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[4]/a"));
		List<WebElement> Pricelist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[10]/span"));
		
		for(int i = 0; i < amountlist.size(); i++) {
			String listamount = amountlist.get(i).getText();
			String pricelist = Pricelist.get(i).getText();
			
			Licenselist.get(i).click();
			
			wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "라이선스 관리"));
			
			//결제정보에서 수량 찾기
			String infoamount = driver.findElement(By.xpath("//td[7][(contains(text(),'" + pricelist + "'))]//..//td[6]")).getText();
			
			if(!listamount.contentEquals(infoamount)) {
				failMsg = "Amount is wrong [Wrong row]" + (i+1) +  "[Company Name]" + Companylist.get(i).getText() + "[Expect Amount]" + infoamount + " [Actual Amount]" + listamount;
			}
			
			Thread.sleep(1000);
			
			driver.navigate().back();
			
			wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));
			
			amountlist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]"));
			Licenselist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[4]/a"));
			Companylist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[2]/a"));
			Pricelist = driver.findElements(By.xpath("//td[6][not(contains(text(),'-'))]//ancestor::tr/td[10]/span"));
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		} 
	}
	
	@Test(priority= 21, enabled = true)
	public void failPayment() throws Exception {
		String failMsg = "";
		
		driver.get(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.PAYMENT_URI + searchFailPayment_URI);

		comm2.waitForLoad(driver);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row active']")));

		List<WebElement> faillist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));

		for (int i = 0; i < faillist.size(); i++) {

			if (!faillist.get(i).findElement(By.xpath(".//td[7]//span")).getText().contentEquals("실패")) {
				failMsg = "Payment status is wrong [Rownum]" + (i + 1) + " [Actual]"
						+ faillist.get(i).findElement(By.xpath(".//td[7]/span")).getText();
			}
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority= 22, enabled = true)
	public void failPayment_Tooltip() throws Exception {
		String failMsg = "";

		List<WebElement> faillist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));

		Actions action = new Actions(driver);
		WebElement tooltip = faillist.get(0).findElement(By.xpath(".//td[7]//span"));
		Thread.sleep(1000);
		action.moveToElement(tooltip).build().perform();
		Thread.sleep(3000);

		Boolean isToolTipDisplayed = driver.findElement(By.xpath("//div[@class='tooltip fade bottom in']"))
				.isDisplayed();
		System.out.println("Is Tooltip displayed ? : " + isToolTipDisplayed);

		if (isToolTipDisplayed == true) {
			String tooltipText = driver.findElement(By.xpath("//div[@class='tooltip fade bottom in']/div[2]"))
					.getText();
			System.out.println("Tooltip Text:- " + tooltipText);
		} else {
			Exception e = new Exception("Payment fail tooltip is not displayed");
			throw e;
		}

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		driver.quit();
		
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public void searchdata(int i, String data) {
		//1=Code, 2=CompanyName, 3=Email
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search.keywordCondition']/option[" + i +"]")).click();
		
		WebElement input = driver.findElement(By.xpath("//input[@id='search-keywordString']"));
		
		while(!input.getAttribute("value").isEmpty() || !input.getText().isEmpty()) {
			input.sendKeys(Keys.BACK_SPACE); }
		
		
		input.sendKeys(data);
		
		if(i == 3) {
			driver.findElement(By.xpath("//select[@id='date-condition']")).click();
			driver.findElement(By.xpath("//select[@id='date-condition']/option[1]")).click();
			
		}
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
	}
	
	public void checkData(int i, String data) throws Exception {
		//1=Code, 2=CompanyName, 3=Email
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		String result;
		
		for (int j = 0; j < list.size(); j++) {

			switch (i) {
			case 1:
				result = list.get(j).findElement(By.xpath(".//td[2]/a")).getText().toLowerCase();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 2:
				result = list.get(j).findElement(By.xpath(".//td[2]/a")).getText().toLowerCase();

				if (!result.contains(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 3:
				
				list.get(j).findElement(By.xpath(".//td[2]/a")).click();

				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
				
				Thread.sleep(1500);

				driver.findElement(By.xpath("//input[@id='searchKeyword']")).sendKeys(data);
				Thread.sleep(1000);
				driver.findElement(By.xpath("//button[@id='doSearch']")).click();
				Thread.sleep(1000);
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//tbody[@id='handleTableUserRow']//td[2]")), data));
				
				result = driver.findElement(By.xpath("//tbody[@id='handleTableUserRow']//td[2]")).getText();
				System.out.println(result);

				if (!result.contentEquals(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}

				driver.navigate().back();
				
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "결제 관리"));

				Thread.sleep(2000);
				searchdata(i, data);
				Thread.sleep(2000);

				list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));

				break;
				}
			}
		}
	
	public String readExcelFile(String filepath, int x, int y) throws Exception {

		File file = new File(filepath);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook testDataWorkBook = new XSSFWorkbook(inputStream);
		Sheet testDataSheet = testDataWorkBook.getSheetAt(0);

		int rowCount = testDataSheet.getLastRowNum();
		
		if(rowCount == 0) {
			testDataWorkBook.close();
			return null;
			
		} else {
			int cells = testDataSheet.getRow(0).getPhysicalNumberOfCells();

			DataFormatter formatter = new DataFormatter();

			String[][] data = new String[rowCount][cells];

			for (int i = 0; i < rowCount; i++) {

				// 첫 행 제외
				Row row = testDataSheet.getRow(i + 1);

				for (int j = 0; j < cells; j++) {
					
					Cell cell = row.getCell(j);
					String a = formatter.formatCellValue(cell);

					data[i][j] = a;
				}
			}
			testDataWorkBook.close();
			return data[x][y];
		}
	}

}
