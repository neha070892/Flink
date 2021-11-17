package com.aeroweb.pages;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByXPath;
import org.testng.Assert;

import com.aeroweb.dataManager.Constant;
import com.aeroweb.objects.DashboardPageObjects;
import com.aeroweb.objects.MaintenancePlanningPageObjects;
import com.aeroweb.utils.CompactUtil;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.ExceptionHandler;
import com.aeroweb.utils.JsonUtils;
import com.aeroweb.utils.ObjectUtil;
import com.aeroweb.utils.PropertyUtil;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;

public class Dashboard {
	DriverUtil driverUtil;
	PropertyUtil prop;
	DashboardPageObjects dashboardPageObj;
	MaintenancePlanningPageObjects maintenancePlanningPageObj;
	commonMethods common;
	
	public Dashboard() {
		String propertyFilePath = "src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
		this.prop = new PropertyUtil(propertyFilePath);
		dashboardPageObj= new DashboardPageObjects();
		maintenancePlanningPageObj= new MaintenancePlanningPageObjects();
		common= new commonMethods();
	}


	public void btnClick(String pageName) {
		WebElement ele;
		if(pageName.equalsIgnoreCase("Moisturizers")) {
			ele = common.getElementExplicitWait(dashboardPageObj.btnMoisturizer, 2);
			common.clickAnElement(ele, "Button");
		}
		else if(pageName.equalsIgnoreCase("Sunscreens"))
		{
			ele = common.getElementExplicitWait(dashboardPageObj.btnSunscreen, 2);
			common.clickAnElement(ele, "Button");
		}
	}



	public void checkOnDashboardPage() throws Exception {
		try {
			common.explicitWait(1);
			common.getElementExplicitWait(dashboardPageObj.menuBar, 0);
			new ReportUtil().logPass("User should be logged in","Logged In SuccessFully");

		} catch (Exception e) {
			new ReportUtil().logFail("User should be logged in","Unable to Login In");
			throw new Exception("Cannot Perform Login");
		}
	}

	public void selectItems(String item) {
		List<Integer> priceList=new ArrayList<Integer> ();
		List<WebElement> list=new ArrayList<WebElement> ();
//		if(item.equalsIgnoreCase("Aloe"))
			list = common.getElements(By.xpath("//div/div/button[contains(@onclick,'"+item+"')]"));
//		else if(item.equalsIgnoreCase("Almond"))
//			list = common.getElements(dashboardPageObj.AlmondItems);

		//adding item's prices in a priceList & sorting & find minimum price
		for (WebElement ele : list) {
			String text=(ele.getAttribute("onclick"));
			int price=Integer.parseInt((String) text.subSequence(text.indexOf(',')+1, text.length() - 1));
			//int price=Integer.parseInt((text.split(",",3)[1]));
			priceList.add(price);
			Collections.sort(priceList);
		}

		//Clicking on min price item
		for (WebElement ele : list) {
			int price=Integer.parseInt((String) ele.getAttribute("onclick").subSequence(ele.getAttribute("onclick").indexOf(',')+1, ele.getAttribute("onclick").length() - 1));
			if(price==priceList.get(0))
			 ele.click();
		}
	}

	public void selectSubMenuOption(String menuOption) {
		boolean optionFound = false;
		menuOption = prop.readPropertyData(menuOption.trim().replaceAll(" ","_"));
		List<WebElement> list = common.getElements(dashboardPageObj.floatingMenuItems);
		for (WebElement ele : list) {
			if (ele.getText().trim().equalsIgnoreCase(menuOption)) {
				optionFound = true;
				ele.click();
				break;
			}
		}
		
		if (!optionFound) {
			new ReportUtil().logFail("Sub menu option " + menuOption + " should be available","Sub menu option " + menuOption + " Not available");
			new Exception("Sub menu option "+ menuOption + " not available");}
		else
			new ReportUtil().logInfo("Sub menu option " + menuOption + " Selected");
	}

	public void performSearch() {
		WebElement ele = common.getElementExplicitWait(dashboardPageObj.searchButton, 2);
		common.clickAnElement(ele, "Search Button");
	}

	public void searchPerform() {
		WebElement ele = common.getElementExplicitWait(dashboardPageObj.buttonSearch, 2);
		common.clickAnElement(ele, "Search Button");
	}
	
	public void performSearchJS() {
		WebElement ele = common.getElementExplicitWait(dashboardPageObj.searchButton, 2);
		common.clickJS(ele, "Search Button");
	}
	
	public void performSearchWithoutJSWait() {
		common.getElementExplicitWait(dashboardPageObj.searchButton, 2);
		common.clickJS_NoJSWaiter(dashboardPageObj.searchButton, "Search Button");
	}

	public void checkResultsPresent() throws Exception {
		try{
		  common.getElement(maintenancePlanningPageObj.typeToFilterBox).isDisplayed();
		}catch(Exception e) {
		}
		WebElement results = common.getElement(dashboardPageObj.resultsCounter);
		String extractedValue = CompactUtil.extractNumber(results.getText());
		//if (Integer.parseInt(extractedValue)>0)
			if (extractedValue != null && extractedValue != "0")
			new ReportUtil().logPass("Search results should be available","Search Result are available");
		else {
			new ReportUtil().logFail("Search results should be available", "No Search Results are available");
			throw new Exception("Unable to proceed Further as no Result available");
		}
	}

	public void openSearchResult(String res) throws Exception {
		try {
		int result = 0;
		try {
			result = Integer.parseInt(res);
		} catch (Exception e) {
		}

		WebElement results = common.getElement(dashboardPageObj.resultsCounter);
		try{Thread.sleep(1000);}catch(Exception e) {}
		String extractedValue = CompactUtil.extractNumber(results.getText());
		
		if (result == 0)
			result++;
		if (extractedValue != null && extractedValue != "0") {
			int numbersOfResult = Integer.parseInt(extractedValue);
			if (numbersOfResult >= result) {
				common.getElementExplicitWait(dashboardPageObj.resultsCheckBox, 2);
				List<WebElement> list = common.getElements(dashboardPageObj.resultsCheckBox);
				if (!list.get(result).getAttribute("class").contains("p-highlight"))
					common.clickJS(list.get(result), "Result Check box");
				// list.get(result).click();

			}
		}
		common.clickAnElement(dashboardPageObj.resultOpenButton, "Open Button");
		//common.getElementExplicitWait(dashboardPageObj.resultOpenButton, 1);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to open search result");
			throw new Exception("Unable to open search result");
		}
	}
	
	public void openSearchResultUsingJS(String res) throws Exception {
		try {
		int result = 0;
		try {
			result = Integer.parseInt(res);
		} catch (Exception e) {
		}

		WebElement results = common.getElement(dashboardPageObj.resultsCounter);
		try{Thread.sleep(1000);}catch(Exception e) {}
		String extractedValue = CompactUtil.extractNumber(results.getText());
		
		if (result == 0)
			result++;
		if (extractedValue != null && extractedValue != "0") {
			int numbersOfResult = Integer.parseInt(extractedValue);
			if (numbersOfResult >= result) {
				common.getElementExplicitWait(dashboardPageObj.resultsCheckBox, 2);
				List<WebElement> list = common.getElements(dashboardPageObj.resultsCheckBox);
				if (!list.get(result).getAttribute("class").contains("p-highlight"))
					common.clickJS(list.get(result), "Result Check box");

			}
		}
		common.clickJS(dashboardPageObj.resultOpenButton, "Open Button");
		common.getElementExplicitWait(dashboardPageObj.resultOpenButton, 1);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to open search result");
			throw new Exception("Unable to open search result");
		}

	}

	public void selectSearchResult(String index, String name) {
		int result = 0;
		try {
			result = Integer.parseInt(index);
		} catch (Exception e) {}
		try {
			common.explicitWait(1);
			//List<WebElement> list = common.getElements(dashboardPageObj.selectResult);
			List<WebElement> list = common.getElements(dashboardPageObj.resultsCheckBoxBody);
			//List<WebElement> list = common.getElements(dashboardPageObj.resultsRows);
			System.out.println(list.size());
			System.out.println(list.get(result-1).getAttribute("class"));
			if (!list.get(result-1).getAttribute("class").contains("p-highlight"))
				//common.clickJS(list.get(result-1), "Result Check box" + " to select the " + name);
				common.clickAnElement(list.get(result-1), "Result Check box" + " to select the " + name);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}

	public void selectFlightResult(String index, String name) {
		int result = 0;
		try {
			result = Integer.parseInt(index);
		} catch (Exception e) {}
		try {
			common.explicitWait(1);
			List<WebElement> list = common.getElements(dashboardPageObj.flightsCheckBoxBody);
			//List<WebElement> list = common.getElements(dashboardPageObj.resultsRows);
			System.out.println(list.size());
			System.out.println(list.get(result-1).getAttribute("class"));
			if (!list.get(result-1).getAttribute("class").contains("p-highlight"))
				//common.clickJS(list.get(result-1), "Result Check box" + " to select the " + name);
				common.clickAnElement(list.get(result-1), "Flight Check box" + " to select the " + name);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}
	
	public String selectSearchResultByRow(String table, String index, String name, boolean capture,	String columnToCapture, String nonLinkableColumn) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		columnToCapture = prop.readPropertyData(columnToCapture.trim().replaceAll(" ", "_"));
		nonLinkableColumn = prop.readPropertyData(nonLinkableColumn.trim().replaceAll(" ", "_"));
		int result = 0;
		try {
			result = Integer.parseInt(index);
			int colInd = new LogisticsModule().findColumnIndexFromTable(table, nonLinkableColumn);
			WebElement resultCounts = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table)));
			List<WebElement> list = common.getElements(By.xpath(ObjectUtil.getTotalRowsXpath(table, colInd)));
			String extractedValue = CompactUtil.extractNumber(resultCounts.getText());
			if (result == 0)
				result++;
			if (!list.get(result-1).getAttribute("class").contains("selected"))
						common.click(list.get(result-1),
								"Result row at index " + result + " to select the " + name);

			if (capture == true) {
				return captureSelectedRow(table,columnToCapture);
			} else {
				return "";
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
			return null;
		}
	}

	public String openSearchResultByRow(String table, String index, String name, boolean capture,	String columnToCapture, String nonLinkableColumn) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		columnToCapture = prop.readPropertyData(columnToCapture.trim().replaceAll(" ", "_"));
		nonLinkableColumn = prop.readPropertyData(nonLinkableColumn.trim().replaceAll(" ", "_"));
		int result = 0;
		try {
			result = Integer.parseInt(index);
			int colInd = new LogisticsModule().findColumnIndexFromTable(table, nonLinkableColumn);
			WebElement resultCounts = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table)));
			List<WebElement> list = common.getElements(By.xpath(ObjectUtil.getTotalRowsXpath(table, colInd)));
			String extractedValue = CompactUtil.extractNumber(resultCounts.getText());
			if (result == 0)
				result++;
			if (!list.get(result-1).getAttribute("class").contains("selected")) {
				common.explicitWait(2);
				common.click(list.get(result - 1),
						"Result row at index " + result + " to select the " + name);
			}
			if (capture == true) {
				return captureSelectedRow(table,columnToCapture);
			} else {
				return "";
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
			return null;
		}
	}
	
	public String captureSelectedRow(String table,String column) {
		String value=null;
		try {
			int index=new LogisticsModule().findColumnIndexFromTable(table, column);		
			List<WebElement> list = common.getElements(By.xpath(ObjectUtil.xpathToGetSelectedRowColumnValues(table)));	
			value=list.get(index-1).getText().trim();
			new ReportUtil().logInfo("Selected item from search results is "+value);
			return value;
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to capture the selected item");
			return value;
		}
		
	}
	
	
	
	public void verifySelectedSummaryPageSubTitle(String name) {
		try {
			common.getElementExplicitWait(maintenancePlanningPageObj.maintenanceSummaryPageSubTitle);
			WebElement ele=common.getElement(maintenancePlanningPageObj.maintenanceSummaryPageSubTitle);
			String titleText=ele.getText();
			
			if(titleText.contains(name)) {
				new ReportUtil().logPass("Name of the page should contain the selected "+name+" item","Name of the page contain the selected "+name);
			}else {
				new ReportUtil().logFail("Name of the page should contain the selected "+name+" item","Name of the page does not contain the selected "+name);
			}
		}catch(Exception e) {
			new ReportUtil().logFail("Name of the page should contain the selected "+name+" item","unable to compare page title with selected result");
		}
	}
	
	public void clickOpenButton() {
		common.clickJS(dashboardPageObj.resultOpenButton, "Open Button");
		//common.getElementExplicitWait(dashboardPageObj.resultOpenButton, 1);
	}

	public void selectValueFromDropDown(By dropDown, By listElement, String value, String generictext) {
		boolean optionFound = false;
		common.clickAnElement(dropDown, generictext + "Dropdown");

		List<WebElement> list = DriverUtil.getInstance().getDriver().findElements(listElement);
		for (WebElement ele : list) {
			if (ele.getText().equalsIgnoreCase(value)) {
				optionFound = true;
				ele.click();
				break;
			}
		}
		if (!optionFound)
			new ReportUtil().logFail("option " + value + " should be available","option " + value + " Not available");
		else
			new ReportUtil().logInfo("option " + value + " Selected");
	}

	public static void clickElementFromList(By element, String value, String generic) {
		boolean optionFound = false;
		List<WebElement> list = DriverUtil.getInstance().getDriver().findElements(element);
		for (WebElement ele : list) {
			System.out.println(ele.getText());
			if (ele.getText().trim().equalsIgnoreCase(value)) {
				optionFound = true;
				ele.click();
				break;
			}
		}
		if (!optionFound)
			new ReportUtil().logFail(generic + " should be available",generic + " Not Found");
		else
			new ReportUtil().logInfo(generic + " Selected");
	}

	public void goBack() {
		common.clickJS(dashboardPageObj.backButton, "Back Button");
	}
	
	public void selectOptionFromCriteriaDropdown(String option, String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			common.clickJS(element, dropdownName);
			common.explicitWait(0.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}

	public void selectcheckboxOptionFromCriteriaDropdown(String option, String dd) throws Exception {
		boolean flag=false;
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-multiselectitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dd
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");
			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			common.clickJS(element, dd);
			common.explicitWait(0.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					String optToSel = ele.getText().trim();
					WebElement ele1 = common.getElement(By.xpath("//p-multiselectitem//*[contains(text(),'"+optToSel+"')]"));
					common.scrollAndClick(ele1,"");
					//common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
							(option + " option is selected from dropdown " + dd));
					common.explicitWait(1);
					flag=true;
					break;
				}
			}
			if(flag==false){
				new ReportUtil().logFail("","Option is not available under dropdown "+dd);
			}
		}catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}

	}
	
	public void selectOptionFromCriteriaDropdown(String option, boolean scroller,String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			if(scroller==true) 
			 common.clickAnElement(element, dropdownName);
			else
				common.clickJS(element, dropdownName);
			common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}
	
	public void selectOptionFromCriteriaDropdown(String option, boolean scroller,boolean normalClick,String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			if(scroller==true) 
			 common.scrollAndClick(element, dropdownName);
			else
				common.clickJS(element, dropdownName);
			common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}
	
	public void selectOptionFromCriteriaDropdown(String option, String dropdownName,boolean following) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath("("+ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/following::span[contains(@class,'chevron-down')])[1]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			common.clickJS(element, dropdownName);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(1);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}
	
	public void selectOptionFromCriteriaDropdown_Index(String dropdownName) throws Exception {
	    try {
	    	dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ","_"));
		    By optionList=By.xpath("//p-dropdownitem/li/span");    
		    By chevronDown=By.xpath("//label[contains(text(),'"+dropdownName+"')]/parent::*//*[contains(@class,'chevron-down')]");
		    WebElement element = common.getElementExplicitWait(chevronDown, 0);
		    common.clickJS(element, dropdownName);
		    common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			common.clickJS(list.get(0), "option");
			new ReportUtil().logPass("option should be selected from dropdown "+dropdownName,
								("option is selected from dropdown "+dropdownName));
			common.explicitWait(.5);
		
		}catch(Exception e) {
			new ReportUtil().logFail("option should be selected from dropdown "+dropdownName, "option is not selected from dropdown "+dropdownName);
		}
	}
	
	public void selectOptionFromCriteriaDropdown_Index(String dropdownName,boolean scroll) throws Exception {
	    try {
	    	dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ","_"));
		    By optionList=By.xpath("//p-dropdownitem/li/span");    
		    By chevronDown=By.xpath("//label[contains(text(),'"+dropdownName+"')]/parent::*//*[contains(@class,'chevron-down')]");
		    WebElement element = common.getElementExplicitWait(chevronDown, 0);
		    if(scroll==true) {
		    	common.clickAnElement(element, dropdownName);}
		    else
		    	common.clickJS(element, dropdownName);
		    common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			common.clickJS(list.get(0), "option");
			new ReportUtil().logPass("option should be selected from dropdown "+dropdownName,
								("option is selected from dropdown "+dropdownName));
			common.explicitWait(.5);
		
		}catch(Exception e) {
			new ReportUtil().logFail("option should be selected from dropdown "+dropdownName, "option is not selected from dropdown "+dropdownName);
		}
	}
	
	
	
	public void selectOptionFromCriteriaDropdown_Index(String dropdownName,int index) throws Exception {
	    try {
	    	dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ","_"));
		    By optionList=By.xpath("//p-dropdownitem/li/span");    
		    By chevronDown=By.xpath("//label[contains(text(),'"+dropdownName+"')]/parent::*//*[contains(@class,'chevron-down')]");
		    WebElement element = common.getElementExplicitWait(chevronDown, 0);
		    common.clickJS(element, dropdownName);
		    common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			common.clickJS(list.get(index), dropdownName);
			new ReportUtil().logPass("option should be selected from dropdown "+dropdownName,
								("option is selected from dropdown "+dropdownName));
			common.explicitWait(.5);
		
		}catch(Exception e) {
			new ReportUtil().logFail("option should be selected from dropdown "+dropdownName, "option is not selected from dropdown "+dropdownName);
		}
	}
	
	public void selectOptionFromCriteriaDropdown(String dropdownName, String option, String popup) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			String popupLowerCase = popup.toLowerCase().replaceAll(" ", "-");
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown;
			if (popup.contains("transfer order popup")) {
				chevronDown = By.xpath("(//aw-" + popupLowerCase + "//label[contains(text(),'" + dropdownName
						+ "')]/following::span[contains(@class,'chevron-down')])[1]");
			} else
				chevronDown = By.xpath("(//aw-dialog-" + popupLowerCase + "//label[contains(text(),'" + dropdownName
						+ "')]/following::span[contains(@class,'chevron-down')])[1]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					ele.click();
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option " + option + " from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}
	
	
	public String selectOptionFromCriteriaDropdown_Index(String dropdownName, int index, String popup)throws Exception {
		try {
			String option = "";
			boolean found = false;
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			String popupLowerCase = popup.toLowerCase().replaceAll(" ", "-");
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown;
			if (popup.contains("work order table filters")) {
				chevronDown = By.xpath("(//aw-" + popupLowerCase + "//label[contains(text(),'" + dropdownName
						+ "')]/following::span[contains(@class,'chevron-down')])[1]");
			} else
				chevronDown = By.xpath("(//aw-dialog-" + popupLowerCase + "//label[contains(text(),'" + dropdownName
						+ "')]/following::span[contains(@class,'chevron-down')])[1]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(.5);
			List<WebElement> list = common.getElements(optionList);
			if (list.size() == 0) {
				new ReportUtil().logInfo("No options are available under dropdown " + dropdownName);
				return "";
			} else {
				option = list.get(index).getText().trim();
				common.clickJS(list.get(index), "Option at index " + index+ " in dropdown "+dropdownName);
				found = true;

				if (!found) {
					new ReportUtil().logFail(" option should be selected from dropdown " + dropdownName,
							" option is not selected from dropdown " + dropdownName);
					throw new Exception("Cannot Found option ");
				}
				return option;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
			return null;
		}
	}
	
	public void selectOptionFromCriteriaDropdownUnderSection(String option, String dropdownName, String section)
			throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath("//h4[contains(.,'" + section + "')]/..//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					ele.click();
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}

	public void selectOptionFromCriteriaDropdownUnderSectionPopup(String option, String dropdownName, String section)
			throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath("//a-modal//h4[contains(.,'" + section + "')]/..//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");
			//By chevronDown = By.xpath("//a-modal//h4[contains(.,'" + section + "')]/..//label[contains(text(),'"+dropdownName+"')]/../following-sibling::div//*[contains(@class,'chevron-down')]");
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					ele.click();
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}


	public void selectOptionFromCriteriaDropdown_AreaData(String option, String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(
					"//label[contains(text(),'" + dropdownName + "')]/parent::*//*[contains(@class,'chevron-down')]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			common.clickJS(element,dropdownName);
			common.explicitWait(3);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					common.clickJS(ele,dropdownName);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(1);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}
	
	public void searchAndselectOptionFromCriteriaDropdown(String option, String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(
					"//label[contains(text(),'" + dropdownName + "')]/parent::*//*[contains(@class,'chevron-down')]");

			By input=By.xpath("//label[contains(text(),'" + dropdownName + "')]/parent::*//input[contains(@class,'inputtext')]");
					
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			common.clickJS(element,dropdownName);
			common.explicitWait(3);
			common.performOperation(input, "input", option, dropdownName);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					common.clickJS(ele,dropdownName);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(1);
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}
	public void SelectOptionFromCriteriaDropdown(String option, String dropdownName) throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			//option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			common.clickJS(element, dropdownName);
			common.explicitWait(0.5);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					found = true;
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}
	public void selectOptionFromCriteriaDropdownUnderSectionpopup(String option, String dropdownName, String section)
			throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			//option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath("//a-modal//h4[contains(.,'" + section + "')]/..//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");
			//By chevronDown = By.xpath("//a-modal//h4[contains(.,'" + section + "')]/..//label[contains(text(),'"+dropdownName+"')]/../following-sibling::div//*[contains(@class,'chevron-down')]");
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					ele.click();
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}
	public void selectSearchResultFromBelowIndex(int index, String name) {

		try {
			common.explicitWait(1);
			//List<WebElement> list = common.getElements(dashboardPageObj.resultReceiptFolder);
			//List<WebElement> list = common.getElements(dashboardPageObj.resultsRows);
			//System.out.println(list.size());
			// System.out.println(list.get(result - 1).getAttribute("class"));
			//common.clickJS(list.get(result-1), "Result Check box" + " to select the " + name);
			DriverUtil.getInstance().getDriver().findElement(dashboardPageObj.resultReceiptFolder(index)).click();

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}
	public void selectOptionFromSiteDropdownUnderShipperSection(String option, String dropdownName, String section)
			throws Exception {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath("(//*[@id='procurement_request_search_shipper_site_ctrl']//div[2]/span)[1]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					ele.click();
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
							(option + " option is selected from dropdown " + dropdownName));
					common.explicitWait(.5);
					found = true;
					break;
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}


	public void selectSearchResultFromSubEquipmentsTable(int index, String name) {
		try {
			common.explicitWait(1);
			DriverUtil.getInstance().getDriver().findElement(dashboardPageObj.selectResultRowFromSubEquipmentsTable(index)).click();

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}
	public void clickNumberLinkFromResultRow(int index, String name) {
		try {
			common.explicitWait(1);
			DriverUtil.getInstance().getDriver().findElement(dashboardPageObj.clickNumberLinkResultRow(index)).click();

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}
	public void SelectMSNcheckboxOptionFromCriteriaDropdown(String option, String dd) throws Exception {
		boolean flag=false;
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-multiselectitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dd
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");
			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			common.clickJS(element, dd);
			common.explicitWait(3);
			common.click(By.xpath("//div[contains(@class,'multiselect-header')]//div[contains(@role,'checkbox')]/span"),"unselect");
			common.explicitWait(2);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					String optToSel = ele.getText().trim();
					WebElement ele1 = common.getElement(By.xpath("//p-multiselectitem//*[contains(text(),'"+optToSel+"')]"));
					//common.scrollAndClick(ele1,"");
					common.clickJS(ele1, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
							(option + " option is selected from dropdown " + dd));
					common.explicitWait(1);
					flag=true;
					break;
				}
			}
			if(flag==false){
				new ReportUtil().logFail("","Option is not available under dropdown "+dd);
			}
		}catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}

	}
}