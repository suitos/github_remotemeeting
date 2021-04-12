package partners;

import static org.testng.Assert.fail;

import java.util.List;

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
 * 2.문의하기 화면 이동
 * 3.문의하기 검색 조건 확인
 * 4.문의 등록 선택 후 화면 이동
 * 5.문의 등록 선택 후 목록 선택
 * 6.문의글 저장
 * 7.문의글 삭제 취소
 * 8.문의글 삭제
 * 9.답변하기 화면 이동
 * 10.답변하기 검색 조건 확인
 * 11.문의글 상세정보 화면 이동
 * 12.답변하기 목록 이동
 * 13.답변 저장 확인 
 */

public class QnA {
	
	public static WebDriver driver;
	
	public static String AlertMsg = "삭제하시겠습니까";
	
	public String title;
	public String context;
	
	
	CommonValues_Partners comm = new CommonValues_Partners();
	mandatory.CommonValues comm2 = new CommonValues();
	
	private StringBuffer verificationErrors = new StringBuffer();

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	public void setUp(ITestContext context, String browsertype) throws Exception {

		driver = comm2.setDriver(driver, browsertype, "lang=ko_KR", true);
		
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
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTION_URI)) {
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
	public void goAddQuestionandCheck() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.QUESTION_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));

		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new']")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "문의 등록 > 신규 등록"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI)) {
			failMsg =  "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if(!driver.findElement(By.xpath("//input[@id='questionUser.company.name']")).isDisplayed() ||
				!driver.findElement(By.xpath("//input[@id='questionUser.company.phone']")).isDisplayed() ||
				!driver.findElement(By.xpath("//input[@id='questionUser.name']")).isDisplayed() ||
				!driver.findElement(By.xpath("//input[@id='questionUser.username']")).isDisplayed() ||
				!driver.findElement(By.xpath("//input[@id='qnaTitle']")).isDisplayed() ||
				!driver.findElement(By.xpath("//textarea[@id='qnaQuestion']")).isDisplayed()) {
					failMsg = failMsg + "\n2.Wrong UI";
				}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 5, enabled = true)
	public void goQuestionlist() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@class='btn btn-default spa-menu']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTION_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTION_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 6, enabled = true)
	public void saveQuestion() throws Exception {
		String failMsg = "";
		
		title = "자동화질문";
		context = "자동화 테스트용 질문입니다";
		
		CreateQuestion(title, context);
		
		driver.findElement(By.xpath(CommonValues_Partners.QUESTION_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));

		List <WebElement> questionlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(!questionlist.get(0).findElement(By.xpath(".//td[3]/a")).getText().contentEquals(title)) {
			failMsg = "Don't saved question - wrong title [Expected]" + title + " [Actual]" + questionlist.get(0).findElement(By.xpath(".//td[3]/a")).getText();
		}
		
		questionlist.get(0).findElement(By.xpath(".//td[3]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "문의 등록 > 상세정보"));
		
		if(!driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")).getText().contentEquals("문의 등록 > 상세정보")) {
			failMsg = failMsg + "\n2.Don't go question info";
		}

		if(!driver.findElement(By.xpath("//textarea[@id='qnaQuestion']")).getText().contentEquals(context)) {
			failMsg = failMsg + "\n3.Don't saved question - wrong context [Expected]" + context + " [Actual]" + driver.findElement(By.xpath("//textarea[@id='qnaQuestion']")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 7, enabled = true)
	public void cancelDeleteQuestion() throws Exception {
		String failMsg = "";
		
		String prev_url = driver.getCurrentUrl();
		
		driver.findElement(By.xpath("//button[@id='qnaDelete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		System.out.println(alert_msg);
		alert.dismiss();
		
		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		if(!driver.getCurrentUrl().contentEquals(prev_url)) {
			failMsg = failMsg + "\n2.Question is delete";
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 8, enabled = true)
	public void DeleteQuestion() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//button[@id='qnaDelete']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.alertIsPresent());
		
		Alert alert = driver.switchTo().alert();
		String alert_msg = alert.getText();
		System.out.println(alert_msg);
		alert.accept();
		
		if (!alert_msg.contentEquals(AlertMsg)) {
			failMsg = "Alert msg is wrong [Expected]" + AlertMsg + " [Actual]" + alert_msg;
		}
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "문의하기"));
		wait.until(ExpectedConditions.textToBe(By.xpath("//button[@class='btn btn-table btn-primary add-new']"), "문의 등록"));
		comm2.waitForLoad(driver);
		Thread.sleep(2000);
		
		List <WebElement> questionlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		if(questionlist.get(0).findElement(By.xpath(".//td[3]/a")).getText().contentEquals(title)) {
			failMsg = "Don't delete question - wrong title  [Actual]" + questionlist.get(0).findElement(By.xpath(".//td[3]/a")).getText();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 9, enabled = true)
	public void goAnswer() throws Exception {
		String failMsg = "";

		driver.findElement(By.xpath(CommonValues_Partners.ANSWER_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "답변 하기"));

		if (!driver.findElement(By.xpath("//div[@class='panel-header']")).getText().contentEquals("답변 하기")) {
			failMsg = "1.Wrong Menu [Expected] 답변 하기 [Actual]"
					+ driver.findElement(By.xpath("//div[@class='panel-header']")).getText();
		}
		
		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI)) {
			failMsg = failMsg + "\n2.Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 10, enabled = true)
	public void searchAnswer() throws Exception {
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
	
	@Test(priority = 11, enabled = true)
	public void goAnswerinfo() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath(CommonValues_Partners.ANSWER_BTN)).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "답변 하기"));

		List <WebElement> answerlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		answerlist.get(0).findElement(By.xpath(".//td[3]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "답변 하기 > 상세정보"));

		if(!driver.getCurrentUrl().contains(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI)) {
			failMsg =  "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 12, enabled = true)
	public void goAnswerlist() throws Exception {
		String failMsg = "";
		
		driver.findElement(By.xpath("//a[@class='btn btn-default spa-menu']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "답변 하기"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI)) {
			failMsg = "Wrong URL [Expected] " + CommonValues_Partners.PARTNER_URL + CommonValues_Partners.ANSWER_URI + " [Actual]" + driver.getCurrentUrl();
		}
		
		if (failMsg != null && !failMsg.isEmpty()) {
			Exception e = new Exception(failMsg);
			throw e;
		}
	}
	
	@Test(priority = 13, enabled = true)
	public void saveAnswer() throws Exception {
		String failMsg = "";
		
		context = "자동화 테스트용 답변입니다";
		
		List <WebElement> answerlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		answerlist.get(0).findElement(By.xpath(".//td[3]/a")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "답변 하기 > 상세정보"));

		driver.findElement(By.xpath("//textarea[@id='answer']")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.xpath("//textarea[@id='answer']")).sendKeys(Keys.BACK_SPACE); 
		
		Thread.sleep(1000);
		driver.findElement(By.xpath("//textarea[@id='answer']")).sendKeys(context);
		
		driver.findElement(By.xpath("//button[@id='answer-save']")).click();
		
		comm2.waitForLoad(driver);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(CommonValues_Partners.ANSWER_BTN)).click();

		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='panel-header']")), "답변 하기"));

		answerlist = driver.findElements(By.xpath("//tbody[@id='companyListWrapper']/tr"));
		
		answerlist.get(0).findElement(By.xpath(".//td[3]/a")).click();
		
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "답변 하기 > 상세정보"));

		if(!driver.findElement(By.xpath("//textarea[@id='answer']")).getText().contentEquals(context)) {
			failMsg = "Wrong save answer [Expected]" + context + " [Actual]" + driver.findElement(By.xpath("//textarea[@id='answer']")).getText();
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
	
	public void CreateQuestion(String Title, String Context) throws Exception {
		
		driver.findElement(By.xpath("//button[@class='btn btn-table btn-primary add-new']")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//div[@class='layer-right custom-breadcrumb']")), "문의 등록 > 신규 등록"));

		if(!driver.getCurrentUrl().contentEquals(CommonValues_Partners.PARTNER_URL + CommonValues_Partners.QUESTIONADD_URI)) {
			Exception e = new Exception("Wrong Url");
			throw e;
		}
		
		driver.findElement(By.xpath("//input[@id='qnaTitle']")).sendKeys(Title);
		driver.findElement(By.xpath("//textarea[@id='qnaQuestion']")).sendKeys(Context);
		
		driver.findElement(By.xpath("//button[@id='qnaSave']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='qnaDelete']")));

	}
}
