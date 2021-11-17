package com.aeroweb.utils;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import io.cucumber.java.bs.A;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class commonMethods {

	public static ExcelUtil refObj;

	String path = System.getProperty("user.dir") +"/src/testdata/TestData.xlsx";
	
	public static Row rows;
	public static HashMap<String, String> completemap;
	public static int testDataRow ;
	public WebElement element = null;
	public void assignObj(String name,String testCaseId) throws IOException {
		if(name.toLowerCase().contains("referential"))
		{
			completemap = getDriverSheetData("Referential",path,testCaseId);
			System.out.println(Arrays.asList(completemap));
		}
		else if(name.toLowerCase().contains("logistics"))
		{
			completemap = getDriverSheetData("logistics",path,testCaseId);
			System.out.println(Arrays.asList(completemap));
		}
		else if(name.toLowerCase().contains("maintenance"))
		{
			completemap = getDriverSheetData("Maintenance",path,testCaseId);
			System.out.println(Arrays.asList(completemap));
		}
		else if(name.toLowerCase().contains("transverse"))
		{
			completemap = getDriverSheetData("Transverse",path,testCaseId);
			System.out.println(Arrays.asList(completemap));
		}
	}

	public HashMap<String, String> getDriverSheetData(String sheetName,String filepath,String testCaseId) {
		HashMap<String, String> map1 = new HashMap<String, String>();
		if(filepath!=null) {
			try {
				int counter = 0;
				refObj = new ExcelUtil(path,sheetName);
				Sheet MasterSheet = refObj.getSheet(sheetName);
				Row row = null;
				int numOfColumns = MasterSheet.getRow(0).getLastCellNum();
				int numOfRows = refObj.getRowCount(MasterSheet);

				for(int i =1;i<=numOfRows;i++)
				{
					System.out.println(refObj.getCellData(i,1).toString());
					if(refObj.getCellData(i,1).trim().equalsIgnoreCase(testCaseId.trim()))
					{
						System.out.println(i);
						counter=i;
						testDataRow=i;
						break;
					}
				}
				ArrayList<String> mapData = new ArrayList<String>();
				row = MasterSheet.getRow(0);
				String val= "";
				for(int colCounter = 0 ; colCounter < numOfColumns; colCounter++) {
					val = refObj.getCellData(MasterSheet , counter , colCounter );

					mapData.add(val);
					map1.put(row.getCell(colCounter).toString(),mapData.get(colCounter));
				}

			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return map1;
	}

	public String getReuiredValue(String key)
	{
		String val = null;
		for(Map.Entry<String , String> m: completemap.entrySet())
		{
			if(m.getKey().equalsIgnoreCase(key))
			{
				System.out.println(Arrays.asList(m.getKey() + ":" + m.getValue()));
				val=m.getValue();
			}
		}
		return val;
	}


	public void navigateURL() {
		DriverUtil.getInstance().getDriver().get(new JsonUtils().getPortalURL());
		new ReportUtil().logInfo("User navigated to URL "+new JsonUtils().getPortalURL());
	}

	public WebElement getElement(By by) {
		WebElement we = null;
		try {
			we = DriverUtil.getInstance().getDriver().findElement(by);

		} catch (Exception e) {
			new ExceptionHandler().unhandledException(e);
		}
		return we;
	}

	public List<WebElement> getElements(By by) {
		List<WebElement> we = null;
		try {
			we = DriverUtil.getInstance().getDriver().findElements(by);
		} catch (Exception e) {
			new ExceptionHandler().unhandledException(e);
		}
		return we;
	}

	public void performOperation(By by, String operation, String value, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException(logicalName + " not found in application");
		} else if (operation.equalsIgnoreCase("input")) {
			try {
				ele.clear();
				ele.click();
				ele.sendKeys(value);
				int i = 1;
				while (ele.getAttribute("value").trim().equalsIgnoreCase("") && i++ < 30) {
					explicitWait(1);
					ele.clear();
					ele.click();
					ele.sendKeys(value);

				}
				new ReportUtil().logInfo(value + " has been entered in " + logicalName);
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be entered in " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("selectByVal")) {
			try {
				if (!value.equalsIgnoreCase("")) {
					Select sel = new Select(ele);
					sel.selectByVisibleText(value);
					new ReportUtil().logInfo(value + " has been selected for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be select for " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("checkbox")) {
			try {
				if (value.equalsIgnoreCase("yes")) {
					if (!ele.isSelected()) {

						ele.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been checked");
				} else if (value.equalsIgnoreCase("no")) {
					if (ele.isSelected()) {
						ele.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been un-checked");
				} else if (!value.equalsIgnoreCase("")) {
					new com.aeroweb.utils.ExceptionHandler().customizedException(value + " is not valid for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be checked/unchecked");
			}
		} else if (operation.equalsIgnoreCase("Click")) {
			try {
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//					new ReportUtil().logInfo("Before clicked " + logicalName, true);
//				} else {
//					new ReportUtil().logInfo("Before clicked " + logicalName, false);
//				}
				ele.click();
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be clicked");
			}
		}

	}

	public void performOperation(WebElement ele, String operation, String value, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		//WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException(logicalName + " not found in application");
		} else if (operation.equalsIgnoreCase("input")) {
			try {
				ele.clear();
				ele.click();
				ele.sendKeys(value);
				int i = 1;
				while (ele.getAttribute("value").trim().equalsIgnoreCase("") && i++ < 30) {
					explicitWait(1);
					ele.clear();
					ele.click();
					ele.sendKeys(value);

				}
				new ReportUtil().logInfo(value + " has been entered in " + logicalName);
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be entered in " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("selectByVal")) {
			try {
				if (!value.equalsIgnoreCase("")) {
					Select sel = new Select(ele);
					sel.selectByVisibleText(value);
					new ReportUtil().logInfo(value + " has been selected for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be select for " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("checkbox")) {
			try {
				if (value.equalsIgnoreCase("yes")) {
					if (!ele.isSelected()) {

						ele.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been checked");
				} else if (value.equalsIgnoreCase("no")) {
					if (ele.isSelected()) {
						ele.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been un-checked");
				} else if (!value.equalsIgnoreCase("")) {
					new com.aeroweb.utils.ExceptionHandler().customizedException(value + " is not valid for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be checked/unchecked");
			}
		} else if (operation.equalsIgnoreCase("Click")) {
			try {
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//					new ReportUtil().logInfo("Before clicked " + logicalName, true);
//				} else {
//					new ReportUtil().logInfo("Before clicked " + logicalName, false);
//				}
				ele.click();
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be clicked");
			}
		}

	}

	public void performOpration(By by, By by1, String operation, String value, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		WebElement ele1 = getElement(by1);
		if (ele == null & ele1 == null) {
			new ExceptionHandler().customizedException(logicalName + " not found in application");
		} else if (operation.equalsIgnoreCase("input")) {
			try {
				ele.clear();
				ele.click();
				ele.sendKeys(value);
				new ReportUtil().logInfo(value + " has been entered in " + logicalName);
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be entered in " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("selectByVal")) {
			try {
				if (!value.equalsIgnoreCase("")) {
					Select sel = new Select(ele);
					sel.selectByVisibleText(value);
					new ReportUtil().logInfo(value + " has been selected for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(value + " cannot be select for " + logicalName);
			}
		} else if (operation.equalsIgnoreCase("checkbox")) {
			try {
				if (value.equalsIgnoreCase("yes")) {
					if (!ele.isSelected()) {

						ele1.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been checked");
				} else if (value.equalsIgnoreCase("no")) {
					if (ele.isSelected()) {
						ele1.click();
					}
					new ReportUtil().logInfo("'" + logicalName + "' has been un-checked");
				} else if (!value.equalsIgnoreCase("")) {
					new com.aeroweb.utils.ExceptionHandler().customizedException(value + " is not valid for " + logicalName);
				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be checked/unchecked");
			}
		} else if (operation.equalsIgnoreCase("Click")) {
			try {
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//					new ReportUtil().logInfo("Before clicked " + logicalName, true);
//				} else {
//					new ReportUtil().logInfo("Before clicked " + logicalName, false);
//				}
				ele.click();
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException(logicalName + " cannot be clicked");
			}
		}

	}

	public void scrollAndClick(By by, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void scrollAndClick(WebElement ele, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				explicitWait(2);
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void click(By by, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void click(WebElement ele, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void scrollAndClick_WithoutJSWait(WebElement ele, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void scrollAndClick_WithoutJSWait(By by, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				ele.click();
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void clickAnElement(By by, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);

			}
		}
	}

	public void clickAnElement(WebElement ele, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				explicitWait(1);
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);
//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);
			}
		}
	}

	public void clickJS_NoJSWaiter(By by, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		WebElement ele = getElement(by);
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);
			}
		}
	}

	public void clickJS_withoutJSWaiter(WebElement ele, String logicalName) {
		JsonUtils jsonObject = new JsonUtils();
		scrollIntoView(ele);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
			Assert.assertTrue(false, "not found in application "+logicalName);
		} else {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", true);
//				}else {
//					new ReportUtil().logInfo("clicked on '"+ logicalName + "'", false);
//				}
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be clicked");
				Assert.assertTrue(false, "Unable to click on "+logicalName);
			}
		}
	}

	public void verifyValue(By by, String propertyName, String valToVerify, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException(logicalName + " not found in application");
		} else if (propertyName.equalsIgnoreCase("exist")) {
			if (ele.isDisplayed()) {
				new ReportUtil().logPass("Element " + logicalName +" should be present in application","Element " + logicalName +" present in application");
			} else {
				new ReportUtil().logFail("Element " + logicalName +" should be present in application","Element " + logicalName +" not present in application");
			}
		} else if (propertyName.equalsIgnoreCase("verifyText")) {
			new ReportUtil().compareResult(logicalName, valToVerify, new StringUtil().removeWhiteSpaces(ele.getText()),
					false);
		} else if (propertyName.equalsIgnoreCase("verifyChkBox")) {
			if (valToVerify.equalsIgnoreCase("yes")) {
				new ReportUtil().compareResult(logicalName, "true", Boolean.toString(ele.isSelected()), false);
			} else if (valToVerify.equalsIgnoreCase("no")) {
				new ReportUtil().compareResult(logicalName, "false", Boolean.toString(ele.isSelected()), false);
			}
		}
	}

	public void clickJS(By by, String logicalName) {
		try {
			JsonUtils jsonObject = new JsonUtils();
			WebElement ele = getElement(by);
			if (ele == null) {
				new ExceptionHandler().customizedException(logicalName + " not found in application");
				Assert.assertTrue(false, "not found in application "+logicalName);
			} else {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//					new ReportUtil().logInfo(logicalName + " clicked ", true);
//				} else
//					new ReportUtil().logInfo(logicalName + " clicked ", false);
				new com.aeroweb.utils.JSWaiter().waitAllRequest();
			}
		} catch (Exception e) {
			new ReportUtil().logFail(logicalName + " can not be clicked", "");
			Assert.assertTrue(false, "Unable to click on "+logicalName);
		}
	}

	public void clickJS_WithoutJSWait(By by, String logicalName) {
		try {
			explicitWait(5);
			JsonUtils jsonObject = new JsonUtils();
			WebElement ele = getElement(by);
			if (ele == null) {
				new ExceptionHandler().customizedException(logicalName + " not found in application");
				Assert.assertTrue(false, "not found in application "+logicalName);
			} else {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo(logicalName + " clicked ", true);
//				} else
//					new ReportUtil().logInfo(logicalName + " clicked ", false);
			}
		} catch (Exception e) {
			new ReportUtil().logFail(logicalName + " can not be clicked", "");
			Assert.assertTrue(false, "Unable to click on "+logicalName);
		}
	}

	public void clickJS_WithoutJSWait(WebElement by, String logicalName) {
		try {
			explicitWait(2);
			JsonUtils jsonObject = new JsonUtils();
			WebElement ele = by;
			if (ele == null) {
				new ExceptionHandler().customizedException(logicalName + " not found in application");
				Assert.assertTrue(false, "not found in application "+logicalName);
			} else {
				JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
				executor.executeScript("arguments[0].click();", ele);

//				if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//						| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//
//					new ReportUtil().logInfo(logicalName + " clicked ", true);
//				} else
//					new ReportUtil().logInfo(logicalName + " clicked ", false);
			}
		} catch (Exception e) {
			new ReportUtil().logFail(logicalName + " can not be clicked", "");
			Assert.assertTrue(false, "Unable to click on "+logicalName);
		}
	}

	public void wheel_element(WebElement element, int deltaY, int offsetX, int offsetY) {
		try {
			String script = "var element = arguments[0];" + "var deltaY = arguments[1];"
					+ "var box = element.getBoundingClientRect();"
					+ "var clientX = box.left + (arguments[2] || box.width / 2);"
					+ "var clientY = box.top + (arguments[3] || box.height / 2);"
					+ "var target = element.ownerDocument.elementFromPoint(clientX, clientY);"
					+ "for (var e = target; e; e = e.parentElement) {" + "if (e === element) {"
					+ "target.dispatchEvent(new MouseEvent('mouseover', {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY}));"
					+ "target.dispatchEvent(new MouseEvent('mousemove', {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY}));"
					+ "target.dispatchEvent(new WheelEvent('wheel',     {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY, deltaY: deltaY}));"
					+ "return;" + "}" + "}";

			WebElement parent = (WebElement) ((JavascriptExecutor) DriverUtil.getInstance().getDriver())
					.executeScript("return arguments[0].parentNode;", element);
			((JavascriptExecutor) DriverUtil.getInstance().getDriver()).executeScript(script, parent, deltaY, offsetX, offsetY);
		} catch (WebDriverException e) {
			System.out.println("Exception caught in Catch block");
		}
	}

	public void highlightElement(WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
	}

	public void explicitWait(int time) {
		try {
			Thread.sleep((1000 * time));
		} catch (Exception e) {

		}
	}

	public void explicitWait(double time) {
		try {
			Thread.sleep((long) (1000 * time));
		} catch (Exception e) {

		}
	}

	public static void explicitWaiting(int time) {
		try {
			Thread.sleep((1000 * time));
		} catch (Exception e) {

		}
	}

	public WebElement getElementExplicitWait(By element) {
		WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(new JsonUtils().getMaxWaitTime()));
//		WebDriverWait wait = new WebDriverWait(driver, new JsonUtils().getMaxWaitTime());
		return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
	}

	/**
	 * wait for element base on condition 0 = visibility..return element after
	 * waiting for specified time 1 = invisibility..return null after waiting for
	 * specified time 2 = clickable.. return element after waiting for specified tye
	 *
	 * @param element
	 * @param mode
	 * @return
	 */

	public WebElement getElementExplicitWait(By element, int mode) {
		try {
			WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(new JsonUtils().getMaxWaitTime()));
			//WebDriverWait wait = new WebDriverWait(driver, new JsonUtils().getMaxWaitTime());
			if (mode == 0) {
				return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			} else if (mode == 1) {
				if (wait.until(ExpectedConditions.invisibilityOfElementLocated(element)) == null)
					return null;
			} else if (mode == 2) {
				return wait.until(ExpectedConditions.elementToBeClickable(element));
			}
		}catch(Exception e) {
			new ReportUtil().logFail("", "Failed to wait for element");
		}

		return null;

	}
	/**
	 * Wait for visibility of element to be true and returns the element else return null
	 * @param element
	 * @param seconds
	 * @param genericname
	 * @return
	 */
	public WebElement getElementExplicitWaitVisibility(By element, String seconds, String genericname) {
		try {
			int sec = Integer.parseInt(seconds);
			WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(sec));
//			WebDriverWait wait = new WebDriverWait(driver, sec);
			return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		} catch (TimeoutException e) {
			new ReportUtil().logFail("Element "+ genericname +" Should be visible" ,"Element "+ genericname +" not visible");
			return null;
		}

	}

	public WebElement getElementExplicitWaitInVisibility(By element, String seconds, String genericname) {
		try {
			int sec = Integer.parseInt(seconds);
			WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(sec));
//			WebDriverWait wait = new WebDriverWait(driver, sec);
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			new ReportUtil().logFail("Element "+ genericname +" Should not be visible" ,"Element "+ genericname +" visible");
			return null;
		} catch (TimeoutException e) {
			new ReportUtil().logPass("Element "+ genericname +" Should not be visible" ,"Element "+ genericname +" not visible");
			return null;
		}

	}

	public void clickJS(WebElement ele, String logicalName) {
		explicitWait(1);
		JsonUtils jsonObject = new JsonUtils();
		if (ele == null) {
			new ExceptionHandler().customizedException(logicalName + " not found in application");
		} else {
			JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
			executor.executeScript("arguments[0].click();", ele);
//			if (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
//					| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
//					| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
//					| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick")) {
//				new ReportUtil().logInfo(logicalName + " clicked", true);
//			} else {
//				new ReportUtil().logInfo(logicalName + " clicked", false);
//			}

			//new com.aeroweb.utils.JSWaiter().waitAllRequest();

		}
	}

	public void checkElementPresent(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, false);
				}
				ele.click();
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}



	public void checkElementPresentByVisibility(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, false);
				}
				ele.isDisplayed();
				new ReportUtil().logPass(logicalName+" should be present"  ,logicalName+" is present" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}

	public void checkElementNotPresentByVisibility(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, false);
				}
				if(!ele.isDisplayed())
					new ReportUtil().logPass(logicalName+" should not be present"  ,logicalName+" is not present" );
				else
					new ReportUtil().logFail(logicalName+" should not be present"  ,logicalName+" is present" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}

	public void checkElementPresentInfo(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				ele.isDisplayed();
				new ReportUtil().logInfo(logicalName+" is present" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}

	public void checkElementPresentByVisibility(WebElement ele, String logicalName) {
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking Presence of " + logicalName, false);
				}
				ele.isDisplayed();
				new ReportUtil().logPass(logicalName+" should be present"  ,logicalName+" is present" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}

	public boolean screenShotRequired() {
		JsonUtils jsonObject = new JsonUtils();
		return (jsonObject.getScreenshotOption().equalsIgnoreCase("always")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onPass")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onFail")
				| jsonObject.getScreenshotOption().equalsIgnoreCase("onClick"));
	}

	public void scrollIntoView(WebElement ele) {
		if (ele != null) {
			((JavascriptExecutor) DriverUtil.getInstance().getDriver()).executeScript("arguments[0].scrollIntoView(true);", ele);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

	}
	/**
	 * Used in case we want to enter a value and show * in report to secure the value
	 * @param element
	 * @param value
	 * @param logicalName
	 */
	public void enterSecuredValue(By element,String value,String logicalName) {
		char[] chars = new char[value.length()];
		Arrays.fill(chars, '*');
		WebElement ele = getElement(element);
		try {
			ele.clear();
			ele.click();
			ele.sendKeys(value);
			int i = 1;
			while (ele.getAttribute("value").trim().equalsIgnoreCase("") && i++ < 30) {
				explicitWait(1);
				ele.click();
				ele.sendKeys(value);

			}

			new ReportUtil().logInfo(new String(chars) + " has been entered in " + logicalName);
		} catch (Exception e) {
			new ExceptionHandler().customizedException(new String(chars) + " cannot be entered in " + logicalName);
		}

	}

	public void validatePageNavigation(By locator,String Msg) {
		try {
			getElementExplicitWait(locator, 0);
			new ReportUtil().logInfo("User navigated to "+Msg+" page");

		} catch (Exception e) {
			new ReportUtil().logFail("User should navigate to "+Msg+" page","User is not navigated to "+Msg+" page");

		}
	}

	public void checkBackgroundColor(By loc, String locName, String colorName) {
		try {
			WebElement ele = DriverUtil.getInstance().getDriver().findElement(loc);
			String bgColor = ele.getCssValue("background-color");
			System.out.println("button color is" + ele.getCssValue("background-color"));
			System.out.println("button color is" + ele.getAttribute("style"));
			System.out.println("button color is" + ele.getCssValue("box-shadow"));
			String[] numbers = bgColor.replace("rgb(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			System.out.println("r: " + r + "g: " + g + "b: " + b);
			String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
			System.out.println("Hex of of a color code is " + hex);

			if (colorName.equalsIgnoreCase("purple")) {
				if (hex.equalsIgnoreCase("#6d326d"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			}

			else if (colorName.equalsIgnoreCase("blue")) {
				if (hex.equalsIgnoreCase("#116fbf") || hex.equalsIgnoreCase("#07ad9") || hex.equalsIgnoreCase("#1579b") || hex.equalsIgnoreCase("#2196F3")|| hex.equalsIgnoreCase("#2598f3")
						||hex.equalsIgnoreCase("#2297f3"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("Unable to validate color code for an element " + locName, "");

		}
	}

	public void checkBackgroundColorForEdge(By loc,String locName,String colorName) {
		try {
			WebElement ele= DriverUtil.getInstance().getDriver().findElement(loc);
			String bgColor=ele.getCssValue("background-color");
			System.out.println("button color is"+ele.getCssValue("background-color"));

			String[] numbers = bgColor.replace("rgba(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			int a = Integer.parseInt(numbers[3].trim());
			System.out.println("r: " + r + "g: " + g + "b: " + b+ "a: " + a);
			String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b) + Integer.toHexString(a);
			System.out.println("Hex of of a color code is "+hex);

			if (colorName.equalsIgnoreCase("purple")) {
				if(hex.equalsIgnoreCase("#6d326d1") || hex.equalsIgnoreCase("#ffffff1") )
					new ReportUtil().logPass(locName+" should highlighted in "+colorName+" color",locName+" is highlighted in "+colorName+" color");
				else
					new ReportUtil().logFail(locName+" should highlighted in "+colorName+" color",locName+" is not highlighted in "+colorName+" color");
			}else if (colorName.equalsIgnoreCase("blue")) {
				if(hex.equalsIgnoreCase("#116fbf1")||hex.equalsIgnoreCase("#07ad91")||hex.equalsIgnoreCase("#1579b1"))
					new ReportUtil().logPass(locName+" should highlighted in "+colorName+" color",locName+" is highlighted in "+colorName+" color");
				else
					new ReportUtil().logFail(locName+" should highlighted in "+colorName+" color",locName+" is not highlighted in "+colorName+" color");
			}
		}catch (Exception e) {
			new ReportUtil().logFail("Unable to validate color code for an element "+locName,"");

		}
	}

	public void checkBackgroundColor(WebElement ele, String locName, String colorName) {
		try {
			String bgColor = ele.getCssValue("background-color");
			System.out.println("background color is" + ele.getCssValue("background-color"));

			String[] numbers = bgColor.replace("rgb(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			System.out.println("r: " + r + "g: " + g + "b: " + b);
			String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
			System.out.println("Hex of of a color code is " + hex);

			if (colorName.equalsIgnoreCase("purple")) {
				if (hex.equalsIgnoreCase("#6d326d"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			}

			else if (colorName.equalsIgnoreCase("blue")) {
				if (hex.equalsIgnoreCase("#116fbf") || hex.equalsIgnoreCase("#07ad9") || hex.equalsIgnoreCase("#1579b")|| hex.equalsIgnoreCase("#2196F3")|| hex.equalsIgnoreCase("#2598f3")
						||hex.equalsIgnoreCase("#2297f3"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("Unable to validate color code for an element " + locName, "");

		}
	}

	public void checkBackgroundColorForEdge(WebElement ele, String locName, String colorName) {
		try {

			String bgColor = ele.getCssValue("background-color");
			System.out.println("background color is" + ele.getCssValue("background-color"));

			String[] numbers = bgColor.replace("rgba(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			int a = Integer.parseInt(numbers[3].trim());
			System.out.println("r: " + r + "g: " + g + "b: " + b + "a: " + a);
			String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b)
					+ Integer.toHexString(a);
			System.out.println("Hex of of a color code is " + hex);

			if (colorName.equalsIgnoreCase("purple")) {
				if (hex.equalsIgnoreCase("#6d326d1"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			} else if (colorName.equalsIgnoreCase("blue")) {
				if (hex.equalsIgnoreCase("#116fbf1") || hex.equalsIgnoreCase("#07ad91")
						|| hex.equalsIgnoreCase("#1579b1"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("Unable to validate color code for an element " + locName, "");

		}
	}

	public String generateRandomNumber() {
		Random r = new Random();
		int low = 10;
		int high = 850;
		int result = r.nextInt(high-low) + low;
		return String.valueOf(result);
	}

	public void scrollUp() {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
			executor.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			explicitWait(2);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to scroll up");
		}
	}

	public void scrollDown() {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
			executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			explicitWait(2);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to scroll up");
		}
	}

	public void scroll(int pixel) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)DriverUtil.getInstance().getDriver();
			jse.executeScript("window.scrollBy(0,"+pixel+")");
			explicitWait(2);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to scroll up");
		}
	}


	public void scrollIntoView(By by) {
		WebElement ele = getElement(by);
		if (ele != null) {
			((JavascriptExecutor) DriverUtil.getInstance().getDriver()).executeScript("arguments[0].scrollIntoView(true);", ele);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Provide the xpath with disable property only
	 * @param by
	 * @param logicalName
	 */
	public void checkElementNotEditable(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking editability of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking editability of " + logicalName, false);
				}
				ele.isDisplayed();
				new ReportUtil().logPass(logicalName+" should be non editable"  ,logicalName+" is non editable" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' is editable");
			}
		}
	}

	public String getText(By locator,String logicalName) {
		try {
			return getElement(locator).getText().trim();
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to get text of an element "+ logicalName);
			return "";
		}
	}

	public String getText(WebElement locator,String logicalName) {
		try {
			return locator.getText().trim();
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to get text of an element "+ logicalName);
			return "";
		}
	}

	public String getAttributeByValue(By locator,String logicalName) {
		try {
			return getElement(locator).getAttribute("value").trim();
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to get attribute value of an element "+ logicalName);
			return "";
		}
	}

	public void compareText(String actual,String expected,String msg) {
		try {
			if(actual.trim().equalsIgnoreCase(expected)) {
				new ReportUtil().logInfo(msg);
			}else
				new ReportUtil().logFail("",msg);
		}catch(Exception e) {
			new ReportUtil().logFail("","Unable to compare results");
		}
	}

	public void elementNotAvailableOnPage(By loc,String logicalName) {
		elementNotVisible(loc,logicalName);
	}

	public void elementNotVisible(By loc,String logicalName) {
		try {
			getElement(loc).isDisplayed();
			new ReportUtil().logFail(logicalName+" should not be present", logicalName+ "  is present");
		}catch(Exception e) {
			new ReportUtil().logPass(logicalName+" should not be present", logicalName+ "  is not present");
		}
	}

	public WebElement getElementExplicitWaitVisibility_NoLogs(By element, String seconds, String genericname) {
		try {
			int sec = Integer.parseInt(seconds);
			WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(sec));
			return wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		} catch (TimeoutException e) {
			return null;
		}
	}

	public Boolean getElementExplicitWaitNoVisibility(By element, String seconds) {
		try {
			int sec = Integer.parseInt(seconds);
			WebDriverWait wait = new WebDriverWait(DriverUtil.getInstance().getDriver(), Duration.ofSeconds(sec));
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
		} catch (TimeoutException e) {
			return null;
		}
	}

	public String generateRandomNumber(int low,int high) {
		Random r = new Random();
		int result = r.nextInt(high-low) + low;
		return String.valueOf(result);
	}

	public void enterTextUsingJS(WebElement element,String value) {
		JavascriptExecutor jse = (JavascriptExecutor)DriverUtil.getInstance().getDriver();
		jse.executeScript("arguments[0].value="+value+"", element);
		new ReportUtil().logInfo("Value entered as "+value);
	}

	public void dragAndDrop(By by,By by2)
	{
		Actions act = new Actions(DriverUtil.getInstance().getDriver());
		WebElement ele = getElement(by);
		WebElement ele2 = getElement(by2);
		//act.dragAndDropBy(ele,50,0).build().perform();
		//act.dragAndDrop(ele,ele2);act.dragAndDropBy();
	}

	public void checkElementEnabled(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking enable of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking enable of " + logicalName, false);
				}
				ele.isEnabled();
				new ReportUtil().logPass(logicalName+" should be enabled"  ,logicalName+" is present" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}

	public void checkElementNotEnabled(By by, String logicalName) {
		WebElement ele = getElement(by);
		if (ele == null) {
			new ExceptionHandler().customizedException("'" + logicalName + "' not found in application");
		} else {
			try {
				if (screenShotRequired()) {
					new ReportUtil().logInfo("Checking disable of " + logicalName, true);
				} else {
					new ReportUtil().logInfo("Checking disable of " + logicalName, false);
				}
				System.out.println( " checking whether enable or not  "+ ele.isEnabled());
				if(!ele.isEnabled())
					new ReportUtil().logPass(logicalName+" should be disabled"  ,logicalName+" is disabled" );
				else
					new ReportUtil().logFail(logicalName+" should be disabled"  ,logicalName+" is not disabled" );
			} catch (Exception e) {
				new ExceptionHandler().customizedException("'" + logicalName + "' cannot be Found");
			}
		}
	}
	public void clickOnElement(WebElement element){
		scrollToElement(element);
		//element.click();
		JavascriptExecutor executor =(JavascriptExecutor) DriverUtil.getInstance().getDriver();
		executor.executeScript("arguments[0].click();",element);
	}
	public void scrollToElement(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) DriverUtil.getInstance().getDriver();
		executor.executeScript("arguments[0].scrollIntoView();",element);
	}
	public String getAttribute(By element,String value){
		return DriverUtil.getInstance().getDriver().findElement(element).getAttribute(value);
	}
	public void doubleclickOnElement(WebElement element){
		Actions act = new Actions(DriverUtil.getInstance().getDriver());
		act.doubleClick(element).perform();

	}
}
