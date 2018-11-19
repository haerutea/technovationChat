package com.example.tszya2020.animalhelp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AuthActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView email;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        email = findViewById(R.id.profile_email);
        logout = findViewById(R.id.logOutButton);

        if(user!= null)
        {
            email.setText(user.getEmail());
        }

        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == logout.getId())
        {
            if(user !=null){
                mAuth.signOut();
                updateUI(null);
            }
        }
    }
}
