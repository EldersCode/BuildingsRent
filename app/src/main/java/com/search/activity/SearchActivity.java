package com.search.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.buildingsrent.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends SearchHandling implements AdapterView.OnItemSelectedListener {

    private static EditText priceFrom;
    private static EditText priceTo;
    private static EditText areaFrom;
    private static EditText areaTo;

    public static String getSharedPrefsSearchType() {
        return SharedPrefsSearchType;
    }

    final static String SharedPrefsSearchType = "SEARCH TYPE";

    public static String getPriceFrom() {
        return priceFrom.getText().toString();
    }


    public static String getPriceTo() {
        return priceTo.getText().toString();
    }



    public static String getAreaFrom() {
        return areaFrom.getText().toString();
    }



    public static String getAreaTo() {
        return areaTo.getText().toString();
    }



    public static String getCategorieItem() {
        return CategorieItem;
    }

    public void setCategorieItem(String categorieItem) {
        CategorieItem = categorieItem;
    }

  static  String CategorieItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        onCreateHandle(SearchActivity.this);
        priceFrom=(EditText)findViewById(R.id.priceFrom);
        priceTo=(EditText)findViewById(R.id.priceTo);
        areaFrom=(EditText)findViewById(R.id.areaFrom);
        areaTo=(EditText)findViewById(R.id.areaTo);
setContext(SearchActivity.this);
        //categorie spiner search
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner searchSpinner = (Spinner) findViewById(R.id.spinner2);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("home");
        categories.add("chalet");
        categories.add("hall");
        categories.add("store");
        categories.add("land");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        /////////////////////////get categorie end

                            // search type spinner here \\

        // Spinner Drop down elements
        List<String> searchList = new ArrayList<String>();
        searchList.add("city");
        searchList.add("area");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchList);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        searchSpinner.setAdapter(dataAdapter2);

        Log.i("Spinner 1 : " , spinner.getSelectedItem().toString());
        Log.i("Spinner 2 : " , searchSpinner.getSelectedItem().toString());

                                  // default \\


                                      //\\

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefsSearchType , 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String myType = parent.getItemAtPosition(position).toString();
                Log.i("search Spinner : ", myType);
                editor.putString("myType" , myType);
                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            // On selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
            Log.i("category Spinner ", item);
            // sending categorie to searchHandling class for felteration
            setCategorieItem(item);





    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
