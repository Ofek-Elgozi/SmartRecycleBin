package com.example.srb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NotRecognized extends Fragment {

    String temp;
    TextView t;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_not_recognized, container, false);
//        temp = NotRecognizedArgs.fromBundle(getArguments()).getTempNumber();
//        t = view.findViewById(R.id.textView11);
//        t.setText(temp);
        return view;
    }
}