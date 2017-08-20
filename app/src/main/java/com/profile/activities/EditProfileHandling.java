package com.profile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.project.buildingsrent.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.project.buildingsrent.HandlingLoginAuth.getPrefsName;

/**
 * Created by Hesham on 8/6/2017.
 */

public class EditProfileHandling extends Activity  implements AdapterView.OnItemSelectedListener ,
        ViewPagerEx.OnPageChangeListener{

                  // declaration of layout views here \\

    private EditText Name , Email , Address , Phone  , Birthday ;
    Spinner Gender;
    private CircleImageView Pic;
    private Button saveBtn;
    String genderS;
    private static final String SHARED_PREFS="EditPrefs";
                                 ///\\\

                    // Request code for start activity for a result to change profile pic \\

    private static final int R_CODE = 1000;
    Bitmap bitmap;




    // Spinner array \\

  String[] genderArray={"Male","Female","other"};


                                  //\\

                    // Firebase and facebook user auth \\
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    Profile profile = Profile.getCurrentProfile();

                                /////\\\\

    public void EditProfileHandlingMain(Context context){

        initializingViews(context);
        StandardDataFromProfile();
        functions(context);

    }


                      // initializing views here in this method \\

    public void initializingViews(Context context){

        Name = (EditText) findViewById(R.id.edit_profile_name);
        Email = (EditText) findViewById(R.id.edit_profile_email);
        Address = (EditText) findViewById(R.id.edit_profile_address);
        Phone = (EditText) findViewById(R.id.edit_profile_contactNumber);
        Gender = (Spinner) findViewById(R.id.genderSpinner);
        Pic = (CircleImageView)findViewById(R.id.profile_image);
        Birthday = (EditText) findViewById(R.id.edit_profile_birthday);
        saveBtn = (Button) findViewById(R.id.save_profile_btn);


                      //Creating the ArrayAdapter instance having the bank name list \\

        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,genderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        //Setting the ArrayAdapter data on the Spinner
        Gender.setAdapter(adapter);
        Gender.setOnItemSelectedListener(this);

    }

                                      ///\\\


    // views function \\

    public void functions(final Context context){

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesChangeData(context);
                startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                finish();

            }
        });

    }

    //\\


    // here the method to retrieve the user's information from shared Preference if logged in with facebook \\
    // and get available current user info if logged in with email and password \\




    ////////////////\\\\\\\\\\\\\\\\


    // handling shared preferences data from facebook \\

    private void SharedPreferencesChangeData(Context context) {


                            // Email and Password handling \\

        if (currentUser != null && currentUser.isEmailVerified()) {
            SharedPreferences shared = getSharedPreferences(SHARED_PREFS, 0);
            SharedPreferences.Editor editor = shared.edit();

            try {
                editor.putString("name" , Name.getText().toString());
                editor.putString("email",Email.getText().toString());
                editor.putString("location",Address.getText().toString());
                editor.putString("phone" , Phone.getText().toString());
                editor.putString("birthday" ,  Birthday.getText().toString());
                editor.putString("gender" , genderS);

                try {
                    String imageName = String.valueOf(Pic.getTag());

                    if( ! imageName.equals("profileuser")) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        editor.putString("profile_pic", encodedImage);

                    }
                }catch (Exception e){

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        }

        if(currentUser != null && profile != null){
            SharedPreferences myPrefs = getSharedPreferences(getPrefsName(),0);
            SharedPreferences.Editor edit = myPrefs.edit();
            try {

                edit.putString("name" , Name.getText().toString());
                edit.putString("email",Email.getText().toString());
                edit.putString("location",Address.getText().toString());
                edit.putString("phone" , Phone.getText().toString());
                edit.putString("birthday" ,  Birthday.getText().toString());
                edit.putString("gender" , genderS);
                try {
                String imageName = String.valueOf(Pic.getTag());

                if( ! imageName.equals("profileuser")) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();

                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        edit.putString("profile_pic", encodedImage);

                }
                }catch (Exception e){

                }
            }catch (Exception e){
                Toast.makeText(context, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
            edit.apply();
        }
    }


                          // Spinner OnItemSelected Methods \\
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         genderS = genderArray[position];
        Log.i("Gender : " , genderS);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
                                        ///\\\

                          // getter for shared preferences of email and password login \\
    public static String getSharedPrefs() {
        return SHARED_PREFS;
    }
                                                    ///\\\

    private void StandardDataFromProfile(){

        // Email and Password handling \\

        if (currentUser != null && currentUser.isEmailVerified()) {
            SharedPreferences shared = getSharedPreferences(SHARED_PREFS, 0);

            try {
                Name.setText(shared.getString("name" ,R.string.editNameHint + ""));
               Email.setText(shared.getString("email",currentUser.getEmail()));
                Address.setText(shared.getString("location",R.string.editAddressHint + ""));
                Phone.setText(shared.getString("phone" , R.string.contactNumEditHint + ""));
               Birthday.setText(shared.getString("birthday" , R.string.editBirthHint + ""));

                try{

                    String profile_pic = shared.getString("profile_pic" , "");
                    Picasso.with(getApplicationContext()).load(profile_pic).into(Pic);


                }catch (Exception e){

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        }

        if(currentUser != null && profile != null){
            SharedPreferences myPrefs = getSharedPreferences(getPrefsName(),0);
            try {
                Name.setText(myPrefs.getString("name" ,R.string.editNameHint + ""));
                Email.setText(myPrefs.getString("email",R.string.editEmailHint + ""));
                Address.setText(myPrefs.getString("location",R.string.editAddressHint + ""));
                Phone.setText(myPrefs.getString("phone" , R.string.contactNumEditHint + ""));
                Birthday.setText(myPrefs.getString("birthday" , R.string.editBirthHint + ""));
                try{

                    String profile_pic = myPrefs.getString("profile_pic" , "");
                    Picasso.with(getApplicationContext()).load(profile_pic).into(Pic);


                }catch (Exception e){

                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void ChangeProfilePic(View View) {//
        Intent intent = new Intent();//
        intent.setType("image/*");//
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);//
        startActivityForResult(intent, R_CODE);//
    }//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        InputStream stream;
        if (requestCode == R_CODE && resultCode == Activity.RESULT_OK)
            try {

                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                Pic = (CircleImageView) findViewById(R.id.edit_profile_image) ;
                Pic.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "Profile Picture Changed Successfully", Toast.LENGTH_SHORT).show();




            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
    }

}
