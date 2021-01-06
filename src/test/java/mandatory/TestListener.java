package mandatory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.rsupport.rm.GoogleSheets;

public class TestListener implements ITestListener {
	WebDriver driver=null;

	String filePath_win = System.getProperty("user.dir") + "\\target\\surefire-reports\\fail_";
	String filePath_mac = System.getProperty("user.dir") + "/target/surefire-reports/fail_";
	String sharedPath ="";
	String filePath = "";
	String fileuri = "/target/surefire-reports";
	
    @Override
    public void onTestFailure(ITestResult result) {
    	
    	String type = result.getInstanceName().split("\\.")[0];
    
    	String methodName=result.getName().toString().trim();
    	System.out.println("***** Error " + methodName +" test has failed *****");
    	
    	//스프레드 시트에 결과
    	GoogleSheets sheet= new GoogleSheets(type);
		try {
			sheet.checkdata(result.getName().toString(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver)context.getAttribute("webDriver");
        
        
		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// check date
		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");


		if(System.getProperty("os.name").toLowerCase().contains("win"))
			filePath = filePath_win + format2.format(cal.getTime()) + "\\";
		else 
			filePath = filePath_mac + format2.format(cal.getTime()) + "/";
		
		//sharedPath = sharedPath_pre + format2.format(cal.getTime()) + "/";
		sharedPath = filePath;
        File d = new File(filePath);
        if (!d.isDirectory()) d.mkdir();
        
        if(CommonValues.FOR_JENKINS) return;
        
        if (driver != null) takeScreenShot(methodName, driver);
    	
    	if ((WebDriver)context.getAttribute("webDriver2") != null) takeScreenShot(methodName + "WD2", (WebDriver)context.getAttribute("webDriver2"));
    	
    	if ((WebDriver)context.getAttribute("webDriver3") != null) takeScreenShot(methodName + "WD3", (WebDriver)context.getAttribute("webDriver3"));
    	if ((WebDriver)context.getAttribute("webDriver4") != null) takeScreenShot(methodName + "WD4", (WebDriver)context.getAttribute("webDriver4"));
    	if ((WebDriver)context.getAttribute("webDriver5") != null) takeScreenShot(methodName + "WD5", (WebDriver)context.getAttribute("webDriver5"));
    	if ((WebDriver)context.getAttribute("webDriver6") != null) takeScreenShot(methodName + "WD6", (WebDriver)context.getAttribute("webDriver6"));
    }
    
    public void takeScreenShot(String methodName, WebDriver driver) {
    	 File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with test method name 
    	
    	 String filename = String.format("%s-%s", methodName, Calendar.getInstance().getTimeInMillis());
            try {
            	File destFile = new File(filePath+filename+".png");
            	String jkpath = sharedPath + filename + ".png";
				FileUtils.copyFile(scrFile, destFile);
				System.out.println("***Placed screen shot file name : "+destFile.getName()+" ***");
				//Reporter.log("<a href='"+ destFile.getName()+ "'> <img src='"+ destFile.getName() + "' height='100' width='100'/> </a>");
				Reporter.log("<a href='"+ jkpath + "'> <img src='"+ jkpath + "' height='100' width='100'/> </a>");
			} catch (IOException e) {
				e.printStackTrace();
			}
    }
	public void onFinish(ITestContext context) {}
  
    public void onTestStart(ITestResult result) { 
    	System.out.println("start : " + result.getName().toString());
    }
  
    public void onTestSuccess(ITestResult result) { 
    	
    	String type = result.getInstanceName().split("\\.")[0];
   
    	System.out.println("test success : " + result.getName().toString());
    	//스프레드 시트에 결과
    	GoogleSheets sheet= new GoogleSheets(type);
		try {
			sheet.checkdata(result.getName().toString(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void onTestSkipped(ITestResult result) {   }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {   }

    public void onStart(ITestContext context) { 
 }
}  