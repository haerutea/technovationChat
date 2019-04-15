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

/**
 * fragment shown when user presses on forgot password in LoginActivity
 */
public class ForgotPasswordFragment extends DialogFragment implements View.OnClickListener
{

    private final String LOG_TAG = "forgotPasswordFragment";
    private String inputEmail;
    private EditText forgotEmail;
    private Button bCancel;
    private Button bConfirm;

    /**
     * required empty constructor
     */
    public ForgotPasswordFragment()
    {
        // Required empty public constructor
    }

    /**
     * creates new instance/object of this class
     * @return new instance of this class
     */
    public static ForgotPasswordFragment newInstance()
    {
        return new ForgotPasswordFragment();
    }

    /**
     * called when this is first created
     * @param savedInstanceState save data from previous opens, not used
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * called to instantiate views onto layout and assign views to fields
     * @param inflater used to inflate views on fragment
     * @param container the parent of where this frag will be shown
     * @param savedInstanceState not used.
     * @return view for this fragment
     */
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

    /**
     * checks if form was filled in correctly
     * @return status of true or false
     */
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

    /**
     * checks if form was filled, then calls built-in method which sends user a
     * password reset email.  If successful, toast message.
     */
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
                            if (task.isSuccessful())
                            {
                                Log.d(LOG_TAG, "Email sent.");
                                Toast.makeText(getActivity(),
                                        "Password reset email sent.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    Log.d(LOG_TAG, e.getMessage());
                                }
                            }
                        }
                    });
        }
    }

    /**
     * triggered when user clicks on buttons with onClickListeners
     * @param v view the user clicked on
     */
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

    /**
     * called when fragment is first attached, has to be overridden
     * @param context the context it'll be attached to
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    /**
     * called when fragment isn't attached to activity anymore
     */
    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}
