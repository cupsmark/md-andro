package com.meetdesk.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.external.uil.core.assist.FailReason;
import com.meetdesk.external.uil.core.listener.ImageLoadingListener;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UICircleImageView;
import com.meetdesk.view.UIText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class FragmentProfile extends BaseFragment{

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_PROFILE = "tag:fragment-profile";

    ImageButton imagebuttonBack;
    UICircleImageView imageviewAvatar;
    RelativeLayout buttonEdit;
    LazyImageLoader imageLoader;
    UIText textFullname, textEmail, textAbout, textPhone, textLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_profile, container, false);
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
            getUser();
        }
    }

    private void init()
    {
        imageLoader = new LazyImageLoader(activity);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.profile_imagebutton_back);
        imageviewAvatar = (UICircleImageView) activity.findViewById(R.id.profile_circleimage_avatar);
        buttonEdit = (RelativeLayout) activity.findViewById(R.id.profile_button_edit);
        textFullname = (UIText) activity.findViewById(R.id.profile_text_fullname);
        textAbout = (UIText) activity.findViewById(R.id.profile_text_about);
        textEmail = (UIText) activity.findViewById(R.id.profile_text_email);
        textPhone = (UIText) activity.findViewById(R.id.profile_text_phone);
        textLocation = (UIText) activity.findViewById(R.id.profile_text_location);

        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<String, String>();
                FragmentProfileEdit edit = new FragmentProfileEdit();
                ArrayList<BaseFragment> fragmentUpdate = new ArrayList<BaseFragment>();
                fragmentUpdate.add(FragmentProfile.this);
                edit.setFragmentUpdate(fragmentUpdate);
                iFragment.onNavigate(edit, param);
            }
        });
    }

    private void getUser()
    {
        final PrefAuthentication auth = new PrefAuthentication(activity);
        textFullname.setText(auth.getKeyUserFullname());
        textEmail.setText(auth.getKeyUserEmail());
        textPhone.setText(auth.getKeyUserPhone());
        textLocation.setText(auth.getKeyUserLocation());
        textAbout.setText(auth.getKeyUserDesc());
        if(!HelperGeneral.checkInternalFile(auth.getKeyUserAvatar(), activity))
        {
            imageLoader.showImage(HelperNative.getURL(11171) + auth.getKeyUserAvatar(), imageviewAvatar, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    HelperGeneral.saveBitmapInternal(auth.getKeyUserAvatar(), activity, loadedImage);
                    imageviewAvatar.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        if(HelperGeneral.checkInternalFile(auth.getKeyUserAvatar(), activity))
        {
            imageLoader.showImage(HelperGeneral.getPrivatePath(auth.getKeyUserAvatar(), activity), imageviewAvatar);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        getUser();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_PROFILE;
    }
}
