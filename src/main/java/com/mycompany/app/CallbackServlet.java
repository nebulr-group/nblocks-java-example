package com.mycompany.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.json.JSONObject;
import org.jose4j.jwt.JwtClaims;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CallbackServlet extends HttpServlet {

    private static final String TOKEN_URL = "https://auth.nblocks.cloud/token/code/" + App.APP_ID;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        // Prepare the request
        JSONObject requestBody = new JSONObject().put("code", code);

        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {

            HttpResponse<String> tokens = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());

            String responseBody = tokens.body();
            JSONObject responseJson = new JSONObject(responseBody);
            String accessToken = responseJson.getString("access_token");

            // Verify the tokens
            JwtClaims jwtClaims = JwtFilter.verifyJwt(accessToken);

            System.out.println("Token verified and processed: " + jwtClaims);

            // Return token to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(accessToken);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidJwtException e) {
            e.printStackTrace();
        }
    }
}
