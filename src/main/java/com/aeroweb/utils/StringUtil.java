package com.aeroweb.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.text.DecimalFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StringUtil {
   
    public String getPercentSharePattern(String percent){
        return (convertStringInPattern(percent, "##.00") + " %");
    }

    public String removeWhiteSpaces(String str){
        return str.trim().replaceAll("\n"," ");
    }

    public String convertStringInPattern(String strToConvert, String pattern){
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(Float.parseFloat(strToConvert));
    }
    public static String jasyptEncryption(String tobeEncrypted) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("password");
        String encryptedText = encryptor.encrypt(tobeEncrypted);
        return encryptedText;

    }

    public static String jasyptDecryption(String tobeDecrypted) {
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword("password");
        String decryptedText = decryptor.decrypt(tobeDecrypted);
        return decryptedText;
    }
    public static void main(String[] ar)
    {
//    	String a ="Administration2020";
    	String a ="Vikas@1977";
    	System.out.println(a);
    	a= jasyptEncryption(a);
    	System.out.println(a);
    	a= jasyptDecryption(a);
    	System.out.println(a);
    	
    	
    }
}
