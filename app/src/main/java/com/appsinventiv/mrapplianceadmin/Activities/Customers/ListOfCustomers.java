package com.appsinventiv.mrapplianceadmin.Activities.Customers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.appsinventiv.mrapplianceadmin.Activities.MainActivity;
import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.User;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfCustomers extends AppCompatActivity {
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    ArrayList<User> itemList = new ArrayList<>();
    CustomersListAdapter adapter;
    HashMap<String, OrderModel> ordersMap = new HashMap<>();
    HashMap<String, User> userMap = new HashMap<>();
    private String csv;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_customers);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Customers");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        search = findViewById(R.id.search);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CustomersListAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
        getDataFromDB();
        getOrderDetailsFromDB();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });

    }

    private void getDataFromDB() {
        mDatabase.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User model = snapshot.getValue(User.class);
                        if (model != null) {
                            HashMap<String, String> map = new HashMap<>();
                            for (DataSnapshot snapshot1 : snapshot.child("Orders").getChildren()) {
                                map.put(snapshot1.getKey(), "" + snapshot1.getValue(Long.class));
                            }
                            model.setOrders(map);
                            itemList.add(model);
                            userMap.put(snapshot.getKey(), model);
                        }
                    }
                    Collections.sort(itemList, new Comparator<User>() {
                        @Override
                        public int compare(User listData, User t1) {
                            String ob1 = listData.getFullName();

                            String ob2 = t1.getFullName();
                            return ob1.compareTo(ob2);
                        }
                    });
                    adapter.updateList(itemList);
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

    private void getOrderDetailsFromDB() {
        mDatabase.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OrderModel orderModel = snapshot.getValue(OrderModel.class);
                        if (orderModel != null) {
                            ordersMap.put(snapshot.getKey(), orderModel);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void performCalculation(String userId) {
        User abc = userMap.get(userId);
        long total = 0;
        for (Map.Entry me : abc.getOrders().entrySet()) {
            if (ordersMap.get(me.getValue()) != null) {
                total = total + ordersMap.get(me.getValue()).getTotalPrice();
            }
        }
        abc.setTotalPayment(total);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(ListOfCustomers.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if (item.getItemId() == R.id.action_export) {
            exportData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportData() {
        int count = 0;
        for (Map.Entry me : userMap.entrySet()) {
            performCalculation(me.getKey().toString());
            count++;
            if (count == userMap.size()) {
                writeDataToFile();
            }
        }
    }

    private void writeDataToFile() {
        csv = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "users" + ".csv";
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Serial #", "Username", "Name", "Phone", "Joined", "Address", "Google Address", "Total services booked",
                    "Total Payment"});
            int serial = 0;
            for (User model : itemList) {
                serial++;
                data.add(new String[]{
                        "" + serial,
                        "" + model.getUsername(),
                        "" + model.getFullName(),
                        model.getMobile(),
                        CommonUtils.getFormattedDateOnly(model.getTime()),
                        model.getAddress(),
                        "" + model.getGoogleAddress(),
                        "" + model.getOrders().size(),
                        "" + model.getTotalPayment()
                });
            }
            writer.writeAll(data); // data is adding to csv
            writer.close();
            File applictionFile = new File(csv);
            if (applictionFile != null && applictionFile.exists()) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(applictionFile), getMimeType(applictionFile.getAbsolutePath()));
                startActivity(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.orders_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
