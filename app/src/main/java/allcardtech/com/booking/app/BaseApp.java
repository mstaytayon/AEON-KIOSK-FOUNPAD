package allcardtech.com.booking.app;

import android.app.Application;

import allcardtech.com.booking.app.utils.AidlUtil;

public class BaseApp extends Application {
    private boolean isAidl;

    public boolean isAidl() {
        return isAidl;
    }


    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);
    }
}
