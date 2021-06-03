package com.appsinventiv.mrapplianceadmin.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.appsinventiv.mrapplianceadmin.Adapters.PicturesAdapter;
import com.appsinventiv.mrapplianceadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ViewServicePictures extends AppCompatActivity {

    ImageView back;
    List<Uri> mSelected2 = new ArrayList<>();

    Button pickBeforePictures, pickAfterPictures;

    LinearLayout beforeUploadBtn, afterUploabtn;
    private ArrayList<String> itemList = new ArrayList<>();
    private ArrayList<Uri> mSelected = new ArrayList<>();
    private PicturesAdapter adapter, adapter2;
    Button uploadBefore;
    StorageReference mStorageRef;

    DatabaseReference mDatabase;
    String orderId;
    RelativeLayout wholeLayout;
    int beforePicsCount = 0;
    int beforePicUploadedCount = 0;
    int afterPicsCount = 0;
    private ArrayList<String> itemList2 = new ArrayList<>();
    Button uploadAfter;
    ImageView shopBill, clientBill;


    Button uploadShopBill, uploadClientBill;
    private String shopBillImg, clientBillImg;
    boolean canViewClientBill = false, canViewShopBill = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_pictures);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Service Pictures");

        orderId = getIntent().getStringExtra("orderId");
        clientBill = findViewById(R.id.clientBill);
        pickBeforePictures = findViewById(R.id.pickBeforePictures);
        shopBill = findViewById(R.id.shopBill);
        uploadShopBill = findViewById(R.id.uploadShopBill);
        uploadClientBill = findViewById(R.id.uploadClientBill);
        wholeLayout = findViewById(R.id.wholeLayout);
        beforeUploadBtn = findViewById(R.id.beforeUploadBtn);
        uploadBefore = findViewById(R.id.uploadBefore);
        afterUploabtn = findViewById(R.id.afterUploabtn);
        pickAfterPictures = findViewById(R.id.pickAfterPictures);
        uploadAfter = findViewById(R.id.uploadAfter);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        getDataFromDB();


        shopBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> item = new ArrayList<>();
                item.add(shopBillImg);
                Intent i = new Intent(ViewServicePictures.this, PicturesSlider.class);
                i.putExtra("list", item);
                i.putExtra("position", 0);
                startActivity(i);

            }
        });
        clientBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> item = new ArrayList<>();
                item.add(clientBillImg);
                Intent i = new Intent(ViewServicePictures.this, PicturesSlider.class);
                i.putExtra("list", item);
                i.putExtra("position", 0);
                startActivity(i);

            }
        });


        setUpBeforeImgs();
        setUpAfterImgs();


    }

    private void getDataFromDB() {
        mDatabase.child("OrderPictures").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.child("before").exists()) {
                        itemList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("before").getChildren()) {
                            itemList.add(snapshot.getValue(String.class));
                        }
                        beforeUploadBtn.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {

                    }

                    if (dataSnapshot.child("after").exists()) {
                        itemList2.clear();
                        for (DataSnapshot snapshot : dataSnapshot.child("after").getChildren()) {

                            itemList2.add(snapshot.getValue(String.class));
                        }
                        afterUploabtn.setVisibility(View.GONE);
                        adapter2.notifyDataSetChanged();
                    } else {


                    }
                    if (dataSnapshot.child("shop").exists()) {
                        canViewShopBill = true;
                        uploadShopBill.setVisibility(View.GONE);
                        Glide.with(ViewServicePictures.this).load(dataSnapshot.child("shop").getValue(String.class)).into(shopBill);
                        shopBillImg = dataSnapshot.child("shop").getValue(String.class);
                    } else {
                    }
                    if (dataSnapshot.child("client").exists()) {
                        canViewClientBill = true;
                        clientBillImg = dataSnapshot.child("client").getValue(String.class);

                        uploadClientBill.setVisibility(View.GONE);
                        Glide.with(ViewServicePictures.this).load(dataSnapshot.child("client").getValue(String.class)).into(clientBill);
                    } else {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void setUpBeforeImgs() {
        RecyclerView beforeRecycler;
        beforeRecycler = findViewById(R.id.beforeRecycler);
        itemList = new ArrayList<>();
        adapter = new PicturesAdapter(this, itemList, new PicturesAdapter.PicturesAdapterCallback() {
            @Override
            public void onDelete(int position) {
//                CommonUtils.showToast("" + position);
                itemList.remove(position);
                mSelected.remove(position);
                adapter.notifyDataSetChanged();

            }
        });
        beforeRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        beforeRecycler.setAdapter(adapter);


    }

    private void setUpAfterImgs() {
        RecyclerView afterRecycler;
        afterRecycler = findViewById(R.id.afterRecycler);
        itemList2 = new ArrayList<>();
        adapter2 = new PicturesAdapter(this, itemList2, new PicturesAdapter.PicturesAdapterCallback() {
            @Override
            public void onDelete(int position) {
//                CommonUtils.showToast("" + position);
                itemList2.remove(position);
                mSelected2.remove(position);
                adapter2.notifyDataSetChanged();

            }
        });
        afterRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        afterRecycler.setAdapter(adapter2);


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
