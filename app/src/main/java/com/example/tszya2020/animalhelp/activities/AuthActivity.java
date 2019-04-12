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

/**
 * the activity where user begins when they first open up the app on fresh download.
 * Also where the user will end up when logging out.
 * Has buttons to signup and login
 */
public class AuthActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String LOG_TAG = "AuthActivity";
    private FirebaseAuth mAuth;

    private Button bSignUp;
    private Button bLogin;

    /**
     * when activity is first opened, sets content from auth_activity.xml layout,
     * sets mAuth value, assigns buttons to viewIds, checks for google play services
     * @param savedInstanceState data saved from onSaveInstanceState
     */
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

    /**
     * whenever app is opened, if there is already a user logged in,
     * call updateUI with that user.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.d(LOG_TAG, "update UI started with user in onStart()");
            updateUI(currentUser);
    }

    /**
     * triggered when a button is clicked
     * @param v is the view that was clicked on
     */
    public void onClick(View v)
    {
        int id = v.getId();
        //if the user pressed on sign up
        if (id == bSignUp.getId())
        {
            Intent intent = new Intent(getApplicationContext(), StrengthsActivity.class);
            startActivity(intent);
        }
        //if user pressed on login
        else if(id == bLogin.getId())
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * opens up ProfileActivity directly is user is present
     * @param user is the user that's logged in
     */
    private void updateUI(FirebaseUser user)
    {
        //show a simple loading popup
        ProgressDialog loadingWindow = DialogUtils.showProgressDialog(this, "loading");
        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(Constants.UID_KEY, user.getUid());
            startActivity(intent);
        }
        //get rid of popup
        loadingWindow.dismiss();
    }
}
