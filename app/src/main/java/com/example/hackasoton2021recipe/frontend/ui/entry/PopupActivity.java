package com.example.hackasoton2021recipe.frontend.ui.entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.FireBaseService;

import java.util.ArrayList;
import java.util.Arrays;

public class PopupActivity extends AppCompatActivity {

    private Button submit;
    private EditText product;
    private EditText ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        submit = findViewById(R.id.submit);
        product = findViewById(R.id.product);
        ingredients = findViewById(R.id.ingredients);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width *.9),(int)(height *.9));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y= -20;

        getWindow().setAttributes(params);
        submit.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> products = new ArrayList<>();
                products.add(product.getText().toString());
                String[] tempingred = ingredients.getText().toString().split(";");
                ArrayList<String> ingredientArray = new ArrayList<>();
                for (int i = 0; i < tempingred.length;i++){
                    ingredientArray.add(tempingred[i]);
                }
                System.out.println("Button pressed: " + products.size() + " " + ingredientArray.size());

                FireBaseService.getInstance().sendLog(null, ingredientArray,products, null);
                finish();
            }
        }));

    }
}