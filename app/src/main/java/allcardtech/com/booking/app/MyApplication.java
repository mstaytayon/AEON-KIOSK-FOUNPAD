package allcardtech.com.booking.app;

import android.app.Application;
import android.graphics.Typeface;

import allcardtech.com.booking.app.utils.FontManager;

/**
 * Created by qrfausto on 2/23/2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontManager.Font_Bebas = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/bebas.otf");
        FontManager.Font_Roboto = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        FontManager.Font_Roboto_Bold = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Bold.ttf");
        FontManager.Font_Roboto_Light = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Light.ttf");
        FontManager.Font_Roboto_Medium = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Medium.ttf");
    }
}
