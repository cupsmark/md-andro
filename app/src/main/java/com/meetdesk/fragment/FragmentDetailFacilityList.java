package com.meetdesk.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIText;
import com.meetdesk.view.ViewHorizontallScroll;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ekobudiarto on 12/4/16.
 */
public class FragmentDetailFacilityList extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_SETTINGS = "tag:fragment-settings";

    ViewHorizontallScroll horizontalScroll;
    ArrayList<String> fac_id, fac_title,fac_icon;
    LinearLayout horizontalScrollLinear;
    LazyImageLoader imageLoader;
    int widthScreen = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_detail_facility_list, container, false);
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
            applyData();
        }
    }

    private void init()
    {
        imageLoader = new LazyImageLoader(activity);
        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        horizontalScroll = (ViewHorizontallScroll) activity.findViewById(R.id.fragment_detail_facility_list_horizontal);
        horizontalScrollLinear = (LinearLayout) activity.findViewById(R.id.fragment_detail_facility_list_linear);
    }

    public void setDataID(ArrayList<String> id)
    {
        this.fac_id = id;
    }

    public void setDataTitle(ArrayList<String> title)
    {
        this.fac_title = title;
    }

    public void setDataIcon(ArrayList<String> icon)
    {
        this.fac_icon = icon;
    }

    private void applyData()
    {
        if(fac_id.size() > 0)
        {
            for(int i = 0;i < fac_id.size();i++)
            {
                View item = LayoutInflater.from(activity).inflate(R.layout.fragment_detail_facility_list_item, null, false);
                horizontalScrollLinear.addView(item);

                item.getLayoutParams().width = widthScreen / 3;
                ImageView viewIcon = (ImageView) item.findViewById(R.id.fragment_detail_facility_list_item_icon);
                UIText viewTitle = (UIText) item.findViewById(R.id.fragment_detail_facility_list_item_title);

                viewTitle.setText(fac_title.get(i));
                imageLoader.showImage(HelperNative.getURL(11171) + fac_icon.get(i), viewIcon);
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        applyData();
    }
}
