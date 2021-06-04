package com.appsinventiv.mrapplianceadmin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.appsinventiv.mrapplianceadmin.Activities.ChatManagement.ListOfChats;
import com.appsinventiv.mrapplianceadmin.Activities.Coupons.ListOfCoupons;
import com.appsinventiv.mrapplianceadmin.Activities.CustomOrder.CustomOrderActivity;
import com.appsinventiv.mrapplianceadmin.Activities.Customers.ListOfCustomers;
import com.appsinventiv.mrapplianceadmin.Activities.Orders.Orders;
import com.appsinventiv.mrapplianceadmin.Activities.Salaries.SalariesList;
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

import java.util.ArrayList;

public class AccountsScreen extends AppCompatActivity {

    Button expenses, salaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Accounts");
        salaries = findViewById(R.id.salaries);
        expenses = findViewById(R.id.expenses);
        expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountsScreen.this, ExpensesList.class));
            }
        });
        salaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountsScreen.this, SalariesList.class));
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

}
