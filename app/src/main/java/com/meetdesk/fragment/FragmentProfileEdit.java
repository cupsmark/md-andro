package com.meetdesk.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.controller.ControllerGeneral;
import com.meetdesk.external.uil.core.assist.FailReason;
import com.meetdesk.external.uil.core.listener.ImageLoadingListener;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.helper.HelperNative;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UICircleImageView;
import com.meetdesk.view.UIDialogConfirm;
import com.meetdesk.view.UIDialogLoading;
import com.meetdesk.view.UIEditText;
import com.meetdesk.view.UIText;
import com.meetdesk.view.UIToast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ekobudiarto on 11/5/16.
 */
public class FragmentProfileEdit extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_PROFILE_EDIT = "tag:fragment-profile-edit";

    ImageButton imagebuttonBack;
    UICircleImageView circleImage;
    UIEditText editFullname, editEmail, editPassword, editContact, editDesc;
    UIText editLocation;
    UIButton buttonBirthday, buttonSave;
    LinearLayout buttonMale, buttonFemale;
    RelativeLayout circleMale, circleFemale;
    DatePickerDialog dateDialog;
    LazyImageLoader imageLoader;
    int selectedGender = 0;
    String selectedBirthday = "", currentLocation = "", selectedImagePath = "";
    double currentLatitude = 0, currentLongitude = 0;
    ArrayList<BaseFragment> fragmentUpdate;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_FILE = 3;
    Location mLastLocation;
    GoogleApiClient mGoogleApi;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleAPI();
        first_load(savedInstanceState);
    }

    @Override
    public void onStart() {
        mGoogleApi.connect();
        super.onStart();
        if(activity != null) {
            init();
            showNotifEnableGPS();
            componentAction();
            getData();
        }
    }

    @Override
    public void onStop() {
        mGoogleApi.disconnect();
        super.onStop();
    }

    private void init()
    {
        fragmentUpdate = getFragmentUpdate();
        imageLoader = new LazyImageLoader(activity);
        imagebuttonBack = (ImageButton) activity.findViewById(R.id.profile_edit_imagebutton_back);
        circleImage = (UICircleImageView) activity.findViewById(R.id.profile_edit_circleimage_avatar);
        editFullname = (UIEditText) activity.findViewById(R.id.profile_edit_fullname);
        editEmail = (UIEditText) activity.findViewById(R.id.profile_edit_email);
        editPassword = (UIEditText) activity.findViewById(R.id.profile_edit_password);
        editContact = (UIEditText) activity.findViewById(R.id.profile_edit_contact);
        editLocation = (UIText) activity.findViewById(R.id.profile_edit_location);
        editDesc = (UIEditText) activity.findViewById(R.id.profile_edit_about);
        buttonBirthday = (UIButton) activity.findViewById(R.id.profile_edit_birthday);
        buttonMale = (LinearLayout) activity.findViewById(R.id.profile_edit_radio_male);
        buttonFemale = (LinearLayout) activity.findViewById(R.id.profile_edit_radio_female);
        buttonSave = (UIButton) activity.findViewById(R.id.profile_edit_save);
        circleMale = (RelativeLayout) activity.findViewById(R.id.profile_edit_holder_circle_male);
        circleFemale = (RelativeLayout) activity.findViewById(R.id.profile_edit_holder_circle_female);
    }

    private void componentAction()
    {
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
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
        final PrefAuthentication auth = new PrefAuthentication(activity);
        editFullname.setText(auth.getKeyUserFullname());
        editEmail.setText(auth.getKeyUserEmail());
        editContact.setText(auth.getKeyUserPhone());
        editLocation.setText(auth.getKeyUserLocation());
        editDesc.setText(auth.getKeyUserDesc());
        setDefaultDate(auth.getKeyUserBirthdate());
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
        if(!HelperGeneral.checkInternalFile(auth.getKeyUserAvatar(), activity))
        {
            imageLoader.showImage(HelperNative.getURL(11171) + auth.getKeyUserAvatar(), circleImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    HelperGeneral.saveBitmapInternal(auth.getKeyUserAvatar(), activity, loadedImage);
                    circleImage.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        if(HelperGeneral.checkInternalFile(auth.getKeyUserAvatar(), activity))
        {
            imageLoader.showImage(HelperGeneral.getPrivatePath(auth.getKeyUserAvatar(), activity), circleImage);
        }
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    selectedBirthday = sdf.format(newDate.getTime());
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
                    String newDate2 = sdf2.format(newDate.getTime());
                    buttonBirthday.setText(newDate2);
                }
                else
                {
                    new UIToast(activity, "This app required age minimum 10 years old. Please select your valid birthdate.").show();
                }
                dateDialog.dismiss();
            }
        }, y, m, d);
        dateDialog.show();
    }

    private void setDefaultDate(String date)
    {
        if(!date.equals(""))
        {
            selectedBirthday = date;
            SimpleDateFormat sdfDefault = new SimpleDateFormat("yyyy-MM-dd");
            String newDate = "";
            try {
                Date defaultDate = sdfDefault.parse(date);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
                newDate = sdf2.format(defaultDate);
            } catch (ParseException e) {
                //e.printStackTrace();
                newDate = "";
            }
            buttonBirthday.setText(newDate);
        }
        else
        {
            Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            c.set(y - 10, m, d);
            SimpleDateFormat sdfDefault = new SimpleDateFormat("yyyy-MM-dd");
            selectedBirthday = sdfDefault.format(c.getTime());
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
            String newDate2 = sdf2.format(c.getTime());
            buttonBirthday.setText(newDate2);
        }
    }
    private void doUpdateProfile()
    {
        new AsyncTask<Void, Integer, String>()
        {
            boolean success = false;
            String msg;
            UIDialogLoading dialog;
            String valueFullname, valuePassword, valueBirthdate,
                    valueGender, valuePhone, valueDesc,
                    valueLatitude, valueLongitude, valueLocation, valueMedia;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new UIDialogLoading(activity);
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
                valueLocation = currentLocation;
                valueMedia = selectedImagePath;
                HelperGeneral.closeKeyboard(activity);
            }

            @Override
            protected String doInBackground(Void... params) {
                PrefAuthentication auth = new PrefAuthentication(activity);
                String[] field = new String[]{"token", "fullname", "password", "birthdate", "gender", "phone", "latitude", "longitude", "desc", "location", "media_file"};
                String[] value = new String[]{auth.getKeyUserToken(), valueFullname, valuePassword, valueBirthdate, valueGender, valuePhone, valueLatitude, valueLongitude, valueDesc, valueLocation, valueMedia};
                if(!valueMedia.equals(""))
                {
                    String resultFilename = HelperGeneral.upload(HelperNative.getURL(11185), valueMedia, field, value);
                    value[10] = HelperGeneral.convertJSONToPathImage(resultFilename);
                }
                ControllerGeneral general = new ControllerGeneral(activity);
                general.setPostParameter(field, value);
                general.executeUpdateAccount();
                if(general.getSuccess())
                {
                    success = true;
                    auth.setKeyUserFullname(valueFullname);
                    auth.setKeyUserBirthdate(valueBirthdate);
                    auth.setKeyUserGender(valueGender);
                    auth.setKeyUserPhone(valuePhone);
                    auth.setKeyUserLat(valueLatitude);
                    auth.setKeyUserLong(valueLongitude);
                    auth.setKeyUserDesc(valueDesc);
                    auth.setKeyUserLocation(valueLocation);
                    if(!value[10].equals("0"))
                    {
                        auth.setKeyUserAvatar(value[10]);
                    }
                    auth.commit();
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
                    for(int i = 0;i < fragmentUpdate.size();i++)
                    {
                        fragmentUpdate.get(i).onUpdate();
                    }
                    activity.onBackPressed();
                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }

    private void initGoogleAPI()
    {
        if (mGoogleApi == null) {
            mGoogleApi = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    private void initMap()
    {
        /*map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                ArrayList<String> getAddr = HelperGeneral.getAddressMaps(activity, currentLatitude, currentLongitude);
                if (getAddr.size() > 3) {
                    String city = getAddr.get(0);
                    String country = getAddr.get(3);
                    currentLocation = city + ", " + country;
                    editLocation.setText(currentLocation);
                }
            }
        });*/
    }

    private void showNotifEnableGPS()
    {
        if(!HelperGeneral.isLocationEnabled(activity))
        {
            new UIToast(activity, "Please turn on your GPS Location").show();
        }
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("imageCaptureUri", mImageCaptureUri);
    }

    private void first_load(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            mImageCaptureUri = savedInstanceState.getParcelable("imageCaptureUri");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK || resultCode != activity.RESULT_CANCELED)
        {
            switch (requestCode) {

                case PICK_FROM_FILE:
                    mImageCaptureUri = null;
                    mImageCaptureUri = data.getData();
                    onChoosePicture();
                    break;
            }
        }
    }

    private void onChoosePicture()
    {
        new AsyncTask<Void,Integer, String>(){

            Bitmap bm;

            @Override
            protected String doInBackground(Void... voids) {
                String urls = "";
                if(mImageCaptureUri != null)
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    String fi = getPath(activity, mImageCaptureUri);
                    Uri r = Uri.parse(fi);
                    File f = new File(r.getPath().toString());
                    urls = f.getAbsolutePath();
                    bm = HelperGeneral.ResizeBitmap(urls, 400, 400, false);
                }
                else
                {
                    urls = "";

                }

                return urls;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null && !s.equals("") && bm != null)
                {
                    selectedImagePath = s;
                    circleImage.setImageBitmap(bm);
                }
            }
        }.execute();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && android.provider.DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = android.provider.DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = android.provider.DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApi);
        if(mLastLocation != null)
        {
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();
            ArrayList<String> getAddr = HelperGeneral.getAddressMaps(activity, currentLatitude, currentLongitude);
            if (getAddr.size() > 3) {
                String city = getAddr.get(0);
                String country = getAddr.get(3);
                currentLocation = city + ", " + country;
                if(editLocation != null)
                {
                    editLocation.setText(currentLocation);
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
