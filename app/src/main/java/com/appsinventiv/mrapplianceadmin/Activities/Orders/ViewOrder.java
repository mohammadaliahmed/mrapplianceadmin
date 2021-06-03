package com.appsinventiv.mrapplianceadmin.Activities.Orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Activities.OrderLogs.SolutionTracking;
import com.appsinventiv.mrapplianceadmin.Activities.ViewInvoice;
import com.appsinventiv.mrapplianceadmin.Activities.ViewQuotation;
import com.appsinventiv.mrapplianceadmin.Activities.ViewServicePictures;
import com.appsinventiv.mrapplianceadmin.Adapters.PicturesAdapter;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceModel;
import com.appsinventiv.mrapplianceadmin.Models.LogsModel;
import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.Constants;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationAsync;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrder extends AppCompatActivity implements NotificationObserver {
    TextView orderId, orderTime, serviceName, price, username, phone, address, city, day, timeChosen;
    String orderIdFromIntent;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OrderedServicesAdapter adapter;
    ArrayList<ServiceCountModel> list = new ArrayList<>();
    Button orderCompleted, underProcess;
    OrderModel model;
    long billNumber = 1;

    String s_orderId, s_orderTime, s_quantity, s_price, s_username, s_phone, s_address, s_city;
    String userFcmKey;
    ImageView invoice;
    Spinner spinner;
    CardView assignToLayout, modifyLayout;

    Button assignTo;
    private String serviceMenSelected;
    ArrayList<ServicemanModel> servicemenList = new ArrayList<ServicemanModel>();
    int positionSelected;
    TextView assignedTo;
    CardView assignedLayout;
    Button viewPictures, modifyOrder;

    Button reAssign;
    TextView buildingType;
    private boolean reassignJob;

    TextView instructions;
    Button orderLogs, quotation;
    RecyclerView clientPictures;
    PicturesAdapter picturesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Service details");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        orderIdFromIntent = intent.getStringExtra("orderId");

        onNewIntent(getIntent());


        quotation = findViewById(R.id.quotation);
        clientPictures = findViewById(R.id.clientPictures);
        orderLogs = findViewById(R.id.orderLogs);
        reAssign = findViewById(R.id.reAssign);
        instructions = findViewById(R.id.instructions);
        orderId = findViewById(R.id.order_id);
        buildingType = findViewById(R.id.buildingType);
        assignedLayout = findViewById(R.id.assignedLayout);
        assignedTo = findViewById(R.id.assignedTo);
        assignToLayout = findViewById(R.id.assignToLayout);
        spinner = findViewById(R.id.spinner);
        modifyLayout = findViewById(R.id.modifyLayout);
        orderTime = findViewById(R.id.order_time);
        serviceName = findViewById(R.id.serviceName);
        price = findViewById(R.id.order_price);
        invoice = findViewById(R.id.invoice);
        assignTo = findViewById(R.id.assignTo);
        viewPictures = findViewById(R.id.viewPictures);
        modifyOrder = findViewById(R.id.modifyOrder);

        username = findViewById(R.id.ship_username);
        phone = findViewById(R.id.ship_phone);
        address = findViewById(R.id.ship_address);
        city = findViewById(R.id.ship_city);
        day = findViewById(R.id.day);
        timeChosen = findViewById(R.id.timeChosen);
        clientPictures.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        orderLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewOrder.this, SolutionTracking.class);
                i.putExtra("orderId", "" + orderIdFromIntent);
                startActivity(i);
            }
        });
        quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewOrder.this, ViewQuotation.class);
                i.putExtra("orderId", "" + orderIdFromIntent);
                startActivity(i);
            }
        });


        orderCompleted = findViewById(R.id.completed);
        underProcess = findViewById(R.id.underProcess);

        getInvoiceCountFromDB();
        getServicemenFromDB();


        modifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewOrder.this, ModifyOrder.class);
                i.putExtra("orderId", "" + orderIdFromIntent);
                i.putExtra("parentService", model.getServiceName());
                startActivity(i);
            }
        });

        viewPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewOrder.this, ViewServicePictures.class);
                i.putExtra("orderId", "" + orderIdFromIntent);
                startActivity(i);
            }
        });
        reAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isAssigned()) {
                    reassignJob = true;
                }
                assignedLayout.setVisibility(View.GONE);
                assignToLayout.setVisibility(View.VISIBLE);
            }
        });


        assignTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceMenSelected.equalsIgnoreCase("select serviceman")) {
                    CommonUtils.showToast("Please select serviceman");
                } else {
                    if (reassignJob) {
                        mDatabase.child("Servicemen").child(model.getAssignedTo()).child("assignedOrders").child(orderIdFromIntent).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                assignJob();
                            }
                        });
                    } else {
                        assignJob();
                    }


                }
            }
        });


        orderCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsComplete();
            }
        });


        underProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsShipped();
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getBillNumber() != 0) {
                    Intent i = new Intent(ViewOrder.this, ViewInvoice.class);
                    i.putExtra("invoiceId", "" + model.getBillNumber());
                    startActivity(i);
                } else {
                    createInvoice();
                }
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        getDataFromServer(orderIdFromIntent);
    }

    public void assignJob() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("assigned", true);
        map.put("assignedTo", servicemenList.get(positionSelected - 1).getId());
        map.put("assignedToName", servicemenList.get(positionSelected - 1).getName());
        mDatabase.child("Orders").child(orderIdFromIntent).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order assigned to: " + serviceMenSelected);
                        CommonUtils.sendMessage(servicemenList.get(positionSelected - 1).getMobile(),
                                "Mr Appliance \nNew order Assigned\nOrder Id: " + orderIdFromIntent + "\n\nClick to view: \n" + Constants.MrAppliance_URL + "staff/" + orderIdFromIntent);
                        mDatabase.child("Servicemen").child(servicemenList.get(positionSelected - 1).getId())
                                .child("assignedOrders").child(orderIdFromIntent)
                                .setValue(orderIdFromIntent);
                        NotificationAsync notificationAsync = new NotificationAsync(ViewOrder.this);
                        String notification_title = "New service assigned";
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", servicemenList.get(positionSelected - 1).getFcmKey(), notification_title, notification_message, "Order", "" + orderId);
                        NotificationAsync notificationAsync1 = new NotificationAsync(ViewOrder.this);
                        notificationAsync1.execute("ali", model.getUser().getFcmKey(), "Your service order has been assigned to " + servicemenList.get(positionSelected - 1).getName()
                                , notification_message, "Order", "" + orderId);
                        String key = mDatabase.push().getKey();
                        mDatabase.child("OrderLogs").child(model.getUser().getUsername()).child("" + model.getOrderId()).child(key).setValue(new LogsModel(
                                key, "Order is Assigned", System.currentTimeMillis()
                        ));

                    }

                });
    }

    private void getDataFromServer(String orderIdFromIntent) {
        mDatabase.child("Orders").child(orderIdFromIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    model = dataSnapshot.getValue(OrderModel.class);
                    if (model != null) {
                        orderId.setText("" + model.getOrderId());
                        orderTime.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                        serviceName.setText("" + model.getServiceName());
                        price.setText("AED " + model.getTotalPrice());
                        username.setText("" + model.getUser().getFullName());
                        buildingType.setText("" + model.getBuildingType());
                        phone.setText("" + model.getUser().getMobile());
                        instructions.setText("Instructions: " + model.getInstructions());
                        address.setText(model.getOrderAddress());
//                        city.setText(model.getUser().getAddress());
                        day.setText("Day: " + model.getDate());
                        timeChosen.setText("Time : " + model.getChosenTime());
                        list = model.getCountModelArrayList();
                        adapter = new OrderedServicesAdapter(ViewOrder.this, list);
                        recyclerView.setAdapter(adapter);
                        if (model.getOrderImages() != null && model.getOrderImages().size() > 0) {
                            picturesAdapter = new PicturesAdapter(ViewOrder.this, model.getOrderImages(), new PicturesAdapter.PicturesAdapterCallback() {
                                @Override
                                public void onDelete(int position) {

                                }
                            });
                            clientPictures.setAdapter(picturesAdapter);
                        }


                        if (model.getOrderStatus().equalsIgnoreCase("Pending")) {
                            orderCompleted.setVisibility(View.GONE);
                            underProcess.setVisibility(View.VISIBLE);
                            assignToLayout.setVisibility(View.VISIBLE);
                            invoice.setVisibility(View.GONE);

                        } else if (model.getOrderStatus().equalsIgnoreCase("under process") && !model.isAssigned()) {
                            orderCompleted.setVisibility(View.VISIBLE);
                            assignToLayout.setVisibility(View.VISIBLE);
                            invoice.setVisibility(View.VISIBLE);


                        } else if (model.getOrderStatus().equalsIgnoreCase("under process") && model.isAssigned()) {
                            invoice.setVisibility(View.VISIBLE);
                            orderCompleted.setVisibility(View.VISIBLE);
                            invoice.setVisibility(View.VISIBLE);

                            assignToLayout.setVisibility(View.GONE);
                            assignedLayout.setVisibility(View.VISIBLE);
                            assignedTo.setText("Assigned to: " + model.getAssignedToName());
                        } else if (model.getOrderStatus().equalsIgnoreCase("completed") && model.isAssigned()) {
                            assignedLayout.setVisibility(View.VISIBLE);
                            assignedTo.setText("Assigned to: " + model.getAssignedToName());
                            underProcess.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.GONE);
                            assignToLayout.setVisibility(View.GONE);
                            invoice.setVisibility(View.VISIBLE);


                        } else {
                            assignedLayout.setVisibility(View.GONE);
                            underProcess.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.GONE);
                            assignToLayout.setVisibility(View.GONE);
                            invoice.setVisibility(View.VISIBLE);

                        }
                        if (model.isJobFinish() || model.isJobDone()) {
                            modifyLayout.setVisibility(View.GONE);
                            invoice.setVisibility(View.VISIBLE);
                            reAssign.setVisibility(View.GONE);
                            assignedLayout.setVisibility(View.GONE);
                        }

//                        Toast.makeText(ViewOrder.this, ""+list, Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();

                        s_orderId = "" + model.getOrderId();
                        s_quantity = "" + model.getCountModelArrayList().size();
                        s_price = "" + model.getTotalPrice();
                        s_username = model.getUser().getFullName();

                        userFcmKey = model.getUser().getFcmKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getServicemenFromDB() {
        mDatabase.child("Servicemen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ServicemanModel model = snapshot.getValue(ServicemanModel.class);
                        if (model != null) {
                            servicemenList.add(model);
                        }
                    }
                    setupSpinner();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setupSpinner() {

        final String[] items = new String[servicemenList.size() + 1];
        items[0] = "Select Serviceman";
        for (int i = 0; i < servicemenList.size(); i++) {
            items[i + 1] = servicemenList.get(i).getName() + " - " + servicemenList.get(i).getRole();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serviceMenSelected = items[i];
                positionSelected = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getInvoiceCountFromDB() {
        mDatabase.child("Invoices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    billNumber = dataSnapshot.getChildrenCount() + 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void createInvoice() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrder.this);
//        builder.setTitle("Alert");
//        builder.setMessage("Create Bill?");
//
//        // add the buttons
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mDatabase.child("Invoices").child("" + billNumber)
//                        .setValue(new InvoiceModel("" + billNumber, model,
//                                System.currentTimeMillis())).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        String key = mDatabase.push().getKey();
//                        mDatabase.child("OrderLogs").child(model.getUser().getUsername()).child("" + model.getOrderId()).child(key).setValue(new LogsModel(
//                                key, "Bill Generated", System.currentTimeMillis()
//                        ));
//                        updateInvoiceInOrder();
//                        Intent i = new Intent(ViewOrder.this, ViewInvoice.class);
//                        i.putExtra("invoiceId", "" + billNumber);
//                        startActivity(i);
//                    }
//                });
//
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//
//        // create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();

    }

    private void updateInvoiceInOrder() {
        mDatabase.child("Orders").child(orderIdFromIntent).child("billNumber").setValue(billNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    private void markOrderAsComplete() {
        showAlertDialogButtonClicked("Completed");
    }

    private void markOrderAsShipped() {
        showAlertDialogButtonClicked("Under Process");
    }

    public void showAlertDialogButtonClicked(final String message) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrder.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to mark this order as " + message + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order marked as " + message);
//                        invoice.setVisibility(View.GONE);
                        NotificationAsync notificationAsync = new NotificationAsync(ViewOrder.this);
                        String notification_title = "You service is " + message;
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", userFcmKey, notification_title, notification_message, "Order", "" + orderIdFromIntent);
//                        Intent i = new Intent(ViewOrder.this, Orders.class);
//                        startActivity(i);
//                        finish();
                        String key = mDatabase.push().getKey();
                        mDatabase.child("OrderLogs").child(model.getUser().getUsername()).child("" + model.getOrderId()).child(key).setValue(new LogsModel(
                                key, "Order is " + message, System.currentTimeMillis()
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

    @Override
    public void onSuccess(String chatId) {
        CommonUtils.showToast("Notification sent to user");
    }

    @Override
    public void onFailure() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String adIdFromLink = data.substring(data.lastIndexOf("/") + 1);
            orderIdFromIntent = adIdFromLink;
//            getOrderFromServer(orderId);
        }
    }
}
