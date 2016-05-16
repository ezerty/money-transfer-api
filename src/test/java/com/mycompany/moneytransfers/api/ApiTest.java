package com.mycompany.moneytransfers.api;

import com.mycompany.moneytransfers.MoneyTransferServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.BeforeClass;

public class ApiTest {
    
    protected static JerseyClient jerseyClient;

    protected static final String TARGET = "http://localhost:8099/api";

    @BeforeClass
    public static void init() throws Exception {
        JerseyClientBuilder clientBuilder = new JerseyClientBuilder();

        jerseyClient = clientBuilder.withConfig(new ClientConfig()).build();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    MoneyTransferServer.main(new String[]{"8099"});
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}
