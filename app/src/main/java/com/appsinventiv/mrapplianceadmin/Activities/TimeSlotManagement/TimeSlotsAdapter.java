package com.appsinventiv.mrapplianceadmin.Activities.TimeSlotManagement;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeSlotModel> itemlist;
    TimeslotCallbacks callbacks;


    public TimeSlotsAdapter(Context context, ArrayList<TimeSlotModel> itemlist, TimeslotCallbacks callbacks) {
        this.context = context;
        this.itemlist = itemlist;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeslot_item_layout, viewGroup, false);
        TimeSlotsAdapter.ViewHolder viewHolder = new TimeSlotsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final TimeSlotModel model = itemlist.get(i);
        holder.serviceName.setText(model.getServiceName());
        holder.timeSLot.setText(model.getTime() + "\n" + model.getDate() + "-" + model.getMonth() + "-" + model.getYear());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onDelete(model.getServiceName()+"/"+model.getYear()+"/"+model.getMonth()+"/"+model.getDate()+"/"+model.getTime());
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, timeSLot;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            serviceName = itemView.findViewById(R.id.serviceName);
            timeSLot = itemView.findViewById(R.id.timeSLot);


        }
    }

    public interface TimeslotCallbacks {
        public void onDelete(String id);

    }

}
