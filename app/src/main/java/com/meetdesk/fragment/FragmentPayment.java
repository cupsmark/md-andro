package com.meetdesk.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerProduct;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 9/17/16.
 */
public class FragmentPayment extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_PAYMENT = "tag:fragment-payment";

    ImageButton imagebuttonBack;
    String selectedID = "0",dataID, dataTitle, dataImage, dataDesc, dataRate;
    Map<String, String> param;
    int widthScreen;
    LinearLayout linearDateFrom, linearDateTo;
    UIButton buttonPay;
    UIText viewTitle, viewDesc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_payment, container, false);
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
        return TAG_FRAGMENT_PAYMENT;
    }

    private void init()
    {
        param = getParameter();
        selectedID = param.get("dataID");
        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.fragment_payment_back);
        linearDateFrom = (LinearLayout) activity.findViewById(R.id.fragment_payment_linear_date_from);
        linearDateTo = (LinearLayout) activity.findViewById(R.id.fragment_payment_linear_date_to);
        buttonPay = (UIButton) activity.findViewById(R.id.fragment_payment_pay);
        viewTitle = (UIText) activity.findViewById(R.id.fragment_payment_data_title);
        viewDesc = (UIText) activity.findViewById(R.id.fragment_payment_data_desc);

        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        linearDateFrom.getLayoutParams().width = (int) (widthScreen / 2) - 10;
        linearDateTo.getLayoutParams().width = (int) (widthScreen / 2) - 10;
    }

    private void getData()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;

            @Override
            protected String doInBackground(Void... params) {
                ControllerProduct product = new ControllerProduct(activity);
                product.setTargetID(selectedID);
                product.executeDetail();
                if(product.getSuccess())
                {
                    success = true;
                    dataID = product.getDataID().get(0);
                    dataTitle = product.getDataTitle().get(0);
                    dataImage = product.getDataImage().get(0);
                    dataRate = product.getDataRate().get(0);
                    dataDesc = product.getDataDesc().get(0);
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
                    viewTitle.setText(dataTitle);
                    viewDesc.setText(dataDesc);

                    buttonPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("dataID", dataID);
                            FragmentPaymentMethod method = new FragmentPaymentMethod();
                            iFragment.onNavigate(method, param);
                        }
                    });
                }
                else
                {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
