package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Activities.ViewInvoice;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;

public class TrackingStaffNamesAdapter extends RecyclerView.Adapter<TrackingStaffNamesAdapter.ViewHolder> {
    Context context;
    ArrayList<ServicemanModel> itemList;
    TrackingStaffNamesAdapterCallbacks callback;
    public TrackingStaffNamesAdapter(Context context, ArrayList<ServicemanModel> itemList, TrackingStaffNamesAdapterCallbacks callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(ArrayList<ServicemanModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrackingStaffNamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tracking_staffname_item_layout, parent, false);
        TrackingStaffNamesAdapter.ViewHolder viewHolder = new TrackingStaffNamesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingStaffNamesAdapter.ViewHolder holder, int position) {
        final ServicemanModel model = itemList.get(position);
        holder.name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);

        }
    }
    public  interface TrackingStaffNamesAdapterCallbacks{
        public void onClick(ServicemanModel model);
    }
}
