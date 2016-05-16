package com.mycompany.moneytransfers;

import com.mycompany.moneytransfers.data.DataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MoneyTransferServer {

    private static final Logger logger = LogManager.getLogger(MoneyTransferServer.class);

    public static void main(String[] args) throws Exception {
        startServer(Integer.parseInt(args[0]));
    }

    public static void startServer(int port) throws Exception {
        DataStorage.initStorage();

        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        initServlets(context);

        final Server server = new Server(port);
        server.setHandler(context);

        server.start();
        server.join();
        
    }

    private static void initServlets(ServletContextHandler context) {
        addServletGroup(context, "/api/*", "com.mycompany.moneytransfers.api");
    }

    private static ServletHolder addServletGroup(ServletContextHandler context, String basePath, String packageName) {
        ServletHolder servlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, basePath);
        servlet.setInitOrder(0);
        servlet.setInitParameter(
                "jersey.config.server.provider.packages",
                packageName);
        return servlet;
    }

}
