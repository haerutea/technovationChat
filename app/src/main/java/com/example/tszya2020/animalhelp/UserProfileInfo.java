package com.example.tszya2020.animalhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileInfo extends Fragment
        implements View.OnClickListener
{

    //changing from fragment to another: https://developer.android.com/training/basics/fragments/communicating
    private FragmentChangeListener mCallback;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    //UI references
    private TextView username;
    private TextView email;
    private Button chat;
    private Button logout;

    public UserProfileInfo()
    {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View profileView = inflater.inflate(R.layout.profile_fragment, container, false);
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        username = profileView.findViewById(R.id.profile_username);
        email = profileView.findViewById(R.id.profile_email);
        chat = profileView.findViewById(R.id.chat_button);
        logout = profileView.findViewById(R.id.log_out_button);

        if(user!= null)
        {
            username.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }

        chat.setOnClickListener(this);
        logout.setOnClickListener(this);

        return profileView;
    }

    //https://developer.android.com/training/basics/fragments/communicating
    public void setFragmentChangeListener(Activity activity)
    {
        mCallback = (FragmentChangeListener) activity;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if(id == chat.getId())
        {
            super.onDetach();
            mCallback.openChat();
        }
        else if(id == logout.getId())
        {
            super.onDetach();
            //mCallback.logOut();
            mAuth.signOut();
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
        }
    }
}
