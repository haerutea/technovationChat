package com.example.tszya2020.animalhelp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        change_password = findViewById(R.id.change_password_button);
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
    }
}
