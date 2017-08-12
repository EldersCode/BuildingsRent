package com.project.buildingsrent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Hesham on 5/26/2017.
 */

public class HandlingMaps extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks ,
        GoogleApiClient.OnConnectionFailedListener ,
        AsyncResponse ,
        LocationListener ,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private static final int REQUEST_CODE = 1;
    Button button;    Bitmap bitmap;
    public static SliderLayout mDemoSlider;


    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient ;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment ;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    ////////////////submitBuildingInfo

    EditText priceEditText;
    EditText ApartmentAreaEditText;
    EditText noOfBedRoomsEditText;
    EditText noOfBathRoomsEditText;
    Switch parkingLotsSwitch;
    Switch LivingRoomSwitch;
    Switch KitchenSwitch;
    Switch coolingSystemSwitch;
    Switch NegotiablePriceSwitch;
    LinearLayout petsLayout;
    Switch petsSwitch;
    Button locateFlat;
    static TextSliderView textSliderView;
    private StorageReference storageReference;

                 //search location on map components here ..

    RelativeLayout search_layout;
    Button cancel_btn , confirm_btn;

    private static final LatLngBounds myBounds = new LatLngBounds(
            new LatLng(-0,0),new LatLng(0,0));

    public static Marker myDefaultMarker;
    LatLng myLatLng;
     RecyclerView mRecyclerView;
     LinearLayoutManager linearLayoutManager;
     RecyclerAdapter recyclerAdapter;
     EditText search_editText;
     ImageView search_img;
    final static String fixedHttp = "https://maps.googleapis.com/maps/api/geocode/json?";
    final static String apiKey = "AIzaSyAcofb6Sy4e8WKXQcqrmWgLFpOYRF4iHr0";

                    //////////////////////////////////////

    public void sheetsWedgits(){

        //slider creation and sending image instance
        storageReference= FirebaseStorage.getInstance().getReference();

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

//////////slider creation end
        //declearing inistances for home
        petsLayout = (LinearLayout) findViewById(R.id.switchOn_pets);
        petsSwitch = (Switch) findViewById(R.id.petSwitch);
        priceEditText = (EditText) findViewById(R.id.Price);
        ApartmentAreaEditText = (EditText) findViewById(R.id.area);
        noOfBedRoomsEditText = (EditText) findViewById(R.id.bedrooms);
        noOfBathRoomsEditText = (EditText) findViewById(R.id.bathrooms);
        parkingLotsSwitch=(Switch)findViewById(R.id.ParkingSwitch);
        LivingRoomSwitch=(Switch)findViewById(R.id.livingRoomSwitch);
        KitchenSwitch=(Switch)findViewById(R.id.kitchenSwitch);
        coolingSystemSwitch=(Switch)findViewById(R.id.coolingSystemSwitch);
        NegotiablePriceSwitch=(Switch)findViewById(R.id.negotiablePriceSwitch);
    locateFlat = (Button) findViewById(R.id.locateFlat);
    }
    void onCreateHandle(){
        buildGoogleApiClient();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationDialog();
        }

        else {

            checkLocationDialog();

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Initializing();
        AutocompleteApi();

    }


    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;

        try{
             mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,R.raw.style_json ));


        }catch (Exception e){

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //to animate camera on the last location when gps closed (it needs database)
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && mLastLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 12));
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //  mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


        public boolean checkLocationPermission() {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
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

                                        ////////////////

                    ////////////////////////////////////////////////////////

                             //moving the camera to the new location and adding a marker

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 7));
               myDefaultMarker = mMap.addMarker(new MarkerOptions().title("here").position(new LatLng(latitude , longitude)).draggable(true));
                myLatLng = new LatLng(latitude,longitude);


                            /* handling dragging the marker to another place methods ..
                 The Latlng after Dragging that we will use called myLatLng (onMarkerDragEnd) ...*/

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                       myLatLng = marker.getPosition();

                    }
                });

                                ///////////////////////////////////////////


            }

        } catch (JSONException e) {
            Toast.makeText(this, "This Place not found or connection lost ..", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


                    ///////////////////////////////////////////////////////////////////////


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
    }



                                 // here the methods I need to search for location


    private void Initializing(){


                    //here the initialization of the layout that contains all of search location views ..

        search_layout = (RelativeLayout)findViewById(R.id.search_location_layout);

                                           //////////////////////////


                    // here the initialization of all the views we use to search and the recycler

        search_editText = (EditText)findViewById(R.id.search_editText);
        search_img = (ImageView)findViewById(R.id.search_img);
        confirm_btn = (Button)findViewById(R.id.confirmBtn_Id);
        cancel_btn = (Button) findViewById(R.id.cancelBtn_Id);
        recyclerAdapter = new RecyclerAdapter(this, R.layout.recycler_row , mGoogleApiClient, myBounds , null);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(recyclerAdapter);

                                    /////////////////////////////////

    }


                ////////////////////////////////////////////////////////////////////////////////////////


                        // the method of autocomplete recycler while typing ..

    private void  AutocompleteApi(){

        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("") && mGoogleApiClient.isConnected()){
                    recyclerAdapter.getFilter().filter(s.toString());
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                else if(!mGoogleApiClient.isConnected()){
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
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    findAddress(search_img);
//                                    Toast.makeText(getApplicationContext(), "please click on Search button on the left ..",Toast.LENGTH_SHORT).show();

                                    //LatLng latLng = String.valueOf(places.get(0).getLatLng()) ;

                                    //////////// hena bageb el latitude w el longitude w ab3thom lel map !! aw a7sn a5ally el edit text yeb2a feh el address
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


                                /////////////////////////////////////////////////


            // the method that performed when Click on search button or recycler

    public void findAddress(View view) {

        //hn5ally el keyboard te5tfy b3d ma el user y5allas ketaba

        mRecyclerView.setVisibility(View.GONE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Your Guide");
        alert.setCancelable(false);
        alert.setIcon(R.mipmap.alarm);
        alert.setMessage("You can long click on marker and drag the nearest marker to the location of your apartment");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.setCancelable(true);
            }
        });
        alert.create().show();

        InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethod.hideSoftInputFromWindow(search_editText.getWindowToken(),0);

        try {

            if (search_editText.length() == 0) {
                Log.i("Empty" , "EditText is Empty");
                Toast.makeText(this, "Please enter the address you want to search for ..", Toast.LENGTH_SHORT).show();
            } else {


                //hn5ally el search editText yeb2a feh + ben kol kelma fl address (n7welha le URL form)

                String encodedAddress = URLEncoder.encode(search_editText.getText().toString(), "UTF-8");
                String  httpWeb = fixedHttp + "address=" + encodedAddress + "&key=" + apiKey;

                Log.i("httpWeb", httpWeb);

                // I will send the httpWeb which contains the full http site for what the user searchs for ..

                // we open connection by the DownloadTask Class
                DownloadTask task = new DownloadTask();
                // we used the interface to get the data we have after the onPostExecute method finished to use it ..
                task.delegate = this;
                task.execute(httpWeb);

            }
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
            Toast.makeText(this, "Please enter the address you want to search for ..", Toast.LENGTH_SHORT).show();
        }


    }

                           //////////////////////////////////////////////////////////////////////

                    // the dialog shown when GPS is disabled ..

    public void checkLocationDialog(){
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder askPermission = new AlertDialog.Builder(this);
            askPermission.setTitle(R.string.ask_permission_title);
            askPermission.setCancelable(false);
            askPermission.setIcon(R.mipmap.alarm);
            askPermission.setMessage(R.string.ask_permission_messege);
            askPermission.setPositiveButton(R.string.ask_permission_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            askPermission.setNegativeButton(R.string.ask_permission_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    askPermission.setCancelable(false);
                }
            });
            askPermission.create();
            askPermission.show();
        }
    }

                    ///////////////////////////////////////////////////////////

    //on sheets result (slider and sending images to firebase)
    @Override
                    //when an image selected
                    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//

                        InputStream stream = null;
                        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
                            try {
                                //sending image to firebase and store it
                                final Uri uri =data.getData();
                                StorageReference filepath =storageReference.child("flats").child("userId");
                                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                               @Override
                                                                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                   Toast.makeText(getApplicationContext(),"sent",Toast.LENGTH_SHORT).show();}
                                });
                                ///////////////////////sending image end
                                // recyle unused bitmaps
                                if (bitmap != null) {
                                    bitmap.recycle();

                                }
                                stream = getContentResolver().openInputStream(data.getData());

                                bitmap = BitmapFactory.decodeStream(stream);
                                //setting the image in the slider
                                 textSliderView = new TextSliderView(this);
                                textSliderView
                                        .image(data.getDataString())
                                        .description("image")

                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        ;

                                mDemoSlider.addSlider(textSliderView);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    }
/////////////////// on sheets result end













//slider images on submit info class
    @Override//
    protected void onStop() {//
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();//
        super.onStop();//
    }//

    @Override//
    public void onSliderClick(BaseSliderView slider) {//
        slider.getView();//
        Log.e("ccccccc",slider.getView().getId()+"");
        Toast.makeText(getApplication(),slider.getView().getId()+"",Toast.LENGTH_SHORT).show();

    }//


    @Override//
    public void onPageScrollStateChanged(int state) {}//
    public void onClick(View View) {//
        Intent intent = new Intent();//
        intent.setType("image/*");//
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);//
        startActivityForResult(intent, REQUEST_CODE);//
    }//


    @Override//
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}//

    @Override//
    public void onPageSelected(int position) {
        Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
        try {
//            mDemoSlider.setDrawingCacheEnabled(false);

        }catch (Exception e){

            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        Log.d("Slider Demo", "Page Changed: " + position);
    }//

}
