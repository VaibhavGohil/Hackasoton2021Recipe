package com.example.hackasoton2021recipe.frontend.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.BarcodeApi;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView txt = root.findViewById(R.id.barcodecheck);
        txt.setText(BarcodeApi.getInstance().getJsonResponses().get(0));
        return root;
    }
}