package com.mycompany.moneytransfers.data.dao;

import com.mycompany.moneytransfers.MoneyTransferServer;
import com.mycompany.moneytransfers.data.DataSource;
import com.mycompany.moneytransfers.dto.AccountDetails;
import com.mycompany.moneytransfers.exceptions.InternalServerException;
import com.mycompany.moneytransfers.exceptions.InvalidConfigurationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountsDao {

    private final Logger logger = LogManager.getLogger(MoneyTransferServer.class);

    private static AccountsDao instance = new AccountsDao();

    private AccountsDao() {
    }

    public static AccountsDao getInstance() {
        return instance;
    }

    public AccountDetails getAccountDetails(long id) throws InternalServerException {
        try (
                Connection connection = DataSource.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE id = ?")) {
            selectStatement.setLong(1, id);
            ResultSet result = selectStatement.executeQuery();
            if (!result.next()) {
                return null;
            }
            double amount = result.getDouble(1);
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setId(id);
            accountDetails.setBalance(amount);
            result.close();
            return accountDetails;
        } catch (SQLException | InvalidConfigurationException e) {
            logger.error(String.format("Failed to get account details with id: %s", id), e);
            throw new InternalServerException();
        }
    }
}
