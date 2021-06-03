package com.appsinventiv.mrapplianceadmin.Activities.Reports;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportsActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    TextView revenue, orderCount, serviceCount, customerCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Reports");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        revenue = findViewById(R.id.revenue);
        orderCount = findViewById(R.id.orderCount);
        customerCount = findViewById(R.id.customerCount);
        serviceCount = findViewById(R.id.serviceCount);

        getOrdersDataFromDB();
        getCustomerDataFromDB();
        getServicesDataFromDB();

    }

    private void getCustomerDataFromDB() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    customerCount.setText("Customers: " + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getServicesDataFromDB() {
        mDatabase.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    serviceCount.setText("Services: " + dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getOrdersDataFromDB() {
        mDatabase.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
//                    ArrayList<OrderModel> models=new ArrayList<>();
                    long totalRevenue = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OrderModel model = snapshot.getValue(OrderModel.class);
                        if (model.getOrderStatus().equalsIgnoreCase("Completed")) {
                            totalRevenue = totalRevenue + model.getTotalPrice();
                        }
                    }
                    orderCount.setText("Orders: " + dataSnapshot.getChildrenCount());
                    revenue.setText("Revenue: " + totalRevenue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
