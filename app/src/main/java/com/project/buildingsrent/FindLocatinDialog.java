package com.project.buildingsrent;

import android.location.Geocoder;
import android.content.Context;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapbox.geocoder.GeocoderCriteria;
import com.mapbox.geocoder.MapboxGeocoder;

import java.util.Locale;

/**
 * Created by heshamsalama on 6/10/2017.
 */


public class FindLocatinDialog extends AppCompatActivity {
    AlertDialog dialog;
    private AlertDialog.Builder alertBuilder;
    private View view;
    Button here_btn, notHere_btn;
    LatLng latLng;
    Location myLocation;
    GoogleMap map;


    public FindLocatinDialog(final Context context ) {


        view = LayoutInflater.from(context).inflate(R.layout.location_find, null, true);

        here_btn = (Button) view.findViewById(R.id.here);
        notHere_btn = (Button) view.findViewById(R.id.notHere);

        here_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandlingMaps handlingMaps = new HandlingMaps();
//                handlingMaps.checkLocationPermission();

                if (handlingMaps.mMap != null) {
                    Location myLocation = handlingMaps.mMap.getMyLocation();
                    latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    handlingMaps.mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
                } else if (handlingMaps.mMap == null) {
//                   handlingMaps.mMap = map;

                    Toast.makeText(context, "Map is unready right now :P", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertBuilder= new AlertDialog.Builder(context);
        alertBuilder.setView(view);
        dialog = alertBuilder.create();
        dialog.show();
    }



}
