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

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.Visiters_Details;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitList;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;

public class MyRecyclerViewAdapterCommetsList extends RecyclerView.Adapter<MyRecyclerViewAdapterCommetsList.DataObjectHolder> {
    private static String LOG_TAG = "AdapterVisitors";
    private OutputBeanSearchProvider outputBeanSearchProviders;
    private Context context;
    public String strName,strDate,strComments;


    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public TextView tvDate;
        public TextView tvComment;
        public View line;
        public LinearLayout linearLayout;


        public DataObjectHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }


    public MyRecyclerViewAdapterCommetsList(List<String> myDataset) {
    }

    public MyRecyclerViewAdapterCommetsList(Context con, OutputBeanSearchProvider outputBeanSearchProviders) {
        context = con;
        this.outputBeanSearchProviders = outputBeanSearchProviders;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commets_list, parent, false);
        CardView cv = (CardView) view.findViewById(R.id.card_view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        strName = outputBeanSearchProviders.getListCommentsBeans().get(position).getName();
        strDate = outputBeanSearchProviders.getListCommentsBeans().get(position).getTime();
        strComments = outputBeanSearchProviders.getListCommentsBeans().get(position).getComment();

        holder.tvName.setText(strName);
        holder.tvDate.setText(strDate);
        holder.tvComment.setText(strComments);

    }

    public void addItem(DataObjectVisitList dataObj, int index) {
    }

    public void deleteItem(int index) {
    }

    @Override
    public int getItemCount() {
        return outputBeanSearchProviders.getListCommentsBeans().size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}