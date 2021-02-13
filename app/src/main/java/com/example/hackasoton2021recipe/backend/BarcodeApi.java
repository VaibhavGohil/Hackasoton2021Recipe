package com.example.hackasoton2021recipe.backend;

import android.app.Application;
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


public class BarcodeApi extends Application

{

    private boolean loaded = false;
    private static BarcodeApi bca;
    private BarcodeApi(){

    }
    public static BarcodeApi getInstance()
    {
        if (bca == null)
        {
            bca = new BarcodeApi();
        }
        return bca;
    }

    public List<String> barcodeConvertor(String barcode, Context c){
        CountDownLatch latch = new CountDownLatch(1);
        BarcodeApi.getInstance().getIngredientsFromBarcode(barcode,c,latch);
        while(latch.getCount() > 0){
            //DO LOADY STUFF HERE, WAITING FOR BARCODE SCANNER TO RETURN
        }
        if (BarcodeApi.getInstance().getJsonResponses() != null){
            return BarcodeApi.getInstance().getJsonResponses();
        }else {
            return null;
        }

    }

    private List<String> jsonResponses;//check this to get fetched data
    //Countdown latch to stop threads messing with eachother (this is probs dumb but makes it work)
    //when using this class pass a coundownlatch of 1, have a while loop which checks value of latch and then do a loading screen in while loop
    //once data is fetched the latch will set to 0

    //Function which returns a list of ingredients from a products barcode
    private void getIngredientsFromBarcode(String barcode, Context c, CountDownLatch latch){
        jsonResponses = new ArrayList<>();

        String url = " https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json?fields=ingredients_hierarchy";
        String url2 = " https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json?fields=product_name";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "HackaSoton - Android - Version 11.0")
                .addHeader("accept", "application/json")
                .build();

        Request request2 = new Request.Builder()
                .url(url2)
                .addHeader("User-Agent", "HackaSoton - Android - Version 11.0")
                .addHeader("accept", "application/json")
                .build();


        new Thread(
                ()->{
                    try {
                        String temp = client.newCall(request).execute().body().string();
                        JSONObject response = new JSONObject(temp);
                        System.out.println("RESP: " + response.toString());
                        JSONArray jsonArray = response.getJSONObject("product").getJSONArray("ingredients_hierarchy");

                        String temp2 = client.newCall(request2).execute().body().string();
                        JSONObject response2 = new JSONObject(temp2);
                        String name = response2.getJSONObject("product").getString("product_name");
                        jsonResponses.add(name);

                        for(int i = 0; i < jsonArray.length(); i++){
                            String ingredient = jsonArray.getString(i);
                            ingredient = ingredient.substring(3);
                            jsonResponses.add(ingredient);
                        }
                        loaded = true;
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
    public boolean getLoaded() {
        return loaded;
    }

}
