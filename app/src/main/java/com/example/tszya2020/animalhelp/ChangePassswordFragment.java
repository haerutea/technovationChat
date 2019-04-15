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

/**
 * shown when user presses on change password button in settings, allows user to set their new password
 */
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

    /**
     * required empty constructor
     */
    public ChangePassswordFragment()
    {
        // Required empty public constructor
    }

    /**
     * creates new object/instance of this class
     * @return new object of this class
     */
    public static ChangePassswordFragment newInstance()
    {
        return new ChangePassswordFragment();
    }

    /**
     * called when fragment is first created
     * @param savedInstanceState data saved from onSaveInstanceState, not used
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * called to instantiate views onto layout and assign views to fields
     * @param inflater used to inflate views on fragment
     * @param container the parent of where this frag wioll be shown
     * @param savedInstanceState not used.
     * @return view for this fragment
     */
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

    /**
     * checks if form is filled in correctly
     * @return true or false depending on status
     */
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

    /**
     * called when user presses confirm, calls formFilled to check,
     * calls built-in updatePassword method from FirebaseUser,
     * if successful, dismiss progress dialog and Toast success message,
     * if not, call reauthenticateAndChange method if it's a re-auth issue,
     * else log the error message.
     */
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

    /**
     * reauthenticates user and tries to updatePassword again by calling confirmChange
     * if re-auth was successful.  If not, Toast error message to user
     */
    private void reauthenticateAndChange()
    {
        //https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

    /**
     * triggered when user clicks on a button with onClickListener
     * @param v the view the user clicked on
     */
    @Override
    public void onClick(View v)
    {
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
