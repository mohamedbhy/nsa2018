package org.nasa.spaceapps.nasaspaceapps2018;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataFactory {
    private static DataFactory INSTANCE;
    public static Context context;
    private List<ListItem> upcoming = new ArrayList<>();
    private List<ListItem> previous = new ArrayList<>();
    private List<ListItem> all = new ArrayList<>();
    private List<ListItem> saved = new ArrayList<>();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final DataFactory update(){
        this.upcoming.clear();
        this.previous.clear();
        this.all.clear();
        this.saved.clear();
        try{
            SQLiteOpenHelper helper = new DBHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"launch_date>date(\"now\")",null,null,null,null);
            while (cursor.moveToNext()){
                ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                        format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                        (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                );
                this.upcoming.add(item);
            }
            cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"launch_date<date(\"now\")",null,null,null,null);
            while (cursor.moveToNext()){
                ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                        format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                        (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                );
                this.previous.add(item);
            }
            cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,null,null,null,null,null);
            while (cursor.moveToNext()){
                ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                        format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                        (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                );
                this.all.add(item);
            }
            cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"saved = 1",null,null,null,null);
            while (cursor.moveToNext()){
                ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                        format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                        (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                );
                this.saved.add(item);
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }
    private DataFactory() throws ParseException {
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"launch_date>date(\"now\")",null,null,null,null);
        while (cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                    format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                    (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                    );
            this.upcoming.add(item);
        }
        cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"launch_date<date(\"now\")",null,null,null,null);
        while (cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                    format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                    (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
            );
            this.previous.add(item);
        }
        cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,null,null,null,null,null);
        while (cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                    format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                    (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
            );
            this.all.add(item);
        }
        cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,"saved = 1",null,null,null,null);
        while (cursor.moveToNext()){
            ListItem item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                    cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                    format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                    (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
            );
            this.saved.add(item);
        }
        db.close();
    }
    public DetailsItem getItemDetails(int id) throws ParseException {
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.DETAILS_COLUMNS,"flight_number = "+id,null,null,null,null);
        cursor.moveToFirst();
        DetailsItem item = new DetailsItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_CORES_NAMES)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_PAYLOADS_IDS)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_DETAILS)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MORE_DETAILS)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_YOUTUBE_ID)),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                this.format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_STATIC_FIRE_DATE))!= null ? this.format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_STATIC_FIRE_DATE))) : null,
                (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SUCCESS))!=0),
                ((cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED)))!=0)
        );
        db.close();
        return item;
    }
    public static synchronized DataFactory getInstance() throws ParseException {
        if(INSTANCE== null) INSTANCE = new DataFactory();
        return INSTANCE;
    }
    public List<ListItem> getUpcoming() {
        return upcoming;
    }
    public List<ListItem> getPrevious() {
        return previous;
    }
    public List<ListItem> getSearch(String search) {
        List<ListItem> items = new ArrayList<>();
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(SQLItems.TABLE_LAUNCHES,SQLItems.LIST_COLUMNS,SQLItems.COLUMN_CORES_NAMES+" lIKE '%"+search+"%' OR "+SQLItems.COLUMN_ROCKET_NAME+" lIKE '%"+search+"%' OR "+SQLItems.COLUMN_LAUNCH_SITE+" lIKE '%"+search+"%' OR "+SQLItems.COLUMN_MISSION_NAME+" lIKE '%"+search+"%' OR "+SQLItems.COLUMN_PAYLOADS_IDS,null,null,null,null);
        while (cursor.moveToNext()){
            ListItem item = null;
            try{
                item = new ListItem(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_MISSION_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_SITE)),
                        cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_ROCKET_NAME)),
                        format.parse(cursor.getString(cursor.getColumnIndex(SQLItems.COLUMN_LAUNCH_DATE))),
                        (cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0)
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            items.add(item);
        }
        return items;
    }
    public List<ListItem> getAll() {
        return all;
    }
    public List<ListItem> getSaved() {
        return saved;
    }
}
