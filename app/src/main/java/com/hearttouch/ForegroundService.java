package com.hearttouch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "heart_touch_channel";

    // Topics
    private static final String SEND_TOPIC = "abd_to_partner_2026";
    private static final String RECV_TOPIC = "partner_to_abd_2026";

    private final OkHttpClient client = new OkHttpClient();
    private WebSocket ws;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create Notification Channel
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heart Touch",
                NotificationManager.IMPORTANCE_LOW
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Foreground Notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Heart Touch يعمل بالخلفية")
                .setContentText("يستقبل نبضات من شريكتك ❤️")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        startForeground(1, notification);

        // Connect WebSocket to receive pulses
        connectWebSocket(RECV_TOPIC);
    }

    // -----------------------------
    // SEND TO NTFY
    // -----------------------------
    private void sendToNtfy(String topic, String message) {

        Request request = new Request.Builder()
                .url("https://ntfy.sh/" + topic)
                .post(RequestBody.create(message, MediaType.parse("text/plain")))
                .addHeader("Title", message)
                .addHeader("X-Push", "yes")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) {}
        });
    }

    // -----------------------------
    // RECEIVE FROM NTFY (WebSocket)
    // -----------------------------
    private void connectWebSocket(String topic) {

        Request request = new Request.Builder()
                .url("wss://ntfy.sh/" + topic + "/ws")
                .build();

        ws = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onMessage(WebSocket webSocket, String text) {

                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                if (text.contains("START")) {
                    vib.vibrate(VibrationEffect.createWaveform(
                            new long[]{0, 300, 100, 300},
                            -1
                    ));
                }

                if (text.contains("STOP")) {
                    vib.cancel();
                }
            }
        });
    }

    // -----------------------------
    // HANDLE START/STOP FROM UI
    // -----------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && intent.getAction() != null) {

            if (intent.getAction().equals("START_TOUCH")) {
                sendToNtfy(SEND_TOPIC, "START");
            }

            if (intent.getAction().equals("STOP_TOUCH")) {
                sendToNtfy(SEND_TOPIC, "STOP");
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
