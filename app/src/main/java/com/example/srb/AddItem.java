package com.example.srb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.srb.Model.Item;
import com.example.srb.Model.Model;


public class AddItem extends Fragment {
    View view;
    String tempid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_add_item, container, false);
        tempid = AddItemArgs.fromBundle(getArguments()).getTempNumber();
        Toast.makeText(getActivity(), "Barcode:" +  tempid, Toast.LENGTH_LONG).show();
        EditText tname = view.findViewById(R.id.item_name);
        Item item = new Item();
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton plastic = view.findViewById(R.id.radio_plastic);
        RadioButton paper = view.findViewById(R.id.radio_paper);
        RadioButton glass = view.findViewById(R.id.radio_glass);
        RadioButton regular = view.findViewById(R.id.radio_regular);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Disable the other radio buttons when one is selected
                if (checkedId == R.id.radio_plastic && plastic.isChecked()) {
                    item.setType("Plastic");
                    paper.setEnabled(false);
                    glass.setEnabled(false);
                    regular.setEnabled(false);
                } else if (checkedId == R.id.radio_paper && paper.isChecked())
                {
                    item.setType("Paper");
                    plastic.setEnabled(false);
                    glass.setEnabled(false);
                    regular.setEnabled(false);
                } else if (checkedId == R.id.radio_glass && glass.isChecked())
                {
                    item.setType("Glass");
                    paper.setEnabled(false);
                    plastic.setEnabled(false);
                    regular.setEnabled(false);
                } else if (checkedId == R.id.radio_regular && regular.isChecked())
                {
                    item.setType("Regular");
                    paper.setEnabled(false);
                    glass.setEnabled(false);
                    plastic.setEnabled(false);
                }
            }
        });
        ImageButton send = view.findViewById(R.id.send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setId(tempid);
                item.setName(tname.getText().toString());
                if(item.getType() != " " && item.getName() != " ")
                {
                    Model.instance.addItem(item, new Model.addItemListener() {
                        @Override
                        public void onComplete() {
                            AddItemDirections.ActionAddItemToSuccessfullyDetected action = AddItemDirections.actionAddItemToSuccessfullyDetected(item);
                            Navigation.findNavController(v).navigate(action);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "Please make sure you chose Type and entered Name.", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.back_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_back:
                Navigation.findNavController(view).popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}

