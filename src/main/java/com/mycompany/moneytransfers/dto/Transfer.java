package com.mycompany.moneytransfers.dto;

public class Transfer {

    private long senderId;
    private long recipientId;
    private double amount;

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" + "senderId=" + senderId + ", recipientId=" + recipientId + ", amount=" + amount + '}';
    }

}
