package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Adapters.PreviewInvoiceAdapter;
import com.appsinventiv.mrapplianceadmin.Adapters.SalariesListAdapter;
import com.appsinventiv.mrapplianceadmin.Models.CustomInvoiceModel;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceItemModel;
import com.appsinventiv.mrapplianceadmin.Models.PaymentModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryItemModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewSalary extends AppCompatActivity {

    DatabaseReference mDatabase;
    private String salaryId;
    private SalaryModel salaryModel;
    TextView name, phone, role, grossSalary, totalAllowancesTV, totalDeductionsTv, totalSalary;
    RecyclerView recyclerAllowances, recyclerDeductions;

    SalaryItemsAdapter allowancesAdapter, deductionAdapter;
    private ArrayList<SalaryItemModel> allowancesList = new ArrayList<>();
    private ArrayList<SalaryItemModel> deductionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        salaryId = getIntent().getStringExtra("salaryId");
        this.setTitle("View Salary");
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        role = findViewById(R.id.role);
        grossSalary = findViewById(R.id.grossSalary);
        totalAllowancesTV = findViewById(R.id.totalAllowancesTV);
        totalDeductionsTv = findViewById(R.id.totalDeductionsTv);
        totalSalary = findViewById(R.id.totalSalary);
        recyclerAllowances = findViewById(R.id.recyclerAllowances);
        recyclerDeductions = findViewById(R.id.recyclerDeductions);


        recyclerAllowances.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDeductions.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        allowancesAdapter = new SalaryItemsAdapter(this, allowancesList);
        deductionAdapter = new SalaryItemsAdapter(this, deductionList);

        recyclerAllowances.setAdapter(allowancesAdapter);
        recyclerDeductions.setAdapter(deductionAdapter);

        getDataFromServer();
    }

    private void getDataFromServer() {
        mDatabase.child("Salaries").child(salaryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    salaryModel = snapshot.getValue(SalaryModel.class);
                    if (salaryModel != null) {
                        setupData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupData() {
        name.setText(salaryModel.getServiceman().getName());
        phone.setText(salaryModel.getServiceman().getMobile());
        role.setText(salaryModel.getServiceman().getRole());
        grossSalary.setText("AED " + salaryModel.getGrossSalary());

        allowancesList = salaryModel.getAllowances();
        deductionList = salaryModel.getDeductions();

        allowancesAdapter.setItemList(allowancesList);
        deductionAdapter.setItemList(deductionList);

        int totalAllowances = 0;
        int totalDeductions = 0;
        for (SalaryItemModel model : allowancesList) {
            totalAllowances = totalAllowances + model.getAmount();
        }

        for (SalaryItemModel model : deductionList) {
            totalDeductions = totalDeductions + model.getAmount();
        }
        totalSalary.setText("Total Salary: AED " + salaryModel.getTotal());
        totalAllowancesTV.setText("AED " + totalAllowances);
        totalDeductionsTv.setText("AED " + totalDeductions);

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
