package com.example.tszya2020.animalhelp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 * A login screen that offers login via uEmail/password.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{


    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authListener;
    private boolean verified;
    private AlertDialog verifyDialog;

    // UI references.
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText rePasswordField;

    private Button bSignUp;

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
        bSignUp = findViewById(R.id.email_sign_up_button);
        bSignUp.setOnClickListener(this);

        verified = false;
    }


    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == bSignUp.getId())
        {
            createAccount(emailField.getText().toString(), passwordField.getText().toString());
        }
    }

    private void createAccount(String email, String password)
    {
        final ProgressDialog loading = DialogUtils.showProgressDialog(this, getString(R.string.loading));
        Log.d(TAG, "createAccount:" + email);
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
                            Log.d(TAG, "createUserWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            loading.dismiss();
                            //send email verification
                            //https://firebase.google.com/docs/auth/android/manage-users
                            checkVerification();
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
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            }
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.  " + errorMsg,
                                    Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }
                });
    }

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

    private void sendEmail()
    {
        mUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            //check if it's verified yet
                            do
                            {
                                mUser.reload().addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            if(mUser.isEmailVerified())
                                            {
                                                Log.d(TAG, ""+mUser.isEmailVerified());
                                                verifyDialog.dismiss();
                                                signUp();
                                            }
                                        }
                                    }
                                });
                            }
                            while(!mUser.isEmailVerified());
                        }
                    }
                });
    }

    private void checkVerification()
    {
        sendEmail();
        AlertDialog.Builder verifyEmail = new AlertDialog.Builder(this);
        verifyEmail.setMessage(R.string.verify_email)
                .setPositiveButton(R.string.resend_email, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail();
                    }
                });
        verifyDialog = verifyEmail.create();
        verifyDialog.show();
        //matakeci@globaleuro.net
    }

    private void reloadStatus()
    {
        if(mUser != null)
        {
            Task verifyTask = mUser.reload();
            verifyTask.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    verified = mUser.isEmailVerified();
                    Log.d(TAG, "verified:" + verified);
                    if(verified)
                    {
                        verifyDialog.dismiss();
                    }
                }
            });
        }
    }

    private void signUp()
    {
        Log.d(TAG, "email is verified.");
        final TaskCompletionSource<String> getNotifToken = new TaskCompletionSource<>();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Constants.notifToken = token;
                        getNotifToken.setResult(null);
                    }
                });
        getNotifToken.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                User newUser = new User(mUser.getUid(), usernameField.getText().toString(), mUser.getEmail(),
                        Constants.DEFAULT, Constants.notifToken, true, false);
                FirebaseDatabase.getInstance().getReference().child(Constants.USER_PATH)
                        .child(mUser.getUid()).setValue(newUser);
            }
        });

        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(Constants.UID_KEY, mUser.getUid());
        startActivity(intent);
    }

    //https://stackoverflow.com/questions/41105826/change-displayname-in-firebase/43680527#43680527
    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authListener);
        //https://stackoverflow.com/a/45306528
        //reloadStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }
}