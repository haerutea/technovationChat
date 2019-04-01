package com.example.tszya2020.animalhelp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tszya2020.animalhelp.ChangePassswordFragment;
import com.example.tszya2020.animalhelp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private Button changePassword;
    private Button changeMedication;
    private Button confirm;
    private EditText username;
    private EditText counsellorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        confirm = findViewById(R.id.confirm_button);
        changeMedication = findViewById(R.id.change_medication_button);
        changeMedication.setEnabled(false);
        changePassword = findViewById(R.id.change_password_button);
        if(!mUser.isEmailVerified())
        {
            changePassword.setEnabled(false);
        }
        confirm.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    public void openPassword()
    {
        ChangePassswordFragment passFrag = ChangePassswordFragment.newInstance();
        passFrag.show(this.getSupportFragmentManager(), "passwordFragment");
    }

    public void confirmChanges()
    {

    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i == changePassword.getId())
        {
            openPassword();
        }
        else if(i == confirm.getId())
        {
            confirmChanges();
        }
    }
}