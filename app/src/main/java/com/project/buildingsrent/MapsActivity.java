package com.project.buildingsrent;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.profile.activities.ProfileActivity;


public class MapsActivity extends HandlingMaps{

    FloatingActionButton logoutFab;
    BottomSheetBehavior homeBottomSheetBehavior;
    BottomSheetBehavior storeBottomSheetBehavior;
    BottomSheetBehavior landBottomSheetBehavior;
    BottomSheetBehavior hallBottomSheetBehavior;
    BottomSheetBehavior bottomSheetBehavior1;
    Button homeSheetBtn;
    Button storeSheetBtn;
    Button chaletSheetBtn;
    Button hallSheetBtn;
    Button landSheetBtn;
    String buildingType;
    boolean flag = true;
    LatLng latLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        onCreateHandle();
// declearing widgets for sheets
        sheetsWedgits();
        /////////////

        try{
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getApplicationContext(), "     Login Successfully\nWelcome "+ profile.getName() , Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }

//        logout fab button handling

        logoutFab = (FloatingActionButton) findViewById(R.id.fabLogout);
        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        //first check which type of accounts that logged in
                    // Facebook or Email and Password registration to logout it ..
                final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Notification")
                        .setMessage( "Do you want to Sign out ?")
                        .setCancelable(false)
                        .setIcon(R.mipmap.alarm);
                alert.setPositiveButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.setCancelable(true);
                    }
                });
                alert.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                    }
                });
                alert.create().show();


            }
        });

        //////////////

        homeSheetBtn = (Button) findViewById(R.id.HomeButton);
        hallSheetBtn = (Button) findViewById(R.id.Hall);
        chaletSheetBtn = (Button) findViewById(R.id.Chalet);
        landSheetBtn = (Button) findViewById(R.id.land);
        storeSheetBtn = (Button) findViewById(R.id.StoreButton);



        //Handling the bottom sheets

        View bottomSheet1 = findViewById(R.id.bottom_sheet1);
        View home_bottomSheet = findViewById(R.id.home_bottom_sheet);
        View store_bottomSheet = findViewById(R.id.store_bottom_sheet);
        View land_bottomSheet = findViewById(R.id.land_bottom_sheet);
        View hall_bottomSheet = findViewById(R.id.hall_bottom_sheet);

        homeBottomSheetBehavior = BottomSheetBehavior.from(home_bottomSheet);
        storeBottomSheetBehavior = BottomSheetBehavior.from(store_bottomSheet);
        landBottomSheetBehavior = BottomSheetBehavior.from(land_bottomSheet);
        hallBottomSheetBehavior = BottomSheetBehavior.from(hall_bottomSheet);
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1);
        //hide sheets on 1st creat
        homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        storeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        landBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hallBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        /////////////////


        //handling the boom menu button

        int ImagesForTheMenu[] = new int[]{R.mipmap.gift, R.mipmap.stage, R.mipmap.user, R.mipmap.user};
        int TextForMenu[] = new int[]{R.string.SearchForAnApartment_Menu, R.string.CreateEvent_Menu, R.string.SetTheApartmentLocation, R.string.SetTheApartmentLocation};
        int HintTextForMenu[] = new int[]{R.string.SearchForAnApartmentHint_Menu, R.string.CreateEventHint_Menu, R.string.SetTheApartmentLocation_hint, R.string.SetTheApartmentLocation_hint};

        final BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);


        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);

        // wheen a boom btn clicked

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder().listener(new OnBMClickListener() {


                @Override
                public void onBoomButtonClick(int index) {
                    if (index == 0) {

                        startActivity(new Intent(getApplicationContext() , ProfileActivity.class));

                    } else if (index == 1) {

                    }
                    // the 3rd boom btn oncklick for submetting a proprty
                    else if (index == 2) {

                    //buttom sheets activated and ready for submtting a proberty
                        //alertdialog for asking where is the property location

                        flag = false;
                            final FindLocatinDialog findLocatinDialog = new FindLocatinDialog(MapsActivity.this);

                        //if same location is the current location btn in the aleret dialog

                            findLocatinDialog.here_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {


                         //getting the current  latlong to be ready then pass it to the submetting class



                                            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                                            // get Location Data from latLng here \\

                                        DataFromLatLng dataFromLatLng = new DataFromLatLng(mLastLocation.getLatitude() , mLastLocation.getLongitude() , MapsActivity.this);


                                                            ////\\\\

                                            flag = true;
                                            findLocatinDialog.dialog.dismiss();

                                    }catch (Exception e){

                                        checkLocationDialog();
                                    }
                    //when the latlong is ready btn sheets activated
                                    if(flag){
//categories expantion
                                        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
////////////////
                                        //categories btn sheets behvior
categoriesBtnSheets();

                                    }

                                }
                            });



                            // here the code if user's apartment is not in the same place of him

                        findLocatinDialog.notHere_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                bmb.setVisibility(View.GONE);
                                logoutFab.setVisibility(View.GONE);
                                search_layout.setVisibility(View.VISIBLE);
                                findLocatinDialog.dialog.dismiss();


                                            // in case of finding the location of the user's apartment

                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(myLatLng == null){
                                            Toast.makeText(MapsActivity.this, "please choose your location first", Toast.LENGTH_SHORT).show();
                                        }else {
                                            // here what happen when we confirm a location after search

                                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this)
                                                    .setTitle("Notification")
                                                    .setMessage("Is this the location of your apartment ?")
                                                    .setCancelable(false)
                                                    .setIcon(R.mipmap.alarm);
                                            alert.setPositiveButton("no", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    alert.setCancelable(true);
                                                }
                                            });
                                            alert.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    search_editText.getText().clear();


                                                                      // get Location Data from latLng here \\

                                                    DataFromLatLng dataFromLatLng = new DataFromLatLng(myLatLng.latitude ,myLatLng.longitude , MapsActivity.this);

                                                                                    ///\\\\





                                                    //when the latLong is ready btn sheets activated

//                                                    new SubmitBuildingInfo(homeBottomSheetBehavior,MapsActivity.this, myLatLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
//                                                            , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch, NegotiablePriceSwitch);


                                                    bmb.setVisibility(View.VISIBLE);
                                                    logoutFab.setVisibility(View.VISIBLE);
                                                    search_layout.setVisibility(View.GONE);

                                                    bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

//                                                    homeSheetBtn.setOnClickListener(new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View view) {
//
//                                                            buildingType = "home";
//                                                            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
//                                                            homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                                                        }
//                                                    });
                                                    categoriesBtnSheets();
                                                }
                                            });
                                            alert.create().show();
                                        }

                                    }
                                });

                                        // if user canceled searching the findLocationDialog appears again

                                cancel_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        findLocatinDialog.dialog.show();
                                        bmb.setVisibility(View.VISIBLE);
                                        logoutFab.setVisibility(View.VISIBLE);
                                        search_layout.setVisibility(View.GONE);


                                    }
                                });

                                                /////////////////////////////////////////
                            }
                        });


                                ///////////////////////////////////////////////////////////


                    }

                }
            })

                    .normalImageRes(ImagesForTheMenu[i])
                    .normalTextRes(TextForMenu[i])
                    .subNormalTextRes(HintTextForMenu[i]);


            bmb.addBuilder(builder);
        }
    }




    @Override
    public void onBackPressed() {

         if(mRecyclerView.getVisibility() == View.VISIBLE){
            mRecyclerView.setVisibility(View.GONE);
        }

        else if(homeBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else if(bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else{


             final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this)
                     .setTitle("Notification")
                     .setMessage( "Do you want to Exit ?")
                     .setCancelable(false)
                     .setIcon(R.mipmap.alarm);
             alert.setPositiveButton("no", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     alert.setCancelable(true);
                 }
             });
             alert.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     finishAffinity();
                 }
             });
             alert.create().show();

        }
    }

    public void Logout(){
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                mAuth.signOut();
                Toast.makeText(MapsActivity.this, "You have signed out successfully ..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }catch (Exception e){

        }
        try {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }catch (Exception e){

        }

    }


public void categoriesBtnSheets(){

    homeSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType="home";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a home btn sheet selected
//                                                textSliderView =  new TextSliderView(MapsActivity.this);

            if (buildingType =="home") {
                new SubmitBuildingInfo(homeBottomSheetBehavior,MapsActivity.this, latLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch, NegotiablePriceSwitch);

            }


        }
    });
    storeSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType="home";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            storeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a home btn sheet selected
//                                                textSliderView =  new TextSliderView(MapsActivity.this);

            if (buildingType =="home") {
                new SubmitBuildingInfo(homeBottomSheetBehavior,MapsActivity.this, latLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch, NegotiablePriceSwitch);

            }


        }
    });
    landSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType="home";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            landBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a home btn sheet selected
//                                                textSliderView =  new TextSliderView(MapsActivity.this);

            if (buildingType =="home") {
                new SubmitBuildingInfo(homeBottomSheetBehavior,MapsActivity.this, latLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch, NegotiablePriceSwitch);

            }


        }
    });
    hallSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType="home";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            hallBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a home btn sheet selected
//                                                textSliderView =  new TextSliderView(MapsActivity.this);

            if (buildingType =="home") {
                new SubmitBuildingInfo(homeBottomSheetBehavior,MapsActivity.this, latLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch, NegotiablePriceSwitch);

            }


        }
    });
    //////////////////////
}

}
