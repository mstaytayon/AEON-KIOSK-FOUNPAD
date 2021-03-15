package allcardtech.com.booking.app.utils;

import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

public class FontManager {

    public static Typeface Font_Bebas = null;
    public static Typeface Font_Roboto = null;
    public static Typeface Font_Roboto_Bold = null;
    public static Typeface Font_Roboto_Light = null;
    public static Typeface Font_Roboto_Medium = null;

    public static void setFontSyle(EditText txt, Typeface tf) {
        txt.setTypeface(tf);
    }

    public static void setFontSyle(TextView txt, Typeface tf) {
        txt.setTypeface(tf);
    }


}