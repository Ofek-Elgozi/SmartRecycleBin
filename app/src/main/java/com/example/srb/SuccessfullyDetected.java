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
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_successfully_detected, container, false);
        item = SuccessfullyDetectedArgs.fromBundle(getArguments()).getItem();
        TextView itemType = view.findViewById(R.id.item_type);
        ImageView v_plastic = view.findViewById(R.id.Vplastic);
        ImageView v_glass = view.findViewById(R.id.Vglass);
        ImageView v_paper = view.findViewById(R.id.Vpaper);
        ImageView v_regular = view.findViewById(R.id.Vregular);
        itemType.setText(item.getType());
        if(item.getType().equals("Plastic"))
        {
            v_glass.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else if(item.getType().equals("Glass"))
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else if(item.getType().equals("Paper"))
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_glass.setVisibility(View.INVISIBLE);
            v_regular.setVisibility(View.INVISIBLE);
        }
        else if(item.getType().equals("Regular"))
        {
            v_plastic.setVisibility(View.INVISIBLE);
            v_glass.setVisibility(View.INVISIBLE);
            v_paper.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuHome:
                Navigation.findNavController(view).navigate(R.id.firstFragment);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}