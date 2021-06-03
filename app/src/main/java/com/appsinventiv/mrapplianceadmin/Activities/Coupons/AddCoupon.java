package com.appsinventiv.mrapplianceadmin.Activities.Coupons;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddCoupon extends AppCompatActivity {
    Button update;
    EditText couponName, couponCode, discount;
    DatabaseReference mDatabase;

    Button pick;

    String id;
    //    private List<Date> datess;
    long couponStartTime, couponEndTime;
    TextView startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Coupon");

        id = getIntent().getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        update = findViewById(R.id.update);
        couponName = findViewById(R.id.couponName);
        couponCode = findViewById(R.id.couponCode);
        discount = findViewById(R.id.discount);
        pick = findViewById(R.id.pick);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponName.getText().toString().trim().isEmpty()) {
                    couponName.setError("Enter coupon name");
                } else if (couponCode.getText().toString().trim().isEmpty()) {
                    couponCode.setError("Enter coupon code");
                } else if (discount.getText().toString().trim().isEmpty()) {
                    discount.setError("Enter  discount");
                } else if (couponStartTime < 1) {
                    CommonUtils.showToast("Please select coupon start and end time");
                } else {
                    if (id != null) {
                        updateDataToDB();
                    } else {
                        sendDataToDB();
                    }
                }
            }
        });
        if (id != null) {
            getDataFromDB();
        }
    }

    private void showPicker() {
        new DoubleDateAndTimePickerDialog.Builder(this)
                .title("Pick coupon start and end time")
                .tab0Text("Start")
                .tab1Text("End")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        couponStartTime = dates.get(0).getTime();
                        couponEndTime = dates.get(1).getTime();
                        startTime.setText(CommonUtils.getFormattedDateOnly(dates.get(0).getTime()));
                        endTime.setText(CommonUtils.getFormattedDateOnly(dates.get(1).getTime()));
                    }
                }).display();
    }

    private void updateDataToDB() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("couponName", couponName.getText().toString());
        map.put("couponCode", couponCode.getText().toString());
        map.put("discount", Integer.parseInt(discount.getText().toString()));
        if (couponStartTime > 0) {
            map.put("couponStartTime", couponStartTime);
            map.put("couponEndTime", couponEndTime);

        }

        mDatabase.child("Coupons").child(id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                    }
                });

    }

    private void getDataFromDB() {
        mDatabase.child("Coupons").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CouponModel model = dataSnapshot.getValue(CouponModel.class);
                    if (model != null) {
                        couponName.setText(model.getCouponName());
                        couponCode.setText("" + model.getCouponCode());
                        discount.setText("" + model.getDiscount());
                        startTime.setText(CommonUtils.getFormattedDateOnly(model.getCouponStartTime()));
                        endTime.setText(CommonUtils.getFormattedDateOnly(model.getCouponEndTime()));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDB() {
        CouponModel serviceModel = new CouponModel(
                couponName.getText().toString(),
                couponName.getText().toString(),
                couponCode.getText().toString(),
                Integer.parseInt(discount.getText().toString()),
                System.currentTimeMillis(),
                true,
                couponStartTime,
                couponEndTime

        );
        mDatabase.child("Coupons").child(couponName.getText().toString()).setValue(serviceModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        CommonUtils.showToast("Updated");


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
