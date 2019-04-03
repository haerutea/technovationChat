package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tszya2020.animalhelp.object_classes.Constants;

// referenced from:
// https://github.com/hieuapp/android-firebase-chat/blob/master/RivChat/app/src/main/java/com/android/rivchat/data/SharedPreferenceHelper.java

public class UserSharedPreferences
{
    //https://stackoverflow.com/a/5950109
    private static UserSharedPreferences prefInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public UserSharedPreferences()
    {
    }

    public static UserSharedPreferences getInstance(Context context)
    {
        if(prefInstance == null)
        {
            prefInstance = new UserSharedPreferences();
            sharedPreferences = context.getSharedPreferences(Constants.PREF_USER_INFO, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return prefInstance;
    }

    public void setInfo(String key, String data)
    {
        editor.putString(key, data);
        editor.apply();
    }

    public String getStringInfo(String key)
    {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolInfo(String key)
    {
        return sharedPreferences.getBoolean(key, true);
    }
}
