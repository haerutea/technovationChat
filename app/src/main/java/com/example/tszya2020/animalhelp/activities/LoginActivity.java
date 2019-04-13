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
import com.example.tszya2020.animalhelp.ForgotPasswordFragment;
import com.example.tszya2020.animalhelp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email and password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //https://stackoverflow.com/questions/37886301/tag-has-private-access-in-android-support-v4-app-fragmentactivity
    private static final String TAG = "LogIn";

    private FirebaseAuth mAuth;

    // UI references.

    private EditText emailField;
    private EditText passwordField;
    private Button bForgot;
    private Button bLogIn;


    /**
     * when activity is first opened, set content from login_activity.xml,
     * assign views to fields, add onClick listeners
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        bForgot = findViewById(R.id.forgot_password_button);
        bLogIn = findViewById(R.id.email_sign_in_button);
        bForgot.setOnClickListener(this);
        bLogIn.setOnClickListener(this);
    }

    /**
     * checks if form is correctly filled, if yes, calls FirebaseAuth's built-in signIn method,
     * pass email and password as parameters.  If successful, call goToProfile with user as parameter
     * if unsuccessful, Toast the error message.
     * @param email email from user's input
     * @param password password from user's input
     */
    private void signIn(String email, String password)
    {
        Log.d(TAG, "signIn:" + email);

        if (!formFilled())
        {
            return;
        }
        ProgressDialog loading = DialogUtils
                .showProgressDialog(this, getString(R.string.loading));

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if user signed in successfully
                        if (task.isSuccessful())
                        {
                            //update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToProfile(user);
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
                            //display error message to user
                            Toast.makeText(LoginActivity.this, "Authentication failed. " + errorMsg,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        loading.dismiss();
    }

    /**
     * checks if the form is filled in correctly where all EditTexts are filled,
     * if not, show an error.
     * @return true or false if form is filled in correctly.
     */
    private boolean formFilled()
    {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            emailField.setError("Required.");
            valid = false;
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            passwordField.setError("Required.");
            valid = false;
        }

        return valid;
    }

    /**
     * sets user to be online on database, puts user's uid into intent and
     * starts ProfileActivity.
     * @param user the signed in user
     */
    private void goToProfile(FirebaseUser user)
    {
        Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(user.getUid())
                .child(Constants.ONLINE_KEY).setValue(true);
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra(Constants.UID_KEY, user.getUid());
        startActivity(intent);
    }

    /**
     * called when user presses on forgot password button, shows ForgotPasswordFragment
     */
    private void forgotPassword()
    {
        ForgotPasswordFragment forgotFrag = ForgotPasswordFragment.newInstance();
        forgotFrag.show(this.getSupportFragmentManager(), "forgotPasswordFrag");
    }

    /**
     * triggered when user clicks on view with onClickListener
     * @param v the view user clicked on
     */
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i == bForgot.getId())
        {
            forgotPassword();
        }
        else if (i == bLogIn.getId())
        {
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        }
    }
}
