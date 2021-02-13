package com.example.hackasoton2021recipe.frontend.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hackasoton2021recipe.MainActivity2;
import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.BarcodeApi;
import com.example.hackasoton2021recipe.backend.FireBaseService;
import com.example.hackasoton2021recipe.frontend.LoginPage;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Button logout;
    private Button add;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        logout = root.findViewById(R.id.logoutbtn);
        add = root.findViewById(R.id.tempadd);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(root.getContext(), LoginPage.class);
                startActivity(intent);
                FireBaseService.getInstance().clear();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> jsonArray = BarcodeApi.getInstance().getJsonResponses();
                if(jsonArray != null){
                    ArrayList<String> product = new ArrayList<>();
                    product.add(jsonArray.get(0));
                    ArrayList<String> ingred = new ArrayList<>();
                    for (int i = 1; i<jsonArray.size();i++){
                        ingred.add(jsonArray.get(i));
                    }
                    FireBaseService.getInstance().sendLog(null,ingred,product,null);
                } else {
                    txt.setText("");
                    Snackbar snackbar = Snackbar
                            .make(v, "Sorry Product Not!!! Please Try Again!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        return root;
    }
}