package com.example.hackasoton2021recipe.backend;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseService extends Application {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<DiaryLog> qds = new ArrayList<>();
    private boolean loaded = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static FireBaseService   fbs;
    private FireBaseService(){

    }
    public static FireBaseService getInstance()
    {
        if (fbs == null)
        {
            fbs = new FireBaseService();
        }
        return fbs;
    }

    public void addData(){
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", "1815");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(null, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(null, "Error adding document", e);
                    }
                });
    }


    public void readData(){
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            DiaryLog temp = new DiaryLog();
                            temp.first = d.get("first").toString();
                            temp.last = d.get("last").toString();
                            temp.born = d.get("born").toString();
                            qds.add(temp);
                            System.out.println(qds.size());
                        }
                    }
                });
        loaded = true;
        System.out.println(loaded + " "+ qds.size());
    }

    public boolean getLoaded(){
        return loaded;
    }

    public  ArrayList<DiaryLog> getData(){
        return qds;
    }

}

