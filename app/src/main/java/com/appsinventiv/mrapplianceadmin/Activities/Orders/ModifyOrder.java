package com.appsinventiv.mrapplianceadmin.Activities.Orders;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Services.ServiceModel;
import com.appsinventiv.mrapplianceadmin.Services.SubServiceModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationAsync;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationObserver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ModifyOrder extends AppCompatActivity implements NotificationObserver {
    RecyclerView recyclerview;
    ModifyOrderAdapter adapter;
    private ArrayList<SubServiceModel> itemList = new ArrayList<>();
    private ArrayList<ServiceCountModel> userCartServiceList = new ArrayList<>();

    public ArrayList<ServiceCountModel> orderList = new ArrayList<>();
    HashMap<String, ServiceCountModel> map = new HashMap<>();
    DatabaseReference mDatabase;
    public String parentService;
    TextView serviceName;

    RelativeLayout confirm;
    private boolean canGoNext = true;
    public ServiceModel parentServiceModel;
    private String orderId;
    private OrderModel model;
    private long finalTotalTime = 0;
    private long finalTotalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_order);
        recyclerview = findViewById(R.id.recyclerview);
        serviceName = findViewById(R.id.serviceName);
        confirm = findViewById(R.id.confirm);
        orderList = new ArrayList<>();


        orderId = getIntent().getStringExtra("orderId");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        parentService = getIntent().getStringExtra("parentService");
        this.setTitle(parentService);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });


        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ModifyOrderAdapter(this, itemList, userCartServiceList, new AddToCartCallbacks() {
            @Override
            public void addedToCart(final SubServiceModel services, int quantity, int position) {
                ServiceCountModel model = new ServiceCountModel(services, quantity, 1555);
                orderList.add(model);
                map.put(services.getName(), model);
                orderList.clear();
                for (Map.Entry<String, ServiceCountModel> entry : map.entrySet()) {
                    orderList.add(entry.getValue());
                }

            }

            @Override
            public void deletedFromCart(final SubServiceModel services, int position) {
                map.remove(services.getName());
                orderList.clear();
                for (Map.Entry<String, ServiceCountModel> entry : map.entrySet()) {
                    orderList.add(entry.getValue());
                }

            }

            @Override
            public void quantityUpdate(SubServiceModel services, int quantity, int position) {
                ServiceCountModel m = map.get(services.getName());
                if (m == null) {
                    m = new ServiceCountModel(services, quantity, 1555);
                    m.setQuantity(quantity);
                    map.put(services.getName(), m);
                } else {
                    m.setQuantity(quantity);
                    map.put(services.getName(), m);
                }

                orderList.clear();
                for (Map.Entry<String, ServiceCountModel> entry : map.entrySet()) {
                    orderList.add(entry.getValue());
                }

            }
        });


        recyclerview.setAdapter(adapter);

        getDataFromDB();
        getUserCartProductsFromDB();
        getParentServiceFromDB();


    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Confirm modified oder?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                calculateTotal();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("newCountModelList", orderList);
                hashMap.put("totalHours", finalTotalTime);
                hashMap.put("totalPrice", finalTotalCost);
                hashMap.put("modifiedOrderConfirmed", false);
                mDatabase.child("Orders").child(orderId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order Updated");
                        NotificationAsync notificationAsync = new NotificationAsync(ModifyOrder.this);
                        String notification_title = "Order Change Request";
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", model.getUser().getFcmKey(), notification_title, notification_message, "Modify", "" + orderId);
                        finish();

                    }
                });



            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private long calculateTotal() {
        float totalMinutes = 0;
        float hours = 0;
        for (ServiceCountModel model : orderList) {
            totalMinutes = totalMinutes + (model.getQuantity() * (model.getService().getTimeMin() + model.getService().getTimeHour()));
        }

        hours = (totalMinutes / 60);
        int h = (int) (totalMinutes / 60);

        float dif = hours - h;

        if (dif > 0.17) {
            finalTotalTime = h + 1;
        } else {
            finalTotalTime = h;
        }

        finalTotalCost = finalTotalTime * parentServiceModel.getServiceBasePrice();
        return finalTotalCost;
    }


    private void getParentServiceFromDB() {
        mDatabase.child("Services").child(parentService).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    parentServiceModel = dataSnapshot.getValue(ServiceModel.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getUserCartProductsFromDB() {
        mDatabase.child("Orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    model = dataSnapshot.getValue(OrderModel.class);
                    userCartServiceList = model.getCountModelArrayList();
                    adapter.setUserCartList(model.getCountModelArrayList());

                    if (model.isModifiedOrderConfirmed()) {
                        userCartServiceList = model.getCountModelArrayList();
                        adapter.setUserCartList(model.getCountModelArrayList());
                    } else {
                        if (model.getNewCountModelList() != null) {
                            userCartServiceList = model.getNewCountModelList();
                            adapter.setUserCartList(model.getNewCountModelList());
                        } else {
                            userCartServiceList = model.getCountModelArrayList();
                            adapter.setUserCartList(model.getCountModelArrayList());
                        }

                    }

                    updateDataToOrder();


                    adapter.notifyDataSetChanged();
                } else {
//                        ListOfSubServices.orderList.clear();
                    userCartServiceList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateDataToOrder() {
        for (ServiceCountModel m : userCartServiceList) {
            orderList.add(m);
            map.put(m.getService().getName(), m);
//            mDatabase.child("Orders").child(orderId).child("newCart").child(m.getService().getName()).setValue(m);

        }
    }

    private void getDataFromDB() {
        mDatabase.child("SubServices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubServiceModel model = snapshot.getValue(SubServiceModel.class);
                        if (model != null) {
                            if (model.getParentService().contains(parentService)) {
                                itemList.add(model);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
