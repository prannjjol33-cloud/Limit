package com.prannjjol.limit;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {

    // Insert one transaction
    @Insert
    void insert(Transaction transaction);

    // Delete one transaction
    @Delete
    void delete(Transaction transaction);

    // Get all transactions (latest first)
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    List<Transaction> getAllTransactions();
}
