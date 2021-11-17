package com.aeroweb.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

public class Teams {


    public static void sendMessageToTeams(String message)
    {
        RestAssured.baseURI = "https://steria.webhook.office.com";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();

        requestParams.put("text",message); // Cast
        request.body(requestParams.toString());
        Response response = request.post("/webhookb2/5555d581-01f1-44da-937e-d8ded852ba2f@8b87af7d-8647-4dc7-8df4-5f69a2011bb5/IncomingWebhook/4883c6cfb76e4d34913b31e08027b432/f11d3ae4-2652-4531-bff5-2689eb02c960");

        int statusCode = response.getStatusCode();

    }


}
