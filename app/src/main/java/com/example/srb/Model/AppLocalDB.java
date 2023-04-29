package com.example.srb.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.srb.MyApplication;
import com.example.srb.Model.Item;

@Database(entities = {Item.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase
{
    public abstract ItemDao itemDao();
}

public class AppLocalDB
{
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
    private AppLocalDB(){}
}
