package vztrack.gls.com.vztrack_user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapterVisitList;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitList;
import vztrack.gls.com.vztrack_user.responce.VisitListResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class Visiters_Details extends BaseActivity {

    ImageView Photo;
    TextView tvName, etMobileNo;

    String PhotoUrl;
    String Name;
    String Contact_No;
    String DocUrl;
    String pos;

    private static Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> list = new ArrayList<String>();
    ArrayList results;
    //private int InternetCheckFlag = 0;
    private VisitListResponse visitListResponse;
    DbHelper dbHelper;
    CheckConnection cc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiters__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CleverTap.cleverTap_Record_Event(this, Events.event_visitor_details_activity);

        dbHelper = new DbHelper(this);

        // Check Internet Connection
        cc = new CheckConnection(getApplicationContext());

        Photo = (ImageView) findViewById(R.id.imgPhoto);
        tvName = (TextView) findViewById(R.id.etName);
        etMobileNo = (TextView) findViewById(R.id.etMobileNo);

        PhotoUrl = getIntent().getStringExtra("PHOTO_URL");
        Name = getIntent().getStringExtra("NAME");
        Contact_No = getIntent().getStringExtra("CONTACT_NO");
        DocUrl = getIntent().getStringExtra("DOC_URL");
        pos = getIntent().getStringExtra("POS");

        tvName.setText(Name);
        etMobileNo.setText(Contact_No);

        if (cc.isConnectingToInternet()) {
            new GetData(Visiters_Details.this, CallFor.VISITOR_LIST, Contact_No).execute();
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, PhotoUrl, Photo, null);
        } else {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, PhotoUrl, Photo, null);
            mRecyclerView = (RecyclerView) findViewById(R.id.visitorList_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new MyRecyclerViewAdapterVisitList(context, getDataSet());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setOnClickListener(null);
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.backPressFlag = 0;
        finish();
    }

    public void ShowDocument(View v) {
        final Dialog settingsDialog = new Dialog(this, R.style.DialogTheme);
        settingsDialog.getWindow().getAttributes().windowAnimations = R.style.animation_id;
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View showData = getLayoutInflater().inflate(R.layout.doc_image_layout, null);
        ImageView imageView = (ImageView) showData.findViewById(R.id.doc_imgImage);
        if (cc.isConnectingToInternet()) {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, DocUrl, imageView, null);
        } else {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, DocUrl, imageView, null);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingsDialog.dismiss();
            }
        });

        settingsDialog.setContentView(showData);
        settingsDialog.setCanceledOnTouchOutside(false);
        settingsDialog.show();
    }

    public void ShowPhoto(View v) {
        final Dialog settingsDialog = new Dialog(this, R.style.DialogTheme);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
        View showData = getLayoutInflater().inflate(R.layout.image_layout_for_visitors, null);
        final TextView ShowUserName = (TextView) showData.findViewById(R.id.tvNameAndNumber);
        final ImageView imageView = (ImageView) showData.findViewById(R.id.imgImage);

        ShowUserName.setText(Name);

        if (cc.isConnectingToInternet()) {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, PhotoUrl, imageView, null);
        } else {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, PhotoUrl, imageView, null);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingsDialog.dismiss();

            }
        });

        ShowUserName.setText(Name);
        settingsDialog.setContentView(showData);
        settingsDialog.setCanceledOnTouchOutside(false);
        settingsDialog.show();
    }

    public void Call(View v) {
        String Mobile_no = etMobileNo.getText().toString();
        Uri number = Uri.parse("tel:" + Mobile_no);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if (cc.isConnectingToInternet()) {
            visitListResponse = new Gson().fromJson(response, VisitListResponse.class);

            if (response == null) {
                return;
            }
            if (callFor.equals(CallFor.VISITOR_LIST)) {
                try {
                    if (visitListResponse.getCode().equals("SUCCESS")) {
                        mRecyclerView = (RecyclerView) findViewById(R.id.visitorList_recycler_view);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new MyRecyclerViewAdapterVisitList(context, getDataSet());
                        mRecyclerView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(this, "Invalid Details", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }  // End If

        }


    }

    private ArrayList<DataObjectVisitList> getDataSet() {

        results = new ArrayList<DataObjectVisitList>();

        if (cc.isConnectingToInternet()) {
            for (int index = 0; index < visitListResponse.getVisitors().size(); index++) {
                DataObjectVisitList obj = new DataObjectVisitList(
                        visitListResponse.getVisitors().get(index).getFirst_name(),
                        visitListResponse.getVisitors().get(index).getLast_name(),
                        visitListResponse.getVisitors().get(index).getIn_time(),
                        visitListResponse.getVisitors().get(index).getOut_time(),
                        visitListResponse.getVisitors().get(index).getVisit_purpose(),
                        visitListResponse.getVisitors().get(index).getFrom(),
                        visitListResponse.getVisitors().get(index).getWhomeToVisit(),
                        visitListResponse.getVisitors().get(index).getVehicle_no(),
                        visitListResponse.getVisitors().get(index).getNo_of_visitors(),
                        visitListResponse.getVisitors().get(index).getWhomeToVisit()
                );
                results.add(index, obj);
            }
            return results;
        } else {
            Cursor rs = dbHelper.getDataFromVisitorsTableById(String.valueOf(pos));
            rs.moveToFirst();
            String first_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FIRST_NAME));
            String last_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_LAST_NAME));
            String date_and_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DATE));
            String out_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_OutTime));
            String purpose = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PURPOSE));
            String from = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FROM));
            String whome_to_see = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_TOMEET));
            String Vehicle_no = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VEHICLE_NO));
            String no_of_visitors = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VISITORS_NO));

            DataObjectVisitList obj = new DataObjectVisitList(
                    first_name, last_name, date_and_time, out_time , purpose, from, whome_to_see, Vehicle_no, no_of_visitors, whome_to_see
            );
            results.add(0, obj);
            return results;
        }
    }
}
