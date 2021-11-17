package com.aeroweb.stepdefination;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.text.Document;

import org.openqa.selenium.WebDriver;

import com.aeroweb.dataManager.Constant;
import com.aeroweb.objects.LoginPageObjects;
import com.aeroweb.pages.Aircraft;
import com.aeroweb.pages.Dashboard;
import com.aeroweb.pages.Login;
import com.aeroweb.pages.LogisticsModule;
import com.aeroweb.pages.MaintenancePlanning;
//import com.aeroweb.pages.ReceiptFolder;
//import com.aeroweb.pages.ReferentialModule;
//import com.aeroweb.pages.TransverseModule;
import com.aeroweb.utils.CompactUtil;
import com.aeroweb.utils.DriverUtil;
import com.aeroweb.utils.JsonUtils;
import com.aeroweb.utils.ReportUtil;
import com.aeroweb.utils.commonMethods;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



public class LogisticsStepDefs extends commonMethods {
	public WebDriver driver;
	String fileName;
	Login login;
	Dashboard dashboard;
	MaintenancePlanning mPlan;
//	ReceiptFolder receiptFolder;
	Aircraft aircraft;
	LogisticsModule logisticsModule;
    List<String> selectedWorkOrder= new ArrayList<String>();
    commonMethods common;

	public LogisticsStepDefs() {
		login = new Login();
		mPlan = new MaintenancePlanning();
		dashboard = new Dashboard();
//		receiptFolder = new ReceiptFolder();
		aircraft = new Aircraft();
		logisticsModule=new LogisticsModule();
        common= new commonMethods();
	}
    int temp;
    @When("^user grab Current \"(.*?)\" value$")
    public void user_grab_current_temp_value(String temperature) throws Throwable {
       temp=44;
       // temp=logisticsModule.getElement(temperature);
    }

    @When("^add least expensive \"(.*?)\" to cart if weather is \"(.*?)\"$")
    public void navigate_to_page_if_weather_condition(String pageName,String condition) throws Throwable {
        temp=35;
        if(condition.equalsIgnoreCase("below 19") && temp<19)
        {
            dashboard.btnClick(pageName);
            new Dashboard().selectItems("Aloe");
            new Dashboard().selectItems("Almond");
        }

        else if (condition.equalsIgnoreCase("above 34") && temp >34)
        {
            dashboard.btnClick(pageName);
            new Dashboard().selectItems("SPF-50");
            new Dashboard().selectItems("SPF-30");
        }

    }

    @Given("^User navigated to URL$")
    public void user_navigated_to_login_page() throws Throwable {
        common.navigateURL();
    }

	@When("^Select \"(.*?)\" Tab and select \"(.*?)\" option$")
	public void select_Tab_and_select_option(String mainMenuOption, String subMenuOption) throws Throwable {
//		new Dashboard().selectMenuOption(mainMenuOption);
		new Dashboard().selectSubMenuOption(subMenuOption);
	}

	@When("^select criteria \"(.*?)\" in search$")
	public void selects_from(String criteria) throws Throwable {
		new MaintenancePlanning().selectMainCriteria(criteria);
	}

	@When("^click Search button$")
	public void click_Search_button() throws Throwable {
		new Dashboard().performSearch();
	}

	@When("^Search fields are available$")
	public void Search_fields_are_available() throws Throwable {
		new MaintenancePlanning().checkResultsCount();
	}

	@When("^Opens \"(.*?)\" Result field$")
	public void fields_are_available(String criteria) throws Throwable {
		new Dashboard().openSearchResult(criteria); 
	}

	@When("^open search result available at below index$")
	public void open_are_available(DataTable arg1) throws Throwable {
		List<String> data= arg1.asList();
		new Dashboard().openSearchResult(data.get(0));
	}
	
	@When("^open search result available at below index on \"(.*?)\" page$")
	public void open_are_available_JS(String arg,DataTable arg1) throws Throwable {
		List<String> data= arg1.asList();
		new Dashboard().openSearchResultUsingJS(data.get(0));
	}
	
	@When("^open transfer order search result available at below index$")
	public void open_to_are_available(DataTable arg1) throws Throwable {
		List<String> data= arg1.asList();
		new LogisticsModule().openTOResult(data.get(0));
	}
	
	@When("^open \"(.*?)\" search result available at below index$")
	public void open_some_are_available(String criteria,DataTable arg1) throws Throwable {
		List<String> data= arg1.asList();
		new LogisticsModule().openTOResult(data.get(0));
	}
	
	@When("^Open Result number \"(.*?)\"$")
	public void open_search_field(String criteria) throws Throwable {
		new Dashboard().openSearchResult(criteria);
	}

//	@Given("^select recipent site as \"(.*?)\" in search Under Receipt folder$")
//	public void selects_recipent_site_as_in_search_Under_Receipt_folder(String value) throws Throwable {
//		new ReceiptFolder().selectRecieptSite(value);
//	}

	@Then("^verify Search results are available for \"(.*?)\"$")
	public void verify_Search_results_are_available(String arg1) throws Throwable {
		new MaintenancePlanning().waitTillDataLoaded();
		new Dashboard().checkResultsPresent();
	}

//	@When("^select \"(.*?)\" tab in Receipt Folder page$")
//	public void selects_tab_in_Receipt_page(String tabName) throws Throwable {
//		new ReceiptFolder().selectTabOnReceiptPage(tabName);
//	}

//	@Then("^verify upload button should be present$")
//	public void upload_button_should_be_present() throws Throwable {
//		new ReceiptFolder().checkUploadButtonExist();
//	}

//	@When("^User uploads a document on Receipt page of type \"(.*?)\"$")
//	public void user_uploads_a_document_on_Receipt_page(String fileType) throws Throwable {
//		fileName = new ReceiptFolder().uploadFile(fileType);
//	}
	
//	@When("^user uploads a document on Receipt Folder page$")
//	public void user_uploads_a_document_on_Receipt() throws Throwable {
//		fileName = new ReceiptFolder().uploadFile(new JsonUtils().getJsonValue("FileNameToUpload"));
//	}

//    @When("^user uploads a document on equipment page$")
//    public void user_uploads_a_document_on_equipment() throws Throwable {
//        fileName = new ReferentialModule().uploadFile_NoConfrmMsg(new JsonUtils().getJsonValue("FileNameToUpload"));
//    }
	 
	@When("^user deletes all the available documents$")
	public void user_delete_all_document() throws Throwable {
	    commonMethods.explicitWaiting(4);
		String cases=new LogisticsModule().selectAllSearchResult("Receipt", "Documents");
		if(!cases.equalsIgnoreCase("0")) {
			new LogisticsModule().clickOnBtn("delete");
            new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
		}
	}

	@When("^user closes the Receipt page$")
	public void user_closes_the_Receipt_page() throws Throwable {
		new Dashboard().goBack();
	}

//	@Then("^verify uploded Document Should be visible$")
//	public void uploded_Document_Should_be_available() throws Throwable {
//		new ReceiptFolder().checkFileExistInDocument(fileName);
//	}

//	@Then("^user select the uploaded document$")
//	public void user_should_select_the_uploaded_document() throws Throwable {
//		new ReceiptFolder().checkSelectFile(fileName);
//	}

//    @Then("^user select the uploaded Document$")
//    public void user_should_select_the_uploaded_Document() throws Throwable {
//        new ReceiptFolder().checkAndSelectFile(fileName);
//    }

	@Then("^Verify operator for each Aircraft is \"(.*?)\" or empty$")
	public void verify_operator_for_each_Aircraft_is_f_or_empty(String arg1) throws Throwable {
		new Aircraft().verifyOperatorInEachResult(arg1);
	}
	
	

//	@When("^click on Download button to download the file$")
//	public void click_on_Download_button_to_download_the_file() throws Throwable {
//		new ReceiptFolder().clickDownload();
//		//new ReceiptFolder().handleOpenSaveDownloadWindow(fileName);
//	}
//
//	String downloadedFile;
//	@Then("^file Should be downloaded$")
//	public void file_Should_be_downloaded() throws Throwable {
//		downloadedFile=new ReceiptFolder().verifyFileDownloaded();
//	}

//	@Then("^user opens the downloaded file, verifies it and close it$")
//	public void user_opens_the_downloaded_file_verify_it_and_close_it() throws Throwable {
//		new ReceiptFolder().verifyFile(downloadedFile);
//
//	}
	
	
	@Then("^verify \"([^\"]*)\" popup appears$")
    public void verify_something_popup_appears(String strArg1) throws Throwable {
        new LogisticsModule().validateCreateShipmentPopup(strArg1);
    }

    @And("^verify the below fields are available$")
    public void verify_the_below_fields_are_available(DataTable arg1) throws Throwable {
    	List<String> data= arg1.asList();
    	new LogisticsModule().validateElementsOnCreateShipmentPopup(data);
    }
    
    @And("^verify the content of the sender field is prefilled with the store to which the user is attached$")
    public void verify_the_content_of_the_sender_field_is_prefilled_with_the_store_to_which_the_user_is_attached() throws Throwable {
        new LogisticsModule().verifySenderOptionsAreAvailable();
    }

    @And("^verify the content of the \"([^\"]*)\" field is prefilled with today's date$")
    public void verify_the_content_of_the_something_field_is_prefilled_with_todays_date(String strArg1) throws Throwable {
    	new LogisticsModule().validateExpectedDateField();
    }
    
    List<String> optionValue= new LinkedList<String>();
    @When("^user select \"([^\"]*)\" in Shipment Type dropdown$")
    public void user_select_something_in_something(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectShipmentType(strArg1);
    }
   
    @And("^user select \"([^\"]*)\" in Shipper Site dropdown$")
    public void user_select_something_in_shipper_site_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectShipperSite(strArg1);
    }

    @And("^user select \"([^\"]*)\" in Shipper Warehouse dropdown$")
    public void user_select_something_in_shipper_warehouse_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectShipperWarehouse(strArg1);
    }
    
    @And("^user select \"([^\"]*)\" in Shipper Workshop dropdown$")
    public void user_select_something_in_shipper_workshop_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectShipperWorkshop(strArg1);
    }

    @And("^user select \"([^\"]*)\" in Recipient Warehouse dropdown$")
    public void user_select_something_in_recipient_warehouse_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectRecipientWarehouse(strArg1);
    }
    
    @And("^user select \"([^\"]*)\" in Recipient Workshop dropdown$")
    public void user_select_something_in_recipient_Workshop_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectRecipientWorkshop(strArg1);
    }
    
    @And("^user select option from Recipient Warehouse dropdown$")
    public void user_select_option_warehouse_dropdown() throws Throwable {
    	new LogisticsModule().selectRecipientWarehouse();
    }
    
    @When("^user select \"([^\"]*)\" in Recipient Site dropdown$")
    public void user_select_something_in_recipient_site_dropdown(String strArg1) throws Throwable {
    	optionValue.add(strArg1);
    	new LogisticsModule().selectRecipientSite(strArg1);
    }
    
    @Then("^verify green confirmation message \"([^\"]*)\" is displayed$")
    public void verify_green_confirmation_message_something_is_displayed(String strArg1) throws Throwable {
        new LogisticsModule().verifyShipmentCreatedConfirmationMsg(strArg1);
    }

    @And("^verify entered details are displayed correctly on \"([^\"]*)\" screen$")
    public void verify_entered_details_are_displayed_correctly_on_something_screen(String strArg1) throws Throwable {
        new LogisticsModule().validateInformationOnShipmentFolder(optionValue);
    }

    @And("^verify an identifier generated by the system$")
    public void verify_an_identifier_generated_by_the_system() throws Throwable {
    	new LogisticsModule().verifyIdentifierIsCreated();
    }
    
    @Then("^verify search results are displayed under \"([^\"]*)\" table$")
    public void verify_search_results_are_displayed_for_something(String strArg1) throws Throwable {
        new LogisticsModule().verifySearchResultsDisplayed(strArg1);
    }
	
	@When("^user open the receipt file$")
    public void user_open_the_receipt_file() throws Throwable {
		new LogisticsModule().openReceiptFromSearchTable(0);
    }
	
	@When("^user open the receipt file at below index$")
    public void user_open_the_receipt_file(DataTable arg) throws Throwable {
		List<String> data= arg.asList();
		new LogisticsModule().openReceiptFromSearchTable(Integer.parseInt(data.get(0))-1);
    }
	
	@And("^click on button \"([^\"]*)\"$")
    public void click_on_button_something(String strArg1) throws Throwable {
        new LogisticsModule().clickOnBtn(strArg1);
    }

    @And("^click on Button \"([^\"]*)\"$")
    public void click_on_Bbutton_something(String strArg1) throws Throwable {
        new LogisticsModule().clickOnStatusBtn(strArg1);
    }
	
	@And("^click on \"([^\"]*)\" button on the \"([^\"]*)\"$")
    public void click_on_button_(String arg1,String arg2) throws Throwable {
        new LogisticsModule().clickOnBtnWithoutCheckingVisibility(arg1);
    }
	
	@And("^click on filter button$")
    public void click_on_button_filter() throws Throwable {
       new LogisticsModule().clickOnBtn("Filter",true,"page");
    }

    @When("click on filter button to filter work management table")
    public void click_on_filter_button_to_filter_work_management_table() {
        new LogisticsModule().filterWMTable("Filter");
    }

    @When("click on filter button to filter maintenance table")
    public void click_on_filter_button_to_filter_maintenance_table() {
        new LogisticsModule().clickOnFilterBtn("Filter");
    }

    @And("^click on filter Button$")
    public void click_on_Button_filter() throws Throwable {
        new LogisticsModule().filterTable("Filter");
    }

    @When("click on filter button to filter work order table")
    public void click_on_filter_button_to_filter_work_order_table() {
        new LogisticsModule().filterWOTable("Filter");
    }
    
    @And("^click on filter button to filter \"([^\"]*)\"$")
    public void click_on_button_filter_WO(String arg) throws Throwable {
        new LogisticsModule().clickOnFilterBtnOnWOFilters("Filter");
    }
	
	@And("^user scroll up and click on button \"([^\"]*)\"$")
    public void click_on_button_something_scroll(String strArg1) throws Throwable {
        new LogisticsModule().scrollAndClickOnBtnUsingJS(strArg1);
    }
	
	@And("^click on refresh button$")
    public void click_on_refresh() throws Throwable {
        new LogisticsModule().clickRefresh();
    }
    
    @And("^click on button \"([^\"]*)\" on picking page$")
    public void click_on_button_traetandship(String strArg1) throws Throwable {
        new LogisticsModule().clickOnTreatAndShip(strArg1);
    }
	
	@When("^click on button \"([^\"]*)\" on \"([^\"]*)\" page$")
    public void click_on_button_something_on_something_page(String strArg1, String strArg2) throws Throwable {
        commonMethods.explicitWaiting(4);
        new LogisticsModule().clickOnBtn(strArg1, true,strArg2);
    }

	@Then("^verify popup \"([^\"]*)\" is displayed$")
    public void verify_popup_something_is_displayed(String strArg1) throws Throwable {
        new LogisticsModule().verifyPopupIsDisplayed(strArg1);
    }

    @And("^verify \"([^\"]*)\" option with \"([^\"]*)\" dropdown are available$")
    public void verify_something_option_with_something_dropdown_are_available(String strArg1, String strArg2) throws Throwable {
        new LogisticsModule().verifyButtonIsVisible(strArg1);
        new LogisticsModule().verifyLabelIsVisisble(strArg2);
    }
	
	@Then("^verify new package number is added to the package section$")
    public void verify_new_package_number_is_added_to_the_package_section() throws Throwable {
        new LogisticsModule().verifyNewReceiptIsCreated("Package", initialCount);
    }
	
	int initialCount;
	@And("^count the number of items available under \"([^\"]*)\" table$")
    public void count_he_number_of_receipts_available_as_of_now(String arg1) throws Throwable {
	  initialCount=new LogisticsModule().captureReceiptCount(arg1);
    }
	
	@When("^user select \"([^\"]*)\" type$")
    public void user_select_something_type(String strArg1) throws Throwable {
		new LogisticsModule().clickMaintenanceType(strArg1);
    }
	
	@And("^verify types of supply request are available$")
    public void verify_types_of_supply_request_are_available(DataTable arg1) throws Throwable {
		List<String> data= arg1.asList();
		new LogisticsModule().validateCreateProcumentTypeOptions(data);
    }
	
	@Then("^verify \"([^\"]*)\" form is displayed$")
    public void verify_something_form_is_displayed(String strArg1) throws Throwable {
		new LogisticsModule().procurementRequestFormDisplayed(strArg1);
    }
	
	@When("^user fill the todays date in \"([^\"]*)\"$")
    public void user_fill_the_todays_date_in_something_in_below_format(String strArg1) throws Throwable {
		new LogisticsModule().enterExpectedDate(strArg1);
    }
	
	@And("^select \"([^\"]*)\" tab from \"([^\"]*)\"$")
    public void select_something_tab_from_something(String strArg1, String strArg2) throws Throwable {
        if(strArg1.trim().contains(strArg2.trim()))
            //strArg1 = getReuiredValue(strArg1);
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().chooseCriticity(strArg1,strArg2);
    }
	
	@Then("^verify \"([^\"]*)\" field consist of below three options and only one can be selected at a time$")
    public void verify_something_field_consist_of_below_three_options_and_only_one_can_be_selected_at_a_time(String strArg1,DataTable arg2) throws Throwable {
		List<String> data= arg2.asList();
		new LogisticsModule().validateCriticityOptions(data);
    }
	
	@Then("^verify \"([^\"]*)\" field consist of below three options on \"([^\"]*)\" popup$")
    public void verify_something_field_consist_of_below_three_options(String strArg1,String arg3,DataTable arg2) throws Throwable {
		List<String> data= arg2.asList();
		new LogisticsModule().validateReceiptFolderCriticityOptions(strArg1,arg3,data);
    }
	
	@And("^click on Search button on popup$")
    public void click_on_search_button_on_popup() throws Throwable {
		new LogisticsModule().clickSearchOnPopup();
    }
	
	@And("^click on Validate button on popup$")
    public void click_on_button_something_on_popup() throws Throwable {
		new LogisticsModule().clickValidateOnPopup();
    }

	
	@When("^enter \"([^\"]*)\" in Criticity Reason field$")
    public void enter_something_in_criticalReason_field(String strArg1) throws Throwable {
        new LogisticsModule().enterDataInCriticityReason(strArg1, "Criticity Reason");
    }
	
	String number;
	@When("^enter \"([^\"]*)\" in \"([^\"]*)\" field$")
    public void enter_something_in_something_field(String strArg1, String strArg2) throws Throwable {
        if(strArg1.trim().contains(strArg2.trim()))
        {
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        }
        new LogisticsModule().enterValueInField(strArg2, strArg1);
        number=strArg1;
    }

    @When("enter {string} in First Name field")
    public void enter_in_First_Name_field(String strArg1) throws IOException {
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().enterValueInField("First Name", strArg1);
    }
	
	@When("^enter \"([^\"]*)\" in \"([^\"]*)\" field on the popup$")
    public void enter_something_in_something_field_popup(String strArg1, String strArg2) throws Throwable {
        if(strArg1.trim().equalsIgnoreCase(strArg2.trim()))
            strArg1=refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().enterValueInFieldOnPopup(strArg2, strArg1);
    }

    @When("enter {string} in Comment field on the popup")
    public void enter_in_Comment_field_on_the_popup(String strArg1) {
        new LogisticsModule().enterValueInFieldonPopup("Comment", strArg1);
    }

	@And("^click on \"([^\"]*)\" field$")
    public void click_on_something_field(String strArg1) throws Throwable {
		new LogisticsModule().clickOnArticleCodeSearchIcon(strArg1);
    }
	
	
	@Then("^verify searched item is displayed in search result$")
    public void verify_item_something_is_displayed_in_search_result() throws Throwable {
		new LogisticsModule().procurementItemDisplayedInSearchResult(number);
    }
	
	@When("^user select the item$")
    public void user_select_the_item() throws Throwable {
		new LogisticsModule().selectProcurementRequestItem(number);
    }
	
	@Then("^verify item code is displayed in \"([^\"]*)\" field$")
    public void verify_item_code_is_displayed_in_something_field(String strArg1) throws Throwable {
		new LogisticsModule().verifyPresenceOfItemInArticleCodeField(number,strArg1);
    }
	
	@When("^enter \"([^\"]*)\" in Quantity field$")
    public void enter_something_in_quantity_field(String strArg1) throws Throwable {
		new LogisticsModule().enterQuantityField(strArg1, "Quantity");
    }

	
	@Then("^verify confirmation message \"([^\"]*)\" is displayed$")
    public void verify_confirmation_message_something_is_displayed(String strArg1) throws Throwable {
		new LogisticsModule().validateConfirmationMsg(strArg1);
    }
	
	@Then("^verify confirmation message \"([^\"]*)\" is displayed at the top$")
    public void verify_confirmation_message_something_is_displayed_top(String strArg1) throws Throwable {
		new LogisticsModule().validateConfirmationMsg(strArg1,"yes");
    }
	
	@Then("^verify error message \"([^\"]*)\" is displayed$")
    public void verify_error_message_something_is_displayed(String strArg1) throws Throwable {
		new LogisticsModule().validateConfirmationMsg(strArg1);
    }
	
	@And("^user view the created Supply Request$")
    public void user_view_the_created_supply_request() throws Throwable {
		new LogisticsModule().procurementRequestCreatedScreen();
    }
	
	@And("^select \"([^\"]*)\" option from Site dropdown$")
    public void select_something_option_from_site_dropdown(String strArg1) throws Throwable {
		new LogisticsModule().selectRecipientSite_PRPage(strArg1);
    }

    @And("^select \"([^\"]*)\" option from Workshop dropdown$")
    public void select_something_option_from_workshop_dropdown(String strArg1) throws Throwable {
    	new LogisticsModule().selectRecipientWorkshop_PRPage(strArg1);
    }

    @When("^user select the procurement request available at below index$")
    public void user_select_the_procurement_request(DataTable arg) throws Throwable {
    	List<String> data= arg.asList();
    	new Dashboard().selectSearchResult(data.get(0),"procurement request");
    }

    @Then("^verify list of supply requests with status \"([^\"]*)\" are displayed$")
    public void verify_list_of_supply_requests_with_status_something_are_displayed(String strArg1) throws Throwable {
        new LogisticsModule().verifyListOfSearchResult(strArg1);
    }

    @And("^click on cancel button$")
    public void click_on_cancel_button() throws Throwable {
    	new LogisticsModule().clickOnBtnCancel("Cancel");
    }

    @When("^user select \"([^\"]*)\" in Receipt Type dropdown$")
    public void user_select_something_in_receipt_type_dropdown(String strArg1) throws Throwable {
    	new LogisticsModule().selectReceiptType(strArg1);
    }
    
    @When("^user select the material$")
    public void user_select_the_material() throws Throwable {
    	new LogisticsModule().selectMaterialFromStockSearchResult("2", "Material from stock list");
    }
    
    @Then("^verify material list is displayed$")
    public void verify_material_list_is_displayed() throws Throwable {
        new LogisticsModule().verifyMaterialListIsDisplayed();
    }
    
    String pckgNo;
    @And("^click on \"([^\"]*)\" button on Select a Location popup$")
    public void click_on_validate_button_on_select_a_location_popup(String arg1) throws Throwable {
    	pckgNo=new LogisticsModule().captureNumOfMaterial();
    	new MaintenancePlanning().clickButtonOnPopUP("Select Location", arg1);
    }
    
    @And("^click on \"([^\"]*)\" button on Location popup$")
    public void click_on_validate_button_location_popup(String arg1) throws Throwable {
    	new MaintenancePlanning().clickButtonOnPopUP("Select Location", arg1);
    }
    
    @When("^user select the movement$")
    public void user_select_the_movement() throws Throwable {
    	new LogisticsModule().selectMaterialFromMovementTable(1);
    }
    
    @When("^user select all the movement$")
    public void user_select_all_the_movement() throws Throwable {
    	new LogisticsModule().selectAllSearchResult("Movement", "Movements");
    }
    
    @Then("^verify the movement status is changed to \"([^\"]*)\"$")
    public void verify_the_movement_status_is_changed_to_something(String strArg1) throws Throwable {
    	new LogisticsModule().verifyStatusUnderMovementTable(strArg1);
    }
    
    @Then("^verify previously selected material is listed in zone \"([^\"]*)\"$")
    public void verify_previously_selected_material_is_listed_in_zone_something(String strArg1) throws Throwable {
        new LogisticsModule().verifypackNoOnStockTable(pckgNo);
    }

    @And("^navigate to tab \"([^\"]*)\"$")
    public void navigateToGivenTab(String strArg1) throws Throwable {
    	new LogisticsModule().navigateToTab(strArg1);
    }
       
    @Then("^verify DE status is changed to \"([^\"]*)\"$")
    public void verify_popup_is_closed_and_dt_status_is_changed_to_something(String strArg1) throws Throwable {
    	new MaintenancePlanning().validate_WPStatus(strArg1); 
    }
      
    @Then("^verify content of the ED cannot be modified$")
    public void verify_content_of_the_ED_cannot_be_modified() throws Throwable {
    	new LogisticsModule().AddPackageBtnVisibility("Add Package", "Not Visible");
    }
    
    @Then("^verify content of the ED can be modified$")
    public void verify_content_of_the_ED_can_be_modified() throws Throwable {
    	new LogisticsModule().AddPackageBtnVisibility("Add Package", "Visible");
    }
    
    @Then("^validate the package if not validated by clicking on \"([^\"]*)\" checkbox$")
    public void validate_the_package_if_not_validated_by_clicking_on_something_checkbox(String strArg1) throws Throwable {
    	new LogisticsModule().clickPackageControlCheckbox();
    }
    
    @Then("^click on control package checkbox$")
    public void clickPC() throws Throwable {
    	new LogisticsModule().clickPackageControlCheckbox();
    }
    
    @Then("^verify procurement status information popup is displayed$")
    public void verify_Procurement_status_information_popup_is_displayed() throws Throwable {
    	new LogisticsModule().verifyProcurementRequestStatusPoupDisplayed();
    }
    
    @When("^user select \"([^\"]*)\" from \"([^\"]*)\" table$")
    public void user_select_something_from_something_table(String strArg1, String strArg2,DataTable arg3) throws Throwable {
    	List<String> data= arg3.asList();
        new LogisticsModule().selectSearchResult(data.get(0), strArg1, strArg2);
    }
    
    @When("^user select all \"([^\"]*)\" from \"([^\"]*)\" table$")
    public void user_select_all_from_something_table(String strArg1, String strArg2) throws Throwable {
        new LogisticsModule().selectAllSearchResult(strArg1, strArg2);
    }
    
    @Then("^verify \"([^\"]*)\" is added under \"([^\"]*)\" table$")
    public void verify_package_is_added_under_something_table(String strArg1,String strArg2) throws Throwable {
        new LogisticsModule().verifyItemAddedToTheTable(strArg2,strArg1);
    }
    
    @When("^user delete all \"([^\"]*)\" from \"([^\"]*)\" table$")
    public void user_delete_all_something_from_something_table(String strArg1, String strArg2) throws Throwable {
        commonMethods.explicitWaiting(4);
    	String flag=new LogisticsModule().selectAllSearchResult(strArg1, strArg2);
    	if(Integer.parseInt(flag)>0) {
    		new LogisticsModule().clickOnBtn("delete");
    		if(strArg1.equalsIgnoreCase("visit")){
                new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
            }
    	}
    }

    @When("^user remove all \"([^\"]*)\" from \"([^\"]*)\" table$")
    public void user_delete__something_from_something_table(String strArg1, String strArg2) throws Throwable {
        commonMethods.explicitWaiting(2);
        if(new LogisticsModule().getCountFromTableByRow(strArg2)>0) {
            new LogisticsModule().selectAllItemUnderTableByRow(strArg2);
            new LogisticsModule().clickOnBtn("delete");
            if (strArg1.equalsIgnoreCase("visit")) {
                new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
            }
        }
    }
    
    @Then("^add the package with below \"([^\"]*)\" if no package exist in \"([^\"]*)\" table$")
    public void add_the_package_with_below_something_if_no_package_exist_in_something_table(String strArg1, String strArg2,DataTable arg3) throws Throwable {
    	List<String> data= arg3.asList();
    	new LogisticsModule().addPackageIfNotExist(strArg2, data.get(0), strArg1);
    }
    
    @When("^select the packages$")
    public void select_the_packages() throws Throwable {
    	new LogisticsModule().selectAllSearchResult("packages", "Package");
    }
    
    @Then("^verify status of the package is changed to \"([^\"]*)\"$")
    public void verify_status_of_the_package_is_changed_to_something(String strArg1) throws Throwable {
    	new LogisticsModule().verifyPackageStatus(strArg1);
    }

    @Then("^verify selected material cannot be removed$")
    public void verify_selected_material_cannot_be_removed() throws Throwable {
    	new LogisticsModule().verifyDeleteButtonIsNotVisble("delete");
    }
    
    @And("^cancel the validation operation$")
    public void cancel_the_validation_operation() throws Throwable {
    	new LogisticsModule().uncheckPackageControlBox();
    }
    
    @Then("^verify selected material can be removed$")
    public void verify_selected_material_can_be_removed() throws Throwable {
    	new LogisticsModule().verifyDeleteButtonIsVisble("delete");
    }
    
    @And("^select one material linked to the package$")
    public void select_one_material_linked_to_the_package() throws Throwable {
    	new LogisticsModule().selectAllSearchResult("Material", "Materials");
    }

    @And("^select all material linked to the package$")
    public void select_all_material_linked_to_the_package() throws Throwable {
        new LogisticsModule().selectAllSearchResult("Material", "Materials");
    }
    
    @When("^user unselect the material$")
    public void user_unselect_the_material() throws Throwable {
    	new LogisticsModule().selectAllSearchResult("Material", "Materials");
    }

    
    @And("^click on \"([^\"]*)\" button on \"([^\"]*)\" popup$")
    public void click_on_something_button_on_something_popup(String strArg1, String strArg2) throws Throwable {
    	new MaintenancePlanning().clickButtonOnPopUP(strArg2, strArg1);
    }

    @And("^click on \"([^\"]*)\" button on variant popup$")
    public void click_on_something_button_on_something_popup_AM(String strArg1) throws Throwable {
        new MaintenancePlanning().clickButtonOnPopUP( "Table",strArg1);
    }
    
    @And("^click on search icon of \"([^\"]*)\" field$")
    public void click_on_search_icon_of_something_field(String strArg1) throws Throwable {
    	new MaintenancePlanning().clickOnSearchIconOfField(strArg1);
    }

    @And("^click on Search icon of \"([^\"]*)\" field$")
    public void click_on_Search_icon_of_something_field(String strArg1) throws Throwable {
        new MaintenancePlanning().clickOnSearchIconOfFieldBesidePlus(strArg1);
    }

    @When("click on Plus icon of {string} field")
    public void click_on_Plus_icon_of_field(String strArg1) {
        new MaintenancePlanning().clickOnPlusIcon(strArg1);
    }
    
    @When("^enter \"([^\"]*)\" in Manufacturing Batch field$")
    public void enter_something_in_manufacturing_batch_field(String strArg1) throws Throwable {
    	new LogisticsModule().enterValueInManufacturingBatch(strArg1);
    }
    
    @When("^enter \"([^\"]*)\" in SN field$")
    public void enter_something_in_sn_field(String strArg1) throws Throwable {
    	new LogisticsModule().enterValueInManufacturingBatch(strArg1);
    }
    
    String randomSN;
    @When("^enter random value in SN field$")
    public void enter_value_in_sn_field() throws Throwable {
    	randomSN=new LogisticsModule().enterValueInManufacturingBatch("","Random");
    }
    
    
    @And("^click on search icon of Manufacturing Batch field$")
    public void click_on_search_icon_of_manufacturing_batch_field() throws Throwable {
    	new LogisticsModule().clickmanufacturingBatchSearchIcon();
    }
    
    @And("^click on search icon of SN field$")
    public void click_on_search_icon_of_SN_field() throws Throwable {
    	new LogisticsModule().clickmanufacturingBatchSearchIcon();
    }
    
    @And("^click on search icon of SN field to get the message$")
    public void click_on_search_icon_of_SN_field_message() throws Throwable {
    	new LogisticsModule().clickmanufacturingBatchSearchIcon_noDelay();
    }
    
    @And("^enter \"([^\"]*)\" in Quantity Kg field$")
    public void enter_something_in_quantity_kg_field(String strArg1) throws Throwable {
    	new LogisticsModule().enterValueInQuanityKg(strArg1);
    }
    
    @And("^click on tick icon$")
    public void click_on_tick_icon() throws Throwable {
    	new LogisticsModule().clickTickIcon();
    }
    
    @And("^select \"([^\"]*)\" option from Asset$")
    public void select_generate_packaging_number_option_from_something(String strArg1) throws Throwable {
    	new LogisticsModule().selectGeneratePackingNumber(strArg1);
    }
    
    @And("^value in \"([^\"]*)\" field reset to \"([^\"]*)\"$")
    public void value_in_something_field_reset_to_something(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().verifyQuantityToReceiptReset(strArg1, strArg2);
    }
    
    @Then("^verify \"([^\"]*)\" is not equal to \"([^\"]*)\"$")
    public void verify_something_is_not_equal_to_something(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().verifyMissingQuanity("Not", strArg1, strArg2);
    }
    
    @Then("^verify \"([^\"]*)\" is set to \"([^\"]*)\"$")
    public void verify_something_is_set_to_something(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().verifyMissingQuanity("zero", strArg1, strArg2);
    }
    
    int itemCount;
    @And("^count number of material displayed in \"([^\"]*)\" table$")
    public void count_number_of_material_displayed_in_something_table(String tableHeader) throws Throwable {
    	itemCount=new LogisticsModule().getCountFromTable(tableHeader);
    }
    
    @And("^verify item is get added to the \"([^\"]*)\" list$")
    public void verify_item_is_get_added_to_the_something_list(String strArg1) throws Throwable {
    	new LogisticsModule().verifyNewReceiptIsCreated(strArg1, itemCount);
    }
    
    @And("^click on \"([^\"]*)\" button to save the filled information$")
    public void click_on_save_button_to_save_the_filled_information(String strArg1) throws Throwable {
    	new LogisticsModule().clickOnBtnUsingJS(strArg1);
    }
    
    @When("^user open the receipt file from \"([^\"]*)\" table having the \"([^\"]*)\" in it$")
    public void user_open_the_receipt_file_from_something_table_having_the_something_in_it(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().selectShipmentHavingMaterialFromSearchResult(strArg1, strArg2);
    }
    
    @Then("^validate the package if not validated$")
    public void validate_the_package_if_not_validated() throws Throwable {
        commonMethods.explicitWaiting(4);
    	new LogisticsModule().validatePackageIfNotValidated();
    }
    
    @Then("^verify progress of the \"([^\"]*)\" is changed to \"([^\"]*)\"$")
    public void verify_progress_of_the_mat_is_changed_to_something(String strArg1,String strArg2) throws Throwable {
    	new LogisticsModule().validateMaterialProgressIsSuccesfull(strArg1,strArg2);
    }

    @Then("^verify progress of the material is changed to \"([^\"]*)\"$")
    public void verify_progress_of_the_material_is_changed_to_something(String strArg1) throws Throwable {
        new LogisticsModule().validateMaterialProgress("Materials",strArg1);
    }
    
    @And("^verify button \"([^\"]*)\" is visible$")
    public void verify_button_something_is_visible(String strArg1) throws Throwable {
    	new LogisticsModule().verifyButtonIsVisible(strArg1);
    }
    
    @And("^user select \"([^\"]*)\" having package with \"([^\"]*)\" status$")
    public void user_select_something_having_package_with_something_status(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().selectMaterial(strArg1, strArg2);
    }

    @And("^verify execution completed successfully$")
    public void verify_execution_completed_successfully() throws Throwable {
    	new LogisticsModule().verifyTestCaseExecutedCompletely();
    }
    
    @When("^user open the work order in \"([^\"]*)\" page$")
    public void user_open_the_work_order_in_something_page(String strArg1) throws Throwable {
    	new LogisticsModule().openWOInPRPage(strArg1);
    }
    
    @And("^modify the \"([^\"]*)\" work order in \"([^\"]*)\" field$")
    public void modify_the_work_order_in_something_field(String arg,String strArg1) throws Throwable {
    	new MaintenancePlanning().clickOnSearchIconOfField(strArg1);
    	new Dashboard().selectOptionFromCriteriaDropdown(arg, "Order Status");
    	new MaintenancePlanning().searchAndAddItemToTheList("search work order", "Search",1);
    	new MaintenancePlanning().clickButtonOnPopUP("search work order", "Validate");
    }
    
    @And("^looks for another \"([^\"]*)\" work order in \"([^\"]*)\" field$")
    public void looking_the_work_order_in_something_field(String arg,String strArg1) throws Throwable {
    	new MaintenancePlanning().clickOnSearchIconOfField(strArg1);
    	new Dashboard().selectOptionFromCriteriaDropdown(arg, "Order Status");
    	new MaintenancePlanning().searchAndAddItemToTheList("search work order", "Search",1);
    	new MaintenancePlanning().clickButtonOnPopUP("search work order", "Validate");
    }
    
    @When("^user navigates back to \"([^\"]*)\"$")
    public void user_navigates_back_to_something(String strArg1) throws Throwable {
    	new LogisticsModule().navigateBackToPage(strArg1);
    }
    
    @When("^user navigates back to \"([^\"]*)\" through breadcrumbs$")
    public void user_navigates_back__breadCrumb(String strArg1) throws Throwable {
    	new LogisticsModule().ClickBreadcrumbs(strArg1);
    }
    
    @And("^click on yes button on confirmation popup$")
    public void click_yes_Btn_popup() throws Throwable {
    	new MaintenancePlanning().clickOnPopupBtn("yes");
    }
    
    @Then("^verify procurement request is added to the work order$")
    public void verify_procurement_request_is_added_to_the_work_order() throws Throwable {
        new LogisticsModule().PRCreatedUnderWO("Request Management ", "Created On");
    }
    
    @And("^click on show all action icon under Material section$")
    public void click_on_show_all_action_icon_under_material_section() throws Throwable {
        new LogisticsModule().clickOnShowAllActionBtnUnderMaterial("Show All Action");
    }
    
    @And("^click on filter icon$")
    public void click_on_filter_icon() throws Throwable {
        new LogisticsModule().clickOnFilterIcon();
    }

    @And("^click on filter icon on my work order page$")
    public void click_on_filter_icon_MWO() throws Throwable {
        new LogisticsModule().clickOnFilterIconMWO();
    }

    @And("^click on filter icon on work management page$")
    public void click_on_filter_icon_MWO_WM() throws Throwable {
        new LogisticsModule().clickOnFilterIconMWO();
    }

    @And("^click on filter icon to filter results$")
    public void click_on_filter_icon_filter() throws Throwable {
        new LogisticsModule().clickOnFilterIconMWO();
    }

    @When("click on filter icon to filter result")
    public void click_on_filter_icon_to_filter_result() {
        new LogisticsModule().clickOnFilterIcon(true);
    }
    
    @Then("^verify filters defined in the CD are accessible$")
    public void verify_filters_defined_in_the_cd_are_accessible() throws Throwable {
    	new LogisticsModule().verifyFilteredSN_Displayed(3, snNumber);

    }
    
    @And("^click on \"([^\"]*)\" menu item$")
    public void click_on_something_menu_item(String strArg1) throws Throwable {
    	new LogisticsModule().clickManageBtn(strArg1);
    }
    
    String snNumber;
    @And("^capture the \"([^\"]*)\" from first row$")
    public void capture_the_something_from_first_row(String strArg1) throws Throwable {
    	snNumber=new LogisticsModule().captureColumnValueOfTypeLink(strArg1,1);
    }
    
    @And("^capture the \"([^\"]*)\" from first row if available$")
    public void capture_the_something_from_first_row_ifexist(String strArg1) throws Throwable {
    	snNumber=new LogisticsModule().captureSNColumnValueIfExist(strArg1,1);
    }
    
    @And("^enter captured SN value in \"([^\"]*)\" field$")
    public void enter_captured_sn_value_in_something_field(String strArg1) throws Throwable {
        new LogisticsModule().enterValueInSNField("sn", snNumber);
    }
    
    @And("^enter captured SN value in \"([^\"]*)\" field if available$")
    public void enter_captured_sn_value_in_something_fieldExist(String strArg1) throws Throwable {
//        commonMethods.explicitWaiting(60);
        new LogisticsModule().enterValueInSNFieldIfExist("sn", snNumber);
    }
  
    @And("^click on \"([^\"]*)\" button on page \"([^\"]*)\"$")
    public void click_on_button_UsingJS(String strArg1,String strArg2) throws Throwable {
    	new LogisticsModule().clickOnBtnUsingJS(strArg1);
    }

    @And("^click on \"([^\"]*)\" button on popup maintenance program$")
    public void click_on_button_MP(String strArg1) throws Throwable {
        new LogisticsModule().clickOnBtnUsingJS(strArg1,true);
    }
    
    @And("^click on \"([^\"]*)\" button$")
    public void click_on_something_button(String strArg1) throws Throwable {
        commonMethods.explicitWaiting(10);
        new LogisticsModule().clickOnElement(strArg1);
    }
    
    @Then("^verify an event with \"([^\"]*)\" number is created$")
    public void verify_an_event_with_something_number_is_created(String strArg1) throws Throwable {
    	number=new MaintenancePlanning().validateEventAndTransferOrder();
    }
    
    @When("^user enter the transfer order on \"([^\"]*)\" field$")
    public void user_enter_the_transfer_order_on_something_field(String strArg1) throws Throwable {
    	String temp=CompactUtil.extractNumber(number);
    	if(temp.trim().contains(" ")) {
    		String[] data=temp.split(" ");
    		temp=data[data.length-1];
    	}
    	new MaintenancePlanning().enterCriteria(strArg1, CompactUtil.extractNumber(temp));
    }
    
    @When("^user enter the transfer order on \"([^\"]*)\" field on reciept page$")
    public void user_enter_the_transfer_order_on_something_field_reciept(String strArg1) throws Throwable {
    	new MaintenancePlanning().enterCriteria(strArg1, number);
    }

    @Then("^verify transfer order is visible under search results$")
    public void verify_transfer_order_is_visible_under_search_results() throws Throwable {
    	new MaintenancePlanning().verifyPackageNumberOnSearchResult(number);
    }
     
    @Then("^clicked on Reference article$")
    public void click_refer_article() throws Throwable {
    	new LogisticsModule().clickReferenceArticle();
    }
    
    @And("^select the row having entered serial number$")
    public void select_the_row() throws Throwable {
    	new LogisticsModule().selectItemUnderSite(randomSN,"Select");
    }

    @And("^select the row having entered Serial number$")
    public void Select_the_row() throws Throwable {
        new LogisticsModule().selectItemInSite(toNumber,"Select");
    }


    @And("^select the row having entered packaging number$")
    public void Select_the_Row() throws Throwable {
        new LogisticsModule().selectItemInWarehouse(toNumber,"Select");
    }
     
    @Then("^verify \"([^\"]*)\" button is not displayed$")
    public void verifyButton_NotDisplayed(String strArg1) throws Throwable {
    	new LogisticsModule().buttonNotDisplayed(strArg1);
    }

    @Then("^verify \"([^\"]*)\" button is not visible")
    public void verifyButton_NotVisible(String strArg1) throws Throwable {
        new LogisticsModule().buttonNotDisplayed(strArg1,true);
    }
     
    @When("^user selects two movements that do not have the same destination$")
    public void select_row_not_same_recipient() throws Throwable {
    	new LogisticsModule().selectRowWithDifferentFinalRec("Movements", "Final Recipient");
    }
    
    @When("^user unselects two movements that do not have the same destination$")
    public void unselect_row_not_same_recipient() throws Throwable {
    	new LogisticsModule().selectRowWithDifferentFinalRec("Movements", "Final Recipient");
    }
    
    @When("^user selects two movements with the same destination but different criticalities$")
    public void select_row_same_recipient_different_criticalities() throws Throwable {
    	new LogisticsModule().selectRowWithSameFinalRecWithDiffCritical("Movements", "Final Recipient","Document of Origin");
    }
    
    @When("^user unselects two movements with the same destination but different criticalities$")
    public void unselect_row_same_recipient_different_criticalities() throws Throwable {
    	new LogisticsModule().selectRowWithSameFinalRecWithDiffCritical("Movements", "Final Recipient","Document of Origin");
    }
    
    @When("^user selects two movements with the same destination and same criticalities$")
    public void select_row_same_recipient_same_criticalities() throws Throwable {
    	new LogisticsModule().selectRowWithSameFinalRecWithSameCritical("Movements", "Final Recipient","Document of Origin");
    }
     
    @When("^user single click on \"([^\"]*)\" column$")
    public void single_click_Code_column(String name) throws Throwable {
    	new LogisticsModule().ClickOnColoumnHeader(name);
    }
    
    @And("^select todays date in Manufacturing date field$")
    public void select_todays_date_in_manufacturing_date_field() throws Throwable {
    	new LogisticsModule().enterValueInManufacturingDateInputbox();
    }
    
    @And("^select todays date in expiration date field$")
    public void select_todays_date_in_expiration_date_field() throws Throwable {
    	new LogisticsModule().enterValueInExpirationDateInputbox();
    }
        
    @And("^click on save button at top right corner$")
    public void click_save_btn_ID() throws Throwable {
    	new LogisticsModule().clickSaveBtnById();
    }
    
    Map<String,String> equipment= new HashMap<String,String>();
    @And("^user capture the \"([^\"]*)\" and \"([^\"]*)\" details of an equipment available at below index$")
    public void user_capture_the_something_and_something_details_of_an_equipment_available_at_below_index(String strArg1, String strArg2, DataTable arg) throws Throwable {
    	List<String> data= arg.asList();
    	equipment.put(strArg1, new LogisticsModule().captureColumnValueOfTypeLink(strArg1, Integer.parseInt(data.get(0))));
    	equipment.put(strArg2, new LogisticsModule().captureColumnValueOfTypeLink(strArg2, Integer.parseInt(data.get(0))));
    }

    Map<String,String> manufacturingBatch= new HashMap<String,String>();
    @And("^user capture the \"([^\"]*)\" and \"([^\"]*)\" details of an manufacturing batch available at below index$")
    public void user_capture_the_something_and_something_details_of_an_mb_available_at_below_index(String strArg1, String strArg2, DataTable arg) throws Throwable {
        List<String> data= arg.asList();
        manufacturingBatch.put(strArg1, new LogisticsModule().captureColumnValueOfTypeLinkByRow(strArg1, Integer.parseInt(data.get(0)),"Results"));
        System.out.println("batch number "+manufacturingBatch.get(strArg1));
        manufacturingBatch.put(strArg2, new LogisticsModule().captureColumnValueOfTypeLinkByRow(strArg2, Integer.parseInt(data.get(0)),"Results"));
        System.out.println("batch material "+manufacturingBatch.get(strArg2));
    }
    
    @When("^enter the captured pn number in \"([^\"]*)\" field$")
    public void enter_the_captured_pn_number_in_something_field(String strArg1) throws Throwable {
    	new LogisticsModule().enterValueInField(strArg1, equipment.get(strArg1));
    
    }
    
    @When("^enter the captured sn value in \"([^\"]*)\" field$")
    public void enter_the_captured_sn_value_in_something_field(String strArg1) throws Throwable {
    	new LogisticsModule().enterValueInManufacturingBatch(equipment.get(strArg1));
    }
    
    @When("^user clicks on folder icon in \"([^\"]*)\" column$")
    public void user_clicks_on_folder_icon_in_something_column(String strArg1) throws Throwable {
        new LogisticsModule().clickonFolderIcon();
    }
    
    String toNumber,transferOrdr;
    @And("^user capture the transfer order number$")
    public void user_capture_the_transfer_order_number() throws Throwable {
    	transferOrdr= new LogisticsModule().captureTransferOrderNumber();
    	System.out.println("capture transfer order "+transferOrdr);
    }
    
    @Then("^verify created transfer request is available in the search result$")
    public void verify_created_transfer_request_is_available_in_the_search_result() throws Throwable {
    	new LogisticsModule().verifySearchResultsDisplayed("Results");
    }
    
    @When("^entered the captured transfer order number in \"([^\"]*)\" field$")
    public void entered_the_captured_transfer_order_number_in_something_field(String strArg1) throws Throwable {
    	String temp=CompactUtil.extractNumber(transferOrdr);
    	if(temp.trim().contains(" ")) {
    		String[] data=temp.split(" ");
    		temp=data[data.length-1];
    	}
        new LogisticsModule().enterValueInField(strArg1,CompactUtil.extractNumber(temp));
    }
      
    @When("^capture the \"([^\"]*)\" detail$")
    public void captureDetails(String strArg1) throws Throwable {
    	toNumber= new LogisticsModule().captureColumnValueOfTypeLink(strArg1, 1);
    }
    
    @And("^select the row having captured packaging number$")
    public void select_the_packaging() throws Throwable {
    	//DriverUtil.explicitWaiting(10);
    	new LogisticsModule().selectItemUnderWarehouse(toNumber,"Select");
    }

    @And("^select the row using button \"([^\"]*)\"$")
    public void select_the_row(String strArg1) throws Throwable {
        new LogisticsModule().selectRowUsingBtn(strArg1);
    }

    @And("^unselect the row using button \"([^\"]*)\"$")
    public void unselect_the_row(String strArg1) throws Throwable {
        new LogisticsModule().selectRowUsingBtn(strArg1);
    }
     
    @Then("^verify \"([^\"]*)\" section is displaying the packaging number$")
    public void verify_pckaging_number_displayed(String strArg1) throws Throwable {
    	new LogisticsModule().pnInSelectedAsset(strArg1,toNumber);
    }
      
    @And("^searched for captured packaging number$")
    public void search_movement() throws Throwable {
    	new LogisticsModule().searchMovement(toNumber);
    }
    
    @And("^search for item \"([^\"]*)\"$")
    public void search_item_from_list(String arg) throws Throwable {
    	new LogisticsModule().searchEquipmentOnPopup(arg);
    }

    @When("search for item {string} from result")
    public void search_for_item_from_result(String arg1) throws IOException {
        arg1 = refObj.getCellData(testDataRow, arg1);
        new LogisticsModule().searchEquipmentOnPopup(arg1);
    }
        
    @Then("^verify item is treated successfully$")
    public void item_treated() throws Throwable {
    	new LogisticsModule().verifyButtonIsVisible("Checked");
    }
     
    @When("^user click on cubes icon under quantity$")
    public void click_cubes_icon() throws Throwable {
    	new LogisticsModule().clickCubesIcon();
    }
    
    @When("^user navigates back to \"([^\"]*)\" page from PR management page$")
    public void naviagte_PR_PRM(String page) throws Throwable {
    	new LogisticsModule().navigateToPRPageFromPRM(page);
    }
    
    @And("^select \"([^\"]*)\" option from \"([^\"]*)\" dropdown on transfer order popup$")
    public void select_something_option_from_something_dropdown_on_transfer_order_popup(String arg1, String arg2) throws Throwable {
        if(arg1.trim().contains(arg2.trim()))
            arg1 = refObj.getCellData(testDataRow, arg1);
		new Dashboard().selectOptionFromCriteriaDropdown(arg2, arg1,"transfer order popup");
    } 
    
    @And("^verify \"([^\"]*)\" is displayed for \"([^\"]*)\" field$")
    public void verify_something_is_displayed_for_something_field(String strArg1, String strArg2) throws Throwable {
    	new LogisticsModule().validateValueInField_Type(strArg2, strArg1);
    }
    
    @And("^verify \"([^\"]*)\" is displayed for \"([^\"]*)\" field on reciept page$")
    public void verify_something_is_displayed_for_something_field_reciept_page(String strArg1, String strArg2) throws Throwable {
        commonMethods.explicitWaiting(4);
        new LogisticsModule().validateValueInTypeField(strArg2, strArg1);
    }
    
    @And("^verify for fields Supplier Site, Supplier Warehouse, Recipient Site information must correspond to the values we set in the Install/Remove screens$")
    public void verify_for_below_fields_information_must_correspond_to_the_values_we_set_in_the_installremove_screens(DataTable arg) throws Throwable {
    	List<String> data= arg.asList();
    	new LogisticsModule().validateTOPageInfoFromInstallScreen(data.get(1));
    }
    
    @And("^verify for fields Shipper Workshop, Shipper Warehouse, Recipient Workshop information on reciept page must correspond to the values we set in the Install/Remove screens$")
    public void verify_for_below_fields_information_on_reciept_page_to_the_values_we_set_in_the_installremove_screens(DataTable arg) throws Throwable {
    	List<String> data= arg.asList();
    	new LogisticsModule().validateReceiptFolderPageInfoFromInstallScreen(data.get(1),data.get(2));
    }
    
    @And("^click on \"([^\"]*)\" button on install remove screen$")
    public void click_on_something_button_on_install_remove_screen(String strArg1) throws Throwable {
    	new LogisticsModule().clickSave_InstallRemoveScreen(strArg1);
    }
    
    String stockNumber;
	@When("^enter random stock value in \"([^\"]*)\" field$")
    public void enter_random_in_something_field(String strArg1) throws Throwable {
		stockNumber= new LogisticsModule().enterNumber_Stock(strArg1);
        
    }
	
	@And("^select newly created area from \"([^\"]*)\" dropdown$")
    public void select_newly_created_option_from_something_dropdown_in_something_popup(String strArg1) throws Throwable {
       new Dashboard().selectOptionFromCriteriaDropdown_AreaData(stockNumber, strArg1);
    }
	
	String storageBin;
	@When("^enter stock value in \"([^\"]*)\" field$")
    public void enter_val_in_something_field(String strArg1) throws Throwable {
		storageBin=new LogisticsModule().enterRandomNumber_Stock(strArg1);
        
    }
	
	@Then("^verify zone is added to the list$")
    public void verify_zone_is_added_to_the_list() throws Throwable {
        new LogisticsModule().verifyZoneIsAddedToList(stockNumber);      
    }
	
	@And("^select option from \"([^\"]*)\" dropdown in \"([^\"]*)\" popup$")
    public void select_option_from_something_dropdown_in_something_popup(String strArg1, String strArg2) throws Throwable {
       new Dashboard().selectOptionFromCriteriaDropdown_Index(strArg1, 0, strArg2);
    }
	
	@And("^select option from type dropdown in \"([^\"]*)\" popup$")
    public void select_option_from_type_dropdown_in_something_popup(String strArg2) throws Throwable {
       new Dashboard().selectOptionFromCriteriaDropdown_Index("Type", 0, strArg2);
    }
	
	@When("^select the newly created area$")
    public void select_the_newly_created_zone() throws Throwable {
        new LogisticsModule().selectZone(stockNumber);
    }
	
	@Then("^verify new entry is added in \"([^\"]*)\" section$")
    public void verify_new_entry_is_added_in_something_section(String strArg1) throws Throwable {
        new LogisticsModule().newEntryAddedToStorageBinSection(strArg1,stockNumber);
        commonMethods.explicitWaiting(2);
    }
	
	@And("^select the newly added Areas$")
    public void select_the_newly_added_something() throws Throwable {
        new LogisticsModule().selectNewlyAddedArea(stockNumber,"Areas");
    }
	
	@And("^select the newly added storage bins$")
    public void select_the_newly_added_sb() throws Throwable {
        new LogisticsModule().selectNewlyAddedSB("Storage Bins");
    }
	
	@When("^enter random order stock value in \"([^\"]*)\" field$")
    public void enter_random_order_stock_value_in_something_field(String strArg1) throws Throwable {
        new LogisticsModule().enterNumber_Stock(strArg1, stockNumber);
    }
	
	@Then("^verify the \"([^\"]*)\" of an item is changed to \"([^\"]*)\" in \"([^\"]*)\"$")
    public void verify_the_something_of_an_item_in_area_is_changed_to_something(String strArg1, String strArg2,String arg) throws Throwable {
       new LogisticsModule().validateItemStatusInArea(strArg2, arg, stockNumber);
    }
	
	@Then("^verify the \"([^\"]*)\" of an \"([^\"]*)\" is changed to \"([^\"]*)\"$")
    public void verify_the_something_of_an_item_in_area_is_changed_to_deleted(String strArg1, String strArg2,String strArg3) throws Throwable {
       new LogisticsModule().validateItemStatusInStorageBin(strArg3, strArg2,storageBin);
    }
	
	@When("^click on button \"([^\"]*)\" under section \"([^\"]*)\"$")
    public void click_button_under_section(String strArg1,String arg) throws Throwable {
        new LogisticsModule().clickBtnUnderSection(arg,strArg1);
    }

    @When("^click on element \"([^\"]*)\" under section \"([^\"]*)\"$")
    public void click_element_under_section(String strArg1,String arg) throws Throwable {
        new LogisticsModule().clickElementUnderSection(arg,strArg1);
    }
	
	@Then("^click on checkbox \"([^\"]*)\"$")
    public void click_checkbox(String strArg1) throws Throwable {
       new LogisticsModule().clickCheckbox(strArg1);
    }
	  
	@And("^verify the item is appeared on deleted list$")
    public void verify_item_deleted() throws Throwable {
       new LogisticsModule().verifyZoneIsAddedToList(storageBin);
    }
	   
	@And("^select area from the list$")
	public void select_area_list() throws Throwable {
		new LogisticsModule().selectAreaFromLocationPopup();
	}
	
	@When("^user enter the shipment folder number on \"([^\"]*)\" field on reciept page$")
    public void user_enter_the_SF_number_on_something_field_reciept(String strArg1) throws Throwable {
    	new MaintenancePlanning().enterCriteria(strArg1, sf.get("Local Shipment"));
    }
	
	Map<String,String> sf= new HashMap<String,String>();
	@And("^capture the \"([^\"]*)\" and \"([^\"]*)\"$")
    public void capture_the_something_and_something(String strArg1, String strArg2) throws Throwable {
        sf=new LogisticsModule().captureSFAndPckgNumber(strArg1, strArg2, "Package");
    }
	

	String materialNo;
	@And("^capture the \"([^\"]*)\" from table \"([^\"]*)\"$")
    public void capture_the_something_from_the_page(String strArg1,String arg) throws Throwable {
		materialNo=new LogisticsModule().getMaterialNumber(arg, strArg1);
    }
	
	@And("^verify captured \"([^\"]*)\" is available on the page$")
    public void verify_captured_somethingis_available_on_the_page(String strArg1) throws Throwable {
        new LogisticsModule().validateItemInPage(sf.get(strArg1), strArg1);
    }
	
	 @Then("^verify captured article reference is available on the page$")
	 public void verify_captured_article_reference_is_available_on_the_page() throws Throwable {
		 new LogisticsModule().validateItemInPage(materialNo, "Article number");
	 }
	  
	 @Then("^verify unit value automatically displayed for quantity$")
	 public void verify_unit_value_automatically_displayed_for_quantity() throws Throwable {
		 new LogisticsModule().verifyUnitExist();
	 }
	   
	 @Then("^wait for the page to load$")
	 public void waitforpageToload() throws Throwable {
		 new LogisticsModule().waitPageLoad();
	 }

    @Then("^wait for some second to finish the switching process$")
    public void waitforpageToload_wait() throws Throwable {
        commonMethods.explicitWaiting(10);
    }
	 
	 @And("^verify \"([^\"]*)\" is mentioned in \"([^\"]*)\" field$")
	 public void verify_something_is_mentioned_in_something_field(String strArg1, String strArg2) throws Throwable {
         if(strArg1.trim().contains(strArg2.trim()))
             strArg1=refObj.getCellData(testDataRow, strArg1);
	     new LogisticsModule().textPresentInField(strArg1,strArg2);
	 }

	 
	 @When("^click on Folder Number$")
	 public void click_on_folder_number() throws Throwable {
	      new LogisticsModule().clickOnFolderNumber("Folder");
	 }
	 
	@And("^click on Validate button on litigation popup$")
	public void click_on_validate_button_on_the_popup() throws Throwable {
		new LogisticsModule().validateLitigation();
	}
    
	String sn;
	@And("^user captured the selected \"([^\"]*)\" number$")
    public void user_captured_the_selected_something_number(String strArg1) throws Throwable {
		sn=new LogisticsModule().captureSN_CreateRepairPopup();
    }
	
	@And("^verify captured \"([^\"]*)\" number is visible on the page$")
    public void verify_captured_something_number_is_visible_on_the_page(String strArg1) throws Throwable {
        new LogisticsModule().verifySNOnRepairRequestPage(sn);
    }	
	
	@And("^click on refuse button$")
    public void click_on_refuse_button() throws Throwable {
        new LogisticsModule().clickRefuse();
    }

    @And("^click on accept button$")
    public void click_on_accept_button() throws Throwable {
        new LogisticsModule().clickAccept();
    }

    @And("^click on \"([^\"]*)\" button on popup \"([^\"]*)\"$")
    public void click_on_something_button_on_popup_something(String strArg1, String strArg2) throws Throwable {
        new LogisticsModule().clickOnBtnOnPopupUSingVisibleText(strArg2,strArg1);
    }

    String pageSubtitleValue;
    @And("capture the deployement batch real number")
    public void captureTheDeployementBatchRealNumber() {
        pageSubtitleValue= new LogisticsModule().captureSubtitleValue();
    }


    @And("user enter the deployment batch number on {string} field on procurement request page")
    public void userEnterTheDeploymentBatchNumberOnFieldOnRecieptPage(String arg0) {
        new MaintenancePlanning().enterCriteria(arg0, pageSubtitleValue);
    }

    @When("click on the deployement number link")
    public void clickOnTheDeployementNumberLink() {
        new LogisticsModule().clickOnDeployementNum(pageSubtitleValue);
    }

    @And("verify below tabs are available")
    public void verifyBelowTabsAreAvailable(DataTable data) {
        List<String> arg=data.asList();
        for(int i=0;i<arg.size();i++) {
            new LogisticsModule().verifyTabsAreAvailable(arg.get(i));
        }

    }

    @And("select {string} option under section Batch owner from {string} dropdown")
    public void selectOptionUnderSectionFromDropdown_batch(String arg0, String arg2) throws Throwable {
        if(arg0.trim().contains(arg2.trim()))
            arg0=refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().selectOptionFromDDUnderSection("Batch owner",arg0,arg2);
    }

    @And("select {string} option under section Receiver from {string} dropdown")
    public void selectOptionUnderSectionFromDropdown_rec(String arg0, String arg2) throws Throwable {
        if(arg0.trim().contains(arg2.trim()))
            arg0=refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().selectOptionFromDDUnderSection("Receiver",arg0,arg2);
    }

    @And("select {string} option under section Recipient from {string} dropdown")
    public void selectOptionUnderSectionFromDropdown_Recipient(String arg0, String arg2) throws Throwable {
        if(arg0.trim().equalsIgnoreCase(arg2.trim()))
            arg0 = getReuiredValue(arg0);
        new LogisticsModule().selectOptionFromDDUnderSection("Recipient",arg0,arg2);
    }

    @And("select {string} option under section Supplier from {string} dropdown")
    public void selectOptionUnderSectionFromDropdown_Supplier(String arg1, String arg2) throws Throwable {
        if(arg1.trim().contains(arg2.trim()))
            arg1 = refObj.getCellData(testDataRow, arg1);
        new LogisticsModule().selectOptionFromDDUnderSection("Supplier",arg1,arg2);
    }

    @And("select {string} option under section Supplier from warehouse dropdown")
    public void selectOptionUnderSectionFromwarehouseDropdown_Supplier(String arg0) throws Throwable {
        new LogisticsModule().selectOptionFromDDUnderSection("Supplier",arg0,"Warehouse",true);
    }

    @When("select {string} option under section Supplier from dropdown {string}")
    public void select_option_under_section_Supplier_from_dropdown(String arg0, String arg2) throws Exception {
        if(arg0.trim().contains(arg2.trim()))
            arg0 = refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().selectOptionFromDDUnderSection("Supplier",arg0,arg2,true);
    }

    @When("select {string} option under section Recipient from dropdown {string}")
    public void select_option_under_section_Recipient_from_dropdown(String arg0, String arg2) throws Exception {
        if(arg0.trim().contains(arg2.trim()))
            arg0 = refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().selectOptionFromDDUnderSection("Recipient",arg0,arg2,true);
    }

    @And("select {string} option under section Recipient from warehouse dropdown")
    public void selectOptionUnderRecipientSectionFromwarehouseDropdown_Supplier(String arg0) throws Throwable {
        new LogisticsModule().selectOptionFromDDUnderSection("Recipient",arg0,"Warehouse",true);
    }

    @When("user select the item in the search")
    public void userSelectTheItemInTheSearch() {
        new LogisticsModule().selectItemFromSearch(number);
    }

    @And("verify {string} checkbox is checked")
    public void verifyBlockingCheckboxIsChecked(String arg) {
        new LogisticsModule().verifyCheckBoxIsChecked(arg);
    }

    @When("user click on lock icon to perform the intervention")
    public void userClickOnLockIconToPerformTheIntervention() {
        new MaintenancePlanning().clickUnLockInter();
    }

    String batchNum;
    @And("user capture the {string} field")
    public void CapturedNumberInField(String arg0) {
        batchNum=new LogisticsModule().captureBatchNumber(arg0);
    }

    @When("user return to the local receiption file by clicking on link available under column {string}")
    public void userReturnToTheLocalReceiptionFileByClickingOnLinkAvailableUnderColumn(String arg0) {
        commonMethods.explicitWaiting(5);
        new LogisticsModule().clickOriginalDocumentLink("Movements","Document of Origin");
    }

    @And("enter captured number in {string} field")
    public void enterCapturedNumberInField(String arg0) {
        new LogisticsModule().enterValueInField(arg0,batchNum);
    }

    @And("enter the captured number in {string} field")
    public void enterTheCapturedNumberInField(String arg0) {
        new LogisticsModule().enterValueInField(arg0,pckNum);
    }

    @And("no movement is created")
    public void noMovementIsCreated() {
        new LogisticsModule().verifyNoMovementCreated("Movements");
    }

    @And("user uncheck the {string} checkbox")
    public void userUncheckTheCheckbox(String arg0) {
        new LogisticsModule().clickCheckboxByLabel(arg0);
    }

    @When("select latest material linked to the package")
    public void selectLatestMaterialLinkedToThePackage() {
       new LogisticsModule().selectSearchResult("2","Material","Materials");
    }

    @When("user select the profile")
    public void userSelectTheProfile() {
        new LogisticsModule().selectAutomation145UserProfile();
    }

    @Then("add the Families if not associated")
    public void addTheIfNotAssociated() {
        new LogisticsModule().UpdateFamilies("Families");
    }

    @And("add the Sites if not associated")
    public void addTheSitesIfNotAssociated() {
        new LogisticsModule().updateSiteAssignment("Sites");
    }

    @And("add the Operators if not associated")
    public void addTheOperatorsIfNotAssociated() {
        new LogisticsModule().UpdateOperator("Operators");
    }

    @When("select the user")
    public void selectTheUser() {
        new LogisticsModule().selectUser();
    }

    @And("click on the number of initial repair request")
    public void clickOnTheNumberOfInitialRepairRequest() {
        new LogisticsModule().clickonInitialRR("Previous repair request");
    }

    @And("click on the number of redirected repair request")
    public void clickOnTheNumberOfRedirectedRepairRequest() {
        new LogisticsModule().clickonInitialRR("Redirected repair request");
    }


    @When("user navigates back to proposed {string}")
    public void userNavigatesBackToProposed(String arg0) {
        new LogisticsModule().ClickBreadcrumbs(arg0);
    }

    @And("verify the value of label {string} is {string}")
    public void verifyTheValueOfLabelIs(String arg0, String arg1) {
        new LogisticsModule().validateTextForRedirectedRR(arg0,arg1);
    }

    @And("user entered deleted procurement request in {string} field")
    public void userEnteredDeletedProcurementRequestInField(String arg0) {
        new LogisticsModule().enterValueInField(arg0,prNum);
    }


    @And("capture the {string} number")
    public void capture_The_Number(String arg0) throws Exception {
        prNum=new LogisticsModule().captureReceiptFromSearchTable(0);
    }

    @And("click on {string} text")
    public void clickOnMissingQuantityText(String arg0) {
        new LogisticsModule().clickMissingQuantity(arg0);
    }

    @When("user select the material from asset details popup")
    public void userSelectTheMaterialFromAssetDetailsPopup() {
        new LogisticsModule().selectMaterialFromAssetDetails();
    }

    @And("user click on {string} of material {string}")
    public void userClickOnOfMaterial(String arg0, String arg1) {
        new LogisticsModule().clickPackagingNumberOfMaterial(arg0,arg1);
    }

    @And("user click on {string} of Material {string}")
    public void userClickOnofMaterial(String arg0, String arg1) throws IOException {
        arg1 = refObj.getCellData(testDataRow, arg1);
        new LogisticsModule().clickPackagingNumberOfMaterial(arg0,arg1);
    }

    @And("click on search icon of {string} field on the page")
    public void clickOnSearchIconOfFieldOnThePage(String arg0) {
        new LogisticsModule().clickOnSearchIconOfFieldOnPage(arg0);
    }

    @And("wait for {string} seconds")
    public void waitForSeconds(String arg0) {
        commonMethods.explicitWaiting(Integer.parseInt(arg0));
    }


    @And("click on save button to submit the changes")
    public void clickSaveToSubmit() {
     new LogisticsModule().clickBtn("Save");
    }

    @Then("verify text no selected asset under {string}")
    public void verifyTextUnder(String arg0) {
        new LogisticsModule().noAssetSelectedText();

    }

    @Then("verify {string} section is displaying the asset")
    public void verifySectionIsDisplayingTheAsset(String arg0) {
        new LogisticsModule().snUnderSelectedAsset();
    }

    @And("{string} is checked")
    public void isChecked(String arg0) {
        new LogisticsModule().textStatusChecked(arg0);
    }

    @And("{string} information is filled with aircraft")
    public void informationIsFilledWithAircraft(String arg0) {
        new LogisticsModule().aircraftSetForSupplier();

    }

    @And("verify supplier store is display as {string}")
    public void verifySupplierStoreIsDisplayAs(String arg0) {
        new LogisticsModule().warehouseTextForSupplier(arg0);
    }

    @And("verify supplier store is Display as {string}")
    public void verifySupplierStoreIsdisplayAs(String arg0) throws IOException {
        arg0 = refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().warehouseTextForSupplier(arg0);
    }

    @And("select below values under {string}")
    public void selectBelowValuesUnder(String arg0,DataTable dt) {
        List<String> data=dt.asList();
        new LogisticsModule().selectCheckboxUnderSection(arg0,data);

    }

    @Then("verify item is created in {string} current status with below previous status")
    public void verifyItemIsCreatedInCurrentStatusWithBelowPreviousStatus(String arg0,DataTable dt) {
        List<String> data=dt.asList();
        new LogisticsModule().verifyStatusWithValues(arg0,data);
    }

    @Then("verify item is updated in {string} current status with below previous status")
    public void verifyItemIsUpdatedInCurrentStatusWithBelowPreviousStatus(String arg0,DataTable dt) {
        List<String> data=dt.asList();
        new LogisticsModule().verifyStatusWithValues(arg0,data);
    }

    @When("user select the checkbox with created {string} status")
    public void userSelectTheCheckboxWithCreatedStatus(String arg0) {
        new LogisticsModule().selectOperationCheckbox(arg0);
    }

    @Then("verify {string} item is deleted")
    public void verifyItemIsDeleted(String arg0) {
        new LogisticsModule().operationDeleted(arg0);
    }

    @And("open {string} by clicking on column {string} link under table {string}")
    public void openByClickingOnColumnLinkUnderTable(String arg0, String arg1, String arg2,DataTable arg) throws Exception {
        List<String> data=arg.asList();
        new LogisticsModule().openRowUsingColumnLink(arg1,arg2,Integer.parseInt(data.get(0)));
    }

    @Then("verify the status of both the {string} is {string} under column {string}")
    public void verifyTheStatusOfBothTheIsUnderColumn(String arg0, String arg1, String arg2) {
        new LogisticsModule().validateColumnValue(arg2,arg1,arg0);
    }

    @Then("verify entered code {string} is visible in the search result")
    public void verifyEnteredCodeIsVisibleInTheSearchResult(String arg0) {
        new LogisticsModule().verifyCodeExist(arg0);
    }

    @Then("verify entered code {string} is visible in the search Result")
    public void verifyEnteredCodeIsVisibleInSearchResult(String arg0) throws IOException {
        arg0 = refObj.getCellData(testDataRow, arg0);
        new LogisticsModule().verifyCodeExist(arg0);
    }

    @When("^user select the code available at below index$")
    public void user_select_the_code(DataTable arg) throws Throwable {
        List<String> data= arg.asList();
        new Dashboard().selectSearchResult(data.get(0),"Code");
    }

    @When("user select the code available at below index row")
    public void user_select_the_code_available_at_below_index_row(DataTable arg) {
        List<String> data= arg.asList();
        new Dashboard().selectSearchResultByRow("Results",data.get(0),"Code",true,"Code","Name");
    }

    @When("user select the Batch posts available at below index row")
    public void user_select_the_Batch_posts_available_at_below_index_row(DataTable arg) {
        List<String> data= arg.asList();
        new Dashboard().selectSearchResultByRow("Batch posts",data.get(0),"Status",true,"Article code","Status");
    }

    @When("enter captured value in {string} field")
    public void enterCapturedValueInField(String arg0) {
        new LogisticsModule().enterValueInField(arg0, manufacturingBatch.get("Material"));
    }

    @And("enter captured value in SN field")
    public void enterCapturedValueInSNField() {
        new LogisticsModule().enterValueInManufacturingBatch(manufacturingBatch.get("Batch Number"));
    }

    @And("enter the captured batch number in {string} field")
    public void enterTheCapturedBatchNumberInField(String arg0) {
        new LogisticsModule().enterValueInField(arg0, manufacturingBatch.get("Batch Number"));
    }

    @When("user open the captured batch number")
    public void userOpenTheCapturedBatchNumber() {
        new LogisticsModule().openBatchNumber(manufacturingBatch.get("Batch Number"));
    }

    @Then("verify captured {string} is present under {string} table")
    public void verifyCapturedIsPresentUnderTable(String arg0, String arg1) {
        new LogisticsModule().verifyCapturedPackageNumber(pckNum);
    }

    @Then("verify {string} and {string} of table {string} is filled with todays date")
    public void verifyAndOfTableIsFilledWithTodaysDate(String arg0, String arg1, String arg2) {
        new LogisticsModule().validateExpirationDate(arg2,arg0);
        new LogisticsModule().validateExpirationDate(arg2,arg1);
    }

    String pckNum;
    @And("capture the {string} from {string} table")
    public void captureTheFromTable(String arg0, String arg1) {
        pckNum=new LogisticsModule().capturePckNum(arg1,arg0);
    }


    @Then("verify material are sorted in order of decreasing satisfaction rate")
    public void verify_material_are_sorted_in_order_of_decreasing_satisfaction_rate() {
        new LogisticsModule().validateSatisfactionRateColumnFiltered("Satisfaction Rate", "Suggested Assets", "Descending");
    }

    @When("select {string} option from {string} dropdown on the filter popup")
    public void select_option_from_dropdown_on_the_filter_popup(String strArg1, String strArg2) throws Exception {
        new LogisticsModule().selectRecipientSite_PRFilterPage(strArg1,strArg2);
    }

    @When("select {string} Option from {string} dropdown on the filter popup")
    public void select_Option_from_dropdown_on_the_filter_popup(String strArg1, String strArg2) throws Exception {
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().selectRecipientSite_PRFilterPage(strArg1,strArg2);
    }

    @When("select {string} option from Warehouse dropdown on the filter popup")
    public void select_option_from_Warehouse_dropdown_on_the_filter_popup(String strArg1) throws Exception {
        new LogisticsModule().selectRecipientWarehouse_PRFilterPage(strArg1);
    }

    @When("select {string} option from Warehouse dropdown on filter popup")
    public void select_option_from_Warehouse_dropdown_on_filter_popup(String strArg1) throws Exception {
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().selectRecipientWarehouse_PRFilterPage(strArg1);
    }

    @Then("verify materials are filtered based on site {string} and warehouse {string}")
    public void verify_materials_are_filtered_based_on_site_and_warehouse(String strArg1, String strArg2) {
        new LogisticsModule().validateMaterialFilteredAsPerLocation("Location", "Suggested Assets", strArg1,strArg2);
    }

    @Then("verify Materials are filtered based on site {string} and warehouse {string}")
    public void verify_Materials_are_filtered_based_on_site_and_warehouse(String strArg1, String strArg2) throws Exception{
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        strArg2 = refObj.getCellData(testDataRow, strArg2);
        new LogisticsModule().validateMaterialFilteredAsPerLocation("Location", "Suggested Assets", strArg1,strArg2);
    }

    @When("user {string} the filter")
    public void user_the_filter(String strArg1) {
        new LogisticsModule().clickOnFilterIcon(true);
        new LogisticsModule().clickOnBtn("Reset",true,"Filter page");
    }

    String snNum;
    @When("capture the {string} from first row if exist")
    public void capture_the_from_first_row_if_exist(String strArg1) {
        snNum = new LogisticsModule().captureColumnValueOfTypeLink(strArg1,1);
        System.out.println(snNum);
    }

    @When("user enters the captured {string} in sn field")
    public void user_enters_the_captured_in_sn_field(String strArg1) {
        new LogisticsModule().enterValueInSNField(strArg1, snNum);
    }

    @Then("verify material with same {string} is displayed")
    public void verify_material_with_same_is_displayed(String strArg1) {
        new LogisticsModule().validateMaterialFilteredAsPerSn(strArg1, "Suggested Assets", snNum);
    }

    @When("user clicks on {string} button on filter {string}")
    public void user_clicks_on_button_on_filter(String strArg1, String strArg2) {
        new LogisticsModule().clickOnSatisfactionRateOptionBtn(strArg1,strArg2);
    }

    @Then("verify materials located in {string} appears")
    public void verify_materials_located_in_appears(String strArg1) {
        new LogisticsModule().validateMaterialFilteredAsPerSite("Location", "Suggested Assets", strArg1);
    }

    @Then("verify Materials located in {string} appears")
    public void verify_Materials_located_in_appears(String strArg1) throws Exception {
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().validateMaterialFilteredAsPerSite("Location", "Suggested Assets", strArg1);

    }

    @Then("verify only materials located in other DA appears")
    public void verify_only_materials_located_in_other_DA_appears() {
        new LogisticsModule().validateMaterialOfOtherDA("Availability at Date","Suggested Assets");
    }

    @When("select option other than {string} from {string} dropdown on the popup")
    public void select_option_other_than_from_dropdown_on_the_popup(String strArg1, String strArg2) throws Exception {
        new Dashboard().selectOptionFromCriteriaDropdown("Crashed CR", strArg2);
    }

    @Then("verify no material is displayed under {string}")
    public void verify_no_material_is_displayed_under(String strArg1) {
        new LogisticsModule().validateNoMaterialInTable(strArg1);
    }

    @And("^click on \"([^\"]*)\" button on Advanced search popup$")
    public void click_on_something_button_on_Advanced_search_popup(String strArg1) throws Throwable {
        new MaintenancePlanning().clickButtonOnPopUP("Advanced search", strArg1);
    }

    @When("^user select item under advanced search table on the popup$")
    public void user_select_the_item_underast_something_table_on_the_popup() throws Throwable {
        new LogisticsModule().AddItemToListForRepair("search equipment");
    }

    String repairReqNum;
    @Then("user capture the repair request {string} number")
    public void user_capture_the_repair_request_number(String strArg1) {
        repairReqNum = new LogisticsModule().captureRepairRequestNumber(strArg1);
        System.out.println("repair request number - " + repairReqNum);
    }

    String transOrderNumber;
    @Then("user capture the transfer order {string} number")
    public void user_capture_the_transfer_order_number(String strArg1) {
        transOrderNumber= new LogisticsModule().captureTransferOrderNumber(strArg1);
        System.out.println("TO number" + transOrderNumber);
    }

    @When("user enter the captured Repair request number on {string} field on Transfer Order page")
    public void user_enter_the_captured_Repair_request_number_on_field_on_Transfer_Order_page(String strArg1) {
        new MaintenancePlanning().enterCriteria(strArg1, repairReqNum);
    }

    @Then("verify created Repair request number is available under {string} field")
    public void verify_created_Repair_request_number_is_available_under_field(String strArg1) {
       new LogisticsModule().validateRRinTOPage(repairReqNum, strArg1);
    }

    @When("^click on Search Button$")
    public void click_on_Search_Button() throws Throwable {
        new Dashboard().searchPerform();
        new MaintenancePlanning().waitTillDataLoaded("Yes");
    }

    @When("user delete the Recipient site dropdown value if exist")
    public void user_delete_the_Recipient_site_dropdown_value_if_exist() {
        new LogisticsModule().clearRecipientSite();
    }

    @When("user delete the Shipper site dropdown value if exist")
    public void user_delete_the_Shipper_site_dropdown_value_if_exist() {
        new LogisticsModule().clearShipperSite();
    }

    @When("user delete the Recipient Warehouse dropdown value if exist")
    public void user_delete_the_Recipient_Warehouse_dropdown_value_if_exist() {
        new LogisticsModule().clearRecipientWarehouse();
    }

    @When("user delete the Receiver site dropdown value if exist")
    public void user_delete_the_Receiver_site_dropdown_value_if_exist() {
        new LogisticsModule().clearReceiverSite();
    }

    @When("user delete the site dropdown value if exist")
    public void user_delete_the_site_dropdown_value_if_exist() {
        new LogisticsModule().clearSite();
    }

    @When("select {string} option from {string} dropdown from {string} section on the popup")
    public void select_option_from_dropdown_from_section_on_the_popup(String strArg1, String strArg2, String strArg3) throws Exception {
        if(strArg1.trim().contains(strArg2.trim()))
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        new Dashboard().selectOptionFromCriteriaDropdownUnderSectionPopup(strArg1, strArg2,strArg3);
    }

    @When("user select {string} option from {string} dropdown")
    public void user_select_option_from_dropdown(String strArg1, String strArg2) throws Exception {
        if(strArg1.trim().contains(strArg2.trim()))
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().selectReceiptType(strArg1);
    }

    @When("^user delete all the available documents$")
    public void user_deletes_all_document() throws Throwable {
        commonMethods.explicitWaiting(4);
        String cases=new LogisticsModule().selectAllSearchResults("Receipt", "Documents");
        if(!cases.equalsIgnoreCase("0")) {
            new LogisticsModule().clickOnBtn("delete");
            new MaintenancePlanning().clickOnPopupBtnIfAppears("Yes");
        }
    }


    @Then("select option from Weight {string} dropdown from add package popup")
    public void select_option_from_Weight_dropdown_from_add_package_popup(String strArg1) throws Exception {
        new LogisticsModule().selectOptionFromUnitDD(strArg1,0,"assign package");
    }

    @When("click on Procurement Request number")
    public void click_on_Procurement_Request_number() {
        new LogisticsModule().clickOnPRNumber();
    }

    @Then("verify {string} field as {string} under section {string}")
    public void verify_field_as_under_section(String strArg1, String strArg2, String strArg3) {
        new LogisticsModule().validateValueInField_TypeUnderSection(strArg3,strArg1, strArg2);
    }

    @Then("verify {string} field as {string} under Section {string}")
    public void verify_field_as_under_Section(String strArg1, String strArg2, String strArg3) throws IOException {
        strArg2 = refObj.getCellData(testDataRow, strArg2);
        new LogisticsModule().validateValueInField_TypeUnderSection(strArg3,strArg1, strArg2);
    }

    @Then("verify {string} field as captured value under section {string}")
    public void verify_field_as_captured_value_under_section(String strArg1, String strArg3) {
        String strArg2=pageSubtitleValue;
        new LogisticsModule().validateValueInField_TypeUnderSection(strArg3,strArg1, strArg2);
    }
    @When("user enters {string} and {string} and {string}")
    public void userEntersAndAnd(String arg1, String arg2, String arg3) throws Throwable {
        if(new JsonUtils().getJsonValue("browser").toLowerCase().contains("edge")) {
            commonMethods.explicitWaiting(15);
        }else
            commonMethods.explicitWaiting(5);
        new Login().enterUsernameAndPasswordAndLanguage(arg1,refObj.getCellData(testDataRow, "Username"),arg2, refObj.getCellData(testDataRow, "Password"),arg3,refObj.getCellData(testDataRow, "Language"));
    }
    @And("click on filecopy icon")
    public void clickOnFilecopyIcon(DataTable arg) {
        List<String> data= arg.asList();
        new LogisticsModule().clickOnFilecopyIcon(data.get(0));
    }

    @And("verify the item is appeared on document list")
    public void verifyTheItemIsAppearedOnDocumentList(DataTable arg) {
        List<String> data= arg.asList();
        new LogisticsModule().verifyTheItemIsAppearedOnDocumentList(data);
    }

    @And("verify the no item is appeared on document list")
    public void verifyTheNoItemIsAppearedOnDocumentList() {
        new LogisticsModule().verifyTheNoItemIsAppearedOnDocumentList();
    }


    @When("user entered {string} in {string} field")
    public void userEnteredInField(String strArg1, String strArg2) throws Throwable {
        if(strArg1.trim().contains(strArg2.trim()))
        {
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        }
        new LogisticsModule().enterValueinField(strArg2, strArg1);
    }

    @And("user enter date in {string} field")
    public void userEnterDateInField(String arg) {
        new LogisticsModule().userEnterDateInField(arg);
    }

    @And("enters {string} in Quantity field")
    public void entersInQuantityField(String arg0) {
        new LogisticsModule().entersInQuantityField(arg0);
    }

    @And("Select {string} option from {string} dropdown from {string} section")
    public void selectOptionFromDropdownFromSection(String strArg1, String strArg2, String strArg3) throws Exception {
        if(strArg1.trim().contains(strArg2.trim())) {
            strArg1 = refObj.getCellData(testDataRow, strArg1);
        }
        new Dashboard().selectOptionFromCriteriaDropdownUnderSectionpopup(strArg1, strArg2,strArg3);
    }

    Map<String,String> items= new HashMap<String,String>();
    @And("^user capture the \"([^\\\"]*)\" details and verify checkbox is disabled at below index$")
    public void verifyCheckboxIsDisabledAtBelowIndex(String strArg1,DataTable arg) {
        List<String> data= arg.asList();
        items.put(strArg1, new LogisticsModule().captureColumnValueOfCheckboxByRow(strArg1, Integer.parseInt(data.get(0)),"Items"));
        System.out.println("LIM "+items.get(strArg1));
//        new LogisticsModule().verifyCheckBoxIsChecked(data);
    }

    @And("^click on Validate button on material popup$")
    public void click_on_button_something_on_material_popup() throws Throwable {
        new LogisticsModule().clickValidateOnMaterialPopup();
    }
    @When("User return to the local receiption file by clicking on link available under column {string}")
    public void userReturnToTheLocalReceiptionFileByClickingOnLink(String arg0) {
        new LogisticsModule().clickLinkUnderOriginalDocumentColumn();
            }


    @When("User open the receipt file")
    public void userOpenReceiptFile()  throws Throwable {
        new LogisticsModule().openReceiptFromSearchResultTable();
    }

    @Then("Verify Procurement Request is added to the work order")
    public void verifyProcurementRequestIsAddedToTheWorkOrder() {
        new LogisticsModule().PRCreatedUnderRM("Request Management ", "Created On");
    }
    @When("user select the code available at below index for Receipt folder {int}")
    public void user_select_the_code(int index) throws Throwable {
        //List<String> data= arg.asList();
        new Dashboard().selectSearchResultFromBelowIndex(index,"Code");
    }
    @Then("^select \"(.*?)\" Option from \"(.*?)\" dropdown from \"(.*?)\" section$")
    public void select_option_from_dropdown_from_section(String arg1,String arg2,String arg3) throws Throwable {
        if(arg1.trim().contains(arg2.trim()))
            arg1 = refObj.getCellData(testDataRow, arg1);
        new Dashboard().selectOptionFromSiteDropdownUnderShipperSection(arg1, arg2,arg3);
    }
    String prNum;
    @And("capture the {string} Number")
    public void captureTheNumber(String arg0) throws Exception {
        prNum=new LogisticsModule().captureProcurementNoFromSearchTable(0);
    }


    @Then("Verify search results are displayed under {string}")
    public void verifySearchResultsAreDisplayedUnder(String arg0) {
        new LogisticsModule().verifyResultsDisplayed(arg0);
    }
    @And("^click on Reset Button$")
    public void click_on_Button_Reset() throws Throwable {
        new LogisticsModule().ResetButtonOnFilterPopup("Reset");
    }
    @When("Enter {string} in First Name field")
    public void enter_in_First_Name_Field(String strArg1) throws IOException {
        strArg1 = refObj.getCellData(testDataRow, strArg1);
        new LogisticsModule().EnterValueInField("First Name", strArg1);
    }
}
