package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.BaseActivity;
import vztrack.gls.com.vztrack_user.BuildConfig;
import vztrack.gls.com.vztrack_user.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class SettingFragment extends Fragment {

    BaseActivity context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    CheckConnection cc;
    public static EditText OldPassword,NewPassword1,NewPassword2;
    Button Submit;
    public static String old_password,new_password,confirm_password;
    private CheckBox notification_checkbox;
    private AppCompatCheckBox sound_checkbox;
    private AppCompatCheckBox vibration_checkbox;
    private RelativeLayout sound_layout;
    private RelativeLayout vibrate_layout;
    private TextView tv_version;
    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_setting, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_setting_fragment);

        OldPassword = (EditText) root.findViewById(R.id.etPasswordOld);
        NewPassword1 = (EditText) root.findViewById(R.id.etPasswordNew1);
        NewPassword2 = (EditText) root.findViewById(R.id.etPasswordNew2);
        notification_checkbox = (CheckBox) root.findViewById(R.id.notification_checkbox);
        sound_checkbox = (AppCompatCheckBox) root.findViewById(R.id.notification_sount_checkbox);
        vibration_checkbox = (AppCompatCheckBox) root.findViewById(R.id.notification_vibration_checkbox);
        Submit = (Button) root.findViewById(R.id.btnChangePassword);
        vibrate_layout = (RelativeLayout) root.findViewById(R.id.rel_lay_vibrate);
        sound_layout = (RelativeLayout) root.findViewById(R.id.rel_lay_sound);
        tv_version = (TextView) root.findViewById(R.id.version);
        context = MainActivity.MainContext;

        //int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        tv_version.setText("Current Version : "+versionName);

        // Check Internet Connection
        cc = new CheckConnection(context);


        if(SheredPref.getNotification(getActivity()).equals("ENABLE"))
        {
            notification_checkbox.setChecked(true);
        }
        else {
            notification_checkbox.setChecked(false);
        }

        if(SheredPref.getSound(getActivity()).equals("ENABLE"))
        {
            sound_checkbox.setChecked(true);
        }
        else {
            sound_checkbox.setChecked(false);
        }

        if(SheredPref.getVibration(getActivity()).equals("ENABLE"))
        {
            vibration_checkbox.setChecked(true);
        }
        else {
            vibration_checkbox.setChecked(false);
        }

        notification_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    SheredPref.setNotification(getActivity(),"ENABLE");
                    sound_checkbox.setChecked(true);
                    vibration_checkbox.setChecked(true);
                    SheredPref.setSound(getActivity(),"ENABLE");
                    SheredPref.setVibration(getActivity(),"ENABLE");
                    sound_layout.setVisibility(View.VISIBLE);
                    vibrate_layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Notification");
                    builder.setMessage("This Will Stop VZ-Track Notification,\nDo You Want To Stop Notification?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            notification_checkbox.setChecked(false);
                            sound_checkbox.setChecked(false);
                            vibration_checkbox.setChecked(false);

                            sound_layout.setVisibility(View.GONE);
                            vibrate_layout.setVisibility(View.GONE);

                            SheredPref.setNotification(getActivity(),"DISABLE");
                            SheredPref.setSound(getActivity(),"DISABLE");
                            SheredPref.setVibration(getActivity(),"DISABLE");
                        }
                    });

                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            notification_checkbox.setChecked(true);
                            //Log.e("DISMISS "," DIALOG");
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });
        sound_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   if(isChecked)
                    {
                        //Toast.makeText(context,"Sound Checked",Toast.LENGTH_LONG).show();
                        SheredPref.setSound(getActivity(),"ENABLE");
                    }
                    else
                    {
                        /*SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setCancelable(false);
                        pDialog.show();*/
                        //Toast.makeText(context,"Sound UnChecked",Toast.LENGTH_LONG).show();
                        SheredPref.setSound(getActivity(),"DISABLE");
                    }


            }
        });
        vibration_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {
                        //Toast.makeText(context,"vibration checked",Toast.LENGTH_LONG).show();
                        SheredPref.setVibration(getActivity(),"ENABLE");
                    }
                    else
                    {
                        //Toast.makeText(context,"vibration Unchecked",Toast.LENGTH_LONG).show();
                        SheredPref.setVibration(getActivity(),"DISABLE");
                    }
            }
        });


        if(cc.isConnectingToInternet())
        {

        }
        else
        {
            Toast.makeText(getActivity(),"No Internet Connection ",Toast.LENGTH_LONG).show();
        }

        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //SOME CODE
            }
        };


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cc.isConnectingToInternet())
                {
                    old_password = OldPassword.getText().toString().trim();
                    new_password = NewPassword1 .getText().toString().trim();
                    confirm_password = NewPassword2.getText().toString().trim();

                    if( SheredPref.getPassword(context).equals(old_password))
                    {
                        if( new_password.equals(confirm_password))
                        {
                            if( new_password.length()>=6)
                            {
                                new GetData(context, CallFor.CHANGE_PASSWORD, new_password).execute();
                            }
                            else
                            {
                                Toast.makeText(context,"Password length should be greater than 5",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"New password mismatch",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context,"Old password mismatch",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"No Internet Connection,\nCan't change password",Toast.LENGTH_LONG).show();
                }
            }
        });



        return root;
    }


}