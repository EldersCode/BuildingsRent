package com.project.buildingsrent;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;


public class MapsActivity extends HandlingMaps{

    FloatingActionButton logoutFab;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetBehavior bottomSheetBehavior1;
    Button TheButtonInTheFirstButtonSheet;
    String buildingType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        onCreateHandle();

//        logout fab button handling

        logoutFab = (FloatingActionButton) findViewById(R.id.fabLogout);
        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth= FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    mAuth.signOut();
                    Toast.makeText(MapsActivity.this, "You have signed out successfully ..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext() , LoginActivity.class));
                    finish();

                } else if (currentUser == null) {
                    Toast.makeText(MapsActivity.this, "You haven't logged in ..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //////////////





        //showing the first bottom sheet by the first button

        TheButtonInTheFirstButtonSheet = (Button) findViewById(R.id.HomeButton);



        //Handling the bottom sheets

        View bottomSheet1 = findViewById(R.id.bottom_sheet1);
        View bottomSheet = findViewById(R.id.bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet1);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        /////////////////


        //handling the boom menu button

        int ImagesForTheMenu[] = new int[]{R.mipmap.gift, R.mipmap.stage, R.mipmap.user, R.mipmap.user};
        int TextForMenu[] = new int[]{R.string.SearchForAnApartment_Menu, R.string.CreateEvent_Menu, R.string.SetTheApartmentLocation, R.string.SetTheApartmentLocation};
        int HintTextForMenu[] = new int[]{R.string.SearchForAnApartmentHint_Menu, R.string.CreateEventHint_Menu, R.string.SetTheApartmentLocation_hint, R.string.SetTheApartmentLocation_hint};

        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);


        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final HamButton.Builder builder = new HamButton.Builder().listener(new OnBMClickListener() {


                @Override
                public void onBoomButtonClick(int index) {
                    if (index == 0) {

                    } else if (index == 1) {

                    } else if (index == 2) {
                        bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);


                        TheButtonInTheFirstButtonSheet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                buildingType="home";
                                bottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            }
                        });
                    } else if (index == 3) {

                    }

                }

        })
             .normalImageRes(ImagesForTheMenu[i])
                    .normalTextRes(TextForMenu[i])
                    .subNormalTextRes(HintTextForMenu[i]);


            bmb.addBuilder(builder);


            ///////////////////



        }
    }


    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        else{
            finishAffinity();
        }
    }


}
