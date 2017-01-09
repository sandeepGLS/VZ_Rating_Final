package vztrack.gls.com.vztrack_user.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import vztrack.gls.com.vztrack_user.BaseActivity;
import vztrack.gls.com.vztrack_user.MainActivity;

/**
 * Created by Santosh on 06-Oct-15.
 */
public class GetData extends AsyncTask {
    String url,url_rating;
    String result = "";
    String callFor = "";
    String extendedUrl = "";
    BaseActivity activity;
    ProgressDialog progressDialog;
    String check;

    public GetData(BaseActivity activity, String callFor, String extendedUrl){
        this.activity = activity;
        this.callFor = callFor;
        this.extendedUrl = extendedUrl;
        url = createURL(callFor);
    }

    private String createURL(String callFor) {
        url = Constants.BASE_URL;
        url_rating = Constants.BASE_URL_FOR_RATING;

        if(callFor==CallFor.NOTICES){
            url = url+URL.NOTICES;
        }
        if(callFor==CallFor.VISITORS){
            url = url+URL.VISITORS+extendedUrl;
        }
        if(callFor==CallFor.VISITOR_LIST){
            url = url+URL.VISIT_LIST+extendedUrl;
        }
        if(callFor==CallFor.CHANGE_PASSWORD){
            url = url+URL.CHANGE_PASSWORD+extendedUrl;
        }
        if(callFor==CallFor.PROVIDER_LIST){
            url = url_rating+URL.PROVIDER_LIST+extendedUrl;
        }
        return url;
    }

    @Override
    protected void onPreExecute() {
        check = SheredPref.getRun(activity);
        if(check==null)
        {
            check="";
        }
        if(check.equals("RUN"))
        {
            if(MainActivity.fragment_flag==1)
            {

            }
            else
            {
                progressDialog = ProgressDialog.show(activity,"","Loading...");
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("URL ===>",url);
        try {
            result = ServerConnection.giveResponse(url,"");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try
        {
            super.onPostExecute(o);
            if(check==null)
            {
                check="";
            }
            if(check.equals("RUN"))
            {
                if(MainActivity.fragment_flag==1)
                {

                }
                else
                {
                    progressDialog.dismiss();
                }
            }
            activity.onGetResponse(result,callFor);
        }catch (Exception ex)
        {
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            final Snackbar snackBar = Snackbar.make(rootView, "Please check internet connection" , 5000);
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }

    }
}
