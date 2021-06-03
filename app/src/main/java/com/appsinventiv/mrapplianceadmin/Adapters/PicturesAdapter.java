package com.appsinventiv.mrapplianceadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.appsinventiv.mrapplianceadmin.Activities.PicturesSlider;
import com.appsinventiv.mrapplianceadmin.R;


import java.util.ArrayList;



public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;
    PicturesAdapterCallback callback;
    boolean value;

    public PicturesAdapter(Context context, ArrayList<String> itemList, PicturesAdapterCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picture_layout, parent, false);
        PicturesAdapter.ViewHolder viewHolder = new PicturesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(itemList.get(position)).into(holder.image);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PicturesSlider.class);
                i.putExtra("list",itemList);
                i.putExtra("position",position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface PicturesAdapterCallback {
        public void onDelete(int position);
    }
}
