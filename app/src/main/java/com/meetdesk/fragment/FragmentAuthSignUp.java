package com.meetdesk.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerAuthentication;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIEditText;
import com.meetdesk.view.UIText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ekobudiarto on 11/2/16.
 */
public class FragmentAuthSignUp extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_AUTH_SIGNUP = "tag:fragment-auth-signup";

    UIEditText editName, editEmail, editPassword;
    LinearLayout buttonChooseMale, buttonChooseFemale;
    UIButton buttonBirthday, buttonSignup;
    RelativeLayout iconButtonMale, iconButtonFemale;
    UIText textButtonMale, textButtonFemale;
    ImageView imageviewBG;
    LazyImageLoader imageLoader;
    DatePickerDialog dateDialog;
    String fullname, email, password, birthdate;
    int gender = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_auth_signup, container, false);
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
        }
    }

    private void init()
    {
        birthdate = "";
        imageLoader = new LazyImageLoader(activity);
        editName = (UIEditText) activity.findViewById(R.id.auth_signup_name);
        editEmail = (UIEditText) activity.findViewById(R.id.auth_signup_email);
        editPassword = (UIEditText) activity.findViewById(R.id.auth_signup_password);
        buttonChooseMale = (LinearLayout) activity.findViewById(R.id.auth_signup_male);
        buttonChooseFemale = (LinearLayout) activity.findViewById(R.id.auth_signup_female);
        buttonBirthday = (UIButton) activity.findViewById(R.id.auth_signup_birthday);
        iconButtonMale = (RelativeLayout) activity.findViewById(R.id.auth_signup_male_icon);
        iconButtonFemale = (RelativeLayout) activity.findViewById(R.id.auth_signup_female_icon);
        textButtonMale = (UIText) activity.findViewById(R.id.auth_signup_male_text);
        textButtonFemale = (UIText) activity.findViewById(R.id.auth_signup_female_text);
        buttonSignup = (UIButton) activity.findViewById(R.id.auth_signup_send);
        imageviewBG = (ImageView) activity.findViewById(R.id.auth_signup_bg);

        iconButtonFemale.setBackgroundResource(R.drawable.bg_circle_grey);
        textButtonFemale.setTextColor(HelperGeneral.getColor(activity, R.color.base_text_normal));
        buttonChooseMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 0;
                iconButtonMale.setBackgroundResource(R.drawable.bg_circle_white);
                textButtonMale.setTextColor(HelperGeneral.getColor(activity, R.color.base_white));
                iconButtonFemale.setBackgroundResource(R.drawable.bg_circle_grey);
                textButtonFemale.setTextColor(HelperGeneral.getColor(activity, R.color.base_text_normal));
            }
        });
        buttonChooseFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 1;
                iconButtonMale.setBackgroundResource(R.drawable.bg_circle_grey);
                textButtonMale.setTextColor(HelperGeneral.getColor(activity, R.color.base_text_normal));
                iconButtonFemale.setBackgroundResource(R.drawable.bg_circle_white);
                textButtonFemale.setTextColor(HelperGeneral.getColor(activity, R.color.base_white));
            }
        });
        buttonBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
        imageLoader.showImage("drawable://"+R.drawable.bg_auth, imageviewBG);
    }

    private void showDateDialog()
    {
        Calendar c = Calendar.getInstance();
        final int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        dateDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(year < (y - 10))
                {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
                    String newDate2 = sdf2.format(newDate.getTime());

                    SimpleDateFormat sdfOrigin = new SimpleDateFormat("yyyy-MM-dd");
                    String dateOrigin = sdfOrigin.format(newDate.getTime());
                    birthdate = dateOrigin;
                    buttonBirthday.setText(newDate2);
                }
                else
                {
                    Toast.makeText(activity, "This app required age minimum 10 years old. Please select your valid birthdate.", Toast.LENGTH_LONG).show();
                }
                dateDialog.dismiss();
            }
        }, y, m, d);
        dateDialog.show();
    }

    private void doRegister()
    {
        new AsyncTask<Void, Integer, String>()
        {

            ProgressDialog dialog;
            boolean success = false;
            String msg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setCancelable(false);
                dialog.show();

                fullname = editName.getText().toString();
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();

            }

            @Override
            protected String doInBackground(Void... params) {
                String[] field = new String[]{"token", "fullname", "email", "password", "gender", "birthdate", "devices_id"};
                String[] value = new String[]{HelperGeneral.getDeviceID(activity), fullname, email, password, Integer.toString(gender), birthdate, HelperGeneral.getDeviceID(activity)};
                ControllerAuthentication authentication = new ControllerAuthentication(activity);
                authentication.setParameter(field, value);
                authentication.doRegister();
                if(authentication.getSuccess())
                {
                    success = true;
                }
                msg = authentication.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                if(success)
                {
                    activity.onBackPressed();
                }
            }
        }.execute();
    }


}
