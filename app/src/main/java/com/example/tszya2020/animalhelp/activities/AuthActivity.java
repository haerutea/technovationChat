package com.example.tszya2020.animalhelp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.DialogUtils;
import com.example.tszya2020.animalhelp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String LOG_TAG = "AuthActivity";
    private FirebaseAuth mAuth;

    private Button bSignUp;
    private Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        mAuth = FirebaseAuth.getInstance();

        bSignUp = findViewById(R.id.button_to_sign_up);
        bLogin = findViewById(R.id.button_to_login);

        bSignUp.setOnClickListener(this);
        bLogin.setOnClickListener(this);

        GoogleApiAvailability gPlayStatus = GoogleApiAvailability.getInstance();
        int result = gPlayStatus.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS)
        {
            Dialog errDialog = gPlayStatus.getErrorDialog(this, result, 1);
            errDialog.show();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //if(currentUser != null) EL - removed this null check as it happens in updateUI
            Log.d(LOG_TAG, "update UI started with user in onStart()");
            updateUI(currentUser);
    }

    public void onClick(View v)
    {
        int id = v.getId();
        if (id == bSignUp.getId())
        {
            Intent intent = new Intent(getApplicationContext(), StrengthsActivity.class);
            startActivity(intent);
        }
        else if(id == bLogin.getId())
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    protected void updateUI(FirebaseUser user)
    {
        ProgressDialog loadingWindow = DialogUtils.showProgressDialog(this, "loading");
        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(Constants.UID_KEY, user.getUid());
            startActivity(intent);
        }
        loadingWindow.dismiss();
    }
}
