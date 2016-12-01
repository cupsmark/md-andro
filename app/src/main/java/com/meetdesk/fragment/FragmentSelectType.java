package com.meetdesk.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerGeneral;
import com.meetdesk.helper.HelperDB;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.EntityProductCategory;
import com.meetdesk.model.EntityRegion;
import com.meetdesk.model.QueryProCat;
import com.meetdesk.model.QueryRegion;
import com.meetdesk.view.UIButton;

import java.util.List;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class FragmentSelectType extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_SELECT_TYPE = "tag:fragment-select-type";

    ImageButton imagebuttonBack;
    UIButton buttonDone, buttonCity, buttonCategory;
    String selectedCityID = "0", selectedCityTitle = "Jakarta", selectedPlaceID = "0", selectedPlaceTitle = "Coworking Space";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_select_type_second, container, false);
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
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.select_type_imagebutton_back);
        buttonDone = (UIButton) activity.findViewById(R.id.select_type_button_done);
        buttonCategory = (UIButton) activity.findViewById(R.id.select_type_button_category);
        buttonCity = (UIButton) activity.findViewById(R.id.select_type_button_city);

        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buttonCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCity();
            }
        });
        buttonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCategory();
            }
        });
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
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
                        buttonCity.setText(selectedCityTitle);
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
                        buttonCategory.setText(selectedPlaceTitle);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_SELECT_TYPE;
    }

}
