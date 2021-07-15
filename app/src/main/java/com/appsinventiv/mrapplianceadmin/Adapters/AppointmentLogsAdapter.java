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


import com.appsinventiv.mrapplianceadmin.Activities.PicturesSlider;
import com.appsinventiv.mrapplianceadmin.Models.LogsModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 30/06/2018.
 */

public class AppointmentLogsAdapter extends RecyclerView.Adapter<AppointmentLogsAdapter.ViewHolder> {
    Context context;
    ArrayList<LogsModel> itemList;


    public AppointmentLogsAdapter(Context context, ArrayList<LogsModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(ArrayList<LogsModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_logs_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LogsModel model = itemList.get(position);
        String text = "";
        if (model.getAssignTo() != null && !model.getAssignTo().equalsIgnoreCase("")) {
            text = model.getText() + "\n     Assigned To: " + model.getAssignTo();
        } else {
            text = model.getText();
        }
        holder.text.setText((position + 1) + ") " + text);
        holder.date.setText(model.getDateTime());
        if (model.getImageUrl() != null && !model.getImageUrl().equalsIgnoreCase("")) {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getImageUrl()).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> item = new ArrayList<>();
                item.add(model.getImageUrl());
                Intent i = new Intent(context, PicturesSlider.class);
                i.putExtra("list",item);
                i.putExtra("position",0);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, date;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.date);


        }
    }


}
