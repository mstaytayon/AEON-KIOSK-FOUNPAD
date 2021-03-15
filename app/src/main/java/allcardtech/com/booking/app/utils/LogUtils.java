package allcardtech.com.booking.app.utils;

import android.util.Log;


public class LogUtils {
    private static boolean enabled = false;

    public LogUtils() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        LogUtils.enabled = enabled;
    }

    public static void d(String tag, String msg) {
        if (enabled) {
            Log.d(tag, msg == null ? "" : msg);
        }

    }

    public static void e(String tag, String msg) {
        if (enabled) {
            Log.e(tag, msg == null ? "" : msg);
        }

    }

    public static void i(String tag, String msg) {
        if (enabled) {
            Log.i(tag, msg == null ? "" : msg);
        }

    }

    public static void v(String tag, String msg) {
        if (enabled) {
            Log.v(tag, msg == null ? "" : msg);
        }

    }

    public static void w(String tag, String msg) {
        if (enabled) {
            Log.w(tag, msg == null ? "" : msg);
        }

    }
}
