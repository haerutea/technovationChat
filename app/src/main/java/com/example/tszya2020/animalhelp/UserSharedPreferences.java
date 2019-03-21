package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void saveUserInfo(User user)
    {
        editor.putString(Constants.UID_KEY, user.getUid());
        editor.putString(Constants.USERNAME_KEY, user.getUsername());
        editor.putString(Constants.EMAIL_KEY, user.getEmail());
        editor.putString(Constants.RANK_KEY, user.getRank());
        editor.putString(Constants.TOKEN_KEY, user.getToken());
        editor.putBoolean(Constants.ONLINE_KEY, user.getOnline());
        editor.putBoolean(Constants.CHATTING_KEY, user.getChatting());
        //https://www.programmergate.com/convert-java-object-to-json/
        //editor.putString(Constants.USER_CHATLOG, user.getChatlog());
        editor.apply();
    }

    public void setToken(String inToken)
    {
        editor.putString(Constants.TOKEN_KEY, inToken);
        editor.apply();
    }
/*
    public User getUserInfo()
    {
        String uid = sharedPreferences.getString(Constants.UID_KEY, "");
        String username = sharedPreferences.getString(Constants.USERNAME_KEY, "");
        String email = sharedPreferences.getString(Constants.EMAIL_KEY, "");
        String rank = sharedPreferences.getString(Constants.RANK_KEY, "");
        String token = sharedPreferences.getString(Constants.TOKEN_KEY, "");
        boolean online = sharedPreferences.getBoolean(Constants.ONLINE_KEY, true);
        boolean chatting = sharedPreferences.getBoolean(Constants.CHATTING_KEY, false);

        return new User(uid, username, email, rank, token, online, chatting);
    }*/
}
