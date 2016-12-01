package com.meetdesk.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.activity.MainActivity;
import com.meetdesk.controller.ControllerAuthentication;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/2/16.
 */
public class FragmentAuthSignIn extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_AUTH_SIGNIN = "tag:fragment-auth-signin";

    UIButton buttonSignIn, buttonCreateNew;
    ImageView imageviewBG;
    LazyImageLoader imageLoader;
    LoginButton btnLoginFB;
    CallbackManager callbackManager;
    UIEditText editUsername, editPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_auth_signin, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            iFragment = (HelperGeneral.FragmentInterface) this.activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(activity != null)
        {
            init();
        }
    }

    private void init()
    {
        imageLoader = new LazyImageLoader(activity);
        callbackManager = CallbackManager.Factory.create();
        buttonSignIn = (UIButton) activity.findViewById(R.id.auth_signin_send);
        buttonCreateNew = (UIButton) activity.findViewById(R.id.auth_signin_create);
        imageviewBG = (ImageView) activity.findViewById(R.id.auth_signin_bg);
        btnLoginFB = (LoginButton) activity.findViewById(R.id.auth_signin_button_facebook);
        editUsername = (UIEditText) activity.findViewById(R.id.auth_signin_username);
        editPassword = (UIEditText) activity.findViewById(R.id.auth_signin_password);

        btnLoginFB.setReadPermissions("email");
        btnLoginFB.setFragment(FragmentAuthSignIn.this);
        btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(activity, "Login FB Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, "ERROR : " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setPreferencesLoggedIn();
                doLogin();
            }
        });
        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<String, String>();
                FragmentAuthSignUp signUp = new FragmentAuthSignUp();
                iFragment.onNavigate(signUp, param);
            }
        });
        imageLoader.showImage("drawable://" + R.drawable.bg_auth, imageviewBG);
    }

    private void setPreferencesLoggedIn()
    {
        PrefAuthentication authPreferences = new PrefAuthentication(activity);
        authPreferences.setIsLoggedIn(true);
        authPreferences.commit();
    }

    private void doLogin()
    {
        new AsyncTask<Void, Integer, String>()
        {

            ProgressDialog dialog;
            boolean success = false;
            String msg, username, password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setCancelable(false);
                dialog.show();

                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
            }

            @Override
            protected String doInBackground(Void... params) {
                String[] field = new String[]{"token", "username", "password"};
                String[] value = new String[]{HelperGeneral.getDeviceID(activity), username, password};
                ControllerAuthentication authentication = new ControllerAuthentication(activity);
                authentication.setParameter(field, value);
                authentication.doLogin();
                if(authentication.getSuccess())
                {
                    success = true;
                }
                msg = authentication.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(success)
                {
                    Intent i = new Intent(activity, MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
}
