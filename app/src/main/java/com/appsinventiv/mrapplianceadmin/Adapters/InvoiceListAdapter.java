package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.Activities.ViewInvoice;
import com.appsinventiv.mrapplianceadmin.Models.CustomInvoiceModel;
import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.ViewHolder> {
    Context context;
    ArrayList<CustomInvoiceModel> itemList;
    boolean value;

    public InvoiceListAdapter(Context context, ArrayList<CustomInvoiceModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(ArrayList<CustomInvoiceModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_invoice_item_layout, parent, false);
        InvoiceListAdapter.ViewHolder viewHolder = new InvoiceListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CustomInvoiceModel model = itemList.get(position);
        holder.name.setText(model.getUser().getFirstname() + " " + model.getUser().getLastname() + "\nInvoice: " + model.getInvoiceId());
        holder.date.setText(CommonUtils.getFormattedDate(model.getTime()) + "\nStatus: " + model.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewInvoice.class);
                i.putExtra("invoiceId", model.getInvoiceId());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
        }
    }
}