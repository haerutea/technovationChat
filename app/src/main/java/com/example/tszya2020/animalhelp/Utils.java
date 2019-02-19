package com.example.tszya2020.animalhelp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Utils
{
    private static User currentAccount;
    private static boolean complete = false;

    public static User getUserFromDB(String givenUid)
    {
        DatabaseReference currentUserDBRef = FirebaseDatabase.getInstance()
                .getReference().child(Constants.USER_PATH).child(givenUid);
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

        currentUserDBRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d("userChildrenData", dataSnapshot.getValue(User.class).toString());
                currentAccount = dataSnapshot.getValue(User.class);
                source.setResult(null);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                source.setException(databaseError.toException());
                Log.d("gettingAccount", "onCancelled: " + databaseError.getMessage());
            }
        });
        source.getTask().addOnCompleteListener(new OnCompleteListener<String>()
        {
            @Override
            public void onComplete(@NonNull Task<String> task)
            {
                complete = true;
            }
        });
        if(complete)
            return currentAccount;
        else
            return null;
    }
}