package partners;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import mandatory.CommonValues;

public class QnA {
	
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
	public void goQuestion() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.QUESTION_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("문의하기")) {
			failMsg = "1.Wrong Menu [Expected] 문의하기 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTION_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTION_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 3, enabled = true)
	public void searchQuestion() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//select[@id='search.answer']")).click();
		driver.findElement(By.xpath("//select[@id='search.answer']/option[2]")).click();
		
		List <WebElement> answertitle = driver.findElements(By.xpath("//td[5][contains(text(),'/')]/ancestor::tr"));
		
		String title = answertitle.get(0).findElement(By.xpath(".//td[3]/a")).getText();
		String anwserdate = answertitle.get(0).findElement(By.xpath(".//td[5]")).getText();
		
		driver.findElement(By.xpath("//input[@id='search-keywordString']")).sendKeys(title);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(2000);
		
		List <WebElement> searchlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(!searchlist.get(0).findElement(By.xpath(".//td[3]/a")).getText().contentEquals(title)) {
			failMsg = "Wrong Search [Expected]" + title + " [Actual]" + searchlist.get(0).findElement(By.xpath(".//td[3]/a")).getText();
		}
		
		if(!searchlist.get(0).findElement(By.xpath(".//td[5]")).getText().contentEquals(anwserdate)) {
			failMsg = failMsg + "\n2.Wrong Search [Expected]" + anwserdate + " [Actual]" + searchlist.get(0).findElement(By.xpath(".//td[5]")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 4, enabled = true)
	public void goAddQuestion() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.QUESTION_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));

		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "문의 등록 > 신규 등록"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI)) {
			failMsg =  "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI + " [Actual]" + driver.getCurrentUrl();
		}
	}

	
	public void CreateQuestion(String Title, String Context) throws Exception {
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "문의 등록 > 신규 등록"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI)) {
			Exception e = new Exception("Wrong Url");
			throw e;
		}
		
		driver.findElement(By.xpath("//input[@id='title']")).sendKeys(Title);
		driver.findElement(By.xpath("//input[@id='qnaQuestion']")).sendKeys(Context);
		
		driver.findElement(By.xpath("//button[@id='qnaSave']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='qnaDelete']")));

	}
}
