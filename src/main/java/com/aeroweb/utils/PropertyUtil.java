package com.aeroweb.utils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    public static String propertyFilePath="src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
    public InputStream input;

    public PropertyUtil() {
        try{
            propertyFilePath = "src/test/resources/properties/"+System.getProperty("appLanguage")+".properties";
            this.input = new FileInputStream(propertyFilePath);
        }catch(Exception e){
            new ExceptionHandler().unhandledException(e);
        }
    }

    public PropertyUtil(String propertyFilePath) {
        try{
            propertyFilePath = propertyFilePath;
            this.input = new FileInputStream(propertyFilePath);
        }catch(Exception e){
            new ExceptionHandler().unhandledException(e);
        }
    }
    public String readPropertyData(String propertyName) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(propertyFilePath));
            String str=prop.getProperty(propertyName).trim();
            return str;
        } catch (Exception e) {
            new ExceptionHandler().customizedException("Value is null for "+propertyName);
            return null;
        }
    }

    public void setPropertyData(String propertyName, String propertyVal) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(this.propertyFilePath));
            prop.setProperty(propertyName, propertyVal);
            prop.store(new FileOutputStream(this.propertyFilePath), null);

        } catch (IOException e) {
            new ExceptionHandler().unhandledException(e);
        }
    }

    // Function to read the property value
    public String readPropertyData(String propertyFilePath, String propertyName) {
        try {
            Properties prop = new Properties();
            InputStream input = new FileInputStream(propertyFilePath);
            prop.load(input);
            return prop.getProperty(propertyName);
        } catch (IOException e) {
            new ExceptionHandler().unhandledException(e);
            return null;
        }
    }

    public String  getBrowser(){
        return this.readPropertyData("browser");
    }
    
    public String  getPortalURL(){
        return this.readPropertyData("PortalURL");
    }

    public String getURL(){
        return this.readPropertyData("url");
    }

    public String getUID(){
        return this.readPropertyData("uid");
    }

    public String  getPWD(){
        return StringUtil.jasyptDecryption(this.readPropertyData("pwd"));
    }


    //Possible values are
    /*
    * always--> Always on step it will take screenshot
    * onPass--> Will take screenshot on pass, fail and click, info will be ignored
    * onFail--> Will take screenshot on fail and click, info and pass will be ignored
    * onClick--> Will take screenshot on click, info, pass and fail will be ignored
    * never--> Never will take screenshot
     */
    public String getScreenshotOption(){
        return this.readPropertyData("src/test/resources/properties/Env.properties","screenshotInReport");
    }
//    jira update value could be yes and no
    public String getSpiraUpdate(){
        return this.readPropertyData("src/test/resources/properties/Env.properties","spiraUpdate");
    }
    
//    public String getJiraUpdate(){
//        return this.readPropertyData("src/test/resources/properties/Env.properties","jiraUpdate");
//    }
    
    
    public int getMaxWaitTime(){
        return Integer.parseInt(this.readPropertyData("src/test/resources/properties/Env.properties","maxWait"));
    }
}
