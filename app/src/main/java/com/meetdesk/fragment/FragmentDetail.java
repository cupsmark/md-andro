package com.meetdesk.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerProduct;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIText;

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
    LinearLayout featureWifi, featureBreakfast, featureParking;
    String selectedID = "0",dataID, dataTitle, dataImage, dataDesc, dataRate, dataUserID, dataUsername;
    UIButton buttonNext;
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
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.fragment_detail_imagebutton_back);
        pagetitle = (UIText) activity.findViewById(R.id.fragment_detail_text_title);
        featureWifi = (LinearLayout) activity.findViewById(R.id.fragment_detail_feature_wifi);
        featureBreakfast = (LinearLayout) activity.findViewById(R.id.fragment_detail_feature_breakfast);
        featureParking = (LinearLayout) activity.findViewById(R.id.fragment_detail_feature_parking);
        imageviewImage = (ImageView) activity.findViewById(R.id.fragment_detail_image);
        rateValue = (UIText) activity.findViewById(R.id.fragment_detail_review_value);
        viewDesc = (UIText) activity.findViewById(R.id.fragment_detail_desc);
        buttonNext = (UIButton) activity.findViewById(R.id.fragment_detail_next_step);
        buttonSendMessage = (LinearLayout) activity.findViewById(R.id.detail_button_message);
        buttonAddToWishlist = (LinearLayout) activity.findViewById(R.id.detail_button_wishlist);

        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        featureWifi.getLayoutParams().width = widthScreen / 3;
        featureBreakfast.getLayoutParams().width = widthScreen / 3;
        featureParking.getLayoutParams().width = widthScreen / 3;
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

                    buttonNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("dataID", dataID);
                            FragmentPayment payment = new FragmentPayment();
                            iFragment.onNavigate(payment, param);
                        }
                    });
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
                    imageLoader.showImage(HelperNative.getURL(11171) + dataImage, imageviewImage);
                }
                else
                {
                    Toast.makeText(activity, msg , Toast.LENGTH_SHORT).show();
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
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
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
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

}
