package com.project.buildingsrent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

public class AdvertiseActivity extends HandlingAdvertise implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    TextView advPrice,advArea,description,bedRooms,bathRooms,features , phone;
    String phoneNum = "";
    public FirebaseStorage mStorage;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        HandlingAdvertise(AdvertiseActivity.this);
        functions(AdvertiseActivity.this);
///////////////
        mDemoSlider = (SliderLayout)findViewById(R.id.advSlider);

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        ////////////
        advPrice=(TextView)findViewById(R.id.advPrice);
        advArea=(TextView)findViewById(R.id.areaAdv);
        description=(TextView)findViewById(R.id.advDescription);
        bedRooms=(TextView)findViewById(R.id.bedroomsNum);
        bathRooms=(TextView)findViewById(R.id.bathroomsNum);
        features=(TextView)findViewById(R.id.advFeatures);
        phone =(TextView) findViewById(R.id.phoneAdv);


        try {
            mStorage= FirebaseStorage.getInstance();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String data = preferences.getString("data", "");
            Log.e("dddddddd", data);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference(data);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String coolingSystem = (String) dataSnapshot.child("coolingSystem").getValue();
                    try {
//                        ArrayList<String> imgs = new ArrayList<String>();
//                        dataSnapshot.child("images").getChildren();
 DatabaseReference imgRef=database.getReference(data+"/images/") ;
                        Log.e("imrg", data+"/images/");

                        imgRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot img : dataSnapshot.getChildren()) {
                                    //instances retrived
                                    String im = (String) img.getValue();

                                    StorageReference storageRef = mStorage.getReferenceFromUrl(im);

                                    String url = storageRef.getBucket();


//                                 RequestCreator f=   Picasso.with(AdvertiseActivity.this).load(url)
//                                            .error(R.mipmap.ic_launcher);


                                    Log.e("url", url);
                                    Log.e("img", im);
                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                             TextSliderView textSliderView = new TextSliderView(AdvertiseActivity.this);

                                            textSliderView
                                                    .image(String.valueOf(uri))


                                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                                    .setOnSliderClickListener(AdvertiseActivity.this);

                                            mDemoSlider.addSlider(textSliderView);                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors


                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }catch (Exception e){
                        Log.e("ddddd", String.valueOf(e));

                    }
                    String area = (String) dataSnapshot.child("area").getValue();
                    final String price = (String) dataSnapshot.child("price").getValue();
                    String descriptionS = (String) dataSnapshot.child("descriptionEditText").getValue();
                    String bedRoomsNo = (String) dataSnapshot.child("bedRoomsNo").getValue();
                    String bathNo = (String) dataSnapshot.child("bathNo").getValue();
                    String kitchen = (String) dataSnapshot.child("kitchen").getValue();
                    String livingRoom = (String) dataSnapshot.child("livingRoom").getValue();
                    String negotiablePrice = (String) dataSnapshot.child("negotiablePrice").getValue();
                    String parking = (String) dataSnapshot.child("parking").getValue();

                    try {
                        phoneNum = (String) dataSnapshot.child("phone number").getValue();
                    } catch (Exception e) {

                    }

                    if (coolingSystem.equals("true")) {
                        features.setText(features.getText() + "\n cooling System");
                    }
                    if (parking.equals("true")) {
                        features.setText(features.getText().toString() + "\n parking");
                    }
                    if (negotiablePrice.equals("true")) {
                        features.setText(features.getText().toString() + "\n Negotiable Price");
                    }
                    if (livingRoom.equals("true")) {
                        features.setText(features.getText().toString() + "\n LivingRoom");
                    }
                    if (kitchen.equals("true")) {
                        features.setText(features.getText().toString() + "\n kitchen");
                    }
                    if (bathNo != null) {
                        bathRooms.setText(bathNo);
                    }
                    if (bedRooms != null) {
                        bedRooms.setText(bedRoomsNo);
                    }
                    if (descriptionS != null) {
                        description.setText(descriptionS);
                    }
                    if (area != null) {
                        advArea.setText(area);
                    }
                    if (price != null) {
                        advPrice.setText(price);
                    }
                    if (phoneNum != null) {
                        phone.setText(phoneNum);
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e ){

        }
    }

    public void functions(final Context context) {


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle(R.string.call)
                        .setCancelable(true)
                        .setMessage(phone.getText().toString() + " ?")
                        .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    startActivity(intent);
                                }catch (Exception e){

                                }
                            }
                        });
                        alert.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.setCancelable(true);
                            }
                        });
                alert.create().show();


            }
        });

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

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
}
