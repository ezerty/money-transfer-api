package com.mycompany.moneytransfers.data;

import org.hsqldb.Server;

public class DataStorage {

    private static DataStorage dataStorage;

    private DataStorage() {
        Server server = new Server();
        server.setDatabaseName(0, "money-transfer");
        server.setDatabasePath(0, "mem:money-transfer-db");
        server.start();
    }

    public synchronized static void initStorage() {
        if (dataStorage == null) {
            dataStorage = new DataStorage();
        }
    }

}
