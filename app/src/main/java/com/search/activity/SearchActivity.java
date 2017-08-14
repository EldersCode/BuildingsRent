package com.search.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.buildingsrent.R;

public class SearchActivity extends SearchHandling {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        onCreateHandle();
    }
}
