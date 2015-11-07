package is.pedals.datatracker;

import android.content.Context;
import android.net.TrafficStats;
import android.text.format.Formatter;

public class AppStats implements Comparable {
    public final CharSequence name;
    public final int uid;
    private final Context context;

    public AppStats(Context context, CharSequence name, int uid) {
        this.context = context;
        this.name = name;
        this.uid = uid;
    }

    public long getBytes() {
        return TrafficStats.getUidRxBytes(uid);
    }


    public String getTotalTraffic() {
        return Formatter.formatFileSize(context, getBytes());
    }

    @Override
    public String toString() {
        return name + "  " + Formatter.formatFileSize(context, getBytes());
    }

    @Override
    public int compareTo(Object o) {
        return name.toString().compareTo(o.toString());
    }
}
