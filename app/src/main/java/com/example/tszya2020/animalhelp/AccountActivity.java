package com.example.tszya2020.animalhelp;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity implements FragmentChangeListener {

    //https://developer.android.com/training/basics/fragments/fragment-ui

    //DON'T MIX UP android.support.v4.app.Fragment AND android.app.Fragment BECAUSE THEY'RE DIFFERENT THINGS
    //AND THEY AREN'T INTERCHANGABLE EVEN THOUGH THEY'RE BASICALLY THE SAME THING
    //WHO MADE THIS HAPPEN AAAAHHHHHHH
    private Fragment initFragment;

    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        initFragment = new UserProfileInfo();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, initFragment).commit();

    }

    @Override
    public void onAttachFragment(Fragment newFragment) {
        if (newFragment instanceof UserProfileInfo) {
            UserProfileInfo profileFragment = (UserProfileInfo) newFragment;
            profileFragment.setFragmentChangeListener(this);
        }
    }

    public void openChat() {
        Fragment chatFragment = new ChatFragment();
        replaceFragment(chatFragment);
    }

    private void replaceFragment(Fragment newFragment)
    //THIS IS DRIVING ME INSANE WHY DOES IT REQUIRE android.app.Fragment HERE WHEN THE DOCS SAY THERE'S ONLY 1 FRAGMENT???
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
