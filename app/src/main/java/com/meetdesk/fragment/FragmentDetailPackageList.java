package com.meetdesk.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIText;
import com.meetdesk.view.ViewSubViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 12/4/16.
 */
public class FragmentDetailPackageList extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_SETTINGS = "tag:fragment-settings";

    ArrayList<String> packageID, packageMasterID, packageMasterName, packageTitle, packagePrice, packageDiscount, packageDescription;
    ViewSubViewPager viewPager;
    DetailPackageListAdapter adapter;
    Map<String, String> param;
    String product;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_detail_package_list, container, false);
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

    public void setPackageID(ArrayList<String> id)
    {
        this.packageID = id;
    }
    public void setPackageMasterID(ArrayList<String> mid)
    {
        this.packageMasterID = mid;
    }
    public void setPackageMasterName(ArrayList<String> name)
    {
        this.packageMasterName = name;
    }
    public void setPackageTitle(ArrayList<String> title)
    {
        this.packageTitle = title;
    }
    public void setPackagePrice(ArrayList<String> price)
    {
        this.packagePrice = price;
    }
    public void setPackageDiscount(ArrayList<String> discount)
    {
        this.packageDiscount = discount;
    }
    public void setPackageDescription(ArrayList<String> desc)
    {
        this.packageDescription = desc;
    }

    private void init()
    {
        param = getParameter();
        product = param.get("product");
        adapter = new DetailPackageListAdapter();
        viewPager = (ViewSubViewPager) activity.findViewById(R.id.fragment_detail_package_list_viewpager);
        viewPager.setAdapter(adapter);
    }

    private void applyData()
    {
        adapter.notifyDataSetChanged();
    }

    private void launchBookingForm(int position)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("product", product);
        param.put("package", packageID.get(position));
        FragmentBookingForm bookingForm = new FragmentBookingForm();
        bookingForm.setParameter(param);
        iFragment.onNavigate(bookingForm, param);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        applyData();
    }

    public class DetailPackageListAdapter extends PagerAdapter{

        public DetailPackageListAdapter()
        {

        }

        @Override
        public int getCount() {
            return packageID.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            LayoutInflater inflater = LayoutInflater.from(activity);
            View viewData = inflater.inflate(R.layout.fragment_detail_package_list_item, container, false);

            UIText textTitle = (UIText) viewData.findViewById(R.id.fragment_detail_package_list_item_title);
            UIText textPrice = (UIText) viewData.findViewById(R.id.fragment_detail_package_list_item_price);
            UIText textDiscount = (UIText) viewData.findViewById(R.id.fragment_detail_package_list_item_discount);
            UIText textDesc = (UIText) viewData.findViewById(R.id.fragment_detail_package_list_item_desc);
            UIButton btnSelect = (UIButton) viewData.findViewById(R.id.fragment_detail_package_list_item_select);

            textTitle.setText(packageTitle.get(position));
            textPrice.setText(packagePrice.get(position));
            textDiscount.setText(packageDiscount.get(position));
            textDesc.setText(packageDescription.get(position));
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchBookingForm(position);
                }
            });
            container.addView(viewData);

            return viewData;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}
