package com.example.srb.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.LinkedList;

public class ModelFireBase
{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void getAllItems(Long since, Model.getAllItemsListener listener)
    {
        db.collection("items").whereGreaterThanOrEqualTo(Item.LAST_UPDATED, new Timestamp(since, 0)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                LinkedList<Item> carList = new LinkedList<Item>();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc: task.getResult())
                    {
                        Item i = Item.fromJson(doc.getData());
                        i.setId(doc.getId());
                        if(i!=null)
                            carList.add(i);
                    }
                }
                listener.onComplete(carList);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.onComplete(null);
            }
        });
    }

    public void addItem(Item item, Model.addItemListener listener)
    {
        if(item.getId()==null)
        {
            db.collection("items")
                    .document().set(item.toJson())
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(@NonNull Void unused)
                        {
                            listener.onComplete();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.d("TAG", e.getMessage());
                        }
                    });
        }
        else
        {
            db.collection("items")
                    .document(item.getId()).set(item.toJson())
                    .addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(@NonNull Void unused)
                        {
                            listener.onComplete();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.d("TAG", e.getMessage());
                        }
                    });
        }
    }

    public void getItemByID(String id, Model.getItemByIDListener listener)
    {
        DocumentReference docRef = db.collection("items").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Item u = Item.fromJson(document.getData());
                        if(u!=null)
                            listener.onComplete(u);
                    }
                    else
                    {
                        listener.onComplete(null);
                    }
                }
                else
                {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }
}
