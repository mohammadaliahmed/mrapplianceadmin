package com.appsinventiv.mrapplianceadmin.Servicemen;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Activities.Appointments.AppointmentList;
import com.appsinventiv.mrapplianceadmin.Services.AddService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddServicemen extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    Button update;
    EditText servicemanName, username, password, mobile, age, eid, skills, salary;
    Spinner spinner;
    DatabaseReference mDatabase;
    ArrayList<String> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    StorageReference mStorageRef;

    CircleImageView servicemanImage;
    String id;
    RelativeLayout wholeLayout;
    String role;
    List<String> list = new ArrayList<>();
    private String downloadUrl = "";
    TextView currentRole;
    Button viewAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_servicemen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Add Serviceman");
        getPermissions();
        list.add("Select role");
        id = getIntent().getStringExtra("id");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        update = findViewById(R.id.update);

        servicemanImage = findViewById(R.id.servicemanImage);
        wholeLayout = findViewById(R.id.wholeLayout);
        viewAppointments = findViewById(R.id.viewAppointments);
        servicemanName = findViewById(R.id.servicemanName);
        username = findViewById(R.id.username);
        currentRole = findViewById(R.id.currentRole);
        password = findViewById(R.id.password);
        spinner = findViewById(R.id.spinner);
        mobile = findViewById(R.id.mobile);
        age = findViewById(R.id.age);
        eid = findViewById(R.id.eid);
        skills = findViewById(R.id.skills);
        salary = findViewById(R.id.salary);


        servicemanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatisse();
            }
        });

        viewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddServicemen.this, AppointmentList.class));
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (servicemanName.getText().toString().trim().isEmpty()) {
                    servicemanName.setError("Enter service name");
                } else if (username.getText().toString().trim().isEmpty()) {
                    username.setError("Enter username");
                } else if (password.getText().toString().trim().isEmpty()) {
                    password.setError("Enter password");
                } else if (mobile.getText().toString().trim().isEmpty()) {
                    mobile.setError("Enter mobile");
                } else if (age.getText().toString().trim().isEmpty()) {
                    age.setError("Enter age");
                } else if (eid.getText().toString().trim().isEmpty()) {
                    eid.setError("Enter EID");
                } else if (salary.getText().toString().trim().isEmpty()) {
                    salary.setError("Enter salary");
                } else if (role == null) {
                    CommonUtils.showToast("Select Role");
                } else {

                    if (id != null) {
                        if (imageUrl.size() > 0) {
                            putPictures(imageUrl.get(0));
                        } else {
                            CommonUtils.showToast("Updated");
                            updateDataToDB();
                            wholeLayout.setVisibility(View.GONE);
                        }

                    } else {
                        id = username.getText().toString();
                        sendDataToDB();
                    }
                }
            }
        });
        if (id != null) {
            getDataFromDB();
        }
        getJobFromServer();

    }

    private void getJobFromServer() {
        mDatabase.child("Admin").child("jobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String jobsList = dataSnapshot.getValue(String.class);
                    if (jobsList != null) {
                        ArrayList<String> abc = new ArrayList<String>(Arrays.asList(jobsList.split(",")));
                        for (String a : abc) {
                            if (!a.equalsIgnoreCase(" ")) {
                                list.add(a);
                            }
                        }
                        setupSpinner();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupSpinner() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = list.get(i).replace(" ", "");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateDataToDB() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", servicemanName.getText().toString());
        map.put("username", username.getText().toString());
        map.put("password", password.getText().toString());
        map.put("mobile", mobile.getText().toString());
        map.put("age", Integer.parseInt(age.getText().toString()));
        map.put("imageUrl", downloadUrl);
        map.put("eid", Long.parseLong(eid.getText().toString()));
        map.put("role", role);
        map.put("salary", Integer.parseInt(salary.getText().toString()));
        map.put("skills", skills.getText().toString());

        mDatabase.child("Servicemen").child(id).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                    }
                });

    }

    private void getDataFromDB() {
        mDatabase.child("Servicemen").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ServicemanModel model = dataSnapshot.getValue(ServicemanModel.class);
                    if (model != null) {
                        servicemanName.setText(model.getName());
                        username.setText("" + model.getUsername());
                        username.setEnabled(false);
                        password.setText("" + model.getPassword());
                        mobile.setText(model.getMobile());
                        salary.setText("" + model.getSalary());
                        skills.setText("" + model.getSkills());
                        downloadUrl = model.getImageUrl();
                        currentRole.setVisibility(View.VISIBLE);
                        currentRole.setText("Current Role: " + model.getRole());
                        mobile.setText(model.getMobile());
                        age.setText("" + model.getAge());
                        eid.setText("" + model.getCnic());
                        Glide.with(AddServicemen.this).load(model.getImageUrl()).placeholder(R.drawable.upload_photo).into(servicemanImage);
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
        ServicemanModel model = new ServicemanModel(
                id,
                servicemanName.getText().toString(),
                username.getText().toString(),
                password.getText().toString(),
                mobile.getText().toString(),
                role,
                true, false,
                Integer.parseInt(age.getText().toString()),
                Long.parseLong(eid.getText().toString()),
                "",
                Integer.parseInt(salary.getText().toString()),
                skills.getText().toString()

        );
        mDatabase.child("Servicemen").child(id).setValue(model)
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
                CompressImage compressImage = new CompressImage(AddServicemen.this);
                imageUrl.add(compressImage.compressImage("" + img));
            }
            Glide.with(this).load(mSelected.get(0)).placeholder(R.drawable.upload_photo).into(servicemanImage);

        }


    }

    public void putPictures(String path) {
        CommonUtils.showToast("Uploading img");
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
                                downloadUrl = task.getResult().toString();
                                updateDataToDB();
//                                mDatabase.child("Servicemen")
//                                        .child(id)
//                                        .child("imageUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        CommonUtils.showToast("Updated");
//                                        wholeLayout.setVisibility(View.GONE);
//                                    }
//                                });
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
