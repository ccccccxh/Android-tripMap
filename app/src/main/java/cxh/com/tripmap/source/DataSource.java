package cxh.com.tripmap.source;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cxh on 2018/6/10.
 */

public class DataSource extends SQLiteOpenHelper{
    private final static String CREATE_TABLE_SQL =
            "CREATE TABLE 'trip_map' (" +
                    "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "'site' varchar(255), " +
                    "'time' date, " +
                    "'longitude' double, "+
                    "'latitude' double "+
                    ")";

    public DataSource(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataSource(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
