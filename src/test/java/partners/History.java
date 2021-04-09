package partners;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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
 * 2.회의 내역 화면 이동 확인
 * 3.CID링크 선택 후 화면 이동 확인
 * 4.그룹명 링크 선택 후 화면 이동 확인
 * 5.코드 조건으로 검색
 * 6.고객사명 조건으로 검색
 * 7.이메일 조건으로 검색
 * 8.CID 조건으로 검색
 * 9.CID 조건으로 엑셀 다운로드 후 데이터 확인
 * 10.검색 조건 없이 엑셀 다운로드 시도
 * 11.
 * 12.파트너사 계정으로 로그인 후 회의내역 메뉴 유무 확인
 */

public class History {
	
	public static WebDriver driver;
	
	public String CID;
	public String Code;
	public String Companyname;
	public String Email;
	
	public static String AlertMsg = "CID 또는 고객코드로 검색한 내역만 엑셀로 출력 가능합니다.";
	
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
	public void goHistory() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("회의내역")) {
			failMsg = "1.Wrong Menu [Expected] 회의내역 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void clickCID() throws Exception {
		String failMsg = "";
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		CID = list.get(0).findElement(By.xpath(".//td[8]/a")).getText();
		Email = list.get(0).findElement(By.xpath(".//td[10]")).getText();
		
		list.get(0).findElement(By.xpath(".//td[8]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "회의 상세"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI + "/" + CID)) {
			failMsg = "Wrong URL [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI + "/" + CID + "[Actual]"
					+ driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void clickCode() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));

		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		list.get(0).findElement(By.xpath(".//td[9]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		Companyname = driver.findElement(By.xpath("//input[@id='companyName']")).getAttribute("value");
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code)) {
			failMsg = "Wrong URL [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code + "[Actual]"
					+ driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void searchHistory_Code() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));
		
		searchdata(1, Code);
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		list.get(0).findElement(By.xpath(".//td[9]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code)) {
			failMsg = "Wrong URL [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.COMPANY_URI + "/" + Code + "[Actual]"
					+ driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void searchHistory_Companyname() throws Exception {

		driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));
		
		searchdata(2, Companyname);
		System.out.println(Companyname);
		Thread.sleep(3000);
		
		checkData(2, Companyname);
	}
	
	@Test(priority = 7, enabled = true)
	public void searchHistory_Email() throws Exception {
		
		searchdata(3, Email);
		System.out.println(Email);
		Thread.sleep(3000);
		
		checkData(3, Email);
	}
	
	@Test(priority = 8, enabled = true)
	public void searchHistory_CID() throws Exception {
		
		searchdata(4, CID);
		System.out.println(CID);
		Thread.sleep(3000);
		
		checkData(1, CID);
		
	}
	
	@Test(priority = 9, enabled = true)
	public void excelsearchCID() throws Exception {
		String failMsg = "";
		
		comm.checkExcelFile("conference-log-list");
		
		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		int ROWcount = rows.size();
		List<WebElement> column = driver.findElements(By.xpath("//td"));
		int Columncount = column.size();
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table excel']")).click();
		
		TimeUnit.SECONDS.sleep(5);
		
		String[][] data = new String[ROWcount][Columncount];

		System.out.println("ROWcount " + ROWcount);
		System.out.println("Columncount " + Columncount);
		for (int i = 0; i < ROWcount; i++) {
			for (int j = 0; j < Columncount; j++) {

				data[i][j] = rows.get(i).findElement(By.xpath(".//td[" + (j + 1) + "]")).getText();
				Thread.sleep(100);

				String webD = data[i][j];
				String excelD = readExcelFile(comm.Excelpath("conference-log-list"), i, j);

				if (j == 2) {

					if (excelD.isEmpty() == true && webD.isEmpty()) {
						excelD = "";
						webD = "";
					}

					if (!webD.contentEquals(excelD)) {
						failMsg = failMsg + "\nNot equal data " + i + "," + j + " : [WEB]" + webD + "[Excel]" + excelD;
					}

				}
			}
		}
		CommonValues_Partners.deleteExcelFile(comm.Excelpath("conference-log-list"));

		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void excelHistory() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));
		
		driver.findElement(By.xpath("//button[@class='btn btn-primary btn-table excel']")).click();
		
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		alert.accept();
		
		System.out.println(alert_msg);

		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "1.Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 11, enabled = true)
	public void checkHistoryMenu() throws Exception {
		String failMsg = "";
		
		comm.logout(driver);
		
		comm.login(driver, mandatory.CommonValues.PARTNERTEST_EMAIL, mandatory.CommonValues.USERPW);
		
		if(!driver.findElements(By.xpath(CommonValues_Partners.HISTORY_BTN)).isEmpty()) {
			failMsg = "History Menu is display";
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
		//1=Code, 2=CompanyName, 3=Email, 4=CID 
		driver.findElement(By.xpath("//select[@id='search-keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search-keywordCondition']/option[" + i +"]")).click();
		
		WebElement input = driver.findElement(By.xpath("//input[@id='search-keywordString']"));
		
		while(!input.getAttribute("value").isEmpty() || !input.getText().isEmpty()) {
			input.sendKeys(Keys.BACK_SPACE); }
		
		
		input.sendKeys(data);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
	}
	
	public void checkData(int i, String data) throws Exception {
		//1=CID, 2=CompanyName, 3=Email
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		String result;
		
		for (int j = 0; j < list.size(); j++) {

			switch (i) {
			case 1:
				result = list.get(j).findElement(By.xpath(".//td[8]/a")).getText();

				if (!result.contains(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[Data]" + result);
					throw e;
				}
				break;

			case 2:
				result = list.get(j).findElement(By.xpath(".//td[9]/a")).getText();

				if (!result.contains(data)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[Data]" + result);
					throw e;
				}
				break;

			case 3:
				result = list.get(j).findElement(By.xpath(".//td[10]")).getText();

				if (!result.contains(data)) {
					list.get(j).findElement(By.xpath(".//td[8]/a")).click();

					WebDriverWait wait = new WebDriverWait(driver, 10);
					wait.until(ExpectedConditions.textToBePresentInElement(
							driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "회의 상세"));

					List<WebElement> endpointemail = driver.findElements(By.xpath("//td[contains(text(), '@')]"));

					String[] a = new String[endpointemail.size()];

					for (int k = 0; k < endpointemail.size(); k++) {
						String b = endpointemail.get(k).getText();

						a[k] = b;
					}

					if (Arrays.asList(a).contains(data) == false) {
						Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[Email]" + Arrays.toString(a));
						throw e;
					}

					driver.findElement(By.xpath(CommonValues_Partners.HISTORY_BTN)).click();

					wait.until(ExpectedConditions.textToBePresentInElement(
							driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));

					searchdata(i, data);
					Thread.sleep(2000);

					list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));

					break;
				}
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
					
					if(j == 8 || j ==11) continue;
					
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
	

