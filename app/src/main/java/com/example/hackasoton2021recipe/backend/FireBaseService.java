package com.example.hackasoton2021recipe.backend;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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



    public void readData(){
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Details").collection("Logs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            DiaryLog temp = new DiaryLog();
                            temp.path = d.getReference().getId();
                            temp.date = d.get("date").toString();
                            temp.inngredients = d.get("ingred").toString();
                            temp.rating = d.get("rating").toString();
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

    public void clear(){
        qds = new ArrayList<DiaryLog>();
    }


    public void sendLog(){
        Map<String, Object> user = new HashMap<>();
        Random random = new Random();
        user.put("date",  (random.nextInt(30) + 1) + "/06/2021");
        user.put("ingred", "temp");
        user.put("rating", random.nextInt(5) + 10);

        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Details").collection("Logs")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(null, "DocumentSnapshot added with ID: " + documentReference.getId());
                        FireBaseService.getInstance().refresh();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(null, "Error adding document", e);
                    }
                });
    }

    public void deleteLog(String path){
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Details").collection("Logs").document(path).delete();
        this.refresh();
    }



    public void refresh() {
        this.clear();
        this.readData();
    }
}

