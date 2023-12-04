package com.mycompany.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class LoginServlet extends HttpServlet {
    private static final String LOGIN_URL = "https://auth.nblocks.cloud/url/login/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(LOGIN_URL + App.APP_ID);
    }
}