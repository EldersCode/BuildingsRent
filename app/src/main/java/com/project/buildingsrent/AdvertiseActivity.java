package com.project.buildingsrent;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvertiseActivity extends HandlingAdvertise {
TextView advPrice,advArea,description,bedRooms,bathRooms,features;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        HandlingAdvertise(AdvertiseActivity.this);
        advPrice=(TextView)findViewById(R.id.advPrice);
        advArea=(TextView)findViewById(R.id.areaAdv);
        description=(TextView)findViewById(R.id.advDescription);
        bedRooms=(TextView)findViewById(R.id.noOfBedRoomsAdv);
        bathRooms=(TextView)findViewById(R.id.noOfBathroomsAdv);
        features=(TextView)findViewById(R.id.advFeatures);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = preferences.getString("data", "");
        Log.e("dddddddd",data);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ref=database.getReference(data);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String coolingSystem = (String) dataSnapshot.child("coolingSystem").getValue();
                Log.e("ddddd",coolingSystem);
                String area = (String) dataSnapshot.child("area").getValue();
                final String price = (String) dataSnapshot.child("price").getValue();
                String descriptionEditText = (String) dataSnapshot.child("descriptionEditText").getValue();
                String bedRoomsNo = (String) dataSnapshot.child("bedRoomsNo").getValue();
                String bathNo = (String) dataSnapshot.child("bathNo").getValue();
                String kitchen = (String) dataSnapshot.child("kitchen").getValue();
                String livingRoom = (String) dataSnapshot.child("livingRoom").getValue();
                String negotiablePrice = (String) dataSnapshot.child("negotiablePrice").getValue();
                String parking = (String) dataSnapshot.child("parking").getValue();
                if (coolingSystem.equals("true")){
  features.setText(features.getText()+"coolig System");}
            if (parking.equals("true")){  features.setText(features.getText()+" , parking");}
                if (negotiablePrice.equals("true")){  features.setText(features.getText()+" , Negotiable Price");}
                if (livingRoom.equals("true")){  features.setText(features.getText()+" , LivingRoom");}
                if (kitchen.equals("true")){  features.setText(features.getText()+" , kitchen");}
                if (bathNo != null){  bathRooms.setText(bathNo);}
                if (bedRooms !=null){  bedRooms.setText(bedRoomsNo);}
                if (descriptionEditText !=null){  description.setText(descriptionEditText);}
                if (area !=null){  advArea.setText(area);}
                if (price !=null){  advPrice.setText(price);}

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
