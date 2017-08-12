package com.profile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.buildingsrent.R;

public class ProfileActivity extends ProfileHandling {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileHandlingMain(ProfileActivity.this);

    }
}
