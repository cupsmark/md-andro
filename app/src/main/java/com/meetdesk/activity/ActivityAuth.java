package com.meetdesk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.fragment.FragmentAuthSelectType;
import com.meetdesk.fragment.FragmentAuthSignIn;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.view.UIDialogConfirm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.facebook.FacebookSdk;

public class ActivityAuth extends BaseActivity{

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMenu().setSlidingEnabled(false);
        addFragmentLogin();
    }

    private void addFragmentLogin()
    {
        Map<String, String> param = new HashMap<String, String>();
        FragmentAuthSignIn login = new FragmentAuthSignIn();
        onNavigate(login, param);
    }

    private void addFragmentSelect()
    {
        Map<String, String> param = new HashMap<String, String>();
        FragmentAuthSelectType authSelectType = new FragmentAuthSelectType();
        onNavigate(authSelectType, param);
    }

    @Override
    public void onNavigate(BaseFragment fragmentSrc, Map<String, String> parameter) {
        if(!fragmentSrc.isAdded())
        {
            fragmentSrc.setParameter(parameter);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.add(R.id.auth_fragment_container, fragmentSrc);
            ft.commit();
        }
    }

    private boolean onBack()
    {
        List<Fragment> lists = getSupportFragmentManager().getFragments();
        for(int i = lists.size() - 1;i > 0;i--)
        {
            BaseFragment fragment = (BaseFragment) lists.get(i);
            if(fragment != null)
            {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).remove(fragment).commit();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(onBack())
        {
            showDialogExit();
        }
    }

    private void showDialogExit()
    {
        final UIDialogConfirm dialogConfirm = new UIDialogConfirm(ActivityAuth.this);
        dialogConfirm.setMessage("Anda ingin keluar dari Aplikasi?");
        dialogConfirm.setDialogTitle(getResources().getString(R.string.dialog_confirm_title));
        dialogConfirm.show();
        dialogConfirm.getButtonYes().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
                finish();
            }
        });
        dialogConfirm.getButtonNo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();
            }
        });
    }
}
