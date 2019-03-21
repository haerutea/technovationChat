package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;


public class ChangePassswordFragment extends DialogFragment implements View.OnClickListener
{
    private final String LOG_TAG = "changePasswordFragment";
    private EditText newPass;
    private EditText reNewPass;
    private EditText currentPass;
    private Button confirm;
    private Button cancel;

    private String password;
    private String rePassword;
    private String currentPassword;

    public ChangePassswordFragment()
    {
        // Required empty public constructor
    }

    public static ChangePassswordFragment newInstance()
    {
        return new ChangePassswordFragment();
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
        View baseView =  inflater.inflate(R.layout.change_password_fragment, container, false);
        newPass = baseView.findViewById(R.id.new_password);
        reNewPass = baseView.findViewById(R.id.re_new_password);
        currentPass = baseView.findViewById(R.id.current_password);
        confirm = baseView.findViewById(R.id.change_confirm_button);
        cancel = baseView.findViewById(R.id.change_cancel_button);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return baseView;
    }

    private boolean formFilled()
    {
        password = newPass.getText().toString();
        rePassword = reNewPass.getText().toString();
        currentPassword = currentPass.getText().toString();
        //if password is less than 6 characters long
        if(TextUtils.isEmpty(password))
        {
            newPass.setError("Required.");
        }
        else if(TextUtils.isEmpty(rePassword))
        {
            reNewPass.setError("Required.");
        }
        else if(TextUtils.isEmpty(currentPassword))
        {
            currentPass.setError("Required.");
        }
        else if(password.length() < 6)
        {
            newPass.setError("Password needs to be longer than 6.");
        }
        //if password contains anything other than that
        else if(!password.matches("([A-Za-z0-9])+"))
        {
            newPass.setError("Only alphabet and digits please.");
        }
        //if the 2 fields don't equal
        else if(!password.equals(rePassword))
        {
            reNewPass.setError("Passwords don't match.");
        }
        else
        {
            return true;
        }
        return false;
    }

    private void confirmChange()
    {
        //do the passwords in 2 fields match
        //otherwise
        if(formFilled())
        {
            ProgressDialog loading = DialogUtils
                    .showProgressDialog(getActivity(), "Attempting to update password...");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Log.d(LOG_TAG, "User password updated.");
                                dismiss();
                                Toast.makeText(getActivity(), "update password success", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                //https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user
                                //TODO: re-authenticate user
                                catch (FirebaseAuthRecentLoginRequiredException e)
                                {
                                    reauthenticateAndChange();
                                }
                                catch (Exception e)
                                {
                                    Log.d(LOG_TAG, e.getMessage());
                                }
                            }
                        }
                    });
            loading.dismiss();
        }
    }

    private void reauthenticateAndChange()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), currentPassword);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Log.d(LOG_TAG, "User re-authenticated.");
                            confirmChange();
                        }
                        else
                        {
                            try
                            {
                                throw task.getException();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(LOG_TAG, e.getMessage());
                            }
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == confirm.getId())
        {
            //https://stackoverflow.com/questions/37209157/hide-keyboard-when-button-click-fragment
            //hide keyboard
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            confirmChange();
        }
        else if(id == cancel.getId())
        {
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
