package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsinventiv.mrapplianceadmin.Activities.ExpensesList;
import com.appsinventiv.mrapplianceadmin.Adapters.SalariesListAdapter;
import com.appsinventiv.mrapplianceadmin.Models.PaymentModel;
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

public class SalariesList extends AppCompatActivity {

    String mMonths[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String mYears[] = {"2020", "2021", "2022", "2023", "2024", "2025", "2026"};

    NumberPicker monthPicker, yearPicker;
    private String monthPicked = "1";
    private String yearPicked = "2020";
    ImageView addSalary;
    DatabaseReference mDatabase;
    private ArrayList<ServicemanModel> staffList = new ArrayList<>();
    private ServicemanModel staffChosen;
    SalariesListAdapter adapter;
    RecyclerView recycler;
    private ArrayList<SalaryModel> originalList = new ArrayList<>();
    private ArrayList<SalaryModel> itemList = new ArrayList<>();
    Button clear;

    Button goBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salaries);
        monthPicker = findViewById(R.id.monthPicker);
        addSalary = findViewById(R.id.addSalary);
        recycler = findViewById(R.id.recycler);
        clear = findViewById(R.id.clear);
        goBtn = findViewById(R.id.goBtn);
        yearPicker = findViewById(R.id.yearPicker);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Salaries");
        setNubmerPicker(monthPicker, mMonths);
        setNubmerPicker(yearPicker, mYears);
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                monthPicked = mMonths[newVal];
            }
        });
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                yearPicked = mYears[newVal];
            }
        });
        addSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSalaryDilog();
                startActivity(new Intent(SalariesList.this,AddSalary.class));
            }
        });
        adapter = new SalariesListAdapter(this, itemList, new SalariesListAdapter.SalaryItemAdapterCallback() {
            @Override
            public void onDelete(SalaryModel model) {
                showDeleteAlert(model);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler.setAdapter(adapter);
        getStaffFromServer();
        getDataFromServer();
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                for (SalaryModel model : originalList) {
                    if (model.getYear() == Integer.parseInt(yearPicked) && model.getMonth() == Integer.parseInt(monthPicked)) {
                        itemList.add(model);
                    }

                }
                adapter.setItemList(itemList);
                if (itemList.size() == 0) {
                    CommonUtils.showToast("No data");
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.clear();
                itemList.addAll(originalList);
                adapter.setItemList(itemList);
            }
        });

    }

    private void showDeleteAlert(SalaryModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this record? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Salaries").child(model.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Removed");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getDataFromServer() {
        mDatabase.child("Salaries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SalaryModel model = snapshot.getValue(SalaryModel.class);
                        if (model != null) {
                            itemList.add(model);
                            originalList.add(model);
                        }
                    }
                    adapter.setItemList(itemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSalaryDilog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_salary, null);

        dialog.setContentView(layout);

        Spinner spinner = layout.findViewById(R.id.spinner);
        EditText amount = layout.findViewById(R.id.amount);
        EditText deduction = layout.findViewById(R.id.deduction);
        Button saveSalary = layout.findViewById(R.id.saveSalary);
        TextView pickDate = layout.findViewById(R.id.pickDate);
        TextView total = layout.findViewById(R.id.total);
        deduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && amount.getText().length() > 0) {
                    total.setText("Total: " + (Integer.parseInt(amount.getText().toString()) - Integer.parseInt(deduction.getText().toString())));

                }
            }
        });
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && deduction.getText().length() > 0) {
                    total.setText("Total: " + (Integer.parseInt(amount.getText().toString()) - Integer.parseInt(deduction.getText().toString())));

                }
            }
        });

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        final String[] dateSelected = {""};
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SalariesList.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateSelected[0] = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                pickDate.setText(dateSelected[0]);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        saveSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateSelected[0].equalsIgnoreCase("")) {
                    CommonUtils.showToast("Please select Date");
                } else if (amount.getText().length() == 0) {
                    amount.setError("Enter gross salary");
                } else if (deduction.getText().length() == 0) {
                    deduction.setError("Enter deduction");
                } else {
                    String key = mDatabase.push().getKey();
                    int total = Integer.parseInt(amount.getText().toString()) - Integer.parseInt(deduction.getText().toString());
//                    SalaryModel model = new SalaryModel(key,
//                            Integer.parseInt(amount.getText().toString()),
//                            Integer.parseInt(deduction.getText().toString()),
//                            total,
//                            mDay, mMonth + 1, mYear, staffChosen
//                    );
//                    mDatabase.child("Salaries").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            CommonUtils.showToast("Salary Added");
//                            dialog.cancel();
//                        }
//                    });
                }
            }
        });
        final ArrayList<String> items = new ArrayList<>();
        items.add("Choose staff member");

        for (ServicemanModel model : staffList) {
            items.add(model.getName());
        }


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


        dialog.show();

    }

    private void setNubmerPicker(NumberPicker nubmerPicker, String[] numbers) {
        nubmerPicker.setMaxValue(numbers.length - 1);
        nubmerPicker.setMinValue(0);
        nubmerPicker.setWrapSelectorWheel(true);
        nubmerPicker.setDisplayedValues(numbers);
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
