package com.appsinventiv.mrapplianceadmin.Services;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListOfServices extends AppCompatActivity {
    ImageView addService;
    RecyclerView recyclerview;
    ServiceListAdapter adapter;
    private ArrayList<ServiceModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        addService = findViewById(R.id.addService);
        recyclerview = findViewById(R.id.recyclerview);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("List of services");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListOfServices.this, AddService.class));
            }

        });
        addService.setVisibility(View.GONE);


        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ServiceListAdapter(this, itemList, new ServiceListAdapter.ServiceListAdapterCallbacks() {
            @Override
            public void onServiceStatusChanged(ServiceModel model, final boolean value) {
                mDatabase.child("Services").child(model.getId()).child("active").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (value) {
                            CommonUtils.showToast("Service activated");
                        } else {
                            CommonUtils.showToast("Service Deactivated");
                        }
                    }
                });
            }

            @Override
            public void onServiceDeleted(ServiceModel model) {
                showAlert(model);
            }

            @Override
            public void onPositionChanged(ServiceModel model, int position) {
                mDatabase.child("Services").child(model.getId()).child("position").setValue(position).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Position updated");
                    }
                });
            }

        });

        recyclerview.setAdapter(adapter);

        getDataFromDB();

    }

    private void getDataFromDB() {
        mDatabase.child("Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ServiceModel model = snapshot.getValue(ServiceModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    Collections.sort(itemList, new Comparator<ServiceModel>() {
                        @Override
                        public int compare(ServiceModel listData, ServiceModel t1) {
                            Integer ob1 = listData.getPosition();
                            Integer ob2 = t1.getPosition();

                            return ob1.compareTo(ob2);

                        }
                    });

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

    private void showAlert(final ServiceModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this service? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Services").child(model.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Services Deleted");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.service_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(ListOfServices.this, AddService.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
