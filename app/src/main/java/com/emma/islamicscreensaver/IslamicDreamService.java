package com.emma.islamicscreensaver;

import android.service.dreams.DreamService;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IslamicDreamService extends DreamService {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView clockView, dateView, duaView, dhikrView;

    private final String[] duas = {
        "اللَّهُمَّ اهْدِنِي وَسَدِّدْنِي\nAllahumma ihdini wa saddidni\nYa Allah, berilah aku petunjuk dan keteguhan.",
        "رَبِّ زِدْنِي عِلْمًا\nRabbi zidni ilma\nWahai Tuhanku, tambahkanlah ilmuku.",
        "اللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي\nAllahummaghfir li warhamni\nYa Allah, ampunilah dan rahmatilah aku."
    };

    private final String[] dhikr = {
        "سُبْحَانَ اللَّهِ • Subhanallah",
        "الْحَمْدُ لِلَّهِ • Alhamdulillah",
        "اللَّهُ أَكْبَرُ • Allahu Akbar",
        "أَسْتَغْفِرُ اللَّهَ • Astaghfirullah"
    };

    private int duaIndex = 0;
    private int dhikrIndex = 0;

    private final Runnable ticker = new Runnable() {
        @Override
        public void run() {
            if (clockView != null) {
                Date now = new Date();
                clockView.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
                dateView.setText(new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(now));
                handler.postDelayed(this, 1000);
            }
        }
    };

    private final Runnable rotateContent = new Runnable() {
        @Override
        public void run() {
            if (duaView != null) {
                duaView.setText(duas[duaIndex]);
                dhikrView.setText(dhikr[dhikrIndex]);
                duaIndex = (duaIndex + 1) % duas.length;
                dhikrIndex = (dhikrIndex + 1) % dhikr.length;
                handler.postDelayed(this, 30000);
            }
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(false);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(48, 24, 48, 24);
        layout.setBackgroundColor(Color.rgb(3, 10, 8));

        clockView = makeText(72, Color.rgb(221, 184, 100));
        dateView = makeText(24, Color.LTGRAY);
        duaView = makeText(25, Color.WHITE);
        dhikrView = makeText(23, Color.rgb(102, 210, 160));

        layout.addView(clockView);
        layout.addView(dateView);

        TextView heading = makeText(24, Color.rgb(221, 184, 100));
        heading.setText("\nDOA HARIAN\n");
        layout.addView(heading);
        layout.addView(duaView);

        TextView dhikrHeading = makeText(22, Color.rgb(221, 184, 100));
        dhikrHeading.setText("\nZIKIR\n");
        layout.addView(dhikrHeading);
        layout.addView(dhikrView);

        setContentView(layout);
        ticker.run();
        rotateContent.run();
    }

    private TextView makeText(int size, int color) {
        TextView view = new TextView(this);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        view.setPadding(12, 8, 12, 8);
        return view;
    }

    @Override
    public void onDreamingStopped() {
        handler.removeCallbacks(ticker);
        handler.removeCallbacks(rotateContent);
        super.onDreamingStopped();
    }
}
