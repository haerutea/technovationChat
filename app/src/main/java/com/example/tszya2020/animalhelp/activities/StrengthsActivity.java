package com.example.tszya2020.animalhelp.activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strengths_activity);

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
}
