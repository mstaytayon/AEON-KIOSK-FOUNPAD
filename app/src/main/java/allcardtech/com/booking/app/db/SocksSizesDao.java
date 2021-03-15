package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SocksSizesDao extends SqlliteHelper  {
    public static String TABLE = "tblBookedSockSizes";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "referenceNumberID int, " +
            "referenceNumber text, " +
            "description text, " +
            "count int, " +
            "rate text, " +
            "transactionDate text)";

    public SocksSizesDao(Context context) {
        super(context);
    }

    public boolean save(int referenceNumberID,String referenceNumber, String Name,int Count, String Rate, String transDate) {
        try {
            ContentValues c = new ContentValues();
            c.put("referenceNumberID", referenceNumberID);
            c.put("referenceNumber", referenceNumber);
            c.put("description", Name);
            c.put("count", Count);
            c.put("rate", Rate.replace(",",""));
            c.put("transactionDate", transDate);
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
