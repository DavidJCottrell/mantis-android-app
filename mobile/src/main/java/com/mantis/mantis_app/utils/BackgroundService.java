package com.mantis.mantis_app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mantis.mantis_app.entities.Invitation;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {
    public BackgroundService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private final int REQUEST_TIMER = 5; // Time between API requests (in seconds)

    private void createInvitationNotification(Invitation newInvitations){

        // Oreo requires notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Invitation Notification", "Invitation Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Invitation Notification");
        builder.setContentTitle(String.valueOf("New Project Invitation"));
        builder.setContentText("Project: " + newInvitations.project.get("title"));
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setAutoCancel(true);

        builder.extend(new NotificationCompat.WearableExtender().setBridgeTag("foo"));

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        RequestQueue queue = Volley.newRequestQueue(this);

        String token = intent.getStringExtra("token");

        // Continuously check API for new invitations
        Thread t1 = new Thread(() -> {
            while(true){
                try {
                    Log.i("Background Service", "Checking API...");
                    // Invitations API Request
                    JsonObjectRequest invitationRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            "http://10.0.2.2:9000/users/invitations",
                            new JSONObject(),
                            result -> {
                                // Make sure the currentUser singleton has been initialised
                                if(User.currentUser != null){
                                    // Make sure the invitations list has been initialised
                                    if(User.currentUser.invitations != null){
                                        // Parse JSON to Invitation objects
                                        List<Invitation> remoteInvitationList = Invitation.parseInvitationsData(result);
                                        // There are new invitations that have not yet been updated on the phone
                                        if(remoteInvitationList.size() > User.currentUser.invitations.size()){
                                            Log.i("Background Service", "Found new invitation.");
                                            Invitation newInvitation = remoteInvitationList.get(remoteInvitationList.size()-1);
                                            createInvitationNotification(newInvitation); // Notify user of new invitation
                                            User.currentUser.setInvitations(remoteInvitationList); // Update application data
                                        }
                                    }
                                }
                            },
                            error -> {}
                    )
                    {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/json");
                            headers.put("auth-token", token);
                            return headers;
                        }
                    };

                    queue.add(invitationRequest);

                    // Repeat every REQUEST_TIMER seconds
                    TimeUnit.SECONDS.sleep(REQUEST_TIMER);

                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });

        t1.start();

        return START_STICKY;
    }
}