package com.appsinventiv.mrapplianceadmin.Activities.ChatManagement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.appsinventiv.mrapplianceadmin.Models.ChatModel;
import com.appsinventiv.mrapplianceadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListOfChats extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DatabaseReference mDatabase;
    ArrayList<ChatModel> chatModels = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();

//    ArrayList<ChatModel> chatModels = new ArrayList<>();

    ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_chats);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Chats");
        recyclerView = findViewById(R.id.recyclerview_chats);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new ChatListAdapter(this, chatModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatModels.clear();
                if (dataSnapshot.getValue() != null) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mDatabase.child("Chats").child(snapshot.getKey()).limitToLast(1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                                        ChatModel model = abc.getValue(ChatModel.class);
                                        if (model != null) {

                                            chatModels.add(model);


                                            Collections.sort(chatModels, new Comparator<ChatModel>() {
                                                @Override
                                                public int compare(ChatModel listData, ChatModel t1) {
                                                    Long ob1 = listData.getTime();
                                                    Long ob2 = t1.getTime();

                                                    return ob2.compareTo(ob1);

                                                }
                                            });
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                } else {
                                    chatModels.clear();
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    chatModels.clear();
                    adapter.notifyDataSetChanged();
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
