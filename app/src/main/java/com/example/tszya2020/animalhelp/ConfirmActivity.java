package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener
{
    private String logName;
    private Request sentRequest;
    //UI fields
    private TextView tDescript;
    private Button bAccept;
    private Button bDeny;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_activity);
        logName = "confirmingRequest";
        tDescript = findViewById(R.id.request_description);
        bAccept = findViewById(R.id.request_accept);
        bDeny = findViewById(R.id.request_deny);

        bAccept.setOnClickListener(this);
        bDeny.setOnClickListener(this);

    }

    private void setDescription()
    {
        Constants.BASE_INSTANCE.child(Constants.USER_PATH).
                child(Constants.REQUEST_PATH).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d(logName, dataSnapshot.getValue(User.class).toString());
                sentRequest = dataSnapshot.getValue(Request.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(logName, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if(id == bAccept.getId())
        {

        }
        else if(id == bDeny.getId())
        {

        }
    }
}