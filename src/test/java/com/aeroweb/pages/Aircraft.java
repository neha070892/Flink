package com.aeroweb.pages;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aeroweb.dataManager.Constant;
import com.aeroweb.objects.AircraftPageObjects;
import com.aeroweb.utils.CompactUtil;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.JsonUtils;
import com.aeroweb.utils.PropertyUtil;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;
import com.google.common.io.Files;

public class Aircraft {
	PropertyUtil prop;
	AircraftPageObjects aircraftPageObj;
	commonMethods common;

	public Aircraft() {
		common = new commonMethods();
		aircraftPageObj= new AircraftPageObjects();
		common= new commonMethods();
		String propertyFilePath = "src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
		this.prop = new PropertyUtil(propertyFilePath);
		
	}

	

	public boolean checkOnCorectPage() {
		WebElement ele = common.getElementExplicitWait(aircraftPageObj.pageHeading);

		if (ele != null)
			return true;
		else
			return false;
	}

	String relativeXpathToAircraftName = "./../../td[2]/span";
	public void verifyOperatorInEachResult(String operator) {
		closeAllExpandButtons();
		List<WebElement> expandButtons = DriverUtil.getInstance().getDriver().findElements(aircraftPageObj.expandButton);

		for (WebElement btn : expandButtons) {
			if (!btn.getAttribute("class").contains("minus")) {
				btn.click();
				WebElement nameOfBtn = btn.findElement(By.xpath(relativeXpathToAircraftName));
				WebElement operatorElement = common.getElementExplicitWait(aircraftPageObj.operatorLable, 0);
				String expectedMsg = "Aircraft " + nameOfBtn.getText() + " should contains Operator " + operator
						+ " or no operator";
				if (operatorElement.getAttribute("value").trim().equalsIgnoreCase(operator))
					new ReportUtil().logPass(expectedMsg,
							"Contains Operator " + operatorElement.getAttribute("value").trim());
				else if (operatorElement.getAttribute("value").trim().length() == 0)
					new ReportUtil().logPass(expectedMsg, "Contains no operator ");
				else
					new ReportUtil().logFail(expectedMsg,
							"Contains Operator " + operatorElement.getAttribute("value").trim());
				btn.click();
			}
		}
		closeAllExpandButtons();
	}

	public void closeAllExpandButtons() {
		List<WebElement> expandButtons = DriverUtil.getInstance().getDriver().findElements(aircraftPageObj.expandButton);
		for (WebElement btn : expandButtons) {
			if (btn.getAttribute("class").contains("minus"))
				btn.click();
		}
	}

}
