package com.profile.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.project.buildingsrent.AdvertiseActivity;

/**
 * Created by Hesham on 8/25/2017.
 */

public class myAdvSharedPrefs extends Activity {

    final static String MY_ADV_KEY = "MY_ADS";

    public myAdvSharedPrefs(Context context , String address , String price , String image ){

        SharedPreferences sharedPreferences = getSharedPreferences(MY_ADV_KEY , 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("address" ,address);
        editor.putString("price" , price);
        editor.putString("image" , image);
        editor.apply();
        Intent intent = new Intent(context , AdvertiseActivity.class);
        startActivity(intent);

    }

}
