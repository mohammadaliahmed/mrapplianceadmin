package com.appsinventiv.mrapplianceadmin.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.appsinventiv.mrapplianceadmin.Utils.CompressImage;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddService extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    Button update;
    EditText servicePrice, serviceDescription, serviceName, servicePeakPrice, commercialServicePrice, commercialServicePeakPrice, villaServicePrice;
    DatabaseReference mDatabase;
    ArrayList<String> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    StorageReference mStorageRef;

    ImageView serviceImage;
    String id;
    RelativeLayout wholeLayout;
    CheckBox residentialService, commercialService, villaService;

    boolean offeringCommercialService, offeringResidentialService, offeringVillaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Add Service");
        getPermissions();

        id = getIntent().getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        villaServicePrice = findViewById(R.id.villaServicePrice);
        villaService = findViewById(R.id.villaService);

        commercialService = findViewById(R.id.commercialService);
        residentialService = findViewById(R.id.residentialService);
        update = findViewById(R.id.update);
        servicePrice = findViewById(R.id.servicePrice);
        serviceDescription = findViewById(R.id.serviceDescription);
        serviceName = findViewById(R.id.serviceName);
        serviceImage = findViewById(R.id.serviceImage);
        wholeLayout = findViewById(R.id.wholeLayout);
        servicePeakPrice = findViewById(R.id.servicePeakPrice);
        commercialServicePeakPrice = findViewById(R.id.commercialServicePeakPrice);
        commercialServicePrice = findViewById(R.id.commercialServicePrice);


        serviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceName.getText().toString().trim().isEmpty()) {
                    serviceName.setError("Enter service name");
                } else if (servicePrice.getText().toString().trim().isEmpty()) {
                    servicePrice.setError("Enter service price");
                } else if (commercialServicePrice.getText().toString().trim().isEmpty()) {
                    commercialServicePrice.setError("Enter service price");
                } else if (serviceDescription.getText().toString().trim().isEmpty()) {
                    serviceDescription.setError("Enter service description");
                } else {
                    if (id != null) {
                        updateDataToDB();
                    } else {
                        sendDataToDB();
                    }
                }
            }
        });

        residentialService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        offeringResidentialService = true;
                    } else {
                        offeringResidentialService = false;
                    }
                }
            }
        });
        commercialService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        offeringCommercialService = true;
                    } else {
                        offeringCommercialService = false;
                    }
                }
            }
        });
        villaService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        offeringVillaService = true;
                    } else {
                        offeringVillaService = false;
                    }
                }
            }
        });


        if (id != null) {
            getDataFromDB();
        }
    }

    private void updateDataToDB() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", serviceName.getText().toString());
        map.put("description", serviceDescription.getText().toString());
        map.put("serviceBasePrice", Integer.parseInt(servicePrice.getText().toString()));
        map.put("peakPrice", Integer.parseInt(servicePeakPrice.getText().toString()));
        map.put("commercialServicePrice", Integer.parseInt(commercialServicePrice.getText().toString()));
        map.put("villaServicePrice", Integer.parseInt(villaServicePrice.getText().toString()));
        map.put("commercialServicePeakPrice", Integer.parseInt(commercialServicePeakPrice.getText().toString()));
        map.put("offeringCommercialService", offeringCommercialService);
        map.put("offeringResidentialService", offeringResidentialService);
        map.put("offeringVillaService", offeringVillaService);

        mDatabase.child("Services").child(id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                        if (imageUrl.size() > 0) {
                            putPictures(imageUrl.get(0));
                        } else {
                            CommonUtils.showToast("Updated");

                            wholeLayout.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void getDataFromDB() {
        mDatabase.child("Services").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ServiceModel model = dataSnapshot.getValue(ServiceModel.class);
                    if (model != null) {
                        serviceName.setText(model.getName());
                        servicePrice.setText("" + model.getServiceBasePrice());
                        villaServicePrice.setText("" + model.getVillaServicePrice());
                        servicePeakPrice.setText("" + model.getPeakPrice());
                        commercialServicePrice.setText("" + model.getCommercialServicePrice());
                        commercialServicePeakPrice.setText("" + model.getCommercialServicePeakPrice());
                        serviceDescription.setText(model.getDescription());
                        try {
                            Glide.with(AddService.this).load(model.getImageUrl()).into(serviceImage);
                        } catch (Exception e) {


                        }
                        if (model.offeringCommercialService) {
                            commercialService.setChecked(true);
                            offeringCommercialService = true;
                        }
                        if (model.offeringResidentialService) {
                            residentialService.setChecked(true);
                            offeringResidentialService = true;
                        }
                        if (model.offeringVillaService) {
                            villaService.setChecked(true);
                            offeringVillaService = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToDB() {
        wholeLayout.setVisibility(View.VISIBLE);
        id = mDatabase.push().getKey();
        ServiceModel serviceModel = new ServiceModel(
                id,
                1,
                serviceName.getText().toString(),
                serviceDescription.getText().toString(),
                true,
                false,
                Integer.parseInt(servicePrice.getText().toString()),
                Integer.parseInt(servicePeakPrice.getText().toString()),
                Integer.parseInt(commercialServicePrice.getText().toString()),
                Integer.parseInt(villaServicePrice.getText().toString()),
                Integer.parseInt(commercialServicePeakPrice.getText().toString()),
                "",
                offeringResidentialService,
                offeringCommercialService,
                offeringVillaService
        );
        mDatabase.child("Services").child(id).setValue(serviceModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (imageUrl.size() > 0) {
                            putPictures(imageUrl.get(0));
                        } else {
                            CommonUtils.showToast("Updated");

                            wholeLayout.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void initMatisse() {
        mSelected.clear();
        imageUrl.clear();
        Options options = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                ;                                       //Custom Path For media Storage

        Pix.start(this, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mSelected = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (String img : mSelected) {

                CompressImage compressImage = new CompressImage(AddService.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            Glide.with(this).load(mSelected.get(0)).into(serviceImage);

        }


    }

    public void putPictures(String path) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        final StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String downloadUrl = task.getResult().toString();
                                mDatabase.child("Services")
                                        .child(id)
                                        .child("imageUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CommonUtils.showToast("Updated");
                                        wholeLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");
                        wholeLayout.setVisibility(View.GONE);

                    }
                });


    }

    //


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

}
