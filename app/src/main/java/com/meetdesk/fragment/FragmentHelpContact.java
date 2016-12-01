package com.meetdesk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIEditText;

/**
 * Created by ekobudiarto on 11/4/16.
 */
public class FragmentHelpContact extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_HELP_CONTACT = "tag:fragment-help-contact";

    ImageButton imagebuttonBack;
    UIEditText editMessage;
    UIButton buttonSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_help_contact, container, false);
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
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.help_contact_imagebutton_back);
        editMessage = (UIEditText) activity.findViewById(R.id.help_contact_message);
        buttonSubmit = (UIButton) activity.findViewById(R.id.help_contact_send);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Thank you for your request", Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }
        });
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

}
