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
    private List<String> productNames = new ArrayList<>();
    private boolean loaded = false;
    private  ArrayList<OccurancePercentage> result = new ArrayList<>();
    private TreeMap<String, Integer> occurrences = new TreeMap<>();
    private TreeMap<String, Integer> nonOccurrences = new TreeMap<>();

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

    public TreeMap<String, Integer> getNonOccurrences() {
        return nonOccurrences;
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
                            temp.productName = (List<String>) d.get("productNames");
                            temp.rating = d.get("rating").toString();
                            dlogs.add(temp);
                            System.out.println(dlogs.size());
                            if (temp.rating.equals("Yes")) {
                                for (String ingredient: temp.ingredients) {
                                    ingredient.toLowerCase();
                                    if (occurrences.containsKey(ingredient)) {
                                        Integer value = occurrences.get(ingredient);
                                        occurrences.replace(ingredient, value, value + 1);
                                    } else {
                                        occurrences.put(ingredient, 1);
                                    }
                                }
                                for (String product: productNames) {
                                    product.toLowerCase();
                                    if (occurrences.containsKey(product)) {
                                        Integer value = occurrences.get(product);
                                        occurrences.replace(product, value, value + 1);
                                    } else {
                                        occurrences.put(product, 1);
                                    }
                                }
                            } else {
                                for (String ingredient: temp.ingredients) {
                                    ingredient.toLowerCase();
                                    if (nonOccurrences.containsKey(ingredient)) {
                                        Integer value = nonOccurrences.get(ingredient);
                                        nonOccurrences.replace(ingredient, value, value + 1);
                                    } else {
                                        nonOccurrences.put(ingredient, 1);
                                    }
                                }
                                for (String product: productNames) {
                                    product.toLowerCase();
                                    if (nonOccurrences.containsKey(product)) {
                                        Integer value = nonOccurrences.get(product);
                                        nonOccurrences.replace(product, value, value + 1);
                                    } else {
                                        nonOccurrences.put(product, 1);
                                    }
                                }
                            }
                        }
                        getIngredientPercentages();
                    }
                });
        loaded = true;
    }

    public boolean getLoaded(){
        return loaded;
    }

    public  List<DiaryLog> getData(){
        return dlogs;
    }
    public ArrayList<OccurancePercentage> getPercentages(){ return this.result;}

    public void clear(){
        dlogs = new ArrayList<DiaryLog>();
    }


    public void sendLog(String date, ArrayList<String> ingredients,  ArrayList<String> productNames, String rating){
        Map<String, Object> log = new HashMap<>();
        Random random = new Random();
        ArrayList<String> tempIngred = new ArrayList<>();
        ArrayList<String> tempProds = new ArrayList<>();
        log.put("date",  (random.nextInt(30) + 1) + "/06/2021");
        for (String s: ingredients) {
            if(!this.ingredients.contains(s)){
                this.ingredients.add(s);
            }
            tempIngred.add(s);
        }
        sendNewIngred(this.ingredients);
        for (String s: productNames) {
            if(!this.productNames.contains(s)){
                this.productNames.add(s);
            }
            tempProds.add(s);
        }
        sendNewProductNames(this.productNames);

        log.put("ingred", tempIngred);
        log.put("productNames", tempProds);
        log.put("rating", (random.nextInt(5) > 2)? "Yes" : "No");

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

    public void sendNewIngred(List<String> newIngreds){
        Map<String, Object> ingred = new HashMap<>();
        ingred.put("ingreds", ingredients);


        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("ingred").update(ingred);
        System.out.println("Updated");
    }

    public void sendNewProductNames(List<String> newProductNames){
        Map<String, Object> newNames = new HashMap<>();
        newNames.put("productNames", productNames);


        // Add a new document with a generated ID
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("productNames").update(newNames);
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



    public void deleteLog(String path,int index){
        this.dlogs.remove(index);
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).document("Details").collection("Logs").document(path).delete();
        this.refresh();
    }



    public void refresh() {
        this.clear();
        this.readData();
    }

    //calculate percentages of reaction occurrences in relation to number of times an ingredient is consumed
    public void getIngredientPercentages(){
        Log.d(null,"NULL OR NOT O" + occurrences);
        Log.d(null,"NULL OR NOT " + nonOccurrences);
        for (Map.Entry<String,Integer> ingredient : occurrences.entrySet()){
            Float occurrence = Float.valueOf(ingredient.getValue());
            Float nonOccurrence = (nonOccurrences == null) ? Float.valueOf(nonOccurrences.get(ingredient.getKey())) : 0;
            Integer percent = Math.round((occurrence / (occurrence + nonOccurrence) * 100));
            OccurancePercentage temp = new OccurancePercentage();
            temp.occuranceName = ingredient.getKey();
            temp.percentage = percent;
            result.add(temp);
        }
    }
}


