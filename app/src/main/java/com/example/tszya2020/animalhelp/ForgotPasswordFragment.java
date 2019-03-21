package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends DialogFragment implements View.OnClickListener
{

    private final String LOG_TAG = "forgotPasswordFragment";
    private String inputEmail;
    private EditText forgotEmail;
    private Button bCancel;
    private Button bConfirm;

    public ForgotPasswordFragment()
    {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance()
    {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View baseView = inflater.inflate(R.layout.forgot_password_fragment, container, false);
        forgotEmail = baseView.findViewById(R.id.forgot_email);
        bCancel = baseView.findViewById(R.id.forgot_cancel_button);
        bConfirm = baseView.findViewById(R.id.forgot_confirm_button);

        bCancel.setOnClickListener(this);
        bConfirm.setOnClickListener(this);
        return baseView;
    }

    private boolean formFilled()
    {
        inputEmail = forgotEmail.getText().toString();
        if(TextUtils.isEmpty(inputEmail))
        {
            forgotEmail.setError("Required.");
        }
        else
        {
            return true;
        }
        return false;
    }

    private void resetPassword()
    {
        if(formFilled())
        {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(inputEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful()) {
                                Log.d(LOG_TAG, "Email sent.");
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                } catch (Exception e)
                                {
                                    Log.d(LOG_TAG, e.getMessage());
                                }
                            }
                        }
                    });

            Toast.makeText(getActivity(), "Password reset email sent.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if(id == bCancel.getId())
        {
            dismiss();
        }
        else if(id == bConfirm.getId())
        {
            //https://stackoverflow.com/questions/37209157/hide-keyboard-when-button-click-fragment
            //hide keyboard, repeated code in changePassFrag
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            resetPassword();
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}
