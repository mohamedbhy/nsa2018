package org.nasa.spaceapps.nasaspaceapps2018;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DataRetriver extends AsyncTask<Void, Integer, HashMap<String,Boolean>> {
    public static final String ALL_LAUNCHES="https://api.spacexdata.com/v3/launches";
    private Context context;
    private JSONArray data;
    public DataRetriver(Context context) {
        this.context = context;
    }
    protected final boolean isDBInit(){
        SQLiteOpenHelper helper = new DBHelper(this.context);
        SQLiteDatabase database = helper.getWritableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database, SQLItems.TABLE_LAUNCHES);
        return NoOfRows == 0 ? false : true;
    }
    protected final boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    protected static boolean isSpaceXAvailable(){
        try(final Socket socket = new Socket()){
            final InetAddress inetAddress= InetAddress.getByName("api.spacexdata.com");
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress,80);
            socket.connect(inetSocketAddress,2000);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    protected final void retriveData() throws IOException, JSONException {
        URLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        URL url = null;
        url = new URL(ALL_LAUNCHES);
        urlConnection = url.openConnection();
        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        this.data = new JSONArray(stringBuffer.toString());
    }
    protected final void resetDB(){
        SQLiteOpenHelper helper = new DBHelper(this.context);
        SQLiteDatabase database = helper.getReadableDatabase();
        database.execSQL(SQLItems.SQL_DELETE_LAUNCHES);
        database.execSQL(SQLItems.SQL_CREATE_LAUNCHES);
        database.close();
    }
    protected final void updateDBData() throws JSONException, ParseException, IOException {
        HashMap<Integer,Boolean> saves = new HashMap<>();
        SQLiteOpenHelper helper = new DBHelper(this.context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SQLItems.TABLE_LAUNCHES,SQLItems.SAVED,null,null,null,null,null);
        while (cursor.moveToNext()){
            saves.put(cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_ID)),cursor.getInt(cursor.getColumnIndex(SQLItems.COLUMN_SAVED))!=0);
        }
        this.retriveData();
        this.resetDB();
        this.insertDataToDB(saves);
    }
    protected final void insertDataToDB(HashMap<Integer,Boolean> saves) throws JSONException, ParseException {
        SQLiteOpenHelper helper = new DBHelper(this.context);
        SQLiteDatabase database = helper.getWritableDatabase();
        DateFormat launchDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateFormat staticFireDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbDateFormat.setTimeZone(TimeZone.getDefault());
        staticFireDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        launchDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String launchDateStr,staticFireDateStr;
        Date launchDate,staticFireDate=new Date();
        for(int i=0;i<this.data.length();i++){
            String coresNames="",payloadsIds="";
            JSONObject item = this.data.getJSONObject(i);
            launchDateStr = item.getString("launch_date_utc").split("\\.")[0];
            staticFireDateStr = item.getString("static_fire_date_utc").split("T")[0];
            launchDate = launchDateFormat.parse(launchDateStr);
            if (staticFireDateStr!="null")staticFireDate = staticFireDateFormat.parse(staticFireDateStr);
            ContentValues values = new ContentValues();
            for(int j=0;j<item.getJSONObject("rocket").getJSONObject("second_stage").getJSONArray("payloads").length();j++){
                String elem = item.getJSONObject("rocket").getJSONObject("second_stage").getJSONArray("payloads").getJSONObject(j).getString("payload_id");
                if(elem!="null"){
                    if(j==0) payloadsIds=payloadsIds.concat(elem);
                    else payloadsIds=payloadsIds.concat(","+elem);
                }
            }
            for(int j=0;j<item.getJSONObject("rocket").getJSONObject("first_stage").getJSONArray("cores").length();j++){
                String elem = item.getJSONObject("rocket").getJSONObject("first_stage").getJSONArray("cores").getJSONObject(j).getString("core_serial");
                if(elem!="null"){
                    if(j==0) coresNames=coresNames.concat(elem);
                    else coresNames=coresNames.concat(","+elem);
                }
            }
            values.put(SQLItems.COLUMN_ID,item.getInt("flight_number"));
            values.put(SQLItems.COLUMN_MISSION_NAME,item.getString("mission_name"));
            values.put(SQLItems.COLUMN_LAUNCH_DATE,dbDateFormat.format(launchDate));
            values.put(SQLItems.COLUMN_ROCKET_NAME,item.getJSONObject("rocket").getString("rocket_name"));
            values.put(SQLItems.COLUMN_LAUNCH_SITE,item.getJSONObject("launch_site").getString("site_name_long"));
            if(coresNames!="")values.put(SQLItems.COLUMN_CORES_NAMES,coresNames);
            if(payloadsIds!="")values.put(SQLItems.COLUMN_PAYLOADS_IDS,payloadsIds);
            if(item.getString("static_fire_date_utc")!="null")values.put(SQLItems.COLUMN_STATIC_FIRE_DATE,dbDateFormat.format(staticFireDate));
            if(item.getString("launch_success")!="null")values.put(SQLItems.COLUMN_LAUNCH_SUCCESS,item.getString("launch_success"));
            if(item.getString("details")!="null")values.put(SQLItems.COLUMN_DETAILS,item.getString("details"));
            if(item.getJSONObject("links").getString("article_link")!="null")values.put(SQLItems.COLUMN_MORE_DETAILS,item.getJSONObject("links").getString("article_link"));
            if(item.getJSONObject("links").getString("mission_patch_small")!="null")values.put(SQLItems.COLUMN_IMAGE,item.getJSONObject("links").getString("mission_patch_small"));
            if(item.getJSONObject("links").getString("youtube_id")!="null")values.put(SQLItems.COLUMN_YOUTUBE_ID,item.getJSONObject("links").getString("youtube_id"));
            if (saves == null || item.getInt("flight_number") == 0) {
                values.put(SQLItems.COLUMN_SAVED, false);
            } else {
                values.put(SQLItems.COLUMN_SAVED, saves.get(item.getInt("flight_number")));
            }
            database.insert(SQLItems.TABLE_LAUNCHES,null,values);
        }
        database.close();
    }
    @Override
    protected HashMap<String, Boolean> doInBackground(Void... voids){
        Map<String,Boolean> res = new HashMap<>();
        if(isNetworkAvailable() && !isDBInit() && isSpaceXAvailable()){
            try {
                this.retriveData();
                this.insertDataToDB(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.put("NetworkErr",!isNetworkAvailable()|| !isSpaceXAvailable());
        res.put("DBIErr",!isDBInit());
        return (HashMap<String, Boolean>) res;
    }
}
