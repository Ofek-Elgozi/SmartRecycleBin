package com.example.srb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srb.Model.Item;


public class SuccessfullyDetected extends Fragment {
    Item item;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        view = inflater.inflate(R.layout.fragment_successfully_detected, container, false);
        item = SuccessfullyDetectedArgs.fromBundle(getArguments()).getItem();
        TextView itemType = view.findViewById(R.id.item_type);
        ImageView v_plastic = view.findViewById(R.id.Vplastic);
        ImageView v_glass = view.findViewById(R.id.Vglass);
        ImageView v_paper = view.findViewById(R.id.Vpaper);
        ImageView v_regular = view.findViewById(R.id.Vregular);
        itemType.setText(item.getType());
        if(item.getType() == "Plastic")
        {
            v_glass.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else if(item.getType() == "Glass")
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else if(item.getType() == "Paper")
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_glass.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else // Regular
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_glass.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}