package com.meetdesk.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerBooking;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIEditText;
import com.meetdesk.view.UIText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 12/4/16.
 */
public class FragmentBookingForm extends BaseFragment {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_SETTINGS = "tag:fragment-settings";

    LinearLayout containerInputSelectDateFrom, containerInputSelectDateTo;
    int widthScreen = 0;
    RelativeLayout btnSelectDateStart, btnSelectDateEnd, btnSelectUnit, containerSelectHour, btnSelectHourStart, btnSelectHourEnd;
    UIText textSelectDateStart, textSelectDateEnd, textSelectUnit, textSelectHourStart, textSelectHourEnd;
    UIEditText editTextQuota;
    String selectedDate = "", selectedTime = "", selectedDateStart = "", selectedDateEnd = "", selectedHourStart = "",
            selectedHourEnd = "", selectedUnit = "daily";
    String product= "", packagePrice = "";
    String[] itemUnitTitle = new String[]{"Hourly", "Daily"};
    String[] itemUnitID = new String[]{"hourly", "daily"};
    UIButton btnCheckAvailability, btnNextStep;
    Map<String, String> param;
    ImageButton imagebuttonBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_booking_form, container, false);
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
            initComponent();
            setDateDefault();
        }
    }

    private void init()
    {
        widthScreen = HelperGeneral.getScreenSize(activity, "w");
        param = getParameter();
        product = param.get("product");
        packagePrice = param.get("package");
        containerInputSelectDateFrom = (LinearLayout) activity.findViewById(R.id.fragment_booking_form_select_date_from);
        containerInputSelectDateTo = (LinearLayout) activity.findViewById(R.id.fragment_booking_form_select_date_to);
        containerSelectHour = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_container_hour);
        btnSelectDateStart = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_select_date_from_choice);
        btnSelectDateEnd = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_select_date_to_choice);
        btnSelectUnit = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_unit_select);
        btnSelectHourStart = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_select_hour_from_choice);
        btnSelectHourEnd = (RelativeLayout) activity.findViewById(R.id.fragment_booking_form_select_hour_to_choice);
        textSelectDateStart = (UIText) activity.findViewById(R.id.fragment_booking_form_select_date_from_choice_selected);
        textSelectDateEnd = (UIText) activity.findViewById(R.id.fragment_booking_form_select_date_to_choice_selected);
        textSelectUnit = (UIText) activity.findViewById(R.id.fragment_booking_form_unit_select_value);
        textSelectHourStart = (UIText) activity.findViewById(R.id.fragment_booking_form_select_hour_from_choice_selected);
        textSelectHourEnd = (UIText) activity.findViewById(R.id.fragment_booking_form_select_hour_to_choice_selected);
        editTextQuota = (UIEditText) activity.findViewById(R.id.fragment_booking_form_quota_edittext);
        btnCheckAvailability = (UIButton) activity.findViewById(R.id.fragment_booking_form_check_availability);
        btnNextStep = (UIButton) activity.findViewById(R.id.fragment_booking_form_next_step);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.fragment_booking_form_back);
    }

    private void initComponent()
    {
        int indexDefaultUnit = Arrays.asList(itemUnitID).indexOf(selectedUnit);
        textSelectUnit.setText(itemUnitTitle[indexDefaultUnit]);
        imagebuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        btnSelectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateStartDialog();
            }
        });
        btnSelectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateEndDialog();
            }
        });
        btnSelectHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeStartDialog();
            }
        });
        btnSelectHourEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeEndDialog();
            }
        });
        btnSelectUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogUnit();
            }
        });
        btnCheckAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckAvailability();
            }
        });
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextStep();
            }
        });
    }

    private void setDateDefault()
    {
        Calendar mCalendar = Calendar.getInstance();
        int y = mCalendar.get(Calendar.YEAR);
        int m = mCalendar.get(Calendar.MONTH);
        int d = mCalendar.get(Calendar.DAY_OF_MONTH);
        int h = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        mCalendar.set(y, m, d, h, min);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        selectedDate = formatDate.format(mCalendar.getTime());
        selectedTime = formatTime.format(mCalendar.getTime());
        selectedDateEnd = selectedDate;
        selectedDateStart = selectedDate;
        selectedHourStart = selectedTime;
        selectedHourEnd = selectedTime;
        textSelectDateStart.setText(getConvertedDate(selectedDateStart));
        textSelectDateEnd.setText(getConvertedDate(selectedDateEnd));
        textSelectHourStart.setText(getConvertedTime(selectedHourStart));
        textSelectHourEnd.setText(getConvertedTime(selectedHourEnd));
    }

    private void openDateStartDialog()
    {
        final String selectedCurrent = selectedDateStart;
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(year, month, day);
        DatePickerDialog dateDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateStart = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
            }
        }, year, month, day);
        dateDialog.setFirstDayOfWeek(Calendar.MONDAY);
        dateDialog.setMinDate(mCalendar);
        dateDialog.setOkText(activity.getResources().getString(R.string.dialog_confirm_yes));
        dateDialog.setCancelText(activity.getResources().getString(R.string.dialog_confirm_no));
        dateDialog.setThemeDark(false);
        dateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                selectedDateStart = selectedCurrent;
            }
        });
        dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String dateConverted = getConvertedDate(selectedDateStart);
                textSelectDateStart.setText(dateConverted);
            }
        });
        dateDialog.show(activity.getFragmentManager(), "");
    }

    private void openDateEndDialog()
    {

        SimpleDateFormat formatDateMin = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final String selectedDateCurrent = selectedDateStart;
            Date dateMin = formatDateMin.parse(selectedDateStart);
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(dateMin.getTime());
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dateDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    selectedDateEnd = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);
                }
            }, year, month, day);
            dateDialog.setFirstDayOfWeek(Calendar.MONDAY);
            dateDialog.setMinDate(mCalendar);
            dateDialog.setOkText(activity.getResources().getString(R.string.dialog_confirm_yes));
            dateDialog.setCancelText(activity.getResources().getString(R.string.dialog_confirm_no));
            dateDialog.setThemeDark(false);
            dateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    selectedDateEnd = selectedDateCurrent;
                }
            });
            dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    String dateConverted = getConvertedDate(selectedDateEnd);
                    textSelectDateEnd.setText(dateConverted);
                }
            });
            dateDialog.show(activity.getFragmentManager(), "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void openTimeStartDialog(){
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        final int min = mCalendar.get(Calendar.MINUTE);
        final int sec = mCalendar.get(Calendar.SECOND);
        mCalendar.set(year, month, day, hour, min, sec);
        final String currentTime = selectedHourStart;
        int hourClose = 17;
        TimePickerDialog timeDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                selectedHourStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
            }
        }, hour, min, sec, true);
        timeDialog.setStartTime(hour, min);
        timeDialog.setMinTime(hour, min, sec);
        timeDialog.setMaxTime(hourClose, 0, 0);
        timeDialog.setOkText(activity.getResources().getString(R.string.dialog_confirm_yes));
        timeDialog.setCancelText(activity.getResources().getString(R.string.dialog_confirm_no));
        timeDialog.setThemeDark(false);
        timeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                selectedHourStart = currentTime;
            }
        });
        timeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String timeConverted = getConvertedTime(selectedHourStart);
                textSelectHourStart.setText(timeConverted);
            }
        });
        timeDialog.show(activity.getFragmentManager(), "");
    }

    private void openTimeEndDialog(){
        SimpleDateFormat formatDateMin = new SimpleDateFormat("HH:mm:ss");
        try {
            final String currentTime = selectedHourStart;
            Date minDate = formatDateMin.parse(currentTime);
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(minDate.getTime());
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            final int min = mCalendar.get(Calendar.MINUTE);
            final int sec = mCalendar.get(Calendar.SECOND);
            mCalendar.set(year, month, day, hour, min, sec);
            int hourClose = 17;
            TimePickerDialog timeDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                    selectedHourEnd = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);
                }
            }, hour, min, sec, true);
            timeDialog.setStartTime(hour, min);
            timeDialog.setMinTime(hour, min, sec);
            timeDialog.setMaxTime(hourClose, 0, 0);
            timeDialog.setOkText(activity.getResources().getString(R.string.dialog_confirm_yes));
            timeDialog.setCancelText(activity.getResources().getString(R.string.dialog_confirm_no));
            timeDialog.setThemeDark(false);
            timeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    selectedHourEnd = currentTime;
                }
            });
            timeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    String timeConverted = getConvertedTime(selectedHourEnd);
                    textSelectHourEnd.setText(timeConverted);
                }
            });
            timeDialog.show(activity.getFragmentManager(), "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private String getConvertedDate(String date)
    {
        String result = "";
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateValue = format.parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMMM yyyy");
            result = newFormat.format(dateValue);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getConvertedTime(String time)
    {
        String result = "";
        try{
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date timeValue = format.parse(time);
            SimpleDateFormat newFormat = new SimpleDateFormat("HH mm a");
            result = newFormat.format(timeValue);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void showDialogUnit()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Select Unit")
                .setItems(itemUnitTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedUnit = itemUnitID[which];
                        textSelectUnit.setText(itemUnitTitle[which]);
                        if (selectedUnit.equals("hourly")) {
                            containerSelectHour.setVisibility(View.VISIBLE);
                        } else {
                            containerSelectHour.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void doCheckAvailability()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            ProgressDialog dialog;
            String msg, valueDateStart, valueDateEnd, valueHourStart, valueHourEnd, valueUnit, valueMember;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setCancelable(false);
                dialog.show();
                valueDateStart = selectedDateStart;
                valueDateEnd = selectedDateEnd;
                valueHourStart = selectedHourStart;
                valueHourEnd = selectedHourEnd;
                valueUnit = selectedUnit;
                valueMember = editTextQuota.getText().toString();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                PrefAuthentication authentication = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "product", "package", "start_date", "end_date", "start_hour", "end_hour", "quota"};
                String[] value = new String[]{authentication.getKeyUserToken(), product, packagePrice, valueDateStart, valueDateEnd, valueHourStart, valueHourEnd, valueMember};
                ControllerBooking booking = new ControllerBooking(activity);
                booking.setParameter(field, value);
                booking.executeCheckAvailability();
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
                if(dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                }
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void doNextStep()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg, valueDateStart, valueDateEnd, valueHourStart, valueHourEnd, valueUnit, valueMember, urlBuilder;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                valueDateStart = selectedDateStart;
                valueDateEnd = selectedDateEnd;
                valueHourStart = selectedHourStart;
                valueHourEnd = selectedHourEnd;
                valueUnit = selectedUnit;
                valueMember = editTextQuota.getText().toString();
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication authentication = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "product", "package", "start_date", "end_date", "start_hour", "end_hour", "quota"};
                String[] value = new String[]{authentication.getKeyUserToken(), product, packagePrice, valueDateStart, valueDateEnd, valueHourStart, valueHourEnd, valueMember};
                ControllerBooking booking = new ControllerBooking(activity);
                booking.setParameter(field, value);
                booking.executeCheckAvailability();
                if(booking.getSuccess())
                {
                    success = true;
                }
                else
                {
                    success = false;
                    msg = booking.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(success)
                {
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("product", product);
                    param.put("package", packagePrice);
                    param.put("start_date", valueDateStart);
                    param.put("end_date", valueDateEnd);
                    param.put("start_hour", valueHourStart);
                    param.put("end_hour", valueHourEnd);
                    param.put("quota", valueMember);
                    param.put("unit", valueUnit);
                    FragmentBookingReview bookingReview = new FragmentBookingReview();
                    bookingReview.setParameter(param);
                    iFragment.onNavigate(bookingReview, param);
                }
                else
                {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
