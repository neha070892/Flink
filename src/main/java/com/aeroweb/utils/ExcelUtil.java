package com.aeroweb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

public class ExcelUtil {
	Workbook xlWorkBook;
	Sheet xlSheet;
	int rowNo;
	String excelPath;

	/* Constructor Started */
	public ExcelUtil() {

	}

	public ExcelUtil(String excelPathWithName) throws IOException {
		excelPath = excelPathWithName;
		setWorkBook(excelPathWithName);
	}

	public ExcelUtil(String excelPathWithName, String sheetName) throws IOException {
		excelPath = excelPathWithName;
		setSheet(excelPathWithName, sheetName);
	}

	/* Constructor Completed */

	public Workbook getWorkBook(String excelPathWithName) throws IOException {
		Workbook xlWorkBook;
		if ((FilenameUtils.getExtension(excelPathWithName)).equalsIgnoreCase("xlsx")) {
			xlWorkBook = new XSSFWorkbook(excelPathWithName);
//			System.out.println("xlsx object created");
		} else {
			xlWorkBook = new HSSFWorkbook();
//			System.out.println("xls object created");

		}
		return xlWorkBook;

	}

	public void setWorkBook(String excelPathWithName) throws IOException {
		this.xlWorkBook = getWorkBook(excelPathWithName);
	}

	public Sheet getSheet(String sheetName) {
		return this.xlWorkBook.getSheet(sheetName);

	}

	public void setSheet(String excelPathWithName, String sheetName) throws IOException {
		this.xlWorkBook = getWorkBook(excelPathWithName);
		this.xlSheet = getSheet(sheetName);
	}

	public void setSheet(String sheetName) throws IOException {
		this.xlSheet = getSheet(sheetName);
	}

	// *****************************************************************************************************//
	/* Get specific Row */

	public Row getRow(String sheetName, int rowNo) throws IOException {
		return this.getSheet(sheetName).getRow(rowNo);
	}

	public Row getRow(Sheet sheet, int rowNo) throws IOException {
		return sheet.getRow(rowNo);
	}

	public Row getRow(int rowNo) throws IOException {
		return this.xlSheet.getRow(rowNo);
	}

	/* Get all Rows */

	public List<Row> getRow(Sheet sheet) throws IOException {
		List<Row> rowArrayList = new ArrayList<Row>();
		for (int currentRow = 0; currentRow <= this.getRowCount(sheet); currentRow++)
			rowArrayList.add(sheet.getRow(currentRow));
		return rowArrayList;
	}
//	// *****************************************************************************************************//

	public String getCellData(String sheetName, int rowNo, int colNo) throws IOException {
		return getCellVal(this.getSheet(sheetName).getRow(rowNo).getCell(colNo));
	}

	public String getCellData(Sheet sheet, int rowNo, int colNo) throws IOException {
		return getCellVal(sheet.getRow(rowNo).getCell(colNo));
	}

	public String getCellData(int rowNo, int colNo) throws IOException {
		return getCellVal(this.xlSheet.getRow(rowNo).getCell(colNo));
	}

	public String getCellData(Row row, int colNo) throws IOException {
		// return getCellVal(row.getCell(colNo));
		return getCellVal(row.getCell(colNo));
	}

	public String getCellData(String sheetName, int rowNo, String colName) throws IOException {
		Sheet sheet = getSheet(sheetName);
		int colNo = this.getColHeaderNo(sheet, colName);
		return getCellVal(this.getSheet(sheetName).getRow(rowNo).getCell(colNo));
	}

	public String getCellData(Sheet sheet, int rowNo, String colName) throws IOException {
		int colNo = this.getColHeaderNo(sheet, colName);
		return getCellVal(sheet.getRow(rowNo).getCell(colNo));
	}

	public String getCellData(int rowNo, String colName) throws IOException {
		int colNo = this.getColHeaderNo(this.xlSheet, colName);
		return getCellVal(this.xlSheet.getRow(rowNo).getCell(colNo));
	}

	public String getCellData(String sheetName, Row row, String colName) throws IOException {
		int colNo = this.getColHeaderNo(getSheet(sheetName), colName);
		return getCellVal(row.getCell(colNo));
	}

	public String getCellData(Row row, String colName) {
		try {

			int colNo = this.getColHeaderNo(row.getSheet(), colName);
//			int colNo = this.getColHeaderNo(this.xlSheet, colName);
			return getCellVal(row.getCell(colNo));
		} catch (Exception e) {
			return null;
		}

	}

	// get Cell data based on row No and column no
	public String getCellVal(Cell col) {
		String cellVal = "";
		CellType cell_Type = col.getCellType();
		switch (cell_Type) {
		case NUMERIC:
			// cellVal=new BigDecimal(col.getNumericCellValue()).toString();
			// cellVal = "" + col.getNumericCellValue() + "";

			cellVal = new DataFormatter().formatCellValue(col);
			break;
		case STRING:
			cellVal = col.getStringCellValue();
			break;
		case FORMULA:
			// cellVal = col.getStringCellValue();
			switch (col.getCachedFormulaResultType()) {
			case NUMERIC:
				cellVal = new BigDecimal(col.getNumericCellValue()).toString();
				// FormulaEvaluator formulaEval =
				// xlsWorkbook.getCreationHelper().createFormulaEvaluator();
				// cellVal=formulaEval.evaluate(col).getNumberValue();
				// cellVal=this.formulaEval.evaluateInCell(col).getCellFormula();
				break;
			case STRING:
				cellVal = col.getStringCellValue();
				break;
			case ERROR:
				cellVal = Byte.toString(col.getErrorCellValue());
				break;
			case BOOLEAN:
				cellVal = Boolean.toString(col.getBooleanCellValue());
				break;
			case BLANK:
				cellVal = "";
				break;
			default:
				System.out.print("Report to automation team need to implement the logic for the cell type in formula:"
						+ col.getCachedFormulaResultType());
				break;
			}
			break;
		case BLANK:
			cellVal = "";
			break;
		case BOOLEAN:
			cellVal = Boolean.toString(col.getBooleanCellValue());
			break;
		case ERROR:
			cellVal = Byte.toString(col.getErrorCellValue());
			System.out.println("formula has an error");
			break;
		default:
			System.out.print("Report to automation team need to implement the logic for the cell type:" + cell_Type);
			break;
		}
		return cellVal;
	}

	public int getColHeaderNo(Sheet sheet, String colName) throws IOException {
		int colNo = -1;
		int totCol = getColCount(sheet, 0);
		int currentColNo;
		for (currentColNo = 0; currentColNo <= totCol; currentColNo++) {
			if (getCellData(sheet, 0, currentColNo).equalsIgnoreCase(colName)) {
				colNo = currentColNo;
				break;
			} else if (currentColNo == totCol) {
				System.out.println("Column header not found in excel file: " + colName);
			}
		}
		return colNo;
	}

	public int getColHeaderNo(String sheetName, String colName) throws IOException {
		Sheet sheet = getSheet(sheetName);
		int colNo = -1;
		int totCol = getColCount(sheet, 0);
		int currentColNo;
		for (currentColNo = 0; currentColNo <= totCol; currentColNo++) {
			if (getCellData(sheet, 0, currentColNo).equalsIgnoreCase(colName)) {
				colNo = currentColNo;
				break;
			} else if (currentColNo == totCol) {
				System.out.println("Column header not found in excel file: " + colName);
			}
		}
		return colNo;
	}

	// get total columns of specific column of specific sheet sheet
	public int getColCount(Sheet sheet, int rowNo) {
		return sheet.getRow(rowNo).getLastCellNum();
	}

	public int getColCount(int rowNo) {
		return this.xlSheet.getRow(rowNo).getLastCellNum();
	}

	// get total rows of specific sheet
	public int getRowCount(Sheet sheet) {
		return sheet.getLastRowNum();
	}

	public int getRowCount(String sheetName) {
		return getSheet(sheetName).getLastRowNum();
	}

	public int getRowCount() {
		return this.xlSheet.getLastRowNum();
	}

	public List<Row> filterRow(Sheet sheet, String columnNameToFilter, String criteria) throws IOException {
		List<Row> row = getRow(sheet);
		return row.stream().filter(currentRow -> getCellData(currentRow, columnNameToFilter).equals(criteria))
				.collect(Collectors.toList());
	}

	public List<Row> filterRow(List<Row> row, String columnNameToFilter, String criteria) {
		return row.stream().filter(currentRow -> getCellData(currentRow, columnNameToFilter).equals(criteria))
				.collect(Collectors.toList());
	}

	public List<Row> filterRow(String columnNameToFilter, String criteria) throws IOException {
		List<Row> row = getRow(this.xlSheet);
		return row.stream().filter(currentRow -> getCellData(currentRow, columnNameToFilter).equals(criteria)).collect(Collectors.toList());
	}

	public void setcellValue(int row, int column, String value) throws IOException {
		Row sheetrow = xlSheet.getRow(row);
		Cell cell = null;
		if (sheetrow == null)
			sheetrow = xlSheet.createRow(row);
		cell = sheetrow.getCell(column);
		if (cell == null)
			cell = sheetrow.createCell(column);
		cell.setCellValue(value);
		xlSheet.getRow(1).getCell(1).setCellValue(value);

//        FileOutputStream outFile =new FileOutputStream(new File(excelPath));
//        xlWorkBook.write(outFile);
//        outFile.close();

//        try (
//        		FileOutputStream fileOut = new FileOutputStream(excelPath + ".new")) {
//	        	xlWorkBook.write(fileOut);
//	        	fileOut.close();
//        }
//        Files.delete(Paths.get(new File(excelPath).getAbsolutePath()));
//        Files.move(Paths.get(excelPath + ".new"), Paths.get(excelPath));
	}

	public static void setcellValue1(String fileName, String sheetName,int rowNo, int columnNo, String value )
			throws IOException {
		InputStream inp = new FileInputStream(fileName);
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheet(sheetName);
		int num = sheet.getLastRowNum();
		Row row = sheet.createRow(++num);
		Row sheetrow = sheet.getRow(rowNo);
		if (sheetrow == null)
			sheetrow = sheet.createRow(rowNo);
		Cell cell = null;
		cell = sheetrow.getCell(columnNo);
		if (cell == null)
			cell = sheetrow.createCell(columnNo);
		sheet.getRow(1).getCell(1).setCellValue(value);
		// Now this Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();
	}

	public void saveChangesInFile() throws IOException {
		File file = new File(excelPath + ".new");
		if (file.exists())
			file.delete();
		FileOutputStream fileOut = new FileOutputStream(excelPath + ".new");
		xlWorkBook.write(fileOut);
		fileOut.close();
	}

}