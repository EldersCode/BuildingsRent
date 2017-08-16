package com.project.buildingsrent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import static com.project.buildingsrent.HandlingMaps.mDemoSlider;
import static com.project.buildingsrent.HandlingMaps.textSliderView;


/**
 * Created by heshamsalama on 6/16/2017.
 */

public class SubmitBuildingInfo extends Activity {
    //private DatabaseReference firebaseDatabase;
    private FirebaseDatabase database;
    private ImageView cameraImg;
    private int flatsNo =1;
    private String adress;
    private DatabaseReference houses;
    ////////////////////////////////
    DatabaseReference ref ;
    private DatabaseReference  users;

    GeoFire geoFire;
    public SubmitBuildingInfo() {


    }

    public SubmitBuildingInfo(final EditText descriptionEditText,
                              final BottomSheetBehavior bottomSheetBehavior1, final Context context,
                              final LatLng latLng, final GoogleMap mMap, final String building, final Button locateFlat,
                              final LinearLayout petsLayout, final Switch petsSwitch, final EditText priceEditText,
                              final EditText apartmentAreaEditText, final EditText noOfBedRoomsEditText,
                              final EditText noOfBathRoomsEditText, final Switch parkingLotsSwitch, final Switch livingRoomSwitch,
                              final Switch kitchenSwitch, final Switch coolingSystemSwitch, final Switch negotiablePriceSwitch,
                              final CheckBox farmLand, final CheckBox buildLand) {

        petsLayout.setVisibility(View.GONE);
//Button rentBtn=(Button) findViewById(R.id.forRentBtn);
//Button saleBtn=(Button) findViewById(R.id.forSaleBtn);
//        rentBtn.setEnabled(false);

        /////geocoding get country ,city and region
        DataFromLatLng dataFromLatLng=new DataFromLatLng(latLng.latitude,latLng.longitude,context);
       String country= dataFromLatLng.getMyCountry();
        String city=dataFromLatLng.getMyCity();
        String area=dataFromLatLng.getMyArea();
       adress=country+"/"+city+"/"+area+"/"  ;
        //////////////////////



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

        houses = database.getReference(adress+building);
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




                submitType( descriptionEditText,bottomSheetBehavior1,context,
                latLng,  mMap,building,locateFlat,petsLayout, petsSwitch, priceEditText,apartmentAreaEditText,  noOfBedRoomsEditText,  noOfBathRoomsEditText,  parkingLotsSwitch,livingRoomSwitch,kitchenSwitch,coolingSystemSwitch,negotiablePriceSwitch,
                        farmLand,buildLand);
                ref = FirebaseDatabase.getInstance().getReference(adress+building+"/"+ flatsNo+"/location");

                geoFire = new GeoFire(ref);
                geoFire.setLocation("firebase-hq", new GeoLocation(latLng.latitude, latLng.longitude));

                //after submetting clear slider and sheet colapse
                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
//              refresh maps activity after submetting
                Activity a=    (Activity)  context;

                final Intent intent = a.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.finish();
                a.overridePendingTransition(0, 0);
                a.startActivity(intent);
                a.overridePendingTransition(0, 0);
                ////////////////////////////////

                /////////////////////////////////////
                try {
                    if (MapsActivity.myDefaultMarker.getTitle().equals("here")) {
                        MapsActivity.myDefaultMarker.remove();
                    }
                }catch (Exception e){

                }
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

    @Override//
    public void onBackPressed() {//
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));//

    }//
//    public static void deleteCache(Context context) {//
//        try {//
//            File dir = context.getCacheDir();//
//            deleteDir(dir);//
//        } catch (Exception e) {}//
//    }//
//
//    public static boolean deleteDir(File dir) {//
//        if (dir != null && dir.isDirectory()) {//
//            String[] children = dir.list();//
//            for (int i = 0; i < children.length; i++) {//
//                boolean success = deleteDir(new File(dir, children[i]));//
//                if (!success) {//
//                    return false;//
//                }//
//            }//
//            return dir.delete();//
//        } else if(dir!= null && dir.isFile()) {//
//            return dir.delete();//
//        } else {//
//            return false;//
//        }//
//    }//

    public void submitType(EditText descriptionEditText,final BottomSheetBehavior bottomSheetBehavior1, final Context context, final LatLng latLng, final GoogleMap mMap, final String building, Button locateFlat, final LinearLayout petsLayout, Switch petsSwitch, final EditText priceEditText, final EditText apartmentAreaEditText, final EditText noOfBedRoomsEditText, final EditText noOfBathRoomsEditText, final Switch parkingLotsSwitch, final Switch livingRoomSwitch, final Switch kitchenSwitch, final Switch coolingSystemSwitch, final Switch negotiablePriceSwitch
    , CheckBox farmLand,CheckBox buildLand) {
       Toast.makeText(context,building,Toast.LENGTH_SHORT).show();
        switch (building){
            case "home":
            {
                users = database.getReference("users/"+me());

                users.child("userId/" + building + "/owened/" + "houseId/"+flatsNo).setValue(flatsNo);

                houses.child(flatsNo + "/bedRoomsNo/").setValue(noOfBedRoomsEditText.getText().toString());
                houses.child(flatsNo + "/bathNo/").setValue(noOfBathRoomsEditText.getText().toString());
                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/descriptionEditText/").setValue(descriptionEditText.getText().toString());
                houses.child(flatsNo + "/livingRoom/").setValue(String.valueOf(livingRoomSwitch.isChecked()));
                houses.child(flatsNo + "/pets/").setValue("boolean");
                houses.child(flatsNo + "/kitchen/").setValue(String.valueOf(kitchenSwitch.isChecked()));
                houses.child(flatsNo + "/coolingSystem/").setValue(String.valueOf(coolingSystemSwitch.isChecked()));
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/parking/").setValue(String.valueOf(parkingLotsSwitch.isChecked()));

                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();

                    }
                });
break;
            }
            //type hall
            case "hall":{
                users = database.getReference("users/"+me());

                users.child("userId/" + building + "/owened/" + "houseId/"+flatsNo).setValue(flatsNo);
                houses.child(flatsNo + "/noOfSeats/").setValue(noOfBedRoomsEditText.getText().toString());
                houses.child(flatsNo + "/buffet/").setValue(String.valueOf(kitchenSwitch.isChecked()));
                houses.child(flatsNo + "/coolingSystem/").setValue(String.valueOf(coolingSystemSwitch.isChecked()));
                houses.child(flatsNo + "/parking/").setValue(String.valueOf(parkingLotsSwitch.isChecked()));
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/descriptionEditText/").setValue(descriptionEditText.getText().toString());
                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();
                    }

                });
      break;
            }
            //type store
            case "store":{
                users = database.getReference("users/"+me());

                users.child("userId/" + building + "/owened/" + "houseId/"+flatsNo).setValue(flatsNo);

                houses.child(flatsNo + "/descriptionEditText/").setValue(descriptionEditText.getText().toString());
                houses.child(flatsNo + "/coolingSystem/").setValue(String.valueOf(coolingSystemSwitch.isChecked()));
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/parking/").setValue(String.valueOf(parkingLotsSwitch.isChecked()));
                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();

                    }

                });
            break;
            }
            //type chalet
            case "chalet" :{
                users = database.getReference("users/"+me());
                Toast.makeText(context , ".." , Toast.LENGTH_SHORT).show();

                users.child("userId/" + building + "/owened/" + "houseId/"+flatsNo).setValue(flatsNo);

                houses.child(flatsNo + "/bedRoomsNo/").setValue(noOfBedRoomsEditText.getText().toString());
                houses.child(flatsNo + "/bathNo/").setValue(noOfBathRoomsEditText.getText().toString());
                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/descriptionEditText/").setValue(descriptionEditText.getText().toString());
                houses.child(flatsNo + "/livingRoom/").setValue(String.valueOf(livingRoomSwitch.isChecked()));
                houses.child(flatsNo + "/pets/").setValue("boolean");
                houses.child(flatsNo + "/kitchen/").setValue(String.valueOf(kitchenSwitch.isChecked()));
                houses.child(flatsNo + "/coolingSystem/").setValue(String.valueOf(coolingSystemSwitch.isChecked()));
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/parking/").setValue(String.valueOf(parkingLotsSwitch.isChecked()));

                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();

                    }
                });
                break;
            }
            //type land
            case "land":{
                users = database.getReference("users/"+me());
                users.child("userId/" + building + "owened/" + "houseId/"+flatsNo).setValue(flatsNo);

                houses.child(flatsNo + "/price/").setValue(priceEditText.getText().toString());
                houses.child(flatsNo + "/descriptionEditText/").setValue(descriptionEditText.getText().toString());
                houses.child(flatsNo + "/negotiablePrice/").setValue(String.valueOf(negotiablePriceSwitch.isChecked()));
                houses.child(flatsNo + "/farm/").setValue(String.valueOf(farmLand.isChecked()));
                houses.child(flatsNo + "/build/").setValue(String.valueOf(buildLand.isChecked()));

                houses.child(flatsNo + "/area/").setValue(apartmentAreaEditText.getText().toString()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context , "Data Sent Successfully .." , Toast.LENGTH_SHORT).show();

                    }

                });
            break;
            }
        }
    }
protected  String me(){
    // getting ID of the user that logged in \\

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
//    Toast.makeText(context, userId, Toast.LENGTH_SHORT).show();

return  userId;}


}