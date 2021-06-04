package com.appsinventiv.mrapplianceadmin.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Adapters.ExpensesListAdapter;
import com.appsinventiv.mrapplianceadmin.ChangePolicy;
import com.appsinventiv.mrapplianceadmin.Models.AdminModel;
import com.appsinventiv.mrapplianceadmin.Models.ExpensesModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpensesList extends AppCompatActivity {
    RecyclerView recycler;
    ImageView addExpense;
    DatabaseReference mDatabase;
    ExpensesListAdapter adapter;

    private ArrayList<ExpensesModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Expenses");
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ExpensesListAdapter(this, itemList, new ExpensesListAdapter.ExpensesItemAdapterCallback() {
            @Override
            public void onDelete(ExpensesModel model) {
                showDeleteAlert(model);
            }

            @Override
            public void onApprove(ExpensesModel model) {
                showApproveAlert(model);
            }

            @Override
            public void onReject(ExpensesModel model) {
                showRejectAlert(model);
            }
        });
        recycler.setAdapter(adapter);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        getDataFromServer();
    }

    private void getDataFromServer() {
        mDatabase.child("Expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ExpensesModel model = snapshot.getValue(ExpensesModel.class);
                        if (model != null) {
                            itemList.add(model);

                        }
                    }
                    adapter.setItemList(itemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDeleteAlert(final ExpensesModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this expense? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Expenses").child(model.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Deleted");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showApproveAlert(final ExpensesModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to approve this expense? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Expenses").child(model.getId()).child("status").setValue("Approved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Approved");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showRejectAlert(final ExpensesModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this expense? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Expenses").child(model.getId()).child("status").setValue("Rejected").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Rejected");
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
