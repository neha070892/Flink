package com.aeroweb.pages;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.aeroweb.objects.LoginPageObjects;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.JSWaiter;
import com.aeroweb.utils.PropertyUtil;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;

public class Login {
	
	DriverUtil driverUtil;
	public PropertyUtil prop;
    LoginPageObjects loginPageObj;
    commonMethods common;
    
	

	public Login() {
		common= new commonMethods();
		loginPageObj = new LoginPageObjects();
	}


	public void enterUserName(String userName,String fieldName) {
		common.getElementExplicitWait(loginPageObj.login, 2);
		common.performOperation(loginPageObj.login, "input", userName, fieldName);
	}

	public void enterPassword(String password,String fieldName) {
		common.getElementExplicitWait(loginPageObj.password, 2);
		common.enterSecuredValue(loginPageObj.password, password, fieldName);
	}

	public void clickCartBtn()  {
//		common.getElementExplicitWait(loginPageObj.cart, 2);
		common.clickJS(loginPageObj.cart, "Cart button");
	}

	public void selectLanguage(String lang,String dropdownName) throws Exception {
		if (!common.getElement(loginPageObj.currentLanguage).getText().contains(lang)) {
			String localLang=lang;
			boolean found = false;
			common.clickJS(loginPageObj.langaugeDropDown, dropdownName+" Dropdown");
			List<WebElement> list = common.getElements(loginPageObj.langaugeList);
			if(lang.equalsIgnoreCase("French")) {
				localLang= lang+"Fran�ais";
			}else
				localLang= lang+"Anglais";
				
			for (WebElement ele : list) {
				if (localLang.contains(ele.getText().trim())) {
					ele.click();
					found = true;
					break;
				}
			}
			if (!found) {
				new ReportUtil().logFail(lang + " Should be present in option", lang + " not present in options");
				throw new Exception("Cannot Found Language " + lang);
			}
			new JSWaiter().waitAllRequest();
		}

	}

	public void selectWorkspace(String ws,String dropdownName) throws Exception {
		if (!common.getElement(loginPageObj.currentWorkspace).getText().contains(ws)) {
			boolean found = false;
			common.clickJS(loginPageObj.workspaceDropDown, dropdownName+" Dropdown");
			List<WebElement> list = common.getElements(loginPageObj.workspaceList);
			for (WebElement ele : list) {
				if (ele.getText().equalsIgnoreCase(ws)) {
					ele.click();
					found = true;
					break;
				}
			}
			
			if (!found) {
				new ReportUtil().logFail(ws + " Should be present in Workspace option", ws + " not present in Workspace options");
				throw new Exception("Cannot Found Workspace " + ws);
			}
		}

	}

	
	
	public void enterUsernameAndPassword(String userName, String userNameValue, String password,String passwordValue) {
		enterUserName(userNameValue,userName);
		enterPassword(passwordValue,password);
		common.explicitWait(2);
	}
	
	public void selectWorkspaceAndLanguage(String workspace, String workspaceValue, String language,String languageValue) throws Exception {
		selectWorkspace(workspaceValue,workspace);
		selectLanguage(languageValue,language);
		System.setProperty("appLanguage",languageValue);
		PropertyUtil.propertyFilePath="src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
	}

	public void selectTheLang(String language,String languageValue) throws Exception {
		selectLanguage(languageValue,language);
		System.setProperty("appLanguage",languageValue);
		PropertyUtil.propertyFilePath="src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
	}

	public void logOut() {
		common.click(loginPageObj.accountChevron, "Account down arrow icon");
		common.clickAnElement(loginPageObj.logoutBtn, "Logout button");
		common.clickJS_WithoutJSWait(loginPageObj.popup_YesBtn, "yes button");
		common.getElementExplicitWait(loginPageObj.login, 0);
	}
	
	public void logoutIfLoggedIn() {
		try {
			if (common.getElement(loginPageObj.accountChevron).isDisplayed()) {
				common.clickJS(loginPageObj.accountChevron, "Account chevron icon");
				common.clickJS(loginPageObj.logoutBtn, "logout");
				common.clickJS(loginPageObj.popup_YesBtn, "Yes on confirmation popup");
				common.getElement(loginPageObj.accountChevron).click();
				common.getElement(loginPageObj.logoutBtn).click();
				common.getElement(loginPageObj.popup_YesBtn).click();
				common.getElementExplicitWait(loginPageObj.login, 0);
			}
		} catch (Exception e) {
			common.explicitWait(3);
		}
	}
	public void enterUsernameAndPasswordAndLanguage(String userName, String userNameValue, String password,String passwordValue, String language, String languageValue) throws Exception {
		enterUserName(userNameValue,userName);
		enterPassword(passwordValue,password);
		enterLanguage(languageValue,language);
		common.explicitWait(2);
	}
	public void enterLanguage(String lang,String dropdownName) throws Exception {
		//if (!common.getElement(loginPageObj.currentLanguage).getText().contains(lang)) {

		boolean found = false;
		common.explicitWait(2);
		common.click(loginPageObj.languageInputBox, dropdownName+" Dropdown");
		List<WebElement> list = common.getElements(loginPageObj.languageInputList);

		for (WebElement ele : list) {
			if (lang.contains(ele.getText().trim())) {
				ele.click();
				found = true;
				break;
			}
		}
		if (!found) {
			new ReportUtil().logFail(lang + " Should be present in option", lang + " not present in options");
			throw new Exception("Cannot Found Language " + lang);
		}
		new JSWaiter().waitAllRequest();
		//}

	}
	
	
}
