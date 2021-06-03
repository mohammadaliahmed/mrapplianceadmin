package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.appsinventiv.mrapplianceadmin.Models.InvoiceItemModel;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class PreviewInvoiceAdapter extends RecyclerView.Adapter<PreviewInvoiceAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceItemModel> itemList;
    boolean value;

    public PreviewInvoiceAdapter(Context context, ArrayList<InvoiceItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(ArrayList<InvoiceItemModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.preview_invoice_item_layout, parent, false);
       PreviewInvoiceAdapter.ViewHolder viewHolder = new PreviewInvoiceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        InvoiceItemModel model = itemList.get(position);
        holder.description.setText(model.getDescription());
        holder.rate.setText("" + model.getPrice());
        holder.serial.setText((position + 1) + ". ");
        holder.qty.setText("" + model.getQuantity());
        holder.amount.setText("" + (model.getQuantity() * model.getPrice()));

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, rate, qty, amount, serial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            rate = itemView.findViewById(R.id.rate);
            serial = itemView.findViewById(R.id.serial);
            qty = itemView.findViewById(R.id.qty);
            amount = itemView.findViewById(R.id.amount);
        }
    }


}
