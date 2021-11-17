package com.aeroweb.utils;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverUtil {
	
	private static DriverUtil instance=null;
	private static ThreadLocal<WebDriver> driver= new ThreadLocal<>();

	public WebDriver getDriver() {
		return driver.get();
	}
	
	private DriverUtil() {
		
	}
	
	public static DriverUtil getInstance() {
		if(instance==null) {
			instance =new DriverUtil();
		}
		return instance;
	}

	public WebDriver openBrowser() {
		String browserType = new JsonUtils().getBrowser();
		try {
			if (browserType.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				/******************************************************************************/
				CompactUtil.closeSpecificProcess("chrome.exe");
				ChromeOptions cromeOptions = new ChromeOptions();
				File file = new File(System.getenv("APPDATA") + "/../Local/Google/Chrome/User Data");
				cromeOptions.addArguments("user-data-dir=" + file.getCanonicalPath());
				/******************************************************************************/
				driver.set(new ChromeDriver(cromeOptions));
				new ReportUtil().logInfo("Chrome browser is launched");
				
			}
			else if (browserType.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				//SETTING TO CHANGE THE DEFAULT DOWNLOAD LOCATIONS
				String location = new File(ReportUtil.getExtentReportFolder()+File.separator+"files"+File.separator+"downloded"+File.separator).getAbsolutePath();
				new CompactUtil().createFolder(location);
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("pdfjs.disabled", true);            // disable the built-in PDF viewer
				profile.setPreference("browser.download.dir", location); //Set the last directory used for saving a file from the "What should (browser) do with this file?" dialog.
				profile.setPreference("browser.download.folderList",2); //Use for the default download directory the last folder specified for a download
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				profile.setPreference("browser.download.useDownloadDir", true);
				profile.setPreference( "layout.css.devPixelsPerPx", "1.2" );
				profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);	
				profile.setPreference("browser.download.manager.useWindow", false);
				profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
				profile.setPreference("browser.download.manager.closeWhenDone", true);
				profile.setPreference("browser.download.manager.showAlertOnComplete", false);
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf,image/jpeg,text/plain,application/msword,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/octet-stream doc xls pdf txt");//list of MIME types to save to disk without asking what to use to open the file
				profile.setPreference("browser.helperApps.neverAsk.openFile","application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf,image/jpeg,text/plain,application/msword,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/octet-stream doc xls pdf txt");//list of MIME types to save to disk without asking what to use to open the file

				FirefoxOptions opt = new FirefoxOptions();
				opt.setHeadless(false);
				opt.setProfile(profile);
				driver.set(new FirefoxDriver(opt));
				//driver.set(new FirefoxDriver(new FirefoxOptions().setProfile(profile)));
				new ReportUtil().logInfo("Firefox browser is launched");
				
				
				
			} else if (browserType.equalsIgnoreCase("ie")) {
				WebDriverManager.iedriver().setup();
				driver.set(new InternetExplorerDriver());
				new ReportUtil().logInfo("IE browser is launched");
				
			}
			 else if (browserType.equalsIgnoreCase("edge")) {
				String location = new File(ReportUtil.getExtentReportFolder()+File.separator+"files"+File.separator+"downloded"+File.separator).getAbsolutePath();
				new CompactUtil().createFolder(location);
				Map<String, Object> prefs = new HashMap<String, Object>();                                                       
			    prefs.put("download.default_directory",location); 
			    WebDriverManager.edgedriver().setup();
			    CompactUtil.closeSpecificProcess("msedge.exe");
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.setExperimentalOption("prefs", prefs);
				//edgeOptions.addArguments("user-data-dir=C:\\Users\\cdurgapal\\AppData\\Local\\Microsoft\\Edge\\User Data");
				//edgeOptions.addArguments("user-data-dir=C:\\Users\\avikumar\\AppData\\Local\\Microsoft\\Edge\\User Data");
				edgeOptions.addArguments("user-data-dir=C:\\Users\\sajay\\AppData\\Local\\Microsoft\\Edge\\User Data");
				edgeOptions.addArguments("headless");


				driver.set(new EdgeDriver(edgeOptions));
				new ReportUtil().logInfo("Edge browser is launched");
				
			} else {

				new ExceptionHandler().customizedException(browserType + " not been automated yet");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		driver.get().manage().deleteAllCookies();
		driver.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get().manage().window().maximize();
		//driver.manage().window().setSize(new Dimension(1920,1080));
		return driver.get();
	}
	
	public void closeBrowser(Scenario scenario) {
		new ReportUtil().logInfo("Last screen of current scenario", true);
		DriverUtil.getInstance().getDriver().quit();
	}

	
	
}
