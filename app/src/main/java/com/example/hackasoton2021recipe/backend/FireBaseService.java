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
import java.util.TreeMap;

public class FireBaseService extends Application {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<DiaryLog> dlogs = new ArrayList<>();
    private List<String> ingredients = new ArrayList<>();
    private boolean loaded = false;
    private TreeMap<String, Integer> occurrences = new TreeMap<>();

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

    public TreeMap<String, Integer> getOccurrences() {
        return occurrences;
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
                            temp.ingredients = (List<String>) d.get("ingred");
                            temp.rating = d.get("rating").toString();
                            dlogs.add(temp);
                            System.out.println(dlogs.size());
                            if (temp.rating.equals("Yes")) {
                                for (String ingredient: temp.ingredients) {
                                    if (occurrences.containsKey(ingredient)) {
                                        Integer value = occurrences.get(ingredient);
                                        occurrences.replace(ingredient, value, value + 1);
                                    } else {
                                        occurrences.put(ingredient, 1);
                                    }
                                }
                            }
                        }
                    }
                });
        loaded = true;
        System.out.println(loaded + " "+ dlogs.size());
    }

    public boolean getLoaded(){
        return loaded;
    }

    public  List<DiaryLog> getData(){
        return dlogs;
    }

    public void clear(){
        dlogs = new ArrayList<DiaryLog>();
    }


    public void sendLog(String date, ArrayList<String> ingredients, String rating){
        Map<String, Object> log = new HashMap<>();
        Random random = new Random();
        log.put("date",  (random.nextInt(30) + 1) + "/06/2021");
        String temp = String.valueOf(random.nextInt(20));
        if(!this.ingredients.contains(temp)){
            this.ingredients.add(temp);
            sendNewIngred(null);
        }
        ArrayList<String> temp2 = new ArrayList<>();
        temp2.add(temp);
        log.put("ingred", temp2);
        log.put("rating", random.nextInt(5) + 10);

        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Details").collection("Logs")
                .add(log)
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

    public void sendNewIngred(ArrayList<String> newIngreds){
        Map<String, Object> ingred = new HashMap<>();
        ingred.put("ingreds", ingredients);


        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Ingred").update(ingred);
        System.out.println("Updated");
    }

    public void checkIngredDoc(){
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean exists = false;
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            if(d.getBoolean("exists")){
                                exists = true;
                                ingredients = (ArrayList<String>) d.get("ingreds");
                                break;
                            }
                        }
                        if(!exists){
                            createIngred();
                        }
                    }
                });
        System.out.println("Ingred: " + ingredients.size());
    }

    public void createIngred(){
        Map<String, Object> ingred = new HashMap<>();
        ArrayList<String> temp = new ArrayList<>();
        ingred.put("exists",true);
        ingred.put("ingreds", temp);


        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                .add(ingred)
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

