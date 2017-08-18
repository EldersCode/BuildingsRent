package com.project.buildingsrent;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by heshamsalama on 8/18/2017.
 */

public class Felteration extends HandlingMaps {
    public void felter(Context context, final GoogleMap M) {
        ////check for searching instances to be feltered
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String searchBath = preferences.getString("bath", "");
        String IdCount = preferences.getString("IdCount", "");
        if (searchBath != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference(searchBath);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildrenCount();
                    int x = 1;
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference refL = FirebaseDatabase.getInstance().getReference(searchBath + "/" + x + "/location");
                        GeoFire geoFire = new GeoFire(refL);
                        final int finalX = x;
                        geoFire.getLocation("firebase-hq", new LocationCallback() {
                            @Override
                            public void onLocationResult(String key, GeoLocation location) {
                                if (location != null) {

                                    M.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(key).icon(BitmapDescriptorFactory.defaultMarker()));

                                    Log.e("aaaaaaaaaaamessage", finalX + "id" + key + location.latitude + location.longitude + "");

                                } else {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        String coolingSystem = (String) messageSnapshot.child("coolingSystem").getValue();
                        String area = (String) messageSnapshot.child("area").getValue();
                        String price = (String) messageSnapshot.child("price").getValue();
                        String descriptionEditText = (String) messageSnapshot.child("descriptionEditText").getValue();
                        String bedRoomsNo = (String) messageSnapshot.child("bedRoomsNo").getValue();
                        String bathNo = (String) messageSnapshot.child("bathNo").getValue();
                        String kitchen = (String) messageSnapshot.child("kitchen").getValue();
                        String livingRoom = (String) messageSnapshot.child("livingRoom").getValue();
                        String negotiablePrice = (String) messageSnapshot.child("negotiablePrice").getValue();
                        String parking = (String) messageSnapshot.child("parking").getValue();
//                            String location = (String) messageSnapshot.child("location").;

//                            Log.e("aaaaaaaaaaaname", name);
//                            Log.e("aaaaaaaaaaamessage", String.valueOf(name));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}