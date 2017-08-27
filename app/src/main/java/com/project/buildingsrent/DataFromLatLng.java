package com.project.buildingsrent;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hesham on 8/5/2017.
 */

public class DataFromLatLng extends Activity{
private String myCountry;
private String myCity;
private String myArea;

    public String getMyCountry() {
        return myCountry;
    }

    public void setMyCountry(String myCountry) {
        this.myCountry = myCountry;
    }

    public String getMyCity() {
        return myCity;
    }

    public void setMyCity(String myCity) {
        this.myCity = myCity;
    }

    public String getMyArea() {
        return myArea;
    }

    public void setMyArea(String myArea) {
        this.myArea = myArea;
    }

    public DataFromLatLng(Double latitude , Double longitude , Context context) {

    Geocoder geocoder;
    List<Address> addresses;


             geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            addresses =geocoder.getFromLocation(latitude,longitude,1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                           // the full address \\

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                               // the area \\

            String area = addresses.get(0).getLocality();

                               // the city \\

            String city = addresses.get(0).getAdminArea();

                              // the country \\

            String country = addresses.get(0).getCountryName();
setMyCountry(country);
            setMyCity(city);
            setMyArea(area);
            Log.i("location address : " , "address : "+ address);
            Log.i("location area : " , "area : "+ area);
            Log.i("location city : " , "city : "+ city);
            Log.i("location country : " , "country : "+ country);




        } catch (IOException e) {
            e.printStackTrace();
        }


}

}
