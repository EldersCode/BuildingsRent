package com.project.buildingsrent;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hesham.Adawy on 4/2/2017.
 */

public class    DownloadTask extends AsyncTask<String , Void , String> {

    Double latitude;
    Double longitude;

    public AsyncResponse delegate = null;




    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection connection;

        try {

            url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);


            int data = reader.read();
            while (data != -1){

                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {

            JSONObject jsonObject = new JSONObject(result);
            String results = jsonObject.getString("results");
            Log.i("results" , results);
            JSONArray arr = new JSONArray(results);

            for(int i=0 ; i<arr.length() ; i++){

                JSONObject jsonPart = arr.getJSONObject(i);
                JSONObject jsonGeometry = jsonPart.getJSONObject("geometry");
                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                String lat = jsonLocation.getString("lat");
                String lng = jsonLocation.getString("lng");

                latitude = Double.valueOf(lat);
                longitude = Double.valueOf(lng);
                Log.i("lat & lng :" , latitude + "  " +longitude);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        delegate.processFinish(result);


    }

}
