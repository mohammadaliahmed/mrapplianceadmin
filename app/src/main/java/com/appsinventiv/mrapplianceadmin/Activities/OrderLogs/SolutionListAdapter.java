package com.appsinventiv.mrapplianceadmin.Activities.OrderLogs;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;



/**
 * Created by AliAh on 30/06/2018.
 */

public class SolutionListAdapter extends RecyclerView.Adapter<SolutionListAdapter.ViewHolder> {
    Context context;
    ArrayList<SolutionTrackingModel> itemList;


    public SolutionListAdapter(Context context, ArrayList<SolutionTrackingModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.solution_item_layout, parent, false);
        SolutionListAdapter.ViewHolder viewHolder = new SolutionListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SolutionTrackingModel model = itemList.get(position);
        holder.text.setText((position + 1) + ") " + model.getText());
        holder.date.setText(CommonUtils.getTimeOnly(model.getTime()));

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, date;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);


        }
    }


}
