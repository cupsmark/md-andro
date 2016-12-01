package com.meetdesk.controller;

import android.content.Context;
import android.net.Uri;

import com.meetdesk.R;
import com.meetdesk.helper.HelperDB;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.EntityProductCategory;
import com.meetdesk.model.EntityRegion;
import com.meetdesk.model.QueryProCat;
import com.meetdesk.model.QueryRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class ControllerSetup {

    Context mContext;
    String msg, token;
    int l, o;
    boolean success;
    ArrayList<String> regionID, regionName, regionCode;
    ArrayList<String> procatID, procatName, procatDesc, procatIcon;

    public ControllerSetup(Context context)
    {
        this.mContext = context;
        l = 0;
        o = 0;
        msg = "";
        token = "";
        success = false;
        regionID = new ArrayList<String>();
        regionName = new ArrayList<String>();
        regionCode = new ArrayList<String>();
        procatID = new ArrayList<String>();
        procatName = new ArrayList<String>();
        procatDesc = new ArrayList<String>();
        procatIcon = new ArrayList<String>();
    }

    public void setL(int l)
    {
        this.l = l;
    }

    public ArrayList<String> getRegionID()
    {
        return regionID;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void getRegion()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11172);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, Integer.toString(l), Integer.toString(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String json = HelperGeneral.getJSON(fixedUrl);
            if(json != null)
            {
                try {
                    JSONObject obj = new JSONObject(json);
                    if(obj.getBoolean("status"))
                    {
                        HelperDB db = new HelperDB(mContext);
                        QueryRegion region = new QueryRegion(db);
                        JSONArray arrData = obj.getJSONArray("item");
                        for(int i = 0;i < arrData.length();i++)
                        {
                            JSONObject objItem = arrData.getJSONObject(i);
                            String id = objItem.getString("id");
                            String name = objItem.getString("name");
                            String code = objItem.getString("code");
                            EntityRegion eRegion = new EntityRegion();
                            eRegion.setRegionID(Integer.parseInt(id));
                            eRegion.setRegionName(name);
                            eRegion.setRegionCode(code);
                            if(region.CheckItemRegion(Integer.parseInt(id)))
                            {
                                region.UpdateRegion(eRegion);
                            }
                            else
                            {
                                region.AddRegion(eRegion);
                            }
                        }
                        success = true;
                        o = o + l;
                    }
                    else
                    {
                        success = false;
                        msg = obj.getString("message");
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

    public void getProcat()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11173);
            String[] field = new String[]{"token", "l", "o"};
            String[] value = new String[]{token, Integer.toString(l), Integer.toString(o)};
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String json = HelperGeneral.getJSON(fixedUrl);
            if(json != null)
            {
                try {
                    JSONObject obj = new JSONObject(json);
                    if(obj.getBoolean("status"))
                    {
                        HelperDB db = new HelperDB(mContext);
                        QueryProCat proCat = new QueryProCat(db);
                        JSONArray arrData = obj.getJSONArray("item");
                        for(int i = 0;i < arrData.length();i++)
                        {
                            JSONObject objItem = arrData.getJSONObject(i);
                            String id = objItem.getString("category_id");
                            String title = objItem.getString("category_title");
                            String desc = objItem.getString("category_desc");
                            String icon = objItem.getString("category_icon");
                            EntityProductCategory category = new EntityProductCategory();
                            category.setCategoryID(Integer.parseInt(id));
                            category.setCategoryName(title);
                            category.setCategoryDesc(desc);
                            category.setCategoryIcon(icon);
                            if(proCat.CheckItemCategory(Integer.parseInt(id)))
                            {
                                proCat.UpdateCategory(category);
                            }
                            else
                            {
                                proCat.AddCategory(category);
                            }
                        }
                        success = true;
                        o = o + l;
                    }
                    else
                    {
                        success = false;
                        msg = obj.getString("message");
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

    public ArrayList<String> getRegionName()
    {
        return regionName;
    }

    public ArrayList<String> getRegionCode()
    {
        return regionCode;
    }

    public ArrayList<String> getProcatID()
    {
        return procatID;
    }

    public ArrayList<String> getProcatName()
    {
        return procatName;
    }

    public ArrayList<String> getProcatDesc()
    {
        return procatDesc;
    }

    public ArrayList<String> getProcatIcon()
    {
        return procatIcon;
    }

    public void setO(int o)
    {
        this.o = o;
    }

    public boolean getSuccess()
    {
        return success;
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
