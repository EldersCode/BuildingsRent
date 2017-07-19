package com.project.buildingsrent;

import android.os.Bundle;

public class LoginActivity extends HandlingLoginAuth {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HandlingLoginAuth(LoginActivity.this);

    }
}
