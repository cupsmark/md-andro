package com.meetdesk.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.meetdesk.BaseActivity;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerSetup;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;

import io.fabric.sdk.android.Fabric;

public class Intro extends BaseActivity {

    boolean isLoggedIn = false, showSelectType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPermissionReadPhoneState();
    }

    private void launch()
    {
        new AsyncTask<Void, Integer, String>()
        {

            boolean success = false;
            String msg = "";

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                PrefAuthentication authPreferences = new PrefAuthentication(Intro.this);
                showSelectType = authPreferences.getDisplayingSelectType();
                isLoggedIn = authPreferences.getLoggedIn();
                if(authPreferences.getKeyUserToken().equals(""))
                {
                    authPreferences.setKeyUserToken(HelperGeneral.getDeviceID(Intro.this));
                }

                ControllerSetup setup = new ControllerSetup(Intro.this);
                setup.setL(50);
                setup.setO(0);
                setup.setToken(HelperGeneral.getDeviceID(Intro.this));
                setup.getRegion();

                ControllerSetup setup2 = new ControllerSetup(Intro.this);
                setup2.setL(50);
                setup2.setO(0);
                setup2.setToken(HelperGeneral.getDeviceID(Intro.this));
                setup2.getProcat();

                if(!setup.getSuccess() && !setup2.getSuccess())
                {
                    success = false;
                    msg = setup.getMessage();
                }
                SystemClock.sleep(3000);
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Intent i = null;
                if(isLoggedIn)
                {
                    i = new Intent(Intro.this, MainActivity.class);
                }
                else
                {
                    i = new Intent(Intro.this, ActivityAuth.class);
                    i.putExtra("isLogout", false);
                }
                startActivity(i);
                finish();

            }
        }.execute();
    }

    private void getPermissionReadPhoneState()
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(ContextCompat.checkSelfPermission(Intro.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            {
                launch();
            }
            else
            {
                ActivityCompat.requestPermissions(Intro.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 112);
            }
        }
        else
        {
            launch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 112:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    launch();
                }
                else
                {
                    Toast.makeText(Intro.this, "No Allowed Permission", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        }
    }
}
