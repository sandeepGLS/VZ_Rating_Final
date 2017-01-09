package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.Visiters_Details;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitList;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class MyRecyclerViewAdapterVisitList extends RecyclerView.Adapter<MyRecyclerViewAdapterVisitList.DataObjectHolder> {
    private static String LOG_TAG = "AdapterVisitors";
    private ArrayList<DataObjectVisitList> mDataset;
    private Context context;
    public String strName,strMobile,strDateTime,strOutTime , strPurpose,strFrom,strVisitors,strVehicleNo;


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public TextView tvVisitors;
        public TextView tvPurpose;
        public TextView tvdateTime;
        public TextView tvFrom;
        public TextView tvVehicleno;
        public TextView tvOutTime;
        public ImageView imgProfilePic;
        public CardView cardView;
        public View line;
        public LinearLayout linearLayout;


        public DataObjectHolder(View itemView) {
            super(itemView);

            imgProfilePic = (ImageView)itemView.findViewById(R.id.circleView);
            tvName = (TextView) itemView.findViewById(R.id.tvWhome);
            tvVisitors = (TextView) itemView.findViewById(R.id.tvVisitors);
            tvdateTime = (TextView) itemView.findViewById(R.id.tvDateAndTime);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            tvFrom = (TextView) itemView.findViewById(R.id.tvfrom);
            tvVehicleno = (TextView) itemView.findViewById(R.id.tvVehicleno);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            line = (View) itemView.findViewById(R.id.line);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }


    public MyRecyclerViewAdapterVisitList(List<String> myDataset) {


    }

    public MyRecyclerViewAdapterVisitList(Context con, ArrayList<DataObjectVisitList> myDataset) {
        context = con;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_list, parent, false);
        CardView cv = (CardView) view.findViewById(R.id.card_view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = new Intent(context, Visiters_Details.class);
                context.startActivity(I);

            }
        });

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        strName = mDataset.get(position).getmWhomeToVisit();
        strDateTime = mDataset.get(position).getInTime();
        strOutTime = mDataset.get(position).getOutTime();
        strPurpose = mDataset.get(position).getmPurpose();
        strFrom = mDataset.get(position).getmFrom();
        strVehicleNo = mDataset.get(position).getmVehicle_no();
        strVisitors = mDataset.get(position).getmNoOfVisitors();

        holder.tvName.setText(strName);
        holder.tvVisitors.setText("( Total Visitors "+strVisitors+ " )");
        holder.tvdateTime.setText("In Time : "+strDateTime);
        try{
            if(strOutTime.equals("NO EXIT TIME") || strOutTime.trim().equals(null)){
                holder.tvOutTime.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }
            else{
                holder.tvOutTime.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
        }
        holder.tvOutTime.setText("Out Time-"+strOutTime);
        holder.tvPurpose.setText(strPurpose);
        holder.tvFrom.setText("From : "+strFrom);
        holder.tvVehicleno.setText("Vehicle No. : "+strVehicleNo);

        //holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(null);

    }

    public void addItem(DataObjectVisitList dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}