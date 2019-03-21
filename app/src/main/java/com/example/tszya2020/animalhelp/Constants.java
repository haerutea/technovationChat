package com.example.tszya2020.animalhelp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    //ranks
    public static final String DEFAULT = "Default";
    public static final String HELPER = "Helper";
    public static final String PRO = "Professional";

    //reference related (database and shared preferences)
    public static final DatabaseReference BASE_INSTANCE = FirebaseDatabase.getInstance().getReference();
    public static final String USER_PATH = "users";
    public static final String CHAT_PATH = "chats";
    public static final String NOTIF_TOKEN_PATH = "notification_token";
    public static final String REQUEST_PATH = "requests";
    public static final String DEFAULT_REQUEST = "noRequest";
    public static final String PREF_USER_INFO = "userdetails";
    public static final String UID_KEY = "uid";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String RANK_KEY = "rank";
    public static final String TOKEN_KEY = "token";
    public static final String ONLINE_KEY = "online";
    public static final String CHATTING_KEY = "chatting";

    //keys for Intent.putExtra
    public static final String CURRENT_USER_KEY = "currentUser";
    public static final String OPPOSING_USER_KEY = "opposingUser";
    public static final String CHAT_ROOM_ID_KEY = "chatRoomId";

    //for notifications
    public static final String CHANNEL_ID = "AnimalChatID";
    public static final String CHANNEL_NAME = "AnimalChat";
    public static final String CHANNEL_DES = "temp";
    public static final int CHAT_NOTIF_ID = 1;
    public static String notifToken = "";

    //user details
    public static final String USER_UID = "";

}