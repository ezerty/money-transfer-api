package com.mycompany.moneytransfers.services;

import com.mycompany.moneytransfers.data.dao.AccountsDao;
import com.mycompany.moneytransfers.data.dao.TransferDao;
import com.mycompany.moneytransfers.dto.AccountDetails;
import com.mycompany.moneytransfers.dto.Transfer;
import com.mycompany.moneytransfers.exceptions.InternalServerException;
import com.mycompany.moneytransfers.exceptions.RequestException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferService {

    private static TransferService instance = new TransferService();

    private TransferDao transferDao;

    private AccountsDao accountsDao;

    private Map<Long, Lock> senderLocks = new ConcurrentHashMap<>();

    private TransferService() {
        transferDao = TransferDao.getInstance();
        accountsDao = AccountsDao.getInstance();
    }

    public static TransferService getInstance() {
        return instance;
    }

    public void sendTransfer(Transfer transfer) throws InternalServerException, RequestException {
        Lock lock = senderLocks.get(transfer.getSenderId());
        if (lock == null) {
            lock = new ReentrantLock();
            Lock actualLock = senderLocks.putIfAbsent(transfer.getSenderId(), lock);
            if (actualLock != null) {
                lock = actualLock;
            }
        }

        lock.lock();
        try {

            AccountDetails sender = accountsDao.getAccountDetails(transfer.getSenderId());
            if (sender == null) {
                throw new RequestException("Sender account was not found");
            } else if (sender.getBalance() < transfer.getAmount()) {
                throw new RequestException("Sender balance is too low");
            } else {
                AccountDetails recipient = accountsDao.getAccountDetails(transfer.getRecipientId());
                if (recipient == null) {
                    throw new RequestException("Recipient account was not found");
                }
                transferDao.commitTransfer(transfer);
            }
        
        } finally {
            lock.unlock();
        }

    }
}
