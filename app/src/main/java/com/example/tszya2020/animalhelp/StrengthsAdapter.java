package com.example.tszya2020.animalhelp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//code referenced from: https://c1ctech.com/android-checkedtextview-example/
// and https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView
public class StrengthsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public class StrengthsTextHolder extends RecyclerView.ViewHolder
    {
        private CheckedTextView strengthsCheckView;

        public StrengthsTextHolder(View view)
        {
            super(view);
            strengthsCheckView = view.findViewById(R.id.strengths_check_view);
        }
    }

    public class StrengthsDescription extends RecyclerView.ViewHolder
    {
        private TextView descriptionView;

        public StrengthsDescription(View view)
        {
            super(view);
            descriptionView = view.findViewById(R.id.strength_desc);
        }
    }

    public class NextButton extends RecyclerView.ViewHolder
    {
        private Button nextButton;

        public NextButton(final View view)
        {
            super(view);
            nextButton = view.findViewById(R.id.next_button);
            nextButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(v.getId() == nextButton.getId())
                    {
                        if(checked.size() == 0)
                        {
                            Toast.makeText(view.getContext(), "You have to select at least one.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                            //https://stackoverflow.com/questions/28528009/start-new-intent-from-recyclerviewadapter
                            Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                            intent.putExtra(Constants.CHECKED_STRENGTHS_KEY, checked);
                            view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    private ArrayList<String> allStrengths;
    private ArrayList<String> checked;
    private final int DESC = 1;
    private final int BUTTON = 2;

    public StrengthsAdapter(ArrayList<String> inStrengths)
    {
        this.allStrengths = inStrengths;
        checked = new ArrayList<>();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position)
    {
        String temp = allStrengths.get(position);
        if (temp.equals(Constants.AGE_GROUP_DESC) ||
                temp.equals(Constants.CATEGORY_DESC)
                || temp.equals(Constants.LANGUAGE_DESC))
        {
            return DESC;
        }
        else if(temp.equals("BUTTON"))
        {
            return BUTTON;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case DESC:
                view = inflater.inflate(R.layout.strengths_description, parent, false);
                viewHolder = new StrengthsDescription(view);
                break;
            case BUTTON:
                view = inflater.inflate(R.layout.strengths_next_button, parent, false);
                viewHolder = new NextButton(view);
                break;
            default:
                view = inflater.inflate(R.layout.strengths_check, parent, false);
                viewHolder = new StrengthsTextHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        switch (viewHolder.getItemViewType())
        {
            case DESC:
                configDescription((StrengthsDescription) viewHolder, position);
                break;
            case BUTTON:
                configButton((NextButton) viewHolder, position);
                break;
            default:
                configStrengthsText((StrengthsTextHolder) viewHolder, position);
                break;
        }
    }

    private void configDescription(StrengthsDescription descriptionHolder, int position)
    {
        descriptionHolder.descriptionView.setText(allStrengths.get(position));
    }

    private void configButton(NextButton buttonHolder, int position)
    {
        Button bNext = buttonHolder.nextButton;
    }

    private void configStrengthsText(StrengthsTextHolder strengthsHolder, int position)
    {
        final CheckedTextView checkView = strengthsHolder.strengthsCheckView;
        checkView.setText(allStrengths.get(position));

        // perform on Click Event Listener on CheckedTextView
        checkView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean value = checkView.isChecked();
                if (value)
                {
                    //uncheck
                    checkView.setCheckMarkDrawable(R.drawable.unchecked_box_icon);
                    checkView.setChecked(false);
                    checked.remove(checkView.getText().toString());
                    Log.d("checked", checked.toString());
                }
                else
                {
                    //check
                    checkView.setCheckMarkDrawable(R.drawable.check);
                    checkView.setChecked(true);
                    checked.add(checkView.getText().toString());
                    Log.d("checked", checked.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return allStrengths.size();
    }

    public ArrayList<String> getChecked()
    {
        return this.checked;
    }

}
