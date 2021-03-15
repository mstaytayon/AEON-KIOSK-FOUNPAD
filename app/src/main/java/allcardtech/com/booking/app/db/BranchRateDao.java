package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.app.models.BranchRateModel;

public class BranchRateDao extends SqlliteHelper {

    public static String TABLE = "tblBranchRates";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "pid int, " +
            "productID text, " +
            "description text, " +
            "minutes text, " +
            "rate text)";

    public BranchRateDao(Context context) {
        super(context);
    }

    public boolean save(int id,String productID,String description,String minutes,String rate) {
        try {
            ContentValues c = new ContentValues();
            c.put("pid", id);
            c.put("productID", productID);
            c.put("description", description);
            c.put("minutes", minutes);
            c.put("rate", rate.replace(",",""));

            db.insert(TABLE, null, c);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {

        }
    }

    public BranchRateModel get(String param) {
        Cursor c = null;
        try {
            BranchRateModel model = new BranchRateModel();
            c = db.rawQuery("select id,productID,rate,minutes from " + TABLE + " where description = '" + param + "'", null);
            if (c.getCount() != 0){
                c.moveToFirst();
                model.setID(c.getInt(0));
                model.setProductID(c.getString(1));
                model.setRate(c.getDouble(2));
                model.setMinutes(c.getString(3));
            }
            return model;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    public String getProductID(String desc) {
        Cursor c = null;
        try {
            c = db.rawQuery("select productID from " + TABLE + " where description = '" + desc + "'", null);
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


    public List<BranchRateModel> getBranchRatesList() {
        Cursor c = null;
        try {
            List<BranchRateModel> list = new ArrayList<>();
            c = db.rawQuery("select " +
                    "pid," +
                    "productID," +
                    "description," +
                    "rate from " + TABLE + "", null);
            while (c.moveToNext()) {
                int i = 0;
                BranchRateModel model = new BranchRateModel();
                model.setID(c.getInt(i++));
                model.setProductID(c.getString(i++));
                model.setDescription(c.getString(i++));
                model.setRate(c.getDouble(i++));

                list.add(model);
            }
            return list;
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
