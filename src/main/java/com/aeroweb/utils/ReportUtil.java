package com.aeroweb.utils;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
//import ru.yandex.qatools.ashot.AShot;
//import ru.yandex.qatools.ashot.Screenshot;
//import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReportUtil {
	private static JsonUtils jsonObject = new JsonUtils();
	private ExtentHtmlReporter extentHtmlReporter;
    private static ThreadLocal<ExtentReports> extentReports=new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    String extentReportPath = "";
    private static ThreadLocal<String> extentReportPaths=new ThreadLocal<>();
	
	
	public String getReportFolder() {
		return extentReportPath;
	}
	
	public static String getExtentReportFolder() {
		return extentReportPaths.get();
	}
	
	public static void setExtentTest(ExtentTest tst) 
	{ 
		extentTest.set(tst); 
	}
	
	public static synchronized ExtentTest getExtentTest() 
	{ 
		return extentTest.get();
	}
	
	public void initReport(String scenarioName) {
	
		if (new JsonUtils().getJsonValue("spiraUpdate").equalsIgnoreCase("yes")) {
			extentReportPath = System.getProperty("user.dir") + "/reports/"
					+scenarioName+"_"+ new CompactUtil().getCurrentTimeStemp("yyyy_MM_dd_HH_mm_ss_SSS");
			extentReportPaths.set(extentReportPath);
		} else {
				extentReportPath = System.getProperty("user.dir") + "/reports/"
						+scenarioName+"_"+ new CompactUtil().getCurrentTimeStemp("yyyy_MM_dd_HH_mm_ss_SSS");
				extentReportPaths.set(extentReportPath);
		}

		new CompactUtil().createFolder(extentReportPaths.get());

		extentHtmlReporter = new ExtentHtmlReporter(extentReportPath + "/ExecutionSummaryReport" + ".html");
		System.out.println(new File("extent-config.xml").getAbsolutePath());

		extentHtmlReporter.loadXMLConfig(new File("extent-config.xml").getAbsolutePath());
		extentHtmlReporter.setAppendExisting(true);
		extentHtmlReporter.config().setDocumentTitle("Aero Web Execution Report");
		extentHtmlReporter.config().setReportName("Automation Regression Report");
		extentHtmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		extentHtmlReporter.config().setTheme(Theme.STANDARD);
	
		
		ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(extentHtmlReporter);

        extentReports.setSystemInfo("Host Name", new CompactUtil().getSystemName());
        extentReports.setSystemInfo("Environment", "SIT");
        extentReports.setSystemInfo("User Name", System.getProperty("user.name"));  
        ReportUtil.extentReports.set(extentReports);
       
	}

	public void initTest(String testName) {
		//ExtentTest extentTest = ReportUtil.extentTest.get();
		ExtentTest extentTest = ReportUtil.extentReports.get().createTest(testName);
        ReportUtil.extentTest.set(extentTest);
	}

	public String getStatus() {
		return ReportUtil.extentTest.get().getStatus().toString().toLowerCase();
	}

	public void logEvent(Status status, String description, boolean img) {
		if (img)
			ReportUtil.extentTest.get().log(status, description + captureScreenShot());
		else
			ReportUtil.extentTest.get().log(status, description);
		extentReports.get().flush();
	}

	public void logInfo(String msgToPrint) {
		String tcData = "";

		if (jsonObject.getScreenshotOption().equalsIgnoreCase("always"))
			tcData = captureScreenShot();

		ReportUtil.extentTest.get().log(Status.INFO, tcData + "<font color='black'>" + msgToPrint);
		extentReports.get().flush();

	}

	public void logPass(String expMsg, String actMsg) {
		String tcData = "";
		String msg = " Expected - " + expMsg + "<br> <b> <font color='green'> Actual - " + actMsg;

		if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass"))
			tcData = captureScreenShot();

		ReportUtil.extentTest.get().log(Status.PASS, tcData + "<font color='black'>" + msg);
		extentReports.get().flush();
	}

	public void logFail(String expMsg, String actMsg) {
		String tcData = "";
		String msg = " Expected - " + expMsg + "<br> <b> <font color='red'> Actual - " + actMsg;

		if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail"))
			tcData = captureScreenShot();
		ReportUtil.extentTest.get().log(Status.FAIL, tcData + "<font color='black'>" + msg);
		extentReports.get().flush();
	}

	public void logFailWithURlLink(String msgToPrint, String url) {
		String tcData = "";
		tcData = "<div align='right' style='float:right'><a target='_blank' href= '" + url
				+ "'>Link</a></div>";
		ReportUtil.extentTest.get().log(Status.INFO, tcData + "<font color='black'>" + msgToPrint);
		extentReports.get().flush();
	}
	

	public void compareResult(String logicalName, String expectedVal, String actualVal, boolean caseSensitive) {
		String msgToPrint = "Verify <B>'" + logicalName + "'</B> value:<BR>Expected: <B>'" + expectedVal
				+ "'</B><BR>Actual: <B>'" + actualVal + "'</B>";
		if (caseSensitive) {
			if (expectedVal == actualVal) {
				logPass(expectedVal, actualVal);
			} else {
				logFail(expectedVal, actualVal);
			}
		} else {
			if (expectedVal.equalsIgnoreCase(actualVal)) {
				logPass(expectedVal, actualVal);
			} else {
				logFail(expectedVal, actualVal);
			}
		}
	}

	public void logInfo(String msgToPrint, boolean takeScreenshot) {
		String tcData = "";
		if (takeScreenshot)
			tcData = captureScreenShot();
		ReportUtil.extentTest.get().log(Status.INFO, tcData + "<font color='black'>" + msgToPrint);
		extentReports.get().flush();
	}

	public void logWarning(String msgToPrint) {
		ReportUtil.extentTest.get().log(Status.WARNING, MarkupHelper.createLabel(msgToPrint, ExtentColor.AMBER));
		extentReports.get().flush();
	}

	public void logSkipped(String msgToPrint) {
		ReportUtil.extentTest.get().log(Status.SKIP, MarkupHelper.createLabel(msgToPrint, ExtentColor.ORANGE));
		extentReports.get().flush();
	}

	public String captureScreenShot(WebElement ele) {
		try {
			String filePath = ReportUtil.getExtentReportFolder() + File.separator + "Screenshots" + File.separator;
			new CompactUtil().createFolder(filePath);
			String fileName = new CompactUtil().getCurrentTimeStemp("yyyyMMdd_hhmmss") + ".png";
			BufferedImage bf = Shutterbug.shootPage(DriverUtil.getInstance().getDriver(), ScrollStrategy.WHOLE_PAGE, true)
					.highlight(ele, Color.RED, 4).getImage();
			File outputfile = new File(filePath + fileName);
			ImageIO.write(bf, "png", outputfile);
			return filePath + fileName;
		} catch (Exception e) {
			new ExceptionHandler().unhandledException(e);
			e.printStackTrace();
			return "";
		}
	}

	public String captureScreenShot() {
		try {
			String filePath = ReportUtil.getExtentReportFolder() + File.separator + "Screenshots" + File.separator;
			new CompactUtil().createFolder(filePath);
			String fileName = new CompactUtil().getCurrentTimeStemp("yyyyMMdd_hhmmss") + ".png";
			BufferedImage bf = Shutterbug.shootPage(DriverUtil.getInstance().getDriver(), ScrollStrategy.VIEWPORT_ONLY, true)
					.withName(filePath + fileName).getImage();
			File outputfile = new File(filePath + fileName);
			ImageIO.write(bf, "png", outputfile);
			bf.flush();
			return "<div align='right' style='float:right'><a " + NewWindowPopUpHTMLCode() + " target='_blank' href= "
					+ "." + File.separator + "Screenshots" + File.separator + fileName + ">Screenshot</a></div>";
		} catch (Exception e) {
			new ExceptionHandler().unhandledException(e);
			e.printStackTrace();
			return "";
		}
	}


	public static String NewWindowPopUpHTMLCode() {
		return "onclick = \"window.open(this.href,'newwindow', 'width=1000" + ",height=500');return false;\"";

	}

	public static String NewTabPopUpHTMLCode() {
		return "onclick = \"window.open(this.href,'_blank');return false;\"";

	}
}
