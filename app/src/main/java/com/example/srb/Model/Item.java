package com.example.srb.Model;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.srb.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Item implements Parcelable
{
    public final static String LAST_UPDATED="LAST_UPDATED";
    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public String type;
    Long lastUpdated= new Long(0);

    public Item(com.example.srb.Model.Item i)
    {
        this.id= i.id;
        this.name =i.name;
        this.type =i.type;
    }

    public Item(String name, String type, String id)
    {
        this.id= id;
        this.name =name;
        this.type =type;
    }

    public Item()
    {
        id = " ";
        name =" ";
        type =" ";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Map<String,Object> toJson()
    {
        Map<String, Object> json = new HashMap<>();
        json.put("id", getId());
        json.put("name", getName());
        json.put("type", getType());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    static com.example.srb.Model.Item fromJson(Map<String, Object> json)
    {
        String id=(String)json.get("id");
        String name=(String)json.get("name");
        String type=(String)json.get("type");
        com.example.srb.Model.Item item = new com.example.srb.Model.Item(name,type,id);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        item.setLastUpdated(new Long(ts.getSeconds()));
        return item;
    }

    static long getLocalLastUpdated()
    {
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("CARS_LAST_UPDATE", 0 );
        Log.d("TAG","localLastUpdate: " + localLastUpdate);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date)
    {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("CARS_LAST_UPDATE",date);
        editor.commit();
        Log.d("TAG", "new lud" + date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(type);
        if (lastUpdated == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastUpdated);
        }
    }

    protected Item(Parcel in)
    {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            lastUpdated = null;
        } else {
            lastUpdated = in.readLong();
        }
    }

    public static final Creator<com.example.srb.Model.Item> CREATOR = new Creator<com.example.srb.Model.Item>()
    {
        @Override
        public com.example.srb.Model.Item createFromParcel(Parcel in)
        {
            return new com.example.srb.Model.Item(in);
        }

        @Override
        public com.example.srb.Model.Item[] newArray(int size)
        {
            return new com.example.srb.Model.Item[size];
        }
    };
}
