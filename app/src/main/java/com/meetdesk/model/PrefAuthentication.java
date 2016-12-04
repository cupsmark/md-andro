package com.meetdesk.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ekobudiarto on 11/3/16.
 */
public class PrefAuthentication {

    public static final String SHARED_PREF_FILE = "authenticationPreferences";
    public static final String KEY_SELECT_TYPE = "showSelectType";
    public static final String KEY_LOGGED_IN = "isLoggedIn";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_USER_FULLNAME = "fullname";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_TOKEN = "token";
    public static final String KEY_USER_AVATAR = "avatar";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_USER_LAT = "lat";
    public static final String KEY_USER_LONG = "long";
    public static final String KEY_USER_DESC = "desc";
    public static final String KEY_USER_BIRTHDATE = "birthdate";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_LOCATION = "location";
    public static final String KEY_USER_ABOUT = "about";
    public static final String KEY_USER_TOKEN_FCM = "token_fcm";
    public static final String KEY_USER_SOURCE = "users_source";
    public static final String KEY_USER_SOCIALID = "users_social_id";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Context mContext;

    public PrefAuthentication(Context context)
    {
        this.mContext = context;
        this.pref = mContext.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        this.editor = pref.edit();
    }

    public void setDisplaySelectType(boolean isDisplaying)
    {
        editor.putBoolean(KEY_SELECT_TYPE, isDisplaying);
    }

    public void setIsLoggedIn(boolean isLoggedIn)
    {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
    }

    public void setKeyUserName(String username)
    {
        editor.putString(KEY_USER_NAME, username);
    }

    public void setKeyUserFullname(String fullname)
    {
        editor.putString(KEY_USER_FULLNAME, fullname);
    }

    public void setKeyUserEmail(String email)
    {
        editor.putString(KEY_USER_EMAIL, email);
    }

    public void setKeyUserToken(String token)
    {
        editor.putString(KEY_USER_TOKEN, token);
    }

    public void setKeyUserAvatar(String avatar)
    {
        editor.putString(KEY_USER_AVATAR, avatar);
    }

    public void setKeyUserPhone(String phone)
    {
        editor.putString(KEY_USER_PHONE, phone);
    }

    public void setKeyUserLat(String latitude)
    {
        editor.putString(KEY_USER_LAT, latitude);
    }

    public void setKeyUserLong(String longitude)
    {
        editor.putString(KEY_USER_LONG, longitude);
    }

    public void setKeyUserDesc(String desc)
    {
        editor.putString(KEY_USER_DESC, desc);
    }

    public void setKeyUserBirthdate(String birthdate)
    {
        editor.putString(KEY_USER_BIRTHDATE, birthdate);
    }

    public void setKeyUserGender(String gender)
    {
        editor.putString(KEY_USER_GENDER, gender);
    }

    public void setKeyUserLocation(String location)
    {
        editor.putString(KEY_USER_LOCATION, location);
    }

    public void setKeyUserAbout(String about)
    {
        editor.putString(KEY_USER_ABOUT, about);
    }

    public void setKeyUserTokenFcm(String tokenFCM)
    {
        editor.putString(KEY_USER_TOKEN_FCM, tokenFCM);
    }

    public void setKeyUserSource(String source)
    {
        editor.putString(KEY_USER_SOURCE, source);
    }

    public void setKeyUserSocialID(String socialID)
    {
        editor.putString(KEY_USER_SOCIALID, socialID);
    }

    public void commit()
    {
        editor.commit();
    }

    public boolean getDisplayingSelectType()
    {
        return pref.getBoolean(KEY_SELECT_TYPE, false);
    }

    public boolean getLoggedIn()
    {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getKeyUserName()
    {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getKeyUserFullname()
    {
        return pref.getString(KEY_USER_FULLNAME, "");
    }

    public String getKeyUserEmail()
    {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getKeyUserToken()
    {
        return pref.getString(KEY_USER_TOKEN, "");
    }

    public String getKeyUserAvatar()
    {
        return pref.getString(KEY_USER_AVATAR, "");
    }

    public String getKeyUserPhone()
    {
        return pref.getString(KEY_USER_PHONE, "");
    }

    public String getKeyUserLat()
    {
        return pref.getString(KEY_USER_LAT, "");
    }

    public String getKeyUserLong()
    {
        return pref.getString(KEY_USER_LONG, "");
    }

    public String getKeyUserDesc()
    {
        return pref.getString(KEY_USER_DESC, "");
    }

    public String getKeyUserBirthdate()
    {
        return pref.getString(KEY_USER_BIRTHDATE, "");
    }

    public String getKeyUserGender()
    {
        return pref.getString(KEY_USER_GENDER, "0");
    }

    public String getKeyUserLocation()
    {
        return pref.getString(KEY_USER_LOCATION, "");
    }

    public String getKeyUserAbout()
    {
        return pref.getString(KEY_USER_ABOUT, "");
    }

    public String getKeyUserTokenFcm()
    {
        return pref.getString(KEY_USER_TOKEN_FCM, "");
    }

    public String getKeyUserSource()
    {
        return pref.getString(KEY_USER_SOURCE, "");
    }

    public String getKeyUserSocialID()
    {
        return pref.getString(KEY_USER_SOCIALID, "");
    }
}
