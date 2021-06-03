package com.appsinventiv.mrapplianceadmin.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;


import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.NotificationObserver;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewQuotation extends AppCompatActivity implements NotificationObserver {
    ImageView image;
    String orderId;
    DatabaseReference mDatabase;
    private String fcmKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quotation);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        image = findViewById(R.id.image);

        orderId = getIntent().getStringExtra("orderId");
        image.setOnTouchListener(new ImageMatrixTouchHandler(this));


        getDataFromServer();

    }


    private void getDataFromServer() {
        mDatabase.child("Quotations").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String imgUrl = dataSnapshot.getValue(String.class);
                    if (imgUrl != null) {
                        Glide.with(ViewQuotation.this).load(imgUrl).into(image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
