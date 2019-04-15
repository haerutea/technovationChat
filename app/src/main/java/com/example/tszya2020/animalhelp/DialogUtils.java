package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * utils class to create and show ProgressDialogs easier
 */
public class DialogUtils {

    //https://stackoverflow.com/a/37428936

    /**
     * creates and shows new progress dialog, then returns the object so it can be dismissed.
     * @param context context of where it'll show up
     * @param message message to be shown on dialog
     * @return the created ProgressDialog object so it can be dismissed
     */
    public static ProgressDialog showProgressDialog(Context context, String message)
    {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        return progress;
    }

}
