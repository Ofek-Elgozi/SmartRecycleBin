package com.example.srb.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.srb.MyApplication;

import java.util.List;

public class Model
{
    public final static Model instance = new Model();
    ModelFireBase modelFireBase = new ModelFireBase();

    public interface getAllItemsListener
    {
        void onComplete(List<Item> item_data);
    }

    MutableLiveData<List<Item>> itemListLd = new MutableLiveData<List<Item>>();

    public MutableLiveData<List<Item>> getAllItemsData()
    {
        return itemListLd;
    }


    public interface addItemListener
    {
        void onComplete();
    }

    public void addItem(Item item, addItemListener listener)
    {
        modelFireBase.addItem(item, new addItemListener()
        {
            @Override
            public void onComplete()
            {
                listener.onComplete();
            }
        });
    }

    public interface getItemByIDListener
    {
        void onComplete(Item item);
    }

    public void getItemByID(String id, getItemByIDListener listener)
    {
        modelFireBase.getItemByID(id,listener);
    }
}

