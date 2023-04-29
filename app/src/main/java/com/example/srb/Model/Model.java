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
    private Model()
    {
        reloadItemList();
    }

    public interface getAllItemsListener
    {
        void onComplete(List<Item> item_data);
    }

    MutableLiveData<List<Item>> itemListLd = new MutableLiveData<List<Item>>();
    public void reloadItemList()
    {
        //1.get local last update
        Long localLastUpdate = Item.getLocalLastUpdated();
        //2.get all cars record since local last update from firebase
        modelFireBase.getAllItems(localLastUpdate, new getAllItemsListener()
        {
            @Override
            public void onComplete(List<Item> item_data)
            {
                //3.update local last update date
                //4.add new records to the local db
                MyApplication.executorService.execute(()->
                {
                    Long lLastUpdate = new Long(0);
                    Log.d("TAG", "FB returned " + item_data.size());
                    for(Item i: item_data)
                    {
                        if(i.getLastUpdated() > lLastUpdate)
                        {
                            lLastUpdate=i.getLastUpdated();
                        }
                    }
                    Item.setLocalLastUpdated(lLastUpdate);
                    //5.return all records to the caller
                    List<Item> itemList = AppLocalDB.db.itemDao().getAllItems();
                    itemListLd.postValue(itemList);
                });
            }
        });
    }

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
                reloadItemList();
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

