package com.meetdesk.controller;

import android.content.Context;

import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class ControllerGeneral {

    Context mContext;
    ArrayList<String> dataID, dataTitle, dataImage, dataDesc, dataRate, dataDate;
    ArrayList<String> accountFullname, accountEmail, accountBirthdate, accountGender, accountAvatar, accountLatitude, accountLongitude, accountPhone;
    boolean success = false;
    String msg;
    String[] field, value;

    public ControllerGeneral(Context context)
    {
        this.mContext = context;
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataDesc = new ArrayList<String>();
        dataImage = new ArrayList<String>();
        dataRate = new ArrayList<String>();
        dataDate = new ArrayList<String>();
        accountFullname = new ArrayList<String>();
        accountEmail = new ArrayList<String>();
        accountBirthdate = new ArrayList<String>();
        accountGender = new ArrayList<String>();
        accountAvatar = new ArrayList<String>();
        accountLatitude = new ArrayList<String>();
        accountLongitude = new ArrayList<String>();
        accountPhone = new ArrayList<String>();
        msg = "";
    }

    public void setPostParameter(String[] field, String[] value)
    {
        this.field = field;
        this.value = value;
    }

    public void executeListCity()
    {
        String response = HelperGeneral.GetJSONAssets("city.json", mContext);
        if(response != null)
        {
            try {
                //response = HelperGeneral.convertStandardJSONString(response);
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("status"))
                {
                    JSONArray arr_item = object.getJSONArray("item");
                    for(int i = 0;i < arr_item.length();i++)
                    {
                        JSONObject objectItem = arr_item.getJSONObject(i);
                        dataID.add(objectItem.getString("id"));
                        dataTitle.add(objectItem.getString("title"));
                    }
                    success = true;
                }
                else
                {
                    success = false;
                    msg = object.getString("msg");
                }
            } catch (JSONException e) {
                success = false;
                msg = e.getMessage().toString();
            }
        }
        else
        {
            success = false;
            msg = "You have connection trouble";
        }
    }

    public void executeRentOut()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11182);
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

    public void executeUpdateAccount()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11184);
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

    public boolean getSuccess()
    {
        return success;
    }

    public String getMessage()
    {
        return msg;
    }
}
