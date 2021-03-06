package com.example.hackasoton2021recipe.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.BarcodeApi;
import com.example.hackasoton2021recipe.frontend.ui.entry.PopupActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.util.List;


public class BarcodeScanner extends Fragment {
    private CodeScanner mCodeScanner;
    private TextView txt;
    private Button manualEntry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        manualEntry = root.findViewById(R.id.manualEntry);

        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        txt = root.findViewById(R.id.ingredientScanned);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> strings = BarcodeApi.getInstance().barcodeConvertor(result.getText(),root.getContext());
                        String temp = "";
                        if(strings != null){
                            temp = strings.get(0);
                            for (int i = 0; i < strings.size();i++) {
                                temp = temp + ", " + strings.get(i);
                            }
                            txt.setText(temp);

                        } else{
                            RelativeLayout layout = (RelativeLayout) root.findViewById(R.id.rellayout);
                            txt.setText("");
                            Snackbar snackbar = Snackbar
                                    .make(layout, "Product Not Found. Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        manualEntry.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext().getApplicationContext(), PopupActivity.class));
            }
        }));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}