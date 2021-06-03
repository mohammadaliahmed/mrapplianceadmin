package com.appsinventiv.mrapplianceadmin.Servicemen;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ServicemanListAdapter extends RecyclerView.Adapter<ServicemanListAdapter.ViewHolder> {
    Context context;
    ArrayList<ServicemanModel> itemlist;
    ServicemenListAdapterCallbacks callbacks;

    public ServicemanListAdapter(Context context, ArrayList<ServicemanModel> itemlist, ServicemenListAdapterCallbacks callbacks) {
        this.context = context;
        this.itemlist = itemlist;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.serviceman_item_layout, viewGroup, false);
        ServicemanListAdapter.ViewHolder viewHolder = new ServicemanListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final ServicemanModel model = itemlist.get(i);
        holder.name.setText(model.getName());
        holder.role.setText(model.getRole());
        if (model.getImageUrl() != null || !model.getImageUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getImageUrl()).into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.ic_profile_plc).into(holder.image);

        }


        if (model.isActive()) {
            holder.activate.setChecked(true);
        } else {
            holder.activate.setChecked(false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddServicemen.class);
                i.putExtra("id", model.getId());
                context.startActivity(i);

            }
        });

        holder.activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    callbacks.onServicemanStatusChanged(model, isChecked);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView options;
        TextView name, role;
        Switch activate;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            options = itemView.findViewById(R.id.options);
            name = itemView.findViewById(R.id.name);
            activate = itemView.findViewById(R.id.activate);
            role = itemView.findViewById(R.id.role);
            image = itemView.findViewById(R.id.image);

        }
    }

    public interface ServicemenListAdapterCallbacks {
        public void onServicemanStatusChanged(ServicemanModel model, boolean value);

        public void onServicemanDeleted(ServicemanModel model);
    }
}
