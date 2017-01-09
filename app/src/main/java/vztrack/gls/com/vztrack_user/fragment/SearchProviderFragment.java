package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;

import vztrack.gls.com.vztrack_user.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.MyRecyclerViewAdapter_search_provider;
import vztrack.gls.com.vztrack_user.beans.InputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class SearchProviderFragment extends Fragment {

    Context context;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    DbHelper dbHelper;
    String img_URL,heading,description,noticeStartdate,noticeEndDate;
    CheckConnection cc;
    TextView tvQuality,tvPrice,tvPunctuality,tvNearBySoc;
    private AppCompatSpinner purposes_spinner;
    private AnimCheckBox checkbox_quality,checkbox_price,checkbox_punctuality;
    private SwitchButton switch_btn;
    public static String strProviderVal,strRatingVal,strSocVal;
    String strSelectProvider="     SELECT PROVIDER     ";
    public static TextView tvNoProvider;
    public SearchProviderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search_provider, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_search_provider_fragment);


        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();

        mRecyclerView = (RecyclerView) root.findViewById(R.id.search_provider_recycler_view);
        purposes_spinner = (AppCompatSpinner) root.findViewById(R.id.spinner_providers_list);

        tvQuality = (TextView) root.findViewById(R.id.tvQuality);
        tvPrice = (TextView) root.findViewById(R.id.tvPrice);
        tvPunctuality = (TextView) root.findViewById(R.id.tvPunctuality);
        tvNearBySoc = (TextView) root.findViewById(R.id.tvNearBySoc);

        checkbox_quality = (AnimCheckBox) root.findViewById(R.id.checkbox_quality);
        checkbox_price = (AnimCheckBox) root.findViewById(R.id.checkbox_price);
        checkbox_punctuality = (AnimCheckBox) root.findViewById(R.id.checkbox_punctuality);

        switch_btn = (SwitchButton) root.findViewById(R.id.switch_btn);

        tvNoProvider = (TextView) root.findViewById(R.id.TvNoProvider);

        tvNoProvider.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        tvNoProvider.setText("Please Select Provider, No Provider Selected !");

        checkbox_quality.setChecked(true);
        checkbox_price.setChecked(false);
        checkbox_punctuality.setChecked(false);
        strSocVal = "nearby";
        strRatingVal="quality";


        tvQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                }
                else{
                    checkbox_quality.setChecked(true);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="quality";
                    getProviderData();
                }
                //checkbox_quality.setChecked(true);
                //checkbox_price.setChecked(false);
                //checkbox_punctuality.setChecked(false);
            }
        });
        tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                }
                else{
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(true);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="price";
                    getProviderData();
                }
                //checkbox_quality.setChecked(false);
                //checkbox_price.setChecked(true);
                //checkbox_punctuality.setChecked(false);
            }
        });
        tvPunctuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkbox_quality.setChecked(false);
                checkbox_price.setChecked(false);
                checkbox_punctuality.setChecked(true);
            }
        });


        checkbox_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                }
                else{
                    checkbox_quality.setChecked(true);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="quality";
                    getProviderData();
                }
            }
        });

        checkbox_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                }
                else{
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(true);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="price";
                    getProviderData();
                }
            }
        });
        checkbox_punctuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                }
                else{
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(true);
                    strRatingVal="punctuality";
                    getProviderData();
                }
            }
        });

        // get the provoder list from SharedPreference
        SharedPreferences prefs = context.getSharedPreferences("LIST",0);
        int size = prefs.getInt("array_size", 0);
        final String[] list = new String[size+1];
        for(int i=0; i<size; i++){
            if(i==0){
                list[i] = strSelectProvider;
                list[i+1] = prefs.getString("array_" + i, null);
            }
            else{
                list[i+1] = prefs.getString("array_" + i, null);
            }
        }

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purposes_spinner.setAdapter(adapter);

        purposes_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos==0){
                    strProviderVal = adapterView.getItemAtPosition(pos).toString();
                    tvNoProvider.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                else{
                    tvNoProvider.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    strProviderVal = adapterView.getItemAtPosition(pos).toString();
                    CleverTap.cleverTap_Record_Event(getActivity(),"Selected Provider - "+strProviderVal);
                    getProviderData();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(strProviderVal.equals(strSelectProvider)){
                    Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                    switch_btn.setChecked(false);
                }
                else{
                    if(isChecked){
                        strSocVal = "OUT_SOC";
                    }else{
                        strSocVal = "nearby";
                    }
                    getProviderData();
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        try
        {
            mAdapter = new MyRecyclerViewAdapter_search_provider(context,MainActivity.outputBeanSearchProviders);
        }catch (Exception ex)
        {

        }
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    public void getProviderData(){
        if (cc.isConnectingToInternet()) {

            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(getContext());
                HashMap<String, Object> searchCriteria = new HashMap<String, Object>();
                searchCriteria.put(Events.event_provider_key, strProviderVal);
                searchCriteria.put(Events.event_rating_key, strRatingVal);
                searchCriteria.put(Events.event_society_key, strSocVal);
                searchCriteria.put(Events.event_date_key, new java.util.Date());
                cleverTap.event.push(Events.searched_provider_criteria, searchCriteria);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }

            tvNoProvider.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            InputBeanSearchProvider inputBeanSearchProvider = new InputBeanSearchProvider();
            inputBeanSearchProvider.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(context)));
            inputBeanSearchProvider.setServiceProvider(strProviderVal);
            inputBeanSearchProvider.setRatingInput(strRatingVal);
            inputBeanSearchProvider.setNearBy(strSocVal);
            new PostData(new Gson().toJson(inputBeanSearchProvider), MainActivity.MainContext, CallFor.PROVIDERS_DATA).execute();
        }
        else{
            MainActivity.ShowErrorAlert();
            tvNoProvider.setVisibility(View.VISIBLE);
            tvNoProvider.setText("Please check internet connection");
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
