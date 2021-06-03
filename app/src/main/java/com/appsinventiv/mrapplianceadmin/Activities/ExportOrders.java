package com.appsinventiv.mrapplianceadmin.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Adapters.ReportsAdapter;
import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

public class ExportOrders extends AppCompatActivity {
    DatabaseReference mDatabase;

    Button export;
    private ArrayList<OrderModel> itemList = new ArrayList<>();
    private String csv;
    TextView startDate, endDate;
    EditText csvFileName;

    String filename = "";

    private long IntStartDate, IntEndDate;
    RecyclerView recycler;
    TextView orderCount;
    private ArrayList<String> filenames = new ArrayList<>();

    ReportsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_orders);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Export");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        orderCount = findViewById(R.id.orderCount);
        recycler = findViewById(R.id.recycler);
        export = findViewById(R.id.export);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        csvFileName = findViewById(R.id.fileName);
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        final String fileName = "AnalysisData.csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        // Here csv file name is MyCsvFile.csv


        IntStartDate = Long.parseLong(startDate.getText().toString().replace("-", "") + "001");
        IntEndDate = Long.parseLong(endDate.getText().toString().replace("-", "") + "001");
        getDataFromDB();
        getReportsFromDB();
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (csvFileName.getText().length() == 0) {
                    csvFileName.setError("Enter filename");
                } else if (itemList.size() < 1) {

                    CommonUtils.showToast("No data to write");
                } else {
                    filename = csvFileName.getText().toString();
                    csv = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename + ".csv";

                    writeDataToFile();
                    updateFileNameToDb();
                }

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(ExportOrders.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        yearr = year;
//                        age = 2019 - yearr;
                        startDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                        String stringEndDate = String.format("%04d%02d%02d", year, month + 1, dayOfMonth);
                        stringEndDate = stringEndDate + "001";
                        IntStartDate = Long.parseLong(stringEndDate);


                    }
                }, Integer.parseInt(CommonUtils.getYear(System.currentTimeMillis())),
                        Integer.parseInt(CommonUtils.getMonth(System.currentTimeMillis())) - 2,
                        1
                );
                dlg.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dlg = new DatePickerDialog(ExportOrders.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        yearr = year;
//                        age = 2019 - yearr;
                        endDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                        String stringEndDate = String.format("%04d%02d%02d", year, month + 1, dayOfMonth);
                        stringEndDate = stringEndDate + "001";
                        IntEndDate = Long.parseLong(stringEndDate);

                    }
                }, Integer.parseInt(CommonUtils.getYear(System.currentTimeMillis())),
                        Integer.parseInt(CommonUtils.getMonth(System.currentTimeMillis())) - 1,
                        Integer.parseInt(CommonUtils.getDay(System.currentTimeMillis()))
                );
                dlg.show();
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ReportsAdapter(this, filenames);
        recycler.setAdapter(adapter);


//
    }

    private void getReportsFromDB() {
        mDatabase.child("Reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    filenames.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        filenames.add(snapshot.getKey() + ".csv");
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateFileNameToDb() {
        mDatabase.child("Reports").child(filename).setValue(filename).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    private void getDataFromDB() {
        mDatabase.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OrderModel model = snapshot.getValue(OrderModel.class);
                        if (model != null) {
                            itemList.add(model);

                        }
                    }
                    orderCount.setText("Pick dates to export report\nTotal Order count: " + itemList.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void writeDataToFile() {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Order #", "Order Status", "Date", "Client Name", "Client Address", "Client Lat", "Client Long",
                    "Services delivered", "Material Bill Amount", "MB charges 10%",
                    "Time consumed", "Service charges", "Tax(s)", "Serviceman", "Distance", "Rating",
                    "Coupon", "Discount applied", "Total Bill"});
            for (OrderModel model : itemList) {
                if (model.getOrderId() > IntStartDate && model.getOrderId() < IntEndDate) {
                    data.add(new String[]{
                            "" + model.getOrderId(),
                            "" + model.getOrderStatus(),
                            "" + CommonUtils.getFormattedDateOnly(model.getTime()),
                            model.getUser().getFullName(),
                            model.getOrderAddress(),
                            "" + model.getLat(),
                            "" + model.getLon(),
                            joinArray(model.getCountModelArrayList()),
                            "AED "+ model.getMaterialBill(),
                            "AED "+ (model.getMaterialBill() / 10),
                            "" + model.getTotalHours() + " hrs",
                            "AED "+ model.getServiceCharges(),
                            "" + 0,
                            model.getAssignedToName(),
                            calculateDistance(model),
                            "" + model.getRating(),
                            model.getCouponCode(),
                            model.getDiscount() + " %",
                            "AED "+ model.getTotalPrice()


                    });
                }
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

    private String calculateDistance(OrderModel model) {
        double abc = CommonUtils.distance(
                model.getStartJourneyLat(),
                model.getStartJourneyLng(),
                model.getEndJourneyLat(),
                model.getEndJourneyLng()
        );
        return ("" + abc);
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

    public String joinArray(ArrayList<ServiceCountModel> itemList) {
        String abc = "";
        for (ServiceCountModel m : itemList) {
            abc = abc + m.getService().getName() + " QTY: " + m.getQuantity() + "\n";
        }
        return abc;
//        final Iterator<ServiceCountModel> it = itemList.iterator();
//        if (!it.hasNext()) {
//            return "";
//        }
//        final StringBuilder sb = new StringBuilder();
//        sb.append(it.next());
//        while (it.hasNext()) {
//            sb.append(delimiter);
//            sb.append(it.next().getService().getName());
//        }
//        return sb.toString();
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
