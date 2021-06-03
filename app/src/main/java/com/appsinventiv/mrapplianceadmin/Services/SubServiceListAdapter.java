package com.appsinventiv.mrapplianceadmin.Services;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class SubServiceListAdapter extends RecyclerView.Adapter<SubServiceListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubServiceModel> itemlist;
    SubServiceListAdapterCallbacks callbacks;

    public SubServiceListAdapter(Context context, ArrayList<SubServiceModel> itemlist, SubServiceListAdapterCallbacks callbacks) {
        this.context = context;
        this.itemlist = itemlist;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_service_item_layout, viewGroup, false);
        SubServiceListAdapter.ViewHolder viewHolder = new SubServiceListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final SubServiceModel model = itemlist.get(i);
        holder.name.setText(model.getName());


        if (model.isActive()) {
            holder.activate.setChecked(true);
        } else {
            holder.activate.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddSubService.class);
                i.putExtra("id", model.getId());
                i.putExtra("parentService", model.getParentService());

                context.startActivity(i);
            }
        });


        holder.activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    callbacks.onServiceStatusChanged(model, isChecked);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Switch activate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            activate = itemView.findViewById(R.id.activate);

        }
    }

    public interface SubServiceListAdapterCallbacks {
        public void onServiceStatusChanged(SubServiceModel model, boolean value);

        public void onServiceDeleted(SubServiceModel model);
    }
}
