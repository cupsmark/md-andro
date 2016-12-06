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
    ArrayList<String> dataID, dataTitle, dataImage, dataDesc, dataRate, dataDate, dataUserID, dataUserName;
    ArrayList<String> facilityID, facilityTitle, facilityIcon;
    ArrayList<String> packageID, packageMasterID, packageMasterName, packageTitle, packagePrice, packageDiscount, packageDescription;
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
        dataUserID = new ArrayList<String>();
        dataUserName = new ArrayList<String>();
        facilityID = new ArrayList<String>();
        facilityTitle = new ArrayList<String>();
        facilityIcon = new ArrayList<String>();
        packageID = new ArrayList<String>();
        packageMasterID = new ArrayList<String>();
        packageMasterName = new ArrayList<String>();
        packageTitle = new ArrayList<String>();
        packagePrice = new ArrayList<String>();
        packageDiscount = new ArrayList<String>();
        packageDescription = new ArrayList<String>();
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
                            dataUserID.add(objectItem.getString("users"));
                            dataUserName.add(objectItem.getString("usersname"));
                            JSONArray arrFacility = objectItem.getJSONArray("facility");
                            for(int a = 0;a < arrFacility.length();a++)
                            {
                                JSONObject objArrFacility = arrFacility.getJSONObject(a);
                                facilityID.add(objArrFacility.getString("id"));
                                facilityTitle.add(objArrFacility.getString("name"));
                                facilityIcon.add(objArrFacility.getString("icon"));
                            }
                            JSONArray arrPackage = objectItem.getJSONArray("package");
                            for(int b = 0;b < arrPackage.length();b++)
                            {
                                JSONObject objArrPackage = arrPackage.getJSONObject(b);
                                packageID.add(objArrPackage.getString("id"));
                                packageMasterID.add(objArrPackage.getString("package_master_id"));
                                packageMasterName.add(objArrPackage.getString("package_master_name"));
                                packageTitle.add(objArrPackage.getString("package_title"));
                                packagePrice.add(objArrPackage.getString("price"));
                                packageDiscount.add(objArrPackage.getString("discount"));
                                packageDescription.add(objArrPackage.getString("package_description"));
                            }
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
        if(HelperGeneral.checkConnection(mContext)) {
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

    public ArrayList<String> getDataUserID()
    {
        return dataUserID;
    }

    public ArrayList<String> getDataUserName()
    {
        return dataUserName;
    }

    public ArrayList<String> getFacilityID()
    {
        return facilityID;
    }

    public ArrayList<String> getFacilityTitle()
    {
        return facilityTitle;
    }

    public ArrayList<String> getFacilityIcon()
    {
        return facilityIcon;
    }

    public ArrayList<String> getPackageID()
    {
        return packageID;
    }

    public ArrayList<String> getPackageMasterID()
    {
        return packageMasterID;
    }

    public ArrayList<String> getPackageMasterName()
    {
        return packageMasterName;
    }

    public ArrayList<String> getPackageTitle()
    {
        return packageTitle;
    }

    public ArrayList<String> getPackagePrice()
    {
        return packagePrice;
    }

    public ArrayList<String> getPackageDiscount()
    {
        return packageDiscount;
    }

    public ArrayList<String> getPackageDescription()
    {
        return packageDescription;
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
