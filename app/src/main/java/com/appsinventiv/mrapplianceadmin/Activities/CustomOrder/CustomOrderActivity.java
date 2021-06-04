package com.appsinventiv.mrapplianceadmin.Activities.CustomOrder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.Activities.MapsActivity;
import com.appsinventiv.mrapplianceadmin.Activities.Orders.ViewOrder;
import com.appsinventiv.mrapplianceadmin.Adapters.TimeslotsAdapter;
import com.appsinventiv.mrapplianceadmin.Models.OrderModel;
import com.appsinventiv.mrapplianceadmin.Models.ServiceCountModel;
import com.appsinventiv.mrapplianceadmin.Models.User;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Services.ServiceModel;
import com.appsinventiv.mrapplianceadmin.Services.SubServiceModel;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomOrderActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    HashMap<String, ServiceModel> serviceModelHashMap = new HashMap<>();
    HashMap<String, ArrayList<SubServiceModel>> subServiceModelHashMap = new HashMap<>();
    SubserviceListAdapter adapter;

    RecyclerView recyclerview;
    private ArrayList<SubServiceModel> itemList = new ArrayList<>();
    HashMap<String, SubServiceModel> selectedMap = new HashMap<>();
    private HashMap<String, String> serviceModelHashMapWithName = new HashMap<>();
    RecyclerView recyclerviewTime;
    TimeslotsAdapter timeslotsAdapter;
    private ArrayList<String> timeList = new ArrayList<>();
    HashMap<String, ArrayList<String>> map = new HashMap<>();
    CalendarView calender;
    private String daySelected;
    private String monthSelected;

    Button createOrder;
    EditText name, phone, address;

    RelativeLayout wholeLayout;
    RadioButton residential, commercial, villa;
    private String buildingType = "Residential";
    private User user;
    long orderId = 000;
    private String serviceId;
    private String timeSelected;
    private String serviceName;
    private String getValue;
    RelativeLayout chooseLocation;
    private double lng;
    private double lat;
    TextView addresss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Create custom Order");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createOrder = findViewById(R.id.createOrder);
        wholeLayout = findViewById(R.id.wholeLayout);
        chooseLocation = findViewById(R.id.chooseLocation);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        addresss = findViewById(R.id.addresss);
        address = findViewById(R.id.address);
        residential = findViewById(R.id.residential);
        commercial = findViewById(R.id.commercial);
        villa = findViewById(R.id.villa);
        getOrderCountFromDB();
        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomOrderActivity.this, MapsActivity.class);
                startActivityForResult(i, 1);
            }
        });

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (phone.getText().length() == 0) {
                    phone.setError("Enter phone");
                } else if (address.getText().length() == 0) {
                    address.setError("Enter address");
                } else if (buildingType == null) {
                    CommonUtils.showToast("Please select building type");
                } else if (timeSelected == null) {
                    CommonUtils.showToast("Please select date and time ");
                } else {
                    String[] fullname = name.getText().toString().split(" ");
                    try {
                        String abc = fullname[1];
                        registerUser();
                    } catch (Exception e) {
                        CommonUtils.showToast("Please enter full name");
                        name.requestFocus();
                        name.setError("Enter full name");
                    }

                }
            }
        });


        initRadio();
        initThings();


        getServicesFromDB();
    }

    private void initRadio() {
        residential.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        buildingType = "Residential";

                    }
                }
            }
        });
        commercial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        buildingType = "Commercial";

                    }
                }
            }
        });
        villa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        buildingType = "Villa";

                    }
                }
            }
        });
    }

    private void getOrderCountFromDB() {
        mDatabase.child("Orders").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        if (key.contains(CommonUtils.getFormattedDateOnlyy(System.currentTimeMillis()))) {
                            orderId = Long.parseLong(key) + 1;
                        } else {
                            orderId = Long.parseLong(CommonUtils.getFormattedDateOnlyy(System.currentTimeMillis()) + String.format("%03d", 1));
                        }

                    }
                } else {
                    orderId = Long.parseLong(CommonUtils.getFormattedDateOnlyy(System.currentTimeMillis()) + String.format("%03d", 1));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerUser() {
        wholeLayout.setVisibility(View.VISIBLE);

        user = new User(

                name.getText().toString().split(" ")[0],
                name.getText().toString().split(" ")[1],
                (name.getText().toString().replace(" ", "")).toLowerCase(),
                (name.getText().toString().replace(" ", "")).toLowerCase(),
                name.getText().toString().replace(" ", "") + "@gmail.com",
                phone.getText().toString(),
                phone.getText().toString(),
                CommonUtils.getFullAddress(CustomOrderActivity.this, lat, lng),
                "",
                System.currentTimeMillis(),
                true
        );

        mDatabase.child("Users").child(user.getUsername()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ArrayList<ServiceCountModel> serviceCountModels = new ArrayList<>();
                for (Map.Entry<String, SubServiceModel> entry : selectedMap.entrySet()) {
                    serviceCountModels.add(new ServiceCountModel(entry.getValue(), 1, System.currentTimeMillis()));
                }
                String orderDate = daySelected.replace("\n", "");

                OrderModel model = new OrderModel(
                        orderId,
                        System.currentTimeMillis(),
                        user,
                        serviceCountModels,
                        150l,
                        orderDate,
                        timeSelected,
                        "Under Process",
                        user.getAddress(),
                        buildingType,
                        serviceName,
                        serviceId, true, lat, lng


                );
                mDatabase.child("Orders").child("" + orderId).setValue(model);
                mDatabase.child("TimeSlots")
                        .child(CommonUtils.getYear(System.currentTimeMillis()))
                        .child(monthSelected)
                        .child(orderDate)
                        .child(timeSelected).setValue(timeSelected);
                mDatabase.child("Users").child(user.getUsername()).child("Orders").child("" + orderId).setValue(orderId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order added");
                        Intent i = new Intent(CustomOrderActivity.this, ViewOrder.class);
                        i.putExtra("orderId", "" + orderId);
                        startActivity(i);
                        wholeLayout.setVisibility(View.GONE);
                        finish();
                    }

                });


            }
        });
    }

    private void initThings() {


        recyclerviewTime = findViewById(R.id.recyclerviewTime);
        calender = findViewById(R.id.calender);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SubserviceListAdapter(this, itemList, new SubserviceListAdapter.SubserviceListAdapterCallbacks() {
            @Override
            public void onSelected(SubServiceModel model) {
                selectedMap.put(model.getId(), model);
            }

            @Override
            public void unSelected(SubServiceModel model) {
                selectedMap.remove(model.getId());

            }
        });
        recyclerview.setAdapter(adapter);
        recyclerviewTime.setLayoutManager(new GridLayoutManager(this, 3));
        timeslotsAdapter = new TimeslotsAdapter(CustomOrderActivity.this, timeList, new ArrayList<String>());
        timeslotsAdapter.setCallback(new TimeslotsAdapter.TimeSlotsCallback() {
            @Override
            public void optionSelected(String timeChosen) {
                timeSelected = timeChosen;
            }

            @Override
            public void optionUnblock(String timeChosen) {

            }
        });
        recyclerview.setAdapter(adapter);
        recyclerviewTime.setAdapter(timeslotsAdapter);
        addTime();

        daySelected = CommonUtils.getDay(System.currentTimeMillis()) + CommonUtils.getDayName(System.currentTimeMillis()).toLowerCase();
        getValue = CommonUtils.getMonthName(System.currentTimeMillis()) + daySelected;


        calender.setMinDate(System.currentTimeMillis() - 1000);
        monthSelected = CommonUtils.getMonthName(System.currentTimeMillis());
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                timeList.clear();
                addTime();
                monthSelected = CommonUtils.getMonthNameAbbr(month);
                daySelected = dayOfMonth + CommonUtils.getNameOfDay(year, month + 1, dayOfMonth).toLowerCase();
                getValue = CommonUtils.getMonthNameAbbr(month) + daySelected;

                timeslotsAdapter.setavailableTime(timeList);
                ArrayList<String> list = map.get(getValue) == null ? new ArrayList<String>() : map.get(getValue);
                timeslotsAdapter.setUnavailableTime(list);

            }
        });
    }


    private void addTime() {
        timeList.add("10:00 am");
        timeList.add("11:00 am");
        timeList.add("12:00 pm");
        timeList.add("1:00 pm");
        timeList.add("2:00 pm");
        timeList.add("3:00 pm");
        timeList.add("4:00 pm");
        timeList.add("5:00 pm");
        timeList.add("6:00 pm");
        timeList.add("7:00 pm");
        timeList.add("8:00 pm");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lat = extras.getDouble("lat");
                lng = extras.getDouble("lon");

                addresss.setText("Address: " + (lat == 0 ? "" : CommonUtils.getFullAddress(CustomOrderActivity.this, lat, lng)));
                address.setText("Address: " + (lat == 0 ? "" : CommonUtils.getFullAddress(CustomOrderActivity.this, lat, lng)));


            }

        }
    }

    private void getServicesFromDB() {
        mDatabase.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ServiceModel model = snapshot.getValue(ServiceModel.class);
                        if (model != null) {
                            serviceModelHashMap.put(model.getId(), model);
                            serviceModelHashMapWithName.put(model.getName(), model.getId());
                        }
                    }
                    if (serviceModelHashMap.size() > 0) {
                        setupSpinner();
                        getSubServicesFromDB();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupSpinner() {

        final ArrayList<String> items = new ArrayList<>();

        items.add("Select Service");
        for (Map.Entry<String, ServiceModel> entry : serviceModelHashMap.entrySet()) {
            items.add(entry.getValue().getName());
        }

        Spinner spinner = findViewById(R.id.spinner);
        final ArrayAdapter<String> adaptera = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adaptera);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {

                } else {
                    ArrayList<SubServiceModel> abc = subServiceModelHashMap.get(serviceModelHashMapWithName.get(items.get(i)));
                    if (abc != null) {
                        adapter.setItemList(abc);
                        serviceId = abc.get(0).getParentService();
                        serviceName = serviceModelHashMapWithName.get(items.get(i));
                        getTimeSlotsFromDB();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getSubServicesFromDB() {
        mDatabase.child("SubServices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubServiceModel model = snapshot.getValue(SubServiceModel.class);
                        if (model != null) {
                            if (subServiceModelHashMap.containsKey(model.getParentService())) {
                                ArrayList<SubServiceModel> list = subServiceModelHashMap.get(model.getParentService());
                                list.add(model);
                                subServiceModelHashMap.put(model.getParentService(), list);
                            } else {
                                ArrayList<SubServiceModel> list = new ArrayList<>();
                                list.add(model);
                                subServiceModelHashMap.put(model.getParentService(), list);
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTimeSlotsFromDB() {
        mDatabase.child("TimeSlots")
                .child(CommonUtils.getYear(System.currentTimeMillis()))

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot allMonths : dataSnapshot.getChildren()) {
                                for (DataSnapshot allDays : allMonths.getChildren()) {
                                    ArrayList<String> timeList = new ArrayList<>();

                                    for (DataSnapshot allTimes : allDays.getChildren()) {
                                        timeList.add(allTimes.getValue(String.class));
                                    }
                                    map.put(allMonths.getKey() + allDays.getKey(), timeList);
                                }
                            }
                            ArrayList<String> item = map.get(getValue);
                            if (item == null) {
                                timeslotsAdapter.setUnavailableTime(new ArrayList<String>());
                            } else {
                                timeslotsAdapter.setUnavailableTime(item);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
