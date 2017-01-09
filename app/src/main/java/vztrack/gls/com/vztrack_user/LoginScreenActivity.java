package vztrack.gls.com.vztrack_user;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapter_search_provider;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.RatingBean;
import vztrack.gls.com.vztrack_user.beans.UserBean;
import vztrack.gls.com.vztrack_user.fragment.NoticesFragment;
import vztrack.gls.com.vztrack_user.fragment.RatingFragment;
import vztrack.gls.com.vztrack_user.fragment.SearchProviderFragment;
import vztrack.gls.com.vztrack_user.fragment.SettingFragment;
import vztrack.gls.com.vztrack_user.fragment.VisitorsFragment;
import vztrack.gls.com.vztrack_user.gcm.GCMClientManager;
import vztrack.gls.com.vztrack_user.profile.VisitorsArray;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.LogoutResponse;
import vztrack.gls.com.vztrack_user.responce.NoticesResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class LoginScreenActivity extends BaseActivity {

    EditText etPassword,etUsername;
    private int InternetCheckFlag = 0;
    private String strSocietyName,strFlatNo,wingName,strUsername, strPassword;
    private int strSocietyId,strFamilyId;
    private String reg_id;
    private String device_id;
    String PROJECT_NUMBER="1082591809215";
    FancyButton etSubmit;
    LinearLayout LoginLayout, SplashLayout;
    public static boolean splashFlag = true;
    private static final int REQUEST_CODE = 1234;
    CleverTapAPI ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login_screen);

        SheredPref.setSound(this,"ENABLE");
        SheredPref.setVibration(this,"ENABLE");
        SheredPref.setNotification(this,"ENABLE");

        LoginLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        SplashLayout = (LinearLayout) findViewById(R.id.splash);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);

        etSubmit = (FancyButton) findViewById(R.id.etSubmit);

        CheckConnection cc = new CheckConnection(getApplicationContext());

        CleverTap.cleverTap_Record_Event(this, Events.event_login);

        Boolean isConnectingToInternet = cc.isConnectingToInternet();

        SplashLayout.setVisibility(View.VISIBLE);

        if(splashFlag==true)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginLayout.setVisibility(View.VISIBLE);
                    SplashLayout.setVisibility(View.GONE);

                }
            }, 2000);
        }
        else
        {
            LoginLayout.setVisibility(View.VISIBLE);
            SplashLayout.setVisibility(View.GONE);
        }

        if(isConnectingToInternet)
        {
            CheckConnection ccAccess = new CheckConnection(getApplicationContext());

            Boolean isInternetAvailable = ccAccess.isConnectingToInternet();

            if(isInternetAvailable)
            {
                InternetCheckFlag = 1;
            }
            else
            {
                InternetCheckFlag = 0;
                Intent ConnCheck = new Intent(this,NoInternetConnection.class);
                startActivity(ConnCheck);
            }
        }
        else
        {
            InternetCheckFlag = 0;
            Intent ConnCheck = new Intent(this,NoInternetConnection.class);
            startActivity(ConnCheck);
        }

        if(InternetCheckFlag==1)
        {
            // Generate Device Id
            device_id = Secure.getString(this.getContentResolver(),Secure.ANDROID_ID);
            // GCM ID
            GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    reg_id = registrationId;
                    //send this registrationId to your server
                }
                @Override
                public void onFailure(String ex) {
                    super.onFailure(ex);
                }
            });
        }
        getSupportActionBar().hide();

    }

    public void Submit(View v)
    {
        etSubmit.setEnabled(false);
        if(etPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"User Name required",Toast.LENGTH_LONG).show();
            etSubmit.setEnabled(true);
        }
        else if(etUsername.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Password required",Toast.LENGTH_LONG).show();
            etSubmit.setEnabled(true);
        }
        else
        {
            if(InternetCheckFlag == 1)
            {
                SheredPref.setDateForApi(this,"");
                UserBean userBean = new UserBean();
                userBean.setUser_name(etUsername.getEditableText().toString().trim());
                userBean.setUser_password(etPassword.getEditableText().toString().trim());
                userBean.setUser_dev_id(device_id);
                userBean.setUser_gcm_id(reg_id);
                userBean.setDevice_os("AND");
                new PostData(new Gson().toJson(userBean), LoginScreenActivity.this, CallFor.LOGIN).execute();
                etSubmit.setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginScreenActivity.this);
        alert.setMessage("Are you sure exit from application?");
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("exit", "app EXIT");
                dialogInterface.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        alert.show();
    }

    public void loadTutorial() {
        getSupportActionBar().hide();
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_vistor_title, R.string.slide_1_visitor_subtitle,
                R.color.slide_1, R.drawable.slide1,  R.drawable.slide1);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_notice_title, R.string.slide_2_notice_subtitle,
                R.color.slide_2,  R.drawable.slide2,  R.drawable.slide2);



        TutorialItem tutorialItem3 = new TutorialItem(R.string.slide_3_feedback_title, R.string.slide_3_feedback_subtitle,
                R.color.slide_3,  R.drawable.slide3,  R.drawable.slide3);

       /* TutorialItem tutorialItem3 = new TutorialItem(R.string.slide_3_feedback_title,R.string.slide_3_feedback_subtitle ,
                R.color.slide_3, R.drawable.tut_page_3_foreground);*/

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_search_provider_title, R.string.slide_4_search_provider_subtitle,
                R.color.slide_4,  R.drawable.slide4, R.drawable.slide4);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //SheredPref.setTutorialFlag(this,false);
            StartMainActivity();
        }
    }

    public void StartMainActivity(){
        SheredPref.setLoginInfo(this,"LoggedIn");
        SheredPref.setUSername(this,strUsername);
        SheredPref.setPassword(this,strPassword);
        SheredPref.setSociety_Name(this,strSocietyName);
        SheredPref.setFlat_No(this,strFlatNo);
        SheredPref.setWingName(this,wingName);
        SheredPref.setSocietyId(this,""+strSocietyId);
        SheredPref.setFamilyId(this,""+strFamilyId);
        SheredPref.setNotification(this,"ENABLE");
        Intent intent = new Intent(LoginScreenActivity.this,MainActivity.class);
        intent.putExtra("CALL","FROM_LOGIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SheredPref.setTutorialFlag(LoginScreenActivity.this,true);
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        LoginResponse loginResponse=new Gson().fromJson(response,LoginResponse.class);
        if (response == null) {
            return;
        }
        if (callFor.equals(CallFor.LOGIN)) {
            etSubmit.setEnabled(true);
            try {
                if (loginResponse.getCode().equals("SUCCESS")){
                    strSocietyName = loginResponse.getSocity().getSocity_name();
                    strFlatNo = loginResponse.getFamily().getFlatNo();
                    wingName = loginResponse.getFamily().getWing();
                    strSocietyId = loginResponse.getFamily().getSocietyId();
                    strFamilyId = loginResponse.getFamily().getFamilyId();
                    strUsername=etUsername.getEditableText().toString().trim();
                    strPassword=etPassword.getEditableText().toString().trim();

                    loadTutorial();

                    etUsername.setText("");
                    etPassword.setText("");

                }
                else if (loginResponse.getCode().equals("ERROR")) {
                    Toast.makeText(this,loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Invalid Details",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
