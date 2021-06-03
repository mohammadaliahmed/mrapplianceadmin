package com.appsinventiv.mrapplianceadmin.Activities.OrderLogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.mrapplianceadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class SolutionTracking extends AppCompatActivity {
    String orderId;
    EditText issue, solution, whatDid;
    DatabaseReference mDatabase;
    ArrayList<SolutionTrackingModel> itemList = new ArrayList<>();
    SolutionListAdapter adapter;
    RecyclerView recyclerview;
    TextView serviceName;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_tracking);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        serviceName = findViewById(R.id.serviceName);

        orderId = getIntent().getStringExtra("orderId");
        this.setTitle("Tracking for: " + orderId);

        issue = findViewById(R.id.issue);
        recyclerview = findViewById(R.id.recyclerview);
        solution = findViewById(R.id.solution);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new SolutionListAdapter(this, itemList);

        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(adapter);



        getCommentsFromDB();

    }

    private void getCommentsFromDB() {
        mDatabase.child("SolutionTracking").child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    String issu = dataSnapshot.child("issue").getValue(String.class);
                    String soluti = dataSnapshot.child("solution").getValue(String.class);

                    solution.setText(soluti);
                    issue.setText(issu);


                    for (DataSnapshot snapshot : dataSnapshot.child("comments").getChildren()) {
                        SolutionTrackingModel model = snapshot.getValue(SolutionTrackingModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    Collections.sort(itemList, new Comparator<SolutionTrackingModel>() {
                        @Override
                        public int compare(SolutionTrackingModel listData, SolutionTrackingModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
