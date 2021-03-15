package allcardtech.com.booking.app.utils;

import android.os.Build;

import java.util.Locale;

public class SystemUtil {
    public static final String SYSTEM_VENDOR = "FounPad";

    public SystemUtil() {
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getDeviceBrand() {
        return "FounPad";
    }
}
