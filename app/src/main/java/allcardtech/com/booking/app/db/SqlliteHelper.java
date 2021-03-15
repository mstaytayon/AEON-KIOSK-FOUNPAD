package allcardtech.com.booking.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;


public class SqlliteHelper {

    private final Context context;
    public static SQLiteDatabase db;
    private static String DBNAME = "booking.db";
    private static final int DBVERSION = 16;

    public SqlliteHelper(Context context) {
        this.context = context;
        if (db == null) {
            OpenHelper helper = new OpenHelper(context);
            db = helper.getWritableDatabase();
        } else {
            if (!db.isOpen())
                db.isOpen();
        }
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        void createTables(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SettingsDao.CREATE);
            sqLiteDatabase.execSQL(ReferenceNumberDao.CREATE);
            sqLiteDatabase.execSQL(BookingInformationDao.CREATE);
            sqLiteDatabase.execSQL(CustomerNameDao.CREATE);
            sqLiteDatabase.execSQL(SocksSizesDao.CREATE);
            sqLiteDatabase.execSQL(BranchRateDao.CREATE);
            sqLiteDatabase.execSQL(CustomerListDao.CREATE);
            sqLiteDatabase.execSQL(TempDataDao.CREATE);

        }

        public OpenHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createTables(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SettingsDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReferenceNumberDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BookingInformationDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CustomerNameDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SocksSizesDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BranchRateDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CustomerListDao.TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TempDataDao.TABLE);
            createTables(sqLiteDatabase);
        }
    }
}
