package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Models.ExpensesModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryModel;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class SalariesListAdapter extends RecyclerView.Adapter<SalariesListAdapter.ViewHolder> {
    Context context;
    ArrayList<SalaryModel> itemList;
    SalaryItemAdapterCallback callback;

    public SalariesListAdapter(Context context, ArrayList<SalaryModel> itemList, SalaryItemAdapterCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    public void setItemList(ArrayList<SalaryModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salary_item_layout, parent, false);
        SalariesListAdapter.ViewHolder viewHolder = new SalariesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SalaryModel model = itemList.get(position);
        holder.serial.setText((position + 1) + ")");
        holder.name.setText("Name: " + model.getServiceman().getName());
        holder.amount.setText("AED " + model.getAmount());
        holder.deduction.setText("AED " + model.getDeduction());
        holder.total.setText("AED " + model.getTotal());
        holder.date.setText(model.getDay() + "/" + model.getMonth() + "/" + model.getYear());


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onDelete(model);
                return false;

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, date, serial, deduction, total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            serial = itemView.findViewById(R.id.serial);
            deduction = itemView.findViewById(R.id.deduction);
            total = itemView.findViewById(R.id.total);


        }
    }

    public interface SalaryItemAdapterCallback {
        public void onDelete(SalaryModel model);


    }
}
