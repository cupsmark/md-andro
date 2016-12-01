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
 * Created by ekobudiarto on 9/17/16.
 */
public class ControllerProduct {

    Context mContext;
    ArrayList<String> dataID, dataTitle, dataImage, dataDesc, dataRate, dataDate;
    String[] field, value;
    boolean success = false;
    String msg, target_id, token;
    int l, o;

    public ControllerProduct(Context context)
    {
        this.mContext = context;
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataDesc = new ArrayList<String>();
        dataImage = new ArrayList<String>();
        dataRate = new ArrayList<String>();
        dataDate = new ArrayList<String>();
        msg = "";
        target_id = "0";
        token = "";
        l = 5;
        o = 0;
    }

    public void setTargetID(String id)
    {
        this.target_id = id;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setPostParameter(String[] field, String[] value)
    {
        this.field = field;
        this.value = value;
    }

    public void setL(int l)
    {
        this.l = l;
    }

    public void setO(int o)
    {
        this.o = o;
    }

    public void executeHome()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11176);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, Integer.toString(l), Integer.toString(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
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
                            dataDesc.add(objectItem.getString("desc"));
                            dataImage.add(objectItem.getString("image"));
                            dataRate.add(objectItem.getString("rate"));
                            dataDate.add(objectItem.getString("date_created"));
                        }
                        o = o + l;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("message");
                    }
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

    public void executeDetail()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11177);
            String[] field = new String[]{"token", "id"};
            String[] value = new String[]{token, target_id};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
            //String response = HelperGeneral.GetJSONAssets("detail"+target_id +".json", mContext);
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
                            dataID.add(objectItem.getString("id"));
                            dataTitle.add(objectItem.getString("title"));
                            dataDesc.add(objectItem.getString("desc"));
                            dataImage.add(objectItem.getString("image"));
                            dataRate.add(objectItem.getString("rate"));
                            dataDate.add(objectItem.getString("date_created"));
                        }
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

    public void executeHotList()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11178);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, Integer.toString(l), Integer.toString(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
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
                            dataDesc.add(objectItem.getString("desc"));
                            dataImage.add(objectItem.getString("image"));
                            dataRate.add(objectItem.getString("rate"));
                            dataDate.add(objectItem.getString("date_created"));
                        }
                        o = o + l;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("message");
                    }
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

    public void executeWishList()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11179);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, Integer.toString(l), Integer.toString(o)};
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
                            dataID.add(objectItem.getString("id"));
                            dataTitle.add(objectItem.getString("title"));
                            dataDesc.add(objectItem.getString("desc"));
                            dataImage.add(objectItem.getString("image"));
                            dataRate.add(objectItem.getString("rate"));
                            dataDate.add(objectItem.getString("date_created"));
                        }
                        o = o + l;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("message");
                    }
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

    public void executeWishAdd()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11180);
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

    public void executeWishDelete()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11181);
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


    public ArrayList<String> getDataID()
    {
        return this.dataID;
    }

    public ArrayList<String> getDataTitle()
    {
        return this.dataTitle;
    }

    public ArrayList<String> getDataDesc()
    {
        return this.dataDesc;
    }

    public ArrayList<String> getDataImage()
    {
        return this.dataImage;
    }

    public ArrayList<String> getDataRate()
    {
        return this.dataRate;
    }

    public ArrayList<String> getDataDate()
    {
        return this.dataDate;
    }

    public boolean getSuccess()
    {
        return this.success;
    }

    public String getMessage()
    {
        return this.msg;
    }

    public int getOffset()
    {
        return o;
    }
}
