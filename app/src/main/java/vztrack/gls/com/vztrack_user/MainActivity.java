package vztrack.gls.com.vztrack_user;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapter_search_provider;
import vztrack.gls.com.vztrack_user.beans.DataObjectRating;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.beans.InputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.RatingBean;
import vztrack.gls.com.vztrack_user.beans.RatingResponseBean;
import vztrack.gls.com.vztrack_user.beans.UserBean;
import vztrack.gls.com.vztrack_user.fragment.NoticesFragment;
import vztrack.gls.com.vztrack_user.fragment.SearchProviderFragment;
import vztrack.gls.com.vztrack_user.fragment.RatingFragment;
import vztrack.gls.com.vztrack_user.fragment.SettingFragment;
import vztrack.gls.com.vztrack_user.fragment.VisitorsFragment;
import vztrack.gls.com.vztrack_user.profile.VisitorsArray;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.LogoutResponse;
import vztrack.gls.com.vztrack_user.responce.NoticesResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvFlatno,tvSocietyName,tvWing;
    private String strValidation;
    private NavigationView navigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction ft;
    String title="";
    public static NoticesResponse noticesResponse;
    public  static VisitorsArray visitorsArray;
    public  static ArrayList<RatingBean> ratingBeanArrayList = new ArrayList<RatingBean>();
    public  static ArrayList<OutputBeanSearchProvider> outputBeanSearchProviders = new ArrayList<OutputBeanSearchProvider>();
    public  static int fragment_flag=0;
    public ImageView imgHiddenImage;
    public static BaseActivity MainContext;
    CheckConnection cc;
    DbHelper dbHelper;
    private Menu menu;
    LinearLayout splashLayout;
    DrawerLayout drawer;
    String strSplashCheck;
    String Notiifcation_flag;
    public static int showFlag;
    LinearLayout ShowLayout;
    public  static FrameLayout NoVisiterLayout;
    public  static TextView NoDataText;
    public  static LinearLayout NoDataLayout;
    public static ArrayList<DataObjectVisitors> Updated_result = new ArrayList<>();
    public static ArrayList<DataObjectRating> Updated_result_rating = new ArrayList<>();
    public static int visitor_PageNo = 0;
    boolean doubleBackToExitPressedOnce = false;
    int drFlag = 0;
    public  static int backPressFlag = 1;
    private boolean mIsInForegroundMode;
    public static String RATING_FLAG = "0";
    public static String[] prov_list;

    String date;
    CleverTapAPI ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try
        {
            Bundle extras = getIntent().getExtras();
            strSplashCheck= extras.getString("CALL");
            Notiifcation_flag= extras.getString("NOT_FLAG");
        }
        catch (Exception ex)
        {
            Log.e("Exception "," "+ex);
        }
        if(Notiifcation_flag==null)
        {
            Notiifcation_flag="1";
        }

        SheredPref.setExecuteOffline(getApplication(),"Yes");
        SheredPref.setExecute(getApplication(),"");
        visitor_PageNo=0;
        backPressFlag=1;
        NoVisiterLayout = (FrameLayout) findViewById(R.id.main);
        NoDataText = (TextView) findViewById(R.id.NoDataText);
        NoDataLayout= (LinearLayout) findViewById(R.id.NoDataLayout);

        getSupportActionBar().setTitle("Visitors");

        MainContext = MainActivity.this;
        dbHelper = new DbHelper(this);

        imgHiddenImage = (ImageView) findViewById(R.id.imgHiddenImage);

        splashLayout = (LinearLayout) findViewById(R.id.splash);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        tvFlatno = (TextView)header.findViewById(R.id.tvFLatNo);
        tvSocietyName = (TextView)header.findViewById(R.id.tvSocietyName);
        tvWing = (TextView)header.findViewById(R.id.tvWing);

        tvFlatno.setText("Flat No. "+SheredPref.getFlat_No(getApplicationContext()));
        tvWing.setText("Wing : "+SheredPref.getWingName(getApplicationContext()));
        tvSocietyName.setText(SheredPref.getSociety_Name(getApplicationContext()));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(c.getTime());
        if(date.compareTo(SheredPref.getDateForApi(this))!=0){
            String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            int app_version_code = BuildConfig.VERSION_CODE;
            SheredPref.setDateForApi(this,date);
            new GetData(MainActivity.MainContext, CallFor.PROVIDER_LIST, "?data="+device_id+"&versionCode="+app_version_code).execute();
        }

        SheredPref.setRun(MainContext,"DONT RUN");
        if(strSplashCheck==null)
        {
            strSplashCheck="";
        }
        if(strSplashCheck.equals("FROM_LOGIN") || Notiifcation_flag.equals("3"))
        {
            SheredPref.setRun(MainContext,"RUN");
            splashLayout.setVisibility(View.INVISIBLE);
            drawer.setVisibility(View.VISIBLE);
        }
        if(strSplashCheck.equals(""))
        {
            if(Notiifcation_flag.equals("1") || Notiifcation_flag.equals("2"))
            {
                if(RATING_FLAG.equals("0")){
                    splashLayout.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashLayout.setVisibility(View.INVISIBLE);
                            drawer.setVisibility(View.VISIBLE);
                            SheredPref.setRun(MainContext,"RUN");
                        }
                    }, 2500);

                }
                else{
                    SheredPref.setRun(MainContext,"RUN");
                    splashLayout.setVisibility(View.INVISIBLE);
                    drawer.setVisibility(View.VISIBLE);
                }
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mIsInForegroundMode = true;
        cc = new CheckConnection(getApplicationContext());
        strValidation = SheredPref.getLoginInfo(getApplicationContext());
        if(strValidation.equals("LoggedIn"))
        {
            if(cc.isConnectingToInternet())
            {
                if (backPressFlag==1)
                {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
                    userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
                    new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGIN).execute();
                }
                if(Notiifcation_flag.equals("3")){
                    CleverTap.cleverTap_Record_Event(this, Events.event_notification_rating_seen);
                    drFlag = 3;
                    Updated_result_rating.clear();
                    try{
                        RatingResponseBean ratingResponseBean = new RatingResponseBean();
                        ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(MainContext)));
                        ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(MainContext)));
                        new PostData(new Gson().toJson(ratingResponseBean), MainActivity.this, CallFor.PENDING_RATING).execute();

                    }catch (Exception ex){
                        Log.e("Exception "," "+ex);
                    }
                }
            }
            else
            {
                if(title.equals("Visitors")){
                    if(SheredPref.getExecuteOffline(getApplicationContext()).equals(""))
                    {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commit();
                        SheredPref.setExecuteOffline(getApplication(),"Not");
                    }
                    else if(SheredPref.getExecuteOffline(getApplicationContext()).equals("Yes"))
                    {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commit();
                        SheredPref.setExecuteOffline(getApplication(),"Not");
                    }
                    else
                    {
                        SheredPref.setExecuteOffline(MainActivity.this,"NotExecute");
                    }
                }
                if(title.equals("Notices")){
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new NoticesFragment(), "Data").commit();
                }

                if(title.equals("Feedback")){
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new RatingFragment(), "Data").commit();
                    NoVisiterLayout.setVisibility(View.GONE);
                    NoDataLayout.setVisibility(View.VISIBLE);
                    NoDataText.setText("Please check Internet Connection !");
                }
            }
        }
        else
        {
            Intent I = new Intent(MainActivity.this,LoginScreenActivity.class);
            startActivity(I);
            I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce){
                super.onBackPressed();
                //finish();
                onStop();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsInForegroundMode = false;
    }

    @Override
    protected void onStop() {
        Log.e("ON STOP","");
        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        ft = fragmentManager.beginTransaction();
        int id = item.getItemId();
        if (id == R.id.nav_visiters) {
            title="Visitors";
            ShowUI();
            outputBeanSearchProviders.clear();
            if(cc.isConnectingToInternet()) {
                if(drFlag==1 || drFlag==2 || drFlag==3 || drFlag==4)
                {
                    visitor_PageNo=0;
                    Updated_result.clear();
                    new GetData(MainActivity.this, CallFor.VISITORS, ""+visitor_PageNo).execute();
                    drFlag = 0;
                }
                else{
                }
            }
            else
            {
                ft.replace(R.id.main, new VisitorsFragment(), "Data").commit();
            }

        } else if (id == R.id.nav_notice) {
           title="Notices";
            ShowUI();
            outputBeanSearchProviders.clear();
            if(cc.isConnectingToInternet())
            {
                if(drFlag==0 || drFlag==2 || drFlag==3 || drFlag==4)
                {
                    new GetData(MainActivity.this, CallFor.NOTICES, "").execute();
                    drFlag = 1;
                }
            }
            else
            {
                ft.replace(R.id.main, new NoticesFragment(), "Data").commit();
            }

        }
        else if (id == R.id.nav_feedback) {
            title="Feedback";
            outputBeanSearchProviders.clear();
            if(cc.isConnectingToInternet())
            {
                if(drFlag==0 || drFlag==1 || drFlag==2 || drFlag==4){
                    Updated_result_rating.clear();
                    RatingResponseBean ratingResponseBean = new RatingResponseBean();
                    ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(MainContext)));
                    ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(MainContext)));
                    new PostData(new Gson().toJson(ratingResponseBean), MainActivity.this, CallFor.PENDING_RATING).execute();
                    ShowUI();
                    drFlag = 3;
                }
            }
            else
            {
                NoVisiterLayout.setVisibility(View.GONE);
                NoDataLayout.setVisibility(View.VISIBLE);
                NoDataText.setText("Please check Internet Connection !");
                ShowErrorAlert();
            }
        }
        else if (id == R.id.nav_search_provider) {
            title="Search Service Provider";
            if(drFlag==0 || drFlag==1 || drFlag==2 || drFlag==3){
                if(cc.isConnectingToInternet())
                {
                    ShowUI();
                }
                else
                {
                    NoVisiterLayout.setVisibility(View.GONE);
                    NoDataLayout.setVisibility(View.VISIBLE);
                    NoDataText.setText("Please check Internet Connection !");
                    ShowErrorAlert();
                }
                drFlag=4;
            }
            ft.replace(R.id.main, new SearchProviderFragment(), "Data").commit();
        }
        else if (id == R.id.nav_logout) {
            outputBeanSearchProviders.clear();
            if(cc.isConnectingToInternet())
            {
                CleverTap.cleverTap_Record_Event(this,Events.event_logout_fragment);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainContext, R.style.AppCompatAlertDialogStyle);
                builder.setMessage("Do you sure logout from application?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        String device_id = Settings.Secure.getString(MainContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                        UserBean userBean = new UserBean();
                        userBean.setUser_dev_id(device_id);
                        new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGOUT).execute();
                        LoginScreenActivity.splashFlag=false;
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
            else
            {
                Toast.makeText(MainContext,"Unable to logout, Please connect to internet",Toast.LENGTH_LONG).show();
            }
        }
        else if(id == R.id.nav_share){
            outputBeanSearchProviders.clear();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "\t\t\tVZ Track - Smart Digital Solution to manage visitors for societies and commercial complex.\n" +
                    "--------------------------------------\n"+
                    "What do we provide to get started?" +
                    "\n1. 7 inches android tablet." +
                    "\n2. 1GB internet data per month " +
                    "\n3. Training to security guards" +
                    "\n4. Multiple user login per flat" +
                    "\n5. Ongoing support & maintenance " +
                    "\n6. Instant notification on owner's mobile with visitors details"+
                    "\n7. Analytics and Reports of visitors visited the premise"+
                    "\n\nContact us : \n" +
                     "Call us: +91 90750 16367\n" +
                    "Email us: sales@vztrack.in\n"+
                    "Website : http://www.vztrack.in\n"
                    +"follow us on Twitter : @VzTrack";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VZ Track");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            CleverTap.cleverTap_Record_Event(this,Events.event_share_fragment);
        }
        else if (id == R.id.nav_setting) {
            outputBeanSearchProviders.clear();
            title="Setting";
            drFlag = 2;
            if(cc.isConnectingToInternet())
            {
                NoVisiterLayout.setVisibility(View.VISIBLE);
                NoDataLayout.setVisibility(View.GONE);
                ft.replace(R.id.main, new SettingFragment(), "Data").commit();
            }
            else
            {
                ft.replace(R.id.main, new SettingFragment(), "Data").commit();
            }
        }
        getSupportActionBar().setTitle(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ShowUI(){
        NoVisiterLayout.setVisibility(View.VISIBLE);
        NoDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if(cc.isConnectingToInternet())
        {
            LoginResponse loginResponse = null;

            if (response == null) {
                return;
            }

            if (callFor.equals(CallFor.LOGIN)) {
                loginResponse=new Gson().fromJson(response,LoginResponse.class);
                try {
                    if (loginResponse.getCode().equals("SUCCESS"))
                    {
                        if(SheredPref.getSocietyId(this).equals("")){
                            int strSocietyId = loginResponse.getFamily().getSocietyId();
                            int strFamilyId = loginResponse.getFamily().getFamilyId();
                            SheredPref.setSocietyId(this,""+strSocietyId);
                            SheredPref.setFamilyId(this,""+strFamilyId);
                        }

                        if(SheredPref.getExecute(getApplicationContext()).equals(""))
                        {
                            if(Notiifcation_flag.equals("1"))
                            {
                                Updated_result.clear();
                                new GetData(MainActivity.this, CallFor.VISITORS, ""+visitor_PageNo).execute();
                                SheredPref.setExecute(getApplication(),"Not");
                                getSupportActionBar().setTitle("Visitors");
                            }
                            if(Notiifcation_flag.equals("2"))
                            {
                                drFlag=2;
                                new GetData(MainActivity.this, CallFor.NOTICES, "").execute();
                                SheredPref.setExecute(getApplication(),"Not");
                                getSupportActionBar().setTitle("Notices");
                            }
                        }
                        else
                        {
                            SheredPref.setExecute(MainActivity.this,"NotExecute");
                        }

                    }
                    else if(loginResponse.getCode().equals("ERROR"))
                    {
                        Toast.makeText(MainActivity.this,"Password changed, \nPlease login with new password",Toast.LENGTH_LONG).show();
                        SheredPref.setUSername(this,"");
                        SheredPref.setPassword(this,"");
                        SheredPref.setLoginInfo(this,"LoggedOut");

                       Intent I = new Intent(MainActivity.this,LoginScreenActivity.class);
                       startActivity(I);
                       I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        this.finish();

                    }
                }catch (Exception ex)
                {

                }
            }

            if (callFor.equals(CallFor.NOTICES)) {
                getSupportActionBar().setTitle("Notices");
                noticesResponse = new Gson().fromJson(response,NoticesResponse.class);
                if(fragment_flag==1)
                {
                    VisitorsFragment.mSwipeRefreshLayout.setRefreshing(false);
                    fragment_flag=0;
                }
                try {

                    if (noticesResponse.getCode().equals("SUCCESS")){
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new NoticesFragment(), "Data").commit();

                        if(noticesResponse.getMessage().equals("No Notices"))
                        {
                            NoVisiterLayout.setVisibility(View.GONE);
                            NoDataLayout.setVisibility(View.VISIBLE);
                            NoDataText.setText("No Notices To Display");
                        }
                        else
                        {
                            NoVisiterLayout.setVisibility(View.VISIBLE);
                            NoDataLayout.setVisibility(View.GONE);
                        }
                    }
                    else if (noticesResponse.getCode().equals("NEED_LOGIN")) {
                        UserBean userBean = new UserBean();
                        userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
                        userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
                        new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGIN).execute();

                    }
                    else {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new NoticesFragment(), "Data").commit();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Server Error" +e,Toast.LENGTH_SHORT).show();
                }
            }  // End If

            if (callFor.equals(CallFor.VISITORS)) {
                getSupportActionBar().setTitle("Visitors");
                visitorsArray = new Gson().fromJson(response,VisitorsArray.class);
                if(fragment_flag==1)
                {
                    VisitorsFragment.mSwipeRefreshLayout.setRefreshing(false);
                    fragment_flag=0;
                }
                if (visitorsArray.getVisitors().size()==0 && visitor_PageNo==0) {
                    NoVisiterLayout.setVisibility(View.GONE);
                    NoDataLayout.setVisibility(View.VISIBLE);
                    NoDataText.setText("No Visitor To Display");
                }
                else
                {
                    NoVisiterLayout.setVisibility(View.VISIBLE);
                    NoDataLayout.setVisibility(View.GONE);
                }
                if (visitorsArray.getCode().equals("SUCCESS")) {
                    showFlag=1;
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                    //ft.replace(R.id.main, new RatingFragment(), "Data").commit();
                }
                else if (noticesResponse.getCode().equals("NEED_LOGIN")) {

                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
                    userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
                    new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGIN).execute();
                }
                else
                {
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new VisitorsFragment(), "Data").commit();
                }
            }

            if (callFor.equals(CallFor.PROVIDER_LIST)) {
                prov_list = new Gson().fromJson(response,String[].class);

                // Store the Provider List in SharedPreferences
                SharedPreferences prefs = this.getSharedPreferences("LIST",0);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("array_size", prov_list.length);
                for(int i=0;i<prov_list.length; i++)
                    edit.putString("array_" + i, prov_list[i]);
                edit.commit();
            }

            if (callFor.equals(CallFor.PROVIDERS_DATA)) {
                getSupportActionBar().setTitle("Search Provider");
                outputBeanSearchProviders = new Gson().fromJson(response,new TypeToken<ArrayList<OutputBeanSearchProvider>>(){}.getType());
                if(outputBeanSearchProviders.size()==0){
                    SearchProviderFragment.tvNoProvider.setVisibility(View.VISIBLE);
                    SearchProviderFragment.tvNoProvider.setText("No Provider Found !");
                    SearchProviderFragment.mRecyclerView.setVisibility(View.GONE);
                }
                else{
                    SearchProviderFragment.tvNoProvider.setVisibility(View.GONE);
                    SearchProviderFragment.mRecyclerView.setVisibility(View.VISIBLE);
                }
                try
                {
                    SearchProviderFragment.mAdapter = new MyRecyclerViewAdapter_search_provider(this,MainActivity.outputBeanSearchProviders);
                }catch (Exception ex)
                {
                    Log.e("EXCEPTION "," "+ex);
                }
                SearchProviderFragment.mRecyclerView.setAdapter( SearchProviderFragment.mAdapter);
            }


            if (callFor.equals(CallFor.PENDING_RATING)) {
                getSupportActionBar().setTitle("Feedback");
                ratingBeanArrayList = new Gson().fromJson(response,new TypeToken<ArrayList<RatingBean>>(){}.getType());
                if(fragment_flag==1)
                {
                    RatingFragment.mSwipeRefreshLayout.setRefreshing(false);
                    fragment_flag=0;
                }
                if (ratingBeanArrayList.size()==0) {
                    NoVisiterLayout.setVisibility(View.GONE);
                    NoDataLayout.setVisibility(View.VISIBLE);
                    NoDataText.setText("No Provider Visited Yet !");
                }
                else
                {
                    NoVisiterLayout.setVisibility(View.VISIBLE);
                    NoDataLayout.setVisibility(View.GONE);
                }
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.main, new RatingFragment(), "Data").commit();
            }


            if (callFor.equals(CallFor.CHANGE_PASSWORD)) {
                loginResponse=new Gson().fromJson(response,LoginResponse.class);
                try {
                    if (loginResponse.getCode().equals("SUCCESS"))
                    {
                        Toast.makeText(this,"Password changed successfully",Toast.LENGTH_LONG).show();
                        SheredPref.setPassword(this,SettingFragment.new_password);
                        SettingFragment.OldPassword.setText("");
                        SettingFragment.NewPassword1.setText("");
                        SettingFragment.NewPassword2.setText("");
                        getSupportActionBar().setTitle("Visitors");
                        new GetData(MainActivity.this, CallFor.VISITORS, ""+visitor_PageNo).execute();


                    }
                    else if(loginResponse.getCode().equals("NEED_LOGIN"))
                    {
                        Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show();
                        UserBean userBean = new UserBean();
                        userBean.setUser_name(SheredPref.getUsername(this));
                        userBean.setUser_password(SheredPref.getPassword(this));
                        new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                    }
                    else
                    {
                        Toast.makeText(this,"Error in password change please try again",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex)
                {

                }
            }

            if (callFor.equals(CallFor.LOGOUT)) {
                LogoutResponse logoutResponse = new Gson().fromJson(response,LogoutResponse.class);
                try {
                    if (logoutResponse.getCode().equals("SUCCESS")) {

                        SheredPref.setUSername(this,"");
                        SheredPref.setPassword(this,"");
                        SheredPref.setDateForApi(this,"");
                        SheredPref.setLoginInfo(this,"LoggedOut");
                        Intent I = new Intent(MainActivity.this,LoginScreenActivity.class);
                        startActivity(I);
                        this.finish();
                        I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SheredPref.setNotification(this,"DISABLE");
                        Toast.makeText(MainContext,logoutResponse.getMessage(),Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(MainContext,logoutResponse.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Exception "," "+ex);
                }
            }
        }
    }

    public static void ShowErrorAlert() {
          new SweetAlertDialog(MainContext, SweetAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText("Check your internet connection")
                  .show();
    }
}