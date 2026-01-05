package com.prannjjol.limit;

public class Transaction {
    public double amount;
    public String merchant;
    public long timestamp;

    public Transaction(double amount, String merchant, long timestamp) {
        this.amount = amount;
        this.merchant = merchant;
        this.timestamp = timestamp;
    }
}
