package com.example.tszya2020.animalhelp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tszya2020.animalhelp.R;
import com.example.tszya2020.animalhelp.StrengthsAdapter;
import com.example.tszya2020.animalhelp.object_classes.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class StrengthsActivity extends AppCompatActivity
{
    private ArrayList<String> allStrengths;
    private StrengthsAdapter strengthsAdapter;
    private RecyclerView recyclerView;

    /**
     * when activity is first opened, set content from strengths_activity.xml,
     * call show disclaimer to show dialog window, then add contents of recycler view
     * to field, assign view to field, create new StrengthsAdapter with the contents,
     * create layout manager and set adapter.
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strengths_activity);

        showDisclaimer();
        allStrengths = new ArrayList<>();
        allStrengths.add(Constants.AGE_GROUP_DESC);
        allStrengths.addAll(Arrays.asList(Constants.TARGET_AGE_GROUPS));
        allStrengths.add(Constants.CATEGORY_DESC);
        allStrengths.addAll(Arrays.asList(Constants.CATEGORY_GROUPS));
        allStrengths.add(Constants.LANGUAGE_DESC);
        allStrengths.addAll(Arrays.asList(Constants.LANGUAGE_GROUPS));
        allStrengths.add("BUTTON");

        recyclerView = findViewById(R.id.strengths_recycler_view);

        strengthsAdapter = new StrengthsAdapter(allStrengths);

        // vertical RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(strengthsAdapter);
    }

    /**
     *  shows diaclaimer dialog and force user to press 'I agree' button before proceeding
     */
    private void showDisclaimer()
    {
        //https://developer.android.com/guide/topics/ui/dialogs.html#java
        String disclaimer = "This app isn\'t tended for emergency purposes, but rather simple and " +
                "emotionally-easing chats with anonymous strangers.  If you're in a critical/dangerous" +
                "situation, you should call emergency services or close ones to seek guidance instead.";
        AlertDialog.Builder disclaimerBuilder = new AlertDialog.Builder(this);
        disclaimerBuilder.setMessage(disclaimer)
                .setTitle("PLEASE READ THIS")
                .setMessage(disclaimer)
                .setNeutralButton("I agree", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //nothing bcs user has to click on this to continue
                    }
                }).setCancelable(false)
                .create()
                .show();
    }
}
