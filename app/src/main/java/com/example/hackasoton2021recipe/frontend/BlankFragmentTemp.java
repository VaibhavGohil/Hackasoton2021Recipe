package com.example.hackasoton2021recipe.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hackasoton2021recipe.R;

import java.util.ArrayList;


public class BlankFragmentTemp extends Fragment {


    private ListView listView;
    private ArrayAdapter<String> stringArrayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_blank_temp, container, false);
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i <4; i++){
            temp.add("number : " + i);
        }
        listView = root.findViewById(R.id.ItemList1);
        stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,temp);
        listView.setAdapter(stringArrayAdapter);
        return root;
    }
}