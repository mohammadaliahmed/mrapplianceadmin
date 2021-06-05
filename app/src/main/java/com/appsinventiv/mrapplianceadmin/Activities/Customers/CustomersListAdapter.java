package com.appsinventiv.mrapplianceadmin.Activities.Customers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Models.User;
import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.ViewHolder> {
    Context context;
    ArrayList<User> itemlist;
    private ArrayList<User> arrayList;

    public CustomersListAdapter(Context context, ArrayList<User> itemlist) {
        this.context = context;
        this.itemlist = itemlist;
        this.arrayList = new ArrayList<>(itemlist);

    }

    public void updateList(ArrayList<User> itemlist) {
        this.itemlist = itemlist;
        arrayList.clear();
        arrayList.addAll(itemlist);
        notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemlist.clear();
        if (charText.length() == 0) {
            itemlist.addAll(arrayList);
        } else {
            for (User product : arrayList) {
                try {
                    if (product.getFullName().toLowerCase().startsWith(charText) ||
                            product.getPhone().toLowerCase().contains(charText) ||
                            product.getMobile().toLowerCase().contains(charText)
                    ) {
                        itemlist.add(product);
                    }
                } catch (Exception e) {

                }
            }


        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_layout, viewGroup, false);
        CustomersListAdapter.ViewHolder viewHolder = new CustomersListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final User model = itemlist.get(i);
        holder.name.setText(model.getFullName());
        holder.phone.setText(model.getMobile());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewCustomer.class);
                i.putExtra("userId", model.getUsername());
                i.putExtra("username", model.getFullName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            phone = itemView.findViewById(R.id.phone);

        }
    }

}
