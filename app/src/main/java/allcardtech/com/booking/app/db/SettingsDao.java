package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SettingsDao extends SqlliteHelper {

    public static String TABLE = "tblsettings";
    public static String CREATE = "CREATE TABLE " + TABLE + " (param TEXT, val TEXT)";

    public
    SettingsDao(Context context) {
        super(context);
    }

    public void save(String param, String val) {
        try {

            ContentValues c = new ContentValues();
            c.put("param", param);
            c.put("val", val);

            if (get(param) != null) {
                db.update(TABLE, c, "param='" + param + "'", null);
            } else {
                db.insert(TABLE, null, c);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    public String get(String param) {
        Cursor c = null;
        try {
            c = db.rawQuery("select val from " + TABLE + " where param = '" + param + "'", null);
            if (c.moveToFirst()) {
                return c.getString(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    public void deleteAll() {

        try {
            db.delete(TABLE, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    public String createUrl(String path) throws IllegalArgumentException {
        String server = get("server");
        if (server != null) {
            return "http://" + get("server") + path;
        } else {
            throw new IllegalArgumentException("No server set on application settings");
        }
    }
}
