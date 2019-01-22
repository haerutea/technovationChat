package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A login screen that offers login via uEmail/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String TAG = "LogIn";

    private FirebaseAuth mAuth;

    // UI references.

    private EditText emailField;
    private EditText passwordField;

    private Button bLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        bLogIn = findViewById(R.id.email_sign_in_button);
        bLogIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!formFilled()) {
            return;
        }
        ProgressDialog loading = ProgressDialogUtils
                .showProgressDialog(this, getString(R.string.loading));

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            logIn(user);
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
                                errorMsg = "Incorrect uEmail or password.";
                            }
                            catch(Exception e)
                            {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed. " + errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        loading.dismiss();
    }
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

    protected void logIn(FirebaseUser user)
    {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap userMap = (HashMap) dataSnapshot.getValue();

                if (userMap != null)
                {
                    String name = (String) userMap.get("name");
                    String email = (String) userMap.get("email");
                    String rank = (String) userMap.get("rank");
                    boolean online = (boolean) userMap.get("onlineStatus");
                    boolean chatting = (boolean) userMap.get("chattingStatus");
                    User userPref = new User(name, email, rank, online, chatting);
                    UserSharedPreferences.getInstance(LoginActivity.this).saveUserInfo(userPref);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "login failed: " + databaseError.getMessage());
            }
        });

        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(Constants.UID_KEY, user.getUid());
        startActivity(intent);
    }
}
