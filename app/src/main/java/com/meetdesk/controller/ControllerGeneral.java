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
    ArrayList<String> messageDetailID, messageDetailDate, messageDetailImage, messageDetailContent, messageDetailIsSelf;
    ArrayList<String> inboxID, inboxTitle, inboxUser, inboxDate, inboxMessage;
    ArrayList<Boolean> inboxNotif, inboxShowGroupDate;
    boolean success = false;
    String msg, token, target_id, target_field;
    String[] field, value;
    int l, o;

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
        messageDetailID = new ArrayList<String>();
        messageDetailDate = new ArrayList<String>();
        messageDetailImage = new ArrayList<String>();
        messageDetailContent = new ArrayList<String>();
        messageDetailIsSelf = new ArrayList<String>();
        inboxID = new ArrayList<String>();
        inboxTitle = new ArrayList<String>();
        inboxUser = new ArrayList<String>();
        inboxDate = new ArrayList<String>();
        inboxMessage = new ArrayList<String>();
        inboxNotif = new ArrayList<Boolean>();
        inboxShowGroupDate  = new ArrayList<Boolean>();
        msg = "";
        token = "";
        l = 10;
        o = 0;
        target_id = "";
        target_field = "";
    }

    public void setPostParameter(String[] field, String[] value)
    {
        this.field = field;
        this.value = value;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setL(int l)
    {
        this.l = l;
    }

    public void setO(int o)
    {
        this.o = o;
    }

    public void setTarget(String target)
    {
        this.target_id = target;
    }

    public void setTargetField(String field)
    {
        this.target_field = field;
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

    public void executeListMessageDetail(){
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11187);
            String[] field = new String[]{"token", "field", "id", "l", "o"};
            String[] value = new String[]{token, target_field, target_id, String.valueOf(l), String.valueOf(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
            if(response != null)
            {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("status"))
                    {
                        JSONArray arr_item = object.getJSONArray("item");
                        for(int i = 0;i < arr_item.length();i++)
                        {
                            JSONObject objectItem = arr_item.getJSONObject(i);
                            messageDetailID.add(objectItem.getString("id"));
                            messageDetailDate.add(objectItem.getString("date"));
                            messageDetailImage.add(objectItem.getString("avatar"));
                            messageDetailContent.add(objectItem.getString("message_content"));
                            messageDetailIsSelf.add(objectItem.getString("is_self"));
                        }
                        o = o + l;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("msg");
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
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

    public void executeAddMessage()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11190);
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

    public void executeListMessage(){
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11188);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, String.valueOf(l), String.valueOf(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
            if(response != null)
            {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("status"))
                    {
                        JSONArray arr_item = object.getJSONArray("item");
                        for(int i = 0;i < arr_item.length();i++)
                        {
                            JSONObject objectItem = arr_item.getJSONObject(i);
                            inboxID.add(objectItem.getString("id"));
                            inboxUser.add(objectItem.getString("users_name"));
                            inboxTitle.add(objectItem.getString("title"));
                            inboxDate.add(objectItem.getString("date"));
                            inboxMessage.add(objectItem.getString("message_content"));
                            inboxShowGroupDate.add(objectItem.getBoolean("show_group"));
                            inboxNotif.add(objectItem.getBoolean("is_unread"));
                        }
                        o = o + l;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("msg");
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
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

    public ArrayList<String> getMessageDetailID()
    {
        return messageDetailID;
    }

    public ArrayList<String> getMessageDetailDate()
    {
        return messageDetailDate;
    }

    public ArrayList<String> getMessageDetailImage()
    {
        return messageDetailImage;
    }

    public ArrayList<String> getMessageDetailContent()
    {
        return messageDetailContent;
    }
    public boolean getSuccess()
    {
        return success;
    }

    public ArrayList<String> getMessageDetailIsSelf()
    {
        return messageDetailIsSelf;
    }

    public ArrayList<String> getInboxID()
    {
        return inboxID;
    }

    public ArrayList<String> getInboxTitle()
    {
        return inboxTitle;
    }

    public ArrayList<String> getInboxUser()
    {
        return inboxUser;
    }

    public ArrayList<String> getInboxDate()
    {
        return inboxDate;
    }

    public ArrayList<String> getInboxMessage()
    {
        return inboxMessage;
    }

    public ArrayList<Boolean> getInboxNotif()
    {
        return inboxNotif;
    }

    public ArrayList<Boolean> getInboxShowGroupDate()
    {
        return inboxShowGroupDate;
    }

    public String getMessage()
    {
        return msg;
    }

    public int getOffset()
    {
        return o;
    }
}
