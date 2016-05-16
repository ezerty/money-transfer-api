package com.mycompany.moneytransfers.data.dao;

import com.mycompany.moneytransfers.MoneyTransferServer;
import com.mycompany.moneytransfers.data.DataSource;
import com.mycompany.moneytransfers.dto.Transfer;
import com.mycompany.moneytransfers.exceptions.InternalServerException;
import com.mycompany.moneytransfers.exceptions.InvalidConfigurationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransferDao {

    private final Logger logger = LogManager.getLogger(MoneyTransferServer.class);

    private static TransferDao instance = new TransferDao();

    private TransferDao() {
    }

    public static TransferDao getInstance() {
        return instance;
    }

    public void commitTransfer(Transfer transfer) throws InternalServerException {

        try (Connection connection = DataSource.getConnection();
                PreparedStatement sendStatement = connection.
                prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?");
                PreparedStatement receiveStatement = connection.
                prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?");
                PreparedStatement logStatement = connection.
                prepareStatement("INSERT INTO transfers (sender_id, recipient_id, amount) VALUES (?, ?, ?)");) {
            connection.setAutoCommit(false);

            sendStatement.setDouble(1, transfer.getAmount());
            sendStatement.setLong(2, transfer.getSenderId());
            sendStatement.executeUpdate();

            receiveStatement.setDouble(1, transfer.getAmount());
            receiveStatement.setLong(2, transfer.getRecipientId());
            receiveStatement.executeUpdate();

            logStatement.setLong(1, transfer.getSenderId());
            logStatement.setLong(2, transfer.getRecipientId());
            logStatement.setDouble(3, transfer.getAmount());
            logStatement.executeUpdate();

            connection.commit();

        } catch (SQLException | InvalidConfigurationException e) {
            logger.error(String.format("Failed to perform transfer %s", transfer), e);
            throw new InternalServerException();
        }
    }

}
