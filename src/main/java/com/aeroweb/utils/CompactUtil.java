package com.aeroweb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CompactUtil {

	public static void main(String args[]) {
//        System.out.println(new CompactUtil().removeSpecialCharacterInFileName("man /:*?\"<>|ish"));
	}

	// To be used in Reporting to create the folder and file
	public String getCurrentTimeStemp(String pattern) {
		return new SimpleDateFormat(pattern).format(new Timestamp(System.currentTimeMillis()));
	}

	// To create the folder on hard drive
	public void createFolder(String directory) {
		File fileLocation = new File(directory);
		if (!fileLocation.exists())
			new File(directory).mkdirs();
	}

	public String getSystemName() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostName();
		} catch (Exception e) {
			new ExceptionHandler().customizedException("Cannot get the host name of execution machine");
			return "";
		}
	}

	public String removeSpecialCharacterInFileName(String fileName) {
//		fileName = fileName.replace(" ", "_");
		fileName = fileName.replace("\\", "");
		fileName = fileName.replace("/", "");
		fileName = fileName.replace(":", "");
		fileName = fileName.replace("*", "");
		fileName = fileName.replace("?", "");
		fileName = fileName.replace("\"", "");
		fileName = fileName.replace("<", "");
		fileName = fileName.replace(">", "");
		fileName = fileName.replace("|", "");
		return fileName;
	}

	public static void closeSpecificProcess(String processName) {
		try {
			Process process = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static String extractNumber(String str) {

		str = str.replaceAll("[^\\d]", " ");
		str = str.trim();
		str = str.replaceAll(" +", " ");

		if (str.equals(""))
			return null;
		else
			return str;

	}

	/**
	 * THIS FUNCTION JUST CHECKS THE EXISTENCE OF FILE
	 * 
	 * @param-filePath
	 * @param seconds
	 * @return
	 */

	public static boolean checkFileDownloaded(String actualFilePath, int seconds, String expectedFilePath) {
		long expectedFilesize = new File(expectedFilePath).length();
		File actualFile = new File(actualFilePath);
		do {

			if (seconds <= 0)
				return false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			seconds--;
		} while ((!actualFile.exists()) && actualFile.length() != expectedFilesize);
		return true;

	}

	/**
	 * verifies whether a PDF is readable or not
	 * 
	 * @param fileName
	 */

	public static boolean verifyPDFReadable(String fileName) {
		String basePath = ReportUtil.getExtentReportFolder() + File.separator + "files" + File.separator;
		String actualFilePath = basePath + "downloded" + File.separator + fileName;
		try {
			File file = new File(actualFilePath);
			PDDocument doc = null;

			doc = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			System.out.println(pdfStripper.getText(doc));
			new ReportUtil().logInfo("Content in the downloaded file --------"+pdfStripper.getText(doc));
			doc.close();
			return true;
		} catch (Exception e) {
			System.out.println("File Not readable");
			return false;
		}

	}

	/**
	 * verifies whether a Docx is readable or not
	 * 
	 * @param fileName
	 */
	public static boolean verifyDocxReadable(String fileName) {
		String basePath = ReportUtil.getExtentReportFolder() + File.separator + "files" + File.separator;
		String actualFilePath = basePath + "downloded" + File.separator + fileName;
		FileInputStream stream = null;
		XWPFDocument docx = null;
		try {

			stream = new FileInputStream(actualFilePath);
			docx = new XWPFDocument(OPCPackage.open(stream));
			XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
			System.out.println(extractor.getText());
			return true;
		} catch (IOException | InvalidFormatException e) {
			e.printStackTrace();
			System.out.println("File Not readable");
			return false;
		}

	}
	public static String extractNum(String str) {

		str = str.replaceAll("[^\\d]", " ");
		str = str.trim();
		str = str.replaceAll("\\s.*", "");

		if (str.equals(""))
			return null;
		else
			return str;

	}
}
