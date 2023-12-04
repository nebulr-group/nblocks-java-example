package com.mycompany.app;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {

    public static final String APP_ID = "XXX"; // Replace with your APP ID

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new CallbackServlet()), "/auth/oauth-callback");
        context.addServlet(new ServletHolder(new PaymentServlet()), "/payment");
        context.addServlet(new ServletHolder(new ModelServlet()), "/model");
        context.addServlet(new ServletHolder(new DashboardDataServlet()), "/protected/dashboardData");
        context.addServlet(new ServletHolder(new AnalyticsDataServlet()), "/protected/analyticsData");

        // Process all incoming requests with JwtFilter to verify and process the JWT
        context.addFilter(new FilterHolder(new JwtFilter()), "/*", EnumSet.of(DispatcherType.REQUEST));

        // Restrict all protected endpoints globally with AccessControlFilter to require authenticated users
        context.addFilter(new FilterHolder(new AccessControlFilter(null, List.of("AUTHENTICATED"))),
                "/protected/*", EnumSet.of(DispatcherType.REQUEST));

        // Restrict /analyticsData with AccessControlFilter to require users with ANALYTICS_READ privilege
        context.addFilter(new FilterHolder(new AccessControlFilter(null, List.of("ANALYTICS_READ"))),
                "/protected/analyticsData/*", EnumSet.of(DispatcherType.REQUEST));

        server.start();
        server.join();
    }
}