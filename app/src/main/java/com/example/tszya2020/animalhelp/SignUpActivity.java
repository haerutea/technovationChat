package com.example.tszya2020.animalhelp;

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

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AuthActivity implements View.OnClickListener {


    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;

    // UI references.

    private EditText emailField;
    private EditText passwordField;

    private Button bCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Set up the login form.
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        // Buttons
        bCreateAccount = findViewById(R.id.emailCreateAccountButton);
        bCreateAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == bCreateAccount.getId()) {
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!formFilled()) {
            return;
        }
        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            String errorMsg = "";
                            try{
                                throw task.getException();
                            }
                            catch(FirebaseAuthInvalidCredentialsException e)
                            {
                                errorMsg = "Invalid email.";
                            }
                            catch(FirebaseAuthUserCollisionException e)
                            {
                                errorMsg = "The email address is already in use.";
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
                });
        // [END create_user_with_email]
    }

    //TODO: DUPLICATED CODE IN LOGINACTIVITY. FFIIIIXXXXXX
    private boolean formFilled() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}