package allcardtech.com.booking.app.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class USBDataBase {
    public static final String CONFIG = "USBDataBase";
    public static final String SVAE_USB_ESC_TSC = "save_usb_esc_tsc";

    public USBDataBase() {
    }

    public static void setUSB_ESC_TSC(Context context, String content) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USBDataBase", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("save_usb_esc_tsc", content);
        editor.commit();
    }

    public static UsbObtainBean getUSB_ESC_TSC(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USBDataBase", 0);
        String string = sharedPreferences.getString("save_usb_esc_tsc", "{\"escList\":[{\"pid\":1803,\"vid\":1155},{\"pid\":20497,\"vid\":1046},{\"pid\":8965,\"vid\":1659},{\"pid\":1536,\"vid\":26728},{\"pid\":30084,\"vid\":6790},{\"pid\":22304,\"vid\":1155},{\"pid\":8211,\"vid\":1305}],\"tscList\":[{\"pid\":23,\"vid\":1137},{\"pid\":1280,\"vid\":26728},{\"pid\":85,\"vid\":1137},{\"pid\":22339,\"vid\":1155}]}");
        LogUtils.d("jssjjsdsscc", string);
        UsbObtainBean bean = (UsbObtainBean)GsonUtils.getObjFromJsonString(string, UsbObtainBean.class);
        return bean;
    }
}
