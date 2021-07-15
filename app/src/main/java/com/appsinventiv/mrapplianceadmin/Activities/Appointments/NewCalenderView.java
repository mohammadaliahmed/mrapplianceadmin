package com.appsinventiv.mrapplianceadmin.Activities.Appointments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.appsinventiv.mrapplianceadmin.Adapters.CalanderDaysAdapter;
import com.appsinventiv.mrapplianceadmin.R;
import com.appsinventiv.mrapplianceadmin.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class NewCalenderView extends AppCompatActivity {
    RecyclerView recycler;
    private List<Long> itemList = new ArrayList<>();

    CalanderDaysAdapter adapter;
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calander);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Calender");
        date = findViewById(R.id.date);
        date.setText(CommonUtils.getMonthAndYear(System.currentTimeMillis()));
        recycler = findViewById(R.id.recycler);
        itemList.add(System.currentTimeMillis() - (86400000 * 3));
        itemList.add(System.currentTimeMillis() - (86400000 * 2));
        itemList.add(System.currentTimeMillis() - (86400000));
        itemList.add(System.currentTimeMillis());
        itemList.add(System.currentTimeMillis() + (86400000));
        itemList.add(System.currentTimeMillis() + (86400000 * 2));
        itemList.add(System.currentTimeMillis() + (86400000 * 3));

        adapter = new CalanderDaysAdapter(this, itemList);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recycler.setAdapter(adapter);


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
