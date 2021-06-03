package com.appsinventiv.mrapplianceadmin.Activities.CustomOrder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Services.SubServiceModel;

import java.util.ArrayList;

public class SubserviceListAdapter extends RecyclerView.Adapter<SubserviceListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubServiceModel> itemList;

    SubserviceListAdapterCallbacks callbacks;

    public SubserviceListAdapter(Context context, ArrayList<SubServiceModel> itemList, SubserviceListAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    public void setItemList(ArrayList<SubServiceModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_item_layout, viewGroup, false);
        SubserviceListAdapter.ViewHolder viewHolder = new SubserviceListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final SubServiceModel model = itemList.get(i);
        viewHolder.check_box.setText(model.getName());
        viewHolder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        callbacks.onSelected(model);
                    } else {
                        callbacks.unSelected(model);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check_box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check_box = itemView.findViewById(R.id.check_box);
        }
    }

    public interface SubserviceListAdapterCallbacks {
        public void onSelected(SubServiceModel model);

        public void unSelected(SubServiceModel model);

    }
}
