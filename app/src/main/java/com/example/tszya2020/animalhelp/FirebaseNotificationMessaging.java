package com.example.tszya2020.animalhelp;

import android.util.Log;

import com.example.tszya2020.animalhelp.activities.ConfirmActivity;
import com.example.tszya2020.animalhelp.activities.ProfileActivity;
import com.example.tszya2020.animalhelp.object_classes.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * extends FirebsaeMessageService, this class handles the message sent to this device from
 * Cloud Function.  Also handles new device tokens.
 */
public class FirebaseNotificationMessaging extends FirebaseMessagingService
{

    //this whole class is basically from the quickstart file by Firebase.
    //https://github.com/firebase/quickstart-android/blob/4017aac2bdc591dc8b9702953702f09921a4e76d/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/java/MyFirebaseMessagingService.java
    private final String logTag = "messagingSerivce";
    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(logTag, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(logTag, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {
            Log.d(logTag, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Class destinationClass = null;
            boolean special = false;
            if(remoteMessage.getNotification().getTitle().equals("You have a new chat request!"))
            {
                destinationClass = ConfirmActivity.class;
                special = true;
            }
            else if(remoteMessage.getNotification().getTitle().equals("Deleted request"))
            {
                destinationClass = ProfileActivity.class;
            }

            NotificationSender.setNotif(this, destinationClass,
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(), special);
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(logTag, "new token: " + token);
        String uid = getSharedPreferences(Constants.PREF_USER_INFO, MODE_PRIVATE)
                .getString(Constants.UID_KEY, "");
        //change token value in database
        if(uid != null)
            Constants.BASE_INSTANCE.child(Constants.USER_PATH).child(uid)
                    .child(Constants.TOKEN_KEY).child(token).setValue(true);
        //change token value in sharedPref
        UserSharedPreferences.getInstance(this).setInfo(Constants.TOKEN_KEY, token);
    }
}