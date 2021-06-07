package com.appsinventiv.mrapplianceadmin.Activities.Salaries;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
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
    Button printSalary;
    LinearLayout salaryLayout;
    TextView salaryDate;
    Button markAsPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        salaryId = getIntent().getStringExtra("salaryId");
        this.setTitle("View Salary");
        printSalary = findViewById(R.id.printSalary);
        salaryDate = findViewById(R.id.salaryDate);
        name = findViewById(R.id.name);
        markAsPaid = findViewById(R.id.markAsPaid);
        phone = findViewById(R.id.phone);
        salaryLayout = findViewById(R.id.salaryLayout);
        role = findViewById(R.id.role);
        grossSalary = findViewById(R.id.grossSalary);
        totalAllowancesTV = findViewById(R.id.totalAllowancesTV);
        totalDeductionsTv = findViewById(R.id.totalDeductionsTv);
        totalSalary = findViewById(R.id.totalSalary);
        recyclerAllowances = findViewById(R.id.recyclerAllowances);
        recyclerDeductions = findViewById(R.id.recyclerDeductions);
        printSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewToBitmap(salaryLayout, salaryLayout.getWidth(), salaryLayout.getHeight());

            }
        });

        recyclerAllowances.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerDeductions.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        allowancesAdapter = new SalaryItemsAdapter(this, allowancesList);
        deductionAdapter = new SalaryItemsAdapter(this, deductionList);

        recyclerAllowances.setAdapter(allowancesAdapter);
        recyclerDeductions.setAdapter(deductionAdapter);

        markAsPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSalary.this);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to mark as paid ");

                // add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Salaries").child(salaryId).child("status").setValue("Paid").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Marked as paid");
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
        });
        getDataFromServer();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, salaryModel.getServiceman().getName()
                + "-" + salaryModel.getMonth() + "-" + salaryModel.getYear(), null);
        return Uri.parse(path);
    }

    public Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
//        bitmap = view.getDrawingCache();

        Uri ui = getImageUri(bitmap);
        CommonUtils.showToast("Saved Image");
//        uploadPicture(CommonUtils.getRealPathFromURI(ui));
        return bitmap;
    }


    private void getDataFromServer() {
        mDatabase.child("Salaries").child(salaryId).addValueEventListener(new ValueEventListener() {
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
        totalSalary.setText("Total Salary: AED " + salaryModel.getTotal() + " (" + salaryModel.getStatus() + ")");
        totalAllowancesTV.setText("AED " + totalAllowances);
        totalDeductionsTv.setText("AED " + totalDeductions);
        salaryDate.setText("Pay slip for " + CommonUtils.getMonthNameAbbr(salaryModel.getMonth() - 1) + "-" + salaryModel.getYear());

    }

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
//        CommonUtils.showToast(PERMISSION_ALL+"");
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
