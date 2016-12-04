package com.meetdesk.controller;

import android.content.Context;

import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class ControllerAuthentication {

    String msg, url;
    boolean success;
    Context mContext;
    String[] field, value;

    public ControllerAuthentication(Context context)
    {
        this.mContext = context;
        success = false;
        msg = "";
        url = "";
    }

    public void setParameter(String[] field, String[] value)
    {
        this.field = field;
        this.value = value;
    }
    public void doRegister()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            url = HelperNative.getURL(11174);
            String json = HelperGeneral.postJSON(url, field, value);
            if(json != null)
            {
                try {
                    JSONObject object = new JSONObject(json);
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
                    msg = "Error Parsing : " + e.getMessage().toString();
                }
            }
            else
            {
                success = false;
                msg = mContext.getResources().getString(R.string.base_null_json);
            }
        }
        else {
            success = false;
            msg = mContext.getResources().getString(R.string.base_no_internet);
        }
    }

    public void doLogin()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            url = HelperNative.getURL(11175);
            String json = HelperGeneral.postJSON(url, field, value);
            if(json != null)
            {
                try {
                    JSONObject object = new JSONObject(json);
                    if(object.getBoolean("status"))
                    {
                        success = true;
                        JSONObject objData = object.getJSONObject("data");
                        String username = objData.getString("username");
                        String fullname = objData.getString("fullname");
                        String email = objData.getString("email");
                        String token = objData.getString("token");
                        String avatar = objData.getString("avatar");
                        String phone = objData.getString("phone");
                        String latitude = objData.getString("latitude");
                        String longitude = objData.getString("longitude");
                        String desc = objData.getString("desc");
                        String birthdate = objData.getString("birthdate");
                        String gender = objData.getString("gender");
                        String location = objData.getString("location");
                        String about = objData.getString("about");
                        String token_fcm = objData.getString("token_fcm");
                        String users_source = objData.getString("users_source");
                        String usersSocialID = objData.getString("users_social_id");

                        PrefAuthentication prefAuthentication = new PrefAuthentication(mContext);
                        prefAuthentication.setIsLoggedIn(true);
                        prefAuthentication.setKeyUserName(username);
                        prefAuthentication.setKeyUserFullname(fullname);
                        prefAuthentication.setKeyUserEmail(email);
                        prefAuthentication.setKeyUserToken(token);
                        prefAuthentication.setKeyUserAvatar(avatar);
                        prefAuthentication.setKeyUserPhone(phone);
                        prefAuthentication.setKeyUserLat(latitude);
                        prefAuthentication.setKeyUserLong(longitude);
                        prefAuthentication.setKeyUserDesc(desc);
                        prefAuthentication.setKeyUserBirthdate(birthdate);
                        prefAuthentication.setKeyUserGender(gender);
                        prefAuthentication.setKeyUserLocation(location);
                        prefAuthentication.setKeyUserAbout(about);
                        prefAuthentication.setKeyUserTokenFcm(token_fcm);
                        prefAuthentication.setKeyUserSource(users_source);
                        prefAuthentication.setKeyUserSocialID(usersSocialID);
                        prefAuthentication.commit();
                    }
                    else
                    {
                        success = false;
                    }
                    msg = object.getString("message");
                } catch (JSONException e) {
                    success = false;
                    msg = "Error Parsing : " + e.getMessage().toString();
                }
            }
            else
            {
                success = false;
                msg = mContext.getResources().getString(R.string.base_null_json);
            }
        }
        else {
            success = false;
            msg = mContext.getResources().getString(R.string.base_no_internet);
        }
    }

    public void doLoginSocial()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            url = HelperNative.getURL(11191);
            String json = HelperGeneral.postJSON(url, field, value);
            if(json != null)
            {
                try {
                    JSONObject object = new JSONObject(json);
                    if(object.getBoolean("status"))
                    {
                        success = true;
                        JSONObject objData = object.getJSONObject("data");
                        String username = objData.getString("username");
                        String fullname = objData.getString("fullname");
                        String email = objData.getString("email");
                        String token = objData.getString("token");
                        String avatar = objData.getString("avatar");
                        String phone = objData.getString("phone");
                        String latitude = objData.getString("latitude");
                        String longitude = objData.getString("longitude");
                        String desc = objData.getString("desc");
                        String birthdate = objData.getString("birthdate");
                        String gender = objData.getString("gender");
                        String location = objData.getString("location");
                        String about = objData.getString("about");
                        String token_fcm = objData.getString("token_fcm");
                        String users_source = objData.getString("users_source");
                        String usersSocialID = objData.getString("users_social_id");

                        PrefAuthentication prefAuthentication = new PrefAuthentication(mContext);
                        prefAuthentication.setIsLoggedIn(true);
                        prefAuthentication.setKeyUserName(username);
                        prefAuthentication.setKeyUserFullname(fullname);
                        prefAuthentication.setKeyUserEmail(email);
                        prefAuthentication.setKeyUserToken(token);
                        prefAuthentication.setKeyUserAvatar(avatar);
                        prefAuthentication.setKeyUserPhone(phone);
                        prefAuthentication.setKeyUserLat(latitude);
                        prefAuthentication.setKeyUserLong(longitude);
                        prefAuthentication.setKeyUserDesc(desc);
                        prefAuthentication.setKeyUserBirthdate(birthdate);
                        prefAuthentication.setKeyUserGender(gender);
                        prefAuthentication.setKeyUserLocation(location);
                        prefAuthentication.setKeyUserAbout(about);
                        prefAuthentication.setKeyUserTokenFcm(token_fcm);
                        prefAuthentication.setKeyUserSource(users_source);
                        prefAuthentication.setKeyUserSocialID(usersSocialID);
                        prefAuthentication.commit();
                    }
                    else
                    {
                        success = false;
                    }
                    msg = object.getString("message");
                } catch (JSONException e) {
                    success = false;
                    msg = "Error Parsing : " + e.getMessage().toString();
                }
            }
            else
            {
                success = false;
                msg = mContext.getResources().getString(R.string.base_null_json);
            }
        }
        else {
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
