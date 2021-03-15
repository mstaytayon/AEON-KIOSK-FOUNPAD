package allcardtech.com.booking.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.app.models.BookingInformationModel;
import allcardtech.com.booking.app.models.CustomerModel;
import allcardtech.com.booking.app.models.SocksModel;


public class BookingInformationDao extends SqlliteHelper {

    public static String TABLE = "tblBookedInformation";
    public static String CREATE = "CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "referenceNumberID int, " +
            "referenceNumber text, " +
            "customerID int, " +
            "cardNumber text, " +
            "name text, " +
            "contactNumber text, " +
            "emailAddress text, " +
            "playTimeHours text, " +
            "childrenCount int, " +
            "adultCount integer, " +
            "additionalCount integer, " +
            "playTimeRate text, " +
            "AdditionalAdultAmount text, " +
            "socksAmount text, " +
            "totalAmount text, " +
            "transactionDate text)";

    public BookingInformationDao(Context context) {
        super(context);
    }

    public boolean save(BookingInformationModel model) {
        try {
            ContentValues c = new ContentValues();
            c.put("referenceNumberID", model.getReferenceID());
            c.put("referenceNumber", model.getReferenceNumber());
            c.put("customerID", model.getCustomerID());
            c.put("cardNumber", model.getCardNumber());
            c.put("name", model.getCustomerName());
            c.put("contactNumber", model.getContactNumber());
            c.put("emailAddress", model.getEmailAddress());
            c.put("playTimeHours", model.getPlayTimeHours());
            c.put("childrenCount", model.getChildrenCount());
            c.put("adultCount", model.getAdultCount());
            c.put("additionalCount", model.getAdditionalCount());
            c.put("playTimeRate", model.getPlayTimeRate());
            c.put("AdditionalAdultAmount", model.getAdditionalAdultAmount());
            c.put("socksAmount", model.getSocksAmount());
            c.put("totalAmount", model.getTotalAmount());
            c.put("transactionDate", model.getTransactionDateTime());
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

    public int getTransactionCount() {
        Cursor c = null;
        try {
            c = db.rawQuery("select COUNT(referenceNumberID) from " + TABLE + "", null);
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


    public List<BookingInformationModel> getOfflineBookedInformationList() {
        Cursor c = null;
        try {
            List<BookingInformationModel> list = new ArrayList<>();
            c = db.rawQuery("select " +
                    "id," +
                    "referenceNumberID," +
                    "referenceNumber," +
                    "customerID," +
                    "cardNumber," +
                    "childrenCount," +
                    "adultCount," +
                    "additionalCount," +
                    "additionalAdultAmount," +
                    "playtimeHours," +
                    "playtimeRate," +
                    "socksAmount," +
                    "totalAmount," +
                    "contactNumber," +
                    "emailAddress," +
                    "transactionDate from " + TABLE + "", null);
            while (c.moveToNext()) {
                int i = 0;
                BookingInformationModel model = new BookingInformationModel();
                model.setid(c.getInt(i++));
                model.setReferenceID(c.getInt(i++));
                model.setReferenceNumber(c.getString(i++));
                model.setCustomerID(c.getInt(i++));
                model.setCardNumber(c.getString(i++));
                model.setChildrenCount((c.getInt(i++)));
                model.setAdultCount((c.getInt(i++)));
                model.setAdditionalCount((c.getInt(i++)));
                model.setAdditionalAdultAmount((c.getDouble(i++)));
                model.setPlayTimeHours((c.getString(i++)));
                model.setPlayTimeRate((c.getDouble(i++)));
                model.setSocksAmount((c.getDouble(i++)));
                model.setTotalAmount((c.getDouble(i++)));
                model.setContactNumber((c.getString(i++)));
                model.setEmailAddress((c.getString(i++)));
                model.setTransactionDateTime((c.getString(i++)));

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

    public List<CustomerModel> getOfflineBookedCustomerName() {
        Cursor c = null;
        try {
            List<CustomerModel> list = new ArrayList<>();
            c = db.rawQuery("select " +
                    "id," +
                    "referenceNumberID," +
                    "referenceNumber," +
                    "name," +
                    "transactionDate," +
                    "type from  tblBookedCustomerName  ", null);
            while (c.moveToNext()) {
                int i = 0;
                CustomerModel model = new CustomerModel();
                model.setid(c.getInt(i++));
                model.setReferenceID(c.getInt(i++));
                model.setReferenceNumber(c.getString(i++));
                model.setCustomerName(c.getString(i++));
                model.setTransactionDateTime(c.getString(i++));
                model.setType((c.getInt(i++)));

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

    public List<SocksModel> getOfflineBookedSocks() {
        Cursor c = null;
        try {
            List<SocksModel> list = new ArrayList<>();
            c = db.rawQuery("select " +
                    "id," +
                    "referenceNumberID," +
                    "referenceNumber," +
                    "description," +
                    "count," +
                    "rate," +
                    "transactionDate from  tblBookedSockSizes  ", null);
            while (c.moveToNext()) {
                int i = 0;

                SocksModel model = new SocksModel();
                model.setid(c.getInt(i++));
                model.setReferenceID(c.getInt(i++));
                model.setReferenceNumber(c.getString(i++));
                model.setDescription(c.getString(i++));
                model.setCount(c.getInt(i++));
                model.setRate(c.getDouble(i++));
                model.setTransactionDateTime(c.getString(i++));

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

    public void delete(String tblName,int id) {

        try {
            db.execSQL("DELETE from " + tblName + " where id = '" + id + "'");
//            db.delete(tblName  where id = '" + id + "'", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }



}
