package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.app.models.CustomerModel;

public class CustomerListDao extends SqlliteHelper  {
    public static String TABLE = "tblCustomerList";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "customerID int, " +
            "cardNumber text, " +
            "firstName text, " +
            "middleName text, " +
            "lastName text, " +
            "mobileNumber text, " +
            "email text)";

    public CustomerListDao(Context context) {
        super(context);
    }

    public void save(CustomerModel model) {
        try {
            ContentValues c = new ContentValues();
            c.put("customerID", model.getCustomerID());
            c.put("cardNumber", model.getCardNumber());
            c.put("firstName", model.getFirstName());
            c.put("middleName", model.getMiddleName());
            c.put("lastName", model.getLastName());
            c.put("mobileNumber", model.getMobileNumber());
            c.put("email", model.getEmailAddress());

            db.insert(TABLE, null, c);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

    public int getLastCustomerID() {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT MAX(customerID) from " + TABLE + "", null);
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

    public int getCustomerID(int customerID) {
        Cursor c = null;
        try {
            c = db.rawQuery("select customerID from " + TABLE + " where customerID = '" + customerID + "'", null);
            if (c.moveToFirst()) {
                int i = 0;
                return c.getInt(1);
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

    public int getCustomerCount() {
        Cursor c = null;
        try {
            c = db.rawQuery("select COUNT(customerID) from " + TABLE + "", null);
            if (c.moveToFirst()) {
                int i = 0;
                return c.getInt(0);
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

    public CustomerModel getCustomerPerCardNumber(String cardNumber){
        Cursor c = null;
        try {
            CustomerModel model = new CustomerModel();
            c = db.rawQuery("select * from " + TABLE + " where cardNumber = '" + cardNumber + "'", null);
            if (c.getCount() != 0){
                c.moveToFirst();
                model.setCustomerID(c.getInt(1));
                model.setCardNumber(c.getString(2));
                model.setFirstName(c.getString(3));
                model.setMiddleName(c.getString(4));
                model.setLastName(c.getString(5));
                model.setMobileNumber(c.getString(6).replace("-",""));
                model.setEmailAddress(c.getString(7));
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


    public void deleteAll() {

        try {
            db.delete(TABLE, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }

}
