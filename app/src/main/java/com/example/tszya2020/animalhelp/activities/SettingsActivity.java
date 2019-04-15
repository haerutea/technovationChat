package com.example.tszya2020.animalhelp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tszya2020.animalhelp.ChangePassswordFragment;
import com.example.tszya2020.animalhelp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * opens when user clicks Settings button in Profile, allows user to change password.
 * change medication button exists but is not yet implemented due to lack of access to
 * Whitney's code/logic.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private Button changePassword;
    private Button changeMedication;

    /**
     * when activity is first opened, set content from settings_activity.xml,
     * assign views to fields, sets change password button to enabled or disabled
     * depending if they verified email or not.  change medication is disabled until
     * I merge Whitney's app with mine.  Adds onClickListener to password button
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        changeMedication = findViewById(R.id.change_medication_button);
        changeMedication.setEnabled(false);
        changePassword = findViewById(R.id.change_password_button);
        if(!mUser.isEmailVerified())
        {
            changePassword.setEnabled(false);
        }
        changePassword.setOnClickListener(this);
    }

    /**
     * called when user clicks on password button, shows changePasswordFragment
     */
    public void openPassword()
    {
        ChangePassswordFragment passFrag = ChangePassswordFragment.newInstance();
        passFrag.show(this.getSupportFragmentManager(), "passwordFragment");
    }

    /**
     * triggers when user clicks on view with onClickListener
     * @param v view user clicked on
     */
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i == changePassword.getId())
        {
            openPassword();
        }
    }
}