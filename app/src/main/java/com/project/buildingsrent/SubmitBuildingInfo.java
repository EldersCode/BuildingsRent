package com.project.buildingsrent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by heshamsalama on 6/16/2017.
 */

public class SubmitBuildingInfo extends Activity {
    //private DatabaseReference firebaseDatabase;
    private FirebaseDatabase database;
    private ImageView cameraImg;
    private int flatsNo =1;
    private DatabaseReference houses;
    ////////////////////////////////
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contry/" + "city/" +"region/"+ flatsNo);
    GeoFire geoFire = new GeoFire(ref);
    public SubmitBuildingInfo() {


    }

    public SubmitBuildingInfo(final Context context, final LatLng latLng, final GoogleMap mMap, final String building, Button locateFlat, final LinearLayout petsLayout, Switch petsSwitch, final EditText priceEditText, final EditText apartmentAreaEditText, final EditText noOfBedRoomsEditText, final EditText noOfBathRoomsEditText, final Switch parkingLotsSwitch, final Switch livingRoomSwitch, final Switch kitchenSwitch, final Switch coolingSystemSwitch, final Switch negotiablePriceSwitch) {
        petsLayout.setVisibility(View.GONE);
//images();
//Button rentBtn=(Button) findViewById(R.id.forRentBtn);
//Button saleBtn=(Button) findViewById(R.id.forSaleBtn);
//        rentBtn.setEnabled(false);
        petsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    petsLayout.setVisibility(View.VISIBLE);
                } else {
                    petsLayout.setVisibility(View.GONE);

                }

            }
        });
        database = FirebaseDatabase.getInstance();

        houses = database.getReference("home");
//
        houses.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                flatsNo= (int) dataSnapshot.getChildrenCount()+1;
                Log.e("nnnnnnn", String.valueOf(flatsNo));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        locateFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent(getApplicationContext() , MapsActivity.class);

                DatabaseReference users = database.getReference("users");
//                DatabaseReference regions = database.getReference("regions");

                users.child("userId/" + "houses/" + "owened/" + "houseId/"+flatsNo).setValue("");

                houses.child(flatsNo + "/bedRoomsNo/").setValue(noOfBedRoomsEditText.getText().toString());
                houses.child(flatsNo + "/bathNo/").setValue(noOfBathRoomsEditText.getText().toString());
                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/parking/").setValue(String.valueOf(parkingLotsSwitch.isChecked()));
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/livingRoom/").setValue(String.valueOf(livingRoomSwitch.isChecked()));
                houses.child(flatsNo + "/pets/").setValue("boolean");
                houses.child(flatsNo + "/kitchen/").setValue(String.valueOf(kitchenSwitch.isChecked()));
                houses.child(flatsNo + "/coolingSystem/").setValue(String.valueOf(coolingSystemSwitch.isChecked()));
                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();

                    }
                });
                houses.child(flatsNo + "/houseIdNo/" + "location/").setValue("");
//                Toast.makeText(getApplicationContext(),"sending",Toast.LENGTH_LONG).show();
                geoFire.setLocation("firebase-hq", new GeoLocation(latLng.latitude, latLng.longitude));

//                regions.child("contry/" + "city/" + flatsNo).setValue("location").addOnFailureListener((Activity) context, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context , "fail "+e.toString() , Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//                Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(latLng).title(priceEditText.getText().toString()).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.house5)));

                String PriceOnMarker=priceEditText.getText().toString();
            }
        });

    }

    public void rentOrSale(final Button rentBtn, final Button saleBtn){
        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentBtn.setEnabled(false);
                saleBtn.setEnabled(true);
//
                rentBtn.setTextColor(Integer.parseInt(String.valueOf(R.color.colorPrimary)));
                saleBtn.setTextColor(Integer.parseInt(String.valueOf(R.color.colorAccent)));
                saleBtn.setBackgroundColor(1);
                rentBtn.setBackgroundColor(Integer.parseInt(String.valueOf(R.color.colorAccent)));
//rentBtn.setBackgroundColor();
            }
        });
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saleBtn.setEnabled(false);
                rentBtn.setEnabled(true);
//
                saleBtn.setTextColor(Integer.parseInt(String.valueOf(R.color.colorPrimary)));
                rentBtn.setTextColor(Integer.parseInt(String.valueOf(R.color.colorAccent)));
                rentBtn.setBackgroundColor(1);
                saleBtn.setBackgroundColor(Integer.parseInt(String.valueOf(R.color.colorAccent)));

            }
        });

    }
    //public void images(){
//    ImageSliderImplementdMethods imageSliderImplementdMethods=new ImageSliderImplementdMethods();
////    imageSliderImplementdMethods.onPageScrolled();
//}
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));

    }}