package is.pedals.datatracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ApplicationView extends AppCompatActivity {
    private long starttime;
    private long prevtime;
    private long startbytes;
    private long prevbytes;
    private AppStats app;
    private TextView textView;
    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long bytes = app.getBytes();
            long time = SystemClock.elapsedRealtime();
            long bytesSinceStart = bytes - startbytes;
            long bytesSincePrev = bytes - prevbytes;
            long elapsedTimeSincePrev = time - prevtime;
            long elapsedTimeSinceStart = time - starttime;
            double rateSincePrev = 0, rateSinceStart = 0;
            try {
                rateSincePrev = (bytesSincePrev / (1024 ^ 2)) / (elapsedTimeSincePrev / 1000);
                rateSinceStart = (bytesSinceStart / (1024 ^ 2)) / (elapsedTimeSinceStart / 1000);
            } catch (ArithmeticException e) {
            }
            String timeElapsed = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeSinceStart) + ":" + TimeUnit.MILLISECONDS.toSeconds((elapsedTimeSinceStart-TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(elapsedTimeSinceStart))));
            String format = "%s %s@%.2fkb/s (%s@%.2fkb/s since start)\nAveraging %s in an hour\nTime elapsed: %s";
            String out = String.format(format, app.name, Formatter.formatFileSize(getBaseContext(), bytesSincePrev), rateSincePrev, Formatter.formatFileSize(getBaseContext(), bytesSinceStart), rateSinceStart, Formatter.formatFileSize(getBaseContext(), (long) ((double) bytesSinceStart / elapsedTimeSinceStart) * 3600000), timeElapsed);
            textView.setText(out);
            prevbytes = app.getBytes();
            prevtime = SystemClock.elapsedRealtime();
            handler.postDelayed(runnable, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_view);
        Intent intent = getIntent();
        String name = intent.getStringExtra("appname");
        int uid = intent.getIntExtra("uid", -1);
        app = new AppStats(this, name, uid);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(app.toString());
        starttime = SystemClock.elapsedRealtime();
        prevtime = starttime;
        startbytes = app.getBytes();
        prevbytes = startbytes;
        runnable.run();
    }

}
