package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FeatureFlag {

    private static final String FEATURE_FLAG_URL = "https://backendless.nblocks.cloud/flags/evaluate/" + App.APP_ID + "/";

    public static Boolean evaluateFlag(String flagId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Extract the access token from the Authorization header
            String authHeader = request.getHeader("Authorization");
            String accessToken = authHeader.substring("Bearer ".length());

            // Prepare the request
            JSONObject requestBody = new JSONObject().put("accessToken", accessToken);

            HttpRequest flagsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(FEATURE_FLAG_URL + flagId))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            // Send the request
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> flagResponse = client.send(flagsRequest, HttpResponse.BodyHandlers.ofString());
            // Extract flag status from response
            String flagResponseBody = flagResponse.body();
            JSONObject flag = new JSONObject(flagResponseBody);

            return flag.getBoolean("enabled");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
