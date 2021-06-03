package com.appsinventiv.mrapplianceadmin.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Adapters.InvoiceListAdapter;
import com.appsinventiv.mrapplianceadmin.Adapters.PreviewInvoiceAdapter;
import com.appsinventiv.mrapplianceadmin.Models.CustomInvoiceModel;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceItemModel;
import com.appsinventiv.mrapplianceadmin.Models.InvoiceModel;
import com.appsinventiv.mrapplianceadmin.Models.PaymentModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewInvoice extends AppCompatActivity {
    RecyclerView recycler;
    private View rootView;
    PreviewInvoiceAdapter adapter;
    private ArrayList<InvoiceItemModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    private CustomInvoiceModel customInvoiceModel;
    TextView total;
    TextView billTo, invoiceIdTv;
    String invoiceId;
    TextView paid, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        invoiceId = getIntent().getStringExtra("invoiceId");
        this.setTitle("Bill Number: " + invoiceId);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        recycler = findViewById(R.id.recycler);
        invoiceIdTv = findViewById(R.id.invoiceId);
        billTo = findViewById(R.id.billTo);
        balance = findViewById(R.id.balance);
        paid = findViewById(R.id.paid);
        total = findViewById(R.id.total);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new PreviewInvoiceAdapter(this, itemList);
        recycler.setAdapter(adapter);

        getDataFromServer();
    }

    private void getDataFromServer() {
        mDatabase.child("Invoices").child(invoiceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    customInvoiceModel = dataSnapshot.getValue(CustomInvoiceModel.class);
                    if (customInvoiceModel != null) {
                        itemList = new ArrayList<>(customInvoiceModel.getInvoiceItems().values());
                        adapter.setItemList(itemList);
                        invoiceIdTv.setText("Invoice: " + customInvoiceModel.getInvoiceId() + "\nDate: " + CommonUtils.getDateOnly(customInvoiceModel.getTime()));
                        total.setText("AED " + customInvoiceModel.getTotal());
                        billTo.setText("To: \n " + customInvoiceModel.getUser().getFirstname() + " " + customInvoiceModel.getUser().getLastname() + "\n" +
                                customInvoiceModel.getUser().getAddress() + "\n" + customInvoiceModel.getUser().getPhone() + "\n" + customInvoiceModel.getUser().getEmail());
                        calculateTotal();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateTotal() {
        int totall = 0;
        for (InvoiceItemModel model : itemList) {
            totall = totall + model.getQuantity() * model.getPrice();
        }
        total.setText("AED " + totall);

        int totalPaid = 0;
        if (customInvoiceModel != null && customInvoiceModel.getPayments() != null) {

            ArrayList<PaymentModel> listt = new ArrayList<>(customInvoiceModel.getPayments().values());
            for (PaymentModel model : listt) {
                totalPaid = totalPaid + model.getPrice();
            }
            paid.setText("AED " + totalPaid);
            balance.setText("AED " + (customInvoiceModel.getTotal() - totalPaid));
        }
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
