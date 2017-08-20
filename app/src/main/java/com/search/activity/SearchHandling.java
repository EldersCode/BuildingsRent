package com.search.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.project.buildingsrent.AsyncResponse;
import com.project.buildingsrent.DataFromLatLng;
import com.project.buildingsrent.DownloadTask;
import com.project.buildingsrent.MapsActivity;
import com.project.buildingsrent.R;
import com.project.buildingsrent.RecyclerAdapter;
import com.project.buildingsrent.Recycler_Listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import static com.search.activity.SearchActivity.getAreaFrom;
import static com.search.activity.SearchActivity.getAreaTo;
import static com.search.activity.SearchActivity.getPriceFrom;
import static com.search.activity.SearchActivity.getPriceTo;
import static com.search.activity.SearchActivity.getSharedPrefsSearchType;

/**
 * Created by Hesham on 8/14/2017.
 */


                          //the latLng I used to filter called filterLatLng



/**  * Two ways to get the filterLatLng :

            1-in mRecyclerView.addOnItemTouchListener the method that gets the latLng of the location
            been chosen in the recycler so there I'll do what I wanted with the latLng in my activity_search filtering code .

            2- in processFinish method if the location didn't be chosen from the recycler and just typed in the editText .
*/




public class SearchHandling extends Activity implements GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener
        , AsyncResponse {


                              // declaring views and classes I need \\

    protected GoogleApiClient googleApiClient;
    private static final LatLngBounds myBounds = new LatLngBounds(
            new LatLng(-0,0),new LatLng(0,0));
    private EditText search_editText;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;
    private Button submit_btn;
    private ImageView clear_img;
    final static String fixedHttp = "https://maps.googleapis.com/maps/api/geocode/json?";
    final static String apiKey = "AIzaSyAcofb6Sy4e8WKXQcqrmWgLFpOYRF4iHr0";

                          ////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\

                    // here the latLng I will user to make filtering \\

   public LatLng filterLatLng;
                                        ////\\\



    public void onCreateHandle(Context context){
        buildGoogleApiClient();
        InitializeViews();
        Functions(context);
    }



    private void InitializeViews() {
        search_editText = (EditText) findViewById(R.id.searchA_editText);
        recyclerAdapter = new RecyclerAdapter(this, R.layout.recycler_row, googleApiClient, myBounds, null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerIdA);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);
        submit_btn = (Button) findViewById(R.id.submit_address_btn);
        clear_img = (ImageView) findViewById(R.id.clear_img);
    }





    private void Functions(final Context context){

        mRecyclerView.setVisibility(View.GONE);
        clear_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_editText.getText().clear();
            }
        });

        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                if(!s.toString().equals("") && googleApiClient.isConnected()){
                    recyclerAdapter.getFilter().filter(s.toString());
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                else if(!googleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), "Google API Client is not connected", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mRecyclerView.addOnItemTouchListener(

                new Recycler_Listener(this, new Recycler_Listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view , int position) {
                        final RecyclerAdapter.AT_Place item = recyclerAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);

                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(googleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){

                                    //here the way I get the latLng of the location clicked on recyclerView \\

//             Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                    filterLatLng = places.get(0).getLatLng();
                                    Log.i("recycler latlng" , filterLatLng.toString());
                                    //country , city , area from searching battern


//                                    // changing the editText text \\

                                    search_editText.setText(String.valueOf(places.get(0).getAddress()));
                                    mRecyclerView.setVisibility(View.GONE);


                                }else {
                                    Toast.makeText(getApplicationContext(),"Something went wrong !",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );







    }
//retriving data according to country city region and categorie
    private void fireRetrive(final String country, final String city, final String area) {
        final SearchActivity searchActivity=new SearchActivity();

//getting the data and begin to felter
        final String adress=country+"/"+city+"/"+area+"/"+searchActivity.getCategorieItem();
        //seaving searching instances to be showen in map's activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contex);
        final SharedPreferences.Editor editor = preferences.edit();
Log.e("ffffffff",searchActivity.getAreaFrom());
        editor.putString("path",adress);
        editor.putString("type", searchActivity.getCategorieItem());
        editor.putString("areaFrom",searchActivity.getAreaFrom());
        editor.putString("areaTo",searchActivity.getAreaTo());
        editor.putString("priceFrom",searchActivity.getPriceFrom());
        editor.putString("priceTo",searchActivity.getPriceTo());
        editor.apply();
        Intent intent = new Intent(contex , MapsActivity.class);
        intent.putExtra("lat" , filterLatLng.latitude);
        intent.putExtra("lng" , filterLatLng.longitude);
        intent.putExtra("ok" , "ok");
        intent.putExtra("location" , "area");
        startActivity(intent);

        /////////////////////////////


    }


    private void fireRetriveCity(final String country, final String city) {
        final SearchActivity searchActivity=new SearchActivity();

//getting the data and begin to felter
        final String adress=country+"/"+city+"/"+searchActivity.getCategorieItem();
        //seaving searching instances to be showen in map's activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contex);
        final SharedPreferences.Editor editor = preferences.edit();
        Log.e("ffffffff",searchActivity.getAreaFrom());
        editor.putString("type", searchActivity.getCategorieItem());
        editor.putString("path",adress);
        editor.putString("areaFrom",searchActivity.getAreaFrom());
        editor.putString("areaTo",searchActivity.getAreaTo());
        editor.putString("priceFrom",searchActivity.getPriceFrom());
        editor.putString("priceTo",searchActivity.getPriceTo());
        editor.apply();
        Intent intent = new Intent(contex , MapsActivity.class);
        intent.putExtra("lat" , filterLatLng.latitude);
        intent.putExtra("lng" , filterLatLng.longitude);
        intent.putExtra("ok" , "ok");
        intent.putExtra("location" , "city");
        startActivity(intent);


        /////////////////////////////


    }





    public void findAddress(View view) {
//fireRetrive();
        //hn5ally el keyboard te5tfy b3d ma el user y5allas ketaba

        InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(search_editText.getWindowToken(),0);

        try {

            if (search_editText.length() == 0 || getAreaFrom().equals("") || getAreaTo().equals("")
                    || getPriceFrom().equals("")
                    || getPriceTo().equals("")) {
                Log.i("Empty" , "EditText is Empty");
                Toast.makeText(this, "Please enter the full information to search ..", Toast.LENGTH_SHORT).show();
            } else {

                ConnectivityManager connectMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                   connectMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()   == NetworkInfo.State.CONNECTED  ) {


                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(getString(R.string.pleaseWait));
                    progressDialog.show();

                    //hn5ally el activity_search editText yeb2a feh + ben kol kelma fl address (n7welha le URL form)

                    String encodedAddress = URLEncoder.encode(search_editText.getText().toString(), "UTF-8");
                    String httpWeb = fixedHttp + "address=" + encodedAddress + "&key=" + apiKey;

                    Log.i("httpWeb", httpWeb);


                    DownloadTask task = new DownloadTask();

                    // we used the interface to get the data we have after the onPostExecute method finished to use it ..

                    task.delegate = this;
                    task.execute(httpWeb);
                    try {

                        //country , city , area from searching battern
                        DataFromLatLng dataFromLatLng = new DataFromLatLng(filterLatLng.latitude, filterLatLng.longitude,
                                this);
                        String country = dataFromLatLng.getMyCountry();
                        String city = dataFromLatLng.getMyCity();
                        String area = dataFromLatLng.getMyArea();
//                    fireRetrive(country,city,area);
                        //////////////////
                    } catch (Exception e) {
//                    Toast.makeText(this, filterLatLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Please check internet connection !", Toast.LENGTH_SHORT).show();
                }
            }
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
            Toast.makeText(this, "Please enter the address you want to search for ..", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting() ){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void processFinish(String output) {
        Log.i("output", output);

        try {

            JSONObject jsonObject = new JSONObject(output);
            String results = jsonObject.getString("results");
            Log.i("results", results);
            JSONArray arr = new JSONArray(results);


            // getting lat & lng from jason

            for (int i = 0; i < arr.length(); i++) {

                JSONObject jsonPart = arr.getJSONObject(i);
                JSONObject jsonGeometry = jsonPart.getJSONObject("geometry");
                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                String lat = jsonLocation.getString("lat");
                String lng = jsonLocation.getString("lng");

                // getting the values of latitude and longitude

                Double latitude = Double.valueOf(lat);
                Double longitude = Double.valueOf(lng);
                Log.i("lat & lng :", latitude + "  " + longitude);

                filterLatLng = new LatLng(latitude,longitude);
                Log.i("process filter latlng" , filterLatLng.toString());

//                Toast.makeText(getApplicationContext(), filterLatLng.toString(), Toast.LENGTH_SHORT).show();

            //country , city , area from searching battern
                SearchActivity activity=new SearchActivity();
                DataFromLatLng dataFromLatLng=new DataFromLatLng(filterLatLng.latitude,filterLatLng.longitude,
                        contex );
                String country=dataFromLatLng.getMyCountry();
                String city=dataFromLatLng.getMyCity();
                String area=dataFromLatLng.getMyArea();

                //////////////////

                    SharedPreferences sharedPreferences =  getSharedPreferences(getSharedPrefsSearchType() , 0);
                    if (sharedPreferences.contains("myType")){

                        String type = sharedPreferences.getString("myType" , "");


                        if(type.equals("city")){

                            fireRetriveCity(country , city);


                        }else if (type.equals("area")) {

                            fireRetrive(country, city, area);

                        }

                    }

            }

        } catch (JSONException e) {
            Toast.makeText(this, "This Place not found or connection lost ..", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if(mRecyclerView.getVisibility() == View.VISIBLE){
            mRecyclerView.setVisibility(View.GONE);
        }else{
            startActivity(new  Intent(getApplicationContext(), MapsActivity.class));
            finish();
        }
    }
    Context contex;
public Context setContext(  Context Scontext){
    contex=Scontext;
return contex;}
}
