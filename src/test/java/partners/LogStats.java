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
 * 2.사용현황 화면 이동
 * 3.고객코드 조건 검색 확인
 * 4.고객사명 조건 검색 확인
 * 5.이메일 조건 검색 확인
 * 6.엑셀 데이터 확인
 * 7.30개 단위 페이징 확인
 * 8.회의 내역 선택 후 이동 확인
 */

public class LogStats {
	
	public String Code;
	public String Companyname;
	
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
	public void goLogStats() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.LOGSTATS_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("사용현황")) {
			failMsg = "1.Wrong Menu [Expected] 사용현황 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCELOGSTAT_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCELOGSTAT_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}

	@Test(priority = 3, enabled = true)
	public void searchLogStats_Code() throws Exception {
		
		driver.findElement(By.xpath(CommonValues_Partners.LOGSTATS_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));

		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td")).getText().contentEquals("데이터가 없습니다.")) {
			driver.findElement(By.xpath("//button[@data-term='-90']")).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row']")));
			
			list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
	
		}
		
		list.get(0).findElement(By.xpath(".//td[1]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']/h2")), "고객사 관리"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		Companyname = driver.findElement(By.xpath("//input[@id='companyName']")).getAttribute("value");
		System.out.println(Code);
		System.out.println(Companyname);
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));
		
		searchdata(1, Code);
		Thread.sleep(1000);
		checkData(1, Code);
		
	}
	
	@Test(priority = 4, enabled = true)
	public void searchLogStats_Companyname() throws Exception {
		
		searchdata(2, Companyname);
		Thread.sleep(1000);
		checkData(2, Companyname);
	}
	
	@Test(priority = 5, enabled = true)
	public void searchLogStats_Email() throws Exception {
		
		searchdata(3, mandatory.CommonValues.ADMEMAIL);
		Thread.sleep(1000);
		checkData(3, mandatory.CommonValues.ADMEMAIL);
	}
	
	@Test(priority = 6, enabled = true)
	public void excelLogStats() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LOGSTATS_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));

		comm.checkExcelFile("conference-log-stats");
		
		List<WebElement> rows = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		int ROWcount = rows.size();
		List<WebElement> column = driver.findElements(By.xpath("//tr[1]/td"));
		int Columncount = column.size()-1;

		// Excel Download
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary excel']")).click();
		
		TimeUnit.SECONDS.sleep(5);
		
		String[][] data = new String[ROWcount][Columncount];

		System.out.println("ROWcount " + ROWcount);
		System.out.println("Columncount " + Columncount);
		for (int i = 0; i < ROWcount; i++) {
			for (int j = 0; j < Columncount; j++) {
				
				data[i][j] = rows.get(i).findElement(By.xpath( ".//td[" + (j + 1) + "]")).getText();
				Thread.sleep(100);
				
				String webD = data[i][j];
				String excelD = readExcelFile(comm.Excelpath("conference-log-stats"), i, j);
				
				if(j == 0) {
					// 고객사명 공백 에러가 있어 공백 제거
					webD = webD.replace(" ", "");
					excelD = excelD.replace(" ", "");
					
				}
				
				if (!webD.contentEquals(excelD) ){
					failMsg = failMsg + "\nNot equal data "  +i + "," + j + " : [WEB]" +webD + "[Excel]"
							+ excelD;
				}
				
			}
		}

		CommonValues_Partners.deleteExcelFile(comm.Excelpath("conference-log-stats"));
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void pagingLogStats() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.LOGSTATS_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));

		String result = driver.findElement(By.xpath("//span[@class='result-info']")).getText().replaceAll("[^0-9]", "");
		int int_result = Integer.parseInt(result);
		
		if(int_result<30) {
			driver.findElement(By.xpath("//button[@data-term='-90']")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//input[@id='startDate']")).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'datepicker-dropdown')]")));
			
			for (int i = 0; i < 2; i++) {
				driver.findElement(By.xpath("//div[@class='datepicker-days']//th[@class='prev']")).click();
			}
		
			driver.findElement(By.xpath("//tr/td[@class='new day'][text()='1']")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			comm2.waitForLoad(driver);
			
		}
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.size() != 30) {
			failMsg = "list size is wrong [Expected] 30 [Actual]" + list.size();
		}
		
		List<WebElement> paginglist = driver.findElements(By.xpath("//div[@class='pagination']/li"));
		
		if(paginglist.size() == 1) {
			failMsg = failMsg + "\n2.paging size is wrong [Expected]" + ((int) Math.ceil((double) int_result / 30)+1) + " [Actual]" + paginglist.size();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void clickUsageBtn() throws Exception {
		String failMsg = "";
		
		List<WebElement> usagebtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-default btn-usage']"));
		
		usagebtn.get(0).findElement(By.xpath(".//ancestor::tr/td[1]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "고객사 관리 > 상세정보"));
		
		Code = driver.findElement(By.xpath("//input[@id='id']")).getAttribute("value");
		
		driver.navigate().back();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));

		usagebtn = driver.findElements(By.xpath("//button[@class='btn btn-table btn-default btn-usage']"));
		usagebtn.get(0).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "회의내역"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row search-box-limi2t active']")));
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI)) {
			failMsg = "Wrong Url [Expected]" + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.CONFERENCE_URI + "[Actual]" + driver.getCurrentUrl(); 
		}
		if(!driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value").contentEquals(Code)) {
			failMsg = failMsg + "\n2.Wrong Code Search [Expected]" + Code + " [Actual]" + driver.findElement(By.xpath("//input[@id='search-keywordString']")).getAttribute("value");
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
		driver.findElement(By.xpath("//select[@id='search-keywordCondition']")).click();
		driver.findElement(By.xpath("//select[@id='search-keywordCondition']/option[" + i +"]")).click();
		
		WebElement input = driver.findElement(By.xpath("//input[@id='search-keywordString']"));
		
		while(!input.getAttribute("value").isEmpty() || !input.getText().isEmpty()) {
			input.sendKeys(Keys.BACK_SPACE); }
		
		input.sendKeys(data);
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		comm2.waitForLoad(driver);
		
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(list.get(0).findElement(By.xpath(".//td")).getText().contentEquals("데이터가 없습니다.")) {
			driver.findElement(By.xpath("//button[@data-term='-90']")).click();
			
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='search-box-row']")));
			
			driver.findElement(By.xpath("//button[@type='submit']")).click();
			comm2.waitForLoad(driver);
		}
		
	}
	
	public void checkData(int i, String data) throws Exception {
		//1=Code, 2=CompanyName, 3=Email
		List<WebElement> list = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		String result;
		
		for (int j = 0; j < list.size(); j++) {

			switch (i) {
			case 1:
				result = list.get(j).findElement(By.xpath(".//td[1]/a")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 2:
				result = list.get(j).findElement(By.xpath(".//td[1]/a")).getText();

				if (!result.contentEquals(Companyname)) {
					Exception e = new Exception("Wrong data [RowNum]" + (j + 1) + "[SearchData]" + data + "[Data]" + result);
					throw e;
				}
				break;

			case 3:
				
				list.get(j).findElement(By.xpath(".//td[1]/a")).click();

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
				
				wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "사용현황"));
				
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
