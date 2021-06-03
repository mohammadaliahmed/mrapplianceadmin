package com.appsinventiv.mrapplianceadmin.Activities;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.view.View;

import com.appsinventiv.mrapplianceadmin.Activities.ChatManagement.ListOfChats;
import com.appsinventiv.mrapplianceadmin.Activities.Coupons.ListOfCoupons;
import com.appsinventiv.mrapplianceadmin.Activities.CustomOrder.CustomOrderActivity;
import com.appsinventiv.mrapplianceadmin.Activities.Customers.ListOfCustomers;
import com.appsinventiv.mrapplianceadmin.Activities.Orders.Orders;
import com.appsinventiv.mrapplianceadmin.Activities.TimeSlotManagement.TimeSlotList;
import com.appsinventiv.mrapplianceadmin.Notifications.SendNotification;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Servicemen.ListOfServicemen;
import com.appsinventiv.mrapplianceadmin.Services.ListOfServices;
import com.appsinventiv.mrapplianceadmin.Services.ServiceModel;
import com.appsinventiv.mrapplianceadmin.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CardView services, customers, bills, orders, notifications, settings, serviceMen, coupons, timeslots, customOrder, chats;
    DatabaseReference mDatabase;
    private ArrayList<String> itemList = new ArrayList();
    private ArrayList<ServiceModel> servicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        services = findViewById(R.id.services);
        customers = findViewById(R.id.customers);
        bills = findViewById(R.id.bills);
        orders = findViewById(R.id.orders);
        notifications = findViewById(R.id.notifications);
        settings = findViewById(R.id.settings);
        serviceMen = findViewById(R.id.serviceMen);
        chats = findViewById(R.id.chats);
        coupons = findViewById(R.id.coupons);
        timeslots = findViewById(R.id.timeslots);
        customOrder = findViewById(R.id.customOrder);


        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, ListOfChats.class);
                startActivity(i);
            }
        });
        customOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomOrderActivity.class));
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Admin").child("fcmKey").setValue(FirebaseInstanceId.getInstance().getToken());

        serviceMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListOfServicemen.class));

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Orders.class));
            }
        });


        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListOfServices.class));

            }
        });
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListOfBills.class));

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AppSettings.class));

            }
        });
        coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListOfCoupons.class));

            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendNotification.class));

            }
        });
        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListOfCustomers.class));

            }
        });
        timeslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TimeSlotList.class);

                startActivity(i);
//                showAlert();

            }
        });
        getServicesFromDB();
    }

    private void showAlert() {
        ArrayList<String> list = SharedPrefs.getServicesList();
        final String[] items = list.toArray(new String[list.size()]);
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Choose Service");
        build.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, TimeSlotList.class);
                i.putExtra("serviceName", items[which]);
                i.putExtra("serviceId", servicesList.get(which).getId());
                startActivity(i);

            }
        }).create().show();
    }


    private void getServicesFromDB() {
        mDatabase.child("Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ServiceModel model = snapshot.getValue(ServiceModel.class);
                        if (model != null) {
                            itemList.add(model.getName());
                            servicesList.add(model);
                        }
                    }
                    if (itemList.size() > 0) {
                        SharedPrefs.setServicesList(itemList);
                    }
                } else {
                    itemList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
