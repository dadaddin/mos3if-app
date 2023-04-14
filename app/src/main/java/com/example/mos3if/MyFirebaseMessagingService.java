package com.example.mos3if;


import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

/*
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        // Get the current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Save the device token to the Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(userId).child("deviceToken").setValue(token)
                .addOnSuccessListener(aVoid -> {
                    Log.e("TAG", "Device token saved to database");
                })
                .addOnFailureListener(e -> {
                    Log.e("Tag", "Error saving device token to database", e);
                });
    }*/

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Extract the message data payload
        Map<String, String> data = remoteMessage.getData();

        // Get the notification title and message
        String title = data.get("title");
        String message = data.get("message");

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}
