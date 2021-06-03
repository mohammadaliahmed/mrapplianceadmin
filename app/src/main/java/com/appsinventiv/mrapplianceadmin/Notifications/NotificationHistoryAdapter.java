package com.appsinventiv.mrapplianceadmin.Notifications;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.R;

import java.util.ArrayList;

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<CustomerNotificationModel> itemList = new ArrayList<>();
    NotificationCallbacks callbacks;

    public NotificationHistoryAdapter(Context context, ArrayList<CustomerNotificationModel> itemList, NotificationCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        NotificationHistoryAdapter.ViewHolder viewHolder = new NotificationHistoryAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerNotificationModel model = itemList.get(position);
        holder.title.setText(model.getTitle());
        holder.message.setText(model.getMessage());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.delete(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface NotificationCallbacks {
        public void delete(CustomerNotificationModel model);
    }

}
