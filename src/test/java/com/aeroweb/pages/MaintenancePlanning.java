package com.aeroweb.pages;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.aeroweb.objects.DashboardPageObjects;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.aeroweb.objects.LogisticModuleObjects;
import com.aeroweb.objects.MaintenancePlanningPageObjects;
import com.aeroweb.utils.*;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aeroweb.dataManager.Constant;
import com.aeroweb.objects.LoginPageObjects;
import com.aeroweb.objects.LogisticModuleObjects;
import com.aeroweb.objects.MaintenancePlanningPageObjects;
import com.aeroweb.utils.CompactUtil;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.JsonUtils;
import com.aeroweb.utils.ObjectUtil;
import com.aeroweb.utils.PropertyUtil;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;


public class MaintenancePlanning {
	DriverUtil driverUtil;
	ReportUtil report;
	PropertyUtil prop;
	MaintenancePlanningPageObjects maintenancePlanningPageObj;
	LogisticModuleObjects logisticModuleObj;
	public List<String> IDArry = new LinkedList<String>();
	public int operationIdIndex = 3;
	public static DecimalFormat df2 = new DecimalFormat("#.##");
	commonMethods common;
	DashboardPageObjects dashboardPageObj;


	public MaintenancePlanning() {
		common = new commonMethods();
		report = new ReportUtil();
		String propertyFilePath = "src/test/resources/properties/" + System.getProperty("appLanguage") + ".properties";
		this.prop = new PropertyUtil(propertyFilePath);
		maintenancePlanningPageObj = new MaintenancePlanningPageObjects();
		logisticModuleObj = new LogisticModuleObjects();
		dashboardPageObj = new DashboardPageObjects();
	}


	public boolean checkOnMaintenancePlanningWindow() {
		WebElement ele = common.getElementExplicitWait(maintenancePlanningPageObj.pageHeading);

		if (ele != null)
			return true;
		else
			return false;
	}

	public void selectMainCriteriaOLD(String criteria) {
		boolean optionFound = false;
		List<WebElement> list = common.getElements(maintenancePlanningPageObj.criteriaOptions);
		for (WebElement ele : list) {
			if (ele.getText().equalsIgnoreCase(criteria)) {
				ele.click();
				optionFound = true;
				break;
			}
		}
		if (!optionFound)
			new ReportUtil().logFail("Criteria option " + criteria + " Should be available", "Criteria option " + criteria + " Not available");
		else
			new ReportUtil().logInfo("Criteria option " + criteria + " Selected");

	}

	public void selectMainCriteria(String criteria) {
		criteria = prop.readPropertyData(criteria.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(criteria)), criteria);
		common.explicitWait(.3);
	}

	public void selectMainCriteriaWithContainer(String criteria) {
		criteria = prop.readPropertyData(criteria.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(criteria, true)), criteria);
	}

	public void performSearch() {
		common.clickJS(maintenancePlanningPageObj.seachButton, "Search Button");
	}

	public void checkResultsCount() {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.resultsCount);
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (extractedValue != null && extractedValue != "0")
				new ReportUtil().logInfo("Search Result are available");
			else
				new ReportUtil().logInfo("No Search Results available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void openSearchResult(int result) {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.resultsCounter);
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (result == 0)
				result++;
			if (extractedValue != null && extractedValue != "0") {
				int numbersOfResult = Integer.parseInt(extractedValue);
				if (numbersOfResult >= result) {
					List<WebElement> list = common
							.getElements(maintenancePlanningPageObj.resultsCheckBox);
					list.get(result).click();
				}
			}
			common.clickAnElement(maintenancePlanningPageObj.resultOpenButton, "Open Button");
			if (common.getElementExplicitWait(maintenancePlanningPageObj.equipementsHeading) != null)
				new ReportUtil().logInfo("Result Page opened");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to open search result");
		}
	}

	public void informationAvailableUnderEquipmentsection(String info) {

		if (info.equalsIgnoreCase("Designation"))
			checkInputElementNotNull(maintenancePlanningPageObj.designation, info, "input");
		else if (info.equalsIgnoreCase("S/N"))
			checkInputElementNotNull(maintenancePlanningPageObj.sn, info, "");
		else if (info.equalsIgnoreCase("Airworthiness Status") || info.equalsIgnoreCase("Etat de navigabilte"))
			checkInputElementNotNull(maintenancePlanningPageObj.airwothiness, info, "");
		else if (info.equalsIgnoreCase("Family-Variant") || info.equalsIgnoreCase("Familie variante"))
			checkInputElementNotNull(maintenancePlanningPageObj.familyVariant, info, "input");
		else if (info.equalsIgnoreCase("P/N"))
			checkInputElementNotNull(maintenancePlanningPageObj.pn, info, "input");
		else if (info.equalsIgnoreCase("Operational Status") || info.equalsIgnoreCase("Statut Operationanel"))
			checkInputElementNotNull(maintenancePlanningPageObj.operationalStatus, info, "input");
		else if (info.equalsIgnoreCase("FH"))
			checkInputElementNotNull(maintenancePlanningPageObj.fh, info, "input");
		else if (info.equalsIgnoreCase("CY"))
			checkInputElementNotNull(maintenancePlanningPageObj.cy, info, "input");
		else if (info.equalsIgnoreCase("Equivalent Potential") || info.equalsIgnoreCase("Potentiel equivalent"))
			checkInputElementNotNull(maintenancePlanningPageObj.equivalentPotential, info, "input");

	}

	public void informationAvailableUnderSection(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		By ele = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName + "')]");
		common.checkElementPresentByVisibility(ele, fieldName);

	}

	private void checkInputElementNotNull(By element, String generalText, String type) {
		WebElement ele = common.getElement(element);
		String innertext;
		if (type.equalsIgnoreCase("input"))
			innertext = ele.getAttribute("value");
		else
			innertext = ele.getText();
		if (innertext.length() > 0)
			new ReportUtil().logPass("Value should exist for " + generalText, "Value exist for " + generalText + " as '" + innertext + "'");
		else
			new ReportUtil().logFail("Value should exist for " + generalText, "Value not exist");

	}


	public void validate_Page_Displayed(String pageName) {
		try {
			if (pageName.equalsIgnoreCase("Part Number")
					&& System.getProperty("appLanguage").equalsIgnoreCase("French")) {
				// do Nothing
			} else
//				pageName = prop.readPropertyData(pageName.trim().replaceAll(" ", "_"));

			common.getElementExplicitWait(By.xpath(ObjectUtil.getXpathForPageHeading(pageName)));
			common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getXpathForPageHeading(pageName)),
					pageName + " page");
			new com.aeroweb.utils.JSWaiter().waitAllRequest();
			waitTillDataLoaded();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Navigation to page " + pageName + " is failed");
		}
	}

	public void validateNamePage_Displayed(String pageName) {
		try {
			new com.aeroweb.utils.JSWaiter().waitAllRequest();
			common.getElementExplicitWait(By.xpath(ObjectUtil.getXpathForPageHeading(pageName)));
			common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getXpathForPageHeading(pageName)),
					pageName + " page");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Navigation to page " + pageName + " is failed");
		}
	}

	public void validate_MaintenanceSumaryPage_Displayed(String pageName) {
		List<WebElement> pageHeader = common
				.getElements(maintenancePlanningPageObj.maintenanceSumaryPage);
		if (pageHeader.size() > 0) {
			report.logPass("User should navigate to " + pageName + "Page", "User navigated to " + pageName + "Page");
		} else
			report.logFail("User should navigate to " + pageName + "Page",
					"User is not navigated to " + pageName + "Page");

	}


	public void enterCriteria(String fieldName, String textToEnter) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getInputboxXpath(fieldName)), 0);
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpath(fieldName)), "input", textToEnter, fieldName);
	}

	public void validateMaintenanceScheduleOnPage(String name) {
		try {
			common.getElementExplicitWait(maintenancePlanningPageObj.loadingIcon, 1);
			List<WebElement> ms = common.getElements(maintenancePlanningPageObj.upcomingMaintenanceOperationSearchResults);
			int searchResults = ms.size();
			if (searchResults > 1)
				new ReportUtil().logPass("There should be a maintenance schedule on the page for " + name,
						"There is a maintenance schedule on the page for " + name + " with total count " + searchResults);
			else
				new ReportUtil().logInfo(
						"Currenly there are no operation available on maintenance page for selected " + name);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate maintenance schedule");
		}
	}

	public void validateNextTermData(String colName) {
		//waitTillDataLoaded();
		try {
			int noData = 0;
			int idNo = 0;
			String idName = null;
			boolean flag = true;
			List<WebElement> nextTerm = common.getElements(maintenancePlanningPageObj.nextTermListUnderArrow);
			String xpathToDataTermCon = "/tr/td[3]//*[contains(@style,'hidden')]/../../../td[3]";
			int size = nextTerm.size();
			for (WebElement ele : nextTerm) {
				idNo++;

				String date = ele.getText().trim();
				if (date.length() == 0) {
					noData++;
					idName = common.getElement(By.xpath("(//tbody[@class='p-treetable-tbody']" + xpathToDataTermCon + "/div/a)[" + idNo + "]")).getText();
					new ReportUtil().logInfo("Value not exist" + " for ID " + idName + " Test Data Issue");
					flag = false;

				}
			}
			if (flag == true) {
				new ReportUtil().logPass("Value should exist for row " + colName,
						"Value exist for row " + colName + " and number of rows validated '" + size + "'");
			} else {
				new ReportUtil().logInfo("Total operations not having next term data under a group are " + noData);
				new ReportUtil().logInfo("Total operations having next term data under a group are " + (size - noData));
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate next term data");

		}
	}

	public void validateNextTerm(String colName) {
		try {
			List<WebElement> nextTerm = common.getElements(maintenancePlanningPageObj.nextTermListUnderArrow);
			String date = nextTerm.get(0).getText().trim();
			if (date.length() != 0) {
				new ReportUtil().logPass("Next Term should exist", "Next Term exist");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate next term data");

		}
	}

	public List<String> selectTaskNotAssociatedWithWP(int NumberOfItems) {
		//waitTillDataLoaded();
		int index = 0;
		int j = 0;
		List<WebElement> checkboxes = common
				.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available",
					"No operations are available. please change the test data");
			Assert.assertTrue(false);
		}

		List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);

		try {
			for (WebElement ele : nextWPList) {
				if (ele.getText().trim().isEmpty()) {
					j++;
					common.scrollIntoView(checkboxes.get(index));
					System.out.println("clicking for task at index " + index);
					common.clickJS(checkboxes.get(index), "task " + j + " which is not associated with WP");
					IDArry.add(IdList.get(index).getText());
					index++;
					if (j == NumberOfItems)
						break;
				} else
					index++;
				if (index == checkboxes.size()) {
					deleteTaskAssignedToWP(NumberOfItems);
					new ReportUtil().logInfo("No more task which are not associated with WP are available");
				}

			}
			System.out.println("Task added " + IDArry.toString());
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select non associated WP");
		}

		return IDArry;
	}

	public List<String> selectTaskWithoutWP(int NumberOfItems) {
		new MaintenancePlanning().waitTillDataLoaded("yes");
		int index = 0;
		List<WebElement> checkboxes = common.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available", "No operations are available. please change the test data");
			Assert.assertTrue(false);
		}

		try {
			for (int i = 0; i < NumberOfItems; i++) {
				List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
				List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
				System.out.println(nextWPList.get(i).getText().trim());
				common.explicitWait(5);
				if (nextWPList.get(i).getText().trim().isEmpty()) {
					System.out.println(IdList.get(i).getText());
					IDArry.add(IdList.get(i).getText());
					index++;
				} else {
					System.out.println(IdList.get(i).getText());
					IDArry.add(IdList.get(i).getText());
					deleteWPFromTask(IdList.get(i).getText().trim());
					index++;
				}
				if (index == NumberOfItems) {
					break;
				}
			}
			new MaintenancePlanning().waitTillDataLoaded("yes");
			common.explicitWait(2);
			SelecTheTask(IDArry, NumberOfItems);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select non associated WP");
		}
		return IDArry;
	}

	public void deleteWPFromTask(String id) {
		try {
			//common.clickJS(common.getElement(By.xpath("//a[contains(text(),'"+id+"')]/../following-sibling::div//span")),"");
			//common.click(common.getElement(By.xpath("//a[contains(text(),'"+id+"')]/../following-sibling::div//span")),"");
			common.click(common.getElement(By.xpath("//a[contains(text(),'" + id + "')]/../../../following-sibling::div//span")), "");
			common.explicitWait(5);
			common.clickAnElement(maintenancePlanningPageObj.removeFromWP, "Remove from WP");
			common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on confirmation popup");
			new MaintenancePlanning().waitTillDataLoaded("yes");
			new LogisticsModule().clickRefresh();
			new MaintenancePlanning().waitTillDataLoaded("yes");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to Remove WP");
		}

	}

	public void SelecTheTask(List<String> selectedWorkOrder, int num) {
		try {
			waitTillDataLoaded("yes");
			//WebElement textbox = common.getElement(By.xpath("//div[@class='upcomimg-maintenance-operations']//input"));
			for (int i = 0; i < num; i++) {
				System.out.println(selectedWorkOrder.get(i));
				//textbox.sendKeys(selectedWorkOrder.get(i));
				//common.getElement(By.xpath("//a[contains(text(),'"+selectedWorkOrder.get(i)+"')]/../following-sibling::div//span")).click();
				common.getElement(By.xpath("//a[contains(text(),'" + selectedWorkOrder.get(i) + "')]/../../../following-sibling::div//span")).click();

				common.explicitWait(3);
				//textbox.clear();
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select task");
		}
	}

	public List<String> selectTaskNotAssociatedWithWP_old(int NumberOfItems) {
		//waitTillDataLoaded();
		int index = 0;
		int j = 0;
		List<WebElement> checkboxes = common
				.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available",
					"No operations are available. please change the test data");
			Assert.assertTrue(false);
		}

		List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);

		try {
			for (WebElement ele : nextWPList) {
				if (ele.getText().trim().isEmpty()) {
					j++;
					//newlyCreatedWP_Index=index;
					common.scrollIntoView(checkboxes.get(index));
					System.out.println("clicking for task at index " + index);
					common.clickJS(checkboxes.get(index), "task " + j + " which is not associated with WP");
					IDArry.add(IdList.get(index).getText());
					index++;
					if (j == NumberOfItems)
						break;
				} else
					index++;
				if (index == checkboxes.size()) {
					deleteTaskAssignedToWP(NumberOfItems);
					new ReportUtil().logInfo("No more task which are not associated with WP are available");
				}

			}
			System.out.println("Task added " + IDArry.toString());
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select non associated WP");
		}

		return IDArry;
	}

	public int captureWPIndex() {
		int j = 0;
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
		try {
			for (WebElement ele : nextWPList) {
				if (ele.getText().trim().isEmpty()) {
					System.out.println("WP will be created at index " + j);
					return j;
				} else {
					j = j + 1;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture WP Index");
		}
		return j;
	}


	public void deleteTaskAssignedToWP(int numberOfTask) {
		try {
			int index = 0;
			List<WebElement> checkboxes = common.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
			List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
			new ReportUtil().logInfo("Deleting the task associated with WP to maintan the test data for Automation");
			for (WebElement ele : nextWPList) {
				if (ele.getText().trim().length() > 0) {
					index++;
					common.scrollIntoView(checkboxes.get(index));
					common.clickJS(checkboxes.get(index), "task associated with " + index + " for deletion");
					if (index == numberOfTask)
						break;
				} else {
					index++;
				}
			}
			common.clickAnElement(maintenancePlanningPageObj.removeFromWP, "Remove from WP");
			common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on confirmation popup");

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to delete associated DT from maintenance table");
		}

	}

	public void clickCreateWP(String btn) {
		common.click(maintenancePlanningPageObj.createWP, btn + " button");
	}

	public void validateCreateWPBtnVisibility(String btn) {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.createWP, btn + "button");
	}

	public void validateAddToWP_BtnVisibility(String btn) {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.addToWPBtn, btn + " button");
	}

	public void clickOnAddToWP(String button) {
		common.clickAnElement(maintenancePlanningPageObj.addToWPBtn, button + " button");
	}

	public void verifyWorkPackageCreationPage() {
		try {

			WebElement element = common.getElementExplicitWait(maintenancePlanningPageObj.workPackageCreationHeader, 0);
			new ReportUtil().logInfo("User navigated to Work Package Creation page");

		} catch (Exception e) {
			new ReportUtil().logFail("User should navigate to Work Package Creation page", "User is not navigated to Work Package Creation page");

		}
	}

	public void enterDataToWPC(String name, String value) {
		common.performOperation(maintenancePlanningPageObj.name_WPC, "input", value, name);
	}

	public void selectMROCenter(String fieldName, String value) {

		common.clickJS(maintenancePlanningPageObj.dropdown_MROCenter, fieldName + " dropdown");
		common.explicitWait(2);

		By option = By.xpath("//li/span[contains(.,'" + value + "')]");
		common.clickJS(option, "dropdown option " + value);

	}

	public void enterDueDate(String fieldName, String date) {
		common.performOperation(maintenancePlanningPageObj.dueDate_WPC, "input", date, fieldName);
	}

	public void clickValidateBtn(String buttonName) {
		common.clickJS(maintenancePlanningPageObj.validateBtn, buttonName + " button");
	}


	public void clickValidateBtnWithoutJsWait() {
		common.clickJS_NoJSWaiter(maintenancePlanningPageObj.validateBtn, "Validate button");
	}

	public void clickValidateBtnCancelPopup() {
		common.click(maintenancePlanningPageObj.validateBtn, "Validate button");
	}

	public void clickValidateBtnForErrMsg() {
		common.scrollAndClick_WithoutJSWait(maintenancePlanningPageObj.validateBtn, "Validate button");
	}

	public void validateBtnDisabled(String buttonName) {
		try {
			String val = common.getElement(maintenancePlanningPageObj.validateBtnDisable).getAttribute("disabled");
			if (val.equalsIgnoreCase("true"))
				new ReportUtil().logPass(buttonName + " button should be disable", buttonName + " button is disable");
			else
				new ReportUtil().logFail(buttonName + " button should be disable", buttonName + " button is not disable");

		} catch (Exception e) {
			new ReportUtil().logFail(buttonName + " button should be disable", buttonName + " button is not disable");
		}
	}

	public void validateBtnClickable(String buttonName) {
//		   WebElement stats= common.getElementExplicitWait(maintenancePlanningPageObj.validateBtn, 2);
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.validateBtn, buttonName);
//		   try {
//
//			   new ReportUtil().logPass(buttonName+" button should be enable", buttonName+" button is enable");
//		   }catch(Exception e) {
//			   new ReportUtil().logFail(buttonName+" button should be enable", buttonName+" button is not enable");
//		   }

	}

	public void enterDueDateWithExpectedType(String type) {
		int index = 10;

		if (type.toLowerCase().contains("yesterday")) {

			common.clickJS(maintenancePlanningPageObj.dueDate_WPC,
					"due date field to fill the Earlier date");
			common.clickJS(maintenancePlanningPageObj.calendarPrevIcon,
					"Calendar previous icon to select earlier date");
			common.clickJS(By.xpath("//*[contains(.,'" + index + "')][contains(@class,'p-ripple')]"),
					"Earlier date");
		} else {

			common.clickJS(maintenancePlanningPageObj.dueDate_WPC, "due date field to fill the future date");
			common.clickJS(maintenancePlanningPageObj.calendarNextIcon, "Calendar next icon to select furure date");
			common.clickJS(maintenancePlanningPageObj.calendarNextIcon,
					"next icon one more time to select furure date");
			common.clickJS(By.xpath("//*[contains(.,'" + index + "')][contains(@class,'p-ripple')]"),
					"Future date");

		}
		common.explicitWait(1);
		common.getElement(maintenancePlanningPageObj.name_WPC).click();
	}


	public void clickNextWPLink() {
		try {
			List<WebElement> nextWPList = common
					.getElements(maintenancePlanningPageObj.nextWPUnderArrow);
			WebElement firstNextWP = nextWPList.get(1);
			common.scrollIntoView(firstNextWP);
			common.clickAnElement(firstNextWP, "Next WP link");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on Next WP link");
		}
	}


	//public static String dtNumber;
	public String captureNewlyCreatedDT(int num) {
		try {
			List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
			String dtNumber = nextWPList.get(num).getText();
			System.out.println("Captured DT is " + dtNumber);
			return dtNumber;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture DT number");
			return null;
		}

	}

	public void clickWorkPackageBtn() {
		common.clickAnElement(maintenancePlanningPageObj.workPackageBtn, "Work Package" + " button");
	}

	public void clickOnNewlyCreatedWorkPackage(String dtNumber) {
		By ele = By.xpath("//a[contains(text(),'" + dtNumber + "')]");
		common.click(ele, "DT link");
	}

	public void verifyAddedWorkOrderList(List<String> workOrders) {
		List<WebElement> addedID = common.getElements(maintenancePlanningPageObj.workOrderID);
		String[] expectedId;
		System.out.println("Work orders " + workOrders.toString());
		for (WebElement ele : addedID) {
			expectedId = ele.getText().split(" ");
			if (workOrders.toString().contains(expectedId[0])) {
				new ReportUtil().logPass("Item " + ele.getText() + " should added to the work orders list", "Item " + ele.getText() + " is added to the work orders list");

			} else {
				new ReportUtil().logFail("Item " + ele.getText() + " should added to the work orders list", "Item " + ele.getText() + " is not added to the work orders list");

			}
		}
	}

	public void buttonClickOnPopup(String button) {
		button = prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath("//span[contains(text(),'" + button + "')]"), button + " button");

	}

	public void buttonClickOnPopupWithoutJSWait(String button) {
		button = prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		common.clickJS_WithoutJSWait(By.xpath("//span[contains(text(),'" + button + "')]"), button + " button");

	}

	public void clickOnPopupBtn(String btn) {
		if (btn.equalsIgnoreCase("yes")) {
			common.clickJS(maintenancePlanningPageObj.popup_YesBtn, btn + " button");
		} else if (btn.equalsIgnoreCase("no")) {
			common.clickJS(maintenancePlanningPageObj.popup_NoBtn, btn + " button");
		}
	}

	public void validateWorkOrderPage(String page) {
		common.validatePageNavigation(maintenancePlanningPageObj.workOrder, page);
	}

	public void clickWorkOrder(String btn) {
		common.getElementExplicitWait(maintenancePlanningPageObj.workOrder, 2);
		common.clickJS(maintenancePlanningPageObj.workOrder, btn);
	}


	public String verifyWPNumberAvailableForSelectedTask(int index) {
		try {
			common.explicitWait(2);
			List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
			WebElement firstNextWP = nextWPList.get(index);
			if (firstNextWP.getText().isEmpty())
				new ReportUtil().logFail("WP number should be created",
						"WP number is not created or the selected task");
			else
				new ReportUtil().logPass("WP number should be created",
						"WP number is created successfully for the selected task as " + firstNextWP.getText());

			String dt = captureNewlyCreatedDT(index);
			return dt;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to Verify WP number for selected task");
			return null;
		}
	}

	public void verifyButonColorForBlue(String elementName, String color) {
		elementName = prop.readPropertyData(elementName.trim().replaceAll(" ", "_"));
		By loc = By.xpath("//span[text()='" + elementName + "']/..");
		if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("Firefox"))
			common.checkBackgroundColor(loc, elementName, color);
		else if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("edge"))
			common.checkBackgroundColorForEdge(loc, elementName, color);
	}

	public void verifyMsgForInvallidDueDate() {
		try {
			common.clickJS_WithoutJSWait(maintenancePlanningPageObj.validateBtn, "Validate");
			new ReportUtil().logInfo("Clicked on validate button");
			common.getElement(maintenancePlanningPageObj.errorMsgDueDate).isDisplayed();
			new ReportUtil().logPass("Error message should be displayed", "Error message displayed for invalid due date");
		} catch (Exception e) {
			new ReportUtil().logFail("Error message should be displayed", "Error message is not displayed for invalid due date");
		}

	}

	public void verifyCreateWP_ConfirmationMsg(String msg) {
		try {
			common.getElement(maintenancePlanningPageObj.createWP_ConfirmMsg);
			new ReportUtil().logPass("Green confirmation message " + msg + " should display", "Green confirmation message " + msg + " is displayed");
		} catch (Exception e) {
			new ReportUtil().logFail("Green confirmation message " + msg + " should display", "Green confirmation message " + msg + " is not displayed");
		}

	}

	public void verifyDocumentPageIsEmpty() {
		try {
			common.getElementExplicitWait(By.xpath("//h3[contains(.,'" +
					prop.readPropertyData("Documents") + "')]"), 0);
			String number = common.getText(By.xpath("//h3[contains(.,'" +
					prop.readPropertyData("Documents") + "')]"), "Documents");
			if (Integer.parseInt(CompactUtil.extractNumber(number)) == 0)
				new ReportUtil().logPass("Document page should be empty", "Document page is empty");
			else {
				new ReportUtil().logFail("Document page should be empty", "Document page is not empty");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("Document page should be empty", "Document page can't be verified");
		}

	}

	public void verifyNewItemInTheList(String dtNumber) {
		common.getElementExplicitWait(By.xpath("//a[contains(text(),'" + dtNumber + "')]"), 0);
		common.checkElementPresentByVisibility(By.xpath("//a[contains(text(),'" + dtNumber + "')]"), "Newly created WP " + dtNumber);

	}

	public String selectNewlyCreatedWPFromWorkPackage() {
		try {
			List<WebElement> ele = common.getElements(maintenancePlanningPageObj.dtListUnderWorkPackage);
			int latestDt = ele.size();
			String text = ele.get(0).getText();
			System.out.println("Newly created WP is " + text);
			common.clickJS(ele.get(0), "Newly created WP under work package");
			return text;
		} catch (Exception e) {
			new ReportUtil().logFail("User should be able to click on newly created WP",
					"Can not click on newly created WP");
			return null;
		}
	}

	public void verifyUnitsCoversionToFightHours(String unit, String flightHours) {
		unit = prop.readPropertyData(unit.trim().replaceAll(" ", "_"));
		flightHours = prop.readPropertyData(flightHours.trim().replaceAll(" ", "_"));

		try {
			int unitIndex = 0, flightHoursIndex = 0;
			List<WebElement> ele = common.getElements(By.xpath("//th"));
			for (int i = 0; i < ele.size(); i++) {
				if (ele.get(i).getText().contains(unit))
					unitIndex = i + 1;
				else if (ele.get(i).getText().contains(flightHours))
					flightHoursIndex = i + 1;
			}
			System.out.println("Index of unit is " + unitIndex);
			System.out.println("Index of flight is " + flightHoursIndex);

			List<WebElement> totalRows = common.getElements(By.xpath("//tr"));
			for (int i = 1; i < totalRows.size(); i++) {
				WebElement flight = common.getElement(By.xpath("(//tr/td[" + flightHoursIndex + "])[" + (i) + "]"));
				WebElement units = common.getElement(By.xpath("(//tr/td[" + unitIndex + "])[" + (i) + "]"));
				String str = flight.getText().trim();
				if (str.matches("^\\d+?\\.\\d+?$") || str.matches("[0-9]+")) {
					new ReportUtil().logPass("Unit of Measure --> " + units.getText() +
							" should be converted into Flight Hours", "Unit of Measure --> " + units.getText() +
							" is converted into Flight Hours as " + str);
				} else {
					new ReportUtil().logFail("Unit of Measure --> " + units.getText() +
							" should be converted into Flight Hours", "Unit of Measure --> " + units.getText() +
							" is not converted into Flight Hours and diplayed as " + str);
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify unit conversion table");
		}
	}


	public void selectUnitType(String unit) {
		unit = prop.readPropertyData(unit.trim().replaceAll(" ", "_"));
		WebElement showAllAction = common.getElement(maintenancePlanningPageObj.showAllActionsBtn);
		common.scrollIntoView(showAllAction);
		common.clickJS(maintenancePlanningPageObj.showAllActionsBtn, "Show All Actions");
		common.clickJS_NoJSWaiter(maintenancePlanningPageObj.potentialUnit, "Potential Unit option");

		WebElement expectedLoc = common.getElement(By.xpath("//button[contains(text(),'" + unit + "')]"));
		common.clickAnElement(expectedLoc, unit + " option");
		new ReportUtil().logInfo(unit + " has been selected for Equivalent Potential column");

	}


	public Map<Integer, Double> verifyByDefaultPotentialUnitValues(String fieldName, String unit) {
		//Equivalent Pot  H

		Map<Integer, Double> mp = new HashMap<>();
		int valueAvailableAtRow = 0;
		double valueInHours;
		try {
			//waitTillDataLoaded();
			List<WebElement> equivPotList = common.getElements(maintenancePlanningPageObj.equivalentPotList);
			List<WebElement> id = common.getElements(maintenancePlanningPageObj.listOfIdNothavingArrow);
			int idNo = 0;
			boolean flag = true;
			for (WebElement ele : equivPotList) {
				idNo++;
				if (ele.getText().trim().isEmpty()) {
					// Do nothing
				} else if (!ele.getText().contains(unit)) {
					String idName = common.getText(id.get(idNo - 1), "ID");
					new ReportUtil().logFail("By default " + fieldName + " unit should be display in " + unit,
							"By default " + fieldName + " is not displayed in " + unit + " for id " + idName);
					flag = false;
					break;
				}
				if (!ele.getText().trim().isEmpty()) {
					if (valueAvailableAtRow == 0) {
						valueAvailableAtRow = idNo;
					}
				}
			}
			if (flag == true) {
				new ReportUtil().logPass("By default " + fieldName + " unit should be display in " + unit,
						"By default " + fieldName + " is displayed in " + unit);
			}
			System.out.println("Value ava at row " + valueAvailableAtRow);
			String[] values = equivPotList.get(valueAvailableAtRow - 1).getText().split(" ");
			valueInHours = Double.parseDouble(values[0]);
			System.out.println("Value captured in hours as " + valueInHours);
			mp.put(valueAvailableAtRow, valueInHours);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify default potential unit");
		}
		return mp;
	}


	public void calculateRationBetweenUnits(String unit, double val, Map<Integer, Double> mp) {
		//waitTillDataLoaded();
		int key = 0;
		double value;
		common.getElementExplicitWaitVisibility(maintenancePlanningPageObj.equivalentPotList,
				"75", "Maintenance operation");
		try {
			for (Integer kk : mp.keySet()) {
				key = kk;
			}
			value = mp.get(key);
			WebElement ele = common.getElements(maintenancePlanningPageObj.equivalentPotList).get(key - 1);
			String[] valueAfter = ele.getText().split(" ");
			String actualValue = valueAfter[0];
			System.out.println("raw value " + actualValue);
			double d = Double.parseDouble(actualValue);
			d = d * val;
			System.out.println("actual value after conversion1 " + d);
			System.out.println("Round off value " + Math.round(d));

			df2.setRoundingMode(RoundingMode.DOWN);

			System.out.println("Value in two decimal format  " + df2.format(d));
			System.out.println("Value should be in round off " + Math.round(value));

			if (Math.round(Double.parseDouble(df2.format(d))) == Math.round(value)) {
				new ReportUtil().logPass("Ratio betwen value selected before and after should be as per unit conversion matrix",
						"Ratio betwen value selected before and after is as per unit conversion matrix");
			} else {
				new ReportUtil().logFail("Ratio betwen value selected before and after should be as per unit conversion matrix",
						"Ratio betwen value selected before and after is not as per unit conversion matrix-->expected=" + value
								+ " Actual " + d);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify the ratio betwen value selected before and after");
		}

	}

	public void verifyAddToWP_PanelDisplayed() {

		int dtAvailable = common.getElements(maintenancePlanningPageObj.dtsUnderAddToWP).size();
		new ReportUtil().logInfo("Panel displayed where WP are available to link-Total WP displayed are " + dtAvailable);
	}

	public String linkedDT = null;

	public void selectDTFromAddToWPPanel(int index) {

		WebElement dtLinkIcon = common.getElements(maintenancePlanningPageObj.dtsUnderAddToWP).get(index);
		String rawDT = common.getElements(maintenancePlanningPageObj.dtTextUnderAddToWP).get(index).getText();
		rawDT = CompactUtil.extractNumber(rawDT);
		System.out.println("WP selected from Add To WP is " + rawDT);
		common.clickJS(dtLinkIcon, "link icon for WP " + rawDT);
		linkedDT = rawDT;

	}

	public void selectTaskAsociatedWithWP(int index) {
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);
		common.click(nextWPList.get(index - 1), "Newly created Work Package");
	}


	public void verifyAddedTaskIsAssociatedInWorkOrderList(List<String> workOrders) {
		List<WebElement> addedID = common.getElements(maintenancePlanningPageObj.workOrderID);
		List<String> expectedId = new ArrayList<String>();

		for (WebElement ele : addedID) {
			//String[] id = ele.getText().split(" ");
			String id = ele.getText().trim();
			expectedId.add(id);
		}
		System.out.println(" work order string" + expectedId);

		for (String str : workOrders) {
			if (expectedId.contains(str)) {
				common.scrollIntoView(addedID.get((addedID.size() - 1)));
				new ReportUtil().logPass("Item " + str + " should added to the work orders list", "Item " + str + " is added to the work orders list");

			} else {
				new ReportUtil().logFail("Item " + str + " should added to the work orders list", "Item " + str + " is not added to the work orders list");

			}
		}
	}

	public void checkResultsAfterSearch(String page) {
		List<WebElement> list = common.getElements(dashboardPageObj.resultsCheckBoxBody);
		WebElement results = common.getElement(maintenancePlanningPageObj.resultsCount);
		String extractedValue = CompactUtil.extractNumber(results.getText());
		if (extractedValue != null && extractedValue != "0")
			new ReportUtil().logInfo("Search Result are available under page " + page);
		else
			new ReportUtil().logInfo("No Search Results available under page " + page);
	}

	public void openWorkPackageAtIndex(int index, int NumberColumn) {
		WebElement ele = common.getElement(By.xpath("//tr[" + index + "]/td[" + NumberColumn + "]/a"));
		common.clickAnElement(ele, "Work package " + ele.getText());

	}

	public void validate_FieldsInWorkOrderEditionPopup(List<String> data) {
		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath("//label[contains(text(),'" +
					str + "')]"), "field " + str);
		}
	}

	public void verifyAdditionalField(String type, List<String> data) {
		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath("//label[contains(text(),'" +
					str + "')]"), "field " + str + " for selected type " + type);
		}

	}

	public void verifyNoAdditionalFieldDisplayed(List<String> data) {
		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			try {
				common.getElement(By.xpath("//label[contains(text(),'" + str + "')]")).click();
				;
				new ReportUtil().logFail("Additional field should not be present", "Additional field are present");
			} catch (Exception e) {
				new ReportUtil().logPass("Additional field " + str + " should not be present", "Additional field " + str + " is not present");
			}
		}
	}

	public void clickOnWPStatus(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			String xpathToContainer = ObjectUtil.xpathToTabContainer;
			WebElement ele = common.getElement(By.xpath(xpathToContainer + "//div[contains(.,'" + status + "')][contains(@class,'object-status')]"));
			common.clickJS(ele, "status " + status);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on status text");
		}
	}


	public void changeTheWPStatus(String buttonName) {
		try {
			buttonName = prop.readPropertyData(buttonName.trim().replaceAll(" ", "_"));
			WebElement ele = common.getElement(By.xpath("//span[contains(text(),'" + buttonName + "')]"));
			common.clickAnElement(ele, buttonName + " button " + " on the popup");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate the status text");
		}
	}

	public void refreshWorkPackage() {
		common.clickJS_WithoutJSWait(maintenancePlanningPageObj.showAllActionsBtn, "Show All Actions buton");
		common.clickAnElement(maintenancePlanningPageObj.refreshBtn, "Refresh button");
	}


	public void validate_WPAndWOsStatusAreSame() {
		try {
			int index = 0;
			String wpStatus = common.getElement(maintenancePlanningPageObj.statusOfWP).getText().trim();
			//List<WebElement> itemList = common.getElements(By.xpath("//span[@class='value'][contains(text(),'" + wpStatus + "')]"));
			List<WebElement> itemList = common.getElements(By.xpath("//div[@class='yac-table-cell']//span[contains(text(),'" + wpStatus + "')]"));
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

			for (WebElement item : itemList) {
				if (item.getText().trim().equalsIgnoreCase(wpStatus)) {
					new ReportUtil().logPass("Status of Work Order " + listOfTo.get(index).getText() + "  should be " + wpStatus,
							"Status of Work Order " + listOfTo.get(index).getText() + "  is " + wpStatus);
				} else {
					new ReportUtil().logFail("Status of Work Order " + listOfTo.get(index).getText() + "  should be " + wpStatus,
							"Status of Work Order " + listOfTo.get(index).getText() + "  is not " + wpStatus);
				}
				index++;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate WP status and Work Order are same");
		}

	}

	public void validate_WOStatus(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			//List<WebElement> itemList = common.getElements(By.xpath("//span[@class='value'][contains(text(),'" + status + "')]"));
			List<WebElement> itemList = common.getElements(By.xpath("//div[@class='yac-table-cell']//span[contains(text(),'" + status + "')]"));
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

			if (itemList.size() == listOfTo.size()) {
				for (WebElement ele : listOfTo) {
					new ReportUtil().logPass("Status of Work Order " + ele.getText() + "  should be " + status,
							"Status of Work Order " + ele.getText() + "  is " + status);
				}
			} else {
				new ReportUtil().logFail("Status of all the Work Orders should be " + status,
						"Status of all the Work Orders is not " + status);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order status");
		}

	}

	public void createWorkOrderIfNotAvailable() {
		try {
			common.explicitWait(2);
			String count = common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order Count");
			count = CompactUtil.extractNumber(count);
			if (Integer.parseInt(count) == 0) {
				new ReportUtil().logInfo("There are no work orders available so we need to create the work order first");
				new LogisticsModule().clickOnBtn("Add");
				new LogisticsModule().verifyPopupIsDisplayed("Work Order Creation");
				new LogisticsModule().selectOptionFromDD_popup("Manual entry", "Type");
				new LogisticsModule().selectOptionFromDD_popup(0, "Workshop");
				new LogisticsModule().selectOptionFromDD_popup("Corrective Maintenance", "Action Type");
				clickButtonOnPopUP_WithoutJSWait("Work Order Edition", "Validate");
				new ReportUtil().logInfo("New work order creation process is completed");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to create work order");
		}
	}

	public void addFreshOT() {
		try {
			new LogisticsModule().clickOnBtn("Add");
			new LogisticsModule().verifyPopupIsDisplayed("Work Order Creation");
			new LogisticsModule().selectOptionFromDD_popup("Manual entry", "Type");
			new LogisticsModule().selectOptionFromDD_popup(1, "Workshop");
			new LogisticsModule().selectOptionFromDD_popup("Corrective Maintenance", "Action Type");
			clickButtonOnPopUP("Work Order Edition", "Validate");
			new ReportUtil().logInfo("New work order creation process is completed");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to create work order");
		}
	}

	public void validate_WPAndLatestWOStatusAreSame(String tableName, String columnName) {
		try {
			String wpStatus = common.getElement(maintenancePlanningPageObj.statusOfWP).getText().trim();
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
			//int columnIndex = getColumnIndexFromTable(tableName, columnName);

			By loc = By.xpath("//div[@class='yac-table-cell']//span[contains(text(),'" + wpStatus + "')]");
			List<WebElement> listOfItems = common.getElements(loc);

			if (listOfItems.get(listOfItems.size() - 1).getText().trim().equalsIgnoreCase(wpStatus)) {
				new ReportUtil().logPass("Status of Work Order " + listOfTo.get(listOfItems.size() - 1).getText() + "  should be " + wpStatus,
						"Status of Work Order " + listOfTo.get(listOfItems.size() - 1).getText() + "  is " + wpStatus);
			} else {
				new ReportUtil().logFail("Status of Work Order " + listOfTo.get(listOfItems.size() - 1).getText() + "  should be " + wpStatus,
						"Status of Work Order " + listOfTo.get(listOfItems.size() - 1).getText() + "  is not " + wpStatus);
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate WP status and Work Order are same");
		}

	}

	public void validate_RecentlyCreatedWOStatus(String status, String tableName, String column) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			List<WebElement> listOfWO = common.getElements(maintenancePlanningPageObj.nameOfTO);
			int columnIndex = getColumnIndexFromTable(tableName, column);
			By loc = By.xpath("//div[@class='yac-table-cell']//span[contains(text(),'" + status + "')]");
			List<WebElement> listOfTo = common.getElements(loc);

			if (listOfTo.get(listOfTo.size() - 1).getText().equalsIgnoreCase(status)) {
				new ReportUtil().logPass(
						"Status of Work Order " + listOfWO.get(listOfWO.size() - 1).getText() + "  should be " + status,
						"Status of Work Order " + listOfWO.get(listOfWO.size() - 1).getText() + "  is " + status);
			} else {
				new ReportUtil().logFail(
						"Status of Work Order " + listOfWO.get(listOfWO.size() - 1).getText() + "  should be " + status,
						"Status of Work Order " + listOfWO.get(listOfWO.size() - 1).getText() + "  is not " + status);
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate WP status and Work Order are same");
		}

	}

	public void verifyStatusPoupDisplayed() {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.statusPopupForWP,
				"Work Package status popup");
	}

	public void validate_WPStatus(String status) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.statusXpath(status)), 0);
	}

	public void verifyStatusPoupNotDisplayed() {
		common.getElementExplicitWaitInVisibility(maintenancePlanningPageObj.statusPopupForWP,
				"1", "status popup");
	}


	public Map<String, String> openWorkPackage(int index) {
		Map<String, String> wpDetails = new HashMap<String, String>();
		try {
			List<WebElement> wpNumberList = common.getElements(maintenancePlanningPageObj.workPackageNumbers);
			new ReportUtil().logInfo("Work page selected--> " + wpNumberList.get(index - 1).getText());
			String msnNumber = captureMSNNumberFromSelectedWP(index);
			wpDetails.put("MSN", msnNumber);
			wpDetails.put("WP Number", wpNumberList.get(index - 1).getText());
			common.clickJS(maintenancePlanningPageObj.resultOpenButton, "Open Button");
			return wpDetails;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to open work package");
			Assert.assertTrue("Unable to open work package", false);
			return null;
		}
	}

	public void clickOnSearchIconOfField(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		List<WebElement> searchIconWithCross = common.getElements(By.xpath(ObjectUtil.getSearchIconXpathLabel(fieldName)));
		if (searchIconWithCross.size() == 2) {
			common.clickAnElement(searchIconWithCross.get(0), fieldName + " search icon");
		}
		WebElement searchIcon = common.getElement(By.xpath(ObjectUtil.getSearchIconXpathLabel(fieldName)));
		common.clickAnElement(searchIcon, fieldName + " search icon");
	}

	public void clickOnSearchIconOfFieldBesidePlus(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		List<WebElement> searchIconWithCross = common.getElements(By.xpath(ObjectUtil.getSearchIconXpathLabel(fieldName)));
		if (searchIconWithCross.size() == 2) {
			common.clickAnElement(searchIconWithCross.get(0), fieldName + " search icon");
		}
	}

	public void clickOnPlusIcon(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		List<WebElement> searchIconWithCross = common.getElements(By.xpath(ObjectUtil.getSearchIconXpathLabel(fieldName)));
		if (searchIconWithCross.size() == 2) {
			common.clickAnElement(searchIconWithCross.get(1), fieldName + " Plus icon");
		}
	}

	public void searchAndAddItemToTheList(String popupName, String searchBtnName, int index) {
		try {
			String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
			searchBtnName = prop.readPropertyData(searchBtnName.trim().replaceAll(" ", "_"));
			WebElement searchIcon = common.getElement(By.xpath(ObjectUtil.getButtonXpathOnPopup(popupLowerCase, searchBtnName)));
			common.clickAnElement(searchIcon, "search button on " + popupName + " popup");
			List<WebElement> resultItems;
			if (popupName.equalsIgnoreCase("Search Evolution")) {
				resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]"));
				common.clickAnElement(resultItems.get(index), "Item on Search result");
			} else {
				resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]/span"));
				common.clickAnElement(resultItems.get(index), "Item on Search result");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to search and select item from result table on the popup");
		}
	}

	public void AddItemToTheList(String popupName) {
		try {
			String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
			if (popupName.equalsIgnoreCase("search equipment") || popupName.equalsIgnoreCase("Search Fleet") || popupName.equalsIgnoreCase("Search Item")
					|| popupName.equalsIgnoreCase("Search Operation") || popupName.equalsIgnoreCase("Search Mission") || popupName.equalsIgnoreCase("Search Aircraft")) {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]"));
				common.clickAnElement(resultItems.get(0), "Item on Search result table");
			} else {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]/span"));
				common.clickAnElement(resultItems.get(0), "Item on Search result table");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select item from result table on the popup");
		}
	}

	public void AddItemToTheList(String popupName, int index) {
		try {
			String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
			if (popupName.equalsIgnoreCase("search equipment") || popupName.equalsIgnoreCase("Search Fleet") || popupName.equalsIgnoreCase("Search Item")) {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]"));
				common.clickAnElement(resultItems.get(index), "Item on Search result table");
			} else {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]/span"));
				common.clickAnElement(resultItems.get(index), "Item on Search result table");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select item from result table on the popup");
		}
	}

	public void clickButtonOnPopUP(String popupName, String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
		WebElement btn = common.getElement(By.xpath("//aw-dialog-" + popupLowerCase +
				"//button/span[contains(.,'" + btnName + "')]"));
		common.clickJS(btn, btnName + " on " + popupName + " popup");
		common.explicitWait(2);

	}

	public void clickButtonOnPopUP_WithoutJSWait(String popupName, String btnName) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
		WebElement btn = common.getElement(By.xpath("//aw-dialog-" + popupLowerCase +
				"//button/span[contains(.,'" + btnName + "')]"));
		common.clickJS_withoutJSWaiter(btn, btnName + " on " + popupName + " popup");

	}

	public void saveForecastHeader(String popupName, String btnName) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
		WebElement btn = common.getElement(By.xpath("//aw-dialog-" + popupLowerCase +
				"//button/span[contains(.,'" + btnName + "')]"));
		common.clickJS_withoutJSWaiter(btn, btnName + " on " + popupName + " popup");
		common.getElementExplicitWait(maintenancePlanningPageObj.forecastHeader, 1);
	}

	public void clickButtonOnPopUP_WithoutDialog(String popupName, String btnName) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
		WebElement btn = common.getElement(By.xpath("//aw-" + popupLowerCase +
				"//button/span[contains(.,'" + btnName + "')]"));
		common.clickJS(btn, btnName + " on " + popupName + " popup");

	}

	public void openCreatedWorkOrder() throws Exception {
		try {
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
			common.click(listOfTo.get(listOfTo.size() - 1), "Work Oder " + listOfTo.get(listOfTo.size() - 1).getText());
			common.getElementExplicitWait(maintenancePlanningPageObj.workOrderPageTitle, 0);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to open the Work Order");
			throw new Exception("Unable to open the Work Order");
		}
	}

	public void openCreatedDefect() throws Exception {
		try {
			List<WebElement> listOfDf = common.getElements(maintenancePlanningPageObj.nameOfDf);
			common.clickAnElement(listOfDf.get(listOfDf.size() - 1), "Work Oder " + listOfDf.get(listOfDf.size() - 1).getText());
			common.getElementExplicitWait(maintenancePlanningPageObj.defectPageTitle, 0);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to open the Work Order");
			throw new Exception("Unable to open the Work Order");
		}
	}

	public void validateWPInfoInWO(Map<String, String> wpDetails) {
		String wpNumber, msn;
		try {
			wpNumber = common.getElement(maintenancePlanningPageObj.wpNumberOnWOScreen).getText().trim();
			msn = common.getElement(maintenancePlanningPageObj.msnNumberOnWOScreen).getText().trim();
			if (wpDetails.get("MSN").trim().equalsIgnoreCase("")) {

				if (wpDetails.get("WP Number").equalsIgnoreCase(wpNumber)) {
					new ReportUtil().logPass("Value of WP Number should be same as " + wpDetails.get("WP Number") + " ",
							"Value of WP Number is same");
				} else {
					new ReportUtil().logFail("Value of WP Number should be same as " + wpDetails.get("WP Number") + " ",
							"Value of WP Number is not same");
				}
			} else {
				if (wpDetails.get("WP Number").equalsIgnoreCase(wpNumber)
						&& msn.contains(wpDetails.get("MSN"))) {

					new ReportUtil().logPass(
							"Value of MSN and WP Number should be same as " + wpDetails.get("MSN") + ","
									+ wpDetails.get("WP Number") + " respectively",
							"Value of MSN and WP Number are same");
				} else {
					new ReportUtil().logFail(
							"Value of MSN and WP Number should be same as " + wpDetails.get("MSN") + ","
									+ wpDetails.get("WP Number") + " respectively",
							"Value of MSN and WP Number are not same");
				}

			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate work package details");
		}
	}

	public String captureMSNNumberFromSelectedWP(int index) {
		try {
			List<WebElement> listOfAsset = common.getElements(maintenancePlanningPageObj.workPackageMSN);
			String[] msn = listOfAsset.get(index - 1).getText().split("\\|");

			if (msn[1].contains("MSN")) {
				new ReportUtil().logInfo("MSN has been captured as " + CompactUtil.extractNumber(msn[1]));
				return CompactUtil.extractNumber(msn[1]);
			} else {
				new ReportUtil().logInfo("MSN number is not mentioned in asset");
				return "";
			}

		} catch (Exception e) {
			new ReportUtil().logInfo("MSN number is not captured");
			return null;
		}

	}


	public int validateWorkOrderInWP() {
		List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
		String count = common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order Count");
		count = CompactUtil.extractNumber(count);
		if (Integer.parseInt(count) == 0) {
			new ReportUtil().logInfo("No work order is available for selected  work package");
			return 0;
		} else {
			for (WebElement ele : listOfTo) {
				new ReportUtil().logInfo("Work order " + ele.getText() + " is present in the work package");
			}
			return listOfTo.size();
		}

	}

	public int countWorkOrderInWP() {
		String count = common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order Count");
		count = CompactUtil.extractNumber(count);
		if (Integer.parseInt(count) == 0) {
			new ReportUtil().logInfo("No work order is available for selected  work package");
			return 0;
		} else {
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
			for (WebElement ele : listOfTo) {
				new ReportUtil().logInfo("Work order " + ele.getText() + " is present in the work package");
			}
			return listOfTo.size();
		}

	}

	public void verifyNewWOIsAddedToWP(int numberOfWO) {
		List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
		System.out.println("count" + listOfTo.size() + numberOfWO);
		if (listOfTo.size() == numberOfWO + 1) {
			new ReportUtil().logPass("Work order should be added to the list",
					"Work order is added to the list");
		} else {
			new ReportUtil().logFail("Work order should be added to the list",
					"Work order is not added to the list");
		}
	}

	public Map<String, String> captureWorkOrderEditionDetails(String type, List<String> data) {

		Map<String, String> woDetails = new HashMap<String, String>();
		String modalName = "//aw-dialog-work-order-edition";
		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			WebElement ele = common.getElement(By.xpath(modalName + "//label[contains(text(),'" + str + "')]/..//*[contains(@class,'input')]"));
			woDetails.put(str, ele.getText());
			System.out.println("Value of " + str + " is recorded as " + ele.getText());
		}
		if (type.contains("Task")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.taskCode);
			woDetails.put("Task Code", ele.getText());
		} else if (type.contains("Defect")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.eventCode);
			woDetails.put("Event Code", ele.getText());
		}

		return woDetails;
	}

	public void verifyCapturedInformationOnWOScreen(Map<String, String> woDetails) {
		for (Map.Entry<String, String> str : woDetails.entrySet()) {
			if (str.getKey().contains("Atelier") || str.getKey().contains("Workshop")) {
				String[] wsArray = str.getValue().split(" ", 2);
				wsArray[1] = wsArray[1].replaceFirst("-", "");
				WebElement ele = common.getElement(maintenancePlanningPageObj.workcenterOnWOScreen);
				if (ele.getAttribute("value").trim().contains(wsArray[1].trim()))
					new ReportUtil().logPass(str.getKey() + " as " + wsArray[1] + " should be available",
							str.getKey() + " as " + wsArray[1] + " is available");
				else {
					new ReportUtil().logFail(str.getKey() + " as " + wsArray[1] + " should be available", str.getKey()
							+ " as " + wsArray[1] + " is not available-->curent value is " + ele.getAttribute("value"));
				}
			} else
				common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'" + str.getValue() + "')]"),
						str.getKey() + " as " + str.getValue());
		}
	}

	public void naviagteBackToWorkPackagePage(String page) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		common.clickJS(By.xpath("//a[contains(.,'" + page + " " + "')]"), page + " page");
		common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on popup");
		validate_Page_Displayed("Work Package");

	}

	public void verifyProcurementRequestStatus(String status) {
		boolean flag = false;
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		List<WebElement> ele = common.getElements((By.xpath("//b[contains(.,'" + status + "')]")));
		for (WebElement element : ele) {
			if (element.getText().equalsIgnoreCase(status))
				flag = true;
			else
				flag = false;
		}

		if (flag = true) {
			new ReportUtil().logPass("Status of procurement request should be " + status, "Status of procurement request is " + status);
		} else
			new ReportUtil().logFail("Status of procurement request should be " + status, "Status of procurement request is not " + status);
	}

	public void verifyPackageNumberOnSearchResult(String number) {
		common.checkElementPresentByVisibility(By.xpath("//a[contains(.,'" + number + "')]"), "package number " + number);
	}

	public void verifyNoPackageNumberOnSearchResult(String number) {
		common.checkElementNotPresentByVisibility(By.xpath("//a[contains(.,'" + number + "')]"), "package number " + number);
	}

	public void verifySchedulePageForOperator(String operator) {
		try {
			WebElement ele = common.getElement(By.xpath(ObjectUtil.getXpathForPageHeading(operator)));
			common.checkElementPresentByVisibility(ele, "schedule page for staff " + operator);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify schedule page for staff");
		}
	}

	public String DeleteNewlyCreatedWPFromWorkPackage() {
		try {
			List<WebElement> ele = common.getElements(maintenancePlanningPageObj.deleteIconsUnderWP);
			List<WebElement> wpNumbers = common.getElements(maintenancePlanningPageObj.dtListUnderWorkPackage);
			int latestDt = ele.size();
			System.out.println("Newly created WP is " + wpNumbers.get(0).getText());
			String wpNumber = wpNumbers.get(0).getText();
			common.clickJS(ele.get(0), "Delete icon of newly created WP under work package");
			return wpNumber;
		} catch (Exception e) {
			new ReportUtil().logFail("User should be able to click on delete icon of newly created WP",
					"Can not click on delete icon of newly created WP");
			return null;
		}
	}

	public void clickOnPopupBtnIfAppears(String btn) {
		try {
			if (btn.equalsIgnoreCase("yes")) {
				common.explicitWait(2);
				if (common.getElement(maintenancePlanningPageObj.popup_YesBtn).isDisplayed() == true)
					common.clickJS_WithoutJSWait(maintenancePlanningPageObj.popup_YesBtn, "Yes buton");
			} else {
				if (common.getElement(maintenancePlanningPageObj.popup_NoBtn).isDisplayed() == true)
					common.clickJS_WithoutJSWait(maintenancePlanningPageObj.popup_NoBtn, "Yes buton");
			}
		} catch (Exception e) {
			//do nothing
		}
	}


	public int countResultsFromTableTag(String itemName, String tableName) {
		try {
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
			WebElement results = common.getElement(By.xpath("//h3[contains(text(),'" + tableName + "')]"));
			int extractedValue = Integer.parseInt(CompactUtil.extractNumber(results.getText()));
			new ReportUtil().logInfo("Total no of " + itemName + " under table " + tableName + " are " + extractedValue);
			return extractedValue;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate number of results under table " + tableName);
			return 0;
		}
	}

	public void checkNoResultsAreFound(String page) {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.resultsCount);
			String extractedValue = CompactUtil.extractNumber(results.getText().trim());
			if (Integer.parseInt(extractedValue) == 0)
				new ReportUtil().logPass("Search Result should not be available on page " + page, "Search Result are not available");
			else
				new ReportUtil().logFail("Search Result should not be available on page " + page, "Search Result are available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate search results under page " + page);
		}

	}


	public String capturePackageNumber(String index) {

		List<WebElement> wpNumberList = common.getElements(maintenancePlanningPageObj.workPackageNumbers);
		String wpNumber = wpNumberList.get(Integer.parseInt(index) - 1).getText();
		new ReportUtil().logInfo("Work page selected--> " + wpNumber);

		return wpNumber;
	}

	public void clickAddBtnUnderRM() {
		common.clickAnElement(maintenancePlanningPageObj.addBtn_RequestMgnt, "Add button");
	}

	public void enterExpectedDate() {
		new LogisticsModule().enterTodaysDate(maintenancePlanningPageObj.expectedDate_Calendar, "Expected Date", "Today");

	}

	public void verifySelectedItemTextOnSummaryPageSubTitle(String name, String selectedText) {
		try {
			common.getElementExplicitWait(maintenancePlanningPageObj.maintenanceSummaryPageSubTitle);
			WebElement ele = common.getElement(maintenancePlanningPageObj.maintenanceSummaryPageSubTitle);
			String titleText = ele.getText();

			if (titleText.contains(selectedText)) {
				new ReportUtil().logPass("Name of the page should contain the selected " + name + " " + selectedText,
						"Name of the page contain the selected " + name + " " + selectedText);
			} else {
				new ReportUtil().logFail("Name of the page should contain the selected " + name + " " + selectedText,
						"Name of the page does not contain the selected selected " + name + " " + selectedText);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("Name of the page should contain the selected " + name + " " + selectedText,
					"unable to compare page title with selected result");
		}
	}

	public void verifyfieldsPresence(String section, List<String> labels) {
		if (!section.trim().isEmpty())
			new ReportUtil().logInfo("Verifying the presence of fields under " + section);
		for (String label : labels) {
			label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
			By ele = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + label + "')]");
			common.checkElementPresentByVisibility(ele, label + " field");
		}
	}

	public void validate_WPStatusText() {
		try {
			//color code verification for purple is in progress-----
			String wpStatus = common.getElement(maintenancePlanningPageObj.statusOfWP).getText().trim();
			new ReportUtil().logInfo("Status of the work package is visible under purple box as " + wpStatus);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify Work Package status");
		}
	}

	public void verifyItemList(String tableName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		By loc = By.xpath(ObjectUtil.getResultCountFromTableHeader(tableName));
		String countText = common.getText(loc, tableName);
		String count = CompactUtil.extractNumber(countText);
		if (Integer.parseInt(count) == 0) {
			new ReportUtil().logInfo("There are no Results under table " + tableName);
		} else {
			new ReportUtil().logPass("Results under table " + tableName + " should be visible",
					"Results under table " + tableName + " is visible");
		}

	}

	public int getColumnIndexFromTable(String tableName, String columnName) {
		int columnIndex = 1;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//app-goods-receipt-expand-list[@id='goods_receipt_list_results_tbl']/app-list-table/div[2]/div/div/div");
			List<WebElement> ele = common.getElements(loc);
			for (WebElement element : ele) {
				if (element.getText().equalsIgnoreCase(columnName)) {
					System.out.println("Index of column " + columnName + " is " + columnIndex);
					break;
				} else
					columnIndex = columnIndex + 1;

			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve column " + columnName + " Index from table " + tableName);
		}
		return columnIndex;
	}

	public void verifyOTStatusInExecutionData(String columnName, String tableName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		int i = 0;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" +
					columnIndex + "]//span[@class='horizontal-separator']/following-sibling::span");

			List<WebElement> woStatus = common.getElements(loc);
			for (WebElement ele : woStatus) {
				if (ele.getText().isEmpty()) {
					new ReportUtil().logFail("Status should be available under column " + columnName,
							"Status is not available under column " + columnName);
					i++;
				}
			}
			if (i == 0) {
				new ReportUtil().logPass("Status should be available under column " + columnName,
						"Status is available under column " + columnName + " for each work order");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify status under column " + columnName);
		}

	}

	public boolean validateSearchFilter(String columnName, String tableName, String filter) {
		String rawColumn = columnName;
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		filter = prop.readPropertyData(filter.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		int row = 1;
		boolean flag = true;
		try {
			By loc;
			if (rawColumn.equalsIgnoreCase("FL Function")) {
				loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
						+ "')]/../../..//tbody/tr/td[" + columnIndex + "]");
			} else {
				loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
						+ "')]/../../..//tbody/tr/td[" + columnIndex + "]/span");
			}
			List<WebElement> ele = common.getElements(loc);
			if (ele.size() == 0) {
				new ReportUtil().logInfo("There are no search results available with entered criteria");
				return false;
			}
			for (WebElement element : ele) {
				if (!element.getText().trim().equalsIgnoreCase(filter)) {
					new ReportUtil().logFail(
							"Search results should be correct for column " + columnName + " according to filter "
									+ filter,
							"Search results are not correct for column " + columnName + " according to filter " + filter
									+ " -->At row " + row);
					flag = false;
					break;
				}
				row++;
			}
			if (flag == true) {
				new ReportUtil().logPass(
						"Search results should be correct for column " + columnName + " according to filter " + filter,
						"Search results are correct for column " + columnName + " according to filter " + filter);
				return true;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results for filter " + filter);
		}

		return false;
	}

	public void removeAllFilter() {
		common.scrollIntoView(maintenancePlanningPageObj.resetIcon);
		common.clickJS(maintenancePlanningPageObj.resetIcon, "Clicked on reset icon to remove filter");

	}

	public void enterDateSearchFilter(String field) {

		try {
			SimpleDateFormat formatter;
			List<WebElement> ele = common.getElements(maintenancePlanningPageObj.calendarFields);
			if (System.getProperty("appLanguage").equalsIgnoreCase("french")) {
				formatter = new SimpleDateFormat("dd/MM/yyyy");
			} else
				formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();

			if (field.equalsIgnoreCase("start date")) {
				ele.get(0).sendKeys(formatter.format(date).toString());
				new ReportUtil().logInfo("Value entered in field " + field + " as " + formatter.format(date).toString());
			} else {
				ele.get(1).sendKeys(formatter.format(date).toString());
				new ReportUtil().logInfo("Value entered in field " + field + " as " + formatter.format(date).toString());
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to enter date in field " + field);
		}

	}

	public void validateResultsDateData(String tableName, String columnName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		int row = 1;
		boolean flag = true;
		try {
			SimpleDateFormat formatter;
			if (System.getProperty("appLanguage").equalsIgnoreCase("french")) {
				formatter = new SimpleDateFormat("dd/MM/yyyy");
			} else
				formatter = new SimpleDateFormat("MM/dd/yyyy");

			Date date = new Date();
			Date expected = formatter.parse(formatter.format(date).toString());

			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName + "')]/../../..//tbody/tr/td[" + columnIndex + "]/span");
			List<WebElement> ele = common.getElements(loc);
			for (WebElement element : ele) {
				if (element.getText().trim().isEmpty()) {
					//skip as no data is available
				} else {
					Date actual = formatter.parse(element.getText().trim());
					if (!actual.before(expected)) {
						new ReportUtil().logFail("Search results dates in column " + columnName + " should be less than entered date",
								"Search results dates in column " + columnName + " are not less than entered date--->At Row " + row);
						flag = false;
						break;
					}
				}
				row++;
			}

			if (flag == true) {
				new ReportUtil().logPass("Search results dates  should be less than entered date",
						"Search results dates are less than entered date");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results according to entered date");
		}
	}

	public boolean waitTillDataLoaded() {
		/*try {
			WebDriverWait wait = new WebDriverWait(common.getDriver(),180);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(maintenancePlanningPageObj.loadingIcon));
		}
		catch(Exception e) {
			System.out.println("Data not loaded successfully");
		}*/
		if (new JsonUtils().getJsonValue("waitLoad").equalsIgnoreCase("yes")) {
			try {
				common.getElement(maintenancePlanningPageObj.loadingIcon).click();
				for (int i = 1; i < 50; i++) {
					try {
						common.getElement(maintenancePlanningPageObj.loadingIcon).click();
						return true;
					} catch (Exception e) {
						common.explicitWait(1);
					}
				}
			} catch (Exception e) {
				System.out.println("Data has been loaded already");
				return true;
			}
		}
		return false;

	}

	public void verifyAddButtonNotVisible() {
		common.getElementExplicitWaitInVisibility(maintenancePlanningPageObj.addBtn_RequestMgnt, "5", "Add button");
	}

	public void mainInformationPanelNotEditable(List<String> labels) {
		for (String str : labels) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			if (str.equalsIgnoreCase("Asset") || str.equalsIgnoreCase("Individu")) {
				common.checkElementNotEditable(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/../div[contains(@class,'disabled')]"), str);
			} else if (str.equalsIgnoreCase("Project TAT") || str.equalsIgnoreCase("TAT Projet")) {
				//Do nothing
			} else
				common.checkElementNotEditable(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/..//input[@disabled='']"), str);
		}


	}

	public void mainInformationPanelEditable(List<String> labels, String wpStatus) {
		for (String str : labels) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			if (str.equalsIgnoreCase("Asset") || str.equalsIgnoreCase("Individu")) {
				common.checkElementNotEditable(By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'"
						+ str + "')]/..//div[contains(@class,'disabled')]"), str);
			} else if ((wpStatus.equalsIgnoreCase("Released") || wpStatus.equalsIgnoreCase("Performed"))
					&& (str.equalsIgnoreCase("MRO Center") || str.equalsIgnoreCase("Centre MRO"))) {
				common.checkElementNotEditable(By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'"
						+ str + "')]/..//div[contains(@class,'disabled')]"), str);
			} else
				common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/..//input[not(@disabled='')]"), "Editable field " + str);
		}

	}


	public List<String> editMainInformationPanel(List<String> inputBox, List<String> dropdown, String tab, String value) throws Exception {
		List<String> editedValue = new ArrayList<String>();
		new ReportUtil().logInfo("Editing the information under tab " + tab);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		for (String str : inputBox) {
			if (str.contains("Due Date")) {
				new LogisticsModule().enterValueInField(str, formatter.format(date).toString());
			} else {
				new LogisticsModule().enterValueInField(str, value + formatter.format(date).toString());
				editedValue.add(value + formatter.format(date).toString());
			}
		}
		for (String dd : dropdown)
			new Dashboard().selectOptionFromCriteriaDropdown_Index(dd, false);
		return editedValue;
	}

	public void verifyEditedValueInHomeTab(List<String> values) {
		for (String str : values) {
			common.checkElementPresentByVisibility(
					By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(.,'" + str + "')]"),
					"Modified value as " + str);
		}

	}

	public void verifyEditedValueInAITab(List<String> values, String label) {
		for (String str : values) {
			if (label.equalsIgnoreCase("Description") || label.equalsIgnoreCase("Remarks")) {
				String val = common.getElement(By.xpath(
						ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + label + "')]/..//textarea"))
						.getAttribute("value");
				if (values.toString().contains(val)) {
					new ReportUtil().logPass("Modified value in label" + label + " should display as " + str,
							"Modified value in label " + label + " is display as " + str);
				} else
					new ReportUtil().logPass("Modified value in label" + label + " should display as " + str,
							"Modified value in label " + label + " is display as " + str);
			} else
				common.checkElementPresentByVisibility(
						By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(.,'" + str + "')]"),
						"Modified value as " + str);
		}

	}


	public void additionalInformationPanelEditable(List<String> labels) {
		for (String str : labels) {
			if (str.equalsIgnoreCase("Remarks") || str.equalsIgnoreCase("Description")) {
				str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
				common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/..//textarea[not(@disabled='')]"), "Editable field " + str);
			} else if (str.equalsIgnoreCase("Sales Request") || str.equalsIgnoreCase("Purchase Request") || str.equalsIgnoreCase("Customer")) {
				str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
				common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/..//div[not(contains(@class,'disabled'))]"), "Editable field " + str);
			} else {
				str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
				common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer
						+ "//label[contains(.,'" + str + "')]/..//input[not(@disabled='')]"), "Editable field " + str);
			}

		}
	}

	public List<String> editAdditionalInformation(List<String> inputBox, List<String> dropdown, String value) throws Exception {
		List<String> editedValue = new ArrayList<String>();
		new ReportUtil().logInfo("Editing the information under Additonal Information tab");
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		for (String str : inputBox) {
			if (str.equalsIgnoreCase("Remarks") || str.equalsIgnoreCase("Description")) {
				new LogisticsModule().enterValueInTextArea(str, value + formatter.format(date).toString());
				editedValue.add(value + formatter.format(date).toString());
			} else {
				new LogisticsModule().enterValueInField(str, value + formatter.format(date).toString());
				editedValue.add(value + formatter.format(date).toString());
			}
		}
		for (String dd : dropdown)
			new Dashboard().selectOptionFromCriteriaDropdown_Index(dd, true);

		return editedValue;

	}

	public void validateStatusNotChangableForOnGoingWP(String btn1, String btn2) {
		btn1 = prop.readPropertyData(btn1.trim().replaceAll(" ", "_"));
		btn2 = prop.readPropertyData(btn2.trim().replaceAll(" ", "_"));

		common.elementNotAvailableOnPage(By.xpath("//span[contains(text(),'" + btn1 + "')]"), "button " + btn1);
		common.elementNotAvailableOnPage(By.xpath("//span[contains(text(),'" + btn2 + "')]"), "button " + btn2);

	}

	public void verifyPopupNotDisplayed(String pageName) {
		pageName = prop.readPropertyData(pageName.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getXpathForPageHeading(pageName)),
				pageName + " page");

	}


	public List<String> captureNumberFromTable(String table, String columnName) {
		List<String> numberList = new ArrayList<String>();
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		try {
			int columnIndex = getColumnIndexFromTable(table, columnName);
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + table + "')]/../../..//tbody/tr/td[" + columnIndex + "]/a");
			List<WebElement> numbers = common.getElements(loc);
			for (WebElement ele : numbers) {
				numberList.add(ele.getText().trim());
			}
			new ReportUtil().logInfo("WP Numbers captured " + numberList.toString());
			return numberList;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture AP numbers from table " + table);
			return null;
		}
	}


	public String selectWorkOrderWithStatus(String status, int index) {
		try {
			String woName = "";
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			//List<WebElement> woCheckbox = common.getElements(By.xpath(ObjectUtil.xpathToTabContainer + "//span[@class='value'][contains(text(),'"
			//		+ status + "')]/ancestor::tr//*[contains(@class,'checkbox-box')]"));
			List<WebElement> woCheckbox = common.getElements(By.xpath(ObjectUtil.xpathToTabContainer + "//div[@class='yac-table-cell']//span[contains(text(),'" + status + "')]"));
			if (woCheckbox.size() == 0) {
				new ReportUtil().logFail("", "No work order with status " + status + " is available under WP");
			} else {
				common.click(woCheckbox.get(index - 1), "Work order with released checkbox");

				//By wo = By.xpath(ObjectUtil.xpathToTabContainer + "//span[@class='value'][contains(text(),'" + status
				//		+ "')]/preceding::div[contains(@class,'designation')][" + index + "]/../a");
				By wo = By.xpath("(//div[@class='yac-table-cell']//span[contains(text(),'" + status + "')]/../../../preceding-sibling::div//a[contains(text(),'WO')])[" + index + "]");


				List<WebElement> wos = common.getElements(wo);
				woName = common.getText(wos.get(index - 1), "Selected Work Order");
				new ReportUtil().logInfo("Work order selected for transfer is " + woName);
			}
			return woName;

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select work order with status " + status);
			return null;
		}

	}

	public void selectOptionFromCriteriaDropdown_Index(String dropdownName, int index) throws Exception {

		dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
		By optionList = By.xpath("//p-dropdownitem/li/span");
		By chevronDown = By.xpath("(//label[contains(text(),'" + dropdownName + "')]/following::span[contains(@class,'chevron-down')])[1]");
		try {
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			list.get(index).click();
			new ReportUtil().logPass("option should be selected from dropdown " + dropdownName,
					("option is selected from dropdown " + dropdownName));
			common.explicitWait(1);

		} catch (Exception e) {
			new ReportUtil().logFail("option should be selected from dropdown " + dropdownName, "option is not selected from dropdown " + dropdownName);
		}
	}

	public String selectWPFromDropdown(List<String> wpList, String status, String dropdown) throws Exception {
		try {
			dropdown = prop.readPropertyData(dropdown.trim().replaceAll(" ", "_"));
			By chevronDown = By.xpath("(//label[contains(text(),'" + dropdown + "')]/following::span[contains(@class,'chevron-down')])[1]");
			common.clickAnElement(chevronDown, dropdown + " dropdown");
			common.explicitWait(1);
			String wp = "";
			List<WebElement> actualList = common.getElements(maintenancePlanningPageObj.workPackageOption);
			if (actualList.size() < 1) {
				new ReportUtil().logFail("", "No work order with " + status + " status is available under dropdown " + dropdown);
				throw new Exception("No work order with " + status + " status is available under dropdown " + dropdown);
			} else {
				for (WebElement ele : actualList) {
					if (wpList.contains(ele.getText().trim())) {
						wp = ele.getText().trim();
						common.clickJS(ele, status + " Work Package");
						common.explicitWait(1);
						break;
					}
				}
				return wp;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select option from " + dropdown);
			Assert.assertTrue(false);
			return null;
		}
	}

	public void validateTransferredWODisplayed(String woNumber) {
		common.checkElementPresentByVisibility(By.xpath("//a[contains(text(),'" + woNumber + "')]"), "Work Order " + woNumber);
	}

	public void verifyWorkOrderTransferPopup(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath("//h3[contains(text(),'" + name + "')]"), "Popup" + name);

	}

	public void filterMenuDisplayed(String name) {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.filterTable, name);
	}

	public void verifyResultsUpdated(String assetName, String expectedControlType, String expectedType, String table,
									 int typeColumn) {
		try {
			boolean flag = true;
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

			List<WebElement> controlList = common
					.getElements(maintenancePlanningPageObj.controlTypeListUnderMaintenanceTable);
			if (controlList.size() != 0) {
				common.checkElementPresentByVisibility(
						By.xpath(ObjectUtil.xpathToTabContainer + "//a[contains(text(),'" + assetName + "')]"),
						"asset " + assetName);
				controlList.remove(0);
			}
			for (WebElement ele : controlList) {
				if (!common.getText(ele, "Control Type").contains(expectedControlType)) {
					new ReportUtil().logFail("Control Type should be " + expectedControlType,
							"Control Type is not " + expectedControlType);
					flag = false;
					break;
				}
			}
			if (flag == true) {
				new ReportUtil().logPass("Control Type should be " + expectedControlType,
						"Control Type is " + expectedControlType + " for all the filtered task");
			}

			List<WebElement> typeList = common.getElements(maintenancePlanningPageObj.typeUnderGroup);
			for (WebElement ele : typeList) {
				if (!common.getText(ele, "Type").contains(expectedType)) {
					new ReportUtil().logFail("Type should be " + expectedType, "Type is not " + expectedType);
					flag = false;
					break;
				}
			}
			if (flag == true) {
				new ReportUtil().logPass("Type should be " + expectedType,
						"Type is " + expectedType + " for all the filtered task");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate filtered results");
		}

	}


	public String selectMaterialAlternative() {
		String number = "";
		try {
			if (common.getElement(maintenancePlanningPageObj.materialAlternative_radioBtn).isDisplayed()) {
				common.clickJS_NoJSWaiter(maintenancePlanningPageObj.materialAlternative_radioBtn, "Alternative radio button");
				number = common.getText(maintenancePlanningPageObj.materialAlternativeName, "Alternative Material Number");
				clickButtonOnPopUP("search pn alternatives", "Validate");
			}
			return number;
		} catch (Exception e) {
			new LogisticsModule().clickOnBtn("Cancel");
			return "";
		}
	}

	public void verifyAlternativeMaterialOnField(String fieldName, String materialNumber) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		if (materialNumber.length() > 1)
			common.checkElementPresentByVisibility(By.xpath("//label[contains(.,'" + fieldName
					+ "')]/../div/div[contains(.,'" + materialNumber + "')]"), "Material number " + materialNumber);
	}

	public void verifyMaterialListIsDisplayedOrErrorMessage(String msg) {
		msg = prop.readPropertyData(msg.trim().replaceAll(" ", "_"));
		if (common.getElement(By.xpath("//*[contains(.,'" + msg + "')]")).isDisplayed()) {
			new ReportUtil().logPass("confirmation message " + msg + " should be present",
					"confirmation message " + msg + " is present");
		} else {
			int count = common.getElements(logisticModuleObj.stockViewList).size();
			if (count > 0) {
				new ReportUtil().logPass("Material list should display",
						"Material list is present- total rows are " + count);
			} else {
				new ReportUtil().logFail("Material list should display", "Material list is not present");
			}
		}
	}

	public void checkResultsPresentUnderPopup() {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.resultTableOverPopup);
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (extractedValue != null && extractedValue != "0")
				new ReportUtil().logInfo("Search Result are available");
			else
				new ReportUtil().logFail("Search Results should display", "No Search Results available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void checkMyWorkOrdersCount(String name) {
		try {
			List<WebElement> results = common.getElements(maintenancePlanningPageObj.myWOItems);
			if (results.size() > 0)
				new ReportUtil().logInfo(name + " are available");
			else
				new ReportUtil().logFail(name + " should display", "No " + name + " are available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void verifyFiltersPresence(List<String> labels) {
		for (String label : labels) {
			label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
			if (label.contains("Next") || label.contains("Prochains")) {
				String[] arrOfStr = label.split(" ", 3);
				By ele = By.xpath("//label[contains(text(),'" + arrOfStr[0] + "')]/..//span[contains(.,'" + arrOfStr[1] + "')]");
				common.checkElementPresentByVisibility(ele, label + " field");

			} else {
				By ele = By.xpath(ObjectUtil.getLabelXpath(label));
				common.checkElementPresentByVisibility(ele, label + " field");
			}
		}
	}


	public void validateMyWorkOrderFilter(List<String> filter) {
		List<WebElement> results = common.getElements(maintenancePlanningPageObj.myWOItems);
		if (results.size() > 0)
			new ReportUtil().logInfo("No work orders are available for these filters");
		else {
			for (String str : filter) {
				str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
				int count = common
						.getElements(By.xpath(ObjectUtil.xpathToTabContainer + "//span[contains(.,'" + str + "')]"))
						.size();
				if (count != results.size())
					new ReportUtil().logFail("Work orders should filter", "Work orders are not updated properly");
				else
					new ReportUtil().logPass("Work orders should filter", "Work orders are updated properly");
			}
		}
	}


	public void verifyMandatoryFieldsArePresent(List<String> fields) {
		for (String str : fields) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			By ele = By.xpath(ObjectUtil.getLabelXpath(str) + "/parent::div[contains(@class,'required')]");
			common.checkElementPresentByVisibility(ele, str + " mandatory field");
		}
	}


	public void verifyDropdownArePresent(List<String> fields) {
		for (String str : fields) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			By ele = By.xpath(ObjectUtil.getLabelXpath(str) + "/..//p-dropdown");
			common.checkElementPresentByVisibility(ele, str + "  dropdown");
		}
	}

	public void verifyfieldsArePresent(List<String> fields) {
		for (String str : fields) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			By ele = By.xpath(ObjectUtil.getLabelXpath(str));
			common.checkElementPresentByVisibility(ele, str + " field");
		}
	}

	public void validateEarlierDatesAreGrayedOut(String field, String popupName) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");

		WebElement btn = common.getElement(By.xpath("//aw-dialog-" + popupLowerCase + ObjectUtil.getInputboxXpath(field)));
		common.clickAnElement(btn, field);
		List<WebElement> greyDate = common.getElements(maintenancePlanningPageObj.greyOutDates);
		if (greyDate.size() > 0) {
			new ReportUtil().logPass("Dates before start date should be greyed out", "Dates before start date are greyed out");
		} else {
			new ReportUtil().logFail("Dates before start date should be greyed out", "Dates before start date are not greyed out");
		}

	}

	public void clickAeroWebLogo() {
		common.clickAnElement(maintenancePlanningPageObj.aeroWebLogo, "Aero Web Logo");
	}

	public void verifyMaintenanceForeCastIsVisibleOnHomePage() {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.forecastName, "Maintenance forecast on favorite page");

	}

	public void deleteAllMaintenanceForecast() {
		List<WebElement> forecastDelete = common.getElements(maintenancePlanningPageObj.forecastDeleteBtn);
		for (WebElement ele : forecastDelete) {
			common.clickJS(ele, "Maintenance forecast delete Icon");
		}

	}

	public void clickMenuItemBtn(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		common.clickJS_WithoutJSWait(By.xpath("//button[contains(text(),'" + field + "')]"), field);
	}

	public void selectStartDateFromForecastPopup(String popupName, String field, String today) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
		WebElement btn = common.getElement(By.xpath("//aw-dialog-" + popupLowerCase + ObjectUtil.getInputboxXpath(field)));
		common.clickAnElement(btn, field);

		today = prop.readPropertyData(today.trim().replaceAll(" ", "_"));
		if (popupName.contains("forecast form work package")) {
			List<WebElement> ele = common.getElements(By.xpath(ObjectUtil.getButtonXpath(today)));
			common.clickJS(ele.get(1), today);
		} else
			common.clickJS(By.xpath(ObjectUtil.getButtonXpath(today)), today);
	}

	public void selectOptionFromFleetData(String label, String option) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath("//h4[contains(.,'" + label + "')]/..//span[contains(.,'" + option + "')]"), option);
	}

	public void deleteExistingFleetCode(String fieldName, String btn, String popup, String index, String forecastName) {
		selectMainCriteria("Fleet");
		clickOnSearchIconOfField(fieldName);
		clickButtonOnPopUP(popup, "Search");
		AddItemToTheList(popup);
		clickButtonOnPopUP(popup, "Validate");
		new LogisticsModule().enterValueInField("Name", forecastName);
		new Dashboard().performSearch();
		if (common.getElements(maintenancePlanningPageObj.ResultsTable_Items).size() > 1) {
			WebElement menu = common.getElement(maintenancePlanningPageObj.tableMenu);
			common.clickAnElement(menu, "table menu");
			WebElement selectAllOption = common.getElement(maintenancePlanningPageObj.selectAllOpt);
			common.clickAnElement(selectAllOption, "select all");
			new LogisticsModule().clickOnBtn(btn);
			new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
		}
		selectMainCriteria("All");
	}

	public void validateEventFieldsArePrefilled(List<String> fields) {
		try {
			String eventForm = "//aw-create-event-form";
			for (String str : fields) {
				By loc;
				String value;
				str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
				if (str.equalsIgnoreCase("category")) {
					loc = By.xpath(eventForm + "//label[contains(text(),'" + str + "')]/..//*[contains(@class,'inputtext')]");
					value = common.getText(loc, str);
				} else {
					loc = By.xpath(eventForm + "//label[contains(text(),'" + str + "')]/../div//input");
					value = common.getAttributeByValue(loc, str);
				}
				if (value.trim().length() > 1)
					new ReportUtil().logPass(str + " field should be prefilled", str + " field is prefilled");
				else
					new ReportUtil().logFail(str + " field should be prefilled", str + " field is not prefilled");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate prefilled fields");
		}
	}


	public void validatePEAInCMPColumn(int columnIndex, String asset, String resultName) {
		try {
			By loc = By.xpath("//tr/td[contains(.,'" + asset + "')]/../td[" + columnIndex + "]");
			String cmp = common.getText(loc, "Current Maintenance Program");
			if (cmp.contains(resultName)) {
				new ReportUtil().logPass(
						"Name of the current maintenace program for an asset " + asset + " should be " + resultName,
						"Name of the current maintenace program for an asset " + asset + " is " + resultName);
			} else {
				new ReportUtil().logFail(
						"Name of the current maintenace program for an asset " + asset + " should be " + resultName,
						"Name of the current maintenace program for an asset " + asset + " is not " + resultName);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "unable to verify the CMP name for an asset " + asset);
		}
	}

	public void verifyStatusColor(String status, String color) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		By loc = maintenancePlanningPageObj.statusOfWP;
		if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("Firefox"))
			common.checkBackgroundColor(loc, status, color);
		else if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("edge"))
			common.checkBackgroundColorForEdge(loc, status, color);
	}

	public void verifybubbleColor(String status, String color) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		common.scrollUp();
		By loc = maintenancePlanningPageObj.bubble;
		common.clickAnElement(loc, "Bubble icon");
		if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("Firefox"))
			checkbubbleBackgroundColor(loc, status, color);
		else if (new JsonUtils().getJsonValue("browser").equalsIgnoreCase("edge"))
			common.checkBackgroundColorForEdge(loc, status, color);
	}

	public void checkbubbleBackgroundColor(By loc, String locName, String colorName) {
		try {
			WebElement ele = DriverUtil.getInstance().getDriver().findElement(loc);
			String bgColor = ele.getCssValue("box-shadow");
			System.out.println("button color is" + ele.getCssValue("box-shadow"));
			bgColor = bgColor.substring(bgColor.indexOf("r"), bgColor.indexOf(")") + 1);
			System.out.println(bgColor);
			String[] numbers = bgColor.replace("rgb(", "").replace(")", "").split(",");
			int r = Integer.parseInt(numbers[0].trim());
			int g = Integer.parseInt(numbers[1].trim());
			int b = Integer.parseInt(numbers[2].trim());
			System.out.println("r: " + r + "g: " + g + "b: " + b);
			String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
			System.out.println("Hex of of a color code is " + hex);

			if (colorName.equalsIgnoreCase("purple")) {
				if (hex.equalsIgnoreCase("#6d326d") || hex.equalsIgnoreCase("#c80ff"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			} else if (colorName.equalsIgnoreCase("blue")) {
				if (hex.equalsIgnoreCase("#116fbf") || hex.equalsIgnoreCase("#07ad9") || hex.equalsIgnoreCase("#1579b") || hex.equalsIgnoreCase("#2196F3") || hex.equalsIgnoreCase("#2598f3")
						|| hex.equalsIgnoreCase("#2297f3"))
					new ReportUtil().logPass(locName + " should highlighted in " + colorName + " color",
							locName + " is highlighted in " + colorName + " color");
				else
					new ReportUtil().logFail(locName + " should highlighted in " + colorName + " color",
							locName + " is not highlighted in " + colorName + " color");
			} else if (colorName.equalsIgnoreCase("orange")) {
				if (hex.equalsIgnoreCase("#ff9d0"))
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

	public void waitTillOperationsAreUpdated(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(name)), 2);
	}

	public void clickOnPrint() {
		common.getElementExplicitWait(maintenancePlanningPageObj.printBtn, 2);
		common.clickJS(maintenancePlanningPageObj.printBtn, "Print" + " button");
		common.explicitWait(5);
	}

	public String verifyFileDownloaded(String fileType, String description) {
		String fileName = "";
		try {
			String basePath, actualFilePath;
			basePath = ReportUtil.getExtentReportFolder() + File.separator + "files" + File.separator;
			actualFilePath = basePath + "downloded" + File.separator;
			File dir = new File(actualFilePath);
			File[] listOfFiles = dir.listFiles();
			fileName = listOfFiles[0].getName();
			System.out.println("file is " + listOfFiles[0].getName());
			if (listOfFiles.length > 0)
				new ReportUtil().logPass(fileType + " File of " + description + " Should be downloaded",
						fileType + " File of " + description + " is Downloaded");
			else
				new ReportUtil().logFail(fileType + " File of " + description + " Should be downloaded",
						fileType + " File of " + description + " cannot be Downloaded");

			return fileName;

		} catch (Exception e) {
			new ReportUtil().logFail(fileType + " File of " + description + " Should be downloaded",
					"Unable to verify " + fileType + " File download validation");
			return fileName;
		}

	}

	public Map<String, List<String>> captureMaintenanceOperationInfo() {
		List<String> idList = new ArrayList<String>();
		Map<String, List<String>> operationsId = new LinkedHashMap<>();

		List<WebElement> operationsRow = common.getElements(maintenancePlanningPageObj.listOfIdNothavingArrow);
		for (WebElement ele : operationsRow) {
			idList.add(ele.getText().trim());
		}
		operationsId.put(prop.readPropertyData("Upcoming_Event"), idList);
		return operationsId;

	}

	public void validateExcelSheetDataWithMaintenanceScreen(Map<String, List<String>> operationIds) {
		String basePath, actualFilePath;
		basePath = ReportUtil.getExtentReportFolder() + File.separator + "files" + File.separator;
		actualFilePath = basePath + "downloded" + File.separator;
		File folder = new File(actualFilePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				Workbook workbook;
				try {
					workbook = WorkbookFactory.create(new FileInputStream(actualFilePath + "\\" + listOfFiles[i].getName()));
					Sheet sheet = workbook.getSheetAt(0);

					String key = null;
					List<String> values = null;
					for (Entry<String, List<String>> ee : operationIds.entrySet()) {
						key = ee.getKey();
						values = ee.getValue();
					}
					String columnWanted = key;
					Integer columnNo = null;

					Row row = sheet.getRow(0);
					for (Cell cell : row) {
						if (cell.getStringCellValue().equals(columnWanted)) {
							columnNo = cell.getColumnIndex();
						}
					}
					int error = 0;
					if (columnNo != null) {
						List<String> excelData = new ArrayList<>();
						for (Row row1 : sheet) {
							Cell c = row1.getCell(columnNo);
							if (c.getStringCellValue().equalsIgnoreCase(key)) {
								// skip header row
							} else
								excelData.add(c.getStringCellValue());
						}
						for (String str : values) {
							if (excelData.contains(str))
								new ReportUtil().logInfo("Maintenance info validated succesfully for " + str);
							else {
								new ReportUtil().logFail("", "Maintenance info validation error for " + str);
								error++;
							}
						}
						if (error == 0)
							new ReportUtil().logPass(
									"Information in excel should be consistent with maintenance schedule screen",
									"Information in excel is consistent with maintenance schedule screen");

					} else
						System.out.println("could not find column " + columnWanted + " in first row of "
								+ listOfFiles[i].getName());

				} catch (Exception e) {
					new ReportUtil().logFail("", "Unable to validate excel sheet data with maintenance screen");
				}
			}
		}

	}


	public void validateNumericValueAndUnits() {
		String basePath, actualFilePath, value = null, unit = null;
		value = prop.readPropertyData("value");
		unit = prop.readPropertyData("unit");
		basePath = ReportUtil.getExtentReportFolder() + File.separator + "files" + File.separator;
		actualFilePath = basePath + "downloded" + File.separator;
		File folder = new File(actualFilePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				Workbook workbook;
				try {
					workbook = WorkbookFactory
							.create(new FileInputStream(actualFilePath + "\\" + listOfFiles[i].getName()));
					Sheet sheet = workbook.getSheetAt(0);
					List<Integer> numericColsList = new ArrayList<>();
					List<Integer> unitColsList = new ArrayList<>();

					Row row = sheet.getRow(0);
					for (Cell cell : row) {
						if (cell.getStringCellValue().contains(value)) {
							numericColsList.add(cell.getColumnIndex());
						} else if (cell.getStringCellValue().contains(unit)) {
							unitColsList.add(cell.getColumnIndex());
						}
					}

					System.out.println("Numeric columns are " + numericColsList.toString());
					System.out.println("Unit columns are " + unitColsList.toString());

					if (numericColsList.size() == unitColsList.size()) {
						int unitflag = 0;
						if (numericColsList != null || unitColsList != null) {

							for (int column : unitColsList) {
								for (Row row1 : sheet) {
									Cell c = row1.getCell(column);
									try {
										if (!c.getStringCellValue().matches("[a-zA-Z\\s\'\"]+"))
											unitflag++;
									} catch (NullPointerException ne) {

									}
								}
							}

							if (unitflag > 0) {
								new ReportUtil().logFail("Numeric values and units should be in seperate columns",
										"Numeric values and units are not in seperate columns");
							} else
								new ReportUtil().logPass("Numeric values and units should be in seperate columns",
										"Numeric values and units are in seperate columns");
						}

					}
				} catch (Exception e) {
					new ReportUtil().logFail("", "Unable to validate numeric and unit values in excel sheet");
				}
			}
		}
	}

	public void clickEditCommentBtn() {
		common.clickAnElement(maintenancePlanningPageObj.editCommentBtn, "Edit button on comment box");
	}

	public void clickEditAdditionalWorkBtn() {
		common.clickAnElement(maintenancePlanningPageObj.editAdditionalWorkBtn, "Edit button on Additional Work box");
	}

	public void enteredTextDisplayed(String text, String box) {
		By loc;
		if (box.equalsIgnoreCase("comment"))
			loc = maintenancePlanningPageObj.commentTextarea;
		else
			loc = maintenancePlanningPageObj.additionalWorkTextarea;

		if (common.getAttributeByValue(loc, box + " box").equalsIgnoreCase(text))
			new ReportUtil().logPass("Entered text " + text + " should display", "Entered text " + text + " displayed");
		else
			new ReportUtil().logFail("Entered text " + text + " should display",
					"Entered text " + text + " is not displayed");

	}

	public void enterTextUnderCommentBox(String value) {
		common.performOperation(maintenancePlanningPageObj.commentTextarea, "input", value, "Comment textarea");
	}

	public void enterTextUnderCommentBoxOfFlightPopup(String value) {
		common.performOperation(maintenancePlanningPageObj.missionComment, "input", value, " Mission Comment textarea");
	}

	public void enterTextUnderAdditionalWorkBox(String value) {
		common.performOperation(maintenancePlanningPageObj.additionalWorkTextarea, "input", value, "additional work textarea");
	}

	public void clickWoUnderMyWorkOrderPage() throws Exception {
		List<WebElement> wos = common.getElements(maintenancePlanningPageObj.woCodeUnderMyWorkOrdersPage);
		int count = wos.size();
		if (count == 0) {
			new ReportUtil().logFail("", "No work order are available to proceed");
			throw new Exception("No work order are available to proceed");
		} else {
			common.clickJS(wos.get(0), "Work Order " + common.getText(wos.get(0), "Work order"));
			common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on confirmation popup");
		}

	}

	public void potentialUnitDisplayed(List<String> data) {
		for (String btnName : data) {
			btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getButtonXpathTagBtn(btnName)), btnName);
		}
	}

	public void potentialColumnDisplayedInUnit(String unit) {
		try {
			//waitTillDataLoaded();
			List<WebElement> equivPotList = common.getElements(maintenancePlanningPageObj.equivalentPotList);
			boolean flag = true;
//			for (WebElement ele : equivPotList) {
//				idNo++;
//				if (ele.getText().trim().isEmpty()) {
//				} else if (!ele.getText().contains(unit)) {
//					String idName = common.getText(id.get(idNo - 1), "ID");
//					new ReportUtil().logFail("Equivalent Potential unit should display in " + unit,
//							"Equivalent Potential unit is not displayed in " + unit + " for id " + idName);
//					flag = false;
//				}
//			}
			System.out.println(equivPotList.get(0));
			if (equivPotList.get(0).getText().contains(unit)) {
				flag = true;
			}

			if (flag == true)
				new ReportUtil().logPass("Equivalent Potential unit should display in " + unit,
						"Equivalent Potential unit is displayed in " + unit);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify default potential unit");
		}

	}

	public void fieldSNAndPNCannotBeModified(String sn, String pn, String tableName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		int snIndex = getColumnIndexFromTable(tableName, sn);
		int pnIndex = getColumnIndexFromTable(tableName, pn);

		String pnText = common.getText(By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + pnIndex + "]"), pn);
		String snText = common.getText(By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + snIndex + "]"), sn);

		if ((pnText + snText).isEmpty())
			new ReportUtil().logPass("SN and PN field should not be editable", "SN and PN field is not editable");
		else
			new ReportUtil().logFail("SN and PN field should not be editable", "SN and PN field is editable");


	}

	public void fieldSNAndPNCanBeModified(String sn, String pn, String tableName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		int snIndex = getColumnIndexFromTable(tableName, sn);
		int pnIndex = getColumnIndexFromTable(tableName, pn);

		String pnText = common.getText(By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + pnIndex + "]"), pn);
		String snText = common.getText(By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + snIndex + "]"), sn);

		if ((pnText + snText).isEmpty())
			new ReportUtil().logFail("SN and PN field should be editable", "SN and PN field is not editable");
		else
			new ReportUtil().logPass("SN and PN field should be editable", "SN and PN field is editable");

	}

	public void selectOptionFromTableDD(String dropdownName, String option, String table) throws Exception {
		System.out.println("Install");
		boolean found = false;
		dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
		option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

		int dropdownColumnIndex = getColumnIndexFromTable(table, dropdownName);
		By optionList = By.xpath("//p-dropdownitem/li/span");
		By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + dropdownColumnIndex + "]//span[contains(@class,'chevron-down')]");

		WebElement element = common.getElementExplicitWait(chevronDown, 0);
		common.clickJS(element, dropdownName);
		new ReportUtil().logInfo("Clicked on dropdown " + dropdownName);
		common.explicitWait(2);
		List<WebElement> list = common.getElements(optionList);
		for (WebElement ele : list) {
			String text = ele.getText();
			if (text.equalsIgnoreCase(option)) {
				common.clickJS(ele, text);
				new ReportUtil().logPass(option + " option should be selected from dropdown " + dropdownName,
						(option + " option is selected from dropdown " + dropdownName));
				common.explicitWait(1.5);
				found = true;
				break;
			}
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName, option + " option is not selected from dropdown " + dropdownName);
			//	throw new Exception("Cannot Found option " + option);
		}
	}

	public void selectOptionFromTableDD(String dropdownName, int index, String table) {
		boolean found = false;
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

			int dropdownColumnIndex = getColumnIndexFromTable(table, dropdownName);
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + dropdownColumnIndex
					+ "]//span[contains(@class,'chevron-down')]");

			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			common.clickJS(element, dropdownName);
			common.explicitWait(2);
			List<WebElement> list = common.getElements(optionList);
			common.clickJS(list.get(index), "option");
			found = true;
		} catch (Exception e) {
			new ReportUtil().logFail(" option should be selected from dropdown " + dropdownName,
					" option is not selected from dropdown " + dropdownName);
		}
		if (!found) {
			new ReportUtil().logFail(" option should be selected from dropdown " + dropdownName,
					" option is not selected from dropdown " + dropdownName);
		}
	}

	public void clickAddBtnToAddDeposit() {
		common.clickAnElement(maintenancePlanningPageObj.addDepositBtn, "Add Button");
	}

	public void clickUndoSelectedDecision(String btn) {
		btn = prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath("//span[contains(text(),'" + btn + "')]"), btn + " Button");
	}

	public Map<String, String> captureWODuration(String totalDuration, String totalElapsed) {
		Map<String, String> duration = new HashMap<>();
		totalDuration = prop.readPropertyData(totalDuration.trim().replaceAll(" ", "_"));
		totalElapsed = prop.readPropertyData(totalElapsed.trim().replaceAll(" ", "_"));

		By locDuration = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + totalDuration + "')]/../div/div");
		By locElapsed = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + totalElapsed + "')]/../div/div");

		duration.put("Total duration", common.getText(locDuration, totalDuration));
		duration.put("Total elapsed", common.getText(locElapsed, totalElapsed));

		return duration;
	}

	public void validateWOUpdated(String totalDuration, String totalElapsed, Map<String, String> mp) {
		try {
			totalDuration = prop.readPropertyData(totalDuration.trim().replaceAll(" ", "_"));
			totalElapsed = prop.readPropertyData(totalElapsed.trim().replaceAll(" ", "_"));

			By locDuration = By.xpath(
					ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + totalDuration + "')]/../div/div");
			By locElapsed = By.xpath(
					ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + totalElapsed + "')]/../div/div");

			if (common.getText(locDuration, totalDuration).equalsIgnoreCase(mp.get("Total duration"))
					&& common.getText(locElapsed, totalElapsed).equalsIgnoreCase(mp.get("Total elapsed"))) {
				new ReportUtil().logFail("Total Duration and Total elapsed time should be updated",
						"Total Duration and Total elapsed time is not updated");
			} else
				new ReportUtil().logPass("Total Duration and Total elapsed time should be updated",
						"Total Duration and Total elapsed time is updated");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify tottal duration and total elapsed time in Work Order");
		}

	}

	public void clickPlayAndStopBtn(String btn) {
		try {
			List<WebElement> btns;
			if (btn.equalsIgnoreCase("play"))
				btns = common.getElements(maintenancePlanningPageObj.playBtn);
			else
				btns = common.getElements(maintenancePlanningPageObj.stopBtn);

			for (WebElement ele : btns)
				common.clickJS(ele, btn + " button");

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on button " + btn);
		}
	}

	public void stopBtnVisible() {
		if (common.getElements(maintenancePlanningPageObj.stopBtn).size() > 0) {
			new ReportUtil().logInfo("Stop button visible", false);
		} else {
			new ReportUtil().logFail("Stop button should be visible", "Stop button is not visible");
		}
	}

	public void clickUnLock() {
		List<WebElement> btns = common.getElements(maintenancePlanningPageObj.unlockBtn);
		for (WebElement ele : btns)
			common.clickJS(ele, "Unlock button");
	}

	public void clickUnLockInter() {
		List<WebElement> btns = common.getElements(maintenancePlanningPageObj.unlockBtnInt);
		for (WebElement ele : btns)
			common.clickJS(ele, "Unlock button");
	}

	public void clickLock() {
		List<WebElement> btns = common.getElements(maintenancePlanningPageObj.lockBtn);
		if (btns.size() > 1)
			common.clickAnElement(btns.get(1), "Lock");
		else
			common.clickAnElement(btns.get(0), "Lock");
	}

	public void verifyProgressBarForAllItems(String percentage, String item) {
		try {
			List<WebElement> btns = common.getElements(logisticModuleObj.progressBarUnderPackage);
			for (WebElement ele : btns) {
				if (common.getText(ele, "Task Progress").contains(percentage))
					new ReportUtil().logPass("Status of " + item + " should be " + percentage,
							"Status of " + item + " is displayed as " + percentage);
				else
					new ReportUtil().logFail("Status of " + item + " should be " + percentage,
							"Status of " + item + " is not displayed as " + percentage);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to " + item + " progress");
		}
	}

	public void verifyProgressStatus(String percentage) {
		try {
			By progress = By.xpath("//div[contains(text(),'" + percentage + "')]");
			common.checkElementPresentByVisibility(progress, "percentage " + percentage);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate " + percentage + " progress");
		}
	}

	public void validate_Status(String status, String type) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		By loc = By.xpath(ObjectUtil.statusXpath(status));
		common.getElementExplicitWait(loc, 0);
		String wpStatus = common.getElement(maintenancePlanningPageObj.statusOfWP).getText().trim();
		if (wpStatus.equalsIgnoreCase(status)) {
			new ReportUtil().logPass(type + " status should be " + status, type + " status is " + status);
		} else {
			new ReportUtil().logFail(type + " status should be " + status, type + " status is not " + status);
		}
	}

	public void verifyTimeInPerfAndElapsedDuration(String perfColumn, String elapsedColumn, String table) {
		try {
			perfColumn = prop.readPropertyData(perfColumn.trim().replaceAll(" ", "_"));
			elapsedColumn = prop.readPropertyData(elapsedColumn.trim().replaceAll(" ", "_"));
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

			int perfIndex = getColumnIndexFromTable(table, perfColumn);
			int elapsedIndex = getColumnIndexFromTable(table, elapsedColumn);

			String textPerf = common.getText(
					By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + perfIndex + "]/span/span"), "Perf Duration");
			String textElapsed = common.getText(
					By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + elapsedIndex + "]/span/span"),
					"Elapsed Duration");

			if (textPerf.isEmpty() || textElapsed.isEmpty()) {
				new ReportUtil().logFail("time should mention in " + perfColumn + " and " + elapsedColumn + " column",
						"time is not mentioned in " + perfColumn + " and " + elapsedColumn + " column");
			} else {
				new ReportUtil().logPass("time should mention in " + perfColumn + " and " + elapsedColumn + " column",
						"time is mentioned in " + perfColumn + " and " + elapsedColumn + " column");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("",
					"Unable to validate time in " + perfColumn + " and " + elapsedColumn + " column");
		}
	}

	public void clickOnAdvancedCriteria() {
		common.clickAnElement(maintenancePlanningPageObj.advancedCriteria, "advanced Criteria");
	}

	public void openWO(String woName) {
		common.clickJS(
				By.xpath(ObjectUtil.xpathToTabContainer + "//a[@class='value'][contains(text(),'" + woName + "')]"),
				"Work order " + woName);
	}


	public void selectWPFromAssetReviewPage(String wpName, String page) {

		common.scrollIntoView(By.xpath(
				ObjectUtil.xpathToTabContainer + "//a[contains(text(),'" + wpName + "')]/../../..//*[contains(@class,'checkbox-box')]"));

		common.clickJS(By.xpath(
				ObjectUtil.xpathToTabContainer + "//a[contains(text(),'" + wpName + "')]/../../..//*[contains(@class,'checkbox-box')]"),
				"Work page " + wpName + " from page " + page);
	}

	public void verifywpAssetReview(String wpNum, String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			WebElement ele1 = common.getElement(By.xpath("//a[contains(text(),'" + wpNum + "')]/../../..//*[contains(@class,'checkbox-box')]/ancestor::td//following-sibling::td[1]//span"));
			WebElement ele2 = common.getElement(By.xpath("//a[contains(text(),'" + wpNum + "')]/../../..//*[contains(@class,'checkbox-box')]/ancestor::td//following-sibling::td[2]//span"));
			WebElement ele3 = common.getElement(By.xpath("//a[contains(text(),'" + wpNum + "')]/../../..//*[contains(@class,'checkbox-box')]/ancestor::td//following-sibling::td[3]//span"));
			String acrs = ele1.getText().trim();
			String logbook = ele2.getText().trim();
			String appliedConfig = ele3.getText().trim();
			Assert.assertTrue(acrs.equalsIgnoreCase(status));
			Assert.assertTrue(logbook.equalsIgnoreCase(status));
			Assert.assertTrue(appliedConfig.equalsIgnoreCase(status));
			new ReportUtil().logPass("verify ACRS logbook and Applied configuration status as " + status, "verified ACRS logbook and Applied configuration status as " + status);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate ACRS Logbook and Applied configuration as " + status);
		}

	}

	public void clickonHeaderCheckbox(String page) {
		common.clickAnElement(maintenancePlanningPageObj.taskHeaderCheckbox, "Task on page " + page);
	}

	public void taskWithOKControlType() {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.okControlTasks, "Task with control type OK");
	}

	public void selectOptionForFilter(String btnName) {
		try {
			btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
			WebElement ele = common.getElement(By.xpath(ObjectUtil.getButtonXpath(btnName) + "/.."));
			if (!ele.getAttribute("class").contains("highlight"))
				common.clickJS(ele, btnName);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on " + btnName);
		}
	}


	public void openWOHavingStatus(String status) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath("//span[contains(text(),'" + status
				+ "')]/../../../..//div[contains(@class,'wo-code')]/a"), "Work Order having status " + status);
	}

	public void openWOHavingType(String type) {
		type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath("//span[contains(.,'" +
				type + "')]/../../..//div[contains(@class,'wo-code')]/a"), "Work Order having type " + type);
	}

	public void NavigateBackToMaintenacePlanning(String page, String type) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath("//aw-breadcrumb//a[contains(.,'" + page + "  - " + type + "')]"), "breadcrumb Maintenance Planning");
	}

	public void verifyNewDateForTask(String wpNumber, String oldDate) {
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[contains(text(),'" + wpNumber
					+ "')]/../../preceding-sibling::td[1]/span");
			String text = common.getText(loc, "Next term date for WP " + wpNumber);
			if (text != oldDate) {
				new ReportUtil().logPass("New next term date should be available for task having WP " + wpNumber,
						"New next term date is available for task having WP " + wpNumber);
			} else
				new ReportUtil().logFail("New next term date should be available for task having WP " + wpNumber,
						"New next term date is not available for task having WP " + wpNumber);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validae new next term date");
		}
	}

	public int selectTaskWithNearestDeadLine() {
		//waitTillDataLoaded();
		int j = 0;
		List<WebElement> checkboxes = common.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available",
					"No operations are available. please change the test data");
			Assert.assertTrue("No operation available", false);
		}

		List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
		List<WebElement> potentialRemianingList = common.getElements(maintenancePlanningPageObj.potentialRemaining);
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);

		try {
			for (WebElement ele : potentialRemianingList) {
				String temp = ele.getText().trim();
				if ((!temp.contains("|")) && (temp.contains("H") || temp.contains("D") || temp.contains("M"))
						&& (!temp.contains("-")) && nextWPList.get(j).getText().trim().isEmpty()) {
					System.out.println("Task found deadline range is " + temp);
					common.scrollIntoView(checkboxes.get(j));
					common.click(checkboxes.get(j), "task " + IdList.get(j).getText() + " whose deadline is coming soon");
					System.out.println("Task added " + IdList.get(j).getText());
					IDArry.add(IdList.get(j).getText());
					return j;
				} else
					j++;

				if (j == checkboxes.size()) {
					new ReportUtil().logInfo("No more task with nearest deadline so selecting random task");
					selectTaskNotAssociatedWithWP(1);
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select DT having less potential remaining");
		}

		return 0;
	}


	public List<String> captureNextTermValueOfSelectedTask() {
		try {
			List<String> data = new ArrayList<String>();
			List<WebElement> rpList = common.getElements(maintenancePlanningPageObj.selectedNextTermValue);
			for (WebElement ele : rpList) {
				data.add(common.getText(ele, "Potential Remaining"));
				new ReportUtil().logPass("Next Term value should captured", "Next Term value of a selected task is " + ele.getText());
			}
			return data;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture value of next term column");
			return null;
		}


	}


	public void selectOptionAndUnselectOthers(String option, String label) {
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
			By loc = By.xpath("//label[contains(text(),'" + label +
					"')]/../div/p-selectbutton//div[contains(@class,'highlight')]");
			List<WebElement> options = common.getElements(loc);
			for (WebElement ele : options) {
				common.clickJS(ele, ele.getAttribute("aria-label") + " to unselect it");
			}
			common.explicitWait(2);
			WebElement ele = common.getElement(By.xpath(ObjectUtil.getButtonXpath(option)));
			common.clickJS(ele, option + " to select it under " + label);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on " + option + " under label " + label);
		}
	}

	public void unselectSelected(String label) {
		try {
			label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
			By loc = By.xpath("//label[contains(text(),'" + label +
					"')]/../div/p-selectbutton//div[contains(@class,'highlight')]");
			List<WebElement> options = common.getElements(loc);
			for (WebElement ele : options) {
				common.clickJS(ele, ele.getAttribute("aria-label") + " to unselect it");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on " + label);
		}
	}

	public boolean waitTillDataLoaded(String type) {
		if (type.equalsIgnoreCase("yes")) {
			try {
				for (int i = 1; i < 50; i++) {
					try {
						common.getElement(maintenancePlanningPageObj.loadingIcon).isDisplayed();
						common.explicitWait(1);
					} catch (Exception e) {
						return true;
					}
				}
			} catch (Exception e) {
				System.out.println("Data has been loaded already");
				return true;
			}
		}
		return false;

	}


	public void deleteMaterialsExceptLatestOne(String table) {
		try {
			String count = common.getElement(By.xpath("//h3[contains(text(),'" + table + "')]")).getText();
			if (Integer.parseInt(CompactUtil.extractNumber(count)) > 1) {
				new LogisticsModule().selectAllSearchResult("Material", "Materials");
				List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.workPackagesResults);
				common.clickAnElement(listOfTo.get(listOfTo.size() - 1), "Latest Material");
				new LogisticsModule().clickOnBtn("delete");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to delete material to refresh test data");
		}

	}

	public int selectTaskWithOverPassedDeadLine() {
		//waitTillDataLoaded();
		int j = 0;
		List<WebElement> checkboxes = common.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available",
					"No operations are available. please change the test data");
			Assert.assertTrue("No operation available", false);
		}

		List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
		List<WebElement> potentialRemianingList = common.getElements(maintenancePlanningPageObj.potentialRemaining);
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);

		try {
			for (WebElement ele : potentialRemianingList) {
				String temp = ele.getText().trim();
				if (temp.contains("-") && nextWPList.get(j).getText().trim().isEmpty()) {
					System.out.println("Task found with overpassed deadline range is " + temp);
					common.scrollIntoView(checkboxes.get(j));
					common.click(checkboxes.get(j), "task " + IdList.get(j).getText() + " whose deadline is overpassed");
					System.out.println("Task added " + IdList.get(j).getText());
					IDArry.add(IdList.get(j).getText());
					return j;
				} else
					j++;

				if (j == checkboxes.size()) {
					new ReportUtil().logFail("", "No more task with overpassed deadline so selecting random task");
					selectTaskNotAssociatedWithWP(1);
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select DT having less potential remaining");
		}

		return 0;
	}

	public void verifyColumnPresent(List<String> data) {
		for (String column : data) {
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
			//common.checkElementPresentByVisibility(By.xpath("//th[contains(text(),'" + column + "')]"), "column " + column);
			common.checkElementPresentByVisibility(By.xpath("//div[@class='yac-table-header resizable']/div/div/div[contains(text(),'" + column + "')]"), "column " + column);
		}
	}

	public void wpHavingStatusNotDisplayed(String tableName, String columnName, String simulated, String planned) {
		simulated = prop.readPropertyData(simulated.trim().replaceAll(" ", "_"));
		planned = prop.readPropertyData(planned.trim().replaceAll(" ", "_"));
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		boolean flag = true;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
					+ "')]/../../..//tbody/tr/td[" + columnIndex + "]/span");

			List<WebElement> statusList = common.getElements(loc);
			for (WebElement ele : statusList) {
				if (ele.getText().contains(simulated) || ele.getText().contains(planned)) {
					new ReportUtil().logFail("Simulated and Planned WP should not display", "Simulated and Planned WP displayed");
					flag = false;
					break;
				}
			}
			if (flag == true) {
				new ReportUtil().logPass("Simulated and Planned WP should not display", "Simulated and Planned WP are not displayed");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate WP status on search results");
		}
	}


	public void dragAndDropToCreateWPOnForecastPage() {
		try {
			List<WebElement> rows = common.getElements(maintenancePlanningPageObj.canvasRows);
			common.getElementExplicitWait(maintenancePlanningPageObj.todayButton, 0);
			common.explicitWait(3);
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			//action.dragAndDrop(rows.get(0), common.getElement(maintenancePlanningPageObj.todayButton)).build().perform();
			//action.dragAndDropBy(rows.get(0), 250, 150).build().perform();
			action.dragAndDropBy(rows.get(0), 165, 30).build().perform();
			common.explicitWait(2);
			new ReportUtil().logInfo("user click on the calendar and stretch while keeping the click pressed ");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to perform drag and drop on forecast page");
		}
	}

	public void wpAvailableOnCanvas(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(maintenancePlanningPageObj.workpackageOnCanvas, "Work package on the calendar");
			//String wp = common.getText(maintenancePlanningPageObj.workpackageOnCanvas, "Work package");
			//common.click(maintenancePlanningPageObj.workpackageOnCanvas, "Work package " + wp);
			//String wpStatus = common.getText(maintenancePlanningPageObj.wptooltipOnCanvas, "WP Status");
			List<WebElement> eleList = common.getElements(maintenancePlanningPageObj.workpackageOnCanvas);
			int size = eleList.size();
			WebElement newEle = eleList.get(size - 1);
			String wp = common.getText(newEle, "Work package");
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.moveToElement(newEle).build().perform();

			String wpStatus = common.getText(common.getElement(maintenancePlanningPageObj.wptooltipOnCanvas), "WP Status");

			if (wpStatus.contains(status)) {
				new ReportUtil().logPass("Work package status should be " + status, "Work package status is " + status);
			} else
				new ReportUtil().logPass("Work package status should be " + status,
						"Work package status is not " + status);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate the WP status on Forecast page");
		}
	}

	public void rightClickOnWP() {
		try {

			List<WebElement> eleList = common.getElements(maintenancePlanningPageObj.workpackageOnCanvas);
			int size = eleList.size();
			WebElement newEle = eleList.get(size - 1);

			Actions action = new Actions(DriverUtil.getInstance().getDriver());
//			WebElement ele = common.getElement(maintenancePlanningPageObj.workpackageOnCanvas);
			action.contextClick(newEle).build()
					.perform();
			new ReportUtil().logInfo("Right clicked on work package");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to perform right click");
		}
	}

	public void clickEditMainInfo() {
		Actions action = new Actions(DriverUtil.getInstance().getDriver());
		common.explicitWait(.5);
		action.sendKeys(Keys.DOWN).perform();
		common.explicitWait(.5);
		action.sendKeys(Keys.RETURN).perform();
	}

	public void validateEnteredInfo(List<String> data) {
		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'" + str + "')]"), str);
		}

	}

	public void clickReverseCheckbox() {
		common.getElementExplicitWait(maintenancePlanningPageObj.reverseCheckbox, 2);
		common.click(maintenancePlanningPageObj.reverseCheckbox, "Reverse checkbox");
	}

	public void clickInstallRemoveItem() {
		common.clickJS(maintenancePlanningPageObj.installRemoveTableChkbx, "Item checkbox");
	}

	public String validateEventAndTransferOrder() {
		try {
			common.checkElementPresentByVisibility(maintenancePlanningPageObj.plusIcon, "plus icon");
			common.clickAnElement(maintenancePlanningPageObj.plusIcon, "plus icon");
			common.checkElementPresentByVisibility(maintenancePlanningPageObj.eventNumber,
					"Event Number " + common.getText(maintenancePlanningPageObj.eventNumber, "Event Number"));
			String to = common.getText(maintenancePlanningPageObj.transferOrderNum, "Transfer Order Number");
			common.checkElementPresentByVisibility(maintenancePlanningPageObj.transferOrderNum,
					"Transfer Order Number");
			return to;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate transfer and event number");
			return "";
		}

	}


	public void verifyWOWithStatusNotDisplayed(String status, String columnName, String tableName) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		int i = 0;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
					+ "')]/../../..//tbody//tr/td[" + columnIndex
					+ "]//span[@class='horizontal-separator']/following-sibling::span");

			List<WebElement> woStatus = common.getElements(loc);
			for (WebElement ele : woStatus) {
				if (ele.getText().trim().equalsIgnoreCase(status)) {
					new ReportUtil().logFail(
							"Work order with " + status + " status should not available under column " + columnName,
							"Work order with " + status + " status is available under column " + columnName);
					i++;
				}
			}
			if (i == 0) {
				new ReportUtil().logPass(
						"Work order with " + status + " status should not available under column " + columnName,
						"Work order with " + status + " status is not available under column " + columnName);
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify status under column " + columnName);
		}

	}


	public void verifyAccountableNameExistInWO(String accountable, String columnName, String tableName) {
		accountable = prop.readPropertyData(accountable.trim().replaceAll(" ", "_"));
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		int i = 0;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
					+ "')]/../../..//tbody//tr/td[" + columnIndex
					+ "]//span[.='" + accountable + "']/following-sibling::*");

			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

			List<WebElement> woStatus = common.getElements(loc);
			for (WebElement ele : woStatus) {
				if (ele.getText().trim().isEmpty()) {
					new ReportUtil().logInfo("Accoutable Information is not available for Work order " + listOfTo.get(i).getText());
				} else {
					new ReportUtil().logInfo("Accountable Information is available for Work order " + listOfTo.get(i).getText());
				}
				i++;
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify accoutable information under column " + columnName);
		}

	}

	public void validateWOFilteredAsPerStatus(String status) {
		try {
			String woCount = CompactUtil.extractNumber(common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order"));
			if (Integer.parseInt(woCount) > 0) {
				status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
				//List<WebElement> itemList = common.getElements(By.xpath("//span[@class='value'][contains(text(),'" + status + "')]"));
				List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

				if (Integer.parseInt(woCount) == listOfTo.size())
					new ReportUtil().logPass("All the work order should get filter as per the status " + status
							, "Work orders filtered correctly");
				else
					new ReportUtil().logFail("All the work order should get filter as per the status " + status
							, "Work orders filtered Incorrectly");
			} else {
				new ReportUtil().logInfo("There are no work orders available having status " + status);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order status after filter");
		}

	}

	public int workOrderCount(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			List<WebElement> itemList = common
					.getElements(By.xpath("//span[@class='value'][contains(text(),'" + status + "')]"));
			new ReportUtil().logInfo("Number of " + status + " work orders are " + itemList.size());
			return itemList.size();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture Work Order status after filter");
			return 0;
		}

	}

	public int workOrderCount_FilterByType(String type) {
		try {
			type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
			List<WebElement> itemList = common.getElements(By.xpath(ObjectUtil.xpathToTabContainer
					+ "//span[contains(text(),'" + type + "')]/following-sibling::span"));
			new ReportUtil().logInfo("Number of work orders of Type " + type + " are " + itemList.size());
			return itemList.size();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture Work Order Type after filter");
			return 0;
		}
	}


	public void validateWOFilteredAsPerType(String type) {
		try {
			String woCount = CompactUtil
					.extractNumber(common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order"));
			if (Integer.parseInt(woCount) > 0) {
				type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
				List<WebElement> itemList = common.getElements(By.xpath(
						"//span[contains(@class,'value')][contains(text(),'" + type + "')]"));

				List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

				if (itemList.size() == listOfTo.size())
					new ReportUtil().logPass("All the work order should get filter as per the Type " + type,
							"Work orders filtered correctly");
				else
					new ReportUtil().logFail("All the work order should get filter as per the Type " + type,
							"Work orders filtered uncorrectly");
			} else {
				new ReportUtil().logInfo("There are no work orders available having type " + type);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order type after filter");
		}

	}

	public void validateWOFilteredAsPerAsset_Assigned_Qualification_Zone(String sn, String asset, String filterType) {
		try {
			sn = prop.readPropertyData(sn.trim().replaceAll(" ", "_"));
			filterType = prop.readPropertyData(filterType.trim().replaceAll(" ", "_"));
			String woCount = CompactUtil
					.extractNumber(common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order"));
			if (Integer.parseInt(woCount) > 0) {
				List<WebElement> itemList = common.getElements(By.xpath(
						"//span[@class='label'][contains(.,'" + sn + "')]/following-sibling::*[contains(.,'" + asset + "')]"));
				List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);

				if (itemList.size() == listOfTo.size())
					new ReportUtil().logPass("All the work order should get filter as per the " + filterType + " " + asset,
							"Work orders filtered correctly");
				else
					new ReportUtil().logFail("All the work order should get filter as per the " + filterType + " " + asset,
							"Work orders filtered uncorrectly");
			} else {
				new ReportUtil().logInfo("There are no work orders available having asset " + filterType + " " + asset);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order for filter " + filterType);
		}

	}

	public void validateNumberColumnFiltered(String columnName, String tableName, String sortingType) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		List<Integer> arry = new ArrayList<Integer>();
		boolean flag = true;
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
				+ "')]/../../..//tbody//tr/td[" + columnIndex + "]/a");
		List<WebElement> numbers = common.getElements(loc);
		for (WebElement ele : numbers) {
			arry.add(Integer.parseInt(common.getText(ele, "")));

		}
		for (int i = 0; i < arry.size() - 1; i++) {
			if (sortingType.equalsIgnoreCase("ascending")) {
				if (arry.get(i) > arry.get(i + 1)) {
					flag = true;
				} else {
					new ReportUtil().logFail("",
							"Number at index " + i + " are not filtered in " + sortingType + " order");
					flag = false;
					break;
				}
			} else {
				if (arry.get(i) < arry.get(i + 1)) {
					flag = true;
				} else {
					new ReportUtil().logFail("",
							"Number at index " + i + " are not filtered in " + sortingType + " order");
					flag = false;
					break;
				}
			}
		}

		if (flag == true) {
			new ReportUtil().logPass("Column " + columnName + " should sorted in " + sortingType + " order", "Column " +
					columnName + " is sorted in " + sortingType + " order");
		}

	}

	public void validatColumnFiltered(String columnName, String tableName, String sortingType) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		List<Character> arry = new LinkedList<Character>();
		boolean flag = true;
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
				+ "')]/../../..//tbody//tr/td[" + columnIndex + "]/*");
		List<WebElement> numbers = common.getElements(loc);
		for (WebElement ele : numbers) {
			if (!ele.getText().trim().isEmpty() && Character.isLetter(ele.getText().charAt(0)))
				arry.add(common.getText(ele, "value from column " + columnName).toLowerCase().charAt(0));


		}
		System.out.println(arry);
		for (int i = 0; i < arry.size() - 1; i++) {
			if (sortingType.equalsIgnoreCase("ascending")) {
				if ((int) arry.get(i) <= (int) arry.get(i + 1)) {
					flag = true;
				} else {
					new ReportUtil().logFail("",
							columnName + " at index " + i + " are not filtered in " + sortingType + " order");
					flag = false;
					break;
				}
			} else {
				if ((int) arry.get(i) >= (int) arry.get(i + 1)) {
					flag = true;
				} else {
					new ReportUtil().logFail("",
							columnName + " at index " + i + " are not filtered in " + sortingType + " order");
					flag = false;
					break;
				}
			}
		}

		if (flag == true) {
			new ReportUtil().logPass("Column " + columnName + " should sorted in " + sortingType + " order", "Column " +
					columnName + " is sorted in " + sortingType + " order");
		}

	}


	public void validateDatesAreSorted(String tableName, String columnName, String type) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		boolean flag = true;
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			List<String> arry = new ArrayList<String>();

			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
					+ "')]/../../..//tbody/tr/td[" + columnIndex + "]/span");
			List<WebElement> ele = common.getElements(loc);
			for (WebElement element : ele) {
				if (!element.getText().trim().isEmpty()) {
					arry.add(element.getText().trim());
				}
			}
			System.out.println(arry);
			for (int i = 0; i < arry.size() - 1; i++) {
				if (type.equalsIgnoreCase("ascending")) {
					if (formatter.parse(arry.get(i)).before(formatter.parse(arry.get(i + 1)))
							|| formatter.parse(arry.get(i)).equals(formatter.parse(arry.get(i + 1)))) {
						flag = true;
					} else {
						new ReportUtil().logFail("Search results in column " + columnName + " should be sorted",
								"Search results in column " + columnName + " are not sorted at index " + i
										+ " value is " + arry.get(i));
						flag = false;
						break;
					}
				} else {
					if (formatter.parse(arry.get(i)).after(formatter.parse(arry.get(i + 1)))
							|| formatter.parse(arry.get(i)).equals(formatter.parse(arry.get(i + 1)))) {
						flag = true;
					} else {
						new ReportUtil().logFail("Search results in column " + columnName + " should be sorted",
								"Search results in column " + columnName + " are not sorted at index " + i
										+ " value is " + arry.get(i));
						flag = false;
						break;
					}
				}
			}

			if (flag == true) {
				new ReportUtil().logPass(
						"Search results in column " + columnName + " should be sorted in order " + type,
						"Search results in column " + columnName + " are sorted " + type);
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify column " + columnName + " sorting in order " + type);
		}
	}


	public void verifyMainInformationPanel_Info(String label) {
		String rawLabel = label;
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//div[@id='mainInformationPanel']//label[contains(text(),'"
				+ label + "')]/../div/*");
		String info = common.getText(loc, label);
		String addtn = "";
		try {
			addtn = common.getElement(loc).getAttribute("value").trim();
		} catch (Exception e) {

		}
		if (rawLabel.contains("Qualification") || rawLabel.contains("Workshop")) {
			if (addtn.isEmpty())
				new ReportUtil().logInfo("Information for label " + label + " is present");
			else
				new ReportUtil().logInfo("Information for label " + label + " is " + addtn);

		} else if (label.contains("Task")) {
			if (common.getText(maintenancePlanningPageObj.woTask, "WO Task").isEmpty()) {
				new ReportUtil().logFail("Information should be available for label " + label,
						"Information is not available for label " + label);
			} else {
				new ReportUtil().logPass("Information should be available for label " + label,
						"Information is available for label " + label + " as " + info + addtn);
			}

		} else {
			if (info.isEmpty() && addtn.isEmpty()) {
				new ReportUtil().logFail("Information should be available for label " + label,
						"Information is not available for label " + label);
			} else {
				new ReportUtil().logPass("Information should be available for label " + label,
						"Information is available for label " + label + " as " + info + addtn);
			}
		}
	}

	public void verifyWorkPackagePanel_Info(String label) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//div[@id='workPackagePanel']//label[contains(text(),'" + label + "')]/../div/*");
		String info = common.getText(loc, label);
		if ((info.isEmpty())) {
			new ReportUtil().logInfo("Information is not available for label " + label);
		} else {
			new ReportUtil().logPass("Information should be available for label " + label, "Information is available for label " + label + " as " + info);
		}
	}

	public void verifyFieldsAreFilled(String dropdownName, String table) throws Exception {
		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

			int dropdownColumnIndex = getColumnIndexFromTable(table, dropdownName);
			By value = By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + dropdownColumnIndex + "]//*[contains(@class,'inputtext')]");
			if (common.getText(value, dropdownName).trim().isEmpty()) {
				new ReportUtil().logFail("Column " + dropdownName + " should be filled",
						"Column " + dropdownName + " is not filled");
			} else
				new ReportUtil().logPass("Column " + dropdownName + " should be filled",
						"Column " + dropdownName + " is filled");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify Column " + dropdownName + " prefilled value");
		}

	}

	public Map<String, String> captureEventDetails() {
		Map<String, String> mp = new HashMap<String, String>();
		try {
			common.getElementExplicitWait(maintenancePlanningPageObj.defectWP, 0);
			String code = common.getAttributeByValue(maintenancePlanningPageObj.defectCode, "Defect Code");
			String wp = common.getText(maintenancePlanningPageObj.defectWP, "Defect WP");
			String wo = common.getText(maintenancePlanningPageObj.defectWO, "Defect WO");
//			String code = common.getText(maintenancePlanningPageObj.defectCode, "Defect Code");
			mp.put("WP", wp);
			mp.put("WO", wo);
			mp.put("Code", code);
			System.out.println(wp);
			System.out.println(wo);
			System.out.println(code);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture event details");

		}
		return mp;

	}

	public String captureEventCode() {
		try {
			String code = common.getAttributeByValue(maintenancePlanningPageObj.defectCode, "Defect Code");
			return code;

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture event details");
			return null;

		}

	}

	public void clickWOLink() {
		common.click(maintenancePlanningPageObj.defectWOLink, "Work order link");
	}

	public void clickAsset() {
		common.clickJS(maintenancePlanningPageObj.assetNumber, "Asset Number " +
				common.getText(maintenancePlanningPageObj.assetNumber, "Asset Number"));
	}

	public void verifyEventStatus(String status, Map<String, String> mp) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			String code = mp.get("Code");
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[contains(.,'" + code
					+ "')]/../..//*[contains(.,'" + status + "')]");
			if (common.getElement(loc).isDisplayed()) {
				new ReportUtil().logPass("Status of event " + code + " should be " + status,
						"Status of event " + code + " is " + status);
			} else {
				new ReportUtil().logFail("Status of event " + code + " should be " + status,
						"Status of event " + code + " is not " + status);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify status of event");
		}
	}

	public void clickOnSaveButton(String page) {
		common.clickJS(maintenancePlanningPageObj.saveBtn_UsingId, "Save button on " + page + " page");
	}


	public void validateOpertaionsUnderWO(String operation) {
		try {
			operation = prop.readPropertyData(operation.trim().replaceAll(" ", "_"));
			WebElement results = common.getElement(By.xpath("//h3[contains(text(),'" + operation + "')]"));
			String extractedValue = CompactUtil.extractNumber(results.getText());
			new ReportUtil().logInfo("Total number of operations are " + extractedValue);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify number of operations");
		}
	}

	public String openEvent(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.recieptFileList);
			String selectedItem = common.getText(receiptList.get(index), "Selected item");
			common.clickAnElement(receiptList.get(index), "Event available at index " + index);
			return selectedItem;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select Event");
			throw new Exception("Unable to select Event");
		}
	}

	public String openEventFromEventTable(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.eventsList);
			String selectedItem = common.getText(receiptList.get(index), "Selected item");
			common.clickAnElement(receiptList.get(index), "Event available at index " + index);
			return selectedItem;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select Event");
			throw new Exception("Unable to select Event");
		}
	}

	public void clickOnCapturedEvent(String id) {
		common.clickJS(By.xpath("//a[contains(text(),'" + id + "')]"), "item " + id);
	}

	public void clickOnEvent(String table, String column) {
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));

			int chronoNumber = getColumnIndexFromTable(table, column);
			By loc = By.xpath("(//h3[contains(text(),'" + table + "')])[1]/../../..//tr/td[" + chronoNumber + "]//a");
			List<WebElement> ele = common.getElements(loc);
			String cNo = ele.get(0).getText();
			common.clickJS(ele.get(0), "Fligh with Chrono number " + cNo);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on item using its chrono Number");
		}

	}

	public void clickOnNewlyCreatedDefect() {
		List<WebElement> ele = common.getElements(maintenancePlanningPageObj.eventsUnderDefectTable);
		common.clickJS(ele.get(ele.size()), "Newly created Event Code");
	}

	public void deleteEvents(String subject) {
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//td[contains(text(),'" + subject + "')]/..//mat-checkbox");
		List<WebElement> ele = common.getElements(loc);
		for (WebElement element : ele) {
			common.clickJS(element, "Events having subject " + subject);
		}
		new LogisticsModule().clickOnBtnUsingJS("Delete");
		new MaintenancePlanning().clickOnPopupBtnIfAppears("yes");

	}

	public void openDocument() {
		common.clickJS(maintenancePlanningPageObj.documentFile, "File");
	}

	public void openDefectWP() {
		common.click(maintenancePlanningPageObj.defectWPLink, "Work package link");
	}

	public void clickOnEventPostponedmentCode() {
		common.clickJS(maintenancePlanningPageObj.postponedEventCode_Value, "Postponement event code");
	}

	public void validateAuthorName(String Author) {
		common.explicitWait(4);
		String name = common.getAttributeByValue(maintenancePlanningPageObj.authorText, "Author Name");
		if (name.contains(Author)) {
			new ReportUtil().logPass("Author name should be " + Author, "Author name is " + Author);
		} else {
			new ReportUtil().logFail("Author name should be " + Author, "Author name is not " + Author);
		}

	}

	public void verifyEventReportInfo(String reasonValue, String assetValue) {
		try {
			String reportType = common.getText(maintenancePlanningPageObj.reportType, "Report Type");
			String asset = common.getAttributeByValue(maintenancePlanningPageObj.assetValue, "Asset");
			String reason = common.getAttributeByValue(maintenancePlanningPageObj.eventDescription,
					"Event description");

			if (reasonValue.equalsIgnoreCase(reason)) {
				new ReportUtil().logPass("Value of the event decription should be " + reasonValue,
						"Value of the event decription is " + reasonValue);
			} else {
				new ReportUtil().logPass("Value of the event decription should be " + reasonValue,
						"Value of the event decription is not " + reasonValue);
			}

			if (!asset.isEmpty() && asset.contains(assetValue)) {
				new ReportUtil().logPass("Value of an asset should be related to " + assetValue,
						"Value of an asset is related to " + assetValue);
			} else {
				new ReportUtil().logPass("Value of an asset should be related to " + assetValue,
						"Value of an asset is related to not " + assetValue);
			}

			new ReportUtil().logInfo("Report type is displayed as " + reportType);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Postponement Event info");
		}

	}

	public void selectTO() throws Exception {
		try {
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.toCheckboxes);
			common.clickAnElement(listOfTo.get(listOfTo.size() - 1), "Work Oder " + listOfTo.get(listOfTo.size() - 1).getText());
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select the Work Order");
			throw new Exception("Unable to select the Work Order");
		}
	}


	public void editWOBtn() {
		common.clickAnElement(maintenancePlanningPageObj.editWOBtn, "Edit");
	}

	public void clickRecipientWorkshop() {
		new LogisticsModule().clickWebElement(logisticModuleObj.recipientWorkshopBtn, "Recipient Workshop");
	}

	public void clickrecipientWorkshop() {
		new LogisticsModule().clickWebElement(logisticModuleObj.recipientWrkshopBtn, "Recipient Workshop");
	}

	public void clickShipperWorkshop() {
		new LogisticsModule().clickWebElement(logisticModuleObj.shipperWorkshopBtn, "Shipper Workshop");
	}

	public void clickShipperWorkshopWarehouse() {
		new LogisticsModule().clickWebElement(logisticModuleObj.shipperWarehouseBtn, "Shipper Warehouse");
	}

	public void itemAddedToSection(String item, String tableName) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		WebElement results = common.getElement(By.xpath("//h3[contains(.,'" + tableName + "')]"));
		String extractedValue = CompactUtil.extractNumber(results.getText());
		if (Integer.parseInt(extractedValue) > 0)
			new ReportUtil().logPass(item + " should be added to section " + tableName, item + " is added to section " + tableName);
		else {
			new ReportUtil().logFail(item + " should be added to section " + tableName, item + " is not added to section " + tableName);
		}
	}

	public void clickAddBtnUnderWorkOrderDocument() {
		common.clickJS(maintenancePlanningPageObj.addWorkOrderDocument, "Add button");
	}

	public void woProgressDetails(List<String> data) {
		for (String label : data) {
			label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
			if (label.toLowerCase().contains("progress")) {
				By progress = By.xpath("//label[contains(text(),'" + label
						+ "')]/..//div[contains(@class,'progressbar-label')]");
				new ReportUtil().logInfo("Progress of a work order is " + common.getText(progress, label) + " percent");
			} else {
				common.checkElementPresentByVisibility(By.xpath("//label[contains(text(),'" +
						label + "')]"), label + " field");
				String val = common.getAttributeByValue(By.xpath("//label[contains(text(),'" +
						label + "')]/..//input"), label + "value");
				if (val.trim().isEmpty()) {
					new ReportUtil().logInfo("Value for label " + label + " is not available as of now");
				} else {
					new ReportUtil().logInfo("Value for label " + label + " is " + val);
				}

			}
		}
	}

	public void procuementRequestHeaderInfo(String tableName, String column) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		WebElement results = common.getElement(By.xpath("//h3[contains(.,'" + tableName + "')]"));
		String extractedValue = CompactUtil.extractNumber(results.getText());
		if (Integer.parseInt(extractedValue) > 0) {
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
			int colIndex = getColumnIndexFromTable(tableName, column);
			By val = By.xpath("//h3[contains(.,'" + column + "')]/../../..//tbody/tr/td[" + colIndex + "]");
			String text = common.getText(val, column);
			if (!text.trim().isEmpty()) {
				new ReportUtil().logPass("Value for a column " + column + " should exist",
						"Value for a column " + column + " exist as " + text);
			} else {
				new ReportUtil().logFail("Value for a column " + column + " should exist",
						"Value for a column " + column + " is not available");
			}
		} else {
			new ReportUtil().logInfo("No procurement request is available so validateion for column " +
					column + " skipped");
		}

	}

	public void clearDateFromField(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		String clear = prop.readPropertyData("Clear");
		common.clickJS(By.xpath(ObjectUtil.getInputboxXpath(field)), field);
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(clear)), "Clear");
	}


	public void countOperation(String table, int expectedCount) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		int count = new LogisticsModule().getItemCountFromTable(table);
		if (expectedCount == count) {
			new ReportUtil().logPass("Number of operation in the table should be " +
					expectedCount, "Number of operation in the table are " + count);
		} else {
			new ReportUtil().logFail("Number of operation in the table should be " +
					expectedCount, "Number of operation in the table are " + count);
		}
	}

	public void verifyOpeningDatePrefilled(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		By value = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName + "')]/..//input");
		String text = common.getAttributeByValue(value, fieldName);
		if (!text.trim().isEmpty()) {
			new ReportUtil().logPass("Opening date should be auto filled", "Opening date is autofilled as " + text);
		} else {
			new ReportUtil().logFail("Opening date should be auto filled", "Opening date is empty");
		}
	}

	public void verifyInfoFilledInField(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		By loc = By.xpath("//label[contains(.,'" + fieldName + "')]/..//span[contains(@class,'inputtext')]");
		String text = common.getText(loc, fieldName);
		if (!text.isEmpty()) {
			new ReportUtil().logPass("Information should be available for field " +
					fieldName, "Information is available for field " + fieldName + " as " + text);
		} else {
			new ReportUtil().logFail("Information should be available for field " +
					fieldName, "Information is not available for field " + fieldName);
		}
	}

	public void clearAssignedToDetails(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		By loc = By.xpath("//label[contains(.,'" + fieldName + "')]/..//span[contains(@class,'inputtext')]");
		By clear = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName + "')]/..//i");
		String text = common.getText(loc, fieldName);
		if (!text.isEmpty()) {
			common.clickJS(clear, fieldName + " clear icon");
		}
	}

	public void selectWorkOrderAtIndex(int index) {
		try {
			List<WebElement> woCheckbox = common.getElements(maintenancePlanningPageObj.workOrderCheckboxList);
			if (woCheckbox.size() == 0) {
				new ReportUtil().logFail("", "No work order is available under WP");
			} else {
				common.click(woCheckbox.get(index - 1), "Work order checkbox");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select work order");

		}

	}

	public void openWorkOrderAtIndex(int index) {
		try {
			List<WebElement> woName = common.getElements(maintenancePlanningPageObj.workOrderNameList);
			if (woName.size() == 0) {
				new ReportUtil().logFail("", "No work order is available under WP");
			} else {
				common.click(woName.get(index - 1), "Work order");
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select work order");

		}

	}

	public void verifyAccountableInfoForWO(int index, String expected) {
		try {
			List<WebElement> woCheckbox = common.getElements(maintenancePlanningPageObj.woAccountableList);
			if (woCheckbox.size() == 0) {
				new ReportUtil().logFail("", "No work order is available under WP");
			} else {
				String name = common.getText(woCheckbox.get(index - 1), "Accountable");
				if (name.equalsIgnoreCase(expected)) {
					new ReportUtil().logPass("Accountable info should be " + expected, "Accountable info is " + expected);
				} else {
					new ReportUtil().logFail("Accountable info should be " + expected, "Accountable info is " + name);
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select work order");

		}
	}

	public void textMatchInField(String expectedText, String field) {
		String rawField = field;
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		By loc;
		String actualText;
		if (rawField.contains("Workshop")) {
			loc = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + field + "')]/..//input");
			actualText = common.getAttributeByValue(loc, field);
		} else {
			loc = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + field + "')]/..//span[contains(@class,'inputtext')]");
			actualText = common.getText(loc, field);
		}
		if (expectedText.contains(actualText)) {
			new ReportUtil().logPass("Text in the field " + field + " should be " +
					expectedText, "Text in the field " + field + " is " + expectedText);
		} else {
			new ReportUtil().logFail("Text in the field " + field + " should be " +
					expectedText, "Text in the field " + field + " is " + actualText);
		}
	}

	public void enterDate(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpath(field)), 0);
		new LogisticsModule().enterDate(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpath(field)), field, "Today");
	}

	public void enterStartDate(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpath(field)), 0);
		new LogisticsModule().enterTodaysDate(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpath(field)), field, "Today");
	}

	public void enterStartDateOnPopup(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpathOnPopup(field)), 0);
		new LogisticsModule().enterTodaysDate(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getInputboxXpathOnPopup(field)), field, "Today");
	}

	public List<String> captureWorkshopAndAccountable(List<String> data) {
		List<String> info = new ArrayList<>();
		for (String ss : data) {
			ss = prop.readPropertyData(ss.trim().replaceAll(" ", "_"));
			By loc = By.xpath("//a-modal//label[contains(.,'" + ss + "')]/..//span[contains(@class,'inputtext')]");
			String value = common.getText(loc, ss);
			info.add(value);
		}
		return info;
	}

	public void clickGeneratePN() {
		common.scrollAndClick_WithoutJSWait(maintenancePlanningPageObj.generatePN, "Generate Packaging number");
	}

	public void enterCapturedWPNumber(String field, String value) {
		new LogisticsModule().enterValueInField(field, value);
	}

	public String getWPFromSubtitle(String type) {
		try {
			String text = common.getElement(maintenancePlanningPageObj.pageSubTitle).getText().trim();
			String[] data = text.split("\\|");
			text = CompactUtil.extractNumber(data[0]);
			new ReportUtil().logInfo("Work package number captured as " + text);
			return text;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture " + type);
			return "";
		}
	}

	public void validateDataExistInColumn_MaintenanceSchedule(String column) {
		if (column.contains("Equivalent")) {
			iterateOverEachTask(maintenancePlanningPageObj.equivalentPotList, "Equivalent Potential");
		} else if (column.contains("Remaining")) {
			iterateOverEachTask(maintenancePlanningPageObj.potentialRemaining, "potential Remaining");
		} else if (column.contains("Next Term")) {
			iterateOverEachTask(maintenancePlanningPageObj.nextTermListUnderArrow, "Next Term");
		} else
			iterateOverEachTask(maintenancePlanningPageObj.periodicityList, "Periodicity/ Lead Time");
	}


	public void iterateOverEachTask(By loc, String column) {
		try {
			int row = 1;
			int check = 0;
			List<WebElement> ID = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
			List<WebElement> element = common.getElements(loc);
			for (WebElement ele : element) {
				if (ele.getText().trim().isEmpty()) {
					String name = common.getText(ID.get(row - 1), "ID for column " + column);
					new ReportUtil().logInfo("Value for column " + column + " is not exist for ID " + name, false);
					check++;
				}
				row++;
				if (row > 1)
					break;
			}
			if (check > 0) {
				new ReportUtil().logInfo("Total missing value for column " + column + " are " + check, false);
			} else {
				new ReportUtil().logPass("for each task value should be present for column " +
						column, "for each task value is present for column " + column);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate for column " + column);
		}

	}

	public void validateControlColumnStatus(String deadlineStatus, String controlStatus) {
		try {
			List<WebElement> potential = common.getElements(maintenancePlanningPageObj.equivalentPotList);
			List<WebElement> ID = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
			List<WebElement> control = common.getElements(maintenancePlanningPageObj.controlList);
			int nokCheck = 0;
			int okCheck = 0;
			int index = 0;
			for (WebElement ele : potential) {
				String text = ele.getText().trim();
				String status = common.getText(control.get(index), "Control Status");
				String id = common.getText(ID.get(index), "ID");
				if (deadlineStatus.toLowerCase().equalsIgnoreCase("passed")) {
					if (text.isEmpty()) {
					} else if (text.contains("-")) {
						if (status.equalsIgnoreCase(controlStatus)) {
						} else {
							new ReportUtil().logInfo("Value of status " + controlStatus + " is incorect for column " + id, false);
							nokCheck++;
						}
					}
				} else {
					if (text.isEmpty()) {
					} else if (!text.contains("-")) {
						if (status.equalsIgnoreCase(controlStatus) || status.equalsIgnoreCase("WARNING") || status.equalsIgnoreCase("ALERTE")) {
						} else {
							new ReportUtil().logInfo("Value of status " + controlStatus + " is incorect for column " + id, false);
							okCheck++;
						}
					}
				}
				index++;
			}
			if (deadlineStatus.toLowerCase().equalsIgnoreCase("passed")) {
				if (nokCheck == 0) {
					new ReportUtil().logPass("Control status for passed dealine task should be NOK",
							"Control status for passed dealine task is NOK");
				} else {
					new ReportUtil().logFail("Control status for passed dealine task should be NOK",
							"Control status for passed dealine task is not NOK");
				}
			} else {
				if (okCheck == 0) {
					new ReportUtil().logPass("Control status for upcoming dealine task should be OK",
							"Control status for upcoming dealine task is OK");
				} else {
					new ReportUtil().logFail("Control status for upcoming dealine task should be OK",
							"Control status for upcoming dealine task is not OK");
				}
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate column status for deadline " + deadlineStatus);
		}
	}


	public void validateWorkMgntList(String accountable, String accountableName) {
		try {
			accountable = prop.readPropertyData(accountable.trim().replaceAll(" ", "_"));
			List<WebElement> ele = common
					.getElements(By.xpath("//span[contains(.,'" + accountable + "')]/following-sibling::span"));
			int count = 0;

			for (WebElement ls : ele) {
				if (!common.getText(ls, accountable).equalsIgnoreCase(accountableName)) {
					new ReportUtil().logFail("Accountable info for all work orders should be " + accountableName,
							"Accountable info is not " + accountableName);
					count++;
				}
			}
			if (count == 0) {
				new ReportUtil().logPass("Accountable info for all work orders should be " + accountableName,
						"Accountable info is " + accountableName);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate accountable info");
		}
	}

	public void WOFilteredAsPerTypeUnderMyWOPage(String type) {
		try {
			type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
			List<WebElement> itemList = common.getElements(By.xpath(
					"//span[contains(@class,'value')][contains(text(),'" + type + "')]"));

			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.woListUnderMyWOPage);

			if (itemList.size() == listOfTo.size())
				new ReportUtil().logPass("All the work order should get filter as per the Type " + type,
						"Work orders filtered correctly");
			else
				new ReportUtil().logFail("All the work order should get filter as per the Type " + type,
						"Work orders filtered uncorrectly");

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order type after filter");
		}

	}

	public void WOFilteredAsPerStatusUnderMyWOPage(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			List<WebElement> itemList = common.getElements(By.xpath("//span[contains(@class,'value')][contains(text(),'" + status + "')]"));
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.woListUnderMyWOPage);

			if (itemList.size() == listOfTo.size())
				new ReportUtil().logPass("All the work order should get filter as per the status " + status
						, "Work orders filtered correctly");
			else
				new ReportUtil().logFail("All the work order should get filter as per the status " + status
						, "Work orders filtered uncorrectly");

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order status after filter");
		}

	}

	public void validateValueUnderColumn(String table, String column, String expectedValue) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));

		int index = getColumnIndexFromTable(table, column);
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + table
				+ "')]/../../..//tbody/tr/td[" + index + "]");
		if (common.getText(loc, column).equalsIgnoreCase(expectedValue)) {
			new ReportUtil().logPass("Value of column " + column + " should be " + expectedValue,
					"Value of column " + column + " is " + expectedValue);
		} else {
			new ReportUtil().logFail("Value of column " + column + " should be " + expectedValue,
					"Value of column " + column + " is " + common.getText(loc, column));
		}
	}

	public String captureWO() {
		List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
		String wo = common.getText(listOfTo.get(0), "Work order number");
		System.out.println("Wo is captured as " + wo);
		return wo;

	}

	public String captureWOFromMWO() {
		List<WebElement> wo = common.getElements(maintenancePlanningPageObj.pageSubTitle);
		String wos = common.getText(wo.get(0), "Work order number");
		System.out.println("Wo is captured as " + wos);
		return wos;
	}

	public String clickLinkOnColumnFromTable(String table, String columnName) {
		String number;
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		try {
			int columnIndex = getColumnIndexFromTable(table, columnName);
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + table
					+ "')]/../../..//tbody/tr/td[" + columnIndex + "]//a");
			List<WebElement> numbers = common.getElements(loc);
			number = common.getText(numbers.get(0), columnName);
			common.clickJS(numbers.get(0), number + " under column " + columnName);
			new ReportUtil().logInfo("Clicked on " + number);
			return number;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on column " + columnName);
			return null;
		}
	}

	public void clickOnAddDefect() {
		common.clickJS(maintenancePlanningPageObj.defectAddBtn, "Add");
	}

	public void clickLength() {
		common.clickAnElement(maintenancePlanningPageObj.lengthLabel, "length");
	}

	public void workManagementAvailable() {
		List<WebElement> ele = common.getElements(By.xpath(ObjectUtil.xpathToTabContainer + "//tbody//tr"));
		if (ele.size() > 0) {
			new ReportUtil().logPass("User should see the list of work managements", "list of work managements dislpayed");
		} else {
			new ReportUtil().logFail("User should see the list of work managements", "list of work managements in not dislpayed");
		}
	}


	public void verifyItemsAvailable(String table) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		int count = new LogisticsModule().getItemCountFromTable(table);
		if (count > 0) {
			new ReportUtil().logPass("Items in the table should be available"
					, "Items in the table are available");
		} else {
			new ReportUtil().logFail("Items in the table should be available"
					, "Items in the table are not available");
		}
	}

	public void verifyItemsNotAvailable(String table) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		int count = new LogisticsModule().getItemCountFromTable(table);
		if (count == 0) {
			new ReportUtil().logPass("Items in the table should not be available"
					, "Items in the table are not available");
		} else {
			new ReportUtil().logFail("Items in the table should not be available"
					, "Items in the table are available");
		}
	}

	public void unselectAsset(String asset) {
		By loc = By.xpath("//td[.='" + asset
				+ "']/../td[contains(@class,'checkbox')]/mat-checkbox[contains(@class,'checkbox-checked')]");
		if (common.getElements(loc).size() == 1) {
			common.clickAnElement(loc, asset);
		}
	}

	public void clickSearchBtnOnPopup() {
		common.clickJS(By.xpath(ObjectUtil.searchIconXpathOnPopup()), "Search");

	}

	public void clickSearchBtnOnAssetAssignmentPopup() {
		common.clickJS(By.xpath("//*[contains(text(),'Asset(s) assignment to Maintenance Program')]/../../following-sibling::div//aw-icon-search"), "Search");
	}

	public void enterNewLimitDate(String label) {
		int index = 10;
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath("//label[normalize-space()='" + label + "']/..//input"), label);
		common.clickJS(maintenancePlanningPageObj.calendarNextIcon, "Calendar next icon to select future date");
		common.clickJS(By.xpath("//*[contains(.,'" + index + "')][contains(@class,'p-ripple')]"),
				"Future date");
	}

	public String captureWOHavingStatus(String status) {
		int index = 1;
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
//		By wo = By.xpath(ObjectUtil.xpathToTabContainer + "//span[@class='value'][contains(text(),'" + status
//				+ "')]/preceding::div[contains(@class,'designation')][" + index + "]/../a");
		//By wo = By.xpath(ObjectUtil.xpathToTabContainer + "//span[@class='value'][contains(text(),'" + status
		//		+ "')]/preceding::div[contains(@class,'designation')]/../a");
		By wo = By.xpath("//span[contains(text(),'" + status + "')]/../../../..//a[contains(text(),'WO')]");
		List<WebElement> wos = common.getElements(wo);
		String woName = common.getText(wos.get(index - 1), "Selected Work Order");
		new ReportUtil().logInfo("Work order captured as  " + woName);
		return woName;
	}

	public void selectWOFromWorkMgnt(String woNumber) {
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + woNumber + "']/../../../..//span[contains(@class,'checkbox')]");
		common.clickJS(loc, woNumber);
	}

	public void openCapturedReleasedWO(String woNumber) {
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + woNumber + "']");
		common.click(loc, woNumber);
	}

	public void openCapturedWP(String wpNumber) {
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + wpNumber + "']");
		common.clickJS(loc, wpNumber);
	}

	public void selectCapturedWPFromWorkMgnt(String wpNumber) {
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + wpNumber + "']/../../../..//span[contains(@class,'checkbox')]");
			List<WebElement> ele = common.getElements(loc);
			for (int i = 0; i < ele.size(); i++) {
				common.clickJS(ele.get(i), wpNumber);
			}
			new ReportUtil().logPass("select captured work package", "selected captured WP");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select WP");
		}

	}

	public void wosNotAvailable(String relWO, String posWO) {
		common.getElementExplicitWaitInVisibility(By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + relWO + "']"), "5", relWO);
		common.getElementExplicitWaitInVisibility(By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + posWO + "']"), "5", posWO);
	}

	public void wpNotAvailable(String wpNum) {
		common.getElementExplicitWaitInVisibility(By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + wpNum + "']"), "5", wpNum);
	}

	public void clickOnLinkUnderLabel(String label) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.xpathToTabContainer + "//label[normalize-space()='" + label + "']/..//a"), label);

	}

	public void openWPWorkMgnt(String woNumber) {
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//a[normalize-space()='" + woNumber + "']/../../../..//a");
		List<WebElement> ele = common.getElements(loc);
		common.clickJS(ele.get(0), woNumber + " work package");
	}

	public List<String> selectDefect(int NumberOfItems, String type) {
		type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
		String[] data = type.split(" ");
		//waitTillDataLoaded();
		int index = 0;
		int j = 0;
		List<WebElement> checkboxes = common
				.getElements(maintenancePlanningPageObj.checkboxListItemUnderGroup);
		if (checkboxes.size() == 0) {
			new ReportUtil().logFail("Operation should be available",
					"No operations are available. please change the test data");
			Assert.assertTrue(false);
		}

		List<WebElement> IdList = common.getElements(maintenancePlanningPageObj.IDListUnderGroup);
		List<WebElement> typeList = common.getElements(maintenancePlanningPageObj.typeUnderGroup);
		List<WebElement> nextWPList = common.getElements(maintenancePlanningPageObj.nextWPLink);

		try {
			for (WebElement ele : typeList) {
				if (ele.getText().trim().contains(data[0]) && ele.getText().trim().contains(data[1])) {
					j++;
					common.scrollIntoView(checkboxes.get(index));
					System.out.println("clicking for task at index " + index);
					common.clickJS(checkboxes.get(index), "defect " + j);
					IDArry.add(IdList.get(index).getText());
					index++;
					if (j == NumberOfItems)
						break;
				} else
					index++;
				if (index == checkboxes.size()) {
					deleteTaskAssignedToWP(NumberOfItems);
					new ReportUtil().logInfo("No more defect are available");
				}

			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select defect");
		}

		return IDArry;
	}

	public int defectWPIndex(String type) {
		type = prop.readPropertyData(type.trim().replaceAll(" ", "_"));
		String[] data = type.split(" ");
		int j = 0;
		List<WebElement> typeList = common.getElements(maintenancePlanningPageObj.typeUnderGroup);
		try {
			for (WebElement ele : typeList) {
				if (ele.getText().trim().contains(data[0]) && ele.getText().trim().contains(data[1])) {
					System.out.println("WP will be created at index " + j);
					return j;
				} else {
					j = j + 1;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture WP Index");
		}
		return j;
	}

	public void openWOFromWorkMgnt(int index) {
		List<WebElement> wos = common.getElements(By.xpath("//tbody//td[" + index + "]//a"));
		common.clickJS(wos.get(0), "work order");
	}

	public void verifyOptionExist(String dropdownName, String option, String table) throws Exception {
		boolean found = false;
		dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
		option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));

		int dropdownColumnIndex = getColumnIndexFromTable(table, dropdownName);
		By optionList = By.xpath("//p-dropdownitem/li/span");
		By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//td[" + dropdownColumnIndex + "]//span[contains(@class,'chevron-down')]");

		WebElement element = common.getElementExplicitWait(chevronDown, 0);
		common.clickJS(element, dropdownName);
		new ReportUtil().logInfo("Clicked on dropdown " + dropdownName);
		common.explicitWait(2);
		List<WebElement> list = common.getElements(optionList);
		for (WebElement ele : list) {
			String text = ele.getText();
			if (text.equalsIgnoreCase(option)) {
				new ReportUtil().logPass(option + " option should exist in dropdown " + dropdownName,
						(option + " option exist in dropdown " + dropdownName));
				common.explicitWait(1);
				found = true;
				break;
			}
		}
		common.clickJS(element, dropdownName);

		if (!found) {
			new ReportUtil().logFail(option + " option should exist in dropdown " + dropdownName, option + " option is not available in dropdown " + dropdownName);
		}
	}

	public void veirfyWarehouseEntryText(String text) {
		common.checkElementPresentByVisibility(maintenancePlanningPageObj.warehouseEntry, text);
	}

	public boolean validateSearchFilterInRows(String columnName, String tableName, String filter) {
		String rawColumn = columnName;
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		filter = prop.readPropertyData(filter.trim().replaceAll(" ", "_"));
		int columnIndex = new LogisticsModule().findColumnIndexFromTable(tableName, columnName);
		int row = 1;
		boolean flag = true;
		try {
			By loc;
			loc = By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getTotalRowsXpath(tableName, columnIndex));

			List<WebElement> ele = common.getElements(loc);
			if (ele.size() == 0) {
				new ReportUtil().logInfo("There are no search results available with entered criteria");
				return false;
			}
			for (WebElement element : ele) {
				if (!element.getText().trim().equalsIgnoreCase(filter)) {
					new ReportUtil().logFail(
							"Search results should be correct for column " + columnName + " according to filter "
									+ filter,
							"Search results are not correct for column " + columnName + " according to filter " + filter
									+ " -->At row " + row);
					flag = false;
					break;
				}
				row++;
			}
			if (flag == true) {
				new ReportUtil().logPass(
						"Search results should be correct for column " + columnName + " according to filter " + filter,
						"Search results are correct for column " + columnName + " according to filter " + filter);
				return true;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results for filter " + filter);
		}

		return false;
	}

	public String captureForecastNumber(int index) {
		By loc = By.xpath(ObjectUtil.getTotalRowsXpath("Results", 1));
		List<WebElement> ele = common.getElements(loc);
		String num = ele.get(index).getText().trim();
		return num;
	}

	public void addOperationIfNotExist(String tableName) {
		common.explicitWait(3);
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		if (new LogisticsModule().getItemCountFromTable(tableName) == 0) {
			common.clickJS(maintenancePlanningPageObj.addOperation, "Add");
			common.explicitWait(1);
			new LogisticsModule().clickOnElementOnPopup("Add", false);
			new LogisticsModule().verifyPopupIsDisplayed("Search Operation");
			common.explicitWait(1);
			new MaintenancePlanning().clickButtonOnPopUP("Search Operation", "Search");
			new MaintenancePlanning().AddItemToTheList("Search Operation");
			new MaintenancePlanning().clickButtonOnPopUP("Search Operation", "Validate");
			new MaintenancePlanning().clickButtonOnPopUP("work order edition", "Validate");
		}

	}

	public void changeStatusFromTBCToPerformed(String tbc) {
		tbc = prop.readPropertyData(tbc.trim().replaceAll(" ", "_"));
		String wpStatus = common.getElement(maintenancePlanningPageObj.statusOfWP).getText().trim();
		if (wpStatus.equalsIgnoreCase(tbc)) {
			common.clickJS(maintenancePlanningPageObj.statusOfWP, wpStatus);
			common.click(By.xpath("//aw-work-order-status//button"), "quality");
		}
	}

	public void verifyValueAvailableForPNField() {
		if (common.getText(maintenancePlanningPageObj.pnLink, "P/N").isEmpty()) {
			new ReportUtil().logFail("Value should be available for PN field", "Value is not available for PN field");
		} else {
			new ReportUtil().logPass("Value should be available for PN field", "Value is available for PN field");
		}
	}

	public void verifyWONotAvailableList(String wo) {
		common.elementNotAvailableOnPage(By.xpath("//a[normalize-space()='" + wo + "']"), wo);
	}

	public void validateAddFlightMsg(String msg1, String msg) {
		try {
			msg1 = prop.readPropertyData(msg1.trim().replaceAll(" ", "_"));
			System.out.println(msg1);
			if (common.getElement(By.xpath("//*[contains(@class,'b-grid-cell')]//*[contains(text(),'" + msg1 + "')]")).isDisplayed()) {
				new ReportUtil().logPass("Create flight message " + msg + " should be present",
						"Create flight message " + msg + " is present");
			} else
				new ReportUtil().logFail("Create flight message " + msg + " should be present",
						"Create flight message " + msg + " is not present");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate message " + msg);
		}
	}


	public void performDragAndDropOperation() {
		try {
			List<WebElement> rows = common.getElements(maintenancePlanningPageObj.canvasRows);
			common.explicitWait(3);
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.dragAndDropBy(rows.get(0), 165, 30).build().perform();
			common.explicitWait(2);
			new ReportUtil().logInfo("user performed drag and drop on forecast page ");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to perform drag and drop on forecast page");
		}
	}

	public int rowCount() {
		List<WebElement> rows = common.getElements(maintenancePlanningPageObj.canvasRows);
		return rows.size();
	}

	public void clickOnFirstRow() {
		try {
			List<WebElement> rows = common.getElements(maintenancePlanningPageObj.canvasRows);
			List<WebElement> rows1 = common.getElements(maintenancePlanningPageObj.ScheduledFlightRows);
			common.explicitWait(3);
			common.click(rows.get(0), "First Row");
			common.click(rows1.get(0), "First Row");
			new ReportUtil().logInfo("user performed click operation on First Row ");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to perform click operation on First Row");
		}
	}

	public void verifyFirstRowDeleted(int prevRowCount) {
		try {
			boolean flag = false;
			common.explicitWait(3);
			List<WebElement> rows = common.getElements(maintenancePlanningPageObj.canvasRows);
			if (rows.size() == prevRowCount)
				flag = true;
			if (flag)
				new ReportUtil().logPass("Flight row should be deleted", "Flight Row is deleted");
			else
				new ReportUtil().logFail("", "Flight Row is not deleted");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to identify Any row ");
		}
	}

	public void verifyFlightInfoUnderInfoTab(String color, String opName) {
		try {
			SimpleDateFormat formatter;
			String currDate, currdat;
			if (System.getProperty("appLanguage").equalsIgnoreCase("french")) {
				formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date = new Date();
				currDate = formatter.format(date);
				currdat = currDate.split("/")[0];
			} else {
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date date = new Date();
				currDate = formatter.format(date);
				currdat = currDate.split("/")[1];
			}
			By infoLoc = By.xpath("//aw-flight-schedule-form//*[contains(text(),' Informations')]/../../following-sibling::div//div[contains(text(),'" + currdat + "')]/.." +
					"/following-sibling::div//td");
			By InfoLocColor = By.xpath("//aw-flight-schedule-form//*[contains(text(),' Informations')]/../../following-sibling::div//div[contains(text(),'" + currdat + "')]/.." +
					"/following-sibling::div//td/..");

			validateOperator(infoLoc, currdat, opName);
			checkbubbleBackgroundColor(InfoLocColor, "Flight info under information tab", color);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify flight information in Information tab ");
		}
	}

	public void validateOperator(By loc, String currdat, String op) {
		try {
			op = prop.readPropertyData(op.trim().replaceAll(" ", "_"));
			if (common.getElement(loc).getText().trim().contains(op.trim())) {
				new ReportUtil().logPass("Flight operator " + op + " should be present",
						"Flight operator " + op + " is present");
			} else
				new ReportUtil().logFail("Flight operator " + op + " should be present",
						"Flight operator " + op + " is not present");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Flight Operator under information tab " + op);
		}
	}

	public int getKeyValueFromTooltip(String key) {
		key = prop.readPropertyData(key.trim().replaceAll(" ", "_"));
		By loc = maintenancePlanningPageObj.bubble;
		common.scrollUp();
		Actions action = new Actions(DriverUtil.getInstance().getDriver());
		action.moveToElement(common.getElement(loc)).build().perform();
		By tooltip = By.xpath("//div[contains(@class,'tooltip-content')]//td[contains(text(),'" + key + "')]/following-sibling::td");
		String takeOffTime = common.getElement(tooltip).getText();
		System.out.println(takeOffTime.split(":")[0]);
		return Integer.parseInt(takeOffTime.split(":")[0]);
	}

	public void dragBubbleRight() {
		try {
			By loc = maintenancePlanningPageObj.bubble;
			common.explicitWait(3);
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.dragAndDropBy(common.getElement(loc), 165, 30).build().perform();
			common.explicitWait(2);
			new ReportUtil().logInfo("user dragged bubble to right ");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to drag bubble towards right");
		}
	}

	public void dragBubbleLeft() {
		try {
			By loc = maintenancePlanningPageObj.bubble;
			common.explicitWait(3);
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.dragAndDropBy(common.getElement(loc), -165, 30).build().perform();
			common.explicitWait(2);
			new ReportUtil().logInfo("user dragged bubble to right ");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to drag bubble towards right");
		}
	}

	public void verifyTimeUpdated(int prevTime, String key) {
		try {
			int currTime = getKeyValueFromTooltip(key);
			System.out.println(currTime);
			System.out.println(prevTime);
			if (currTime != prevTime)
				new ReportUtil().logPass(key + " " + "should be updated", key + " " + "is updated");
			else
				new ReportUtil().logFail(key + " " + "should be updated", key + " " + "is not updated");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Time is not updated");
		}
	}

	public void clickOnBubbleIcon() {
		try {
			By loc = maintenancePlanningPageObj.bubble;
			common.clickAnElement(loc, "Bubble Icon");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to find bubble icon");
		}
	}

	public void mouseOver() {
		try {
			By loc = maintenancePlanningPageObj.bubble;
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.moveToElement(common.getElement(loc)).build().perform();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to mouse over bubble icon");
		}
	}

	public void verifyBubbleTooltipValue(String val, String key) {
		try {
			key = prop.readPropertyData(key.trim().replaceAll(" ", "_"));
			val = prop.readPropertyData(val.trim().replaceAll(" ", "_"));
			By loc = maintenancePlanningPageObj.bubble;
			Actions action = new Actions(DriverUtil.getInstance().getDriver());
			action.moveToElement(common.getElement(loc)).build().perform();
			By tooltip = By.xpath("//div[contains(@class,'tooltip-content')]//td[contains(text(),'" + key + "')]/following-sibling::td");
			String value = common.getElement(tooltip).getText();
			if (val.trim().equalsIgnoreCase(value.trim()))
				new ReportUtil().logPass("verify" + " " + key + " " + "value as" + " " + val, "verified" + " " + key + " " + "value as" + " " + val);
			else
				new ReportUtil().logFail("verify" + " " + key + " " + "value as" + " " + val, "Not verified" + " " + key + " " + "value as" + " " + val);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to get value of" + key);
		}
	}

	public void clickOnFlightMissionCode(String code, String name) {
		try {
			By loc = By.xpath("//*[contains(@class,'b-grid-cell')]//*[contains(text(),'" + code + "')]");
			common.clickAnElement(loc, name);
			new ReportUtil().logPass("click on" + code, "clicked on" + code);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to click on Mission code");
		}
	}


	public void verifySectionUnderTab(String tabName, List<String> labels) {
		for (String str : labels) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer
					+ "//*[contains(text(),'" + tabName + "')]/../../following-sibling::div//*[contains(text(),'" + str + "')]"), "Section" + str + "under" + tabName);

		}
	}

	public void verifyRowStatus(String status) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			boolean flag = false;
			String flightStatus = common.getElement(maintenancePlanningPageObj.flightStatusOnRow).getText().trim();
			if (flightStatus.contains(status)) {
				flag = true;
			}
			Assert.assertTrue("Flight status didn't matched", flag);
			new ReportUtil().logPass("Flight status should be " + status, "Flight status is " + status);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to flight status on row");
		}
	}

	public void clickComputeButton(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getElementXpath(name)), 0);
		common.clickAnElement(By.xpath(ObjectUtil.getElementXpath(name)), name);
		//common.getElementExplicitWait(maintenancePlanningPageObj.computeloadingIcon,1);
	}

	public void verifyAircraftSearchResultsDisplayed(String tableHeader) {
		try {
			tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));

			int results = common.getElements(By.xpath("//*[contains(text(),'" + tableHeader + "')]/../../following-sibling::div//div[contains(@class,'table-body')]//div[contains(@class,'row')]")).size();
			if (results > 0) {
				new ReportUtil().logPass("Search results should display under " + tableHeader + " table",
						"Search results are displayed under " + tableHeader + " table" + "--count=" + results);
			} else
				new ReportUtil().logFail("Search results should display under " + tableHeader + " table",
						"Search results are not displayed under " + tableHeader + " table");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results under table " + tableHeader);
		}
	}

	public String clickonAIS(String tableName) {
		String aisValue = null;
		try {
			int colIndex = 0;
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
			By ele = By.xpath("//*[contains(text(),'" + tableName + "')]/../../following-sibling::div//div[contains(@class,'table-header')]//div[contains(@class,'row')]/div");
			int size = common.getElements(ele).size();
			List<WebElement> wbEle = common.getElements(ele);
			for (int i = 0; i < size; i++) {
				if (wbEle.get(i).getText().contains("AIS")) {
					colIndex = i + 1;
					break;
				}
			}
			By element = By.xpath("//div[contains(@class,'table-body')]//div[contains(@class,'row')]//div[" + colIndex + "]//a");
			for (int j = 0; j < common.getElements(element).size(); j++) {
				System.out.println(common.getElements(element).get(j).getText());
				if (common.getElements(element).get(j).getText().length() > 2) {
					aisValue = common.getElements(element).get(j).getText();
					common.click(common.getElement(By.xpath("//a[text()='" + aisValue + "']")), "AIS");
					break;
				}
			}
			new ReportUtil().logPass("Click on AIS value", "Clicked on AIS value");
		} catch (Exception e) {
			new ReportUtil().logFail("Click on AIS value", "Not clicked on AIS value");
		}
		return aisValue;
	}


	public void verifyAISPage(String aisValue) {
		try {
			common.getElementExplicitWait(maintenancePlanningPageObj.pageTitle, 0);
			if (common.getElement(maintenancePlanningPageObj.pageTitle).getText().contains(aisValue))
				new ReportUtil().logPass("verify navigate to " + aisValue + "AIS page", "verified navigated to " + aisValue + "AIS page");
			else
				new ReportUtil().logFail("verify navigate to " + aisValue + "AIS page", "verified not navigated to " + aisValue + "AIS page");
		} catch (Exception e) {
			new ReportUtil().logFail("verify navigate to " + aisValue + "AIS page", "verified not navigated to " + aisValue + "AIS page");
		}
	}

	public String captureAircraftMSNValue() {
		String msnValue = null;
		try {
			msnValue = common.getElement(maintenancePlanningPageObj.aircraftMSN).getText().trim();
			new ReportUtil().logPass("Capture the Aircraft assigned value " + msnValue, "Captured the Aircraft assigned value " + msnValue);
		} catch (Exception e) {
			new ReportUtil().logFail("Capture the Aircraft assigned value ", "Not Captured the Aircraft assigned value");
		}
		return msnValue;
	}

	public void verifyAircraftAsHyperlink(String msnValue) {
		try {
			By ele = By.xpath("//a[contains(text(),'" + msnValue + "')]");
			common.checkElementPresentByVisibility(ele, "Aircraft as hyperlink inside bubble");
			new ReportUtil().logPass("verify aircraft assigned as hyperlink", "verified aircraft assigned as hyperlink");
		} catch (Exception e) {
			new ReportUtil().logFail("verify aircraft assigned as hyperlink", "aircraft is not assigned as hyperlink");
		}
	}


	public void verifyAccessright(String block, String right, String level) {
		By ele = By.xpath("//*[text()='" + block + "']/..//td[text()='" + right + "']/..//td[text()='" + level + "']");
		common.checkElementPresentByVisibility(ele, "Access right");
		new ReportUtil().logPass("verify access right", "verified access right");
	}

	public void verifyPNHyperLinkAppliedConfig() {
		List<WebElement> list = DriverUtil.getInstance().getDriver().findElements(By.xpath("//tbody[@class='p-treetable-tbody']/tr"));
		WebDriver driver = DriverUtil.getInstance().getDriver();
		try {
			for (int i = 1; i < list.size(); i++) {
				if (driver.findElement(maintenancePlanningPageObj.checkButtonVisiblity(i)).getAttribute("style").contains("visibility: hidden")) {
					if (driver.findElement(maintenancePlanningPageObj.checkLink(i)).getAttribute("class").equals("asset ng-star-inserted")) {
						driver.findElement(maintenancePlanningPageObj.checkLinkVisiblity(i)).click();
						break;
					}
				}
			}
		} catch (NoSuchElementException e) {
			System.out.println(e);
		}
	}

	public void enterTodayDatePlusHour(int hour, String name) throws InterruptedException {
		String label = "Today";
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath("//label[normalize-space()='" + name + "']/..//input"), name);
		Thread.sleep(3000);
		for (int i = 0; i < hour; i++) {
			common.click(maintenancePlanningPageObj.calendarHoursIncrementIcon, "Calendar hours increment icon to increment hours");
			Thread.sleep(3000);
		}
//		common.clickJS(By.xpath("//span[contains(text(),'\"+label+\"')]"), name);

	}

	public void clickOnEditFlightSetup() {
		//common.checkElementPresentByVisibility(maintenancePlanningPageObj.editOnFlightSetupPage,"edit button flight setup page");
		common.clickJS(maintenancePlanningPageObj.editOnFlightSetupPage, "edit button flight setup page");
	}

	public void clickOnTargetConfCode() {
		common.clickJS(maintenancePlanningPageObj.targetConfCode, "click on target config code search");
	}

	public void enterOperationConfCode(String value) {
		common.enterSecuredValue(maintenancePlanningPageObj.operationConfCode, value, "Operation config code");
	}

	public void selectResultFromOperationalConfig(String index) {
		common.click(maintenancePlanningPageObj.getSearchResultFromOperationalConfig(index), "searchResultFromOperationalConfig");
	}

	public void verifySVGImageIsUpdated() {
		String actualValue = common.getAttribute(maintenancePlanningPageObj.getSVGImageObject(), "tagname");
		Assert.assertEquals(actualValue, null);
	}

	public void clickOnRandomText(String text) {
		common.clickOnElement(DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getRandomText(text)));
	}

	public void clickonElementImage() {
		common.doubleclickOnElement(DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getChildObject()));
	}

	public void clickonFatherLoad() {
		common.clickOnElement(DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getFatherObject()));
	}

	public void verifyChildisUnselected() {
		String actualValue = common.getAttribute(maintenancePlanningPageObj.getChildObject(), "class");
		Assert.assertFalse(actualValue.contains("selected"));
	}

	public void verifyFatherisSelected(String text) {
		String actualValue = common.getAttribute(maintenancePlanningPageObj.getFatherObject(text), "class");
		Assert.assertTrue(actualValue.contains("selected"));
	}

	//@kouselya methods
	public void enterTextUnderDescriptionTextArea(String value) {
		common.performOperation(maintenancePlanningPageObj.Description, "input", value, "Description");
	}

	public void clickOnButtonUnderDefectDetails(String btnName) {
		common.clickAnElement(By.xpath("//span[contains(text(),'" + btnName + "')]"), btnName);
		//common.clickAnElement(By.xpath("//div/./h3[contains(text(),'Defect Detail')]/parent::div/parent::div/following-sibling::div//span[contains(text(),'"+btnName+"')]"),btnName);
	}

	public void verifyCheckboxIsEnabled(String name) {
		By ele = By.xpath("//label[contains(text(),'" + name + "')]/..//span[contains(@class,'checkbox')]");
		common.performOperation(ele, "checkbox", "yes", name);
	}

	public void verifyFieldIsNotVisible(String fieldName) {
		//common.checkElementNotPresentByVisibility(By.xpath("//label[contains(text(),'"+fieldName+"')]"),fieldName);
		By ele = By.xpath("//label[contains(text(),'" + fieldName + "')]");
		WebElement we = common.getElement(ele);
		if (we == null)
			new ReportUtil().logPass(fieldName + " should not be present", fieldName + " is not present");
		else
			new ReportUtil().logFail(fieldName + " should not be present", fieldName + " is present");
	}

	public void verifyTheFieldIsAutomaticallyFilled(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		By value = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName + "')]/..//span");
		String text = common.getText(value, fieldName);
		if (!text.trim().isEmpty()) {
			new ReportUtil().logPass("Authorized Delay Unit should be auto filled", "Authorized Delay Unit is autofilled as " + text);
		} else {
			new ReportUtil().logFail("Authorized Delay Unit should be auto filled", "Authorized Delay Unit is empty");
		}
	}

	public void verifyTheFieldsAndAreAutomaticallyFilled(String fieldName1, String fieldName2) {
		fieldName1 = prop.readPropertyData(fieldName1.trim().replaceAll(" ", "_"));
		fieldName2 = prop.readPropertyData(fieldName2.trim().replaceAll(" ", "_"));
		By value1 = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName1 + "')]/..//input");
		By value2 = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(.,'" + fieldName2 + "')]/..//span");
		String text1 = common.getAttributeByValue(value1, fieldName1);
		String text2 = common.getText(value2, fieldName2);
		if (!(text1.trim().isEmpty() && text2.trim().isEmpty())) {
			new ReportUtil().logPass("Authorized Delay and Authorized Delay Unit should be auto filled", "Authorized Delay and Authorized Delay Unit is autofilled as " + text1 + text2);
		} else {
			new ReportUtil().logFail("Authorized Delay and Authorized Delay Unit should be auto filled", "Authorized Delay and Authorized Delay Unit is empty");
		}
	}

	public void selectHILTabFromTechnicalStatusSection() {
		common.explicitWait(5);
		common.clickAnElement(maintenancePlanningPageObj.HIL, "HIL");
	}

	public void ClickOnTheEventCodeLink(int index) throws Exception {
		//common.clickAnElement(maintenancePlanningPageObj.Event,"Event");
		try {
			List<WebElement> EventList = common.getElements(maintenancePlanningPageObj.Event);
			String selectedItem = common.getText(EventList.get(index), "Selected item");
			common.explicitWait(5);
			common.click(EventList.get(index), "Event available at index " + index);
			//return selectedItem;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select Event");
			throw new Exception("Unable to select Event");
		}
	}

	static int clicked = 0;

	public void verifyBelowOptionsAreDisplayedUnderDropdown(String option, String dropdownName, int size) throws Exception {
		boolean found = false;

		try {
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));

			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown = By.xpath(ObjectUtil.xpathToTabContainer + "//label[contains(text(),'" + dropdownName
					+ "')]/parent::*//*[contains(@class,'chevron-down')]");

			List<WebElement> elementList = common.getElements(chevronDown);
			WebElement element = elementList.get(0);
			if (clicked == 0) {
				common.clickJS(element, dropdownName);
			}

			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(option)) {
					//common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be Available from dropdown " + dropdownName,
							(option + " option is Available from dropdown " + dropdownName));
					found = true;
					break;
				}
			}
			clicked++;
			if (clicked == size) {
				common.clickJS(element, dropdownName);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "option is not Available from dropdown " + dropdownName);
		}

		if (!found) {
			new ReportUtil().logFail(option + " option should be Available from dropdown " + dropdownName,
					option + " option is not Available from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}

	}

	String FlightNo = "";

	public void clickOnFlightNumber() {
		common.explicitWait(3);
		WebElement element = common.getElement(maintenancePlanningPageObj.FlightNumber);
		FlightNo = element.getText();
		List<WebElement> flights = common.getElements(maintenancePlanningPageObj.Flights);
		if (flights.size() == 1) {
			FlightNo = common.getElement(maintenancePlanningPageObj.Flights).getText();
			common.clickJS(common.getElement(maintenancePlanningPageObj.Flights), "Flight Number");
		}
		common.explicitWait(2);
		common.clickAnElement(element, "Flight Number");
	}

	public void entersValueInField(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("(//label[contains(text(),'Reference')]/..//input)[3]"), "input", value, fieldName);
	}

	public void enterValueInFields(String fieldName, String value) {
		//fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpath(fieldName)), "input", value, fieldName);
	}

	public void naviagteBackToFlightSchedulePage(String page) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		common.clickJS(By.xpath("//a[contains(.,'" + page + " " + "')]"), page + " page");
		WebElement ele = common.getElement(maintenancePlanningPageObj.popup_YesBtn);
		if (ele != null) {
			common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on popup");
		}
		validate_Page_Displayed("Flight Schedule");
	}

	public void ClicksOnMSNOnTheBubbleIcon() {
		try {
			By loc = maintenancePlanningPageObj.bubble_MSN;
			common.clickAnElement(loc, "MSN on Bubble Icon");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Not able to find MSN on bubble icon");
		}
	}

	public void clickTheLineUnderSection(String sectionName) {

		common.click(By.xpath("(//div[contains(@id,'" + sectionName + "')]//child::div/div[contains(@class,'table-body')]//div[contains(@class,'table-row')][not(contains(@class,'hidden'))])[1]/div"), sectionName + "sectionName");
	}

	public void verifyAllLinesOfTheTableFilled() {
		List<WebElement> list = common.getElements(maintenancePlanningPageObj.cellContent);
		boolean flag = false;
		for (WebElement ele : list) {
			String text = ele.getText();
			if (text.trim().isEmpty()) {
				flag = true;
				continue;
			}
		}
		if (flag) {
			new ReportUtil().logPass("The lines of the table are totally filled", "The lines of the table are totally filled");
		} else {
			new ReportUtil().logFail("", "The lines of the table are not totally filled");
		}
	}

	public void selectRadiobuttonOptionFromCriteriaDropdown(String option, String dd) throws Exception {
		boolean flag = false;
		try {
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			//List<WebElement> elementList = common.getElements(By.xpath("//h4[contains(.,'Results')]/../../..//tbody//*[contains(@class,'checkbox')]"));
			List<WebElement> elementList = common.getElements(By.xpath("//h4[contains(.,'Results')]/../../..//tbody//td[2]"));

			for (WebElement ele : elementList) {
				String text = ele.getText();
				if (text.trim().equalsIgnoreCase(option)) {
					String optToSel = ele.getText().trim();
					WebElement ele1 = common.getElement(By.xpath("(//h4[contains(text(),'Results')]/..//tbody//td[contains(text(),'" + optToSel + "')])[1]"));
					common.scrollAndClick(ele1, "");
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
							(option + " option is selected from dropdown " + dd));
					common.explicitWait(1);
					flag = true;
					break;
				}
			}
			if (flag == false) {
				new ReportUtil().logFail("", "Option is not available under dropdown " + dd);
			}
		} catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}
	}

	public void verifyPopupDisplay(String popupName) {
		try {

			WebElement element = common.getElementExplicitWait(maintenancePlanningPageObj.workPackageCreationHeader, 0);
			new ReportUtil().logInfo("User navigated to" + popupName + "Page");

		} catch (Exception e) {
			new ReportUtil().logFail("User navigated to" + popupName + "Page", "User is not navigated to " + popupName + "page");

		}
	}

	public void verifySearchResultDisplayed(String tableHeader) {
		try {
			tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
			//int results = getNumberOfItemsInTable(tableHeader);
			int results = verifyNumberOfItemsInTable(tableHeader);
			if (results > 0) {
				new ReportUtil().logPass("Search results should display under " + tableHeader + " table",
						"Search results are displayed under " + tableHeader + " table" + "--count=" + results);
			} else
				new ReportUtil().logFail("Search results should display under " + tableHeader + " table",
						"Search results are not displayed under " + tableHeader + " table");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results under table " + tableHeader);
		}
	}

	public int verifyNumberOfItemsInTable(String tableHeader) {
		int count = common.getElements(By.xpath(ObjectUtil.getResultTableXpath(tableHeader))).size();
		new ReportUtil().logInfo("Total number of item in " + tableHeader + " are " + count);
		return count;

	}


	public void clickUnderSection(String section, String btn) {
		section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));
		btn = prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		if (common.getElement(By.xpath(ObjectUtil.getElementXpathUnderSection(section, btn))) != null) {
			common.clickJS(By.xpath(ObjectUtil.getElementXpathUnderSection(section, btn)), btn
					+ " under section " + section);
		}
	}

	public void selectResult(String index, String name) {
		int result = 0;
		try {
			result = Integer.parseInt(index);
		} catch (Exception e) {
		}
		try {
			common.explicitWait(1);
			List<WebElement> list = common.getElements(By.xpath("//h3[contains(.,'Results')]/../../following-sibling::div//tbody//tr[contains(@class,'selectable-row')]"));
			//List<WebElement> list = common.getElements(dashboardPageObj.resultsRows);
			System.out.println(list.size());
			System.out.println(list.get(result - 1).getAttribute("class"));
			if (!list.get(result - 1).getAttribute("class").contains("p-highlight"))
				//common.clickJS(list.get(result-1), "Result Check box" + " to select the " + name);
				common.clickAnElement(list.get(result - 1), "Result Check box" + " to select the " + name);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name);
		}
	}

	public void verifyFieldIsEmpty(String fieldName) {
		WebElement element = common.getElement(maintenancePlanningPageObj.configurationCode);
		String ele = element.getText();
		common.explicitWait(1);
		if (ele.isEmpty()) {
			new ReportUtil().logPass("", fieldName + "is Empty");
		} else
			new ReportUtil().logFail("", fieldName + "is not Empty" + ele);
	}

	public void verifyFieldIsFilledWith(String fieldName, String value) {
		common.explicitWait(2);
		WebElement element = common.getElement(maintenancePlanningPageObj.configurationCode);
		if (element.getText().contains(value)) {
			new ReportUtil().logPass("", fieldName + "is Filled with " + value);
		} else
			new ReportUtil().logFail("", fieldName + "is not Filled with" + value);
	}

	public void verifyTheAndFrameIsAvailable(String fieldName, String fieldName1) {
		WebElement ele = common.getElement(By.xpath("//div[contains(@class,'grid-cell-title')]/span[contains(text(),'" + fieldName + "')]"));
		WebElement ele1 = common.getElement(By.xpath("//div[contains(@class,'grid-cell-title')]/span[contains(text(),'" + fieldName1 + "')]"));
		if (ele != null && ele1 != null) {
			new ReportUtil().logPass("", fieldName + "and" + fieldName1 + "Frames are available");
		} else
			new ReportUtil().logFail("", fieldName + "and" + fieldName1 + "Frames are not available");
	}

	public void verifyListOfActions(String fieldName) {
		List<WebElement> element = common.getElements(maintenancePlanningPageObj.actionList);
		if (element.size() > 0) {
			new ReportUtil().logPass("", fieldName + "contains list of action to Execute");
		} else
			new ReportUtil().logFail("", fieldName + "not contains list of action to Execute");
	}

	public void clickOnSearchIconField(String fieldName) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		WebElement searchIcon = common.getElement(maintenancePlanningPageObj.Target_conf_code);
		common.clickAnElement(searchIcon, fieldName + " search icon");
	}

	public void selectOptionFromTable(String option, String dd) throws Exception {
		boolean flag = false;
		boolean found = false;
		int count = 0;
		try {
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));

			do {
				List<WebElement> elementList = common.getElements(By.xpath("//div[contains(text(),'Operational Configuration')]/../..//following-sibling::div//div[contains(@class,'yac-table-body')]//div[contains(@class,'yac-table-cell sortable frozen')]//span"));

				for (WebElement ele : elementList) {
					count++;
					String text = ele.getText();
					common.explicitWait(2);
					if (text.trim().equalsIgnoreCase(option)) {
						String optToSel = ele.getText().trim();
						WebElement ele1 = common.getElement(By.xpath("(//div[contains(text(),'Operational Configuration')]/../..//following-sibling::div//div[contains(@class,'yac-table-body')]//div[contains(@class,'yac-table-cell sortable frozen')]//span[contains(text(),'" + optToSel + "')])[1]"));
//					common.scrollAndClick(ele1,"");
						common.click(ele1, "");
						new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
								(option + " option is selected from dropdown " + dd));
						common.explicitWait(1);
						flag = true;
						found = true;
						break;
					}
				}
				common.explicitWait(2);
				WebElement e = common.getElement(By.xpath("(//div[contains(text(),'Operational Configuration')]/../..//following-sibling::div//div[contains(@class,'yac-table-body')]//div[contains(@class,'yac-table-cell sortable frozen')]//span)[" + count + "]"));
				common.scrollIntoView(e);
			} while (!found);
			if (flag == false) {
				new ReportUtil().logFail("", "Option is not available under dropdown " + dd);
			}
		} catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}
	}

	public void AddItem(String popupName) {
		try {
			String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
			if (popupName.equalsIgnoreCase("search equipment") || popupName.equalsIgnoreCase("Search Fleet") || popupName.equalsIgnoreCase("Search Item")
					|| popupName.equalsIgnoreCase("Search Operation") || popupName.equalsIgnoreCase("Search Mission") || popupName.equalsIgnoreCase("Search Aircraft")) {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[2]"));
				common.clickAnElement(resultItems.get(1), "Item on Search result table");
			} else {
				List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[2]/span"));
				common.clickAnElement(resultItems.get(1), "Item on Search result table");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select item from result table on the popup");
		}
	}

	public void clickOn(String button) {
		common.clickAnElement(maintenancePlanningPageObj.addToWPBtn, button + " button");
	}

	public void unselectAllSelectedCheckBox(String dd) {
		dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
		WebElement chevronDown = DriverUtil.getInstance().getDriver().findElement(By.xpath("//aw-tab-container[@class='ng-star-inserted']//label[contains(text(),'" + dd + "')]/parent::*//*[contains(@class,'chevron-down')]"));
		chevronDown.click();

		WebElement unselectAll = DriverUtil.getInstance().getDriver().findElement(By.xpath("//div[@class='p-multiselect-header ng-tns-c245-128 ng-star-inserted']//div[@role='checkbox']"));
		String ariaChecked = unselectAll.getAttribute("aria-checked");
		if (ariaChecked == "true") {
			unselectAll.click();
		}

	}

	public void selectAllSelectedCheckBox(String dd) {
		dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));

		WebElement unselectAll = DriverUtil.getInstance().getDriver().findElement(By.xpath("//div[@class='p-multiselect-header ng-tns-c245-128 ng-star-inserted']//div[@role='checkbox']"));
		String ariaChecked = unselectAll.getAttribute("aria-checked");
		if (ariaChecked == "false") {
			unselectAll.click();
		}

	}


	public void NavigateBackToWorkPackagePage(String page) {
		common.explicitWait(2);
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		common.clickJS(By.xpath("//a[contains(.,'" + page + " " + "')]"), page + " page");
		validate_Page_Displayed("Work Package");
	}

	public void clickAddButtonUnderDefectTable() {
		common.clickJS(maintenancePlanningPageObj.defectAdd, "Add");
	}

	public void clickOnExpandCriteriaButton() {
		common.clickAnElement(maintenancePlanningPageObj.ExpandCriteria, "Expand Criteria");
	}

	public void navigateBackPage(String page) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		List<WebElement> breadCrumb = common.getElements(By.xpath("//a[contains(.,'" + page + " " + "')]"));
		if (breadCrumb.size() == 2) {
			WebElement ele = common.getElement(By.xpath((breadCrumb.get(1)) + "[1]"));
			common.clickJS(ele, page + " page");
		} else
			common.clickJS(breadCrumb.get(0), page + " page");
		common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on popup");
		new MaintenancePlanning().validate_Page_Displayed(page);

	}

	public void selectResultFromSearchMissionPopup(String index) {
		common.clickJS(maintenancePlanningPageObj.getSearchMissionPopupRow(index), "searchResultFromMissionPopup");
	}

	public void verifyAISUpdatedStatus(String statusValue) {
		Assert.assertTrue(DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getAISStatus()).getText().contains(statusValue));
	}

	public void clickOnWorkOrderLink() {
		common.click(maintenancePlanningPageObj.getWorkOrderLink(), "click on work order link");
	}

	public void clickOnButtonFromTheDropDown(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.click(maintenancePlanningPageObj.clickChevronDownButton(), "click on chevron down button");
		common.getElementExplicitWait(maintenancePlanningPageObj.clickRemoveByRegularizationButton(name));
		common.clickJS(maintenancePlanningPageObj.clickRemoveByRegularizationButton(name), "click on Remove by Regularization button");
	}

	public void verifyUpdatedStatus(String statusValue) {
		String status = DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getStatusValue()).getText();
		Assert.assertTrue(status.contains(statusValue));
	}

	public void verifyUpdatedNewOperationalStatus(String dd, String statusValue) {
		dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
		String status = DriverUtil.getInstance().getDriver().findElement(maintenancePlanningPageObj.getNewOperationalStatusValue()).getText();
		System.out.println(status + " " + "status is");
		Assert.assertTrue(status.contains(statusValue));
	}

	public void selectTheFromSearchResult() {
		try {
			common.explicitWait(2);
			List<WebElement> elements = common.getElements(By.xpath("//h3[contains(.,'Results')]/../../following-sibling::div//div[contains(@class,'table-body')]//a"));
			for (WebElement ele : elements) {
				String text = ele.getText();
				if (text.contains(FlightNo)) {
					WebElement FlightToSel = common.getElement(By.xpath("//h3[contains(.,'Results')]/../../following-sibling::div//div[contains(@class,'table-body')]//a[contains(text(),'" + FlightNo + "')]/../../.."));
					common.click(FlightToSel, "Result Check box" + " to select the " + FlightNo);
					break;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + FlightNo);
		}
	}

	public void uncheckCheckbox(String fieldName) {
		By ele = By.xpath("(//label[contains(text(),'"+ fieldName +"')]/..//span[contains(@class,'checkbox')])[1]");
		//common.click(ele,"Uncheck");
		common.performOperation(ele, "checkbox", "no", fieldName);
	}

	public void selectCILTabFromTechnicalStatusSection() {
		common.explicitWait(5);
		common.clickAnElement(maintenancePlanningPageObj.CIL, "CIL");
	}

	public void verifyIsFilledWith(String fieldName, String value) {
		common.explicitWait(2);
		WebElement element = common.getElement(maintenancePlanningPageObj.configurationCodeOCI);
		if (element.getText().contains(value)) {
			new ReportUtil().logPass("", fieldName + "is Filled with " + value);
		} else
			new ReportUtil().logFail("", fieldName + "is not Filled with" + value);
	}


	public void verifyTheGeneralInformationOfTheAIS(List<String> data) {
		boolean found = false;
		List<WebElement> buttonList = common.getElements(By.xpath("//div[contains(@id,'generalInformation')]//span[1]"));
		List<WebElement> customBtnList = common.getElements(By.xpath("//div[contains(@id,'generalInformation')]//div[contains(@class,'custom-btn')]"));

		for (String info : data) {
			if (!info.contains("Time Stamp")) {
				for (WebElement button : buttonList) {
					if (button.getText().contains(info)) {
						new ReportUtil().logPass("", info + "tile is present on General Information Of the AIS");
						found = true;
						break;
					}
				}
				if (!found) {
					for (WebElement btn : customBtnList) {
						if (btn.getText().contains(info)) {
							new ReportUtil().logPass("", info + "tile is present on General Information Of the AIS");
							found = true;
							break;
						}
					}
				}
				if (!found) {
					new ReportUtil().logFail("", info + "tile is not present on General Information Of the AIS");
				}
			}
			found = false;
		}
	}

	public static String statusOfACRS, statusOfAVAIL, statusOfGO;

	public void captureTheStatusOfBelowFields(List<String> data) {
		WebElement ele1 = common.getElement(maintenancePlanningPageObj.AvailStatus);
		statusOfAVAIL = ele1.getText();
		WebElement ele2 = common.getElement(maintenancePlanningPageObj.ACRSStatus);
		statusOfACRS = ele2.getText();
		WebElement ele3 = common.getElement(maintenancePlanningPageObj.GoStatus);
		statusOfGO = ele3.getText();
	}

	public void userComparesGeneralInfotilesWithCapturedStatusInfo() {
		String className;
		if (statusOfAVAIL.contains("NOK")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.AvailButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-red"))
				new ReportUtil().logPass("", "Status of AVAIL as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of AVAIL not as per ACRS/Go-Nogo Tab");
		}
		if (statusOfAVAIL.equalsIgnoreCase("OK")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.AvailButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-green"))
				new ReportUtil().logPass("", "Status of AVAIL as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of AVAIL not as per ACRS/Go-Nogo Tab");
		}

		if (statusOfACRS.contains("Force final ACRS") || statusOfACRS.contains("Invalidate final ACRS")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.ACRSButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-red"))
				new ReportUtil().logPass("", "Status of ACRS as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of ACRS not as per ACRS/Go-Nogo Tab");
		}
		if (statusOfACRS.equalsIgnoreCase("Forced")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.ACRSButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-green"))
				new ReportUtil().logPass("", "Status of ACRS as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of ACRS not as per ACRS/Go-Nogo Tab");
		}
		if (statusOfGO.contains("NoGo")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.GoButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-red"))
				new ReportUtil().logPass("", "Status of GO as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of GO not as per ACRS/Go-Nogo Tab");
		}
		if (statusOfGO.equalsIgnoreCase("Go")) {
			WebElement ele = common.getElement(maintenancePlanningPageObj.GoButton);
			className = ele.getAttribute("class");
			if (className.contains("bg-green"))
				new ReportUtil().logPass("", "Status of GO as per ACRS/Go-Nogo Tab");
			else
				new ReportUtil().logFail("", "Status of GO not as per ACRS/Go-Nogo Tab");
		}
	}
	public void userClickOnTile(String tileName) {
		WebElement element = common.getElement(By.xpath("(//div[contains(@id,'generalInformation')]//*[contains(text(),'" + tileName + "')])[1]"));
		common.clickAnElement(element, "TileName" + tileName);
	}

	public void selectWO() throws Exception {
		try {
			List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.selectWO);
			common.clickAnElement(listOfTo.get(listOfTo.size()), "Work Oder " + listOfTo.get(listOfTo.size()).getText());
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select the Work Order");
			throw new Exception("Unable to select the Work Order");
		}
	}

	public void checkResultSearch(String page) {
		List<WebElement> list = common.getElements(dashboardPageObj.ResultsRows);
		WebElement results = common.getElement(maintenancePlanningPageObj.resultsCount);
		String extractedValue = CompactUtil.extractNumber(results.getText());
		if (extractedValue != null && extractedValue != "0")
			new ReportUtil().logInfo("Search Result are available under page " + page);
		else
			new ReportUtil().logInfo("No Search Results available under page " + page);
	}

	public void openWorkPackageFromSearch(int index) {
		try {
			List<WebElement> wpList = common.getElements(dashboardPageObj.ResultsRows);
			common.explicitWait(2);
			common.click(wpList.get(index - 1), "select from search");
			common.explicitWait(2);
			common.clickJS(maintenancePlanningPageObj.resultOpenButton, "Open Button");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to open work package");
			Assert.assertTrue("Unable to open work package", false);

		}
	}
	public int validateWorkOrderCount() {
		String count = common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order Count");
		count = CompactUtil.extractNumber(count);
		int previousCount=Integer.parseInt(count) ;
		if (previousCount == 0) {
			new ReportUtil().logInfo("No work order is available for selected  work package");
			return 0;
		} else {
				new ReportUtil().logInfo("Work order is present in the work package");
			return previousCount;
		}

	}

	public void verifyNewWOIsAddedToWorkPackage(int numberOfWOPresent) {
		String count = common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order Count");
		count = CompactUtil.extractNumber(count);
		int totalCount=Integer.parseInt(count);
		if (totalCount == 0) {
			new ReportUtil().logInfo("No work order is available for selected  work package");
		}
//		List<WebElement> listOfTo = common.getElements(maintenancePlanningPageObj.nameOfTO);
//		System.out.println("count" + listOfTo.size() + numberOfWO);
		if (totalCount == numberOfWOPresent+1) {
			new ReportUtil().logPass("Work order should be added to the list",
					"Work order is added to the list");
		} else {
			new ReportUtil().logFail("Work order should be added to the list",
					"Work order is not added to the list");
		}
	}
	public String ClickEventCode(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.clickEventCode);
			String selectedItem = common.getText(receiptList.get(index), "Selected item");
			common.clickAnElement(receiptList.get(index), "Event available at index " + index);
			return selectedItem;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select Event");
			throw new Exception("Unable to select Event");
		}
	}

	public boolean validateSearchFilterInResultsRows(String columnName, String tableName, String filter) {
		String rawColumn = columnName;
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		//filter = prop.readPropertyData(filter.trim().replaceAll(" ", "_"));
		int columnIndex = new LogisticsModule().findColumnIndexFromTable(tableName, columnName);
		int row = 1;
		boolean flag = true;
		try {
			By loc;
			loc = By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getTotalRowsXpath(tableName, columnIndex));

			List<WebElement> ele = common.getElements(loc);
			if (ele.size() == 0) {
				new ReportUtil().logInfo("There are no search results available with entered criteria");
				return false;
			}
			for (WebElement element : ele) {
				if (!element.getText().trim().equalsIgnoreCase(filter)) {
					new ReportUtil().logFail(
							"Search results should be correct for column " + columnName + " according to filter "
									+ filter,
							"Search results are not correct for column " + columnName + " according to filter " + filter
									+ " -->At row " + row);
					flag = false;
					break;
				}
				row++;
			}
			if (flag == true) {
				new ReportUtil().logPass(
						"Search results should be correct for column " + columnName + " according to filter " + filter,
						"Search results are correct for column " + columnName + " according to filter " + filter);
				return true;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results for filter " + filter);
		}
		return false;
	}

	public void verifyCapturedInformationsOnWOScreen(Map<String, String> woDetails)  {
		for (Map.Entry<String, String> str : woDetails.entrySet()) {
			if (str.getKey().contains("Atelier") || str.getKey().contains("Workshop")) {
				String[] wsArray = str.getValue().split(" ", 2);
				wsArray[1] = wsArray[1].replaceFirst("-", "");
				WebElement ele = common.getElement(maintenancePlanningPageObj.WorkCenterWOPage);
				if (ele.getAttribute("value").trim().contains(wsArray[1].trim()))
					new ReportUtil().logPass(str.getKey() + " as " + wsArray[1] + " should be available",
							str.getKey() + " as " + wsArray[1] + " is available");
				else {
					new ReportUtil().logFail(str.getKey() + " as " + wsArray[1] + " should be available", str.getKey()
							+ " as " + wsArray[1] + " is not available-->curent value is " + ele.getAttribute("value"));
				}
			} else
				common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'" + str.getValue() + "')]"),
						str.getKey() + " as " + str.getValue());
		}
	}

	public void checkResultCounter() {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.ResultsCount);
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (extractedValue != null && extractedValue != "0")
				new ReportUtil().logInfo("Search Result are available");
			else
				new ReportUtil().logInfo("No Search Results available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void validate_RecentCreatedWOStatus(String status, String tableName, String column) {
		try {
			status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
			List<WebElement> listOfWO = common.getElements(maintenancePlanningPageObj.nameOfTO);
			int columnIndex = getColumnIndexFromTable(tableName, column);
			By loc = By.xpath("//div[@class='yac-table-cell']//span[contains(text(),'"+status+"')]");
			List<WebElement> listOfTo = common.getElements(loc);

			if (listOfTo.get(listOfTo.size() - 1).getText().equalsIgnoreCase(status)) {
				new ReportUtil().logPass(
						"Status of Work Order should be " + status,
						"Status of Work Order is " + status);
			} else {
				new ReportUtil().logFail(
						"Status of Work Order should be " + status,
						"Status of Work Order is not " + status);
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate WP status and Work Order are same");
		}

	}
	public void validateWOFilteredAsPerAsset(String sn, String asset, String filterType) {
		try {
			sn = prop.readPropertyData(sn.trim().replaceAll(" ", "_"));
			filterType = prop.readPropertyData(filterType.trim().replaceAll(" ", "_"));
			String woCount = CompactUtil
					.extractNumber(common.getText(maintenancePlanningPageObj.workOrders_Table, "Work Order"));
			int rowCount=Integer.parseInt(woCount);
			int colCount=getColumnPosition(sn);
			if (rowCount > 0) {
				int flag=0;
				for (int i=1;i<=rowCount;i++){
					//System.out.println(common.getElement(By.xpath("(//div[@class='yac-table-body resizable']//div[@class='yac-table-row'])["+i+"]/div["+colCount+"]")).getText());
					Assert.assertTrue(common.getElement(By.xpath("(//div[@class='yac-table-body resizable']//div[@class='yac-table-row'])["+i+"]/div["+colCount+"]")).getText().trim().equalsIgnoreCase(asset));
					flag=flag+1;
				}
				if (flag>0)
					new ReportUtil().logPass("All the work order should get filter as per the " + filterType + " " + asset,
							"Work orders filtered correctly");
				else
					new ReportUtil().logFail("All the work order should get filter as per the " + filterType + " " + asset,
							"Work orders filtered uncorrectly");
			} else {
				new ReportUtil().logInfo("There are no work orders available having asset " + filterType + " " + asset);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate Work Order for filter " + filterType);
		}}

	public int getColumnPosition(String column) {
		int position = 1;
		List<WebElement> colHeaders = common.getElements(By.xpath("(//div[@class='yac-table-header resizable']/div)[1]/div/div[@class='yac-table-cell-content left']"));
		for (WebElement header : colHeaders) {
			if (header.getText().trim().equalsIgnoreCase(column))
				break;
			position++;
		}
		return position;
	}
	public void deleteAllResults(String tableName){
		try {
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
			//WebElement resultCount=common.getElement(By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]/../../following-sibling::div//span"));
			WebElement resultCount=common.getElement(By.xpath("(//aw-tab-container[@class='ng-star-inserted']//h3[contains(.,'"+ tableName +"')]/../../following-sibling::div//span)[3]"));
			String extractedValue = CompactUtil.extractNumber(resultCount.getText().replaceAll("\\(","").trim());

			if (Integer.parseInt(extractedValue)>=1) {
				common.clickAnElement(maintenancePlanningPageObj.tableMenuOption,"Table menu option");
				common.clickAnElement(maintenancePlanningPageObj.selectAllOpt,"Select All option");
				new LogisticsModule().clickOnBtn("delete");
				new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
			}else {
				new ReportUtil().logInfo("No Flight exist in tabel "+tableName+" to select");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select flights from " + tableName + " table");
		}
	}
	public void checkZeroResultsAreFound(String page) {
		try {
			WebElement results = common.getElement(maintenancePlanningPageObj.WorkPackageresultsCount);
			String extractedValue = CompactUtil.extractNumber(results.getText().trim());
			if (Integer.parseInt(extractedValue) == 0)
				new ReportUtil().logPass("Search Result should not be available on page " + page, "Search Result are not available");
			else
				new ReportUtil().logFail("Search Result should not be available on page " + page, "Search Result are available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate search results under page " + page);
		}

	}

	public void checkNoResultInSearch(String  page) {
		List<WebElement> list = common.getElements(maintenancePlanningPageObj.resultsCount);
		String extractedValue = CompactUtil.extractNumber(list.get(0).getText());
		if (extractedValue != null && extractedValue.contains("0"))
			new ReportUtil().logInfo("No Search Results available under page " + page);
		else
			new ReportUtil().logInfo("Search Result are available under page " + page);
	}

	public void selectAMG(String arg1){
		common.explicitWait(2);
		boolean found=false;
		common.performOperation(By.xpath("//aw-tab-container[not(contains(@class,'display--none'))]//input[contains(@placeholder,'Search in table')]"),"input",arg1,"search");
		common.explicitWait(3);
		List<WebElement> AMGDefectList = common.getElements(maintenancePlanningPageObj.getDefectList(arg1));
		int i=1;
		try {
			for (WebElement ele : AMGDefectList) {
				common.explicitWait(1);
				if (ele.getText().isEmpty()) {
					common.explicitWait(2);
					WebElement row = common.getElement(maintenancePlanningPageObj.getDefectRow(arg1,i));
					common.explicitWait(2);
					common.click(row, "Select AMG");
					found = true;
					break;
				}
				++i;
			}
			if (!found) {
				new ReportUtil().logFail("", "AMG Defect Not found in Upcoming Maintenance Operation");
			}
		}
		catch (Exception e){
			new ReportUtil().logFail("", "unable to select AMG Defect in Upcoming Maintenance Operation");
		}
		common.performOperation(By.xpath("//aw-tab-container[not(contains(@class,'display--none'))]//input[contains(@placeholder,'Search in table')]"),"input","","search");
	}

	public void selectDefectOpen(String arg0) {
		common.explicitWait(2);
		boolean found=false;
		List<WebElement> DefectList = common.getElements(maintenancePlanningPageObj.getDefectList(arg0));
		int i=1;
		try {
			for (WebElement ele : DefectList) {
				//String wp=ele.getText();
				common.explicitWait(1);
				if (ele.getText().isEmpty()){
					common.explicitWait(2);
					WebElement row = common.getElement(maintenancePlanningPageObj.getDefectRow(arg0,i));
					common.explicitWait(2);
					common.click(row, "Select Defect");
					found = true;
					break;
				}
				++i;
			}
			if (!found) {
				new ReportUtil().logFail("", " Defect Not found in Upcoming Maintenance Operation");
			}
		}
		catch (Exception e){
			new ReportUtil().logFail("", "unable to select Defect in Upcoming Maintenance Operation");
		}
	}

	public void ClickBreadcrumb(String page) {
		List<WebElement> breadCrumb=common.getElements(By.xpath("//a[contains(.,'"+page+"')]"));
		common.clickJS(breadCrumb.get(0),"Page"+page);
	}

	public void ClickOnTheEventCodeLinkUnderTheHILSection() {
		List<WebElement> eventCodeList=common.getElements(maintenancePlanningPageObj.EventCodeOnHIL);
		common.click(eventCodeList.get(eventCodeList.size()-1),"EventCode");
	}

	public void ClickOnTheEventCodeLinkUnderTheCILSection() {
		List<WebElement> eventCodeList=common.getElements(maintenancePlanningPageObj.EventCodeOnCIL);
		common.click(eventCodeList.get(eventCodeList.size()-1),"EventCode");
	}

	public void userUncheckCheckbox(String fieldName) {
		By ele = By.xpath("(//label[contains(text(),'"+ fieldName +"')]/..//span[contains(@class,'checkbox')])[2]");
		common.click(ele,"Uncheck");
		//common.performOperation(ele, "checkbox", "no", fieldName);
	}

	public void verifyTheColumnOfTaskWhichHaveValueInColumn(String arg0, String arg1) {
		List<WebElement> NextTermList = common.getElements(By.xpath("//aw-tab-container[not(contains(@class,'display--none'))]//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//div[7]//span[contains(text(),'/')]"));
		int i=1;
		common.explicitWait(3);
		for(WebElement ele:NextTermList){
				common.explicitWait(2);
				WebElement DefectOpen=common.getElement(By.xpath("(//aw-tab-container[not(contains(@class,'display--none'))]//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//div[7]//span[contains(text(),'/')]/../../..//div[@class='yac-table-cell-content left']//div[2]/span)["+i+"]"));
				if(DefectOpen.getText().contains("Open")){
					++i;
					continue;
				}else{
					common.explicitWait(2);
					WebElement PeriodicityColumn = common.getElement(By.xpath("(//aw-tab-container[not(contains(@class,'display--none'))]//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//div[7]//span[contains(text(),'/')]/../../..//div[9]//span)["+i+"]"));
					if(!PeriodicityColumn.getText().isEmpty()){
						new ReportUtil().logPass("",arg0+"and"+arg1+"both columns having value");
					}
					else{
						new ReportUtil().logFail("",arg1+"column not having value");
					}
				}
				++i;
		}
//		List<WebElement> PeriodicityList = common.getElements(By.xpath("//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//div[9]//span"));
//		List<WebElement> DefectOpenList = common.getElements(By.xpath("//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//div[7]//span/../../..//div//span[contains(text(),'Open')]"));
	}

	public void verifyTheUpcomingMaintenanceOperationsTablefilter(String wpNum) {
		WebElement element = common.getElement(By.xpath("//aw-tab-container[not(contains(@class,'display--none'))]//div[contains(@class,'yac-table-body')]//div[@class='yac-table-row']//a[contains(text(),'"+wpNum+"')]"));
		if(element != null){
				new ReportUtil().logPass("","Upcoming Maintenance Operation Table is filtered according to the filter");
		}
		else {
			    new ReportUtil().logFail("","Upcoming Maintenance Operation Table is not filtered according to the filter");
		}
	}

	public String CapturePackageNumber(String index) {
		List<WebElement> wpNumberList = common.getElements(By.xpath("//aw-tab-container[@class='ng-star-inserted']//div[contains(@class,'table-body')]//div[contains(@class,'right')]/a"));
		String wpNumber = wpNumberList.get(Integer.parseInt(index) - 1).getText();
		new ReportUtil().logInfo("Work page selected--> " + wpNumber);

		return wpNumber;
	}

	public void enterCriteriaInAllWorkPackagePage(String fieldName, String textToEnter) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("(//aw-tab-container[not(contains(@class,'display--none'))]//label[contains(text(),'"+fieldName+"')]/..//input)[1]"), "input", textToEnter, fieldName);
	}
}
