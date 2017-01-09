package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.Notice_DetailsActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.DataObjectNotices;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class MyRecyclerViewAdapter_notices extends RecyclerView.Adapter<MyRecyclerViewAdapter_notices.MyViewHolder> {

    private ArrayList<DataObjectNotices> mDataset;
    Context context;
    String PhotoURL;
    String strTitle;
    String strDescription;
    String strStartDate,strEndDate;
    private static MyClickListener myClickListener;
   // public int InternetCheckFlag;
    private List<String> visibleObjects;
    private List<String> allObjects;
    CheckConnection cc;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView title, description, startDate,endDate;
        public ImageView imgIcon;
        public CardView cardView;
        public LinearLayout linearLayout;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvNoticeTitle);
            description = (TextView) view.findViewById(R.id.tvNoticeDesc);
            startDate = (TextView) view.findViewById(R.id.tvStartDate);
            endDate = (TextView) view.findViewById(R.id.tvEndDate);
            imgIcon = (ImageView)view.findViewById(R.id.imgIcon);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }

    public MyRecyclerViewAdapter_notices(Context con,ArrayList<DataObjectNotices> myDataset) {
        this.mDataset = myDataset;
        this.context = con;
       // this.InternetCheckFlag = InternetCheckFlag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notices_list, parent, false);

        cc = new CheckConnection(context);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        strTitle = mDataset.get(position).getmTextTitle();
        strDescription = mDataset.get(position).getmTextdescription();
        strStartDate = mDataset.get(position).getmTextDateStart();
        strEndDate = mDataset.get(position).getmTextDateEnd();

        allObjects=new ArrayList<>();

        for(int i=0;i<mDataset.size();i++)
        {
            allObjects.add(mDataset.get(i).getmTextTitle());
        }

        holder.title.setText(strTitle);
        holder.description.setText(strDescription);
        holder.startDate.setText("Start Date : "+strStartDate);
        holder.endDate.setText("End Date : "+strEndDate);
        if(cc.isConnectingToInternet())
        {
            PhotoURL = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmTextURL();
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, PhotoURL ,  holder.imgIcon, null);
        }
        else
        {
            if(mDataset.get(position).getmTextURL().equals("NO PHOTO"))
            {
                holder.imgIcon.setBackgroundResource(R.drawable.no_photo_icon);
            }
            else
            {
                new LoadImage().loadImage(context, R.drawable.no_photo_icon, mDataset.get(position).getmTextURL() ,  holder.imgIcon, null);

            }

        }
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int pos= (int) view.getTag();

            Intent I = new Intent(context, Notice_DetailsActivity.class);
                    I.putExtra("TITLE",mDataset.get(pos).getmTextTitle());
                    I.putExtra("DESCRIPTION",mDataset.get(pos).getmTextdescription());
                    I.putExtra("START_DATE",mDataset.get(pos).getmTextDateStart());
                    I.putExtra("END_DATE",mDataset.get(pos).getmTextDateEnd());
                    if (cc.isConnectingToInternet())
                    {
                        I.putExtra("PHOTO",Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmTextURL());
                        I.putExtra("NET","1");
                    }
                    else
                    {
                        I.putExtra("PHOTO",mDataset.get(pos).getmTextURL());
                        I.putExtra("NET","0");
                    }

                    context.startActivity(I);
        }
    };

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}