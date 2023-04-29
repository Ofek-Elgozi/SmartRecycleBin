package com.example.srb;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.srb.Model.Item;
import com.example.srb.Model.Model;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;

public class FirstFragment extends Fragment {

    private static final int REQUEST_CODE_SCAN = 1;
    private ImageButton scanBtn;
    private String tempnumber;
    View view;
    Item Item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        scanBtn = view.findViewById(R.id.scan_btn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[] { Manifest.permission.CAMERA },
                            REQUEST_CODE_SCAN);
                } else {
                    startScanActivity();
                }
            }
        });

        return view;
    }

    private void startScanActivity() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        intent.putExtra(Intents.Scan.MODE, Intents.Scan.ONE_D_MODE);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanActivity();
            } else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                BarcodeFormat format = BarcodeFormat.valueOf(data.getStringExtra(Intents.Scan.RESULT_FORMAT));
                String contents = data.getStringExtra(Intents.Scan.RESULT);
                tempnumber = contents;
                Model.instance.getItemByID(tempnumber, new Model.getItemByIDListener() {
                    @Override
                    public void onComplete(Item item) {
                        if(item == null)
                        {
                            FirstFragmentDirections.ActionFirstFragmentToNotRecognized action = FirstFragmentDirections.actionFirstFragmentToNotRecognized(tempnumber);
                            Navigation.findNavController(view).navigate(action);
                        }
                        else
                        {
                            FirstFragmentDirections.ActionFirstFragmentToSuccessfullyDetected action = FirstFragmentDirections.actionFirstFragmentToSuccessfullyDetected(item);
                            Navigation.findNavController(view).navigate(action);
                        }
                    }
                });
                // Do something with the barcode data
            }
        }
    }

}
