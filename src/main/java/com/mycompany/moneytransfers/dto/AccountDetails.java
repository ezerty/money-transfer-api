package com.mycompany.moneytransfers.dto;

public class AccountDetails {

    long id;
    double balance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDetails{" + "id=" + id + ", balance=" + balance + '}';
    }

}
