package com.appsinventiv.mrapplianceadmin.Activities.TimeSlotManagement;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.appsinventiv.mrapplianceadmin.Adapters.TimeslotsAdapter;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeSlotList extends AppCompatActivity {
    DatabaseReference mDatabase;
    public static String timeSelected;
    public String daySelected, monthSelected;
    HashMap<String, ArrayList<String>> map = new HashMap<>();


    RecyclerView recyclerview;
    TimeslotsAdapter adapter;
    ArrayList<String> itemList = new ArrayList<>();


    CalendarView calender;
    private String getValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_management);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        this.setTitle("Time Slots");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        calender = findViewById(R.id.calender);



        recyclerview = findViewById(R.id.recyclerview);

        addTime();
        adapter = new TimeslotsAdapter(this, itemList, new ArrayList<String>());
        adapter.setCallback(new TimeslotsAdapter.TimeSlotsCallback() {
            @Override
            public void optionSelected(String timeChosen) {
                showBookAlert(timeChosen);

            }

            @Override
            public void optionUnblock(String timeChosen) {
                showUnblockSlotAlert(timeChosen);
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));

        recyclerview.setAdapter(adapter);
        daySelected = CommonUtils.getDay(System.currentTimeMillis()) + CommonUtils.getDayName(System.currentTimeMillis()).toLowerCase();
        getValue=CommonUtils.getMonthName(System.currentTimeMillis())+daySelected;
        calender.setMinDate(System.currentTimeMillis() - 1000);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                monthSelected = CommonUtils.getMonthNameAbbr(month);
                daySelected = dayOfMonth + CommonUtils.getNameOfDay(year, month + 1, dayOfMonth).toLowerCase();
                getValue=CommonUtils.getMonthNameAbbr(month)+daySelected;
                ArrayList<String> list = map.get(getValue) == null ? new ArrayList<String>() : map.get(getValue);
                adapter.setUnavailableTime(list);
                itemList.clear();
                itemList.add("10:00 am");
                itemList.add("11:00 am");
                itemList.add("12:00 pm");
                itemList.add("1:00 pm");
                itemList.add("2:00 pm");
                itemList.add("3:00 pm");
                itemList.add("4:00 pm");
                itemList.add("5:00 pm");
                itemList.add("6:00 pm");
                itemList.add("7:00 pm");
                itemList.add("8:00 pm");

                adapter.setavailableTime(itemList);


            }
        });

        getTimeSlotsFromDB();

    }

    private void showBookAlert(final String timeChosen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to book this slot? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                mDatabase.child("TimeSlots")
                        
                        .child(CommonUtils.getYear(System.currentTimeMillis()))
                        .child(monthSelected)
                        .child(daySelected).child(timeChosen).setValue(timeChosen).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Slot booked");
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUnblockSlotAlert(final String timeChosen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to un book this slot? ");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("TimeSlots")
                        .child(CommonUtils.getYear(System.currentTimeMillis()))
                        .child(monthSelected)
                        .child(daySelected).child(timeChosen)
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Slot un-booked");
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getTimeSlotsFromDB() {
        mDatabase.child("TimeSlots")
//                
                .child(CommonUtils.getYear(System.currentTimeMillis()))

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            map.clear();
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
                            if(item==null){
                                adapter.setUnavailableTime(new ArrayList<String>());
                            }else {
                                adapter.setUnavailableTime(item);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addTime() {
        itemList.add("10:00 am");
        itemList.add("11:00 am");
        itemList.add("12:00 pm");
        itemList.add("1:00 pm");
        itemList.add("2:00 pm");
        itemList.add("3:00 pm");
        itemList.add("4:00 pm");
        itemList.add("5:00 pm");
        itemList.add("6:00 pm");
        itemList.add("7:00 pm");
        itemList.add("8:00 pm");

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
