package com.prannjjol.limit;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public double amount;
    public String merchant;
    public long timestamp;

    public Transaction(double amount, String merchant, long timestamp) {
        this.amount = amount;
        this.merchant = merchant;
        this.timestamp = timestamp;
    }
}
