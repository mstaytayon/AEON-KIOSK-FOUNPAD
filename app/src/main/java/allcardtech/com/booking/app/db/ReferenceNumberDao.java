package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.DateFormat;
import java.util.Date;

public class ReferenceNumberDao extends SqlliteHelper  {
    public static String TABLE = "tblBoookedReferenceNumber";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "referenceNumber text)";

    public ReferenceNumberDao(Context context) {
        super(context);
    }

    public boolean save(String referenceNumber) {
        try {
            ContentValues c = new ContentValues();
            c.put("referenceNumber", referenceNumber);

            db.insert(TABLE, null, c);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {

        }
    }

    public int  getLastInsertID() {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT MAX(id) from " + TABLE + "", null);
            if (c.moveToFirst()) {
                return Integer.parseInt(c.getString(0));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return 0;
    }

    public void deleteAll() {

        try {
            db.delete(TABLE, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

}
