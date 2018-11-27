package org.nasa.spaceapps.nasaspaceapps2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    public static final String NAME="NSA.db";
    public static final int VERSION=1;
    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLItems.SQL_CREATE_LAUNCHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLItems.SQL_DELETE_LAUNCHES);
        db.execSQL(SQLItems.SQL_CREATE_LAUNCHES);
    }
}
