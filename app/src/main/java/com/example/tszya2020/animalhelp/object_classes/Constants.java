package com.example.tszya2020.animalhelp.object_classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    //ranks
    public static final String DEFAULT = "Default";
    public static final String HELPER = "Helper";
    public static final String PRO = "Professional";

    //dropdown menu choices, these categories are referenced off 7Cups' list
    public static final String AGE_GROUP_DESC = "Age Groups";
    public static final String[] TARGET_AGE_GROUPS =
            {"Teens (18<)", "Young Adults (18-35)", "Adults (35+)", "Everyone"};
    public static final String CATEGORY_DESC = "Categories";
    public static final String[] CATEGORY_GROUPS =
            {"Depression", "Anxiety", "Stress", "Motivation", "ADHD", "General Mental Health", "Others"};
    public static final String LANGUAGE_DESC = "Languages";
    public static final String[] LANGUAGE_GROUPS = {"English", "Cantonese"};

    //reference related (database and shared preferences)
    public static final DatabaseReference BASE_INSTANCE = FirebaseDatabase.getInstance().getReference();
    public static final String USER_PATH = "users";
    public static final String CHAT_PATH = "chats";
    public static final String MESSAGE_PATH = "messages";
    public static final String SAVED_MESSAGE_PATH = "saved";
    public static final String REQUEST_PATH = "requests";
    public static final String DEFAULT_REQUEST = "noRequest";
    public static final String PREF_USER_INFO = "userdetails";
    public static final String UID_KEY = "uid";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String TOKEN_KEY = "token";
    public static final String ONLINE_KEY = "online";
    public static final String CHATTING_KEY = "chatting";

    //keys for Intent.putExtra
    public static final String CURRENT_USER_KEY = "currentUser";
    public static final String CHAT_ROOM_ID_KEY = "chatRoomId";
    public static final String CHECKED_STRENGTHS_KEY = "checkedKey";
    public static final String CHAT_OBJECT_KEY = "chatObject";

    //for notifications
    public static final String CHANNEL_ID = "AnimalChatID";
    public static final String CHANNEL_NAME = "AnimalChat";
    public static final String CHANNEL_DES = "temp";
    public static final int CHAT_NOTIF_ID = 1;

}