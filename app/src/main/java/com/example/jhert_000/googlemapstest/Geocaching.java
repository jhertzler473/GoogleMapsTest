package com.example.jhert_000.googlemapstest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhert_000 on 4/17/2016.
 */
public class Geocaching {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "cache_name";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LON = "longitude";

    private static final String DATABASE_NAME = "Cachedb";
    private static final String DATABASE_TABLE = "cacheTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;




    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(
                    "CREATE TABLE " + DATABASE_TABLE + " (" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_NAME + " TEXT NOT NULL, " +
                            KEY_LAT + " TEXT NOT NULL, " +
                            KEY_LON + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public Geocaching(Context c){
        ourContext = c;
    }

    public Geocaching open() throws SQLException{
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String name, String latitude, String longitude) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_LAT, latitude);
        cv.put(KEY_LON, longitude);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public String[] getData() {
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_LAT, KEY_LON};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        List<String> caches = new ArrayList<String>();

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iLat = c.getColumnIndex(KEY_LAT);
        int iLon = c.getColumnIndex(KEY_LON);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            caches.add(c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iLat) + " " + c.getString(iLon));
        }

        String[] result = new String[caches.size()];

        caches.toArray(result);

        return result;
    }

    public void deleteEntry(int selectedCacheId){
        ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + selectedCacheId, null);
    }
}
