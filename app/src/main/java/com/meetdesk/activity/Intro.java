package com.meetdesk.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import com.meetdesk.BaseActivity;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerSetup;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;

public class Intro extends BaseActivity {

    boolean isLoggedIn = false, showSelectType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        launch();
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
                }
                startActivity(i);
                finish();

            }
        }.execute();
    }
}
