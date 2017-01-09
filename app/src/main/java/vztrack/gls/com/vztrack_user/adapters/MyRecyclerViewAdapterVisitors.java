package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.Visiters_Details;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.fragment.VisitorsFragment;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class MyRecyclerViewAdapterVisitors extends RecyclerView.Adapter<MyRecyclerViewAdapterVisitors.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapterVisitors";
    private ArrayList<DataObjectVisitors> mDataset;
    private Context context;
    public String strName,strMobile,strDateTime,strPurpose,strFrom,strPhotUrl,strDocUrl;
    CheckConnection cc;
    int size;
    private final int TITLE = 0;
    private final int LOAD_MORE = 1;
    private boolean hasLoadButton = true;
    public static String photo[]=new String[10];
    DbHelper dbHelper;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public TextView tvdateTime;
        public TextView tvPurpose;
        public TextView tvMobile, tvFrom;
        public ImageView imgProfilePic;
        public CardView cardView;
        public LinearLayout linearLayout;
        public Button btnLoadMore;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imgProfilePic = (ImageView)itemView.findViewById(R.id.circleView);
            tvName = (TextView) itemView.findViewById(R.id.tvTitle);
            tvMobile = (TextView) itemView.findViewById(R.id.tvMobile);
            tvdateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            tvFrom = (TextView) itemView.findViewById(R.id.tvFrom);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            btnLoadMore = (Button) itemView.findViewById(R.id.load_more);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }


    public MyRecyclerViewAdapterVisitors(Context con, ArrayList<DataObjectVisitors> myDataset) {
        context = con;
        mDataset = myDataset;
        size = mDataset.size();
        dbHelper = new DbHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount()-1) {
            return TITLE;
        } else {
            return LOAD_MORE;
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visiters_list, parent, false);
            CardView cv = (CardView) view.findViewById(R.id.card_view);
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            cc = new CheckConnection(context);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent I = new Intent(context, Visiters_Details.class);
                    context.startActivity(I);
                }
            });

            return dataObjectHolder;
        } else if (viewType == LOAD_MORE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_row, parent, false);
            Button Load_m_button = (Button) v.findViewById(R.id.load_more);
            Log.e("SIZE "," "+mDataset.size());
            if(mDataset.size()%10 !=0 || mDataset.size()==0) {
                Load_m_button.setVisibility(View.GONE);
            }
            else if(MainActivity.visitor_PageNo!=0 && MainActivity.visitorsArray.getVisitors().size()==0){
                Toast.makeText(context,"You have reached last visitor...",Toast.LENGTH_LONG).show();
                Load_m_button.setVisibility(View.GONE);
            }
            else
            {
                Load_m_button.setVisibility(View.VISIBLE);
            }
            return new DataObjectHolder(v);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        if(position >= getItemCount()-1) {
            holder.btnLoadMore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    holder.btnLoadMore.setBackgroundColor(Color.parseColor("#013220"));
                    return false;
                }



            });
            holder.btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.visitor_PageNo = MainActivity.visitor_PageNo+1;
                    if(cc.isConnectingToInternet())
                    {
                        new GetData(MainActivity.MainContext, CallFor.VISITORS, ""+MainActivity.visitor_PageNo).execute();
                    }
                    else
                    {
                        holder.btnLoadMore.setVisibility(View.GONE);
                        Toast.makeText(context,"Unable to load more, No internet connection",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            strName = mDataset.get(position).getmFirst_Name()+" "+mDataset.get(position).getmLast_Name();
            strMobile = mDataset.get(position).getmMobile();
            strDateTime = mDataset.get(position).getmTime();
            strPurpose = mDataset.get(position).getmPurpose();
            strFrom = mDataset.get(position).getmFrom();

            holder.tvName.setText(strName);
            holder.tvMobile.setText(strMobile);
            holder.tvdateTime.setText(strDateTime);
            holder.tvPurpose.setText(strPurpose);
            holder.tvFrom.setText(strFrom);

            if(cc.isConnectingToInternet())
            {
                strPhotUrl = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmPhoto();
                strDocUrl = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmDocUrl();
                new LoadImage().loadImage(context, R.drawable.no_photo_icon, strPhotUrl ,  holder.imgProfilePic, null);
            }
            else
            {
                strPhotUrl = mDataset.get(position).getmPhoto();
                strDocUrl = mDataset.get(position).getmDocUrl();

                new LoadImage().loadImage(context, R.drawable.no_photo_icon, strPhotUrl ,  holder.imgProfilePic, null);
            }
            holder.cardView.setTag(position);
            holder.cardView.setOnClickListener(clickListener);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos= (int) view.getTag();
            if( cc.isConnectingToInternet())
            {
                Intent I = new Intent(context, Visiters_Details.class);
                I.putExtra("PHOTO_URL",Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmPhoto());
                I.putExtra("NAME",mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name());
                I.putExtra("CONTACT_NO",mDataset.get(pos).getmMobile());
                I.putExtra("DOC_URL",Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmDocUrl());
                I.putExtra("POS",""+pos);
                context.startActivity(I);
            }
            else
            {
                Intent I = new Intent(context, Visiters_Details.class);
                I.putExtra("PHOTO_URL",mDataset.get(pos).getmPhoto());
                I.putExtra("NAME",mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name());
                I.putExtra("CONTACT_NO",mDataset.get(pos).getmMobile());
                I.putExtra("DOC_URL",mDataset.get(pos).getmDocUrl());
                I.putExtra("POS",""+pos);
                context.startActivity(I);
            }


        }
    };

    @Override
    public int getItemCount() {
        if (hasLoadButton) {
            return mDataset.size() + 1;
        } else {
            return mDataset.size();
        }
    }
}