package com.example.tszya2020.animalhelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private Button change_password;
    private Button change_medication;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        change_password = findViewById(R.id.change_password_button);
        change_medication = findViewById(R.id.change_medication_button);
        change_medication.setEnabled(false);
        if(!mUser.isEmailVerified())
        {
            change_password.setEnabled(false);
        }
        change_password.setOnClickListener(this);
    }

    public void openPassword()
    {
        ChangePassswordFragment passFrag = ChangePassswordFragment.newInstance();
        passFrag.show(this.getSupportFragmentManager(), "passwordFragment");
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i == change_password.getId())
        {
            openPassword();
        }
        else if(i == change_medication.getId())
        {
            //do nothing as of now
        }
    }
}