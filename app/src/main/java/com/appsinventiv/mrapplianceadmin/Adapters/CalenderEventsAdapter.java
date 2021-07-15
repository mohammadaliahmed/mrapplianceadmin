package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.appsinventiv.mrapplianceadmin.Activities.Appointments.AppointmentModel;
import com.appsinventiv.mrapplianceadmin.Activities.Appointments.NewAppointment;
import com.appsinventiv.mrapplianceadmin.Activities.MainActivity;
import com.appsinventiv.mrapplianceadmin.Models.CalenderModel;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CalenderEventsAdapter extends RecyclerView.Adapter<CalenderEventsAdapter.ViewHolder> {
    Context context;
    List<CalenderModel> itemList = new ArrayList<>();

    public CalenderEventsAdapter(Context context, List<CalenderModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CalenderModel time = itemList.get(position);
        holder.time.setText(time.getHour() + ":" + time.getMin());
        for (AppointmentModel model : MainActivity.appointmentList) {
            int hour = Integer.parseInt(model.getTimeSelected().split(":")[0]);
            int minu = Integer.parseInt(model.getTimeSelected().split(":")[1]);

            int day = Integer.parseInt(model.getDate().split("/")[0]);
            int month = Integer.parseInt(model.getDate().split("/")[1]);
            int year = Integer.parseInt(model.getDate().split("/")[2]);

            if (day == time.getDay() && month == time.getMonth() && year == time.getYear() && hour == Integer.parseInt(time.getHour()) && minu == Integer.parseInt(time.getMin())) {
                holder.filled.setVisibility(View.VISIBLE);
                holder.empty.setVisibility(View.GONE);
                holder.title.setText(model.getTitle());
                holder.staffName.setText("Staff: " + model.getStaff());
                holder.details.setText(model.getCustomerName() + "\n" + model.getLocation());
            }
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout empty;
        RelativeLayout filled;
        TextView time, details, title, staffName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empty = itemView.findViewById(R.id.empty);
            filled = itemView.findViewById(R.id.filled);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            time = itemView.findViewById(R.id.time);
            staffName = itemView.findViewById(R.id.staffName);
        }
    }
}
