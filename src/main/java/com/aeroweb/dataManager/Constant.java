package com.aeroweb.dataManager;

import java.awt.Container;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.Reporter;

import com.aeroweb.utils.ExcelUtil;
import com.aeroweb.utils.JsonUtils;
import com.aeroweb.utils.ReportUtil;

import io.cucumber.java.Scenario;


public class Constant {
	private static String testCaseId;
	private static int iteration;

	static Sheet sheet;
	static List<Row> row;

	public static String getTestCaseId() {
		return testCaseId;
	}

	public static void setTestCaseId(String testCaseId) {
		Constant.testCaseId = testCaseId;
	}

	public static int getIteration() {
		return iteration;
	}

	public static void setIteration(int iteration) {
		Constant.iteration = iteration;
	}

	public static void initialiseTestData(Scenario scenario) {
		try {
			String tcName = scenario.getName();
			ExcelUtil excelUtil = new ExcelUtil(new JsonUtils().getJsonValue("testDataFilePath"));
			sheet = excelUtil.getSheet(new JsonUtils().getJsonValue("testDataSheet"));
			row = excelUtil.filterRow(sheet, "TestcaseName", tcName);
			if (row == null) {
				new ReportUtil().logFail("Testdata for testcase " + tcName + " Should be available",
						"Testdata not found");
			}
		} catch (IOException e) {
			new ReportUtil().logFail("Testdata file should be readable", "Cannot read Testdata file");
		}
	}

	public static String getData(String columnName) {
		int iteration = Constant.iteration;
		try {
			if (iteration <= row.size()) {
				if (iteration != 0)
					iteration--;
				String calu = new ExcelUtil().getCellData(row.get(iteration),
						new ExcelUtil().getColHeaderNo(sheet, columnName));
				return calu;
			} else
				new ReportUtil().logFail("Test data for iteration " + Constant.iteration + " should be present",
						"Test data for iteration " + Constant.iteration + " not present");
			return null;
		} catch (IOException e) {
			return null;
		} catch (NullPointerException e) {
			new ReportUtil().logInfo("No Value found for column " + columnName);
			return null;
		}

	}

}
