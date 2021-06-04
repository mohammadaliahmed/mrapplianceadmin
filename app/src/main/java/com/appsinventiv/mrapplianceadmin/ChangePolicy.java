package com.appsinventiv.mrapplianceadmin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChangePolicy extends AppCompatActivity {

    DatabaseReference mDatabase;

    EditText privacyUrl;
    Button updatePrivacyUrl;
    Button choosePrivacyFile;


    EditText aboutUsUrl;
    Button updateAboutUsUrl;
    Button chooseAboutUsFile;


    EditText ratelistUrl;
    Button updateratelistUrl;
    Button chooseratelistFile;

    EditText serviceAreaUrl;
    Button updateserviceAreaUrl;
    Button chooseserviceAreaFile;


    EditText faqsUrl;
    Button updatefaqsUrl;
    Button choosefaqsFile;


    private StorageReference mStorageRef;
    private static final int REQUEST_CODE_FILE = 25;
    String ke;

    RelativeLayout wholeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_policy);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.setTitle("PDF settings");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getPermissions();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        wholeLayout = findViewById(R.id.wholeLayout);
        updatePrivacyUrl = findViewById(R.id.updatePrivacyUrl);
        privacyUrl = findViewById(R.id.privacyUrl);
        choosePrivacyFile = findViewById(R.id.choosePrivacyFile);

        aboutUsUrl = findViewById(R.id.aboutUsUrl);
        chooseAboutUsFile = findViewById(R.id.chooseAboutUsFile);
        updateAboutUsUrl = findViewById(R.id.updateAboutUsUrl);

        ratelistUrl = findViewById(R.id.ratelistUrl);
        chooseratelistFile = findViewById(R.id.chooseratelistFile);
        updateratelistUrl = findViewById(R.id.updateratelistUrl);


        serviceAreaUrl = findViewById(R.id.serviceAreaUrl);
        chooseserviceAreaFile = findViewById(R.id.chooseserviceAreaFile);
        updateserviceAreaUrl = findViewById(R.id.updateserviceAreaUrl);


        faqsUrl = findViewById(R.id.faqsUrl);
        choosefaqsFile = findViewById(R.id.choosefaqsFile);
        updatefaqsUrl = findViewById(R.id.updatefaqsUrl);


        updatefaqsUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("PDFs").child("faqs").setValue(faqsUrl.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Url Updated");

                    }
                });
            }
        });

        choosefaqsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ke = "faqs";
                openFile(REQUEST_CODE_FILE);
            }
        });


        serviceAreaUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("PDFs").child("serviceArea").setValue(serviceAreaUrl.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Url Updated");

                    }
                });
            }
        });

        chooseserviceAreaFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ke = "serviceArea";
                openFile(REQUEST_CODE_FILE);
            }
        });
        updateratelistUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("PDFs").child("rateList").setValue(ratelistUrl.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Url Updated");

                    }
                });
            }
        });

        chooseratelistFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ke = "rateList";
                openFile(REQUEST_CODE_FILE);
            }
        });


        updateAboutUsUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("PDFs").child("aboutUs").setValue(aboutUsUrl.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Url Updated");

                    }
                });
            }
        });

        chooseAboutUsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ke = "aboutUs";
                openFile(REQUEST_CODE_FILE);
            }
        });


        updatePrivacyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("PDFs").child("terms").setValue(privacyUrl.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Privacy Policy Url Updated");

                    }
                });
            }
        });

        choosePrivacyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ke = "terms";
                openFile(REQUEST_CODE_FILE);
            }
        });


        getURLsFromDB();

    }

    private void getURLsFromDB() {
        mDatabase.child("PDFs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    privacyUrl.setText(dataSnapshot.child("terms").getValue(String.class));
                    serviceAreaUrl.setText(dataSnapshot.child("serviceArea").getValue(String.class));
                    aboutUsUrl.setText(dataSnapshot.child("aboutUs").getValue(String.class));
                    ratelistUrl.setText(dataSnapshot.child("rateList").getValue(String.class));
                    faqsUrl.setText(dataSnapshot.child("faqs").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openFile(Integer CODE) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE && data != null) {
            Uri Fpath = data.getData();
//            CommonUtils.showToast(Fpath+"");
            putFile("" + Fpath, ke);
        }


    }


    public void putFile(String path, final String key) {
        wholeLayout.setVisibility(View.VISIBLE);
        CommonUtils.showToast("Uploading file..");
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        final Uri file = Uri.parse(path);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("PDFs").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        mDatabase.child("PDFs").child(key).setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                CommonUtils.showToast("Updated");
//                                wholeLayout.setVisibility(View.GONE);
//                            }
//                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast(exception.getMessage() + "");

                    }
                });


    }

    //

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
