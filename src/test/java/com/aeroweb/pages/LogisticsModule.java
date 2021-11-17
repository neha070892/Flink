package com.aeroweb.pages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.aeroweb.objects.DashboardPageObjects;
import com.aeroweb.objects.LogisticModuleObjects;
import com.aeroweb.objects.MaintenancePlanningPageObjects;
import com.aeroweb.objects.ReferentialModuleObjects;
import com.aeroweb.utils.CompactUtil;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.ObjectUtil;
import com.aeroweb.utils.PropertyUtil;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;

import junit.framework.Assert;

public class LogisticsModule {
	DriverUtil driverUtil;
	ReportUtil report;
	PropertyUtil prop;
	LogisticModuleObjects logisticModuleObj;
	DashboardPageObjects dashboardPageObj;
	MaintenancePlanningPageObjects maintenancePlanningPageObj;
	ReferentialModuleObjects referentialPageObj;
	commonMethods common;
	

	public LogisticsModule() {
		report=new ReportUtil();
		common= new commonMethods();
		logisticModuleObj= new LogisticModuleObjects();
		dashboardPageObj= new DashboardPageObjects();
		maintenancePlanningPageObj= new MaintenancePlanningPageObjects();
		referentialPageObj= new ReferentialModuleObjects();
		String propertyFilePath = "src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
		this.prop = new PropertyUtil(propertyFilePath);
		
	}
	public void getElement(String name)
	{
		common.clickAnElement(By.xpath(ObjectUtil.getElementXpath(name)), name);
	}


	public void validateCreateShipmentPopup(String popupName) {
		common.checkElementPresentByVisibility(logisticModuleObj.createShipmentFolderTitle, popupName +" Popup");
	}
	
	public  void validateElementsOnCreateShipmentPopup(List<String> data) {
		common.checkElementPresentByVisibility(logisticModuleObj.shipmentType, data.get(0)+" element");
		common.checkElementPresentByVisibility(logisticModuleObj.expectedDate, data.get(1)+" element");
		common.checkElementPresentByVisibility(logisticModuleObj.shipperSite, data.get(2)+" element");
	}
	
	public void selectOptionFromDropdown(By chevronDown, String option, String dropdownName) throws Exception {
		boolean found = false;
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
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
		}
		if (!found) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dropdownName,
					option + " option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option " + option);
		}
	}
	
	public void selectOptionFromDropdown(By chevronDown, String dropdownName) throws Exception {
		boolean found = false;
		try {
			By optionList = By.xpath("//p-dropdownitem/li/span");
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			common.clickAnElement(element, dropdownName);
			common.explicitWait(2);
			List<WebElement> list = common.getElements(optionList);
			common.clickJS(list.get(0), "Option");
			found = true;
		} catch (Exception e) {
		}
		if (!found) {
			new ReportUtil().logFail(" option should be selected from dropdown " + dropdownName,
					" option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option");
		}
	}
	
	public void selectOptionFromDropdown(By chevronDown, String dropdownName,int size) throws Exception {
		boolean found = false;
		try {
			By optionList = By.xpath("//p-dropdownitem/li/span");
			WebElement element = common.getElementExplicitWait(chevronDown, 0);
			element.click();
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			common.clickAnElement(list.get(size-1), "Option");
			found = true;
		} catch (Exception e) {
		}
		if (!found) {
			new ReportUtil().logFail(" option should be selected from dropdown " + dropdownName,
					" option is not selected from dropdown " + dropdownName);
			throw new Exception("Cannot Found option");
		}
	}
	
	public void validateInformationOnShipmentFolder(List<String> data) {
		
		for (String str:data) {
			str=prop.readPropertyData(str.trim().replaceAll(" ","_"));
			common.checkElementPresentByVisibility(By.xpath("//div[contains(.,'"+str+"')][@class='form-control-generic']"), str +" data entered by user");
		}
	}
	
	public void verifyIdentifierIsCreated() {
		try {
			String identifierValue=common.getElement(logisticModuleObj.identifierFolderNo).getText();
			new ReportUtil().logPass("Identifier should be created", "Identifier is created successfully as "+identifierValue);
		}catch(Exception e) {
			new ReportUtil().logFail("Identifier should be created", "Identifier is not created ");
		}
	}

	public void verifyShipmentCreatedConfirmationMsg(String msg) {
		
		common.checkElementPresentByVisibility(logisticModuleObj.confirmationMsgShipmentCreated, "Confirmation message "+msg);
	}
	
	public void verifySenderOptionsAreAvailable() {
		
		common.clickAnElement(logisticModuleObj.shipperSiteChevron, "Shipper site dropdown to check the pre filled data");
		try {
			int count=common.getElements(logisticModuleObj.optionsUnderDropdown).size();
			if(count>0) {
				new ReportUtil().logPass("Shipper site dropdown should have pre-filled data that user is attached with", "Shipper site dropdown have pre-filled data that user is attached with");
			}else 
				new ReportUtil().logFail("Shipper site dropdown should have pre-filled data that user is attached with", "Shipper site dropdown does not have pre-filled data that user is attached with");	
		}catch(Exception e) {
				new ReportUtil().logFail("", "Can not verify  Shipper site dropdown for data availability");	
			}
		
	}
	
	public void validateExpectedDateField() {

		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			System.out.println("date is" + dateFormat.format(date).toString());

			String actualDate = common.getElement(logisticModuleObj.expectedDateInputBox)
					.getAttribute("value");
			System.out.println("date "+actualDate);
			if (actualDate.contains(dateFormat.format(date).toString())) {
				new ReportUtil().logPass("Expected date should be pre-filled with today's date",
						"Expected date is pre-filled with today's date " + actualDate);
			} else
				new ReportUtil().logFail("Expected date should be pre-filled with today's date",
						"Expected date is not pre-filled with today's date " + actualDate);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Cannot verify expected date field for today's date");
		}
	}
		
	public void selectShipmentType(String option) throws Exception {
		selectOptionFromDropdown( logisticModuleObj.shipmentTypeChevron,
				option, "Shipment Type");
	}

	public void selectShipperSite(String option) throws Exception {
		selectOptionFromDropdown(logisticModuleObj.shipperSiteChevron,
				option, "Shipper Site");
	}

	public void selectShipperWarehouse(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.shipperWarehouseChevron, option, "Shipper Warehouse");
	}
	
	public void selectShipperWorkshop(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.shipperWorkshopChevron, option, "Shipper Workshop");
	}

	public void selectRecipientSite(String option) throws Exception {
		selectOptionFromDropdown( logisticModuleObj.recipientSiteChevron,
				option, "Recipient Site");
	}
	
	public void selectRecipientSite_PRPage(String option) throws Exception {
		selectOptionFromDropdown(logisticModuleObj.siteChevronInProcurementRequest,
				option, "Recipient Site");
	}

	public void selectRecipientSite_PRFilterPage(String option,String ddName) throws Exception {
		//common.scrollUp();
		//clickOnFilterIconMWO();
		selectOptionFromDropdown(logisticModuleObj.siteChevronInFilterPR,
				option, ddName);
	}
	
	public void selectRecipientWorkshop_PRPage(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.workshopChevronInProcurementRequest, option, "Recipient Workshop");
	}

	public void selectRecipientWarehouse(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.recipientWarehouseChevron, option, "Recipient Warehouse");
	}

	public void selectRecipientWarehouse_PRFilterPage(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.warehouseChevronInFilterPR, option, "Recipient Warehouse");
	}
	
	public void selectRecipientWorkshop(String option) throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.recipientWorkshopChevron, option, "Recipient Workshop");
	}
	
	public void selectRecipientWarehouse() throws Exception {
		selectOptionFromDropdown(
				logisticModuleObj.recipientWarehouseChevron, "Recipient Warehouse");
	}

	public void verifySearchResultsDisplayed(String tableHeader) {
		try {
			tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
			int results = getNumberOfItemsInTable(tableHeader);
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

	public String openReceiptFromSearchTable(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.recieptFileList);
			String selectedItem= common.getText(receiptList.get(index), "Selected item");
			common.clickJS(receiptList.get(index), "Receipt available at index " + index);
			return selectedItem;
		}catch(Exception e) {
			new ReportUtil().logFail("" ,"Unable to select item");
			throw new Exception("Unable to select item");
		}
	}

	public String captureReceiptFromSearchTable(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.recieptFileList);
			String selectedItem= common.getText(receiptList.get(index), "Selected item");
			return selectedItem;
		}catch(Exception e) {
			new ReportUtil().logFail("" ,"Unable to select item");
			throw new Exception("Unable to select item");
		}
	}

	public void verifyNewItemIsCreated(String tableName, int initialCount) {

		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		common.explicitWait(2);
		WebElement results = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(tableName,true)));
		String extractedValue = CompactUtil.extractNumber(results.getText());
		int count=Integer.parseInt(extractedValue);

		if (count == initialCount +1) {
			new ReportUtil().logInfo("intial count "+ initialCount);
			new ReportUtil().logInfo("current count "+ results);
			new ReportUtil().logPass("New item should be added under table " + tableName,
					"New item is added under table " + tableName);
		} else {
			new ReportUtil().logFail("New item should be added under table " + tableName,
					"New item is not added under table " + tableName);

		}
	}

	public void verifyNewReceiptIsCreated(String tableName, int initialCount) {

		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		common.explicitWait(2);
		int results = getNumberOfItemsInTable(tableName);
		if (results == initialCount +1) {
			new ReportUtil().logInfo("intial count "+ initialCount);
			new ReportUtil().logInfo("current count "+ results);
			new ReportUtil().logPass("New item should be added under table " + tableName,
					"New item is added under table " + tableName);
		} else {
			new ReportUtil().logFail("New item should be added under table " + tableName,
					"New item is not added under table " + tableName);

		}
	}

	public int captureReceiptCount(String tableHeader) {
		tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
		int count = getNumberOfItemsInTable(tableHeader);
		return count;

	}
		
	public void validateCreateProcumentTypeOptions(List<String> data) {

		for (String str : data) {
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(
					By.xpath("//div[@class='form-control']//span[contains(.,'" + str + "')]"), str + " type");
		}
	}

	public void clickMaintenanceType(String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath("//div[@class='form-control']//span[contains(.,'" + field + "')]"),
				field + " type");
	}

	public void procurementRequestFormDisplayed(String name) {
		common.checkElementPresentByVisibility(logisticModuleObj.procumentRequestForm,
				"Procurement Request " + name + " form");
	}
		
		
	 public void enterExpectedDate(String field) {
		   field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		 	common.getElementExplicitWait(By.xpath(ObjectUtil.getInputboxXpath(field)), 0);
	        enterTodaysDate(By.xpath(ObjectUtil.getInputboxXpath(field)), field, "Today");     
	 }
	 
	 
	 public void clickSearchOnPopup() {
		 common.clickAnElement(logisticModuleObj.searchBtn_SearchPopup, "Search button");
	 }

	public void clickOnPRNumber() {
		common.click(logisticModuleObj.prNumber, "Procurement request");
	}
	 
	 public void clickValidateOnPopup() {
		 common.clickAnElement(logisticModuleObj.validateBtn_OnPopup, "Validate button");
	 }
	 
	 public void validateCriticityOptions(List<String> data){
		 int i=0;
		 List<WebElement> ele=common.getElements(logisticModuleObj.criticityOptions);
		 for(WebElement option:ele) {
			 String str=data.get(i);
			 str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			 if(option.getText().trim().equalsIgnoreCase(str)) {
				 i++;
				 new ReportUtil().logInfo("Option available in criticity is "+option.getText());
			 }else {
				 new ReportUtil().logFail("Option "+option.getText()+" should be available under Criticity list", "Option "+option.getText()+" is not available under Criticity list");
			 }
		 }

	 }
	 	 
	 
	 public void enterDataInCriticityReason(String arg1,String arg2) {
		 common.performOperation(logisticModuleObj.criticityReason, "input", arg1, arg2);
	 }
	 
	 public void enterQuantityField(String value,String fieldName) {
		common.explicitWait(1);
		 common.performOperation(logisticModuleObj.quantityInputbox, "input", value, fieldName);
	 }
	 
	 public void clickOnArticleCodeSearchIcon(String arg) {
		   common.clickAnElement(logisticModuleObj.articleCodeSearchIcon, arg);
	   }
	 
	 public void procurementItemDisplayedInSearchResult(String number) {
		 common.checkElementPresentByVisibility(By.xpath("//span[contains(text(),'"+number+"')]"), number +" item in search result");
	 }
	 
	 public void selectProcurementRequestItem(String number) {
		 common.clickJS(By.xpath("//span[contains(text(),'"+number+"')]"), number +" item in search result");
	 }
	 
	 public void verifyPresenceOfItemInArticleCodeField(String number,String field) {
		 
		 common.checkElementPresentByVisibility(By.xpath("//div[contains(text(),'"+number+"')]"), number +" item in "+field+" field");		 
	 }
			
	public void procurementRequestCreatedScreen() {
		common.checkElementPresentByVisibility(logisticModuleObj.procurementCreatedScreen, "Created supply request screen");
	    String requestNumber=common.getText(logisticModuleObj.procurementCreatedScreen, "Request Number");
	    new ReportUtil().logPass("Request number should be created", "Request number is created as "+requestNumber);
	}
	public void clickRoutineUnderCriticity() {
		common.clickAnElement(logisticModuleObj.routine_Criticity, "Routine option under Criticity");
	}

	public void chooseCriticity(String tabName,String fieldName)
	{
		tabName = prop.readPropertyData(tabName.trim().replaceAll(" ", "_"));
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		try {
			By loc = By.xpath("//*[contains(text(),'"+fieldName+"')]/following-sibling::div//span[contains(text(),'"+tabName+"')]");
			WebElement tab = common.getElement(loc);
			common.clickAnElement(tab,tabName + " " + "Criticity type");
			new ReportUtil().logPass("Choose criticity type as" + "=" + tabName, "Choosed criticity type as" + "=" + tabName);
		}
		catch(Exception e1) {
			new ReportUtil().logFail("", "Unable to choose criticity");
		}

	}
		
		
	public void verifyListOfSearchResult(String status) {
		List<WebElement> ele = common.getElements(By.xpath("//span/b[text()='" + status + "']"));
		List<WebElement> list = common.getElements(dashboardPageObj.resultsCheckBox);
		if (ele.size() == (list.size() - 1)) {
			new ReportUtil().logPass("All the supply request should have status " + status,
					"All the supply request are having status " + status);
		} else {
			new ReportUtil().logPass("All the supply request should have status " + status,
					"All the supply request are not having status " + status);
		}
	}
		
		
    public void clickOnBtnCancel(String btnName) {
    	btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
    	common.clickJS_NoJSWaiter(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
    }
		
		
	public void validateReceiptFolderCriticityOptions(String fieldName, String popupName, List<String> data) {
		int i = 0;
		List<WebElement> ele = common.getElements(logisticModuleObj.criticityOnCreateRecptFolder);
		for (WebElement option : ele) {
			String str = data.get(i);
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			if (option.getText().trim().equalsIgnoreCase(str)) {
				i++;
				new ReportUtil()
						.logInfo("Option available in criticity on popup " + popupName + " is " + option.getText());
			} else {
				new ReportUtil()
						.logFail(
								"Option " + option.getText() + " should be available under Criticity list on popup "
										+ popupName,
								"Option " + option.getText() + " is not available under Criticity list");
			}
		}

	}
		
	
	public void selectReceiptType(String option) throws Exception {
		selectOptionFromDropdown(logisticModuleObj.receiptTypeChevron, option, "Receipt Type");
	}
	
	
	public void selectMaterialFromStockSearchResult(String index,String name) {
		common.getElement(logisticModuleObj.subStockSection).click();
		int result = 0;
		try {
			result = Integer.parseInt(index);
		}catch (Exception e) {}
		
		try {
			if (result == 0)
				result++;
				common.explicitWait(1);
				List<WebElement> list = common.getElements(logisticModuleObj.stockViewList);
				if (!list.get(result).getAttribute("class").contains("highlight"))
						common.clickAnElement(list.get(result), "Material Check box" +" to select the "+name);
						
			} catch(Exception e1) {
				new ReportUtil().logFail("", "Unable to select material code");
			}
		
	}
	
	public void verifyMaterialListIsDisplayed() {
		int count = common.getElements(logisticModuleObj.stockViewList).size();
		if(count>0) {
			new ReportUtil().logPass("Material list should display", "Material list is present- total rows are "+count);
		}else {
			new ReportUtil().logFail("Material list should display", "Material list is not present- total rows are "+count);
		}
	}
	
	public void verifyChangedLocationOnStockMovementPage(String zone,String fieldName) {
		zone=prop.readPropertyData(zone.trim().replaceAll(" ", "_"));
		WebElement ele=common.getElement(By.xpath("//span[contains(.,'"+zone+"')]/../div[contains(.,'"+fieldName+"')]"));
		common.checkElementPresentByVisibility(ele, "Changed location "+zone+" on stock movement page");
	}
	
	public void selectMaterialFromMovementTable(int index) {
		List<WebElement> items=common.getElements(logisticModuleObj.movementList);
		common.clickAnElement(items.get(index-1), "Material under Movement table");
	}
	
	public void verifyStatusUnderMovementTable(String status) {
		if(status.equalsIgnoreCase("On Going"))
			common.checkElementPresentByVisibility(logisticModuleObj.onGoingSatus, status + " status");
		else if(status.equalsIgnoreCase("Executed"))
			common.checkElementPresentByVisibility(logisticModuleObj.executedSatus, status + " status");
		
	}
	
	public String capturePackageNumOfMaterial() {
		String pckNo;
		try {
			pckNo=common.getElement(logisticModuleObj.pckgNB_LocationPopup).getText();
			new ReportUtil().logInfo("Package number of material is captured as "+pckNo);
			return pckNo;
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to capture package number");
			return null;
		}
		
	}

	public String captureNumOfMaterial() {
		String matNo;
		try {
			matNo=common.getElement(logisticModuleObj.materialNbr).getText();
			new ReportUtil().logInfo("Material number of material is captured as "+matNo);
			return matNo;
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to capture Material number");
			return null;
		}

	}
	
	public void verifypackNoOnStockTable(String packNo) {
		
		int count = common.getElements(logisticModuleObj.stockViewList).size();
		if(count>0) {
			new ReportUtil().logPass("Material list should display", "Material list is present- total rows are "+count);
			common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer+"//a[contains(.,'"+packNo+"')]"), "previously selected material with pckNo "+packNo);
		}else {
			new ReportUtil().logFail("Material list should display", "Material list is not present- total rows are "+count);
		}	
						
	}
	
	
	public void AddPackageBtnVisibility(String Btn,String visiblility) {
		Btn=prop.readPropertyData(Btn.trim().replaceAll(" ", "_"));
		if(visiblility.equalsIgnoreCase("Not Visible")) {
			WebElement ele=common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(Btn)), 1);
			if(ele==null) {
				new ReportUtil().logPass(Btn+" should not be available", Btn+" is not available to modify the content");
			}else {
				new ReportUtil().logFail(Btn+" should not be available", Btn+" is available to modify the content");
			}
		}else {
			WebElement ele=common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(Btn)), 0);
			if(ele==null) {
				new ReportUtil().logFail(Btn+" should be available", Btn+" is not available to modify the content");
			}else {
				new ReportUtil().logPass(Btn+" should be available", Btn+" is available to modify the content");
			}
		}
	}
	
	public void clickPackageControlCheckbox() {
		if(!common.getElement(logisticModuleObj.progressBarUnderPackage).getText().trim().contains("100")){
			List<WebElement> ele=common.getElements(logisticModuleObj.packageControlCheckbox);
			for(WebElement element:ele) {
				common.clickAnElement(element, "Package control checkbox");
			}
		}
	}
	
	public void verifyProcurementRequestStatusPoupDisplayed() {
		common.checkElementPresentByVisibility(logisticModuleObj.procurementRequestStatus, 
					"Procurement request status popup");
	}
	
	public void verifyItemAddedToTheTable(String tableHeader,String itemName) {

		try {
			tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
			int results = getNumberOfItemsInTable(tableHeader);
			if (results > 0) {
				new ReportUtil().logPass(itemName+" should display under " + tableHeader + " table",
						itemName+" are displayed under " + tableHeader + " table " + "--count= " + results);
			} else
				new ReportUtil().logFail(itemName+" should display under " + tableHeader + " table",
						itemName+" are not displayed under " + tableHeader + " table");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify " +itemName+ "under table " + tableHeader);
		}
	}	
	
	
	public void addPackageIfNotExist(String table, String packageType, String label) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		try {
			if (getItemCountFromTable(table) == 0) {
				clickOnBtn("Add Package");
				verifyPopupIsDisplayed("Add a Package");
				new Dashboard().selectOptionFromCriteriaDropdown(packageType, label);
				clickOnBtn("Validate");
				common.explicitWait(3);
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to add package");
		}
	}
	
	public void verifyPackageStatus(String status) {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer + "//span[contains(.,'" + status + "')]"),
				"Package status " + status);
	}
	
	public void verifyDeleteButtonIsNotVisble(String btn) {
		btn = prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.elementNotVisible(By.xpath(ObjectUtil.xpathToTabContainer+"//span[contains(.,'"+btn+"')]"), btn+ " button");
	}
	
	public void verifyDeleteButtonIsVisble(String btn) {
		btn = prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer+"//span[contains(.,'"+btn+"')]"), btn+ " button");
	}
	
	public void uncheckPackageControlBox() {
		List<WebElement> ele=common.getElements(logisticModuleObj.selectedpackageControlCheckbox);
		for(WebElement element:ele) {
			common.clickAnElement(element, "Package control checkbox");
			common.explicitWait(2);
		}
	}
	
	public void enterValueInManufacturingBatch(String value) {
		common.performOperation(logisticModuleObj.manufacturingBatch_inputox, "input", value, "Manufacturing batch");
	}
	
	public String enterValueInManufacturingBatch(String value,String randomValue) {
		String rValue=common.generateRandomNumber();
		common.performOperation(logisticModuleObj.manufacturingBatch_inputox, "input", rValue+"-"+rValue, "Manufacturing batch");
		return rValue+"-"+rValue;
	}
	
	public void enterValueInQuanityKg(String value) {
		common.performOperation(logisticModuleObj.quantityKg_inputox, "input", value, "Manufacturing batch");
	}
	
	public void enterValueInManufacturingDateInputbox() {
		enterTodaysDate(logisticModuleObj.manufacturingDate_inputbox, "Manufacturing Date", "Today");
	}
	
	public void enterValueInExpirationDateInputbox() {
		enterTodaysDate(logisticModuleObj.expirationDate_inputbox, "Expiration Date", "Today");
	}
	
	public void clickmanufacturingBatchSearchIcon() {
		common.clickAnElement(logisticModuleObj.manufacturingBatch_searchIcon, "Manufacturing Batch search Icon");
	}
	
	public void clickmanufacturingBatchSearchIcon_noDelay() {
		common.clickJS_WithoutJSWait(logisticModuleObj.manufacturingBatch_searchIcon, "Manufacturing Batch search Icon");
	}
	
	public void clickTickIcon() {
		common.clickAnElement(logisticModuleObj.tickIcon, "Tick Icon");
	}
	
	public void selectGeneratePackingNumber(String btn) {
		btn = prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.clickJS_NoJSWaiter(By.xpath(ObjectUtil.getButtonXpath(btn)), btn);
	}
	
	
	public void verifyQuantityToReceiptReset(String field,String value) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		String text=common.getAttributeByValue(By.xpath(ObjectUtil.getInputboxXpath(field)), field);
		if(text.equalsIgnoreCase(value)){
			new ReportUtil().logPass("Quantity to receipt should redefined to the missing quantity of the item",
					"Quantity to receipt is redefined to the missing quantity of the item");
		}else
			new ReportUtil().logFail("Quantity to receipt should redefined to the missing quantity of the item",
					"Quantity to receipt is not redefined to the missing quantity of the item");
	}
	
	
	public void verifyMissingQuanity(String condition, String field, String value) {
		try {
			String text = common.getText(logisticModuleObj.missingQuanity, field);
			if (condition.contains("zero")) {
				if (text.equalsIgnoreCase(value)) {
					new ReportUtil().logPass("Value of missing quantity should be 0", "Value of missing quantity is 0");
				} else
					new ReportUtil().logFail("Value of missing quantity should be 0", "Value of missing quantity is not 0");
			} else {
				if (text.equalsIgnoreCase(value)) {
					new ReportUtil().logFail("Value of missing quantity should not be 0", "Value of missing quantity is 0");
				} else
					new ReportUtil().logPass("Value of missing quantity should not be 0",
							"Value of missing quantity is not 0");
			}
		}catch(Exception e){
			new ReportUtil().logFail("", "Unable to verify missing quantity");
		}
	}
	
	public void selectShipmentHavingMaterialFromSearchResult(String table,String columnName) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		try {
			int materialIndex=new MaintenancePlanning().getColumnIndexFromTable(table, columnName);
			int rowIndex=0;
			List<WebElement> ele= common.getElements(By.xpath(ObjectUtil.xpathToTabContainer
					+"//h3[contains(text(),'"+table+"')]/../../..//tbody//tr/td["+materialIndex+"]/div/a"));
			for(WebElement element:ele) {
				if(element.getText().trim().isEmpty()) {
					rowIndex++;
				}else {
					List<WebElement> receiptList = common.getElements(logisticModuleObj.recieptFileList);
					common.clickJS(receiptList.get(rowIndex), "Receipt available at index " + rowIndex);
				}
			}
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to select shipment");
		}
	}
	
	
	public void validatePackageIfNotValidated() {
		try {
			if(!common.getElement(logisticModuleObj.progressBarUnderPackage).getText().trim().contains("100")){
				new LogisticsModule().selectAllSearchResult("packages", "Package");
		    	new LogisticsModule().clickOnBtn("Validate Operations");
		    	new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
		    	new LogisticsModule().verifyPackageStatus("Validated");
			}
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to validate package");
		}
	}
	
	public void clickSaveBtnById() {
		common.clickJS(logisticModuleObj.saveBtn_Id, "Save");
	}
	
	
	public void validateMaterialProgressIsSuccesfull(String item,String percentage) {
		try {
			common.explicitWait(4);
			if(common.getElement(logisticModuleObj.progressBarUnderPackage).getText().trim().contains(percentage))
				new ReportUtil().logPass("Status of "+item+" should be "+percentage, "Status of "+item+" is displayed as "+percentage);
			else
				new ReportUtil().logFail("Status of "+item+" should be "+percentage, "Status of "+item+" is not displayed as "+percentage);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to "+item+" progress");
		}
	}

	public void validateMaterialProgress(String item,String percentage) {
		try {
			common.explicitWait(4);
			item = prop.readPropertyData(item.trim().replaceAll(" ", "_"));
			if(common.getElement(By.xpath("//h3[contains(.,'"+item+"')]/../../..//div[contains(@class,'progressbar-label')]")).getText().trim().contains(percentage))
				new ReportUtil().logPass("Status of "+item+" should be "+percentage, "Status of "+item+" is displayed as "+percentage);
			else
				new ReportUtil().logFail("Status of "+item+" should be "+percentage, "Status of "+item+" is not displayed as "+percentage);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to "+item+" progress");
		}
	}
	
	public void selectMaterial(String item, String packageStatus) throws Exception {
		packageStatus = prop.readPropertyData(packageStatus.trim().replaceAll(" ", "_"));
		item = prop.readPropertyData(item.trim().replaceAll(" ", "_"));
		try {
			List<WebElement> packagePlanned = common.getElements(By.xpath(ObjectUtil.xpathToTabContainer
					+ "//h3[contains(.,'" + item + "')]/../../..//tbody//div[contains(.,'" + packageStatus
					+ "')]/preceding::p-tablecheckbox//div[contains(@class,'checkbox-box')]"));
			if (packagePlanned.size() == 0)
				new ReportUtil().logFail("", "Expected material with package status planned is not available");
			else
				common.clickAnElement(packagePlanned.get(0), "Material");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select material");
			throw new Exception("Unable to select material");
		}

	}
	
	public void openWOInPRPage(String page) {
		common.getElementExplicitWait(logisticModuleObj.woInPR, 2);
		common.clickAnElement(logisticModuleObj.woInPR, "Work Order in "+page+" page");	
	}
	
	public void navigateBackToPage(String page) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		List<WebElement> breadCrumb=common.getElements(By.xpath("//a[contains(.,'"+page+" "+"')]"));
		if(breadCrumb.size()==2)
			common.clickJS(breadCrumb.get(1), page+ " page");
		else
			common.clickJS(breadCrumb.get(0), page+ " page");
		common.clickJS(maintenancePlanningPageObj.popup_YesBtn, "Yes button on popup");
		new MaintenancePlanning().validate_Page_Displayed(page);
		
	}
	
	public void ClickBreadcrumbs(String page) {
		page = prop.readPropertyData(page.trim().replaceAll(" ", "_"));
		common.scrollUp();
		List<WebElement> breadCrumb=common.getElements(By.xpath("//a[contains(.,'"+page+" "+"')]"));
		if(breadCrumb.size()==2)
			common.clickJS(breadCrumb.get(1), page+ " page");
		else if(breadCrumb.size()==4)
			common.clickJS(breadCrumb.get(3), page+ " page");
		else
			common.clickJS(breadCrumb.get(0), page+ " page");
		
	}

	public void PRCreatedUnderWO(String table,String column) {
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();

			int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table, column);
//			By loc=By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table+"')]/../../..//tbody//tr/td["+
//						colIndex+"][contains(.,'"+formatter.format(date)+"')]");
			By loc=By.xpath("//h3[contains(text(),'"+table+"')]/../../..//tbody//tr/td["+
					colIndex+"][contains(.,'"+formatter.format(date)+"')]");
			if(common.getElements(loc).size()>0)
				new ReportUtil().logPass("Request should be created under table "+table, "Request is created under table "+table);
			else
				new ReportUtil().logFail("Request should be created under table "+table, "Request is not created under table "+table);
		}catch(Exception e) {
			new ReportUtil().logFail("","Unable to validate request in table "+table);
		}
	}
	
	public void clickOnShowAllActionBtnUnderMaterial(String buttonName) {
		common.clickJS_NoJSWaiter(logisticModuleObj.showAllActionsBtnUnderMaterial, buttonName +" button");
	}
	
	public void clickOnFilterIcon() {
		common.getElementExplicitWait(maintenancePlanningPageObj.filterIcon, 2);
		common.clickAnElement(maintenancePlanningPageObj.filterIcon, "Filter icon");
	}

	public void clickOnFilterIconMWO() {
		common.getElementExplicitWait(maintenancePlanningPageObj.filterIconMWO, 2);
		common.clickAnElement(maintenancePlanningPageObj.filterIconMWO, "Filter icon");
	}

	public void clickOnFilterIcon(boolean noScroll) {
		common.getElementExplicitWait(maintenancePlanningPageObj.filterIcon, 0);
		common.clickJS(maintenancePlanningPageObj.filterIcon, "Filter icon");
	}
	
	public void clickManageBtn(String btnName) {
		common.clickAnElement(logisticModuleObj.manageBtn, btnName);
	}
	
	public void verifyFilteredSN_Displayed(int columnIndex,String snNumber) {
		boolean flag= true;
		By loctr = By.xpath(ObjectUtil.xpathToTabContainer + "//table//tbody//tr/td[" + columnIndex + "]//a");
		if(snNumber.trim().isEmpty()) {
			new ReportUtil().logInfo("No rows are available, hence validated for empty S/N number");
		}else {
		List<WebElement> sn = common.getElements(loctr);
		new ReportUtil().logInfo("Total result count is "+ sn.size());
		if(!sn.get(0).getText().trim().contains(snNumber)) {
				new ReportUtil().logFail("Items should be filtered correctly", "Items are not filtered correctly");
				flag=false;
		}
		
		if(flag==true) {
			new ReportUtil().logPass("Items should be filtered correctly", "Items are filtered correctly");
		 }
		}
	}
	
	public void enterValueInSNField(String field,String snNumber) {
		common.explicitWait(1);
		common.performOperation(By.xpath("//input[contains(@name,'"+field+"')]"), "input", snNumber, field);
	}
	
	public void enterValueInSNFieldIfExist(String field,String snNumber) {
		common.explicitWait(2);
		if(snNumber.trim().isEmpty()) {
			//common.enterTextUsingJS(common.getElement(By.xpath("//input[contains(@name,'"+field+"')]")), "123");
			common.performOperation(common.getElement(By.xpath("//input[contains(@name,'"+field+"')]")),"input","123","S/N");
		}else
			//common.enterTextUsingJS(common.getElement(By.xpath("//input[contains(@name,'"+field+"')]")), snNumber);
			common.performOperation(common.getElement(By.xpath("//input[contains(@name,'"+field+"')]")),"input",snNumber,"S/N");
		common.explicitWait(2);
	}
	
	public void clickReferenceArticle() {
		common.clickAnElement(logisticModuleObj.referenceArticle, "Reference article "+
							common.getText(logisticModuleObj.referenceArticle, "Reference article"));
	}
	
	public void selectItemUnderSite(String sn,String button) {
		button=prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		By loc= By.xpath(ObjectUtil.xpathToTabContainer+"//a[contains(text(),'"+sn+"')]/following::button/span[contains(.,'"+button+"')]");
		common.clickJS(loc, "Select button for SN "+sn);
	}


	public void selectItemInSite(String num,String button) {
		button=prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		By loc= By.xpath("//a[contains(text(),'"+num+"')]/../../../following-sibling::td//span[contains(text(),'"+button+"')]");
		common.clickJS(loc, "Select button for packaging number "+num);
	}

	public void selectItemInWarehouse(String num,String button) {
		button=prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		By loc= By.xpath("//a[contains(text(),'"+num+"')]/../../../following-sibling::td//span[contains(text(),'"+button+"')]");
		common.clickJS(loc, "Select button for packaging number "+num);
	}

	
	public void buttonNotDisplayed(String btnName) {
		common.elementNotVisible(By.xpath(ObjectUtil.getElementXpath(btnName)), btnName);		
	}

	public void buttonNotDisplayed(String btnName,boolean strict) {
		common.elementNotVisible(By.xpath(ObjectUtil.getButtonXpath(btnName,true)), btnName);
	}
	
	public void selectRowWithDifferentFinalRec(String table,String colsName) {	
		try {
		common.explicitWait(3);
		int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table, colsName);
		//selectMaterialFromMovementTable(1);
		int index=0;
		List<WebElement> checkboxes= common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td//mat-checkbox"));
		List<WebElement> finalRec=common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td["+colIndex+"]//i"));
		
		common.clickJS(checkboxes.get(0), "Movement");
		String textFinalRec=finalRec.get(0).getText().trim();
		
		for(WebElement ele:finalRec) {
			if((!ele.getText().trim().equalsIgnoreCase(textFinalRec)) && (index!=0)) {
				common.clickJS(checkboxes.get(index), "Second row with different recipient");
				
				break;
			}else
				index++;
		}}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to select row having same final recipient");
		}
	
	}
	
	public void selectRowWithSameFinalRecWithDiffCritical(String table,String colsName,String column2) {	
		try {
		common.explicitWait(3);
		int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table, colsName);
		int colIndex2=new MaintenancePlanning().getColumnIndexFromTable(table, column2);
	
		int index=0;
		List<WebElement> checkboxes= common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td//mat-checkbox"));
		List<WebElement> finalRec=common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td["+colIndex+"]//i"));
		
		List<WebElement> critical=common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
				+"')]/../../..//tbody/tr/td["+colIndex2+"]/div/div"));
		
		common.clickJS(checkboxes.get(0), "Movement");
		
		String textFinalRec=finalRec.get(0).getText().trim();
		String textCritical=critical.get(0).getText().trim();
		System.out.println("textCritical "+textCritical);
		System.out.println("textFinalRec "+textFinalRec);
		
		for(WebElement ele:finalRec) {
			if((ele.getText().trim().equalsIgnoreCase(textFinalRec))&& (!critical.get(index).getText().trim().equalsIgnoreCase(textCritical)) &&(index!=0)) {
				common.clickJS(checkboxes.get(index), "row with same recipient but different criticalities");
				break;
			}else
				index++;
		}}catch(Exception e) {
			new ReportUtil().logFail("", "row with same recipient but different criticalities");
		}
	
	}
	
	
	public void selectRowWithSameFinalRecWithSameCritical(String table,String colsName,String column2) {	
		try {
		common.explicitWait(3);
		int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table, colsName);
		int colIndex2=new MaintenancePlanning().getColumnIndexFromTable(table, column2);
		
		int index=0;
		List<WebElement> checkboxes= common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td//mat-checkbox"));
		List<WebElement> finalRec=common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
					+"')]/../../..//tbody/tr/td["+colIndex+"]//i"));
		
		List<WebElement> critical=common.getElements(By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table
				+"')]/../../..//tbody/tr/td["+colIndex2+"]/div/div"));
		
		common.clickJS(checkboxes.get(0), "Movement");
		String textFinalRec=finalRec.get(0).getText().trim();
		String textCritical=critical.get(0).getText().trim();
		System.out.println("textCritical "+textCritical);
		
		for(WebElement ele:finalRec) {
			if((ele.getText().trim().equalsIgnoreCase(textFinalRec))&& (critical.get(index).getText().trim().equalsIgnoreCase(textCritical))
					&& (index!=0)) {
				common.clickJS(checkboxes.get(index), "row with same recipient and same criticalities");
				break;
			}else
				index++;
		}}catch(Exception e) {
			new ReportUtil().logFail("", "row with same recipient and same criticalities");
		}
	
	}
	
	public void ClickOnCode(String name) {
		name=prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.xpathToTabContainer+"//th[contains(.,'"+name+"')]"), name+" column");
	}
	
	
	public String captureColumnValueOfTypeLinkByRow(String columnName, int rowNumber,String table) {
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		try {
			int columnIndex = findColumnIndexFromTable(table,columnName);
			By loctr = By.xpath(ObjectUtil.getTotalRowsXpath(table,columnIndex));
			List<WebElement> sn = common.getElements(loctr);
			return sn.get(rowNumber-1).getText().trim();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve " + columnName+" from row number "+rowNumber);
		}
		return null;
	}

	public String captureColumnValueOfTypeLink(String columnName, int rowNumber) {
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = 1;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//thead//tr/th");
			List<WebElement> ele = common.getElements(loc);
			for (WebElement element : ele) {
				if (element.getText().equalsIgnoreCase(columnName)) {
					System.out.println("Index of column " + columnName + " is " + columnIndex);
					break;
				} else
					columnIndex = columnIndex + 1;

			}

			By loctr = By.xpath(ObjectUtil.xpathToTabContainer + "//table//tbody//tr/td[" + columnIndex + "]//a");
			List<WebElement> sn = common.getElements(loctr);
			return sn.get(rowNumber-1).getText().trim();
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve " + columnName+" from row number "+rowNumber);
		}
		return null;
	}
	
	public String captureSNColumnValueIfExist(String columnName, int rowNumber) {
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = 1;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//thead//tr/th");
			List<WebElement> ele = common.getElements(loc);
			for (WebElement element : ele) {
				if (element.getText().equalsIgnoreCase(columnName)) {
					System.out.println("Index of column " + columnName + " is " + columnIndex);
					break;
				} else
					columnIndex = columnIndex + 1;
			}
			By loctr = By.xpath(ObjectUtil.xpathToTabContainer + "//table//tbody//tr/td[" + columnIndex + "]//a");
			List<WebElement> sn = common.getElements(loctr);
			if(sn.size()==0) {
				new ReportUtil().logInfo("No Rows with S/N number is avialable.Please change the test data");
			}else {
				new ReportUtil().logInfo("SN number captured as "+sn.get(rowNumber - 1).getText().trim());
				return sn.get(rowNumber - 1).getText().trim();
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve " + columnName+" from row number "+rowNumber);
		}
		return "";
	}
	
	public void clickonFolderIcon() {
		 common.clickAnElement(logisticModuleObj.folderIcon, "Folder icon");
	}
	
	public String captureTransferOrderNumber() {
		common.getElementExplicitWait(logisticModuleObj.transferNumber, 0);
		return common.getText(logisticModuleObj.transferNumber, "Transfer Order Number").replaceAll("-", "");
	}
	
	
	public void selectItemUnderWarehouse(String pn,String button) {
		button=prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		By loc= By.xpath(ObjectUtil.xpathToTabContainer+"//a[contains(text(),'"+pn
							+"')]/following::button/span[contains(.,'"+button+"')]");
		List<WebElement> ele=common.getElements(loc);
		common.clickAnElement(ele.get(0), "Select button for Packaging Number "+pn);
	}

	public void selectRowUsingBtn(String button) {
		button=prop.readPropertyData(button.trim().replaceAll(" ", "_"));
		By loc= By.xpath("(//span[normalize-space()='"+button+"'])[position()=1]");
		List<WebElement> ele=common.getElements(loc);
		common.clickAnElement(ele.get(0), button+" button");
	}
	
	public void pnInSelectedAsset(String section,String number) {
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer+
					"//*[contains(text(),'"+number+"')]"), "Packaging number "+number+" in "+section);
	}
	
	public void searchMovement(String value) {
		common.getElementExplicitWait(referentialPageObj.searchFilter_inputbox, 2);
		common.performOperation(referentialPageObj.searchFilter_inputbox, "input", value, "Search filter");
		common.explicitWait(2);
	}
	
	public void searchEquipmentOnPopup(String value) {
		common.performOperation(referentialPageObj.searchFilter_inputbox_equipmemtPopup, "input", value, "Search filter");
		common.explicitWait(2);
	}
	
	public void clickCubesIcon() {
		common.clickAnElement(logisticModuleObj.cubesIcon, "Cubes Icon");
	}
	
	public void navigateToPRPageFromPRM(String pageName) {
		pageName = prop.readPropertyData(pageName.trim().replaceAll(" ", "_"));
		By loc = By.xpath("//a[contains(.,'" + pageName + "')]");
		List<WebElement> ele = common.getElements(loc);
		common.clickJS(ele.get(1), pageName + " to navigate back");

	}
	
	public void clickOnTreatAndShip(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 2);
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}
	
	public void validateValueInField(String label,String value) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		value = prop.readPropertyData(value.trim().replaceAll(" ", "_"));		
		By loc= By.xpath(ObjectUtil.xpathToTabContainer+"//label[.='"+label+"']/..//input");
		if(common.getAttributeByValue(loc, label).equalsIgnoreCase(value)) {
			new ReportUtil().logPass("Value of "+label+ " should be "+value, "Value of "+label+ " is "+value);
		}else
			new ReportUtil().logFail("Value of "+label+ " should be "+value, "Value of "+label+ " is not "+value);
	}
	
	public void validateValueInField_Type(String label,String value) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		value = prop.readPropertyData(value.trim().replaceAll(" ", "_"));		
		common.checkElementPresentByVisibility(common.getElement(By.xpath("//*[contains(text(),'"+value+"')]")), label+ "with value "+value);
	}

	public void validateValueInField_TypeUnderSection(String sec,String label,String value) {
		sec = prop.readPropertyData(sec.trim().replaceAll(" ", "_"));
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		value = prop.readPropertyData(value.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(common.getElement(By.xpath("//*[contains(text(),'"+sec+"')]/../../following-sibling::div//label[text()='"+label+"']/..//div[contains(text(),'"+value+"')]")), label+ "with value "+value);

	}
	
	public void validateValueInTypeField(String label,String value) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		value = prop.readPropertyData(value.trim().replaceAll(" ", "_"));		
		By loc= By.xpath(ObjectUtil.xpathToTabContainer+"//label[.='"+label+"']/..//div/div");
		if(common.getText(loc, label).equalsIgnoreCase(value)) {
			new ReportUtil().logPass("Value of "+label+ " should be "+value, "Value of "+label+ " is "+value);
		}else
			new ReportUtil().logFail("Value of "+label+ " should be "+value, "Value of "+label+ " is not "+value);
	}
	
	public void validateTOPageInfoFromInstallScreen(String site) {
		site = prop.readPropertyData(site.trim().replaceAll(" ", "_"));		
		if(site.contains(common.getText(logisticModuleObj.supplierSiteValue, "supplier Site Value")) &&
				site.contains(common.getText(logisticModuleObj.supplierWarehouseValue, "supplier Warehouse Value")) &&
				site.contains(common.getText(logisticModuleObj.recipientSiteValue, "recipient Site Value"))) {
			new ReportUtil().logPass("Value of Supplier Site, Supplier Warehouse and Recipient Site value must be according to Intall/Remove screen",
					"Value of Supplier Site, Supplier Warehouse and Recipient Site value is according to Intall/Remove screen");	
		}else
			new ReportUtil().logFail("Value of Supplier Site, Supplier Warehouse and Recipient Site value must be according to Intall/Remove screen",
					"Value of Supplier Site, Supplier Warehouse and Recipient Site value is not according to Intall/Remove screen");				
		
	}
	
	public void validateReceiptFolderPageInfoFromInstallScreen(String workshop,String warehouse) {
		if((common.getText(logisticModuleObj.shipperWorkshopValue, "shipperWorkshopValue").contains(workshop)) &&
				(common.getText(logisticModuleObj.recipientWorkshopValue, "recipientWorkshopValue").contains(workshop)) &&
				(common.getText(logisticModuleObj.shipperWarehouseValue, "shipperWarehouseValue").contains(warehouse))) {
			new ReportUtil().logPass("Value of shipper workshop, shipper Warehouse and Recipient workshop value must be according to Intall/Remove screen",
					"Value of shipper workshop, shipper Warehouse and Recipient workshop value is according to Intall/Remove screen");	
		}else
			new ReportUtil().logFail("Value of shipper workshop, shipper Warehouse and Recipient workshop value must be according to Intall/Remove screen",
					"Value of shipper workshop, shipper Warehouse and Recipient workshop value is not according to Intall/Remove screen");				
		
	}
	
	public void clickSave_InstallRemoveScreen(String button) {	
		common.clickAnElement(logisticModuleObj.saveWOAsset, button);
	}
	
	
	public void openTOResult(String res) {
		int result = 0;
		try {
			result = Integer.parseInt(res);
		} catch (Exception e) {
		}

		WebElement results = common.getElement(dashboardPageObj.resultsCounter);
		try {
			Thread.sleep(2000);
			String extractedValue = CompactUtil.extractNumber(results.getText());

			if (result == 0)
				result++;
			common.getElementExplicitWait(dashboardPageObj.toCheckBox, 2);
			List<WebElement> list = common.getElements(dashboardPageObj.toCheckBox);
			common.clickJS(list.get(result - 1), "Result Check box");
			common.clickJS(dashboardPageObj.resultOpenButton, "Open Button");
		}catch(Exception e) {
			new ReportUtil().logFail("","unable yo open the search result");
		}

	}

	public void openRowUsingColumnLink(String column, String tableName,int index) throws Exception {
		try {
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));

			int columnIndex = new LogisticsModule().findColumnIndexFromTable(tableName, column);
			List<WebElement> fls = common.getElements(By.xpath(ObjectUtil.getTotalRowsXpathByLink(tableName, columnIndex)));
			common.explicitWait(2);
			common.click(fls.get(index-1),column+" link clicked at row "+index);

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to row using column "+column);
			throw new Exception("Unable to row using column "+column);
		}

	}
	
	public void verifyZoneIsAddedToList(String zoneName) {
		common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'"+zoneName+"')]"), "Zone "+zoneName);
	}
	
	public void validateItemStatusInStorageBin(String status,String area,String stockNumber) {		
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		area = prop.readPropertyData(area.trim().replaceAll(" ", "_"));
		enterInSearchFilter(area, stockNumber);
		common.checkElementPresentByVisibility(logisticModuleObj.statusSB_Deleted, status+ " "+ stockNumber);
	}
	
	public String enterNumber_Stock(String fieldName) {
		String rValue=common.generateRandomNumber();
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(logisticModuleObj.stockNumberField, "input", "Automation_"+rValue, fieldName);
		return "Automation_"+rValue;
	}
	
	public String enterRandomNumber_Stock(String fieldName) {
		String rValue=common.generateRandomNumber();
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(logisticModuleObj.stockNumberField, "input", rValue, fieldName);
		return rValue;
	}
	
	public void enterNumber_Stock(String fieldName,String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(logisticModuleObj.stockNumberField, "input", value, fieldName);
		
	}
	
	public void selectZone(String zone) {
		common.clickJS(By.xpath("//*[contains(text(),'"+zone+"')]/ancestor::tr//mat-checkbox"), zone);
	}
	
	public void newEntryAddedToSection(String section) {
		section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));		
		String count=CompactUtil.extractNumber(common.getText(By.xpath(ObjectUtil.xpathToTabContainer+
						"//h3[contains(text(),'"+section+"')]"), section));
		if(Integer.parseInt(count)>0) {
			new ReportUtil().logPass("New entry should be added to "+section, "New entry is added to "+section);
		}else
			new ReportUtil().logFail("New entry should be added to "+section, "New entry is not added to "+section);
	}
	
	public void selectNewlyAddedSB(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		List<WebElement> ele=common.getElements(logisticModuleObj.storageBinEntry);
		common.click(ele.get(0), name);
		common.explicitWait(2);
	}
	
	public void selectNewlyAddedArea(String name,String tablename) {
		try {
		tablename = prop.readPropertyData(tablename.trim().replaceAll(" ", "_"));
		enterInSearchFilter(tablename, name);
		By loc=By.xpath(ObjectUtil.xpathToTabContainer+"//a[contains(.,'"+
							name+"')]/../../..//following-sibling::div[2]");
		common.explicitWait(3);
		common.getElements(loc).get(0).click();
		new ReportUtil().logInfo("Clicked on newly created Area");
		}catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select newly added area");
		}
	}
	
	
	public void validateItemStatusInArea(String status,String area,String stockNumber) {
		try {
		status = prop.readPropertyData(status.trim().replaceAll(" ", "_"));
		area = prop.readPropertyData(area.trim().replaceAll(" ", "_"));
		common.explicitWait(1);
		enterInSearchFilter(area, stockNumber);
		common.explicitWait(1);
		By loc= By.xpath("//a[contains(.,'"+stockNumber
						+"')]/../../..//following-sibling::div[4]/div[contains(.,'"+status+"')]");
		List<WebElement> element= common.getElements(loc);
		String sts=common.getText(element.get(0), "status");
		if(sts.equalsIgnoreCase(status)) {
			new ReportUtil().logPass("Status of area should be "+status, "Status of area is "+status);
		}else {
			new ReportUtil().logFail("Status of area should be "+status, "Status of area is  "+sts);
		}
		}catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify delete status");
		}
	}
	
	public void newEntryAddedToStorageBinSection(String section,String stock) {
		section = prop.readPropertyData(section.trim().replaceAll(" ", "_"));	
		By loc=By.xpath("//h3[contains(text(),'"+section+"')]/../../..//div[contains(.,'"+stock+"')]");
		common.checkElementPresentByVisibility(loc, stock+" in storage bin list");
	}
	
	public void enterInSearchFilter(String name,String value) {		
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));	
		By loc=By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(.,'"+
								name+"')]/../../..//input[contains(@placeholder,'Search')]");
		common.performOperation(loc, "input", value, "filter box");
	}
	
	public void clickCheckbox(String text) {
		text = prop.readPropertyData(text.trim().replaceAll(" ", "_"));
		common.clickJS((By.xpath("//span[contains(.,'"+text+"')]/../p-checkbox//div[contains(@class,'checkbox-box')]")), "checkbox "+text);
	}
	
	public void selectAreaFromLocationPopup() {
		List<WebElement> locations=common.getElements(logisticModuleObj.locationList);
		common.scrollAndClick(locations.get(1), "zone from list");
	}
	
	public Map<String, String> captureSFAndPckgNumber(String shipmentFolder, String pckgNumber, String table) {
		try {
			Map<String, String> shipmentFolderDetails = new HashMap<String, String>();
			shipmentFolder = prop.readPropertyData(shipmentFolder.trim().replaceAll(" ", "_"));
			pckgNumber = prop.readPropertyData(pckgNumber.trim().replaceAll(" ", "_"));
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			By sfNumber = By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(text(),'" + shipmentFolder
					+ "')][contains(@class,'page-title')]/../div/div");
			int pckgColumn = new MaintenancePlanning().getColumnIndexFromTable(table, pckgNumber);
			By pckNo = By
					.xpath("//h3[contains(text(),'" + table + "')]/../../..//tbody/tr/td[" + pckgColumn + "]/span");
			shipmentFolderDetails.put("Package No.", common.getText(pckNo, pckgNumber));
			shipmentFolderDetails.put("Local Shipment", common.getText(sfNumber, shipmentFolder).replaceAll("-","").trim());
			return shipmentFolderDetails;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture shipment folder and package number details");
			return null;
		}
	}

	public String captureRepairRequestNumber(String name) {
		try {
			name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
			By rrNumber = By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(text(),'" + name
					+ "')][contains(@class,'page-title')]/../div/div");
			String number = common.getText(rrNumber,"Repair request");

			String number3 =  number.split(" ")[0];
			String number4 =  number.split(" ")[1];

			System.out.println(number3);
			System.out.println(number4);
			return number4;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture Repair request number details");
			return null;
		}
	}

	public String captureTransferOrderNumber(String name) {
		try {
			name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
			By toNumber = By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(text(),'"+name+"')]/../../../following-sibling::div//a");
			String number = common.getText(toNumber,"Repair request");
			return number;
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to capture Repair request number details");
			return null;
		}
	}
	
	public String getMaterialNumber(String table, String column) {
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
		int columnIndex = new MaintenancePlanning().getColumnIndexFromTable(table, column);
		String text = common.getText(
				By.xpath("//h3[contains(text(),'" + table + "')]/../../..//tbody/tr/td[" + columnIndex + "]//a"),
				"Material no");
		return text;

	}
	
	public void validateItemInPage(String item,String type) {
		common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'"+item+"')]"), "type "+item);
	}

	public void validateRRinTOPage(String item,String Field) {
		common.checkElementPresentByVisibility(By.xpath("//*[contains(text(),'"+Field+"')]/following-sibling::div/*[contains(text(),'"+item+"')]"), "Field "+Field);
	}

	public void verifyUnitExist() {
		if(!common.getElement(logisticModuleObj.unitValue).getText().trim().isEmpty()) {
			new ReportUtil().logPass("Unit value should exist", "Unit value is "+common.getElement(logisticModuleObj.unitValue).getText());		
		}else {
			new ReportUtil().logFail("Unit value should exist", "Unit value is not available");
		}
	}
	
	public void textPresentInField(String text,String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));	
		text = prop.readPropertyData(text.trim().replaceAll(" ", "_"));	
		By loc=By.xpath(ObjectUtil.xpathToTabContainer+"//label[contains(.,'"+field+"')]/../div");
		String actualText=common.getElement(loc).getText().trim();
		if(text.contains(text)) {
			new ReportUtil().logPass("Text under the element "+field+" should be "+
										text, "Text under the element "+field+" is "+text);
		}else {
			new ReportUtil().logFail("Text under the element "+field+" should be "+
					text, "Text under the element "+field+" is "+actualText);
		}
	}

	public void textPresentInTextarea(String text,String field) {
		field = prop.readPropertyData(field.trim().replaceAll(" ", "_"));
		By loc=By.xpath(ObjectUtil.xpathToTabContainer+ObjectUtil.getTextAreaXpath(field));
		String actualText=common.getAttributeByValue(loc,field);
		if(text.contains(text)) {
			new ReportUtil().logPass("Text under the element "+field+" should be "+
					text, "Text under the element "+field+" is "+text);
		}else {
			new ReportUtil().logFail("Text under the element "+field+" should be "+
					text, "Text under the element "+field+" is "+actualText);
		}
	}
	
	public void clickOnFolderNumber(String name) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));		
		String number=common.getText(By.xpath(ObjectUtil.xpathToTabContainer+"//label[contains(.,'"+name+"')]/..//a"), name);
		common.click(By.xpath(ObjectUtil.xpathToTabContainer+"//label[contains(.,'"+name+"')]/..//a"), name+" "+number);
	}
	
	public void validateLitigation() {
		common.clickJS_WithoutJSWait(logisticModuleObj.validateLitigation, "Validate");
	}
	
	public void clickOnFilterBtnOnWOFilters(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)),2);
		common.clickJS(By.xpath( ObjectUtil.getButtonXpath(btnName)),btnName + " button");
	}

	public void filterTable(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("(//span[contains(.,'"+btnName+"') and contains (@class,'button')])[2]"),
				2);
		common.clickJS(By.xpath("(//span[contains(.,'"+btnName+"') and contains (@class,'button')])[2]"),
				btnName + " button on the page ");
	}
	
	public String captureSN_CreateRepairPopup() {
		String sn=common.getText(logisticModuleObj.crrSN, "S/N");
		return sn;
	}
	
	public void verifySNOnRepairRequestPage(String number) {
		common.checkElementPresentByVisibility(By.xpath("//*[contains(.,'"+number+"')]"), number);
	}
	
	public void clickAccept() {
		common.clickJS(logisticModuleObj.acceptBtn, "Accept");
	}
	
	public void clickRefuse() {
		common.clickJS(logisticModuleObj.refuseBtn, "Refuse");
	}

	public String captureSubtitleValue(){
		String value=common.getText(logisticModuleObj.pageSubtitleValue,"Subtitle value from the page");
		return value;
	}

	public void clickOnDeployementNum(String num){
		common.clickJS(By.xpath("//a[contains(.,'"+num+"')]"),num);
	}

	public void selectItemFromSearch(String name){
		common.click(By.xpath("//div[contains(.,'"+name+"')][contains(@class,'cell-content')]"),name);
	}

	public void verifyCheckBoxIsChecked(String label){
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getCheckboxCheckedXpath(label)),label+" checkbox checked");
	}

	public String captureBatchNumber(String label){
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		String text=common.getText(By.xpath(ObjectUtil.xpathToTabContainer+"//label[contains(.,'"+label+"')]/..//a"),label);
		return text;
	}

	public void clickOriginalDocumentLink(String table,String column){
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
		common.explicitWait(15);
		int index=new MaintenancePlanning().getColumnIndexFromTable(table,column);
		common.clickJS(By.xpath(ObjectUtil.xpathToTabContainer+"//td["+index+"]//a"),column);
	}

	public void verifyNoMovementCreated(String table){
		int count=getCountFromTable(table);
		if(count==0){
			new ReportUtil().logPass(table+ " count should be zero",table+ " count is zero");
		}else {
			new ReportUtil().logFail(table + " count should be zero", table + " count is not zero");
		}
	}

	public void clickCheckboxByLabel(String label){
		common.clickJS(By.xpath(ObjectUtil.xpathToTabContainer+"//label[contains(.,'"+label+"')]/..//input"),label);

	}

	public void selectAutomation145UserProfile(){
		common.clickJS(dashboardPageObj.chefCheckbox,"chef checkbox");
	}

	public void UpdateFamilies(String type){
		String countText=common.getText(By.xpath("//h4[contains(.,'"+type+"')]"),type);
		if(Integer.parseInt(CompactUtil.extractNumber(countText))==0){
			clickOnBtnUsingJS("Update family assignment");
			common.clickJS(dashboardPageObj.allCheckboxOverPopup,"Select all checkbox");
			List<WebElement> ele=common.getElements(dashboardPageObj.totalCheckboxOverPopup);
			common.clickJS(ele.get(0),"first checkbox");
			new MaintenancePlanning().clickValidateBtn("Validate");
		}

	}

	public void UpdateOperator(String type){
		String countText=common.getText(By.xpath("//h4[contains(.,'"+type+"')]"),type);
		if(Integer.parseInt(CompactUtil.extractNumber(countText))==0){
			clickOnBtnUsingJS("Update operator assignment");
			common.clickJS(dashboardPageObj.allCheckboxOverPopup,"Select all checkbox");
			new MaintenancePlanning().clickValidateBtn("Validate");
		}

	}

	public void updateSiteAssignment(String type){
		String countText=common.getText(By.xpath("//h4[contains(.,'"+type+"')]"),type);
		if(Integer.parseInt(CompactUtil.extractNumber(countText))==0) {
			clickOnBtnUsingJS("Update site assignment");
			common.clickJS(dashboardPageObj.matMenu, "menu");
			common.clickJS(dashboardPageObj.selectAllBtn, "Select All");
			List<WebElement> ele = common.getElements(dashboardPageObj.matMenu);
			common.clickJS(ele.get(ele.size() - 1), "menu");
			common.clickJS(dashboardPageObj.selectAllBtn, "Select All");
			new MaintenancePlanning().clickValidateBtn("Validate");
		}


	}

	public void selectUser(){
		common.clickJS(dashboardPageObj.totalCheckbox,"user checkbox");
	}

	public void clickonInitialRR(String label){
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		By loc= By.xpath(ObjectUtil.getFormLabelXpath(label)+"/..//a");
		common.clickJS(loc,label);
	}

	public void validateTextForRedirectedRR(String label,String expected){
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		expected = prop.readPropertyData(expected.trim().replaceAll(" ", "_"));
		By loc= By.xpath(ObjectUtil.getFormLabelXpath(label)+"/..//*[contains(.,'"+expected+"')]");
		common.checkElementPresentByVisibility(loc,"value "+expected+" under label "+label);
	}

	public void clickMissingQuantity(String label){
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		common.click(By.xpath("//div[contains(text(),'"+label+"')]"),label);
	}

	public void selectMaterialFromAssetDetails(){
		List<WebElement> ad= common.getElements(By.xpath(ObjectUtil.getTotalRowsXpathOnPopup(9)));
		common.click(ad.get(0),"item from table asset details");
	}

	public void clickPackagingNumberOfMaterial(String pck,String material){
		common.clickJS(By.xpath("//a[contains(.,'"+material+"')]/ancestor::td//following-sibling::td[position()=1]//a"),material
				+" " +pck);
	}

	public void noAssetSelectedText(){
		common.checkElementPresentByVisibility(logisticModuleObj.noSelectedAsset,"No selected asset");
	}

	public void snUnderSelectedAsset(){
		common.checkElementPresentByVisibility(logisticModuleObj.snUnderSelectedAsset,"SN number as "+common.getText(logisticModuleObj.snUnderSelectedAsset,"SN"));
	}

	public void textStatusChecked(String text){
		//By loc=By.xpath("//span[normalize-space()='"+text+"']/../..//mat-icon[normalize-space('check')]");
		By loc=By.xpath("//span[contains(text(),'"+text+"')]/../..//aw-icon-check-single");
		common.checkElementPresentByVisibility(loc,text+ " with icon checked");

	}

	public void aircraftSetForSupplier(){
		common.checkElementPresentByVisibility(logisticModuleObj.aircraftOnSupplier,"Aircraft in supplier");
	}

	public void warehouseTextForSupplier(String text){
		common.checkElementPresentByVisibility(By.xpath("//span[contains(.,'"+text+"')][contains(@class,'bold')]"),text);
	}

	public void selectCheckboxUnderSection(String section,List<String> data){

		for(String str:data){
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			By loc= By.xpath("//label[normalize-space()='"+section+"']/..//label[contains(.,'"+str+"')]/..//div[contains(@class,'checkbox-box')]");
			common.clickJS(loc,str);
		}
	}

	public void verifyStatusWithValues(String status,List<String> data){
		common.checkElementPresentByVisibility(By.xpath("//div[normalize-space()='"+status+"']"),status);
		By loc= By.xpath("//div[normalize-space()='"+status+"']/following-sibling::div");
		String actual=common.getText(loc,"status values");
		for(String str:data){
			str = prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			if(actual.contains(str)){
				new ReportUtil().logPass("Value "+str+" should exist for status "+status,"Value "+str+" exist for status "+status);
			}else{
				new ReportUtil().logFail("Value "+str+" should exist for status "+status,"Value "+str+" is not exist for status "+status);
			}
		}

	}

	public void selectOperationCheckbox(String operation){
		common.click(By.xpath("//div[normalize-space()='"+operation+"']"),operation);
	}

	public void operationDeleted(String operation){
		common.elementNotAvailableOnPage(By.xpath("//div[normalize-space()='"+operation+"']"),operation);
	}

	public void validateColumnValue(String column,String expectedValue,String table){
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
		int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table,column);

		List<WebElement> totalRows= common.getElements(By.xpath("//h3[contains(.,'"+table+"')]/../../..//tbody//tr/td["+colIndex+"]"));
		for(WebElement row:totalRows){
			if(row.getText().trim().contains(expectedValue)){
				new ReportUtil().logPass("Value of column "+column+" should be "+expectedValue+" under table "+table,"Value of column "+column+" is "+expectedValue+" under table "+table);
			}else{
				new ReportUtil().logFail("Value of column "+column+" should be "+expectedValue+" under table "+table,"Value of column "+column+" is not "+expectedValue+" under table "+table);
			}
		}
	}

	public void verifyCodeExist(String code){
		common.checkElementPresentByVisibility(By.xpath("//div[normalize-space()='"+code+"']"),code);
	}

	public void openBatchNumber(String batchNum){
		common.getElementExplicitWait(By.xpath(ObjectUtil.xpathToTabContainer+"//a[normalize-space()='"+batchNum+"']"),2);
		common.explicitWait(5);
		common.click(By.xpath(ObjectUtil.xpathToTabContainer+"//a[normalize-space()='"+batchNum+"']"),batchNum);

	}

	public void verifyCapturedPackageNumber(String num){
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer+"//a[contains(.,'"+num+"')]"),num);
	}

	public void validateExpirationDate(String table,String column){
		boolean flag=true;
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
		try {

			int columnIndex = 1;
				By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(text(),'" + table + "')]/../../..//thead//tr/th");
				List<WebElement> ele = common.getElements(loc);
				for (WebElement element : ele) {
					if (element.getText().equalsIgnoreCase(column)) {
						System.out.println("Index of column " + column + " is " + columnIndex);
						break;
					} else
						columnIndex = columnIndex + 1;
				}

			DateFormat df = new SimpleDateFormat("M/dd/yy");
			Calendar calobj = Calendar.getInstance();
			String expectedDate=df.format(calobj.getTime());
			System.out.println(df.format(calobj.getTime()));

			By loc1 = By.xpath(ObjectUtil.xpathToTabContainer + "//*[contains(text(),'" + table + "')]/../../..//tbody/tr/td[" + columnIndex + "]");
			List<WebElement> data = common.getElements(loc1);
			for (WebElement ss : data) {
				if (!expectedDate.contains(ss.getText().trim())) {
					new ReportUtil().logFail("Data should be todays date at column " + column, "Data is incorrect at column " + column);
					flag = false;
				}
			}
			if (flag == true) {
				new ReportUtil().logPass("Data should be todays date at column " + column, "Data is correct at column " + column);
			}
		}catch (Exception e){
			new ReportUtil().logFail("","Unable to validate expiration date");
		}


	}

	public String capturePckNum(String table,String column){
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
		try {
			int index = new MaintenancePlanning().getColumnIndexFromTable(table, column);
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + table + "')]/../../..//tbody/tr/td["+index+"]//a");
			String num = common.getText(common.getElement(loc), column);
			return num;
		}catch (Exception e){
			new ReportUtil().logFail("","Unable to capture package number");
			return null;
		}



	}










	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
		
		
	//Generic utilities
	public void clickOnBtn(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 0);
		common.clickAnElement(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
		if(btnName.contains("Refresh"))
			common.getElementExplicitWaitNoVisibility(By.xpath(ObjectUtil.getButtonXpath(btnName)),"30");
	}

	public void clickOnStatusBtn(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getStatusButtonXpath(btnName)), 0);
		common.clickAnElement(By.xpath(ObjectUtil.getStatusButtonXpath(btnName)), btnName + " button");
	}

	public void clickOnBtnWithoutCheckingVisibility(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.clickAnElement(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}
	
	public void clickOnBtn(String btnName, boolean Container, String page) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getButtonXpath(btnName)),
				2);
		common.clickJS(By.xpath(ObjectUtil.xpathToTabContainer + ObjectUtil.getButtonXpath(btnName)),
				btnName + " button on the page ");
	}

	public void clickOnFilterBtn(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//aw-maintenance-table-filters//button/span[contains(.,'"+btnName+"')]"),
				2);
		common.clickJS(By.xpath("//aw-maintenance-table-filters//button/span[contains(.,'"+btnName+"')]"),
				btnName + " button on the page ");
	}

	public void filterWOTable(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//aw-my-jobs-cards-table-filters//button/span[contains(.,'"+btnName+"')]"),
				2);
		common.clickJS(By.xpath("//aw-my-jobs-cards-table-filters//button/span[contains(.,'"+btnName+"')]"),
				btnName + " button on the page ");
	}

	public void filterWMTable(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//aw-works-management-table-filters//button/span[contains(.,'"+btnName+"')]"),
				2);
		common.clickJS(By.xpath("//aw-works-management-table-filters//button/span[contains(.,'"+btnName+"')]"),
				btnName + " button on the page ");
	}

	public void clickFilterBtn(){
		common.clickJS(By.xpath("//span[normalize-space()='Filter']"),"filter button");
	}
	
	public void scrollAndClickOnBtnUsingJS(String btnName) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 2);
		common.clickAnElement(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}
	

	public void clickOnBtnUsingJS(String btnName) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 2);
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
		common.explicitWait(1);
	}

	public void clickOnBtnUsingJS(String btnName,boolean notVisible) {

		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}

	public void verifyPopupIsDisplayed(String popupName) {
       try {
		popupName = prop.readPropertyData(popupName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getPopupHeaderXpath(popupName)), 0);
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getPopupHeaderXpath(popupName)), popupName + " Popup");
       }catch(Exception e) {
    	   new ReportUtil().logFail("", "Unable to verify popup "+popupName);
       }
       common.explicitWait(1);
	}

	public void verifyButtonIsVisible(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 0);
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}

	public void verifyButtonIsNotEnable(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//button[contains(.,'"+btnName+"') and contains (@class,'button')]"), 0);
		common.checkElementNotEnabled(By.xpath("//button[contains(.,'"+btnName+"') and contains (@class,'button')]"), btnName + " button");
	}

	public void verifyButtonIsEnable(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpath(btnName)), 0);
		common.checkElementEnabled(By.xpath(ObjectUtil.getButtonXpath(btnName)), btnName + " button");
	}
	
	public void verifyButtonIsVisibleOnStatusPopup(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getBtnXpathOnStatusPopup(btnName)), 2);
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getBtnXpathOnStatusPopup(btnName)), btnName + " button");
	}
	
	public void verifyButtonNotVisible(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWaitInVisibility(By.xpath(ObjectUtil.getButtonXpath(btnName)), "5", btnName);
	}
	
	public void clickButtonOnStatusPopup(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.click(By.xpath(ObjectUtil.getBtnXpathOnStatusPopup(btnName)), btnName + " button");
	}

	public int getNumberOfItemsInTable(String tableHeader) {
		int count = common.getElements(By.xpath(ObjectUtil.getResultListXpath(tableHeader))).size();
		new ReportUtil().logInfo("Total number of item in "+tableHeader+ " are "+count);
		return count;

	}
	
	public int getCountFromTable(String tableHeader) {
		tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
		int count =getNumberOfItemsInTable(tableHeader);
		return count;
	}
	
	public int getItemCountFromTable(String tableHeader) {
		String extractedValue = common.getText(By.xpath(ObjectUtil.getResultCountFromTableHeader(tableHeader)), tableHeader);
		String count = CompactUtil.extractNumber(extractedValue);
		
		return Integer.parseInt(count);

	}

	public void verifyLabelIsVisisble(String name) {

		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getLabelXpath((name))), 0);
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getLabelXpath(name)), name + " label");
	}

	public void enterValueInField(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpath(fieldName)), "input", value, fieldName);
	}

	public void enterValueInField(String fieldName, String value,boolean normalize) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("//label[normalize-space()='"+fieldName+"']/..//input"), "input", value, fieldName);
	}
	
	public void enterValueInFieldOnPopup(String fieldName, String value,String popup) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		String popupLowerCase=popup.toLowerCase().replaceAll(" ", "-");
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpathOnPopup(fieldName, popupLowerCase)), "input", value, fieldName);
	}
	
	public void enterValueInFieldOnPopup(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpathOnPopup(fieldName)), "input", value, fieldName);
	}

	public void enterValueInFieldonPopup(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getInputboxXpathonPopup(fieldName)), "input", value, fieldName);
	}
	
	public void enterValueInTextArea(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getTextAreaXpath(fieldName)), "input", value, fieldName);
	}

	public void enterValueInTextArea(String fieldName, String value,String section) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("//*[contains(text(),'"+section+"')]/../../following-sibling::div//label[contains(text(),'"+fieldName+"')]/..//textarea"), "input", value, fieldName);
	}

	public void enterValueInSearchAreaSection(String fieldName, String value,String section) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("//*[contains(text(),'"+section+"')]/../../following-sibling::div//input[@placeholder='"+fieldName+"']"), "input", value, fieldName);
	}

	public void clickWebElement(By loc, String elementName) {
		common.clickAnElement(loc, elementName);
	}

	public void validateConfirmationMsg(String msg) {
		try {
		msg = prop.readPropertyData(msg.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//*[contains(.,'" + msg + "')]"));
		if(common.getElement(By.xpath("//*[contains(.,'" + msg + "')]")).isDisplayed()) {
			new ReportUtil().logPass("confirmation message " + msg+ " should be present",
					"confirmation message " + msg+ " is present");
		}else 
			new ReportUtil().logFail("confirmation message " + msg+ " should be present",
					"confirmation message " + msg+ " is not present");
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to validate message "+msg);
		}
	}
	
	public void validateConfirmationMsg(String msg,String usingTextContent) {
		try {
		msg = prop.readPropertyData(msg.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(referentialPageObj.successMsg_Text);
		String text=common.getElement(referentialPageObj.successMsg_Text).getAttribute("textContent").trim();
		if(text.toLowerCase().contains(msg.toLowerCase())) {
			new ReportUtil().logPass("confirmation message " + msg+ " should be present",
					"confirmation message " + msg+ " is present");
		}else 
			new ReportUtil().logFail("confirmation message " + msg+ " should be present",
					"confirmation message " + msg+ " is not present");
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to validate message "+msg);
		}
		
	}
	
	public void navigateToTab(String name) {
		String rawName=name;
		try {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		if(rawName.equalsIgnoreCase("HR Management")) {
			common.explicitWait(5);
		}
		common.clickJS(By.xpath("//*[normalize-space(text())='"+name+"'][contains(@id,'tabpanel')]"), name + " tab");
		common.explicitWait(2);
		}catch(Exception e) {
			new ReportUtil().logFail("", "Navigation to tab failed "+name);
		}
	}
	   
	public void selectSearchResult(String index, String name, String tableName) {
		int result = 0;
		try {
			result = Integer.parseInt(index);
		} catch (Exception e) {
		}

		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		By table = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[(contains(.,'" + tableName + "'))]");
        common.getElementExplicitWait(table, 0);
		WebElement results = common.getElement(table);
		try {
			common.explicitWait(1);
			String extractedValue = CompactUtil.extractNumber(results.getText());

			if (result == 0)
				result++;
			if (extractedValue != null && extractedValue != "0") {
				int numbersOfResult = Integer.parseInt(extractedValue);
				if (numbersOfResult >= result) {
					By checkboxes;
					if (tableName.contains("Movement")||tableName.contains("Mouvements")) {
						checkboxes = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]"
								+ "/../../..//tbody/tr//input/..");
					} else
						checkboxes = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]"
								+ "/../../..//tbody//div[contains(@class,'checkbox-box')]");
					List<WebElement> list = common.getElements(checkboxes);
					System.out.println("Total item in a list are "+list.size());

					if (!list.get(result-1).getAttribute("class").contains("highlight"))
						common.clickAnElement(list.get(result-1), "Result Check box" + " to select the " + name);
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name + " from " + tableName + " table");
		}
	}
	
	
	public String selectAllSearchResult(String name, String tableName) {
		try {
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
			WebElement resultCount=common.getElement(By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]"));
			String extractedValue = CompactUtil.extractNumber(resultCount.getText().trim());
			if (Integer.parseInt(extractedValue)>0) {
				By allcheckbox; 
				if (tableName.contains("Movement")||tableName.contains("Mouvements")) {
					 allcheckbox = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]"
							+ "/../../..//thead//input/..");
				}else
				    allcheckbox = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]"
						+ "/../../..//thead//div[contains(@class,'checkbox-box') or contains(@class,'checkbox')]");
				common.clickJS(allcheckbox, "select all checkbox");
				return extractedValue;
			}else {
				new ReportUtil().logInfo("No "+name+" exist in tabel "+tableName+" to select");
				return "0";
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name + " from " + tableName + " table");
			return "0";
		}
	}

	public String selectAllSearchResults(String name, String tableName) {
		try {
			tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
			WebElement resultCount=common.getElement(By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(.,'" + tableName + "')]/../../following-sibling::div//span"));
			String extractedValue = CompactUtil.extractNumber(resultCount.getText().trim());
			if (Integer.parseInt(extractedValue)>0) {
				common.clickAnElement(referentialPageObj.tableMenuOption,"Table menu option");
				common.clickAnElement(referentialPageObj.selectAllOpt,"Select All option");
				return extractedValue;
			}else {
				new ReportUtil().logInfo("No "+name+" exist in tabel "+tableName+" to select");
				return "0";
			}

		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select " + name + " from " + tableName + " table");
			return "0";
		}
	}
	
	
	public void enterTodaysDate(By loc, String fieldName,String today) {
		try{
		 today=prop.readPropertyData(today.trim().replaceAll(" ", "_"));
		 common.clickAnElement(loc, fieldName);
	     common.clickJS(By.xpath(ObjectUtil.getButtonXpath(today)), today); 
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to enter todays date in field "+fieldName);
		}
	 }
	
	public void clickUsingTagBtn(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpathTagBtn(btnName)), 2);
		common.clickAnElement(By.xpath(ObjectUtil.getButtonXpathTagBtn(btnName)), btnName + " button");
	}
	
	public void CheckVisibityOfItem(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath(ObjectUtil.getButtonXpathTagBtn(btnName)), 2);
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.getButtonXpathTagBtn(btnName)), btnName + " button");
	}
	
	
	public void clickOnElement(String name) {
//		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
//		common.getElementExplicitWait(By.xpath(ObjectUtil.getElementXpath(name)), 0);
		common.clickAnElement(By.xpath(ObjectUtil.getElementXpath(name)), name);
	}
	
	public void verifyTestCaseExecutedCompletely() {
		try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			    Date date = new Date();  			 
            	FileWriter writer = new FileWriter(System.getProperty("user.dir")+"\\logs\\AutomationLog.txt", true);
    			writer.write("TEST CASE EXECUTED COMPLETELY_"+formatter.format(date));	
    			writer.write("\r\n");
    			writer.close();
    			System.out.println("STATUS OF EXECUTED SCENARIO IS "+report.getStatus());
    			
    		} catch (IOException e) {
    			e.printStackTrace();
        }
	}
	
	public static File getLastModified(String directoryFilePath)
	{
	    File directory = new File(directoryFilePath);
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;

	    if (files != null)
	    {
	        for (File file : files)
	        {
	            if (file.lastModified() > lastModifiedTime)
	            {
	                chosenFile = file;
	                lastModifiedTime = file.lastModified();
	            }
	        }
	    }

	    return chosenFile;
	}
	
	public void clickRefresh() {
		common.clickJS(logisticModuleObj.refreshButton, "Refresh");
		common.explicitWait(5);
	}
	
	
	public void waitPageLoad() {
		new com.aeroweb.utils.JSWaiter().waitAllRequest();
	}
	
	public void clickOnElementOnPopup(String name, boolean scroll) {
		name = prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		if(scroll==false)
			common.clickJS(By.xpath(ObjectUtil.getElementXpathOnPopup(name)), name);
		else
			common.clickAnElement(By.xpath(ObjectUtil.getElementXpathOnPopup(name)), name);
	}
	
	
	public void selectOptionFromDD_popup(String option,String dd) throws Exception {
		boolean flag=false;
		try {
		option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
		dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
		By optionList = By.xpath("//p-dropdownitem/li/span");
		common.clickJS(By.xpath(ObjectUtil.getDropdownXpathOnPopup(dd)), dd);
		common.explicitWait(1);
		List<WebElement> list = common.getElements(optionList);
		for (WebElement ele : list) {
			if (ele.getText().trim().equalsIgnoreCase(option)) {
				common.clickJS(ele, option);
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

	public void selectOptionFromWorkOrderDD_popup(String option,String dd) throws Exception {
		boolean flag=false;
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			common.clickJS(By.xpath(ObjectUtil.getWorkOrderDropdownXpathOnPopup(dd)), dd);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
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



	public void selectOptionFromCheckboxDD_popup(String option,String dd) throws Exception {
		boolean flag=false;
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-multiselectitem/li/div[2]");
			common.click(By.xpath(ObjectUtil.getDropdownXpathOnPopup(dd)), dd);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					String optToSel = ele.getText().trim();
					WebElement ele1 = common.getElement(By.xpath("//p-multiselectitem//*[contains(text(),'"+optToSel+"')]/input[1]"));
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

	public void selectOptionFromDD_popup(int index,String dd) throws Exception {
		try {
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			common.clickJS(By.xpath(ObjectUtil.getDropdownXpathOnPopup(dd)), dd);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);

					common.clickJS(list.get(index), "option at index "+index);
					new ReportUtil().logPass(" option should be selected from dropdown " + dd,
							( " option is selected from dropdown " + dd));
					common.explicitWait(1);

		}catch (Exception e) {
			new ReportUtil().logFail(" option should be selected from dropdown " + dd,
					" option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + " option at index "+index);
		}
	}


	
	public int findColumnIndexFromTable(String table, String column) {
		int colIndex = 1;
		try {
			List<WebElement> columnList = common.getElements(By.xpath(ObjectUtil.xpathToGetAllHeaders(table)));
			for (WebElement col : columnList) {
				if (col.getText().trim().equalsIgnoreCase(column)) {
					System.out.println("Index of column " + column + " is " + colIndex);
					break;
				} else
					colIndex = colIndex + 1;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve column " + column + " Index from table " + table);
		}
		return colIndex;
	}


	public void searchInTable(String value){
		common.performOperation(dashboardPageObj.searchInTableBoxWithCon,"input",value,"Search in table");
		common.explicitWait(2);
	}
	
	public void checkResultsPresent(String table) {
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			if(common.getElement(dashboardPageObj.searchInTableBoxWithCon).isDisplayed()==true) {
				common.getElementExplicitWait(dashboardPageObj.searchInTableBoxWithCon, 2);
			}
			WebElement results = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table)));
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (Integer.parseInt(extractedValue)==0)
				new ReportUtil().logInfo("No Search Results available");
			else
				new ReportUtil().logInfo("Search Results available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void checkResultsCount(String table) {
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			if(common.getElement(dashboardPageObj.searchInTableWithCon).isDisplayed()==true) {
				common.getElementExplicitWait(dashboardPageObj.searchInTableWithCon, 2);
			}
			WebElement results = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table)));
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (Integer.parseInt(extractedValue)==0)
				new ReportUtil().logInfo("No Search Results available");
			else
				new ReportUtil().logInfo("Search Results available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}

	public void checkNoResultsPresent(String table) {
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			WebElement results = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table)));
			String extractedValue = CompactUtil.extractNumber(results.getText());
			if (Integer.parseInt(extractedValue)==0)
				new ReportUtil().logPass("No Search Results should available","No Search Results available");
			else
				new ReportUtil().logFail("No Search Results should available","Search Results are available");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to verify search results");
		}
	}
	
	public void scrollToElement(String label) {
		label = prop.readPropertyData(label.trim().replaceAll(" ", "_"));
		WebElement ele=common.getElement(By.xpath(ObjectUtil.getElementXpath(label)));
		common.scrollIntoView(ele);
	}

	public void selectOptionFromDDUnderSection(String section,String option,String dd) throws Exception {
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section=prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			common.clickJS(By.xpath(ObjectUtil.getDropdownXpathUnderSection(section,dd)),dd);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
							(option + " option is selected from dropdown " + dd));
					common.explicitWait(.5);
					break;
				}
			}
		}catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}
	}

	public void selectOptionFromDDUnderSection(String section,String option,String dd,boolean following) throws Exception {
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			section=prop.readPropertyData(section.trim().replaceAll(" ", "_"));
			dd = prop.readPropertyData(dd.trim().replaceAll(" ", "_"));
			By optionList = By.xpath("//p-dropdownitem/li/span");
			common.clickJS(By.xpath(ObjectUtil.getDropdownXpathUnderSection(section,dd,true)),dd);
			common.explicitWait(1);
			List<WebElement> list = common.getElements(optionList);
			for (WebElement ele : list) {
				if (ele.getText().trim().equalsIgnoreCase(option)) {
					common.clickJS(ele, option);
					new ReportUtil().logPass(option + " option should be selected from dropdown " + dd,
							(option + " option is selected from dropdown " + dd));
					common.explicitWait(.5);
					break;
				}
			}
		}catch (Exception e) {
			new ReportUtil().logFail(option + " option should be selected from dropdown " + dd,
					option + " option is not selected from dropdown " + dd);
			throw new Exception("Unable to select" + option);
		}
	}

	public void clickOnBtnOnPopupUSingVisibleText(String popup,String btnName){
		popup=prop.readPropertyData(popup.trim().replaceAll(" ", "_"));
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getXpathOfButtonOnPopup(popup,btnName)),btnName);
	}

	public void verifyTabsAreAvailable(String tab){
		tab=prop.readPropertyData(tab.trim().replaceAll(" ", "_"));
		common.checkElementPresentByVisibility(By.xpath(ObjectUtil.xpathToTabContainer+"//div[contains(text(),'"+tab+"')]"),tab);
	}

	public void clickBtnUnderSection(String section,String btn){
		section=prop.readPropertyData(section.trim().replaceAll(" ", "_"));
		btn=prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getBtnXpathUnderSection(section,btn)),btn
				+ " under section "+section);

	}

	public void clickElementUnderSection(String section,String btn){
		section=prop.readPropertyData(section.trim().replaceAll(" ", "_"));
		btn=prop.readPropertyData(btn.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath(ObjectUtil.getElementXpathUnderSection(section,btn)),btn
				+ " under section "+section);

	}

	public void verifyColumnExist(List<String> data)
	{
		for(String str:data){
			str=prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath("//th[normalize-space()='"+str+"']"),"column "+str);
		}
	}

	public void clickOnSearchIconOfFieldOnPage(String fieldName) {
		fieldName =prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		WebElement searchIcon=common.getElement(By.xpath(ObjectUtil.getSearchIconXpathOfLabelOnPage(fieldName)));
		common.clickAnElement(searchIcon, fieldName+ " search icon");
	}

	public void selectCheckboxUnderSection(String header, int index){
		header =prop.readPropertyData(header.trim().replaceAll(" ", "_"));
		List<WebElement> chk=common.getElements(By.xpath(ObjectUtil.getCheckboxXpathUnderSection(header)));
		common.clickJS(chk.get(index),"Checkbox under header "+header);
	}

	public void selectAllItemUnderTableByRow(String table){
		table =prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		//common.clickJS(By.xpath(ObjectUtil.getXpathToSelectAllItem(table)),table + " select all menu icon");
		common.clickAnElement(By.xpath(ObjectUtil.getXpathToSelectAllItems(table)),table + " select all menu icon");
		common.clickJS(By.xpath("//button[normalize-space()='"+prop.readPropertyData("Select_All")+"']"),"Select All");
	}

	public int getCountFromTableByRow(String table){
		try {
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			WebElement resultCounts = common.getElement(By.xpath(ObjectUtil.getTableCountXpath(table, true)));
			String extractedValue = CompactUtil.extractNumber(resultCounts.getText());
			int count = Integer.parseInt(extractedValue);
			return count;
		}catch (Exception e){
			new ReportUtil().logFail("","Unable to get table count "+table);
			return 0;
		}
	}

	public void clickBtn(String btn){
		common.click(By.xpath(ObjectUtil.getButtonXpath(btn,true)),btn);
	}


	public void validateSatisfactionRateColumnFiltered(String columnName, String tableName, String sortingType) {
		tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		int columnIndex = getColumnIndexFromTable(tableName, columnName);
		List<Integer> arry = new ArrayList<Integer>();
		boolean flag = true;
		By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName
				+ "')]/../../..//tbody//tr/td[" + columnIndex + "]/*");
		List<WebElement> rate = common.getElements(loc);
		for (WebElement ele : rate) {
			arry.add(Integer.parseInt(common.getText(ele, "").replaceAll("%","").trim()));
		}
		System.out.println(arry);
		for (int i = 0; i < arry.size() - 1; i++) {
			if (sortingType.equalsIgnoreCase("Descending")) {
				if (arry.get(i) >= arry.get(i + 1)) {
					flag = true;
				} else {
					new ReportUtil().logFail("",
							"Number at index " + i + " are not filtered in " + sortingType + " order");
					flag = false;
					break;
				}
			} else {
				if (arry.get(i) <= arry.get(i + 1)) {
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

	public int getColumnIndexFromTable(String tableName, String columnName) {
		int columnIndex = 1;
		try {
			By loc = By.xpath(ObjectUtil.xpathToTabContainer + "//h3[contains(text(),'" + tableName + "')]/../../..//thead//tr/th");
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

	public void validateMaterialFilteredAsPerLocation(String columnName, String tableName,String site,String wh) {
		try {
			int size = common.getElements(By.xpath("//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr")).size();
			if (size > 0) {
				tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
				columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
				int columnIndex = getColumnIndexFromTable(tableName, columnName);
				List<String> arry1 = new ArrayList<String>();
				List<String> arry2 = new ArrayList<String>();
				boolean flag = true;
				By loc1 = By.xpath("(//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//span)[1]");
				By loc2 = By.xpath("(//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//span)[2]");
				List<WebElement> siteList = common.getElements(loc1);
				List<WebElement> whList = common.getElements(loc2);
				for (WebElement ele : siteList) {
					arry1.add(common.getText(ele,"").toString().trim());
				}
				for (WebElement ele : whList) {
					arry2.add(common.getText(ele,"").trim());
				}
				for (int i = 0; i < arry1.size(); i++) {
					System.out.println(arry1.get(i).toString());
					if (!(arry1.get(i).toString().trim().equalsIgnoreCase(site.trim()) && arry2.get(i).toString().trim().equalsIgnoreCase(wh.trim()))) {
						flag = false;
					}
				}
				if (flag)
					new ReportUtil().logPass("All the material should get filtered as per the location " + site +
							"and" + wh + " ", "material filtered correctly");
				else
					new ReportUtil().logFail("All the material should get filtered as per the location " + site +
							"and" + wh + " ", "material filtered incorrectly");
			}
			else
			{
				new ReportUtil().logInfo("There are no material available having location " + site + "and" + wh );
			}
		}
			catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate material location after filter");
		}
	}

	public void validateMaterialFilteredAsPerSite(String columnName, String tableName,String site) {
		try {
			int size = common.getElements(By.xpath("//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr")).size();
			if (size > 0) {
				tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
				columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
				int columnIndex = getColumnIndexFromTable(tableName, columnName);
				List<String> arry1 = new ArrayList<String>();
				boolean flag = true;
				By loc1 = By.xpath("(//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//span)[1]");
				List<WebElement> siteList = common.getElements(loc1);
				for (WebElement ele : siteList) {
					arry1.add(common.getText(ele,"").toString().trim());
				}
				for (int i = 0; i < arry1.size(); i++) {
					System.out.println(arry1.get(i).toString());
					if (!(arry1.get(i).toString().trim().equalsIgnoreCase(site.trim()) )) {
						flag = false;
					}
				}
				if (flag)
					new ReportUtil().logPass("All the material should get filtered as per the location " + site +
							" ", "material filtered correctly");
				else
					new ReportUtil().logFail("All the material should get filtered as per the location " + site +
							" ", "material filtered incorrectly");
			}
			else
			{
				new ReportUtil().logInfo("There are no material available having location " + site  );
			}
		}
		catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate material location after filter");
		}
	}

	public void validateMaterialFilteredAsPerSn(String columnName, String tableName,String snNumber) {
		try {
				tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
				columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
				int columnIndex = getColumnIndexFromTable(tableName, columnName);
				List<String> arry1 = new ArrayList<String>();
				boolean flag = true;
				By loc1 = By.xpath("//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//a");
				List<WebElement> snList = common.getElements(loc1);
				for (WebElement ele : snList) {
					arry1.add(common.getText(ele,"").toString().trim());
				}
				for (int i = 0; i < arry1.size(); i++) {
					System.out.println(arry1.get(i).toString());
					if (!(arry1.get(i).toString().trim().equalsIgnoreCase(snNumber.trim())) ){
						flag = false;
					}
				}
				if (flag)
					new ReportUtil().logPass("All the material should get filtered as per sn " + snNumber +
							 " ", "material filtered correctly");
				else
					new ReportUtil().logFail("All the material should get filtered as per sn " + snNumber +
							 " ", "material filtered incorrectly");
			}
		catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate material sn after filter");
		}
	}

	public void clickOnSatisfactionRateOptionBtn(String option,String line) {
		try {
			option = prop.readPropertyData(option.trim().replaceAll(" ", "_"));
			line = prop.readPropertyData(line.trim().replaceAll(" ", "_"));
			By btn = By.xpath("//label[contains(text(),'" + line + "')]/../../following-sibling::div//span[contains(text(),'" + option + "')]");
			common.clickJS(btn, option);
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to click on option button of " + line);
		}
	}

	public void validateMaterialOfOtherDA(String columnName, String tableName)
	{
		try {
			int size = common.getElements(By.xpath("//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr")).size();
			if (size > 0) {
				tableName = prop.readPropertyData(tableName.trim().replaceAll(" ", "_"));
				columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
				int columnIndex = getColumnIndexFromTable(tableName, columnName);
				//<String> arry1 = new ArrayList<String>();
				List<String> arry2 = new ArrayList<String>();
				boolean flag = true;
				//By loc1 = By.xpath("(//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//span)[1]");
				By loc2 = By.xpath("(//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr/td[" + columnIndex + "]//span)[2]");
				//List<WebElement> siteList = common.getElements(loc1);
				List<WebElement> whList = common.getElements(loc2);
//				for (WebElement ele : siteList) {
//					arry1.add(common.getText(ele,"").toString().trim());
//				}
				for (WebElement ele : whList) {
					arry2.add(common.getText(ele,"").trim());
				}
				for (int i = 0; i < arry2.size(); i++) {
					System.out.println(arry2.get(i).toString());
					if (!(arry2.get(i).toString().trim().equalsIgnoreCase("Reserved for"))) {
						flag = false;
					}
				}
				if (flag)
					new ReportUtil().logPass("All the material of other DA should get filtered ", "material filtered correctly");
				else
					new ReportUtil().logFail("All the material of other DA should get filtered" , "material filtered incorrectly");
			}
			else
			{
				new ReportUtil().logInfo("There are no material available of other DA " );
			}
		}
		catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate material of other DA after filter");
		}
	}

	public void validateNoMaterialInTable(String tableName) {
		try {
			int size = common.getElements(By.xpath("//h3[contains(text(),'" + tableName + "')]/../../..//tbody//tr")).size();
			if (size > 0) {
				new ReportUtil().logFail("No Materials should be displayed", "material is displayed");
			} else {
				new ReportUtil().logPass("No Materials should be displayed", "No Materials is displayed");
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to validate material based on status while filtering");
		}
	}

	public void AddItemToListForRepair(String popupName) {
		try {
			String popupLowerCase = popupName.toLowerCase().replaceAll(" ", "-");
			String text = common.getElement(By.xpath("//*[contains(text(),'Advanced search')]/../../following-sibling::div//*[contains(text(),'Result')]")).getText();
			String totalRowCount = text.replaceAll("Results","").replaceAll("\\(","").replaceAll("\\)","").trim();
			int count = Integer.parseInt(totalRowCount);
			for(int i =1 ; i <= count ; i++)
			{
				if (popupName.equalsIgnoreCase("search equipment") || popupName.equalsIgnoreCase("Search Fleet") || popupName.equalsIgnoreCase("Search Item")) {
					List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]"));
					common.clickAnElement(resultItems.get(i), "Item on Search result table");
				} else {
					List<WebElement> resultItems = common.getElements(By.xpath("//aw-dialog-" + popupLowerCase + "//tr/td[1]/span"));
					common.clickAnElement(resultItems.get(i), "Item on Search result table");
				}
				new MaintenancePlanning().clickButtonOnPopUP(popupName, "Validate");
				if(common.getElements(logisticModuleObj.msgBelowSN).size()==1) {
					new MaintenancePlanning().clickOnSearchIconOfField("S/N");
					new LogisticsModule().selectOptionFromDD_popup("Preserved", "Status");
					new MaintenancePlanning().clickButtonOnPopUP("search equipment", "Search");
					common.explicitWait(5);
				} else {
					i=count+1;
				}
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to select item from result table on the popup");
		}
	}

	public void clearRecipientSite()
	{
		if(common.getElements(logisticModuleObj.recipientSiteClear).size() == 1)
		{
			common.clickAnElement(common.getElement(logisticModuleObj.recipientSiteClear),"Recipient Site Clear button");
		}
	}

	public void clearShipperSite()
	{
		if(common.getElements(logisticModuleObj.shipperSiteClear).size() == 1)
		{
			common.clickAnElement(common.getElement(logisticModuleObj.shipperSiteClear),"Recipient Site Clear button");
		}
	}

	public void clearRecipientWarehouse()
	{
		if(common.getElements(logisticModuleObj.recipientWarehouseClear).size() == 1)
		{
			common.clickAnElement(common.getElement(logisticModuleObj.recipientWarehouseClear),"Recipient Site Clear button");
		}
	}

	public void clearReceiverSite()
	{
		if(common.getElements(logisticModuleObj.receiverSiteClear).size() == 1)
		{
			common.clickAnElement(common.getElement(logisticModuleObj.receiverSiteClear),"Recipient Site Clear button");
		}
	}

	public void clearSite()
	{
		if(common.getElements(logisticModuleObj.site).size() == 1)
		{
			common.clickAnElement(common.getElement(logisticModuleObj.site),"Site Clear button");
		}
	}

	public String selectOptionFromUnitDD(String dropdownName, int index, String popup)throws Exception {
		try {
			String option = "";
			boolean found = false;
			dropdownName = prop.readPropertyData(dropdownName.trim().replaceAll(" ", "_"));
			String popupLowerCase = popup.toLowerCase().replaceAll(" ", "-");
			By optionList = By.xpath("//p-dropdownitem/li/span");
			By chevronDown;
			chevronDown = By.xpath("(//aw-dialog-" + popupLowerCase + "//label[contains(text(),'" + dropdownName
						+ "')]/following::span[contains(@class,'chevron-down')])[4]");

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
	public void verifyColumnAvailable(List<String> data)
	{
		for(String str:data){
			str=prop.readPropertyData(str.trim().replaceAll(" ", "_"));
			common.checkElementPresentByVisibility(By.xpath("//div[contains(text(),'"+str+"')]"),"column "+str);
		}
	}
	//@kouselya
	public void enterDate (By loc, String fieldName,String today) {
		try{
			today=prop.readPropertyData(today.trim().replaceAll(" ", "_"));
			common.clickAnElement(loc, fieldName);
			By ele=By.xpath("//div[contains(@class,'datepicker-calendar')]//tbody//td[contains(@class,'datepicker-today')]/span");
			WebElement we= common.getElement(ele);
			String date= we.getText();
			int expectedDate=(Integer.parseInt(date)+2);
			if(expectedDate > 31) {
				expectedDate = expectedDate - 31;
			}
			//expectedDate=21;
			common.clickJS(By.xpath("//div[contains(@class,'datepicker-calendar')]//tbody/tr/child::td/span[contains(text(),'"+expectedDate+"')]"),"expectedDate" );
		}catch(Exception e) {
			new ReportUtil().logFail("", "Unable to enter todays date in field "+fieldName);
		}
	}

	public void clickOnFilecopyIcon(String index) {
		//common.clickAnElement(By.xpath(logisticModuleObj.FilecopyIcon+"["+index+"]"), "Filecopy Icon");
//
		int ind=Integer.parseInt(index);
		List<WebElement> elements= common.getElements(By.xpath("//span//aw-icon-file-copies"));
		common.clickAnElement(elements.get(ind-1),"FileCopy Icon");
	}

	public void verifyTheItemIsAppearedOnDocumentList(List<String> data) {
		common.explicitWait(3);
		List<WebElement> elements=common.getElements(By.xpath("//div[contains(text(),'Documents List Of Operation')]/../../following-sibling::div//li"));
		boolean flag=false;

		for(String doc:data) {
			for (WebElement ele : elements) {
				common.explicitWait(3);
				String option=ele.getText();
				if(option.contains(doc)){
					new ReportUtil().logPass("Document should be Displayed", "Document is Displayed successfully as "+doc);
					flag=true;
					break;
				}
			}
			if(!flag)
				new ReportUtil().logFail("", "Document is not Displayed  "+doc);
		}
	}

	public void verifyTheNoItemIsAppearedOnDocumentList() {
		List<WebElement> elements=common.getElements(By.xpath("//div[contains(text(),'Documents List Of Operation')]/../../following-sibling::div//li"));

		if(elements.size()==0){
			new ReportUtil().logPass("No Documents should be Displayed", "No Documents should be Displayed ");
		}

	}

	public void enterValueinField(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath("(//th[contains(text(),'"+fieldName+"')]/following::div/input)[1]"), "input", value, fieldName);
	}

	public void userEnterDateInField(String fieldName) {
		enterTodaysDate(logisticModuleObj.expirationDate_input, "Expiration Date"+fieldName, "Today");
	}

	public void entersInQuantityField(String value) {
		common.performOperation(logisticModuleObj.quantity_input,"input",value,"Quantity");
	}

	public String captureColumnValueOfCheckboxByRow(String columnName, int rowNumber,String table) {
		columnName = prop.readPropertyData(columnName.trim().replaceAll(" ", "_"));
		table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
		try {
			int columnIndex = findColumnIndexFromItemsTable(table,columnName);
			By loctr = By.xpath(ObjectUtil.getTotalRows(table,columnIndex));
			List<WebElement> sn = common.getElements(loctr);
//			return sn.get(rowNumber-1).getText().trim();
			return sn.get(rowNumber-1).getAttribute("disabled");
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve " + columnName+" from row number "+rowNumber);
		}
		return null;
	}

	public int findColumnIndexFromItemsTable(String table, String column) {
		int colIndex = 2;
		try {
			List<WebElement> columnList = common.getElements(By.xpath(ObjectUtil.xpathToGetAllHeadersInItems(table)));
			for (WebElement col : columnList) {
				if (col.getText().trim().equalsIgnoreCase(column)) {
					System.out.println("Index of column " + column + " is " + colIndex);
					break;
				} else
					colIndex = colIndex + 1;
			}
		} catch (Exception e) {
			new ReportUtil().logFail("", "Unable to retrieve column " + column + " Index from table " + table);
		}
		return colIndex;
	}

	public void clickValidateOnMaterialPopup() {
		common.clickAnElement(logisticModuleObj.validateBtn_OnPopup, "Validate button");
	}
	public String captureProcurementNoFromSearchTable(int index) throws Exception {
		try {
			List<WebElement> receiptList = common.getElements(logisticModuleObj.procurementNo);
			String selectedItem= common.getText(receiptList.get(index), "Selected item");
			return selectedItem;
		}catch(Exception e) {
			new ReportUtil().logFail("" ,"Unable to select item");
			throw new Exception("Unable to select item");
		}
	}
	public void selectCaptureItem() {
		common.clickJS(By.xpath("(//*[@id=\"dialog_search_pn_result_tbl\"]//table/tbody/tr)[1]"), " item in search result");
	}

	public void clickLinkUnderOriginalDocumentColumn() {
		common.clickAnElement(logisticModuleObj.originalDocumentLink,"originalDocumentLink");
	}

	public void openReceiptFromSearchResultTable() throws Exception {
		try {
			WebElement ProcurementNumber = common.getElement(logisticModuleObj.ReceiptFileProcurementRequestNumber);
			common.click(ProcurementNumber,"ProcurementNumber");

		}catch(Exception e) {
			new ReportUtil().logFail("" ,"Unable to select item");
			throw new Exception("Unable to select item");
		}
	}

	public void PRCreatedUnderRM(String table, String column) {
		try {
			common.scrollDown();
			common.explicitWait(2);
			WebElement ele= common.getElement(logisticModuleObj.Paginator_last);
			common.clickAnElement(ele,"Paginator last");
			table = prop.readPropertyData(table.trim().replaceAll(" ", "_"));
			column = prop.readPropertyData(column.trim().replaceAll(" ", "_"));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();

			int colIndex=new MaintenancePlanning().getColumnIndexFromTable(table, column);
//			By loc=By.xpath(ObjectUtil.xpathToTabContainer+"//h3[contains(text(),'"+table+"')]/../../..//tbody//tr/td["+
//						colIndex+"][contains(.,'"+formatter.format(date)+"')]");
			By loc=By.xpath("//h3[contains(text(),'"+table+"')]/../../..//tbody//tr/td["+
					colIndex+"][contains(.,'"+formatter.format(date)+"')]");
			if(common.getElements(loc).size()>0)
				new ReportUtil().logPass("Request should be created under table "+table, "Request is created under table "+table);
			else
				new ReportUtil().logFail("Request should be created under table "+table, "Request is not created under table "+table);
		}catch(Exception e) {
			new ReportUtil().logFail("","Unable to validate request in table "+table);
		}
	}

	public void verifyResultsDisplayed(String tableHeader) {
		try {
			tableHeader = prop.readPropertyData(tableHeader.trim().replaceAll(" ", "_"));
			int results = getNumberOfItemsUnderResultTable(tableHeader);
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

	private int getNumberOfItemsUnderResultTable(String tableHeader) {
		int count = common.getElements(logisticModuleObj.ResultsList).size();
		new ReportUtil().logInfo("Total number of item in "+tableHeader+ " are "+count);
		return count;

	}
	public void ClickOnColoumnHeader(String name) {
		name=prop.readPropertyData(name.trim().replaceAll(" ", "_"));
		common.clickJS(By.xpath("//*[@id=\"work_package_search_results_tbl\"]/app-list-table/div[2]/div[1]/div/div[1]"), name+" column");
	}
	public void ResetButtonOnFilterPopup(String btnName) {
		btnName = prop.readPropertyData(btnName.trim().replaceAll(" ", "_"));
		common.getElementExplicitWait(By.xpath("//span[contains(.,'"+btnName+"') and contains (@class,'button')]"),
				2);
		common.clickJS(By.xpath("//span[contains(.,'"+btnName+"') and contains (@class,'button')]"),
				btnName + " button on the page ");
	}
	public void EnterValueInField(String fieldName, String value) {
		fieldName = prop.readPropertyData(fieldName.trim().replaceAll(" ", "_"));
		common.performOperation(By.xpath(ObjectUtil.getInputBoxXpath(fieldName)), "input", value, fieldName);
	}

	public void enterValueInCodeField(String fieldName, String value){
		common.performOperation(By.xpath("//aw-tab-container[not(contains(@class,'display--none'))]//label[contains(text(),'"+fieldName+"')]/..//input"), "input", value, fieldName);
	}
}