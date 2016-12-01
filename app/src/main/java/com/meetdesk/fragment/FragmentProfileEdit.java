package com.meetdesk.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerGeneral;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UICircleImageView;
import com.meetdesk.view.UIEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentProfileEdit extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_PROFILE_EDIT = "tag:fragment-profile-edit";

    ImageButton imagebuttonBack;
    UICircleImageView circleImage;
    UIEditText editFullname, editEmail, editPassword, editContact, editLocation, editDesc;
    UIButton buttonBirthday, buttonSave;
    LinearLayout buttonMale, buttonFemale;
    RelativeLayout circleMale, circleFemale;
    DatePickerDialog dateDialog;
    LazyImageLoader imageLoader;
    int selectedGender = 0;
    String selectedBirthday = "", currentLocation = "";
    SupportMapFragment mapFragment;
    double currentLatitude = 0, currentLongitude = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
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
            componentAction();
            initMap();
            getData();
        }
    }

    private void init()
    {
        imageLoader = new LazyImageLoader(activity);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.profile_edit_imagebutton_back);
        circleImage = (UICircleImageView) activity.findViewById(R.id.profile_edit_circleimage_avatar);
        editFullname = (UIEditText) activity.findViewById(R.id.profile_edit_fullname);
        editEmail = (UIEditText) activity.findViewById(R.id.profile_edit_email);
        editPassword = (UIEditText) activity.findViewById(R.id.profile_edit_password);
        editContact = (UIEditText) activity.findViewById(R.id.profile_edit_contact);
        editLocation = (UIEditText) activity.findViewById(R.id.profile_edit_location);
        editDesc = (UIEditText) activity.findViewById(R.id.profile_edit_location);
        buttonBirthday = (UIButton) activity.findViewById(R.id.profile_edit_birthday);
        buttonMale = (LinearLayout) activity.findViewById(R.id.profile_edit_radio_male);
        buttonFemale = (LinearLayout) activity.findViewById(R.id.profile_edit_radio_female);
        buttonSave = (UIButton) activity.findViewById(R.id.profile_edit_save);
        circleMale = (RelativeLayout) activity.findViewById(R.id.profile_edit_holder_circle_male);
        circleFemale = (RelativeLayout) activity.findViewById(R.id.profile_edit_holder_circle_female);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.profile_edit_map_hidden);
    }

    private void componentAction()
    {
        imageLoader.showImage(HelperGeneral.getAssetsPath("images/sample_avatar.jpg"), circleImage);
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        editEmail.setEnabled(false);
        editLocation.setEnabled(false);
        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = 0;
                circleMale.setBackgroundResource(R.drawable.bg_circle_blue);
                circleFemale.setBackgroundResource(R.drawable.bg_circle_border_grey);
            }
        });
        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = 1;
                circleMale.setBackgroundResource(R.drawable.bg_circle_border_grey);
                circleFemale.setBackgroundResource(R.drawable.bg_circle_blue);
            }
        });
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdateProfile();
            }
        });
        buttonBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void getData()
    {
        PrefAuthentication auth = new PrefAuthentication(activity);
        editFullname.setText(auth.getKeyUserFullname());
        editEmail.setText(auth.getKeyUserEmail());
        editContact.setText(auth.getKeyUserPhone());
        editLocation.setText(auth.getKeyUserLocation());
        buttonBirthday.setText(auth.getKeyUserBirthdate());
        if(auth.getKeyUserGender().equals("0"))
        {
            circleMale.setBackgroundResource(R.drawable.bg_circle_blue);
            circleFemale.setBackgroundResource(R.drawable.bg_circle_border_grey);
        }
        if(!auth.getKeyUserGender().equals("0"))
        {
            circleMale.setBackgroundResource(R.drawable.bg_circle_border_grey);
            circleFemale.setBackgroundResource(R.drawable.bg_circle_blue);
        }
    }

    private void showDateDialog()
    {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        dateDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                selectedBirthday = sdf.format(newDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
                String newDate2 = sdf2.format(newDate.getTime());
                buttonBirthday.setText(newDate2);
                dateDialog.dismiss();
            }
        }, y, m, d);
        dateDialog.show();
    }

    private void doUpdateProfile()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            ProgressDialog dialog;
            String valueFullname, valuePassword, valueBirthdate,
                    valueGender, valuePhone, valueDesc,
                    valueLatitude, valueLongitude;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setCancelable(false);
                dialog.show();

                valueFullname = editFullname.getText().toString();
                valuePassword = editPassword.getText().toString();
                valueBirthdate = selectedBirthday;
                valueGender = Integer.toString(selectedGender);
                valuePhone = editContact.getText().toString();
                valueDesc = editDesc.getText().toString();
                valueLatitude = Double.toString(currentLatitude);
                valueLongitude = Double.toString(currentLongitude);
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "fullname", "password", "birthdate", "gender", "phone", "latitude", "longitude", "desc"};
                String[] value = new String[]{auth.getKeyUserToken(), valueFullname, valuePassword, valueBirthdate, valueGender, valuePhone, valueLatitude, valueLongitude, valueDesc};
                ControllerGeneral general = new ControllerGeneral(activity);
                general.setPostParameter(field, value);
                general.executeUpdateAccount();
                if(general.getSuccess())
                {
                    success = true;
                }
                msg = general.getMessage();
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                if(success)
                {
                    activity.onBackPressed();
                }
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void initMap()
    {
        mapFragment.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapFragment.getMap().setMyLocationEnabled(true);
        mapFragment.getMap().setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                ArrayList<String> getAddr = HelperGeneral.getAddressMaps(activity, currentLatitude, currentLongitude);
                if(getAddr.size() > 3)
                {
                    String city = getAddr.get(0);
                    String country = getAddr.get(3);
                    currentLocation = city + ", " + country;
                    editLocation.setText(currentLocation);
                }
            }
        });
    }
}
