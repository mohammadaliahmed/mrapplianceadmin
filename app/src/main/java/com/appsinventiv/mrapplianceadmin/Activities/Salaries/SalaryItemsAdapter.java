package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Models.SalaryItemModel;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class SalaryItemsAdapter extends RecyclerView.Adapter<SalaryItemsAdapter.ViewHolder> {
    Context context;
    ArrayList<SalaryItemModel> itemList;

    public SalaryItemsAdapter(Context context, ArrayList<SalaryItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(ArrayList<SalaryItemModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salary_items_layout, parent, false);
        SalaryItemsAdapter.ViewHolder viewHolder = new SalaryItemsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SalaryItemModel model = itemList.get(position);
        holder.name.setText((position + 1) + ") " + model.getName());
        holder.amount.setText("AED " + model.getAmount());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);

        }
    }


}
