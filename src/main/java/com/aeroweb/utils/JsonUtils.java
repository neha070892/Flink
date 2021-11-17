package com.aeroweb.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonUtils {

	JSONObject jo;

	public JsonUtils() {
		Object obj;
		try {
			obj = new JSONParser().parse(new FileReader("src/test/resources/properties/Env.Json"));
			jo = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			new ReportUtil().logFail("Config file should be present", "Config file not present");
		} catch (IOException | ParseException e) {
			new ReportUtil().logFail("Config file Should be readable", "Config file not readable");
		}
	}

	public JsonUtils(String filePath) {
		Object obj;
		try {
			obj = new JSONParser().parse(new FileReader(filePath));
			jo = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			new ReportUtil().logFail("Config file should be present", "Config file not present");
		} catch (IOException | ParseException e) {
			new ReportUtil().logFail("Config file Should be readable", "Config file not readable");
		}
	}

	public String getJsonValue(String jsonKey) {
		if (jo.containsKey(jsonKey))
			return (String) jo.get(jsonKey);
		else
			return null;
	}

	public String getScreenshotOption() {
		return this.getJsonValue("screenshotInReport");
	}

	public String getBrowser() {
		return this.getJsonValue("browser");
	}
	public String  getPortalURL(){
        return this.getJsonValue("PortalURL");
    }

	public int getMaxWaitTime(){
        return Integer.parseInt(this.getJsonValue("maxWait"));
    }
	public static void main(String[] args) {
		JsonUtils a = new JsonUtils("src/test/resources/properties/Env.Json");
		System.out.println(a.getJsonValue("browser"));
		System.out.println(a.getJsonValue("screenshotInReport"));
		System.out.println(a.getJsonValue("Password"));
		System.out.println(a.getJsonValue("browser1"));
		if (a.getJsonValue("browser1") == null)
			System.out.println("Value Not Found");
		else
			System.out.println("Value Found");
	}

}
