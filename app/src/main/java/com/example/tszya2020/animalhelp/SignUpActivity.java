package com.example.tszya2020.animalhelp;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A login screen that offers login via uEmail/password.
 */
public class SignUpActivity extends AuthActivity implements View.OnClickListener, OnCompleteListener<AuthResult>
{


    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authListener;

    // UI references.
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText rePasswordField;

    private Button bCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //https://stackoverflow.com/questions/41105826/change-displayname-in-firebase/43680527#43680527
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null)
                {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernameField.getText().toString()).build();
                    user.updateProfile(profileUpdates);
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Display name: ", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            }
                        }
                    });
                }
            }
        };
        // Set up the login form.
        usernameField = findViewById(R.id.sign_up_username);
        emailField = findViewById(R.id.sign_up_email);
        passwordField = findViewById(R.id.sign_up_password);
        rePasswordField = findViewById(R.id.sign_up_re_password);

        // Buttons
        bCreateAccount = findViewById(R.id.email_sign_up_button);
        bCreateAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == bCreateAccount.getId())
        {
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        }
    }

    private void createAccount(String email, String password)
    {
        Log.d(TAG, "createAccount:" + email);
        if (!formFilled())
        {
            return;
        }
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);

    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
        else
        {
            String errorMsg = "";
            try
            {
                throw task.getException();
            }
            catch(FirebaseAuthInvalidCredentialsException e)
            {
                errorMsg = "Invalid uEmail.";
            }
            catch(FirebaseAuthUserCollisionException e)
            {
                errorMsg = "The uEmail address is already in use.";
            }
            catch (Exception e)
            {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
            }
            // If sign in fails, display a message to the user.
            Toast.makeText(SignUpActivity.this, "Authentication failed.  " + errorMsg,
                    Toast.LENGTH_SHORT).show();
        }

        // [START_EXCLUDE]
        hideProgressDialog();
        // [END_EXCLUDE]
    }

    //TODO: DUPLICATED CODE IN LOGINACTIVITY. FFIIIIXXXXXX
    private boolean formFilled() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        }
        else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        String rePassword = rePasswordField.getText().toString();

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        }
        else if(!password.matches("([A-Za-z0-9])+"))
        {
            passwordField.setError("Only alphabet and digits please.");
        }
        else if(!(password.equals(rePassword)))
        {
            passwordField.setError("Passwords don't match.");
            rePasswordField.setError("Passwords don't match.");
        }
        else
        {
            passwordField.setError(null);
        }
        return valid;
    }

    //https://stackoverflow.com/questions/41105826/change-displayname-in-firebase/43680527#43680527
    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authListener); //firebaseAuth is of class FirebaseAuth
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }
}