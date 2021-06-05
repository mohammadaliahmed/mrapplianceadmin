package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Models.InvoiceItemModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryItemModel;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class AddSalaryItemsAdapter extends RecyclerView.Adapter<AddSalaryItemsAdapter.ViewHolder> {
    Context context;
    ArrayList<SalaryItemModel> itemList;
    AddSalaryItemsAdapterCallback callback;

    public AddSalaryItemsAdapter(Context context, ArrayList<SalaryItemModel> itemList, AddSalaryItemsAdapterCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(ArrayList<SalaryItemModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_salary_item_layout, parent, false);
        AddSalaryItemsAdapter.ViewHolder viewHolder = new AddSalaryItemsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SalaryItemModel model = itemList.get(position);
        holder.name.setText((position + 1) + ") " + model.getName());
        holder.amount.setText("AED " + model.getAmount());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDelete(model,position);
            }
        });


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

    public interface AddSalaryItemsAdapterCallback {
        public void onDelete(SalaryItemModel model, int position);
    }


}
