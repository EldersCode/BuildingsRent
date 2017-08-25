package com.profile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.buildingsrent.AdvertiseActivity;
import com.project.buildingsrent.MapsActivity;
import com.project.buildingsrent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.profile.activities.EditProfileHandling.getSharedPrefs;
import static com.project.buildingsrent.HandlingLoginAuth.getPrefsName;

/**
 * Created by Hesham on 8/6/2017.
 */

public class ProfileHandling extends Activity implements AdapterView.OnItemClickListener {

                    // declaration of layout views here \\

    private TextView profileName , profileEmail , profileAddress , profilePhone , profileGender , profileBirthday , noAdsText ;
    private CircleImageView profilePic;
    private Button editBtn;
    private ListView myAdListView;
    private ArrayList<HashMap<String , String>> AdvCardArrayList;
    private  CustomAdAdapter customAdAdapter;

                             // Firebase and facebook user auth \\
                             FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Profile profile = Profile.getCurrentProfile();

                                            /////\\\\

    public void ProfileHandlingMain(Context context){

        initializingViews(context);
        getUserData(context);
        functions(context);

    }


                            // initializing views here in this method \\

    public void initializingViews(Context context){

        profileName = (TextView) findViewById(R.id.profile_name);
        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileAddress = (TextView)findViewById(R.id.profile_address);
        profilePhone = (TextView) findViewById(R.id.profile_contactNumber);
        profileGender = (TextView) findViewById(R.id.profile_gender);
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        profileBirthday = (TextView) findViewById(R.id.profile_birthday);
        editBtn = (Button) findViewById(R.id.edit_profile_btn);
        noAdsText = (TextView)findViewById(R.id.noAdsText);
        AdvCardArrayList = new ArrayList<>();


        HashMapAdvList(AdvCardArrayList);

        myAdListView = (ListView) findViewById(R.id.userAdsListId);


        myAdListView.setOnItemClickListener(this);

        if(AdvCardArrayList.size() >0) {
            if(noAdsText.getText().length() >0){
                noAdsText.setText("");
            }
                customAdAdapter = new CustomAdAdapter(context, AdvCardArrayList , myAdListView);
                myAdListView.setAdapter(customAdAdapter);
                setListSize(myAdListView);
        }else{
            noAdsText.setText(R.string.noAds);
        }
    }

                                                ///\\\


                             // views function \\

    public void functions(final Context context){

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext() , EditProfileActivity.class));
                finish();

            }
        });


    }

                                     //\\


          // here the method to retrieve the user's information from shared Preference if logged in with facebook \\
                        // and get available current user info if logged in with email and password \\


    public void getUserData(Context context){


                       // if logged in with email and password \\
        if (currentUser != null && currentUser.isEmailVerified()){
            try {

                SharedPreferences prefs = getSharedPreferences(getSharedPrefs() , 0);
                profileName.setText(prefs.getString("name" , "Not found"));
                profilePhone.setText(prefs.getString("phone" , "Not found"));
                profileAddress.setText(prefs.getString("location" , "Not found"));
                profileBirthday.setText(prefs.getString("birthday" , "Not found"));
                profileGender.setText(prefs.getString("gender" , "Not found"));
                profileEmail.setText(prefs.getString("email" , currentUser.getEmail()));

                try{

                    String profile_pic = prefs.getString("profile_pic" , "");
                    if( ! profile_pic.equals("")) {
                        byte[] b = Base64.decode(profile_pic, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        profilePic.setImageBitmap(bitmap);
                    }

                }catch (Exception e){

                }

            }catch (Exception e){

            }
        }

                                         ///\\\

                            // if logged in with facebook \\


        if (currentUser != null && profile != null){
            SharedPreferencesRetrieve(context);
        }

                                         ///\\\

    }




                                           ////////////////\\\\\\\\\\\\\\\\


                               // handling shared preferences data from facebook \\

    private void SharedPreferencesRetrieve(Context context){

        SharedPreferences myPrefs = getSharedPreferences(getPrefsName() , 0);
        String id = myPrefs.getString("id" , "No ID");
        Log.i("id" , id);

        try {


            try {

                String profile_pic = myPrefs.getString("profile_pic" , "");
                Log.i("profile_pic", profile_pic);
                if(profile_pic.contains("https")) {
                    Picasso.with(context).load(profile_pic).into(profilePic);
                }else {
                    byte[] b = Base64.decode(profile_pic, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    profilePic.setImageBitmap(bitmap);
                }

            } catch (Exception e) {

            }


            if(myPrefs.contains("name")){
                String user_name = myPrefs.getString("name" , "Not Found");
                Log.i("SharedPrefs name: " , user_name);
                profileName.setText(user_name);
            }
            if (myPrefs.contains("email")) {
                String user_email = myPrefs.getString("email" , "Not Found");
                Log.i("SharedPrefs email: " , user_email);
                profileEmail.setText(user_email);
            }
            if (myPrefs.contains("gender")) {
                String user_gender = myPrefs.getString("gender" , "Not Found");
                Log.i("SharedPrefs gender: " , user_gender);
                profileGender.setText(user_gender);
            }
            if (myPrefs.contains("birthday")) {
                String user_bod = myPrefs.getString("birthday" , "Not Found");
                Log.i("SharedPrefs birthday: " , user_bod);
                profileBirthday.setText(user_bod);
            }
            if (myPrefs.contains("location")) {
                String user_location = myPrefs.getString("location" , "Not Found");
                Log.i("SharedPrefs location: " , user_location);
                profileAddress.setText(user_location);
                if(myPrefs.contains("phone")){
                    String phone = myPrefs.getString("phone" , "Not found");
                    profilePhone.setText(phone);
                }
            }

        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext() , MapsActivity.class));
        finish();
    }

    public void HashMapAdvList(ArrayList<HashMap<String,String>> arrayListAdd ){

        try{

            Bundle bundle = getIntent().getExtras();
            String key = bundle.getString("change");
            if(key.equals("change")){

                String[] address = {"Sedi gaber , Alexandria , Egypt" , "Mandara , Alexandria , Egypt" , "ibrahimia , Alexandria , Egypt" , "Sedi beshr , Alexandria , Egypt" };
                String[] price = {"3500" , "1200" , "2500" , "900"};

                for(int i=0 ; i <price.length ; i++){
                    HashMap<String , String> data = new HashMap<>();
                    data.put("price" , price[i]);
                    data.put("address" , address[i]);
                    data.put("cardImage" , "");

                    arrayListAdd.add(data);

                }

            }

        }catch (Exception e){

            String[] address = {"Sedi gaber , Alexandria , Egypt" , "Mandara , Alexandria , Egypt" , "ibrahimia , Alexandria , Egypt" , "Sedi beshr , Alexandria , Egypt" };
            String[] price = {"3500" , "1200" , "2500" , "900"};
            int[] images = {R.mipmap.house , R.mipmap.stage , R.mipmap.beachhouse , R.mipmap.land};


            // I will make a HashMap inside for loop to add the Ads to the array list \\

                for(int i=0 ; i <price.length ; i++){
                    HashMap<String , String> data = new HashMap<>();
                    data.put("price" , price[i]);
                    data.put("address" , address[i]);
                    data.put("cardImage" , String.valueOf(images[i]));

                    arrayListAdd.add(data);

                }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String mPosition = parent.getItemAtPosition(position).toString();

        Toast.makeText(this, "position is : " + mPosition, Toast.LENGTH_SHORT).show();

    }

    public void setListSize(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight =0 ;
        View view = null;
        for(int i=0 ; i < listAdapter.getCount();i++){
            view = listAdapter.getView(i , view , listView);

            if(i == 0)
            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth , ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth , View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}


