package com.meetdesk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerBooking;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIDialogLoading;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by ekobudiarto on 12/5/16.
 */
public class FragmentBookingReview extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_SETTINGS = "tag:fragment-settings";

    Map<String, String> param;
    String product, packagePrice, dateStart, dateEnd, hourStart, hourEnd, quota, unit;
    UIText textMemberName, textMemberPhone, textMemberEmail, textPaymentProductPackage, textPaymentPrice,
            textPaymentQuota, textPaymentTotalPrice, textDateFromDayNumber, textDateFromMonth, textDateFromDayName,
            textDateToDayNumber, textDateToMonth, textDateToDayName, textProductTitle, textProductDesc;
    LinearLayout linearContainerDateFrom, linearContainerDateTo;
    ImageButton btnCallPhone;
    int widthScreen = 0;
    String phoneNumberOffice = "081287540692";
    String dataProductID, dataProductTitle, dataProductImage, dataProductDesc, dataProductUser,
            dataPackagePriceID, dataPackagePriceTitle, dataPackagePriceDesc, dataPackagePriceDiscount, dataPackagePriceValue;
    ImageView viewImageAssets;
    LazyImageLoader imageLoader;
    ImageButton imagebuttonBack;
    UIButton btnBookNow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_booking_review, container, false);
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
            initAction();
            getData();
        }
    }

    private void init()
    {
        param = getParameter();
        product = param.get("product");
        packagePrice = param.get("package");
        dateStart = param.get("start_date");
        dateEnd = param.get("end_date");
        hourStart = param.get("start_hour");
        hourEnd = param.get("end_hour");
        quota = param.get("quota");
        unit = param.get("unit");
        imageLoader = new LazyImageLoader(activity);
        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        textMemberEmail = (UIText) activity.findViewById(R.id.fragment_booking_review_member_email);
        textMemberName = (UIText) activity.findViewById(R.id.fragment_booking_review_member_name);
        textMemberPhone = (UIText) activity.findViewById(R.id.fragment_booking_review_member_phone);
        textPaymentProductPackage = (UIText) activity.findViewById(R.id.fragment_booking_review_book_data_title);
        textPaymentPrice = (UIText) activity.findViewById(R.id.fragment_booking_review_book_data_title_price);
        textPaymentQuota = (UIText) activity.findViewById(R.id.fragment_booking_review_book_data_quota);
        textPaymentTotalPrice = (UIText) activity.findViewById(R.id.fragment_booking_review_book_data_total_price);
        textDateFromDayNumber = (UIText) activity.findViewById(R.id.fragment_booking_review_date_from_daynumber);
        textDateFromDayName = (UIText) activity.findViewById(R.id.fragment_booking_review_date_from_dayname);
        textDateFromMonth = (UIText) activity.findViewById(R.id.fragment_booking_review_date_from_month);
        textDateToDayNumber = (UIText) activity.findViewById(R.id.fragment_booking_review_date_to_daynumber);
        textDateToDayName = (UIText) activity.findViewById(R.id.fragment_booking_review_date_to_dayname);
        textDateToMonth = (UIText) activity.findViewById(R.id.fragment_booking_review_date_to_month);
        linearContainerDateFrom = (LinearLayout) activity.findViewById(R.id.fragment_booking_review_linear_date_from);
        linearContainerDateTo = (LinearLayout) activity.findViewById(R.id.fragment_booking_review_linear_date_to);
        btnCallPhone = (ImageButton) activity.findViewById(R.id.fragment_booking_review_phone_call);
        textProductTitle = (UIText) activity.findViewById(R.id.fragment_booking_review_data_title);
        textProductDesc = (UIText) activity.findViewById(R.id.fragment_booking_review_data_desc);
        viewImageAssets = (ImageView) activity.findViewById(R.id.fragment_booking_review_data_image);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.fragment_booking_review_back);
        btnBookNow = (UIButton) activity.findViewById(R.id.fragment_booking_review_book);
    }
    private void initAction()
    {
        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBookNow();
            }
        });
        applyMember();
        applyDate();
    }

    private void applyMember()
    {
        PrefAuthentication auth = new PrefAuthentication(activity);
        textMemberEmail.setText(auth.getKeyUserEmail());
        textMemberPhone.setText(auth.getKeyUserPhone());
        textMemberName.setText(auth.getKeyUserFullname());
    }

    private void applyDate()
    {
        int newWidth = widthScreen / 2;
        linearContainerDateFrom.getLayoutParams().width = newWidth;
        linearContainerDateTo.getLayoutParams().width = newWidth;
        convertDateFrom();
        convertDateTo();
    }

    private void applyPaymentDetail()
    {
        textPaymentProductPackage.setText(dataPackagePriceTitle);
        textPaymentPrice.setText(dataPackagePriceValue);
        textPaymentQuota.setText(quota);
        textPaymentTotalPrice.setText(dataPackagePriceValue);
        textProductTitle.setText(dataProductTitle);
        textProductDesc.setText(dataProductDesc);
        imageLoader.showImage(HelperNative.getURL(11171) + dataProductImage, viewImageAssets);
    }

    private void convertDateFrom()
    {
        SimpleDateFormat formatDefault = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date valDate = formatDefault.parse(dateStart);
            SimpleDateFormat formatDNumber = new SimpleDateFormat("d");
            SimpleDateFormat formatMonth = new SimpleDateFormat("MMM");
            SimpleDateFormat formatDName = new SimpleDateFormat("EEE");
            String newValueDNumber = formatDNumber.format(valDate);
            String newValueDName = formatDName.format(valDate);
            String newValueMonth = formatMonth.format(valDate);
            textDateFromMonth.setText(newValueMonth);
            textDateFromDayNumber.setText(newValueDNumber);
            textDateFromDayName.setText(newValueDName);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void convertDateTo()
    {
        SimpleDateFormat formatDefault = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date valDate = formatDefault.parse(dateEnd);
            SimpleDateFormat formatDNumber = new SimpleDateFormat("d");
            SimpleDateFormat formatMonth = new SimpleDateFormat("MMM");
            SimpleDateFormat formatDName = new SimpleDateFormat("EEE");
            String newValueDNumber = formatDNumber.format(valDate);
            String newValueDName = formatDName.format(valDate);
            String newValueMonth = formatMonth.format(valDate);
            textDateToMonth.setText(newValueMonth);
            textDateToDayNumber.setText(newValueDNumber);
            textDateToDayName.setText(newValueDName);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void call()
    {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + phoneNumberOffice));
        startActivity(call);
    }

    private void getData()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "product", "package", "quota"};
                String[] value = new String[]{auth.getKeyUserToken(), product, packagePrice, quota};
                ControllerBooking booking = new ControllerBooking(activity);
                booking.setParameter(field, value);
                booking.executeBookingReview();
                if(booking.getSuccess())
                {
                    success = true;
                    dataProductID = booking.getProductID().get(0);
                    dataProductUser = booking.getProductUser().get(0);
                    dataProductTitle = booking.getProductTitle().get(0);
                    dataProductImage = booking.getProductImage().get(0);
                    dataProductDesc = booking.getProductDesc().get(0);
                    dataPackagePriceTitle = booking.getPackagePriceTitle().get(0);
                    dataPackagePriceDesc = booking.getPackagePriceDesc().get(0);
                    dataPackagePriceDiscount = booking.getPackagePriceDiscount().get(0);
                    dataPackagePriceValue = booking.getPackagePriceValue().get(0);
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    applyPaymentDetail();
                }
                else
                {
                    new UIToast(activity, msg).show();
                }

            }
        }.execute();
    }

    private void doBookNow()
    {
        new AsyncTask<Void,Integer, String>()
        {
            boolean success = false;
            String msg;
            UIDialogLoading dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new UIDialogLoading(activity);
                dialog.setCancelable(false);
                dialog.show();
                btnBookNow.setEnabled(false);
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token","product","package","start_date","end_date","start_hour","end_hour","quota","unit","price"};
                String[] value = new String[]{auth.getKeyUserToken(),product,packagePrice,dateStart,dateEnd,hourStart,
                        hourEnd,quota,unit, dataPackagePriceValue};
                ControllerBooking booking = new ControllerBooking(activity);
                booking.setParameter(field, value);
                booking.executeBookNow();
                if(booking.getSuccess())
                {
                    success = true;
                }
                msg = booking.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                btnBookNow.setEnabled(true);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(success)
                {
                    activity.removeFragment(1);
                    activity.onBackPressed();
                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }
}
