package com.screenshotwithExtentReport;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class freeFacebookTest {

	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;

	@BeforeClass
	public void setExtent() {
		extent = new ExtentReports(System.getProperty("user.dir") + "\\test-output\\extentReport.html", true);
		// user.name means project file path and + added upto extentReport file.
		// you can pass below environment
		extent.addSystemInfo("Host name", "Aarna377");
		extent.addSystemInfo("User Name", "Vijay");
		extent.addSystemInfo("Environment", "QA");
	}

	@AfterTest
	public void endreport() {
		extent.flush(); // closed the connection with extent report
		extent.close();

	}

	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots under src
		// folder"
		String destination = System.getProperty("user.dir") + "\\FailedTestesScreenshots\\" + screenshotName + dateName
				+ ".png";

		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;

	}

	@BeforeMethod
	public void setUp() {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\AARNA377.000\\Desktop\\Vijay_Dcuments\\SeleniumDriverTools\\ChromeDriver_Version 80.0.3987.122 (64-bit)\\chromedriver_win32\\chromedriver.exe");

		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		driver.get("https://www.facebook.com/");
	}

	@Test
	public void facebookTitleTest() {
		extentTest = extent.startTest("facebookTitleTest"); //each test case have to write this line
		String title = driver.getTitle();
		System.out.println(title);
		Assert.assertEquals(title, "Facebook - Log In or Sign U");
	}
	@Test
	public void facebookLogoTest() {
		extentTest = extent.startTest("Facebook Logo Test");
		boolean logo = driver.findElement(By.xpath("//*[@id=\"blueBarDOMInspector\"]/div/div/div/div[1]/h1/a/i")).isDisplayed();
		Assert.assertEquals(logo, true);
		
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		
		if(result.getStatus()==ITestResult.FAILURE) {
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName()); //to add name in extent report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable()); //to add error/exception in extent report
			
			String screenshotPath = freeFacebookTest.getScreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //to add screenshot in extend report
			extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath)); //to add video in extend report
		}
		else if(result.getStatus()==ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP, "TEST CASE SKIPPED IS " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "TEST CASE PASSED IS " + result.getName());
		}
		
		extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
		driver.quit();
	}

}
