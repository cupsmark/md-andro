package com.meetdesk.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.fragment.FragmentHome;
import com.meetdesk.view.UIDialogConfirm;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMenu().setSlidingEnabled(false);
        addFragmentHome();
    }

    private void addFragmentHome()
    {
        FragmentHome home = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        ft.add(R.id.main_container, home);
        ft.commit();
    }

    @Override
    public void onNavigate(BaseFragment fragmentSrc, Map<String, String> parameter) {
        super.onNavigate(fragmentSrc, parameter);
        if(!fragmentSrc.isAdded())
        {
            fragmentSrc.setParameter(parameter);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.add(R.id.main_container, fragmentSrc);
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
    public void onBackPressed() {
        if(onBack())
        {
            showDialogExit();
        }
    }

    private void showDialogExit()
    {
        final UIDialogConfirm dialogConfirm = new UIDialogConfirm(MainActivity.this);
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
