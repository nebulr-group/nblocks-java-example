package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PaymentServlet extends HttpServlet {
    private static final String HANDOVER_URL = "https://auth.nblocks.cloud/handover/code/" + App.APP_ID;
    private static final String PAYMENT_URL = "https://backendless.nblocks.cloud/subscription-portal/selectPlan?code=";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Extract the access token from the Authorization header
            String authHeader = request.getHeader("Authorization");
            String accessToken = authHeader.substring("Bearer ".length());

            // Prepare the request
            JSONObject requestBody = new JSONObject().put("accessToken", accessToken);

            HttpRequest flagsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(HANDOVER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            // Send the request
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> handoverResponse = client.send(flagsRequest, HttpResponse.BodyHandlers.ofString());
            // Extract handover code from response
            JSONObject handover = new JSONObject(handoverResponse.body());
            String handoverCode = handover.getString("code");
            
            if (handoverCode == null) {
                throw new Exception("Handover code is null");
            }
            
            response.sendRedirect(PAYMENT_URL + handoverCode);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
