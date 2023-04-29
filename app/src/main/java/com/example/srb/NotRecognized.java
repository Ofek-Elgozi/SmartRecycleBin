package com.example.srb;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srb.Model.Item;
import com.example.srb.Model.Model;


public class NotRecognized extends Fragment {

    String temp;
    TextView t;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_not_recognized, container, false);
        temp = NotRecognizedArgs.fromBundle(getArguments()).getTempNumber();
        Toast.makeText(getActivity(), "Barcode:" + temp, Toast.LENGTH_LONG).show();
        ImageButton No = view.findViewById(R.id.No_ReHome);
        No.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setType("Regular");
                item.setId(temp);
                item.setName("Unknown");
                Model.instance.addItem(item, new Model.addItemListener() {
                    @Override
                    public void onComplete() {
                        NotRecognizedDirections.ActionNotRecognizedToSuccessfullyDetected action = NotRecognizedDirections.actionNotRecognizedToSuccessfullyDetected(item);
                        Navigation.findNavController(view).navigate(action);
                    }
                });
            }
        });
        ImageButton Yes = view.findViewById(R.id.YesEntDeat);
        Yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NotRecognizedDirections.ActionNotRecognizedToAddItem action = NotRecognizedDirections.actionNotRecognizedToAddItem(temp);
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }
}