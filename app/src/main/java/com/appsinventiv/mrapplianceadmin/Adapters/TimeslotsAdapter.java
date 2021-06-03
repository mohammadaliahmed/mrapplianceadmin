package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;



public class TimeslotsAdapter extends RecyclerView.Adapter<TimeslotsAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;
    ArrayList<String> unavailableTime;
    TimeSlotsCallback callback;
    private int checkedPosition = -1;

    public TimeslotsAdapter(Context context, ArrayList<String> itemList, ArrayList<String> unavailableTime) {
        this.context = context;
        this.itemList = itemList;
        this.unavailableTime = unavailableTime;
    }

    public void setCallback(TimeSlotsCallback callback) {
        this.callback = callback;
    }

    public void setUnavailableTime(ArrayList<String> unavailableTime){
        this.unavailableTime=unavailableTime;
        notifyDataSetChanged();
    }
   public void setavailableTime(ArrayList<String> itemList){
        this.itemList=itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot_item, parent, false);
        TimeslotsAdapter.ViewHolder viewHolder = new TimeslotsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String name = itemList.get(position);
        holder.button.setText(name);

        boolean flag = false;
        if(unavailableTime.size()>0){
            if(unavailableTime.contains(name)){
                flag=true;
            }
        }

        if(flag) {
            holder.button.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_disabled));
            holder.button.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }else{
            if (checkedPosition == -1) {
                holder.button.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                holder.button.setTextColor(context.getResources().getColor(R.color.colorBlue));
            } else {
                if (checkedPosition == position) {
                    holder.button.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    holder.button.setTextColor(context.getResources().getColor(R.color.colorWhite));
                } else {
                    holder.button.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selector));
                    holder.button.setTextColor(context.getResources().getColor(R.color.colorBlue));
                }
            }
        }

        final boolean finalFlag = flag;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalFlag) {
                    holder.button.setBackground(context.getResources().getDrawable(R.drawable.radio_flat_selected));
                    holder.button.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    callback.optionSelected(name);
                    if (checkedPosition != position) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = position;
                    }
                }else{
                    callback.optionUnblock(name);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }

    public  interface TimeSlotsCallback{
        public void optionSelected(String timeChosen);
        public void optionUnblock(String timeChosen);
    }
}
