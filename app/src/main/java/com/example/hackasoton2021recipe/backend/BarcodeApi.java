package com.example.hackasoton2021recipe.backend;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class BarcodeApi {

    private List<String> jsonResponses = new ArrayList<>();//check this to get fetched data
    //Countdown latch to stop threads messing with eachother (this is probs dumb but makes it work)
    //when using this class pass a coundownlatch of 1, have a while loop which checks value of latch and then do a loading screen in while loop
    //once data is fetched the latch will set to 0

    //Function which returns a list of ingredients from a products barcode
    public void getIngredientsFromBarcode(String barcode, Context c, CountDownLatch latch){
        jsonResponses.clear();

        String url = "https://uk.openfoodfacts.org/api/v0/product/3017620422003.json?fields=ingredients_hierarchy";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "HackaSoton - Android - Version 11.0")
                .addHeader("accept", "application/json")
                .build();


        new Thread(
                ()->{
                    try {
                        client.setReadTimeout(1, TimeUnit.MILLISECONDS);
                        client.setRetryOnConnectionFailure(false);
                        String temp = client.newCall(request).execute().body().string();
                        System.out.println("_____________________________________");
                        System.out.println(temp);
                        JSONObject response = new JSONObject(temp);
                        System.out.println(response.toString());
                        JSONArray jsonArray = response.getJSONObject("product").getJSONArray("ingredients_hierarchy");
                        for(int i = 0; i < jsonArray.length(); i++){
                            String ingredient = jsonArray.getString(i);
                            ingredient = ingredient.substring(3);
                            System.out.println("_____________________________________");
                            System.out.println(ingredient);
                            jsonResponses.add(ingredient);
                        }
                        latch.countDown();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        jsonResponses = null;
                        latch.countDown();

                    }
                }).start();

        /* JUST IN CASE
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "HackaSoton - Android - Version 11.0");
            connection.setRequestProperty("accept", "application/json");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            readstream(in);


            System.out.println(response.body());
        }
        catch (Exception e){
            connection.disconnect();
            return null;
        }
        */
    }

    public List<String> getJsonResponses() {
        return jsonResponses;
    }

}
