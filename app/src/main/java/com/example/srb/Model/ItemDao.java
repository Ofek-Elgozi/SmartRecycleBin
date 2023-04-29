package com.example.srb.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao
{
    @Query("select * from Item")
    List<Item> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Item... items);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM Item WHERE id=:id_key")
    LiveData<List<Item>> getItemsByID(String id_key);
}
