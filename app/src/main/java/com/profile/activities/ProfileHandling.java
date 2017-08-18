package com.profile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.project.buildingsrent.AdvertiseActivity;
import com.project.buildingsrent.MapsActivity;
import com.project.buildingsrent.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.profile.activities.EditProfileHandling.getSharedPrefs;
import static com.project.buildingsrent.HandlingLoginAuth.getPrefsName;

/**
 * Created by Hesham on 8/6/2017.
 */

public class ProfileHandling extends Activity {

                    // declaration of layout views here \\

    private TextView profileName , profileEmail , profileAddress , profilePhone , profileGender , profileBirthday ;
    private CircleImageView profilePic;
    private Button editBtn;

                             // Firebase and facebook user auth \\
                             FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Profile profile = Profile.getCurrentProfile();

                                            /////\\\\

    public void ProfileHandlingMain(Context context){

        initializingViews();
        getUserData(context);
        functions(context);

    }


                            // initializing views here in this method \\

    public void initializingViews(){

        profileName = (TextView) findViewById(R.id.profile_name);
        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileAddress = (TextView)findViewById(R.id.profile_address);
        profilePhone = (TextView) findViewById(R.id.profile_contactNumber);
        profileGender = (TextView) findViewById(R.id.profile_gender);
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        profileBirthday = (TextView) findViewById(R.id.profile_birthday);
        editBtn = (Button) findViewById(R.id.edit_profile_btn);

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
}
