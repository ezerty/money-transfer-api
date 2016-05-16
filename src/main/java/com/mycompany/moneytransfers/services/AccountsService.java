package com.mycompany.moneytransfers.services;

import com.mycompany.moneytransfers.data.dao.AccountsDao;
import com.mycompany.moneytransfers.dto.AccountDetails;
import com.mycompany.moneytransfers.exceptions.InternalServerException;

public class AccountsService {

    private static AccountsService instance = new AccountsService();

    private AccountsDao accountsDao;

    private AccountsService() {
        accountsDao = AccountsDao.getInstance();
    }

    public static AccountsService getInstance() {
        return instance;
    }

    public AccountDetails getAccountDetails(long id) throws InternalServerException {
        return accountsDao.getAccountDetails(id);
    }
}
