package com.appsinventiv.mrapplianceadmin.Activities.Customers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.User;
import com.appsinventiv.mrapplianceadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCustomer extends AppCompatActivity {
    DatabaseReference mDatabase;

    String userId, username;
    User user;
    TextView name, phone, address, orders, totalPayment;
    private int totalOrders;
    private ArrayList<OrderModel> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        orders = findViewById(R.id.orders);
        totalPayment = findViewById(R.id.totalPayment);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        this.setTitle(username);

        getDataFromDB();

    }

    private void getDataFromDB() {
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        name.setText(user.getFullName());
                        phone.setText(user.getMobile());
                        address.setText(user.getAddress());

                        HashMap<String, String> map = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.child("Orders").getChildren()) {
                            map.put(snapshot.getKey(), "" + snapshot.getValue(Long.class));
                        }
                        user.setOrders(map);
                        orders.setText(map.size() + "");
                        getOrdersFromDB(map);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getOrdersFromDB(HashMap<String, String> orders) {
        totalOrders = orders.size();
        for (Map.Entry me : orders.entrySet()) {
            getOrderDetailsFromDB(me.getKey().toString());
        }
    }

    private void getOrderDetailsFromDB(String key) {
        mDatabase.child("Orders").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);
                    if (orderModel != null) {
                        orderItems.add(orderModel);
                        if (orderItems.size() == totalOrders) {
                            calculate(orderItems);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void calculate(ArrayList<OrderModel> orderItems) {
        long total = 0;
        for (OrderModel model : orderItems) {
            total = total + model.getTotalPrice();
        }

        totalPayment.setText("AED" + total);
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
}
