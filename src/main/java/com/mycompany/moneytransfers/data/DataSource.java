package com.mycompany.moneytransfers.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mycompany.moneytransfers.exceptions.InvalidConfigurationException;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSource {

    private static ComboPooledDataSource dataSource;

    static {
        try {
            init();
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void init() throws InvalidConfigurationException {
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.hsqldb.jdbcDriver");
        } catch (PropertyVetoException e) {
            throw new InvalidConfigurationException("Failed to set db driver", e);
        }
        dataSource.setJdbcUrl("jdbc:hsqldb:mem:money-transfer");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(5);
        dataSource.setMaxPoolSize(20);

        try (Connection connection = dataSource.getConnection();
                PreparedStatement createAccounts = connection.
                prepareStatement("CREATE TABLE accounts (id BIGINT, balance DOUBLE);");
                PreparedStatement createTransfers = connection.
                prepareStatement("CREATE TABLE transfers (sender_id BIGINT, recipient_id BIGINT, amount DOUBLE);")) {
            createAccounts.execute();
            createTransfers.execute();
            connection.prepareStatement("INSERT INTO accounts (id, balance) VALUES(1, 1200);").execute();
            connection.prepareStatement("INSERT INTO accounts (id, balance) VALUES(2, 2100);").execute();
            connection.prepareStatement("INSERT INTO accounts (id, balance) VALUES(3, 3540);").execute();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init database", e);
        }
    }

    public static Connection getConnection() throws SQLException, InvalidConfigurationException {
        return dataSource.getConnection();
    }

}
