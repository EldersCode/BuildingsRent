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
import com.search.activity.SearchActivity;


public class MapsActivity extends HandlingMaps{

    FloatingActionButton logoutFab;
    BottomSheetBehavior homeBottomSheetBehavior;
    BottomSheetBehavior storeBottomSheetBehavior;
    BottomSheetBehavior landBottomSheetBehavior;
    BottomSheetBehavior hallBottomSheetBehavior;
    BottomSheetBehavior chaletBottomSheetBehavior;
    BottomSheetBehavior bottomSheetBehavior1;
    Button homeSheetBtn;
    Button storeSheetBtn;
    Button chaletSheetBtn;
    Button hallSheetBtn;
    Button landSheetBtn;
    String buildingType;
    boolean flag = true;
    LatLng latLng;
    private Button rentBtnAppartment , saleBtnAppartment
                  , chaletRentBtn    , chaletsaleBtn
                  , hallRentBtn      , hallSaleBtn
                  , landRentBtn      , landSaleBtn
                  , storeRentBtn      , storeSaleBtn  ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        onCreateHandle();
        mRecyclerView.setVisibility(View.GONE);
    // declearing widgets for sheets
        sheetsWedgits();
        /////////////
        handleRentSaleBtns();

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
        View chalet_bottomSheet = findViewById(R.id.chalet_bottom_sheet);

        homeBottomSheetBehavior = BottomSheetBehavior.from(home_bottomSheet);
        storeBottomSheetBehavior = BottomSheetBehavior.from(store_bottomSheet);
        landBottomSheetBehavior = BottomSheetBehavior.from(land_bottomSheet);
        hallBottomSheetBehavior = BottomSheetBehavior.from(hall_bottomSheet);
       chaletBottomSheetBehavior = BottomSheetBehavior.from(chalet_bottomSheet);
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1);
        //hide sheets on 1st creat
        homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        storeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        landBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hallBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        chaletBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        /////////////////


        //handling the boom menu button

        int ImagesForTheMenu[] = new int[]{R.mipmap.user, R.mipmap.gift, R.mipmap.stage};
        int TextForMenu[] = new int[]{R.string.profile_menu , R.string.SearchForAnApartment_Menu, R.string.setYourAdvertisement, R.string.setYourAdvertisement};
        int HintTextForMenu[] = new int[]{R.string.SearchForAnApartmentHint_Menu, R.string.CreateEventHint_Menu, R.string.SetTheApartmentLocation_hint};

        final BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);


        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);

        // wheen a boom btn clicked

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder().listener(new OnBMClickListener() {


                @Override
                public void onBoomButtonClick(int index) {
                    if (index == 0) {

                        startActivity(new Intent(getApplicationContext() , ProfileActivity.class));

                    } else if (index == 1) {
                                             // go to activity_search activity \\
                        startActivity(new Intent(getApplicationContext() , SearchActivity.class));

//                                    // getting ID of the user that logged in \\
//
//                        FirebaseAuth mAuth= FirebaseAuth.getInstance();
//                        FirebaseUser currentUser = mAuth.getCurrentUser();
//                        String userId = currentUser.getUid();
//                        Toast.makeText(MapsActivity.this, userId, Toast.LENGTH_SHORT).show();

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
                                            // here what happen when we confirm a location after activity_search

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

//

                                                    bmb.setVisibility(View.VISIBLE);
                                                    logoutFab.setVisibility(View.VISIBLE);
                                                    search_layout.setVisibility(View.GONE);

                                                    bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

//
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

         else if(bottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_EXPANDED) {
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

         }

        else if(homeBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

         }
        else if(chaletBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

            chaletBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

         }
         else if(hallBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

             hallBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

         }
         else if(landBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

             landBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

         }
         else if(storeBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

             storeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
             bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

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


public void categoriesBtnSheets() {

    homeSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType = "home";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            homeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a home btn sheet selected

            if (buildingType.equals("home") && myLatLng == null) {
                new SubmitBuildingInfo(descriptionEditText, homeBottomSheetBehavior, MapsActivity.this, latLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch,
                        NegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            } else if (buildingType.equals("home") && latLng == null) {
                new SubmitBuildingInfo(descriptionEditText, homeBottomSheetBehavior, MapsActivity.this, myLatLng, mMap, buildingType, locateFlat, petsLayout, petsSwitch, priceEditText, ApartmentAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch,
                        NegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);
            }

        }
    });
    storeSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType = "store";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            storeBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a store btn sheet selected

            if (buildingType.equals("store") && myLatLng == null) {
                new SubmitBuildingInfo(storeDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this, latLng, mMap, buildingType, submit_store, petsLayout, petsSwitch, storePriceEditText, storeAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText,
                        storeParkingLotsSwitch,
                        LivingRoomSwitch, KitchenSwitch, storeCoolingSystemSwitch, storeNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            } else if (buildingType.equals("store") && latLng == null) {
                new SubmitBuildingInfo(storeDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this, myLatLng, mMap, buildingType, submit_store, petsLayout, petsSwitch, storePriceEditText, storeAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText,
                        storeParkingLotsSwitch,
                        LivingRoomSwitch, KitchenSwitch, storeCoolingSystemSwitch, storeNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            }


        }
    });
    landSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType = "land";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            landBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a land btn sheet selected

            if (buildingType.equals("land") && myLatLng == null) {
                new SubmitBuildingInfo(landDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this, latLng, mMap,
                        buildingType, landSubmit, petsLayout, petsSwitch, landPriceEditText, landAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch,
                        landNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);


            } else if (buildingType.equals("land") && latLng == null) {
                new SubmitBuildingInfo(landDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this, myLatLng, mMap,
                        buildingType, landSubmit, petsLayout, petsSwitch, landPriceEditText, landAreaEditText, noOfBedRoomsEditText
                        , noOfBathRoomsEditText, parkingLotsSwitch, LivingRoomSwitch, KitchenSwitch, coolingSystemSwitch,
                        landNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            }


        }
    });
    hallSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType = "hall";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            hallBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a hall btn sheet selected

            if (buildingType.equals("hall") && myLatLng == null) {
                new SubmitBuildingInfo(hallDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this,
                        latLng, mMap, buildingType, hallSubmit,
                        petsLayout, petsSwitch, hallPriceEditText, hallAreaEditText, noOfSeatsEditText
                        , noOfBathRoomsEditText,
                        hallParkingLotsSwitch, LivingRoomSwitch, hallBuffetSwitch, hallCoolingSystemSwitch,
                        hallNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            } else if (buildingType.equals("hall") && latLng == null) {
                new SubmitBuildingInfo(hallDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this,
                        myLatLng, mMap, buildingType, hallSubmit,
                        petsLayout, petsSwitch, hallPriceEditText, hallAreaEditText, noOfSeatsEditText
                        , noOfBathRoomsEditText,
                        hallParkingLotsSwitch, LivingRoomSwitch, hallBuffetSwitch, hallCoolingSystemSwitch,
                        hallNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            }


        }
    });
    //////////////////////
    chaletSheetBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            buildingType = "chalet";
            bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

            chaletBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //if a chalete btn sheet selected


            if (buildingType.equals("chalet") && myLatLng == null) {
                new SubmitBuildingInfo(chaletDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this,
                        latLng, mMap, buildingType, chaletSubmit, petsLayout, petsSwitch, chaletPriceEditText
                        , chaletAreaEditText, chaletNoOfBedRoomsEditText
                        , chaletNoOfBathRoomsEditText, chaletParkingLotsSwitch, chaletLivingRoomSwitch, chaletKitchenSwitch,
                        chaletCoolingSystemSwitch, chaletNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            } else if (buildingType.equals("chalet") && latLng == null) {

                new SubmitBuildingInfo(chaletDescriptionEditText, homeBottomSheetBehavior, MapsActivity.this,
                        myLatLng, mMap, buildingType, chaletSubmit, petsLayout, petsSwitch, chaletPriceEditText
                        , chaletAreaEditText, chaletNoOfBedRoomsEditText
                        , chaletNoOfBathRoomsEditText, chaletParkingLotsSwitch, chaletLivingRoomSwitch, chaletKitchenSwitch,
                        chaletCoolingSystemSwitch, chaletNegotiablePriceSwitch, landCheckBoxFarm, landCheckBoxbuild);

            }


        }
    });
    //////////////////////
}

    public void handleRentSaleBtns(){


        rentBtnAppartment = (Button) findViewById(R.id.forRentBtnApp);
        saleBtnAppartment = (Button) findViewById(R.id.forSaleBtnApp);
        chaletRentBtn     = (Button) findViewById(R.id.chaletForRentBtn);
        chaletsaleBtn     = (Button) findViewById(R.id.chaletForSaleBtn);
        hallRentBtn       = (Button) findViewById(R.id.hallForRentBtn);
        hallSaleBtn       = (Button) findViewById(R.id.hallForSaleBtn);
        landRentBtn       = (Button) findViewById(R.id.landForRentBtn);
        landSaleBtn       = (Button) findViewById(R.id.landForSaleBtn);
        storeRentBtn      = (Button) findViewById(R.id.storeForRentBtn);
        storeSaleBtn      = (Button) findViewById(R.id.storeForSaleBtn);

                 // performing functions appartment btns here \\


        rentBtnAppartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rentBtnAppartment.setClickable(false);
                rentBtnAppartment.setBackgroundResource(R.drawable.btn_design);
                saleBtnAppartment.setClickable(true);
                saleBtnAppartment.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });


        saleBtnAppartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saleBtnAppartment.setClickable(false);
                saleBtnAppartment.setBackgroundResource(R.drawable.btn_design);
                rentBtnAppartment.setClickable(true);
                rentBtnAppartment.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });

        rentBtnAppartment.performClick();

                                  ///\\\

                    // performing functions chalet btns here \\


        chaletRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chaletRentBtn.setClickable(false);
                chaletRentBtn.setBackgroundResource(R.drawable.btn_design);
                chaletsaleBtn.setClickable(true);
                chaletsaleBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });


        chaletsaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chaletsaleBtn.setClickable(false);
                chaletsaleBtn.setBackgroundResource(R.drawable.btn_design);
                chaletRentBtn.setClickable(true);
                chaletRentBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });

        chaletRentBtn.performClick();

                                       ///\\\


                        // performing functions hall btns here \\


        hallRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hallRentBtn.setClickable(false);
                hallRentBtn.setBackgroundResource(R.drawable.btn_design);
                hallSaleBtn.setClickable(true);
                hallSaleBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });


        hallSaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hallSaleBtn.setClickable(false);
                hallSaleBtn.setBackgroundResource(R.drawable.btn_design);
                hallRentBtn.setClickable(true);
                hallRentBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });

        hallRentBtn.performClick();

                                           ///\\\


                          // performing functions land btns here \\


        landRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                landRentBtn.setClickable(false);
                landRentBtn.setBackgroundResource(R.drawable.btn_design);
                landSaleBtn.setClickable(true);
                landSaleBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });


        landSaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                landSaleBtn.setClickable(false);
                landSaleBtn.setBackgroundResource(R.drawable.btn_design);
                landRentBtn.setClickable(true);
                landRentBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });

        landRentBtn.performClick();

                                             ///\\\

                           // performing functions store btns here \\


        storeRentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeRentBtn.setClickable(false);
                storeRentBtn.setBackgroundResource(R.drawable.btn_design);
                storeSaleBtn.setClickable(true);
                storeSaleBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });


        storeSaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeSaleBtn.setClickable(false);
                storeSaleBtn.setBackgroundResource(R.drawable.btn_design);
                storeRentBtn.setClickable(true);
                storeRentBtn.setBackgroundResource(R.drawable.btn_design_clicked);

            }
        });

        storeRentBtn.performClick();

                                          ///\\\




    }

}


