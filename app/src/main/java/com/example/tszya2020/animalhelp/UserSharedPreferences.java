package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tszya2020.animalhelp.object_classes.Constants;

// referenced from:
// https://github.com/hieuapp/android-firebase-chat/blob/master/RivChat/app/src/main/java/com/android/rivchat/data/SharedPreferenceHelper.java

/**
 * own class to handle SharedPreferences to save and get info
 */
public class UserSharedPreferences
{
    //https://stackoverflow.com/a/5950109
    private static UserSharedPreferences prefInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * needed empty constructor
     */
    public UserSharedPreferences()
    {
    }

    /**
     * acts like a constructor, creates and returns instance of this class,
     * also sets sharedPreferences and editor fields
     * @param context context of where this is called
     * @return new instance of UserSharedPreferences.
     */
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

    /**
     * puts string into editor and applies it to make changes and add data to sharedpreferences.
     * @param key String, key of info
     * @param data String, value of info
     */
    public void setInfo(String key, String data)
    {
        editor.putString(key, data);
        editor.apply();
    }

    /**
     * gets the String with the corresponding key, default value is an empty string
     * @param key String, key of needed String.
     * @return value with the corresponding key
     */
    public String getStringInfo(String key)
    {
        return sharedPreferences.getString(key, "");
    }
}
