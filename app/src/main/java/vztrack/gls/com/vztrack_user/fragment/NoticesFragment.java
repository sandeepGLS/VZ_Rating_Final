package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vztrack.gls.com.vztrack_user.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapter_notices;
import vztrack.gls.com.vztrack_user.beans.DataObjectNotices;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class NoticesFragment extends Fragment {

    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    DbHelper dbHelper;
    String img_URL,heading,description,noticeStartdate,noticeEndDate;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public NoticesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notices, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_notice_fragment);

        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.notice_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        try
        {
            mAdapter = new MyRecyclerViewAdapter_notices(context, getDataSet("All"));
        }catch (Exception ex)
        {

        }

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cc.isConnectingToInternet())
                {
                    MainActivity.fragment_flag=1;
                    new GetData(MainActivity.MainContext, CallFor.NOTICES, "").execute();
                }
                else
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context,"Unable To Refresh , No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        if(cc.isConnectingToInternet())
        {
            String currentDateString = DateFormat.getDateInstance().format(new Date());
            if(currentDateString.equals(SheredPref.getDate(context))  && MainActivity.noticesResponse.getNotices().size()==dbHelper.getNumberOfRowsOfNotices() )
            {
            }
            else
            {
                dbHelper.deleteDataFromNotices();

                for (int index = 0; index < MainActivity.noticesResponse.getNotices().size(); index++) {

                    heading = MainActivity.noticesResponse.getNotices().get(index).getNoticeHeading();
                    description = MainActivity.noticesResponse.getNotices().get(index).getNoticeDesc();
                    noticeStartdate = MainActivity.noticesResponse.getNotices().get(index).getNoticeStartDate();
                    noticeEndDate = MainActivity.noticesResponse.getNotices().get(index).getNoticeEndDate();
                    img_URL = Constants.BASE_IMG_URL+"/"+MainActivity.noticesResponse.getNotices().get(index).getPath();
                    if(dbHelper.insertDataInNotices(heading,description,noticeStartdate,noticeEndDate,img_URL)){
                    }
                }
                SheredPref.setDate(context , currentDateString);
            }
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Search By Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                query = query.toLowerCase();

                ArrayList filteredList = new ArrayList<DataObjectNotices>();
                filteredList = getDataSet(query);

                try
                {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyRecyclerViewAdapter_notices(context,filteredList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                catch (Exception ex)
                {
                    Log.e("Ex "," "+ex);
                }
                return true;
            }
        });
    }


    private ArrayList<DataObjectNotices> getDataSet(String startWith) {
        results = new ArrayList<DataObjectNotices>();
        if(startWith.equals("All") || startWith.equals(""))
        {
            if(cc.isConnectingToInternet())
            {
                for (int index = 0; index < MainActivity.noticesResponse.getNotices().size(); index++) {
                    DataObjectNotices obj = new DataObjectNotices(MainActivity.noticesResponse.getNotices().get(index).getNoticeHeading(),
                            MainActivity.noticesResponse.getNotices().get(index).getNoticeDesc(),
                            MainActivity.noticesResponse.getNotices().get(index).getNoticeStartDate(),
                            MainActivity.noticesResponse.getNotices().get(index).getNoticeEndDate(),
                            MainActivity.noticesResponse.getNotices().get(index).getPath()
                    );
                    results.add(index, obj);
                }
            }
            else
            {
                int row_count = dbHelper.getNumberOfRowsOfNotices();
                Cursor rs = dbHelper.getDataFromNoticesTable();
                rs.moveToFirst();

                for(int i=0;i<row_count;i++)
                {
                    String title = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_TITLE));
                    String desc = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_DESCRIPTION));
                    String noticeStartDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_START_DATE));
                    String noticeEndDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_END_DATE));
                    String path = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_PHOTO_STRING));
                    DataObjectNotices obj = new DataObjectNotices(title,desc,noticeStartDate,noticeEndDate,path);
                    results.add(i, obj);
                    rs.moveToNext();
                }
            }
        }
        else
        {
            if(cc.isConnectingToInternet())
            {
                for (int i = 0; i < MainActivity.noticesResponse.getNotices().size(); i++) {
                    String check = MainActivity.noticesResponse.getNotices().get(i).getNoticeHeading().trim();
                    if(check.toLowerCase().contains(startWith)){
                        DataObjectNotices obj = new DataObjectNotices(MainActivity.noticesResponse.getNotices().get(i).getNoticeHeading(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeDesc(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeStartDate(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeEndDate(),
                                MainActivity.noticesResponse.getNotices().get(i).getPath()
                        );
                        results.add(obj);
                    }
                }
            }
            else
            {
                int row_count = dbHelper.getNumberOfRowsOfNotices();
                Cursor rs = dbHelper.getDataFromNoticesTable();
                rs.moveToFirst();
                for(int i=0;i<row_count;i++)
                {
                    String title = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_TITLE));
                    String desc = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_DESCRIPTION));
                    String noticeStartDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_START_DATE));
                    String noticeEndDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_END_DATE));
                    String path = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_PHOTO_STRING));
                    if(title.toLowerCase().contains(startWith)){
                        DataObjectNotices obj = new DataObjectNotices(title,desc,noticeStartDate,noticeEndDate,path);
                        results.add(obj);
                    }
                    rs.moveToNext();
                }
            }
        }
        return results;
    }
}
