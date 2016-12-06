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
 * Created by ekobudiarto on 12/5/16.
 */
public class ControllerBooking {

    Context mContext;
    boolean success;
    String msg, token;
    String[] field, value;
    ArrayList<String> productID, productTitle, productImage, productDesc, productUser;
    ArrayList<String> packagePriceID, packagePriceTitle,packagePriceDesc, packagePriceDiscount, packagePriceValue;
    ArrayList<String> bookingDetailID, bookingDetailDate, bookingDetailCode, bookingDetailAmount;
    int l, o;

    public ControllerBooking(Context context)
    {
        this.mContext = context;
        success = false;
        msg = "";
        l = 10;
        o = 0;
        productID = new ArrayList<String>();
        productTitle = new ArrayList<String>();
        productImage = new ArrayList<String>();
        productDesc = new ArrayList<String>();
        productUser = new ArrayList<String>();
        packagePriceID = new ArrayList<String>();
        packagePriceTitle = new ArrayList<String>();
        packagePriceDesc = new ArrayList<String>();
        packagePriceDiscount = new ArrayList<String>();
        packagePriceValue = new ArrayList<String>();
        bookingDetailID = new ArrayList<String>();
        bookingDetailDate = new ArrayList<String>();
        bookingDetailCode = new ArrayList<String>();
        bookingDetailAmount = new ArrayList<String>();
    }

    public void setParameter(String[] field, String[] value)
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

    public void setToken(String token)
    {
        this.token = token;
    }

    public void executeCheckAvailability()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11192);
            String fixedUrl = HelperGeneral.buildURL(baseUrl, field, value);
            fixedUrl = fixedUrl.replace(" ", "%20");
            String response = HelperGeneral.getJSON(fixedUrl);
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

    public void executeBookingReview()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11194);
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
                            productID.add(objectItem.getString("product_id"));
                            productTitle.add(objectItem.getString("product_title"));
                            productDesc.add(objectItem.getString("product_desc"));
                            productImage.add(objectItem.getString("product_image"));
                            productUser.add(objectItem.getString("product_users"));
                            packagePriceTitle.add(objectItem.getString("package_title"));
                            packagePriceDesc.add(objectItem.getString("package_desc"));
                            packagePriceDiscount.add(objectItem.getString("package_discount"));
                            packagePriceValue.add(objectItem.getString("package_price"));
                        }
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("message");
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

    public void executeBookNow()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11193);
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

    public void executeBookingList()
    {
        if(HelperGeneral.checkConnection(mContext))
        {
            String baseUrl = HelperNative.getURL(11195);
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
                            bookingDetailID.add(objectItem.getString("id"));
                            productID.add(objectItem.getString("product_id"));
                            productTitle.add(objectItem.getString("product_title"));
                            productDesc.add(objectItem.getString("product_desc"));
                            packagePriceID.add(objectItem.getString("package_id"));
                            packagePriceTitle.add(objectItem.getString("package_title"));
                            bookingDetailDate.add(objectItem.getString("booking_date"));
                            bookingDetailCode.add(objectItem.getString("booking_code"));
                            packagePriceDesc.add(objectItem.getString("package_desc"));
                            bookingDetailAmount.add(objectItem.getString("booking_amount"));
                        }
                        o = l + o;
                        success = true;
                    }
                    else
                    {
                        success = false;
                        msg = object.getString("message");
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

    public ArrayList<String> getProductID()
    {
        return productID;
    }

    public ArrayList<String> getProductTitle()
    {
        return productTitle;
    }

    public ArrayList<String> getProductImage()
    {
        return productImage;
    }

    public ArrayList<String> getProductDesc()
    {
        return productDesc;
    }

    public ArrayList<String> getProductUser()
    {
        return productUser;
    }

    public ArrayList<String> getPackagePriceID()
    {
        return packagePriceID;
    }

    public ArrayList<String> getPackagePriceTitle()
    {
        return packagePriceTitle;
    }

    public ArrayList<String> getPackagePriceDesc()
    {
        return packagePriceDesc;
    }

    public ArrayList<String> getPackagePriceDiscount()
    {
        return packagePriceDiscount;
    }

    public ArrayList<String> getPackagePriceValue()
    {
        return packagePriceValue;
    }

    public ArrayList<String> getBookingDetailID()
    {
        return bookingDetailID;
    }

    public ArrayList<String> getBookingDetailDate()
    {
        return bookingDetailDate;
    }

    public ArrayList<String> getBookingDetailCode()
    {
        return bookingDetailCode;
    }

    public ArrayList<String> getBookingDetailAmount()
    {
        return bookingDetailAmount;
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
