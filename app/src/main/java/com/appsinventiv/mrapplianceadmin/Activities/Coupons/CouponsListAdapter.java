package com.appsinventiv.mrapplianceadmin.Activities.Coupons;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class CouponsListAdapter extends RecyclerView.Adapter<CouponsListAdapter.ViewHolder> {
    Context context;
    ArrayList<CouponModel> itemlist;
    CouponsListAdapterCallbacks callbacks;

    public CouponsListAdapter(Context context, ArrayList<CouponModel> itemlist, CouponsListAdapterCallbacks callbacks) {
        this.context = context;
        this.itemlist = itemlist;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coupon_item_layout, viewGroup, false);
        CouponsListAdapter.ViewHolder viewHolder = new CouponsListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final CouponModel model = itemlist.get(i);
        holder.name.setText("Name: " + model.getCouponName()
                + "\nCode: " + model.getCouponCode() + "\nDiscount: " + model.getDiscount());


        holder.serial.setText(""+(i+1));

        if (model.isActive()) {
            holder.activate.setChecked(true);
        } else {
            holder.activate.setChecked(false);
        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callbacks.onServiceDeleted(model);
                PopupMenu popup = new PopupMenu(context, holder.options);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                //handle menu1 click
                                Intent i = new Intent(context, AddCoupon.class);
                                i.putExtra("id", model.getCouponId());
                                context.startActivity(i);
                                return true;
                            case R.id.action_delete:
                                //handle menu2 click
                                callbacks.onCouponDeleted(model);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddCoupon.class);
                i.putExtra("id", model.getCouponId());
                context.startActivity(i);
            }
        });

        holder.activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    callbacks.onCouponStatusChanged(model, isChecked);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, options;
        TextView name,serial;
        Switch activate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            options = itemView.findViewById(R.id.options);
            name = itemView.findViewById(R.id.name);
            activate = itemView.findViewById(R.id.activate);
            serial = itemView.findViewById(R.id.serial);

        }
    }

    public interface CouponsListAdapterCallbacks {
        public void onCouponStatusChanged(CouponModel model, boolean value);

        public void onCouponDeleted(CouponModel model);
    }
}
