package com.mycompany.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Boolean useNewModel = FeatureFlag.evaluateFlag("new-model", request, response);
            if (useNewModel) {
                // Logic for new model
                response.getWriter().write("Running new model");
                return;
            } else {
                // Logic for old model
                response.getWriter().write("Running old model");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
