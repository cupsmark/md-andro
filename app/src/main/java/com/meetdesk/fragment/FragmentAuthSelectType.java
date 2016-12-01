package com.meetdesk.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.meetdesk.helper.HelperDB;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.EntityProductCategory;
import com.meetdesk.model.EntityRegion;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.model.QueryProCat;
import com.meetdesk.model.QueryRegion;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class FragmentAuthSelectType extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_AUTH_SELECT_TYPE = "tag:fragment-auth-select-type";

    UIButton buttonSelectCity, buttonSelectCategory, buttonSearch;
    LinearLayout buttonLogin, buttonSignup;
    ImageView imageviewBG;
    String selectedCityID = "0", selectedCityTitle = "Jakarta", selectedPlaceID = "0", selectedPlaceTitle = "Coworking Space";
    int widthScreen = 0;
    LazyImageLoader imageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_auth_select_type, container, false);
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
            setStatusDisplaySelectType();
        }
    }

    private void init()
    {
        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        imageLoader = new LazyImageLoader(activity);
        buttonSelectCity = (UIButton) activity.findViewById(R.id.auth_select_type_city);
        buttonSelectCategory = (UIButton) activity.findViewById(R.id.auth_select_type_space);
        buttonSearch = (UIButton) activity.findViewById(R.id.auth_select_type_search);
        buttonLogin = (LinearLayout) activity.findViewById(R.id.auth_select_type_btn_login);
        buttonSignup = (LinearLayout) activity.findViewById(R.id.auth_select_type_btn_signup);
        imageviewBG = (ImageView) activity.findViewById(R.id.auth_select_type_bg);

        buttonLogin.getLayoutParams().width = (widthScreen / 2) - 2;
        buttonSignup.getLayoutParams().width = (widthScreen / 2) - 2;
        buttonSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCity();
            }
        });
        buttonSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCategory();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<String, String>();
                FragmentAuthSignIn login = new FragmentAuthSignIn();
                iFragment.onNavigate(login, param);
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<String, String>();
                FragmentAuthSignUp signup = new FragmentAuthSignUp();
                iFragment.onNavigate(signup, param);
            }
        });

        imageLoader.showImage("drawable://"+R.drawable.bg_auth_select, imageviewBG);
    }

    private void setStatusDisplaySelectType()
    {
        PrefAuthentication authPreferences = new PrefAuthentication(activity);
        authPreferences.setDisplaySelectType(false);
        authPreferences.commit();
    }

    private void showDialogCity()
    {
        HelperDB db = new HelperDB(activity);
        QueryRegion region = new QueryRegion(db);
        List<EntityRegion> listData = region.GetAllRegion();
        final String[] itemTitle = new String[listData.size()];
        final String[] itemID = new String[listData.size()];
        int i = 0;
        for(EntityRegion regionData : listData)
        {
            itemID[i] = Integer.toString(regionData.getRegionID());
            itemTitle[i] = regionData.getRegionName();
            i++;
        }
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Select City")
                .setItems(itemTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCityID = itemID[which];
                        selectedCityTitle = itemTitle[which];
                        buttonSelectCity.setText(selectedCityTitle);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void showDialogCategory()
    {
        HelperDB db = new HelperDB(activity);
        QueryProCat proCat = new QueryProCat(db);
        List<EntityProductCategory> listData = proCat.GetAllCategory();
        final String[] itemTitle = new String[listData.size()];
        final String[] itemID = new String[listData.size()];
        int i = 0;
        for(EntityProductCategory category : listData)
        {
            itemID[i] = Integer.toString(category.getCategoryID());
            itemTitle[i] = category.getCategoryName();
            i++;
        }
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Select Space Type")
                .setItems(itemTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPlaceID = itemID[which];
                        selectedPlaceTitle = itemTitle[which];
                        buttonSelectCategory.setText(selectedPlaceTitle);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

}
