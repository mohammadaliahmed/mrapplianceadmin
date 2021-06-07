package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.ChangePolicy;
import com.appsinventiv.mrapplianceadmin.Models.AdminModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryItemModel;
import com.appsinventiv.mrapplianceadmin.Models.SalaryModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Servicemen.ServicemanModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class AddSalary extends AppCompatActivity {
    DatabaseReference mDatabase;

    Spinner spinner;
    RecyclerView recyclerAllowances, recyclerDeductions;
    private ArrayList<ServicemanModel> staffList = new ArrayList<>();
    private ServicemanModel staffChosen;
    TextView pickDate;
    ArrayList<SalaryItemModel> allowancesList = new ArrayList<>();
    ArrayList<SalaryItemModel> deductionList = new ArrayList<>();
    private int mDay, mMonth, mYear;
    AddSalaryItemsAdapter allowancesAdapter, deductionAdapter;
    Button addAllowance, addDeductions;
    int totalSalary = 0;
    EditText grossSalary;
    TextView total, totalDeductionsTV, totalAllowancesTV;
    Button saveSalary;
    boolean datePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Add Salary");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addDeductions = findViewById(R.id.addDeductions);
        saveSalary = findViewById(R.id.saveSalary);
        total = findViewById(R.id.total);
        totalDeductionsTV = findViewById(R.id.totalDeductionsTV);
        totalAllowancesTV = findViewById(R.id.totalAllowancesTV);
        grossSalary = findViewById(R.id.grossSalary);
        addAllowance = findViewById(R.id.addAllowance);
        recyclerAllowances = findViewById(R.id.recyclerAllowances);
        recyclerDeductions = findViewById(R.id.recyclerDeductions);
        recyclerAllowances.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDeductions.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        allowancesAdapter = new AddSalaryItemsAdapter(this, allowancesList, new AddSalaryItemsAdapter.AddSalaryItemsAdapterCallback() {
            @Override
            public void onDelete(SalaryItemModel model, int position) {
                allowancesList.remove(position);
                ArrayList<SalaryItemModel> list = new ArrayList<>(allowancesList);
                allowancesList.clear();
                allowancesList.addAll(list);
                allowancesAdapter.setItemList(allowancesList);
            }


        });
        deductionAdapter = new AddSalaryItemsAdapter(this, deductionList, new AddSalaryItemsAdapter.AddSalaryItemsAdapterCallback() {
            @Override
            public void onDelete(SalaryItemModel model, int position) {
                deductionList.remove(position);
                ArrayList<SalaryItemModel> list = new ArrayList<>(deductionList);
                deductionList.clear();
                deductionList.addAll(list);
                deductionAdapter.setItemList(deductionList);
            }


        });
        recyclerAllowances.setAdapter(allowancesAdapter);
        recyclerDeductions.setAdapter(deductionAdapter);

        pickDate = findViewById(R.id.pickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                final String[] dateSelected = {""};
                pickDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddSalary.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        dateSelected[0] = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                        mYear = year;
                                        mMonth = monthOfYear + 1;
                                        mDay = dayOfMonth;
                                        pickDate.setText("Date selected: " + dateSelected[0]);
                                        datePicked = true;
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }

                });
            }
        });
        addAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllowanceAlert();
            }
        });
        addDeductions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeductionAlert();
            }
        });
        grossSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    calculateTotal();
                }
            }
        });
        getStaffFromServer();
        saveSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staffChosen.getName()==null){
                    CommonUtils.showToast("Please select staff member");
                }else
                if (grossSalary.getText().length() < 2) {
                    CommonUtils.showToast("Enter gross salary");
                } else if (!datePicked) {
                    CommonUtils.showToast("Please select date");
                } else {
                    calculateTotal();
                    showAlert();
                }
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to add salary? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String key = mDatabase.push().getKey();
                SalaryModel model = new SalaryModel(key,
                        Integer.parseInt(grossSalary.getText().toString()),
                        allowancesList,
                        deductionList,
                        totalSalary,
                        mDay, mMonth, mYear, staffChosen,"Pending"
                );
                mDatabase.child("Salaries").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Salary Added");
                        dialogInterface.dismiss();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeductionAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_add_allowance, null);

        dialog.setContentView(layout);

        EditText title = layout.findViewById(R.id.title);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        EditText amount = layout.findViewById(R.id.amount);
        Button add = layout.findViewById(R.id.add);
        alertTitle.setText("Add Deduction");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().length() == 0) {
                    title.setError("Enter title");
                } else if (amount.getText().length() == 0) {
                    amount.setError("Enter amount");
                } else {
                    SalaryItemModel model = new SalaryItemModel(title.getText().toString(), Integer.parseInt(amount.getText().toString()));
                    deductionList.add(model);
                    deductionAdapter.setItemList(deductionList);
                    dialog.cancel();
                    calculateTotal();
                }
            }
        });
        dialog.show();
    }

    private void calculateTotal() {
        if (grossSalary.getText().length() > 0) {
            int totalAllowances = 0;
            int totalDeductions = 0;
            for (SalaryItemModel model : allowancesList) {
                totalAllowances = totalAllowances + model.getAmount();
            }

            for (SalaryItemModel model : deductionList) {
                totalDeductions = totalDeductions + model.getAmount();
            }
            totalSalary = (Integer.parseInt(grossSalary.getText().toString()) + totalAllowances) - totalDeductions;
            total.setText("Total Salary: AED " + totalSalary);
            totalAllowancesTV.setText("Total Allowances: AED " + totalAllowances);
            totalDeductionsTV.setText("Total Deductions: AED " + totalDeductions);
        }
    }

    private void showAllowanceAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_add_allowance, null);

        dialog.setContentView(layout);

        EditText title = layout.findViewById(R.id.title);
        TextView alertTitle = layout.findViewById(R.id.alertTitle);
        EditText amount = layout.findViewById(R.id.amount);
        Button add = layout.findViewById(R.id.add);
        alertTitle.setText("Add Allowances");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().length() == 0) {
                    title.setError("Enter title");
                } else if (amount.getText().length() == 0) {
                    amount.setError("Enter amount");
                } else {
                    SalaryItemModel model = new SalaryItemModel(title.getText().toString(), Integer.parseInt(amount.getText().toString()));
                    allowancesList.add(model);
                    allowancesAdapter.setItemList(allowancesList);
                    dialog.cancel();
                    calculateTotal();
                }
            }
        });
        dialog.show();
    }

    private void getStaffFromServer() {
        mDatabase.child("Servicemen").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ServicemanModel model = snapshot.getValue(ServicemanModel.class);
                        if (model != null) {
                            staffList.add(model);
                        }
                    }
                }
                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupSpinner() {
        final ArrayList<String> items = new ArrayList<>();
        items.add("Choose staff member");

        for (ServicemanModel model : staffList) {
            items.add(model.getName());
        }
        spinner = findViewById(R.id.spinner);
        final ArrayAdapter<String> adaptera = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adaptera);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 1) {
                    staffChosen = staffList.get(i - 1);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
