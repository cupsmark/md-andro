package com.meetdesk.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ekobudiarto on 12/3/16.
 */
public class MDFirebaseIDServices extends FirebaseInstanceIdService {

    boolean success = false;
    String msg = "";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        success = false;
        msg = "";
        String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
        registerDevice(tokenRefresh);
    }

    private void registerDevice(final String tokenFCM)
    {
        new AsyncTask<Void, Integer, String>()
        {

            @Override
            protected String doInBackground(Void... params) {
                Context mContext = getApplicationContext();
                PrefAuthentication auth = new PrefAuthentication(mContext);
                String tokenDefault = HelperGeneral.getDeviceID(mContext);
                if(!auth.getKeyUserToken().equals(""))
                {
                    tokenDefault = auth.getKeyUserToken();
                }
                String[] field = new String[]{"token", "device_id", "device_os", "device_os_version", "device_brand", "device_provider", "token_fcm"};
                String[] value = new String[]{tokenDefault, HelperGeneral.getDeviceID(mContext), "android", HelperGeneral.getDeviceVersion(),
                                    HelperGeneral.getDeviceBrand(), "", tokenFCM};
                doRegisterDevice(mContext, field, value);
                if(success)
                {
                    auth.setKeyUserTokenFcm(tokenFCM);
                    auth.commit();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void doRegisterDevice(Context mContext, String[] field, String[] value)
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11186);
            String response = HelperGeneral.postJSON(baseUrl, field, value);
            if(response != null)
            {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("status"))
                    {
                        success = true;
                    }
                    else
                    {
                        success = false;
                    }
                    msg = object.getString("message");
                } catch (JSONException e) {
                    success = false;
                    msg = e.getMessage().toString();
                }
            }
            else
            {
                success = false;
                msg = mContext.getResources().getString(R.string.base_null_json);
            }
        }
        else
        {
            success = false;
            msg = mContext.getResources().getString(R.string.base_no_internet);
        }
    }
}
