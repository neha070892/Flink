package com.aeroweb.utils;

public class ExceptionHandler {
    public void unhandledException(Exception e){
        System.out.println(e.getMessage());
    }

    public void customizedException(String msgDescription){
        new ReportUtil().logFail("",msgDescription);
    }
}
