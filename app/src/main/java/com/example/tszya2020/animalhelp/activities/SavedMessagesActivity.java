package com.example.tszya2020.animalhelp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.SavedMessagesAdapter;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * activity that's shown when user pressed Saved Messages button in profile, simply lists
 * all the saved messages the user saved from chats so far
 */
public class SavedMessagesActivity extends AppCompatActivity
{
    private String currentUid;
    private DatabaseReference savedMessageRef;
    private LinearLayoutManager linearLayoutManager;
    private SavedMessagesAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> allMessages;

    /**
     * when activity is first opened, set content from saved_messages_activity.xml,
     * assigns views to fields, gets saved messages from database, adds it to UI
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_messages_activity);
        currentUid = getIntent().getStringExtra(Constants.UID_KEY);
        Log.d("savedMsg", currentUid);
        allMessages = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.saved_msg_recycler_view);
        adapter = new SavedMessagesAdapter(allMessages);

        savedMessageRef = Constants.BASE_INSTANCE.child(Constants.USER_PATH)
                .child(currentUid).child(Constants.SAVED_MESSAGE_PATH);
        getInfo();

    }

    /**
     * gets all saved messages from database.  When complete, set layout manager,
     * add linear layout manager and adapter to recycler view
     */
    private void getInfo()
    {
        final TaskCompletionSource<String> getMessages = new TaskCompletionSource<>();
        savedMessageRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    String msg = data.getKey();
                    allMessages.add(msg);
                }
                Log.d("savedMsg", allMessages.toString());
                Log.d("savedMsg", "" + dataSnapshot.getChildrenCount());
                getMessages.setResult(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });

        getMessages.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(allMessages.size() == 0)
                {
                    allMessages.add("You haven't added any new messages!  " +
                            "Click on chats when chatting to add them here.");
                }
                adapter.notifyDataSetChanged();
                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                //recyclerView.addItemDecoration(new DividerItemDecoration(SavedMessagesActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
