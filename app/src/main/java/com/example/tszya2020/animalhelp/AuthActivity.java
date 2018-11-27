package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    private Button bSign_up;
    private Button bLogin;

    private SignUpActivity signUp;
    private LoginActivity login;
    private AccountActivity profile;
    private AuthActivity auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        signUp = new SignUpActivity();
        login = new LoginActivity();
        profile = new AccountActivity();
        auth = new AuthActivity();

        bSign_up = findViewById(R.id.button_to_sign_up);
        bLogin = findViewById(R.id.button_to_login);

        bSign_up.setOnClickListener(this);
        bLogin.setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    public void onClick(View v)
    {
        int id = v.getId();
        if (id == bSign_up.getId())
        {
            Intent intent = new Intent(getApplicationContext(), signUp.getClass());
            startActivity(intent);
        }
        else if(id == bLogin.getId())
        {
            Intent intent = new Intent(getApplicationContext(), login.getClass());
            startActivity(intent);
        }
    }

    //takes in user and gets profile data (or sign up logic)
    //TODO: CHANGE ALLLLLLLLLL THIS TO GO TO PROFILEACTIVITIY  INSTEAD
    protected void updateUI(FirebaseUser user)
    {
        showProgressDialog();
        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), profile.getClass());
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), auth.getClass());
            startActivity(intent);
        }
    }

    protected void showProgressDialog()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
    }
}
