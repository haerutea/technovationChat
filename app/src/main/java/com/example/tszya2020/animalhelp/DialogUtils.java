package com.example.tszya2020.animalhelp;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {


    //https://stackoverflow.com/a/37428936
    public static ProgressDialog showProgressDialog(Context context, String message)
    {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setIndeterminate(true);
        progress.show();
        return progress;
    }

}
