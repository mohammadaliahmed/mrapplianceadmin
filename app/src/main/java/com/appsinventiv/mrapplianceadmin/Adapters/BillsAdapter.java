package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.Activities.ViewInvoice;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceModel> itemList;
    BillsCallback callback;
    public BillsAdapter(Context context, ArrayList<InvoiceModel> itemList,BillsCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_item_layout, parent, false);
        BillsAdapter.ViewHolder viewHolder = new BillsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillsAdapter.ViewHolder holder, int position) {
        final InvoiceModel model = itemList.get(position);
        holder.orderDetails.setText("Bill Number: " + model.getInvoiceId()
                + "\n\nOrder Number: " + model.getOrder().getOrderId()
                + "\n\nTotal Price: AED " + model.getOrder().getTotalPrice()
                + "\n\nBill Time: " + CommonUtils.getFormattedDate(model.getTime())
        );
        holder.userDetails.setText("Name: " + model.getOrder().getUser().getFullName()
                + "\n\nAddress: " + model.getOrder().getOrderAddress()
                + "\n\nPhone: " + model.getOrder().getUser().getMobile()

        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewInvoice.class);
                i.putExtra("invoiceId", model.getInvoiceId());
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDelete(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDetails, userDetails;
        ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            userDetails = itemView.findViewById(R.id.userDetails);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            delete = itemView.findViewById(R.id.delete);
        }
    }
    public  interface BillsCallback{
        public void onDelete(InvoiceModel model);
    }
}
