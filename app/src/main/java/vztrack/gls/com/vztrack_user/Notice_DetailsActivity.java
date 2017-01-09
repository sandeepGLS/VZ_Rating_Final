package vztrack.gls.com.vztrack_user;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class Notice_DetailsActivity extends AppCompatActivity {

    Bitmap show_large;
    CheckConnection cc;
    String title;
    String URLPhoto;
    ZoomControls zoom;
    CleverTapAPI ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice__details);

        CleverTap.cleverTap_Record_Event(this, Events.event_notice_details_activity);

        cc = new CheckConnection(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notice Details");

        TextView Title = (TextView) findViewById(R.id.tvTitle);
        TextView Description = (TextView) findViewById(R.id.tvDescription);
        TextView StartDate = (TextView) findViewById(R.id.tvStartDate);
        TextView EndDate = (TextView) findViewById(R.id.tvEndDate);
        ImageView imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        title = getIntent().getStringExtra("TITLE");
        Title.setText(getIntent().getStringExtra("TITLE"));
        Description.setText(getIntent().getStringExtra("DESCRIPTION"));
        StartDate.setText("Notice Start Date\n"+getIntent().getStringExtra("START_DATE"));
        EndDate.setText("Notice End Date\n"+getIntent().getStringExtra("END_DATE"));

        URLPhoto = getIntent().getStringExtra("PHOTO");

        cc = new CheckConnection(this);

        if(cc.isConnectingToInternet())
        {
            if(URLPhoto==null)
            {
                imgPhoto.setBackgroundResource(R.drawable.no_photo_icon);
            }
            else {
                new LoadImage().loadImage(this, R.drawable.no_photo_icon, URLPhoto ,imgPhoto, null);
            }
            BitmapDrawable drawable = (BitmapDrawable) imgPhoto.getDrawable();
            show_large = drawable.getBitmap();

        }
        else
        {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, URLPhoto ,imgPhoto, null);
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.backPressFlag=0;
        finish();
    }

//    public Bitmap StrintToBitmap(String imgString)
//    {
//        byte[] encodeByte = Base64.decode(imgString, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        return bitmap;
//
//    }
    public void ShowPhoto(View v)
    {
        final Dialog settingsDialog = new Dialog(this,R.style.DialogTheme);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
        View showData = getLayoutInflater().inflate(R.layout.image_layout, null);
        TextView ShowUserName = (TextView) showData.findViewById(R.id.tvNameAndNumber);
        final ImageView imageView = (ImageView) showData.findViewById(R.id.imgImage);
        cc = new CheckConnection(this);

        if(cc.isConnectingToInternet())
        {
            if(URLPhoto.equals("NO PHOTO"))
            {
                imageView.setImageResource(R.drawable.no_photo_icon);
            }
            else {
                new LoadImage().loadImage(this, R.drawable.no_photo_icon, URLPhoto ,imageView, null);
            }
        }
        else
        {
            if(URLPhoto.equals("NO PHOTO"))
            {
                imageView.setImageResource(R.drawable.no_photo_icon);

            }
            else {
                new LoadImage().loadImage(this, R.drawable.no_photo_icon, URLPhoto ,imageView, null);
            }

        }

        ShowUserName.setText(title);

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

}
