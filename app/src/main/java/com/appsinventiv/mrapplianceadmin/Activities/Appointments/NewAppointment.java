package com.appsinventiv.mrapplianceadmin.Activities.Appointments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsinventiv.mrapplianceadmin.Activities.ViewInvoice;
import com.appsinventiv.mrapplianceadmin.Models.User;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;


public class NewAppointment extends AppCompatActivity {

    TextView location, title, brand, model, status, report, payment, paymentType, customerName, phone, address;
    DatabaseReference mDatabase;
    public String id;
    private AppointmentModel appointmentModel;
    TextView pickDate, pickTime;
    private String pickedDate;
    private String timeSelected;
    Button addWorkHistory;
    TextView currentStatus;
    Button viewInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Appointment Detials");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        id = getIntent().getStringExtra("appointmentId");

        location = findViewById(R.id.location);
        brand = findViewById(R.id.brand);

        model = findViewById(R.id.model);
        viewInvoice = findViewById(R.id.viewInvoice);
        address = findViewById(R.id.address);
        title = findViewById(R.id.title);
        status = findViewById(R.id.status);
        pickDate = findViewById(R.id.pickDate);
        pickTime = findViewById(R.id.pickTime);
        addWorkHistory = findViewById(R.id.addWorkHistory);
        currentStatus = findViewById(R.id.currentStatus);
        report = findViewById(R.id.report);
        payment = findViewById(R.id.payment);
        paymentType = findViewById(R.id.paymentType);
        customerName = findViewById(R.id.customerName);
        phone = findViewById(R.id.phone);

        pickedDate = getIntent().getStringExtra("pickedDate");
        timeSelected = getIntent().getStringExtra("timeSelected");
        if (pickedDate != null) {
            pickTime.setText("Time: " + timeSelected);
            pickDate.setText("Date: " + pickedDate);
        }

        addWorkHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewAppointment.this, WorkHistory.class);
                i.putExtra("appointmentId", id);
                startActivity(i);
            }
        });


        viewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appointmentModel.getInvoiceId() != null) {
                    Intent i = new Intent(NewAppointment.this, ViewInvoice.class);
                    i.putExtra("invoiceId", appointmentModel.getInvoiceId());
                    startActivity(i);
                } else {
                    CommonUtils.showToast("No Invoice linked");
                }
            }
        });

        getDataFromServer();


    }


    private void getDataFromServer() {
        mDatabase.child("Appointments").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    appointmentModel = dataSnapshot.getValue(AppointmentModel.class);
                    location.setText(appointmentModel.getLocation());
                    brand.setText(appointmentModel.getBrand());
                    model.setText(appointmentModel.getModel());
                    pickDate.setText("Date: " + appointmentModel.getDate());
                    pickTime.setText("Time: " + appointmentModel.getTimeSelected());
                    status.setText(appointmentModel.getStatus());
                    report.setText(appointmentModel.getReport());
                    payment.setText(appointmentModel.getPayment());
                    paymentType.setText(appointmentModel.getPaymentType());
                    title.setText(appointmentModel.getTitle());
                    address.setText(appointmentModel.getAddress());
                    phone.setText(appointmentModel.getPhone());
                    currentStatus.setText("Current Appointment Status: " + appointmentModel.getAppointmentStatus());

                    customerName.setText(appointmentModel.getCustomerName());


                    timeSelected = appointmentModel.getTimeSelected();
                    pickedDate = appointmentModel.getDate();
                    addWorkHistory.setVisibility(View.VISIBLE);
                    if (appointmentModel.getInvoiceId() != null) {
                        viewInvoice.setVisibility(View.VISIBLE);
                        viewInvoice.setText("View Invoice #: " + appointmentModel.getInvoiceId());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
