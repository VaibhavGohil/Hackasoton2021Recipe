package com.example.hackasoton2021recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hackasoton2021recipe.backend.FireBaseService;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBaseService fbs = new FireBaseService();
       // fbs.addData();

        TextView txt = (TextView) findViewById(R.id.helloworld);
        fbs.readData();
        txt.setText("fbs.readData()");
    }
}