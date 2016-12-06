package com.meetdesk.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerProduct;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIDialogLoading;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 9/17/16.
 */
public class FragmentDetail extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_DETAIL = "tag:fragment-detail";

    ImageButton imagebuttonBack;
    ImageView imageviewImage;
    UIText pagetitle, rateValue, viewDesc;
    LinearLayout linearFragmentFacility;
    String selectedID = "0",dataID, dataTitle, dataImage, dataDesc, dataRate, dataUserID, dataUsername;
    ArrayList<String> facilityID, facilityTitle, facilityIcon;
    ArrayList<String> packageID, packageMasterID, packageMasterName, packageTitle, packagePrice, packageDiscount, packageDescription;
    Map<String, String> param;
    int widthScreen;
    LazyImageLoader imageLoader;
    LinearLayout buttonSendMessage, buttonAddToWishlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_detail, container, false);
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
            getData();
        }
    }

    @Override
    public String getFragmentTAG() {
        return TAG_FRAGMENT_DETAIL;
    }

    private void init()
    {
        param = getParameter();
        selectedID = param.get("dataID");
        imageLoader = new LazyImageLoader(activity);
        facilityID = new ArrayList<String>();
        facilityIcon = new ArrayList<String>();
        facilityTitle = new ArrayList<String>();
        packageID = new ArrayList<String>();
        packageMasterID = new ArrayList<String>();
        packageMasterName = new ArrayList<String>();
        packageTitle = new ArrayList<String>();
        packagePrice = new ArrayList<String>();
        packageDiscount = new ArrayList<String>();
        packageDescription = new ArrayList<String>();
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.fragment_detail_imagebutton_back);
        pagetitle = (UIText) activity.findViewById(R.id.fragment_detail_text_title);
        imageviewImage = (ImageView) activity.findViewById(R.id.fragment_detail_image);
        rateValue = (UIText) activity.findViewById(R.id.fragment_detail_review_value);
        viewDesc = (UIText) activity.findViewById(R.id.fragment_detail_desc);
        buttonSendMessage = (LinearLayout) activity.findViewById(R.id.detail_button_message);
        buttonAddToWishlist = (LinearLayout) activity.findViewById(R.id.detail_button_wishlist);
        linearFragmentFacility = (LinearLayout) activity.findViewById(R.id.fragment_detail_linear_fragment_facility);

        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    private void getData()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ArrayList<String> tempFacID, tempFacTitle, tempFacIcon;
            ArrayList<String> tempPackageID, tempPackageMasterID, tempPackageMasterName, tempPackageTitle, tempPackagePrice, tempPackageDiscount, tempPackageDescription;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                tempFacID = new ArrayList<String>();
                tempFacIcon = new ArrayList<String>();
                tempFacTitle = new ArrayList<String>();
                tempPackageID = new ArrayList<String>();
                tempPackageMasterID = new ArrayList<String>();
                tempPackageMasterName = new ArrayList<String>();
                tempPackageTitle = new ArrayList<String>();
                tempPackagePrice = new ArrayList<String>();
                tempPackageDiscount = new ArrayList<String>();
                tempPackageDescription = new ArrayList<String>();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                ControllerProduct product = new ControllerProduct(activity);
                product.setTargetID(selectedID);
                product.setToken(auth.getKeyUserToken());
                product.executeDetail();
                if(product.getSuccess())
                {
                    success = true;
                    dataID = product.getDataID().get(0);
                    dataTitle = product.getDataTitle().get(0);
                    dataImage = product.getDataImage().get(0);
                    dataRate = product.getDataRate().get(0);
                    dataDesc = product.getDataDesc().get(0);
                    dataUserID = product.getDataUserID().get(0);
                    dataUsername = product.getDataUserName().get(0);

                    tempFacID.addAll(product.getFacilityID());
                    tempFacTitle.addAll(product.getFacilityTitle());
                    tempFacIcon.addAll(product.getFacilityIcon());
                    tempPackageID.addAll(product.getPackageID());
                    tempPackageMasterID.addAll(product.getPackageMasterID());
                    tempPackageMasterName.addAll(product.getPackageMasterName());
                    tempPackageTitle.addAll(product.getPackageTitle());
                    tempPackagePrice.addAll(product.getPackagePrice());
                    tempPackageDiscount.addAll(product.getPackageDiscount());
                    tempPackageDescription.addAll(product.getPackageDescription());
                }
                else
                {
                    success = false;
                    msg = product.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    pagetitle.setText(dataTitle);
                    rateValue.setText(dataRate);
                    viewDesc.setText(dataDesc);

                    buttonAddToWishlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doAddWishList();
                        }
                    });
                    buttonSendMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("new", "1");
                            param.put("user", dataUsername);
                            param.put("target_id", selectedID);
                            FragmentInboxNew inboxNew = new FragmentInboxNew();
                            iFragment.onNavigate(inboxNew, param);
                        }
                    });
                    addViewFacility(tempFacID, tempFacTitle, tempFacIcon);
                    addViewPackage(tempPackageID,tempPackageMasterID, tempPackageMasterName, tempPackageTitle,tempPackagePrice, tempPackageDiscount, tempPackageDescription);
                    imageLoader.showImage(HelperNative.getURL(11171) + dataImage, imageviewImage);
                }
                else
                {
                    new UIToast(activity, msg).show();
                }
            }
        }.execute();
    }

    private void doAddWishList()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            UIDialogLoading dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new UIDialogLoading(activity);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "product"};
                String[] value = new String[]{auth.getKeyUserToken(), selectedID};
                ControllerProduct product = new ControllerProduct(activity);
                product.setPostParameter(field, value);
                product.executeWishAdd();
                if(product.getSuccess())
                {
                    success = true;
                }
                msg = product.getMessage();
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

                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }

    private void addViewFacility(ArrayList<String> tempID, ArrayList<String> tempTitle, ArrayList<String> tempIcon)
    {
        if(tempID.size() > 0)
        {
            facilityID.addAll(tempID);
            facilityTitle.addAll(tempTitle);
            facilityIcon.addAll(tempIcon);

            FragmentDetailFacilityList facilityList = new FragmentDetailFacilityList();
            facilityList.setDataID(facilityID);
            facilityList.setDataIcon(facilityIcon);
            facilityList.setDataTitle(facilityTitle);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_detail_linear_fragment_facility, facilityList).commit();
        }
    }

    private void addViewPackage(ArrayList<String> pacID,ArrayList<String> pacMstrID,ArrayList<String> pacMstrName,ArrayList<String> pacTitle, ArrayList<String> pacPrice, ArrayList<String> pacDiscount,ArrayList<String> pacDesc)
    {
        if(pacID.size() > 0)
        {
            packageID.addAll(pacID);
            packageMasterID.addAll(pacMstrID);
            packageMasterName.addAll(pacMstrName);
            packageTitle.addAll(pacTitle);
            packagePrice.addAll(pacPrice);
            packageDiscount.addAll(pacDiscount);
            packageDescription.addAll(pacDesc);

            FragmentDetailPackageList packageList = new FragmentDetailPackageList();
            packageList.setPackageID(packageID);
            packageList.setPackageMasterID(packageMasterID);
            packageList.setPackageMasterName(packageMasterName);
            packageList.setPackageTitle(packageTitle);
            packageList.setPackagePrice(packagePrice);
            packageList.setPackageDiscount(packageDiscount);
            packageList.setPackageDescription(packageDescription);
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("product", selectedID);
            packageList.setParameter(parameter);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_detail_linear_fragment_package, packageList).commit();
        }
    }
}
