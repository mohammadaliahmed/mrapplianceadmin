package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.appsinventiv.mrapplianceadmin.Activities.PicturesSlider;
import com.appsinventiv.mrapplianceadmin.Models.ExpensesModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ExpensesListAdapter extends RecyclerView.Adapter<ExpensesListAdapter.ViewHolder> {
    Context context;
    ArrayList<ExpensesModel> itemList;
    ExpensesItemAdapterCallback callback;

    public ExpensesListAdapter(Context context, ArrayList<ExpensesModel> itemList, ExpensesItemAdapterCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(ArrayList<ExpensesModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_item_layout, parent, false);
        ExpensesListAdapter.ViewHolder viewHolder = new ExpensesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ExpensesModel model = itemList.get(position);
        holder.serial.setText((position + 1) + ")");
        holder.title.setText("Category: " + model.getCategory() + "\nBy: " + model.getStaffMember() + "\n\n" + model.getTitle());
        holder.status.setText(model.getStatus());
        holder.amount.setText("AED " + model.getPrice());
        holder.date.setText(model.getDate());
        holder.description.setText(model.getDescription());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onDelete(model);
                return false;

            }
        });
        if (model.getImgUrl() != null) {
            Glide.with(context).load(model.getImgUrl()).into(holder.receipt);
            holder.receipt.setVisibility(View.VISIBLE);


        } else {
            holder.receipt.setVisibility(View.GONE);
        }
        holder.receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PicturesSlider.class);
                ArrayList<String> picList = new ArrayList<>();
                picList.add(model.getImgUrl());
                i.putExtra("list", picList);
                i.putExtra("position", 0);
                context.startActivity(i);
            }
        });
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onApprove(model);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onReject(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button approve, reject;
        TextView title, status, amount, date, serial, description;
        ImageView receipt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            serial = itemView.findViewById(R.id.serial);
            status = itemView.findViewById(R.id.status);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            approve = itemView.findViewById(R.id.approve);
            receipt = itemView.findViewById(R.id.receipt);
            reject = itemView.findViewById(R.id.reject);

        }
    }

    public interface ExpensesItemAdapterCallback {
        public void onDelete(ExpensesModel model);

        public void onApprove(ExpensesModel model);

        public void onReject(ExpensesModel model);
    }
}
