package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.joda.time.DateTime;

public class CustomerNameDao extends SqlliteHelper  {
    public static String TABLE = "tblBookedCustomerName";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "referenceNumberID int, " +
            "referenceNumber text, " +
            "name text, " +
            "transactionDate text, " +
            "type int)";

    public CustomerNameDao(Context context) {
        super(context);
    }

    public boolean save(int referenceNumberID,String referenceNumber, String Name, int type, String transDate) {
        try {

            ContentValues c = new ContentValues();
            c.put("referenceNumberID", referenceNumberID);
            c.put("referenceNumber", referenceNumber);
            c.put("name", Name);
            c.put("transactionDate", transDate);
            c.put("type", type);
            db.insert(TABLE, null, c);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {

        }
    }


    public String get(String param) {
        Cursor c = null;
        try {
            c = db.rawQuery("select val from " + TABLE + " where param = '" + param + "'", null);
            if (c.moveToFirst()) {
                int i = 0;
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

}
