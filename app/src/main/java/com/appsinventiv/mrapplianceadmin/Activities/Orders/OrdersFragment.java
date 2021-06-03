package com.appsinventiv.mrapplianceadmin.Activities.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.appsinventiv.mrapplianceadmin.Models.LogsModel;
import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationAsync;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class OrdersFragment extends Fragment {

    Context context;
    RecyclerView recycler_orders;
    ArrayList<OrderModel> arrayList = new ArrayList<>();
    String orderStatus;
    OrdersAdapter adapter;
    DatabaseReference mDatabase;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public OrdersFragment(String orderStatus) {
        this.orderStatus = orderStatus;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrdersAdapter(context, arrayList, new OrdersAdapter.ChangeStatus() {
            @Override
            public void markOrderAsUnderProcess(OrderModel order, boolean b) {
                if (b) {
                    showUnderProcessDialog(order, b);
                } else {

                }
            }

            @Override
            public void markOrderAsCancelled(OrderModel order) {
                showCancelDilog(order);
            }
        });
        recyclerView.setAdapter(adapter);


        return rootView;


    }

    private void showUnderProcessDialog(final OrderModel order, boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to mark this order as under process? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Orders").child(order.getOrderId() + "").child("orderStatus").setValue("Under Process").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order Marked as Under Process");
                        NotificationAsync notificationAsync = new NotificationAsync(context);
                        String notification_title = "You order has been accepted ";
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", order.getUser().getFcmKey(), notification_title, notification_message, "Order", "abc");
                        String key = mDatabase.push().getKey();
                        mDatabase.child("OrderLogs").child(order.getUser().getUsername()).child("" + order.getOrderId()).child(key).setValue(new LogsModel(
                                key, "Order is Under Process", System.currentTimeMillis()
                        ));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCancelDilog(final OrderModel order) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Cancel Order?");
        alertDialog.setMessage("Enter reason..");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("cancelReason", input.getText().toString());
                        map.put("cancelled", true);
                        map.put("orderStatus", "Cancelled");

                        mDatabase.child("Orders").child("" + order.getOrderId()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Order Cancelled");
                                adapter.notifyDataSetChanged();
                                NotificationAsync notificationAsync = new NotificationAsync(context);
                                String notification_title = "You order has been cancelled ";
                                String notification_message = "Reason: " + input.getText().toString();
                                notificationAsync.execute("ali", order.getUser().getFcmKey(), notification_title, notification_message, "Order", "abc");
                                String key = mDatabase.push().getKey();
                                mDatabase.child("OrderLogs").child(order.getUser().getUsername()).child("" + order.getOrderId()).child(key).setValue(new LogsModel(
                                        key, "Order is cancelled", System.currentTimeMillis()
                                ));

                            }
                        }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }
// setup the alert builder


    private void getDataFromServer() {

        mDatabase.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    arrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            OrderModel model = snapshot.getValue(OrderModel.class);
                            if (model != null) {
                                if (model.getOrderStatus().equalsIgnoreCase(orderStatus)) {
                                    arrayList.add(model);

                                }
                            }
                        } catch (Exception e) {
                            CommonUtils.showToast(e.getMessage());
                        }
                    }
                    Collections.sort(arrayList, new Comparator<OrderModel>() {
                        @Override
                        public int compare(OrderModel listData, OrderModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob2.compareTo(ob1);

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
