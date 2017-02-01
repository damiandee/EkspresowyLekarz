package praca_inzynierska.damian_deska.ekspresowylekarz.Controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import praca_inzynierska.damian_deska.ekspresowylekarz.Controller.Activities.MainActivity;
import praca_inzynierska.damian_deska.ekspresowylekarz.R;

/**
 * Created by Damian Deska on 2017-01-20.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /*funkcja odpowiedzialna za dzialania wykonywane w przypadku otrzymania wiadomosci z serwisu Firebase*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        /*sprawdzenie, czy wiadomosc zawiera dodatkowe informacje*/
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        /*sprawdzenie, czy wiadomosc zawiera powiadomienie*/
        if (remoteMessage.getNotification() != null) {
            /*dodanie informacji o powiadomieniu do intencji i uruchomienie glownej aktywnosci; uruchmiania jest ona w przypadku
            * klikniecia przez pacjenta na powiadomienie*/
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("title", remoteMessage.getNotification().getTitle());
            i.putExtra("body", remoteMessage.getNotification().getBody());
            startActivity(i);
        }
    }

}
