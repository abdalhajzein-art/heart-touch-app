package com.hearttouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView statusText;
    private Button touchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        touchBtn = findViewById(R.id.touchBtn);

        // تشغيل الخدمة بالخلفية فور فتح التطبيق
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        startService(serviceIntent);

        // لمس مستمر → إرسال START
        touchBtn.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                statusText.setText("🔴 يتم إرسال اللمسة…");

                Intent i = new Intent(this, ForegroundService.class);
                i.setAction("START_TOUCH");
                startService(i);
            }

            if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {

                statusText.setText("🟢 توقفت اللمسة");

                Intent i = new Intent(this, ForegroundService.class);
                i.setAction("STOP_TOUCH");
                startService(i);
            }

            return true;
        });
    }
}
