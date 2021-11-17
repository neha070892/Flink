package com.aeroweb.utils;

import org.openqa.selenium.By;

public class ObjectUtil {

	public static String xpathToTabContainer="//aw-tab-container[@class='ng-star-inserted']";

	public static String xpathToDeploymentContainer="//aw-deployment-batch-real-status";
	
	/**
	 * Gets Xpath of all the button. Just pass name of the button
	 * 
	 * @param buttonText
	 * @return
	 */
	public static String getButtonXpath(String buttonText) {
		return "//span[contains(.,'" + buttonText + "') and contains (@class,'button')]";
	}

	public static String getStatusButtonXpath(String buttonText) {
		return xpathToDeploymentContainer+"//span[contains(.,'" + buttonText + "') and contains (@class,'button')]";
	}

	public static String getButtonXpath(String buttonText,boolean container) {
		return xpathToTabContainer+"//span[contains(.,'" + buttonText + "') and contains (@class,'button')]";
	}

	/**
	 * gets xpaths for pageHeading
	 * 
	 * @param heading
	 * @return
	 */
	
	public static String getXpathForPageHeading(String heading) {
		return "//h2[contains(text(),'"+heading+"')]";
		//  "//h2[contains(text(),'"+heading+"')]"
		
	}

	/**
	 * perform click on dropdown and then selects value
	 * 
	 * @param sectionHeading
	 * @param labelName
	 * @param valueToSelect
	 * @param generictext
	 */
	public static void SelectValueDromDropDown(String sectionHeading, String labelName, String valueToSelect,
			String generictext) {
		String xpathToDropDown = "//h4[contains(.,'" + sectionHeading + "')]/..//label[contains(.,'" + labelName
				+ "')]/..//div[contains(@class,'dropdown-trigger')]";
		String xpathToValue = "//li/span[contains(.,'" + valueToSelect + "')]";
		commonMethods common= new commonMethods();
		common.clickJS(By.xpath(xpathToDropDown), generictext);
		common.clickJS(By.xpath(xpathToValue), valueToSelect);

	}
	
	public static String getInputboxXpath(String label) {
		return "//label[contains(text(),'"+label+"')]/..//input";
	}
	
	public static String getTextAreaXpath(String label) {
		return "//label[contains(text(),'"+label+"')]/..//textarea";
	}
	
	public static String getPopupHeaderXpath(String label) {
		return "//a-header/child::*[contains(.,'"+label+"')]";
	}
	
	public static String getResultListXpath(String label) {
		return xpathToTabContainer+"//h3[contains(text(),'"+label+"')]/../../..//tbody/tr";
	}
	
	public static String getResultCountFromTableHeader(String label) {
		return xpathToTabContainer+"//h3[contains(text(),'"+label+"')]";
	}
	
	public static String getLabelXpath(String label) {
		return xpathToTabContainer+"//label[contains(text(),'"+label+"')]";
	}
	
	public static String getSearchIconXpathOfLabel(String label) {
		return "//label[contains(text(),'"+label+"')]/..//i[contains(@class,'search')]";
	}

	//..Created new xpath
	public static String getSearchIconXpathLabel(String label) {
		return "//label[contains(text(),'"+label+"')]/..//*[contains(@class,'aw-icon')]";
	}

//	public static String getSearchIconXpathOfLabelOnPage(String label) {
//		return xpathToTabContainer+"//label[contains(text(),'"+label+"')]/..//i[contains(@class,'search')]";
//	}

	public static String getSearchIconXpathOfLabelOnPage(String label) {
		return xpathToTabContainer+"//label[contains(text(),'"+label+"')]/..//aw-icon-search[contains(@class,'aw-icon')]";
	}

	public static String getButtonXpathOnPopup(String popup,String btn) {
		return "//aw-dialog-"+popup+"//button/span[contains(.,'"+btn+"')]";
	}
	
	public static String getInputboxXpathOnPopup(String label,String popup) {
		return "//aw-dialog-"+popup+"//label[contains(text(),'"+label+"')]/..//input";
	}
	
	public static String getButtonXpathTagBtn(String buttonText) {
		return "//button[contains(.,'" + buttonText + "')]";
	}
	
	public static String getElementXpath(String name) {
		return "//*[contains(text(),'"+name+"')]";
	}
	
	public static String getGenericXpathForBtn(String name) {
		return "//*[contains(text(),'"+name+"')][contains(@class,'button')]";
	}
	
	public static String getElementXpathOnPopup(String name) {
		return "//a-modal//span[contains(.,'"+name+"')]";
	}
	
	public static String getDropdownXpathOnPopup(String name) {
		return "//a-modal//label[contains(text(),'"+name+"')]/parent::*//*[contains(@class,'chevron-down')]";
	}
	public static String getWorkOrderDropdownXpathOnPopup(String name) {
		return "(//a-modal//label[contains(text(),'Workshop')]/parent::*//*[contains(@class,'chevron-down')])[2]";
	}
	
	public static String getBtnXpathOnStatusPopup(String name) {
		return "//aw-object-status//button[contains(.,'"+name+"')]/span";
	}
	
	public static String getInputboxXpathOnPopup(String label) {
		return "//a-modal//label[contains(text(),'"+label+"')]/..//input";
	}

	public static String getInputboxXpathonPopup(String label) {
		return "//a-modal//label[contains(text(),'"+label+"')]/..//textarea";
	}
	
	public static String getTableCountXpath(String table) {
		return xpathToTabContainer+"//h3[contains(.,'"+table+"')]/../../..//span[contains(@class,'table-count')]";
	}
	
	public static String getTotalRowsXpath(String table) {
		return "//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'table-row')]";
	}
	
	public static String selectedRowXpath(String table) {
		return "//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'row selected')]";
	}
	
	public static String xpathToGetAllHeaders(String table) {
		return xpathToTabContainer+"//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-header')]//div[contains(@class,'cell-content')]";
	}
	
	public static String xpathToGetSelectedRowColumnValues(String table) {
		return "//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'row selected')]//div[contains(@class,'content')]";
	}
	
	public static String getTotalRowsXpath(String table,int colIndex) {
		return "//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'table-row')and not(contains(@class,'hidden'))]/div["+
				colIndex+"]/div";
	}
	
	public static String searchIconXpathOnPopup() {
		return "//a-modal//i[contains(@class,'icon-search')]";
	}

	public static String getDropdownXpathUnderSection(String section,String name) {
		return "//*[normalize-space()='"+section+"']/..//label[contains(text(),'"+name+"')]/parent::*//*[contains(@class,'chevron-down')]";
	}

	public static String getDropdownXpathUnderSection(String section,String name,boolean following) {
		return "//*[normalize-space()='"+section+"']/..//label[contains(text(),'"+name+"')]/following::*[contains(@class,'chevron-down')][position()=1]";
	}

	public static String getXpathOfButtonOnPopup(String popupName,String btnName){
		return "//a-modal//div[contains(text(),'"+popupName+"')]/../../..//span[contains(.,'"+btnName
						+"') and contains (@class,'button')]";
	}

	public static String getCheckboxCheckedXpath(String label){
		return xpathToTabContainer+"//label[normalize-space()='"+label+"']/..//div[contains(@class,'highlight')]";
	}

	public static String getBtnXpathUnderSection(String section,String btn){
		return "//h3[normalize-space()='"+section+"']/../../..//span[contains(.,'"+btn+"')]";
	}

	public static String getFormLabelXpath(String label){
		return xpathToTabContainer+"//*[contains(.,'"+label+"')][contains(@class,'form-label')]";
	}

	public static String getTotalRowsXpathOnPopup(int colIndex) {
		return "//a-modal//div[contains(@class,'table-body')]//div[contains(@class,'table-row')]/div["+colIndex+"]";

	}

	public static String getTotalRowsXpathByLink(String table,int colIndex) {
		return "//h3[contains(.,'"+table
				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'table-row')]/div["+
				colIndex+"]//a";
	}

	public static String statusXpath(String status){
		return ObjectUtil.xpathToTabContainer+"//div[contains(@class,'object-status')][contains(.,'"+status+"')]";
	}

	public static String getElementXpathUnderSection(String section,String btn){
		return xpathToTabContainer+"//h3[contains(.,'"+section+"')]/../../..//span[normalize-space()='"+btn+"']";
	}

	public static String getCheckboxXpathUnderSection(String header){
		return xpathToTabContainer+"//h3[contains(.,'"+header+"')]/../../..//tbody/tr//span[contains(@class,'p-checkbox')]";
	}

	public static String getTableCountXpath(String table,boolean position) {
		return  "("+xpathToTabContainer+"//h3[contains(.,'"+table+"')]/../../..//span[contains(@class,'table-count')])[position()=1]";
	}

	public static String getXpathToSelectAllItem(String table){
		return "("+xpathToTabContainer+"//h3[contains(.,'Visits')]//following::mat-icon[.='menu'])[position()=1]";
	}

	//..created new xpath
	public static String getXpathToSelectAllItems(String table){
		return xpathToTabContainer+"//h3[contains(.,'"+table+"')]//following::button[contains(@class,'mat-menu-trigger')]";
	}
	public static String getResultTableXpath(String label) {
		return xpathToTabContainer+"//h3[contains(text(),'"+label+"')]/../../following::div//div[contains(@class,'yac-table-body')]//div[contains(@class,'yac-table-row')][not(contains(@class,'hidden'))]";
	}

	public static String xpathToGetAllHeadersInItems(String table) {
		return xpathToTabContainer+"//p-table//th";
	}

	public static String getTotalRows(String table,int colIndex) {
		return "//tr[contains(@class,'ng-star-inserted')]";
//		return "//h3[contains(.,'"+table
//				+"')]/../../..//div[contains(@class,'table-body')]//div[contains(@class,'table-row')and not(contains(@class,'hidden'))]/div["+
//				colIndex+"]/div";
	}
	public static String getInputBoxXpath(String label) {
		return "(//label[contains(text(),'"+label+"')]/..//input)[2]";
	}
}
