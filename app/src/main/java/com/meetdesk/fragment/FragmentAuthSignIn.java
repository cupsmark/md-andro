package com.meetdesk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.meetdesk.BaseActivity;
import com.meetdesk.BaseFragment;
import com.meetdesk.R;
import com.meetdesk.activity.MainActivity;
import com.meetdesk.controller.ControllerAuthentication;
import com.meetdesk.helper.HelperGeneral;
import com.meetdesk.model.PrefAuthentication;
import com.meetdesk.util.LazyImageLoader;
import com.meetdesk.view.UIButton;
import com.meetdesk.view.UIDialogLoading;
import com.meetdesk.view.UIEditText;
import com.meetdesk.view.UIToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ekobudiarto on 11/2/16.
 */
public class FragmentAuthSignIn extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener {

    View main_view;
    HelperGeneral.FragmentInterface iFragment;
    BaseActivity activity;
    public static final String TAG_FRAGMENT_AUTH_SIGNIN = "tag:fragment-auth-signin";
    private static final int RC_SIGN_IN = 9001;

    UIButton buttonSignIn, buttonCreateNew;
    ImageView imageviewBG;
    LazyImageLoader imageLoader;
    LoginButton btnLoginFB;
    CallbackManager callbackManager;
    UIEditText editUsername, editPassword;

    GoogleApiClient mGoogleApiClient;
    SignInButton btnLoginGoogle;
    boolean isLogout = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_auth_signin, container, false);
        return main_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        buildGoogle();
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
        imageLoader = new LazyImageLoader(activity);
        isLogout = activity.getIntent().getBooleanExtra("isLogout", false);
        buttonSignIn = (UIButton) activity.findViewById(R.id.auth_signin_send);
        buttonCreateNew = (UIButton) activity.findViewById(R.id.auth_signin_create);
        imageviewBG = (ImageView) activity.findViewById(R.id.auth_signin_bg);
        btnLoginFB = (LoginButton) activity.findViewById(R.id.auth_signin_button_facebook);
        btnLoginGoogle = (SignInButton) activity.findViewById(R.id.auth_signin_button_google);
        editUsername = (UIEditText) activity.findViewById(R.id.auth_signin_username);
        editPassword = (UIEditText) activity.findViewById(R.id.auth_signin_password);

        btnLoginFB.setReadPermissions(Arrays.asList("email"));
        btnLoginFB.setFragment(FragmentAuthSignIn.this);
        btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken fbToken = loginResult.getAccessToken();
                getFBUserdata(fbToken);
            }

            @Override
            public void onCancel() {
                new UIToast(activity, "Something wrong with this device. Please restart this application").show();
            }

            @Override
            public void onError(FacebookException error) {
                new UIToast(activity, "ERROR : " + error.getMessage().toString()).show();
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLoginGoogle();
            }
        });
        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> param = new HashMap<String, String>();
                FragmentAuthSignUp signUp = new FragmentAuthSignUp();
                iFragment.onNavigate(signUp, param);
            }
        });
        imageLoader.showImage("drawable://" + R.drawable.bg_auth, imageviewBG);
        doLogout();
    }

    private void setPreferencesLoggedIn()
    {
        PrefAuthentication authPreferences = new PrefAuthentication(activity);
        authPreferences.setIsLoggedIn(true);
        authPreferences.commit();
    }

    private void doLogin()
    {
        new AsyncTask<Void, Integer, String>()
        {

            UIDialogLoading dialog;
            boolean success = false;
            String msg, username, password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new UIDialogLoading(activity);
                dialog.setCancelable(false);
                dialog.show();

                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                HelperGeneral.closeKeyboard(activity);
            }

            @Override
            protected String doInBackground(Void... params) {
                String[] field = new String[]{"token", "username", "password", "device_id"};
                String[] value = new String[]{HelperGeneral.getDeviceID(activity), username, password, HelperGeneral.getDeviceID(activity)};
                ControllerAuthentication authentication = new ControllerAuthentication(activity);
                authentication.setParameter(field, value);
                authentication.doLogin();
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
                if(success)
                {
                    Intent i = new Intent(activity, MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }

    private void getFBUserdata(AccessToken token)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        String defaultGender = "0";
                        try {
                            if(!object.getString("gender").equals("male"))
                            {
                                defaultGender = "1";
                            }
                            JSONObject objPicture = object.getJSONObject("picture");
                            JSONObject objPictureData = objPicture.getJSONObject("data");
                            String sourceEngine = "facebook";
                            String tokenDefault = HelperGeneral.getDeviceID(activity);
                            String fullname = object.getString("name");
                            String email = object.getString("email");
                            String gender = defaultGender;
                            String birthdate = "";
                            String socialID = object.getString("id");
                            String avatar = objPictureData.getString("url");
                            String deviceID = tokenDefault;
                            String[] valueSend = new String[]{tokenDefault, sourceEngine, fullname, email, gender, birthdate, socialID, avatar, deviceID};
                            doLoginSocial(valueSend);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new UIToast(activity, "ERROR Server " + e.getMessage().toString()).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email, gender, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void buildGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                String sourceEngine = "google";
                String tokenDefault = HelperGeneral.getDeviceID(activity);
                String fullname = acct.getDisplayName();
                String email = acct.getEmail();
                String gender = "0";
                String birthdate = "";
                String socialID = acct.getId();
                String avatar = acct.getPhotoUrl().toString();
                String deviceID = tokenDefault;

                String[] valueSend = new String[]{tokenDefault, sourceEngine, fullname, email, gender, birthdate, socialID, avatar, deviceID};
                doLoginSocial(valueSend);
            }
        }
    }

    private void doLoginGoogle()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void doLoginSocial(final String[] value)
    {
        new AsyncTask<Void, Integer, String>()
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
            }

            @Override
            protected String doInBackground(Void... params) {
                String[] field = new String[]{"token", "source_engine", "fullname", "email", "gender", "birthdate", "social_id", "avatar", "device_id"};
                String[] newValue = new String[]{value[0], value[1], value[2], value[3], value[4], value[5], value[6], value[7], value[8]};
                ControllerAuthentication authentication = new ControllerAuthentication(activity);
                authentication.setParameter(field, newValue);
                authentication.doLoginSocial();
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
                if(success)
                {
                    Intent i = new Intent(activity, MainActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
                new UIToast(activity, msg).show();
            }
        }.execute();
    }

    public void doLogout()
    {
        if(isLogout)
        {
            String source = activity.getIntent().getStringExtra("userSource");
            if(source.equals("facebook"))
            {
                LoginManager.getInstance().logOut();
                new UIToast(activity, "Logout Success").show();
            }
            else if(source.equals("google"))
            {
                new UIToast(activity, "Logout Success").show();
                /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(activity, "Logout Success", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
            else
            {
                new UIToast(activity, "Logout Success").show();
                PrefAuthentication authPreferences = new PrefAuthentication(activity);
                authPreferences.setIsLoggedIn(false);
                authPreferences.setDisplaySelectType(false);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        new UIToast(activity, "Error Connection : " + connectionResult.getErrorMessage().toString()).show();
    }
}
