package com.profile.activities;

import android.os.Bundle;

import com.project.buildingsrent.R;

public class EditProfileActivity extends EditProfileHandling {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        EditProfileHandlingMain(EditProfileActivity.this);
    }
}
