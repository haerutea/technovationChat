package com.example.tszya2020.animalhelp;

import android.content.Context;
import android.content.SharedPreferences;

// referenced from:
// https://github.com/hieuapp/android-firebase-chat/blob/master/RivChat/app/src/main/java/com/android/rivchat/data/SharedPreferenceHelper.java

public class UserSharedPreferences
{
    //https://stackoverflow.com/a/5950109
    private static UserSharedPreferences prefInstance;
    private static String pref_user_info = "userdetails";
    private static String user_uid = "uid";
    private static String user_username = "username";
    private static String user_email = "email";
    private static String user_rank = "rank";
    private static String user_online = "onlineStatus";
    private static String user_chatting = "chattingStatus";
    private static String user_chatlog = "chatlog";
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
            sharedPreferences = context.getSharedPreferences(pref_user_info, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return prefInstance;
    }

    public void saveUserInfo(User user)
    {
        editor.putString(user_uid, user.getUid());
        editor.putString(user_username, user.getUsername());
        editor.putString(user_email, user.getEmail());
        editor.putString(user_rank, user.getRank());
        editor.putBoolean(user_online, user.getOnline());
        editor.putBoolean(user_chatting, user.getChatting());
        //TODO: FIX THIS SO THAT IT WOULD SHOW UP IN DBvvv
        //https://www.programmergate.com/convert-java-object-to-json/
        //editor.putString(user_chatlog, user.getChatlog());
        editor.apply();
    }

    public User getUserInfo()
    {
        String uid = sharedPreferences.getString(user_uid, "");
        String username = sharedPreferences.getString(user_username, "");
        String email = sharedPreferences.getString(user_email, "");
        String rank = sharedPreferences.getString(user_rank, "");
        boolean online = sharedPreferences.getBoolean(user_online, true);
        boolean chatting = sharedPreferences.getBoolean(user_chatting, false);

        return new User(uid, username, email, rank, online, chatting);
    }
}
