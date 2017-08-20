package com.project.buildingsrent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

/**
 * Created by Hesham on 8/17/2017.
 */

public class HandlingAdvertise extends Activity {

    Button callBtn;


    public void HandlingAdvertise(Context context) {

        InitializeViews();

    }

    public void InitializeViews() {

        callBtn = (Button) findViewById(R.id.call_btn);

    }





}
