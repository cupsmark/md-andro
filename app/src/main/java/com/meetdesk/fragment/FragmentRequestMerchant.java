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
import android.widget.Toast;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerGeneral;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIEditText;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class FragmentRequestMerchant extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_REQ_MERCHANT = "tag:fragment-req-merchant";

    ImageButton imagebuttonClose;
    UIButton buttonSend;
    UIEditText editEmail, editAddress, editPhone, editName, editAmenities, editSpaceDetail, editMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_request_merchant, container, false);
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
        imagebuttonClose = (ImageButton) activity.findViewById(R.id.req_merchant_imagebutton_close);
        buttonSend = (UIButton) activity.findViewById(R.id.req_merchant_send);
        editEmail = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_email);
        editAddress = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_address);
        editPhone = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_phone);
        editName = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_merchantname);
        editAmenities = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_amenities);
        editSpaceDetail = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_detail);
        editMessage = (UIEditText) activity.findViewById(R.id.req_merchant_edittext_message);

        imagebuttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });
    }

    private void doRequest()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ProgressDialog dialog;
            String valueEmail, valueAddress, valuePhone, valueName, valueAmenities, valueDetail, valueMessage;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setCancelable(false);
                dialog.show();

                valueEmail = editEmail.getText().toString();
                valueAddress = editAddress.getText().toString();
                valuePhone = editPhone.getText().toString();
                valueName = editName.getText().toString();
                valueAmenities = editAmenities.getText().toString();
                valueDetail = editSpaceDetail.getText().toString();
                valueMessage = editMessage.getText().toString();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "name", "address", "phone", "email", "space", "amenities", "message"};
                String[] value = new String[]{auth.getKeyUserToken(), valueName, valueAddress, valuePhone, valueEmail, valueDetail, valueAmenities, valueMessage};
                ControllerGeneral general = new ControllerGeneral(activity);
                general.setPostParameter(field, value);
                general.executeRentOut();
                if(general.getSuccess())
                {
                    success = true;
                }
                msg = general.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();;
                }
                if(success)
                {
                    activity.onBackPressed();
                }
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
}
