package com.appsinventiv.mrapplianceadmin.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.mrapplianceadmin.ChangePolicy;
import com.appsinventiv.mrapplianceadmin.Models.AdminModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppSettings extends AppCompatActivity {
    DatabaseReference mDatabase;

    EditText adminNumber, cities, tax, jobs, address;
    Button updateNumber, updateCities, updateTax, updateJobs, updateAddress;

    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Settings");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateNumber = findViewById(R.id.updateNumber);
        adminNumber = findViewById(R.id.adminNumber);
        tax = findViewById(R.id.tax);
        updateTax = findViewById(R.id.updateTax);
        address = findViewById(R.id.address);
        updateAddress = findViewById(R.id.updateAddress);
        change = findViewById(R.id.change);
        jobs = findViewById(R.id.jobs);
        updateJobs = findViewById(R.id.updateJobs);


        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText().length() == 0) {
                    address.setError("Enter address");
                } else {
                    mDatabase.child("Admin").child("address").setValue(address.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Address Added");
                        }
                    });
                }
            }
        });


        updateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminNumber.getText().length() == 0) {
                    adminNumber.setError("Enter number");
                } else {
                    mDatabase.child("Admin").child("adminNumber").setValue(adminNumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Number Added");
                        }
                    });
                }
            }
        });
        updateTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tax.getText().length() == 0) {
                    tax.setError("Enter tax");
                } else {
                    mDatabase.child("Admin").child("tax").setValue(Integer.parseInt(tax.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Tax Added");
                        }
                    });
                }
            }
        });
        cities = findViewById(R.id.cities);
        updateCities = findViewById(R.id.updateCities);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppSettings.this, ChangePolicy.class));
            }
        });

        updateCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("Admin").child("providingServiceInCities").setValue(cities.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                    }
                });

            }
        });
        updateJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("Admin").child("jobs").setValue(jobs.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                    }
                });

            }
        });

        getDataFromDB();

    }

    private void getDataFromDB() {
        mDatabase.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue() != null) {
                        AdminModel model = dataSnapshot.getValue(AdminModel.class);
                        if (model != null) {
                            adminNumber.setText(model.getAdminNumber());
                            jobs.setText(model.getJobs());
                            address.setText(model.getAddress());
                            cities.setText(model.getProvidingServiceInCities());
                            tax.setText("" + model.getTax());
                        }
                    }
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
