package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

import java.io.File;
import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapterVisitors;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class VisitorsFragment extends Fragment {

    private static Context context;
    public static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList results;
    ArrayList searched_results = new ArrayList<DataObjectVisitors>();
    String first_name;
    String last_name;
    String photo_URL;
    String doc_URL;
    String mobile_no;
    String date_and_time;
    String out_time;
    String purpose;
    String from;
    String vehicle_no;
    String total_visitors;
    String toMeet;
    DbHelper dbHelper;
    CheckConnection cc;
    public static int clearFlag = 0;
    File file;
    public VisitorsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_visitors, null);

        context = getActivity();
        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_visitor_fragment);

        dbHelper = new DbHelper(context);
        setHasOptionsMenu(true);
        cc = new CheckConnection(getActivity());
        mRecyclerView = (RecyclerView) root.findViewById(R.id.visiter_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (clearFlag == 1) {
            MainActivity.Updated_result.clear();
            clearFlag = 0;
        }
        try {
            mAdapter = new MyRecyclerViewAdapterVisitors(context, getDataSet("All"));
        } catch (Exception ex) {

        }
        mRecyclerView.setAdapter(mAdapter);
        if (MainActivity.visitor_PageNo != 0) {
            mRecyclerView.scrollToPosition(MainActivity.visitor_PageNo * 10);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cc.isConnectingToInternet()) {
                    MainActivity.visitor_PageNo = 0;
                    MainActivity.fragment_flag = 1;
                    clearFlag = 1;
                    new GetData(MainActivity.MainContext, CallFor.VISITORS, "" + MainActivity.visitor_PageNo).execute();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(context, "Unable to refresh, No internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        if (cc.isConnectingToInternet()) {
            final int visitorListSize = MainActivity.visitorsArray.getVisitors().size();
            final int dbListSize = dbHelper.getNumberOfRowsOfVisitors();
            int row = dbHelper.getNumberOfRowsOfVisitors();
            String offlineRec = null;
            if (row != 0) {
                Cursor cursor = dbHelper.getDataFromVisitorsTable();
                cursor.moveToFirst();
                offlineRec = cursor.getString(cursor.getColumnIndex(dbHelper.VISITORS_FIRST_NAME)) + "-" + cursor.getString(cursor.getColumnIndex(dbHelper.VISITORS_LAST_NAME)) + "-" + cursor.getString(cursor.getColumnIndex(dbHelper.VISITORS_MOBILE_NO));
            } else {
                offlineRec = "";
            }
            String onlineRec = null;
            if(MainActivity.visitorsArray.getVisitors().size()!=0){
                onlineRec = MainActivity.visitorsArray.getVisitors().get(0).getFirst_name() + "-" + MainActivity.visitorsArray.getVisitors().get(0).getLast_name() + "-" + MainActivity.visitorsArray.getVisitors().get(0).getMobile_no();
            }

            if (MainActivity.visitor_PageNo == 0) {
                if (!offlineRec.equals(onlineRec)) {
                    try {
                        dbHelper.deleteDataFromVisitors();
                        for (int index = 0; index < visitorListSize; index++) {
                            first_name = MainActivity.visitorsArray.getVisitors().get(index).getFirst_name();
                            last_name = MainActivity.visitorsArray.getVisitors().get(index).getLast_name();
                            photo_URL = Constants.BASE_IMG_URL + "/" + MainActivity.visitorsArray.getVisitors().get(index).getPhoto_url();
                            doc_URL = Constants.BASE_IMG_URL + "/" + MainActivity.visitorsArray.getVisitors().get(index).getDoc_url();
                            mobile_no = MainActivity.visitorsArray.getVisitors().get(index).getMobile_no();
                            date_and_time = MainActivity.visitorsArray.getVisitors().get(index).getIn_time();
                            out_time = MainActivity.visitorsArray.getVisitors().get(index).getOut_time();
                            if(out_time == null){
                                out_time = "NO EXIT TIME" ;
                            }

                            purpose = MainActivity.visitorsArray.getVisitors().get(index).getVisit_purpose();
                            from = MainActivity.visitorsArray.getVisitors().get(index).getFrom();
                            vehicle_no = MainActivity.visitorsArray.getVisitors().get(index).getVehicle_no();
                            total_visitors = MainActivity.visitorsArray.getVisitors().get(index).getNo_of_visitors();
                            toMeet = MainActivity.visitorsArray.getVisitors().get(index).getWhomeToVisit();
                            if (dbHelper.insertDataInVisitors("" + index, first_name, last_name, mobile_no, date_and_time, out_time, purpose, from, vehicle_no, total_visitors, toMeet, photo_URL, doc_URL)) {
                                //int row_count = dbHelper.getNumberOfRowsOfVisitors();
                            }
                        }
                   } catch (Exception ex) {
                        Log.e("EXCE", "" + ex);
                   }
                }
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
        searchView.setQueryHint("Search By Name Or Number");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                searched_results.clear();
                //  ArrayList filteredList = new ArrayList<DataObjectVisitors>();
                ArrayList filteredList;
                filteredList = getDataSet(query);
                try {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyRecyclerViewAdapterVisitors(context, filteredList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } catch (Exception ex) {
                    Log.e("Ex ", " " + ex);
                }
                return true;
            }
        });
    }


    private ArrayList<DataObjectVisitors> getDataSet(String startWith) {
        results = new ArrayList<DataObjectVisitors>();
        if (startWith.equals("All") || startWith.equals("")) {
            if (startWith.equals("")) {
                MainActivity.Updated_result.addAll(results);
                return MainActivity.Updated_result;
            } else {
                if (cc.isConnectingToInternet()) {
                    for (int index = 0; index < MainActivity.visitorsArray.getVisitors().size(); index++) {
                        DataObjectVisitors obj = new DataObjectVisitors(
                                MainActivity.visitorsArray.getVisitors().get(index).getFirst_name(),
                                MainActivity.visitorsArray.getVisitors().get(index).getLast_name(),
                                MainActivity.visitorsArray.getVisitors().get(index).getMobile_no(),
                                MainActivity.visitorsArray.getVisitors().get(index).getPhoto_url(),
                                MainActivity.visitorsArray.getVisitors().get(index).getIn_time(),
                                MainActivity.visitorsArray.getVisitors().get(index).getVisit_purpose(),
                                MainActivity.visitorsArray.getVisitors().get(index).getFrom(),
                                MainActivity.visitorsArray.getVisitors().get(index).getWhomeToVisit(),
                                MainActivity.visitorsArray.getVisitors().get(index).getDoc_url()
                        );
                        results.add(obj);
                    }
                } else {
                    MainActivity.Updated_result.clear();
                    int row_count = dbHelper.getNumberOfRowsOfVisitors();
                    Cursor rs = dbHelper.getDataFromVisitorsTable();
                    rs.moveToFirst();
                    for (int i = 0; i < row_count; i++) {
                        String id = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_INDEX_ID));
                        String first_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FIRST_NAME));
                        String last_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_LAST_NAME));
                        String mobile_no = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_MOBILE_NO));
                        String photo_URL = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PHOTO_STRING));
                        String date_and_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DATE));
                        String purpose = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PURPOSE));
                        String from = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FROM));
                        String whome_to_see = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VEHICLE_NO));
                        String doc_URL = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DOC_STRING));
                        DataObjectVisitors obj = new DataObjectVisitors(first_name, last_name, mobile_no, photo_URL, date_and_time, purpose, from, whome_to_see, doc_URL);
                        results.add(obj);
                        rs.moveToNext();
                    }
                }
                MainActivity.Updated_result.addAll(results);
                return MainActivity.Updated_result;
            }
        } else {
            if (cc.isConnectingToInternet()) {
                for (int index = 0; index < MainActivity.Updated_result.size(); index++) {
                    DataObjectVisitors obj = MainActivity.Updated_result.get(index);
                    String check_first_name = obj.getmFirst_Name();
                    String check_last_name = obj.getmLast_Name();
                    String check_mobile = obj.getmMobile();
                    if (check_mobile.trim().contains(startWith) || check_first_name.toLowerCase().startsWith(startWith) || check_last_name.toLowerCase().startsWith(startWith)) {
                        DataObjectVisitors objNew = new DataObjectVisitors(
                                obj.getmFirst_Name(),
                                obj.getmLast_Name(),
                                obj.getmMobile(),
                                obj.getmPhoto(),
                                obj.getmTime(),
                                obj.getmPurpose(),
                                obj.getmFrom(),
                                obj.getmTomeet(),
                                obj.getmDocUrl()


                        );
                        results.add(objNew);
                    }
                }
            } else {
                int row_count = dbHelper.getNumberOfRowsOfVisitors();
                Cursor rs = dbHelper.getDataFromVisitorsTable();
                rs.moveToFirst();
                for (int i = 0; i < row_count; i++) {
                    String first_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FIRST_NAME));
                    String last_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_LAST_NAME));
                    String mobile_no = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_MOBILE_NO));
                    String photo_URL = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PHOTO_STRING));
                    String date_and_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DATE));
                    String purpose = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PURPOSE));
                    String from = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FROM));
                    String whome_to_see = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VEHICLE_NO));
                    String doc_URL = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DOC_STRING));
                    String check_first_name = first_name;
                    String check_last_name = last_name;
                    if (check_first_name.toLowerCase().startsWith(startWith) || check_last_name.toLowerCase().startsWith(startWith) || mobile_no.contains(startWith)) {
                        DataObjectVisitors obj = new DataObjectVisitors(first_name, last_name, mobile_no, photo_URL, date_and_time, purpose, from, whome_to_see, doc_URL);
                        results.add(obj);
                    }
                    rs.moveToNext();
                }
            }
            searched_results.addAll(results);
            return searched_results;
        }
    }

}
