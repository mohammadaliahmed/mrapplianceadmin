package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.appsinventiv.mrapplianceadmin.Models.CalenderModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CalanderDaysAdapter extends RecyclerView.Adapter<CalanderDaysAdapter.ViewHolder> {
    Context context;
    List<Long> itemList = new ArrayList<>();

    public CalanderDaysAdapter(Context context, List<Long> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calender_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Long name = itemList.get(position);
        holder.name.setText(CommonUtils.getDayName(name) + " " + CommonUtils.getDayNumber(name));
        String today = CommonUtils.getDayName(name) + " " + CommonUtils.getDayNumber(name);
        String listCurrent = CommonUtils.getDayName(System.currentTimeMillis()) + " " + CommonUtils.getDayNumber(System.currentTimeMillis());
        if (today.equalsIgnoreCase(listCurrent)) {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        } else {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorDark));
        }
        List<CalenderModel> timeList = new ArrayList<>();
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "09", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "09", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "10", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "10", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "11", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "11", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "12", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "12", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "13", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "13", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "14", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "14", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "15", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "15", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "16", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "16", "30"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "17", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "18", "00"));
        timeList.add(new CalenderModel(Integer.parseInt(CommonUtils.getDayNumber(name)), Integer.parseInt(CommonUtils.getMonthNumber(name)), Integer.parseInt(CommonUtils.getYear(name)), "18", "30"));

        CalenderEventsAdapter adapter = new CalenderEventsAdapter(context, timeList);
        holder.recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.recycler.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView recycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            recycler = itemView.findViewById(R.id.recycler);
        }
    }
}
