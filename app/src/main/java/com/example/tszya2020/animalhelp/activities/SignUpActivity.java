package com.example.tszya2020.animalhelp.activities;

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

import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.example.tszya2020.animalhelp.DialogUtils;
import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.object_classes.User;
import com.example.tszya2020.animalhelp.UserSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

/**
 * A sign up screen that offers login via email and password.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{


    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String LOG_TAG = "SignUp";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authListener;
    private ArrayList<String> checkedStrengths;

    // UI references.
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText rePasswordField;
    private Button bSignUp;

    /**
     * when activity is first opened, set content from sign_up_activity.xml,
     * get FirebaseAuth instance, get the list of strengths they checked from previous activity,
     * set authListener to allow code to change username of FirebaseUser, assign views to fields,
     * add onClickListener for sign up button
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        checkedStrengths = (ArrayList<String>) getIntent().getSerializableExtra(Constants.CHECKED_STRENGTHS_KEY);
        Log.d("checked:", checkedStrengths.toString());

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

        usernameField = findViewById(R.id.sign_up_username);
        emailField = findViewById(R.id.sign_up_email);
        passwordField = findViewById(R.id.sign_up_password);
        rePasswordField = findViewById(R.id.sign_up_re_password);

        // Buttons
        bSignUp = findViewById(R.id.email_sign_up_button);
        bSignUp.setOnClickListener(this);
    }


    /**
     * triggered when user clicks on sign up button
     * @param v the view user clicked on
     */
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == bSignUp.getId())
        {
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        }
    }

    /**
     * shows loading dialog, checks if form is filled correctly, then calls built-in
     * create user method from FirebaseAuth and pass the email and password parameters in,
     * sends verification email and calls signUp method
     * @param email from EditText emailField
     * @param password from EditText passwordField
     */
    private void createAccount(String email, String password)
    {
        final ProgressDialog loading = DialogUtils.showProgressDialog(this, getString(R.string.loading));
        Log.d(LOG_TAG, "createAccount:" + email);
        if (!formFilled())
        {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "createUserWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            //send email verification
                            sendEmail();
                            //https://firebase.google.com/docs/auth/android/manage-users
                            signUp();
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
                                errorMsg = "Invalid email.";
                            }
                            catch(FirebaseAuthUserCollisionException e)
                            {
                                errorMsg = "The email address is already in use.";
                            }
                            catch (Exception e)
                            {
                                Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                            }
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.  " + errorMsg,
                                    Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }
                });
    }

    /**
     * checks if form is filled in correctly
     * @return true or false depending on result
     */
    private boolean formFilled()
    {
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

    /**
     * sends verification email to email address of the freshly signed up user
     */
    private void sendEmail()
    {
        mUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "Email sent.");
                        }
                    }
                });
    }

    /**
     * creates new User object and adds data to database, adds notif token to SharedPreferences,
     * starts ProfileActivity when done
     */
    private void signUp()
    {
        //change FirebaseUser displayName field
        String username = usernameField.getText().toString();
        User newUser = new User(mUser.getUid(), username, mUser.getEmail(), checkedStrengths, true, false);
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.USER_PATH).child(mUser.getUid()).setValue(newUser);

        //https://firebase.google.com/docs/cloud-messaging/android/client?authuser=0
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(mUser.getUid()).
                                child(Constants.TOKEN_KEY).child(token).setValue(true);
                        UserSharedPreferences.getInstance(SignUpActivity.this).setInfo(Constants.TOKEN_KEY, token);
                    }
                });

        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * when user comes back to this screen
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        mAuth.addAuthStateListener(authListener);
    }

    /**
     * when user leaves this screen
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        if (authListener != null)
        {
            mAuth.removeAuthStateListener(authListener);
        }
    }
}